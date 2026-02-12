package nms.device.discovery.service;

import nms.device.discovery.model.domain.Device;
import org.springframework.stereotype.Service;


public interface PersistenceService {
    void save(Device device);
}
