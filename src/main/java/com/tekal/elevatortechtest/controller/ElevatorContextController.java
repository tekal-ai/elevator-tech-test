package com.tekal.elevatortechtest.controller;

import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.exception.ServiceNotFoundException;
import com.tekal.elevatortechtest.service.manager.ElevatorServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/v1/elevator")
public class ElevatorContextController {
    private final ElevatorServiceManager elevatorServiceManager;

    @Autowired
    public ElevatorContextController(ElevatorServiceManager elevatorServiceManager) {
        this.elevatorServiceManager = elevatorServiceManager;
    }

    @GetMapping("/services")
    public ResponseEntity<Map<String, ElevatorService>> getAllServices() {
        return ResponseEntity.ok(elevatorServiceManager.getElevatorServices());
    }

    @PostMapping("/activate")
    public ResponseEntity<String> activateService(@RequestParam String serviceName) {
        try {
            elevatorServiceManager.setActiveElevatorService(serviceName);
            return ResponseEntity.ok("Active service set to: " + serviceName);
        } catch (ServiceNotFoundException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
