package nms.device.discovery.discover.snmp;

import nms.device.discovery.model.raw.RawDeviceData;
import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class SnmpDiscoveryServiceImpl implements SnmpDiscoveryService {

    private static final Logger logger = LoggerFactory.getLogger(SnmpDiscoveryServiceImpl.class);
    private static final String HOST = "127.0.0.1";
    private static final String COMMUNITY = "public";
    private static final int PORT = 161;

    @Override
    public void enrichRawDeviceData(RawDeviceData raw) {
        try {
            logger.info("Starting SNMP discovery for host: {}", HOST);
            TransportMapping<UdpAddress> transport = new DefaultUdpTransportMapping();
            transport.listen();
            Snmp snmp = new Snmp(transport);

            CommunityTarget<UdpAddress> target = new CommunityTarget<>();
            target.setCommunity(new OctetString(COMMUNITY));
            target.setAddress(new UdpAddress(HOST + "/" + PORT));
            target.setRetries(2);
            target.setTimeout(1500);
            target.setVersion(SnmpConstants.version2c);

            // SNMP OIDs
            OID sysName = new OID("1.3.6.1.2.1.1.5.0");
            OID sysDescr = new OID("1.3.6.1.2.1.1.1.0");
            OID sysUpTime = new OID("1.3.6.1.2.1.1.3.0");

            PDU pdu = new PDU();
            pdu.add(new VariableBinding(sysName));
            pdu.add(new VariableBinding(sysDescr));
            pdu.add(new VariableBinding(sysUpTime));
            pdu.setType(PDU.GET);

            ResponseEvent response = snmp.get(pdu, target);

            if (response.getResponse() != null) {
                var vars = response.getResponse().getVariableBindings();

                raw.setDeviceName(vars.get(0).getVariable().toString());
                raw.setOsDescription(vars.get(1).getVariable().toString());
                long ticks = vars.get(2).getVariable().toLong();
                raw.setUptime(convertTicksToBootTime(ticks));
                raw.setIpAddress(HOST);

                logger.info("SNMP data collected: {}, {}, {}", raw.getDeviceName(), raw.getOsDescription(), raw.getUptime());
            } else {
                logger.warn("No SNMP response from {}", HOST);
            }

            snmp.close();
        } catch (Exception e) {
            logger.error("Error during SNMP discovery", e);
        }
    }

    private String convertTicksToBootTime(long ticks) {
        long uptimeMillis = ticks * 10L; // 1 tick = 1/100 sec
        long bootTimeMillis = System.currentTimeMillis() - uptimeMillis;
        return new java.text.SimpleDateFormat("dd-MM-yyyy, HH:mm:ss").format(new java.util.Date(bootTimeMillis));
    }
}
