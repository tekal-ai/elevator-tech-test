package com.tekal.elevatortechtest.util;

import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.provider.TimeProvider;

import java.util.HashSet;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class ElevatorUtil {
    public static Elevator createTestElevator() {
        return Elevator.builder()
                .elevatorId(UUID.randomUUID())
                .currentFloor(1)
                .destinationFloor(null)
                .passengers(new HashSet<>())
                .isMoving(false)
                .timeProvider(mock(TimeProvider.class))
                .build();
    }
}
