package com.tekal.elevatortechtest.service.manager;

import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.exception.ServiceNotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
@Slf4j
public class ElevatorServiceManager {

    private final Map<String, ElevatorService> elevatorServices;
    private ElevatorService activeElevatorService;

    @Autowired
    public ElevatorServiceManager(ApplicationContext context) {
        this.elevatorServices = context.getBeansOfType(ElevatorService.class);
        this.activeElevatorService = elevatorServices.values().stream().findFirst().orElse(null);
    }

    public void setActiveElevatorService(String serviceName) {
        ElevatorService selectedService = elevatorServices.get(serviceName);
        if (selectedService != null) {
            activeElevatorService = selectedService;
            log.info("Active elevator service set to: " + serviceName);
        } else {
            throw new ServiceNotFoundException("Service not found: " + serviceName);
        }
    }
}
