package com.tekal.elevatortechtest.service.manager;

import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.exception.ServiceNotFoundException;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ElevatorServiceManagerTest {

    @Test
    void constructor_ShouldInitializeElevatorServicesAndSetActiveService() {
        ElevatorServiceManager manager = createMockManager();

        assertNotNull(manager.getElevatorServices());
        assertNotNull(manager.getActiveElevatorService());
    }

    @Test
    void setActiveElevatorService_ShouldSetSelectedService() {
        ElevatorServiceManager manager = createMockManager();
        ElevatorService mockService = mock(ElevatorService.class);
        String serviceName = "MockService";

        manager.setActiveElevatorService(serviceName);

        assertEquals(mockService.getClass(), manager.getActiveElevatorService().getClass());
        assertThat(manager.getActiveElevatorService().getClass(), is(Matchers.equalTo(mockService.getClass())));
    }

    @Test
    void setActiveElevatorService_WithInvalidServiceName_ShouldThrowServiceNotFoundException() {
        ElevatorServiceManager manager = createMockManager();

        assertThrows(ServiceNotFoundException.class, () -> manager.setActiveElevatorService("InvalidService"));
    }

    private ElevatorServiceManager createMockManager() {
        ElevatorService mockService = mock(ElevatorService.class);

        Map<String, ElevatorService> mockServices = new HashMap<>();
        mockServices.put("MockService", mockService);

        ApplicationContext mockContext = mock(ApplicationContext.class);
        when(mockContext.getBeansOfType(ElevatorService.class)).thenReturn(mockServices);

        ElevatorServiceManager manager = new ElevatorServiceManager(mockContext);

        try {
            Field elevatorServicesField = ElevatorServiceManager.class.getDeclaredField("elevatorServices");
            elevatorServicesField.setAccessible(true);
            elevatorServicesField.set(manager, mockServices);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return manager;
    }
}