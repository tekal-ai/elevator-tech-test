package com.tekal.elevatortechtest.service.impl;

import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.service.ElevatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import io.reactivex.rxjava3.core.Observable;

import java.util.Objects;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.*;

@Service
public class FCFSElevatorService extends ElevatorCallServer implements ElevatorService {

    private final Set<Elevator> elevators;
    private final Queue<ElevatorCall> elevatorCalls;


    @Autowired
    public FCFSElevatorService(Set<Elevator> elevators, Queue<ElevatorCall> elevatorCalls) {
        this.elevators = elevators;
        this.elevatorCalls = elevatorCalls;
        startElevatorServiceThread();
    }

    @Override
    public void processElevatorCall(ElevatorCall elevatorCall) {
        elevatorCalls.offer(elevatorCall);
    }

    @Override
    protected void serveElevatorCalls() {
        synchronized (elevatorCalls){
            if(elevatorCalls.isEmpty()) {
                return;
            }
            synchronized (elevators) {
                for (Elevator elevator : elevators) {
                    if (!elevator.isMoving()) {
                        serveElevatorCall(elevator, Objects.requireNonNull(elevatorCalls.poll()));
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
