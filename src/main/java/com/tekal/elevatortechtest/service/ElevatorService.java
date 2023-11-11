package com.tekal.elevatortechtest.service;

import com.tekal.elevatortechtest.model.request.ElevatorCall;
import org.springframework.scheduling.annotation.Async;

public interface ElevatorService {
    @Async("elevatorTaskExecutor")
    void processElevatorCall(ElevatorCall elevatorCall);

}
