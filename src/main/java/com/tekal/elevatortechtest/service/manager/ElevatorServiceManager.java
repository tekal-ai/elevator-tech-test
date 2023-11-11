package com.tekal.elevatortechtest.service.manager;

import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.exception.ServiceNotFoundException;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Getter
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
        } else {
            throw new ServiceNotFoundException("Service not found: " + serviceName);
        }
    }
}
