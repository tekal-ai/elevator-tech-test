package com.tekal.elevatortechtest.model.factory;

import com.tekal.elevatortechtest.model.Elevator;

import java.util.HashSet;
import java.util.UUID;

public class ElevatorFactory {
    public static Elevator createElevator() {

        return Elevator.builder()
                .elevatorId(UUID.randomUUID())
                .currentFloor(1)
                .destinationFloor(null)
                .passengers(new HashSet<>())
                .build();
    }

}
