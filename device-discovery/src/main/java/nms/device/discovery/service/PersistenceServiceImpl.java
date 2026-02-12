package nms.device.discovery.service;

import nms.device.discovery.model.domain.Device;
import nms.device.discovery.repository.DeviceRepository;
import org.springframework.stereotype.Service;

@Service
public class PersistenceServiceImpl implements PersistenceService {

    private final DeviceRepository repository;

    public PersistenceServiceImpl(DeviceRepository repository) {
        this.repository = repository;
    }

    @Override
    public void save(Device device) {
        repository.save(device);
    }
}
