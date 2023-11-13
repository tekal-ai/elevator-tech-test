package com.tekal.elevatortechtest.controller.Response;

import lombok.Builder;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;

import java.util.List;
import java.util.UUID;

@Data
@Builder
public class SimulationResult {
    private final Long averageWaitingTime;
    private final Long averageTravelTime;
    private final Long maxWaitingTime;
    private final Long minWaitingTime;
    private final Long maximumTravelTime;
    private final Long minimumTravelTime;
    private final Integer countFinishedTravels;
    private final Integer totalPeople;
    private final List<Pair<UUID, Long>> peopleFinishedTravels;
}