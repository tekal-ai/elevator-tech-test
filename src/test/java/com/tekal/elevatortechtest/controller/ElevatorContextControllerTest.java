package com.tekal.elevatortechtest.controller;

import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.exception.ServiceNotFoundException;
import com.tekal.elevatortechtest.service.manager.ElevatorServiceManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ElevatorContextControllerTest {
    @Mock
    private ElevatorServiceManager elevatorServiceManager;

    @InjectMocks
    private ElevatorContextController elevatorContextController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllServices_ReturnsOk() {
        Map<String, ElevatorService> mockServices = Collections.singletonMap("mockElevatorService", mock(ElevatorService.class));
        when(elevatorServiceManager.getElevatorServices()).thenReturn(mockServices);

        ResponseEntity<Map<String, ElevatorService>> response = elevatorContextController.getAllServices();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(mockServices, response.getBody());
        verify(elevatorServiceManager, times(1)).getElevatorServices();
    }

    @Test
    void activateService_WithValidServiceName_ReturnsOk() {
        String serviceName = "validService";
        doNothing().when(elevatorServiceManager).setActiveElevatorService(serviceName);

        ResponseEntity<String> response = elevatorContextController.activateService(serviceName);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Active service set to: " + serviceName, response.getBody());
        verify(elevatorServiceManager, times(1)).setActiveElevatorService(serviceName);
    }

    @Test
    void activateService_WithInvalidServiceName_ReturnsBadRequest() {
        String invalidServiceName = "invalidService";
        doThrow(new ServiceNotFoundException("Service not found: " + invalidServiceName))
                .when(elevatorServiceManager).setActiveElevatorService(invalidServiceName);

        ResponseEntity<String> response = elevatorContextController.activateService(invalidServiceName);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Service not found: " + invalidServiceName, response.getBody());
        verify(elevatorServiceManager, times(1)).setActiveElevatorService(invalidServiceName);
    }
}