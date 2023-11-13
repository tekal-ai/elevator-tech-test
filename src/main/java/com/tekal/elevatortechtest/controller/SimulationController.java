package com.tekal.elevatortechtest.controller;

import com.tekal.elevatortechtest.controller.Response.SimulationResult;
import com.tekal.elevatortechtest.model.request.SimulationRequest;
import com.tekal.elevatortechtest.service.SimulationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/simulation")
@Slf4j
public class SimulationController {

    private final SimulationService simulationService;

    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/run")
    public ResponseEntity<SimulationResult> runSimulation(@RequestBody SimulationRequest simulationRequest) {
        log.info("Received simulation request with parameters: {Seed: " + simulationRequest.simulationSeed() + ", Duration: " + simulationRequest.durationInSeconds() + "}");
        SimulationResult result = simulationService.runSimulation(simulationRequest);
        return ResponseEntity.ok(result);
    }
}