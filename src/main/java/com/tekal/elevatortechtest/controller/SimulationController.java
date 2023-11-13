package com.tekal.elevatortechtest.controller;

import com.tekal.elevatortechtest.controller.Response.SimulationResult;
import com.tekal.elevatortechtest.model.request.SimulationRequest;
import com.tekal.elevatortechtest.service.SimulationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;

import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/v1/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    @Autowired
    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @PostMapping("/run")
    public DeferredResult<ResponseEntity<SimulationResult>> runSimulation(@RequestBody SimulationRequest simulationRequest) {
        DeferredResult<ResponseEntity<SimulationResult>> deferredResult = new DeferredResult<>();

        CompletableFuture.supplyAsync(() -> simulationService.runSimulation(simulationRequest))
                .whenComplete((result, throwable) -> {
                    if (throwable != null) {
                        deferredResult.setErrorResult(ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null));
                    } else {
                        deferredResult.setResult(ResponseEntity.ok(result));
                    }
                });

        return deferredResult;
    }

}