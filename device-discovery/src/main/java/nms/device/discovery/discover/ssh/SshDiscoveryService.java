package nms.device.discovery.discover.ssh;

import nms.device.discovery.model.raw.RawDeviceData;

public interface SshDiscoveryService {
    void enrichRawDeviceData(RawDeviceData raw);
}
