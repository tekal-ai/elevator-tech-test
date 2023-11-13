package com.tekal.elevatortechtest.service.impl;

import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.PersonService;
import com.tekal.elevatortechtest.service.StatisticsService;
import io.reactivex.rxjava3.core.Observable;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.*;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class CLookElevatorService extends ElevatorCallServer implements ElevatorService {

    private final Set<Elevator> elevators;
    private final Queue<ElevatorCall> elevatorCalls;

    @Autowired
    public CLookElevatorService(Set<Elevator> elevators, Queue<ElevatorCall> elevatorCalls, PersonService personService, StatisticsService statisticsService) {
        super(personService, statisticsService);
        this.elevators = elevators;
        this.elevatorCalls = elevatorCalls;
        startElevatorServiceThread();
    }

    @Override
    public void processElevatorCall(ElevatorCall elevatorCall) {
        log.info("Processing Elevator Call: " + elevatorCall.getCalledFromFloor() + " -> " + elevatorCall.getDestinationFloor());
        elevatorCalls.offer(elevatorCall);
    }

    @Override
    protected void serveElevatorCalls() {
        synchronized (elevatorCalls) {
            if (elevatorCalls.isEmpty()) {
                log.trace("No elevator calls to serve");
                return;
            }
            synchronized (elevators) {
                for (Elevator elevator : elevators.stream().filter(elevator -> !elevator.isMoving()).toList()) {
                    ElevatorCall elevatorCall = getNextElevatorCall(elevator);
                    if (elevatorCall != null) {
                        log.info("Elevator " + elevator.getElevatorId() + " is not moving, serving call");
                        serveElevatorCall(elevator, elevatorCall);
                    }
                }
            }
        }
    }

    private ElevatorCall getNextElevatorCall(Elevator elevator) {
        synchronized (elevatorCalls) {
            if (elevatorCalls.isEmpty()) {
                return null;
            }

            // Sorting the elevator calls based on the current direction
            List<ElevatorCall> sortedCalls = new ArrayList<>(elevatorCalls);
            sortedCalls.sort(Comparator.comparingInt(ElevatorCall::getCalledFromFloor));

            // Find the next elevator call based on the current direction
            for (ElevatorCall call : sortedCalls) {
                if (isCallInDirection(call, elevator.getCurrentFloor())) {
                    elevatorCalls.remove(call);
                    return call;
                }
            }

            // If no call in the current direction, go to the opposite end
            return null; // C-LOOK does not change direction immediately, just goes to the opposite end
        }
    }

    private boolean isCallInDirection(ElevatorCall call, Integer currentFloor) {
        return call.getCalledFromFloor() >= currentFloor;
    }

    @Async
    protected void startElevatorServiceThread() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .subscribe(tick -> serveElevatorCalls(),
                        throwable -> log.error("Error in elevator service thread", throwable),
                        () -> log.info("Elevator service thread stopped"));
    }
}
