package nms.device.discovery.repository;

import nms.device.discovery.model.domain.Device;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeviceRepository extends JpaRepository<Device, Long> {
}
