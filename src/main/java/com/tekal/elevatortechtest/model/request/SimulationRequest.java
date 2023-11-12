package com.tekal.elevatortechtest.model.request;

import lombok.Builder;

@Builder
public record SimulationRequest(Long simulationSeed, Integer durationInSeconds){}
