package com.tekal.elevatortechtest.service.impl;

import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.PersonService;
import com.tekal.elevatortechtest.service.SimulationService;
import com.tekal.elevatortechtest.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import io.reactivex.rxjava3.core.Observable;

import java.util.*;
import java.util.concurrent.*;

@Service
@Slf4j
public class FCFSElevatorService extends ElevatorCallServer implements ElevatorService {

    private final Set<Elevator> elevators;
    private final Queue<ElevatorCall> elevatorCalls;

    @Autowired
    public FCFSElevatorService(Set<Elevator> elevators, Queue<ElevatorCall> elevatorCalls, PersonService personService, StatisticsService statisticsService) {
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
        synchronized (elevatorCalls){
            if(elevatorCalls.isEmpty()) {
                log.trace("No elevator calls to serve");
                return;
            }
            synchronized (elevators) {
                for (Elevator elevator : elevators) {
                    if (!elevator.isMoving()) {
                        ElevatorCall elevatorCall = elevatorCalls.poll();
                        if (elevatorCall != null) {
                            log.info("Elevator " + elevator.getElevatorId() + " is not moving, serving call");
                            serveElevatorCall(elevator, elevatorCall);
                        }
                    }
                }
            }
        }
    }

    @Async
    protected void startElevatorServiceThread() {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .subscribe(tick -> serveElevatorCalls());
    }
}
