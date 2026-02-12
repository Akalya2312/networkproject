package nms.device.discovery.model.raw;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RawDeviceData {
    private String deviceName;
    private String ipAddress;
    private String osDescription;
    private String osDetails;
    private String uptime;
    private String manufacturer;
    private String architecture;
}
