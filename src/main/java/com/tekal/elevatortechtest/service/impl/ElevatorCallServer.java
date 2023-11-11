package com.tekal.elevatortechtest.service.impl;

import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.request.ElevatorCall;

public abstract class ElevatorCallServer {

    protected abstract void serveElevatorCalls();
    protected void serveElevatorCall(Elevator elevator, ElevatorCall elevatorCall) {
        performElevatorRoutine(elevator, elevatorCall.calledFromFloor());
        performElevatorRoutine(elevator, elevatorCall.destinationFloor());
    }

    private void performElevatorRoutine(Elevator elevator, Integer destinationFloor) {
        elevator.closeDoors();
        elevator.move(destinationFloor);
        elevator.openDoors();
    }
}