package nms.device.discovery.model.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "device")
public class Device {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String ipAddress;
    private String osDescription;

    @Column(length = 2000)
    private String osDetails;

    private String uptime;
    private String manufacturer;
    private String architecture;
}
