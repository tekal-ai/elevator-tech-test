package com.tekal.elevatortechtest.service.impl;

import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Queue;
import java.util.Set;

import static com.tekal.elevatortechtest.util.ElevatorUtil.createTestElevator;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class FCFSElevatorServiceTest {
    @Mock
    private Set<Elevator> elevators;

    @Mock
    private Queue<ElevatorCall> elevatorCalls;

    @InjectMocks
    private FCFSElevatorService elevatorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void test_ProcessElevatorCall() {
        ElevatorCall elevatorCall = new ElevatorCall(1, 5);

        elevatorService.processElevatorCall(elevatorCall);

        verify(elevatorCalls).offer(elevatorCall);
    }

    @Test
    void test_ServeElevatorCalls_when_queue_is_empty() {
        Elevator elevator = mock(Elevator.class);
        when(elevators.iterator()).thenReturn(Set.of(elevator).iterator());
        when(elevatorCalls.isEmpty()).thenReturn(true);

        elevatorService.serveElevatorCalls();

        verify(elevatorCalls, never()).poll();
    }

    @Test
    void test_ServeElevatorCalls_when_queue_not_empty_and_elevator_not_moving() {
        Elevator elevator = mock(Elevator.class);
        ElevatorCall elevatorCall = new ElevatorCall(1, 5);

        when(elevators.iterator()).thenReturn(Set.of(elevator).iterator());
        when(elevatorCalls.isEmpty()).thenReturn(false);
        when(elevator.isMoving()).thenReturn(false);
        when(elevatorCalls.poll()).thenReturn(elevatorCall);

        elevatorService.serveElevatorCalls();

        verify(elevatorCalls, times(1)).poll();
    }

    @Test
    void test_ServeElevatorCalls_when_elevator_is_moving() {
        Elevator elevator = mock(Elevator.class);
        ElevatorCall elevatorCall = new ElevatorCall(1, 5);

        when(elevators.iterator()).thenReturn(Set.of(elevator).iterator());
        when(elevatorCalls.isEmpty()).thenReturn(false);
        when(elevator.isMoving()).thenReturn(true);
        when(elevatorCalls.poll()).thenReturn(elevatorCall);

        elevatorService.serveElevatorCalls();

        verify(elevatorCalls, never()).poll();
    }

    @Test
    void test_ServeElevatorCalls_with_multiple_elevators_and_calls() {
        Elevator elevator1 = mock(Elevator.class);
        Elevator elevator2 = mock(Elevator.class);
        ElevatorCall elevatorCall1 = new ElevatorCall(1, 5);
        ElevatorCall elevatorCall2 = new ElevatorCall(3, 7);

        when(elevators.iterator()).thenReturn(Set.of(elevator1, elevator2).iterator());
        when(elevatorCalls.isEmpty()).thenReturn(false);
        when(elevator1.isMoving()).thenReturn(false);
        when(elevator2.isMoving()).thenReturn(false);
        when(elevatorCalls.poll()).thenReturn(elevatorCall1, elevatorCall2);

        elevatorService.serveElevatorCalls();

        verify(elevatorCalls, times(2)).poll();
    }

    @Test
    void test_ServeElevatorCalls_with_one_elevator_moving_and_one_not_moving() {
        Elevator elevator1 = mock(Elevator.class);
        Elevator elevator2 = mock(Elevator.class);
        ElevatorCall elevatorCall1 = new ElevatorCall(1, 5);
        ElevatorCall elevatorCall2 = new ElevatorCall(3, 7);

        when(elevators.iterator()).thenReturn(Set.of(elevator1, elevator2).iterator());
        when(elevatorCalls.isEmpty()).thenReturn(false);
        when(elevator1.isMoving()).thenReturn(true);
        when(elevator2.isMoving()).thenReturn(false);
        when(elevatorCalls.poll()).thenReturn(elevatorCall1, elevatorCall2);

        elevatorService.serveElevatorCalls();

        verify(elevatorCalls, times(1)).poll();
    }

//    @Test
//    void test_ServeElevatorCalls_with_one_elevator_and_two_requests() {
//        Elevator elevator = mock(Elevator.class);
//        ElevatorCall elevatorCall1 = new ElevatorCall(1, 5);
//        ElevatorCall elevatorCall2 = new ElevatorCall(3, 7);
//
//        when(elevators.iterator()).thenReturn(Set.of(elevator).iterator());
//        when(elevatorCalls.isEmpty()).thenReturn(false);
//        when(elevator.isMoving()).thenReturn(false);
//        when(elevatorCalls.poll()).thenReturn(elevatorCall1, elevatorCall2);
//
//        elevatorService.serveElevatorCalls();
//
//        verify(elevatorCalls, times(2)).poll();
//    }

}