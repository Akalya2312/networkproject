package nms.device.discovery.runner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import nms.device.discovery.discover.snmp.SnmpDiscoveryService;
import nms.device.discovery.discover.ssh.SshDiscoveryService;
import nms.device.discovery.model.raw.RawDeviceData;
import nms.device.discovery.model.domain.Device;
import nms.device.discovery.repository.DeviceRepository;
import nms.device.discovery.transformer.DeviceTransformer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DiscoveryRunner implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DiscoveryRunner.class);

    private final SnmpDiscoveryService snmp;
    private final SshDiscoveryService ssh;
    private final DeviceTransformer transformer;
    private final DeviceRepository repository;

    public DiscoveryRunner(SnmpDiscoveryService snmp,
                           SshDiscoveryService ssh,
                           DeviceTransformer transformer,
                           DeviceRepository repository) {
        this.snmp = snmp;
        this.ssh = ssh;
        this.transformer = transformer;
        this.repository = repository;
    }

    @Override
    public void run(String... args) {

        // Step 1: Create empty RawDeviceData
        RawDeviceData raw = new RawDeviceData();

        // Step 2: Enrich from SNMP
        snmp.enrichRawDeviceData(raw);

        // Step 3: Enrich from SSH (same object, merge data)
        ssh.enrichRawDeviceData(raw);

        // Step 4: Cache data
        Map<String, Object> cacheMap = new HashMap<>();
        cacheMap.put("deviceName", raw.getDeviceName());
        cacheMap.put("ipAddress", raw.getIpAddress());
        cacheMap.put("osDescription", raw.getOsDescription());
        cacheMap.put("osDetails", raw.getOsDetails());
        cacheMap.put("uptime", raw.getUptime());
        cacheMap.put("manufacturer", raw.getManufacturer());
        cacheMap.put("architecture", raw.getArchitecture());



        // Step 5: Transform and persist
        Device device = transformer.transform(raw);
        logger.info("Transformed Device data: Name={}, IP={}, OS={}, Manufacturer={}, Architecture={}",
                device.getName(),
                device.getIpAddress(),
                device.getOsDescription(),
                device.getManufacturer(),
                device.getArchitecture());
        repository.save(device);
        System.out.println("Device saved to DB: " + device.getName());
    }
}
