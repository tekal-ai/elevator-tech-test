package com.tekal.elevatortechtest.model.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class ElevatorCall {
    private final Integer calledFromFloor;
    private final Integer destinationFloor;
}
