package com.tekal.elevatortechtest.model;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.tekal.elevatortechtest.util.ElevatorUtil.createTestElevator;
import static com.tekal.elevatortechtest.util.ElevatorUtil.createTestPerson;
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
        Person person = createTestPerson();

        elevator.addPassenger(person);

        assertFalse(elevator.isMoving());
        assertEquals(1, elevator.getPassengers().size());
        assertEquals(person, elevator.getPassengers().iterator().next());
    }

    @Test
    void removePassenger_ShouldSimulateTimePassing() {
        Elevator elevator = createTestElevator();
        Person person = createTestPerson();
        elevator.addPassenger(person);

        elevator.removePassenger(person);

        assertFalse(elevator.isMoving());
    }

    @Test
    void move_InvalidTargetFloor_ShouldThrowIllegalArgumentException() {
        Elevator elevator = createTestElevator();

        assertThrows(IllegalArgumentException.class, () -> elevator.move(0));
        assertThrows(IllegalArgumentException.class, () -> elevator.move(101));
    }

    @Test
    void move_SameTargetFloor_ShouldNotChangeCurrentFloor() {
        Elevator elevator = createTestElevator();

        elevator.move(5);
        elevator.move(5);

        assertEquals(5, elevator.getCurrentFloor());
        assertFalse(elevator.isMoving());
    }

    @Test
    void addPassenger_ElevatorFull_ShouldThrowIllegalStateException() {
        Elevator elevator = createTestElevator();

        // Fill the elevator with 10 passengers
        for (int i = 0; i < 10; i++) {
            Person person = createTestPerson();
            elevator.addPassenger(person);
        }

        Person extraPerson = createTestPerson();
        assertThrows(IllegalStateException.class, () -> elevator.addPassenger(extraPerson));
    }

    @Test
    void addPassenger_NonWaitingPerson_ShouldThrowIllegalStateException() {
        Elevator elevator = createTestElevator();
        Person person = createTestPerson();
        person.setState(PersonState.IN_ELEVATOR); // Set the person state to non-waiting

        assertThrows(IllegalStateException.class, () -> elevator.addPassenger(person));
    }

    @Test
    void addPassenger_PersonOnSameFloor_ShouldThrowIllegalStateException() {
        Elevator elevator = createTestElevator();
        Person person = createTestPerson();
        person.setDestinationFloor(elevator.getCurrentFloor());

        assertThrows(IllegalStateException.class, () -> elevator.addPassenger(person));
    }
}
