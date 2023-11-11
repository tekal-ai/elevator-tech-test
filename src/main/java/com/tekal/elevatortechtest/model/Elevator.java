package com.tekal.elevatortechtest.model;

import com.tekal.elevatortechtest.model.provider.TimeProvider;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

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
