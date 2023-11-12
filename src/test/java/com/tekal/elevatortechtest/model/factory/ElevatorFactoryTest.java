package com.tekal.elevatortechtest.model.factory;

import com.tekal.elevatortechtest.model.Elevator;
import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.*;

class ElevatorFactoryTest {
    @Test
    void createElevator_ReturnsValidElevator() {
        Elevator elevator = ElevatorFactory.createElevator();

        assertNotNull(elevator.getElevatorId());
        assertEquals(1, elevator.getCurrentFloor());
        assertNotNull(elevator.getPassengers());
        assertFalse(elevator.isMoving());
        assertEquals(new HashSet<>(), elevator.getPassengers());
    }

}