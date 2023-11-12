package com.tekal.elevatortechtest.controller.Response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SimulationResult {
    private final double averageWaitingTime;
    private final double averageTravelTime;
    private final long maxWaitingTime;
    private final long minWaitingTime;
}
