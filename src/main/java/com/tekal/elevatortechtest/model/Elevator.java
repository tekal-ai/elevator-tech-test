package com.tekal.elevatortechtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Builder
@AllArgsConstructor
public class Elevator {
    private UUID elevatorId;
    private Integer currentFloor;
    private Integer destinationFloor;
    private Set<Person> passengers;
    private Boolean isMoving;

    private void simulateTimePassing(Integer seconds) {
        try {
            Thread.sleep(seconds * 1000L);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void move(Integer targetFloor) {
        this.isMoving = Boolean.TRUE;
        int floorsToMove = Math.abs(targetFloor - currentFloor);
        simulateTimePassing(floorsToMove);
        currentFloor = targetFloor;
    }

    public void openDoors() {
        if (currentFloor == 1) {
            this.isMoving = Boolean.FALSE;
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
    }

    public void removePassenger(Person person) {
        simulateTimePassing(5);
    }
}
