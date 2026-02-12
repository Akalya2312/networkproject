package nms.device.discovery.parser;

import nms.device.discovery.model.raw.RawDeviceData;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class DiscoveryParser {

    /**
     * Parses raw cached discovery data (from SNMP + SSH)
     * and converts it into a RawDeviceData object
     */
    public RawDeviceData parse(Map<String, Object> cachedData) {
        RawDeviceData raw = new RawDeviceData();

        // SNMP part
        if (cachedData.containsKey("snmpDevice")) {
            Map<String, Object> snmp = (Map<String, Object>) cachedData.get("snmpDevice");
            raw.setDeviceName((String) snmp.getOrDefault("deviceName", "Unknown"));
            raw.setIpAddress((String) snmp.getOrDefault("ipAddress", "127.0.0.1"));
            raw.setOsDescription((String) snmp.getOrDefault("osDescription", "Unknown OS"));
            raw.setOsDetails((String) snmp.getOrDefault("osDetails", ""));
            raw.setUptime((String) snmp.getOrDefault("uptime", ""));
        }

        // SSH part
        if (cachedData.containsKey("sshDevice")) {
            Map<String, Object> ssh = (Map<String, Object>) cachedData.get("sshDevice");
            // If some fields were empty from SNMP, fill from SSH
            if (raw.getOsDescription() == null || raw.getOsDescription().isEmpty()) {
                raw.setOsDescription((String) ssh.getOrDefault("osDescription", "Unknown OS"));
            }
            if (raw.getOsDetails() == null || raw.getOsDetails().isEmpty()) {
                raw.setOsDetails((String) ssh.getOrDefault("osDetails", ""));
            }
            if (raw.getManufacturer() == null || raw.getManufacturer().isEmpty()) {
                raw.setManufacturer((String) ssh.getOrDefault("manufacturer", "Unknown"));
            }
            if (raw.getArchitecture() == null || raw.getArchitecture().isEmpty()) {
                raw.setArchitecture((String) ssh.getOrDefault("architecture", "Unknown"));
            }
        }

        return raw;
    }
}
