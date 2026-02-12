package nms.device.discovery.transformer;

import nms.device.discovery.model.domain.Device;
import nms.device.discovery.model.raw.RawDeviceData;
import org.springframework.stereotype.Component;

@Component
public class DeviceTransformer {
    public Device transform(RawDeviceData raw) {
        Device device = new Device();
        device.setName(raw.getDeviceName());
        device.setIpAddress(raw.getIpAddress());
        device.setOsDescription(raw.getOsDescription());
        device.setOsDetails(raw.getOsDetails());
        device.setUptime(raw.getUptime());
        device.setManufacturer(raw.getManufacturer());
        device.setArchitecture(raw.getArchitecture());
        return device;
    }
}
