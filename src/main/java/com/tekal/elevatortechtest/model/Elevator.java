package com.tekal.elevatortechtest.model;

import com.tekal.elevatortechtest.model.provider.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Getter
@Setter
public class Elevator {
    private UUID elevatorId;
    private Integer currentFloor;
    private Set<Person> passengers;
    private Boolean isMoving;
    private final TimeProvider timeProvider;

    public Boolean isMoving() {
        return getIsMoving();
    }

    public void move(Integer targetFloor) {
        if (targetFloor < 1 || targetFloor > 100) {
            throw new IllegalArgumentException("Target floor must be between 1 and 100");
        }
        if (Objects.equals(targetFloor, currentFloor)) {
            return;
        }
        this.isMoving = Boolean.TRUE;
        int floorsToMove = Math.abs(targetFloor - currentFloor);
        simulateTimePassing(floorsToMove);
        currentFloor = targetFloor;
    }

    public void openDoors() {
        this.isMoving = Boolean.FALSE;
        if (currentFloor == 1) {
            simulateTimePassing(30);
        } else {
            simulateTimePassing(5);
        }
    }

    public void closeDoors() {
        simulateTimePassing(5);
    }

    public void addPassenger(Person person) {
        if(passengers.size() == 10) {
            throw new IllegalStateException("Elevator is full");
        }
        if (person.getState() != PersonState.WAITING) {
            throw new IllegalStateException("Person is not waiting");
        }
        if (Objects.equals(person.getDestinationFloor(), currentFloor)) {
            throw new IllegalStateException("Person is already on the same floor as the elevator");
        }
        simulateTimePassing(5);
        passengers.add(person);
    }

    public void removePassenger(Person person) {
        simulateTimePassing(5);
        passengers.remove(person);
    }
    
    private void simulateTimePassing(Integer seconds) {
        try {
            timeProvider.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
