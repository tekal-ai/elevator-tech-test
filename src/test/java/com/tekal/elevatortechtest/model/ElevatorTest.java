package com.tekal.elevatortechtest.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.tekal.elevatortechtest.util.ElevatorUtil.createTestElevator;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

class ElevatorTest {

    @Test
    void move_ShouldUpdateCurrentFloorAndSimulateTimePassing() {
        Elevator elevator = createTestElevator();

        elevator.move(5);

        assertEquals(5, elevator.getCurrentFloor());
        assertTrue(elevator.isMoving());
    }

    @Test
    void openDoors_AtFirstFloor_ShouldSimulateTimePassingForLobby() throws InterruptedException {
        Elevator elevator = createTestElevator();

        elevator.openDoors();

        assertFalse(elevator.isMoving());
        assertEquals(1, elevator.getCurrentFloor());
        verify(elevator.getTimeProvider(), times(1)).sleep(30 * 1000L);
    }

    @Test
    void openDoors_AtNonFirstFloor_ShouldSimulateTimePassing() {
        Elevator elevator = createTestElevator();

        elevator.openDoors();

        assertFalse(elevator.isMoving());
        assertEquals(1, elevator.getCurrentFloor());
    }

    @Test
    void closeDoors_ShouldSimulateTimePassing() {
        Elevator elevator = createTestElevator();

        elevator.closeDoors();

        assertFalse(elevator.isMoving());
    }

    @Test
    void addPassenger_ShouldSimulateTimePassing() {
        Elevator elevator = createTestElevator();
        Person person = new Person(UUID.randomUUID(), 10);

        elevator.addPassenger(person);

        assertFalse(elevator.isMoving());
        assertEquals(1, elevator.getPassengers().size());
        assertEquals(person, elevator.getPassengers().iterator().next());
    }

    @Test
    void removePassenger_ShouldSimulateTimePassing() {
        Elevator elevator = createTestElevator();
        Person person = new Person(UUID.randomUUID(), 10);
        elevator.addPassenger(person);

        elevator.removePassenger(person);

        assertFalse(elevator.isMoving());
    }
}
