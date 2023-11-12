package com.tekal.elevatortechtest.util;

import ch.qos.logback.core.testUtil.RandomUtil;
import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.Person;
import com.tekal.elevatortechtest.model.PersonState;
import com.tekal.elevatortechtest.model.provider.TimeProvider;

import java.util.HashSet;
import java.util.Random;
import java.util.UUID;

import static org.mockito.Mockito.mock;

public class ElevatorUtil {
    public static Elevator createTestElevator() {
        return Elevator.builder()
                .elevatorId(UUID.randomUUID())
                .currentFloor(1)
                .passengers(new HashSet<>())
                .isMoving(false)
                .timeProvider(mock(TimeProvider.class))
                .build();
    }

    public static Person createTestPerson() {
        return Person.builder()
                .personId(UUID.randomUUID())
                .destinationFloor(5)
                .state(PersonState.WAITING)
                .build();
    }
}
