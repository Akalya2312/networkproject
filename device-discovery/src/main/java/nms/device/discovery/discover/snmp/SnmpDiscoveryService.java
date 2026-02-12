package nms.device.discovery.discover.snmp;

import nms.device.discovery.model.raw.RawDeviceData;

public interface SnmpDiscoveryService {
    void enrichRawDeviceData(RawDeviceData raw);
}
