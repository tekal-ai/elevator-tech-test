package com.tekal.elevatortechtest.service;

import com.tekal.elevatortechtest.controller.Response.SimulationResult;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.model.request.SimulationRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.math3.distribution.LogNormalDistribution;
import org.apache.commons.math3.random.RandomGenerator;
import org.apache.commons.math3.random.Well19937c;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SimulationService {
    private final ElevatorService elevatorService;
    private final PersonService personService;
    private final StatisticsService statisticsService;

    @Autowired
    public SimulationService(ElevatorService elevatorService, PersonService personService, StatisticsService statisticsService) {
        this.elevatorService = elevatorService;
        this.personService = personService;
        this.statisticsService = statisticsService;
    }

    public SimulationResult runSimulation(SimulationRequest simulationRequest) {
        RandomGenerator randomGenerator = new Well19937c(simulationRequest.simulationSeed());

        long simulationEndTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(simulationRequest.durationInSeconds());

        while (System.currentTimeMillis() < simulationEndTime) {
            generateRandomElevatorCall(randomGenerator);
            sleep(5000);
        }

        log.info("Simulation complete, calculating statistics");

        Long simulationFinishedTime = System.currentTimeMillis();

        return SimulationResult.builder()
                .averageWaitingTime(TimeUnit.MILLISECONDS.toSeconds(statisticsService.getAverageWaitingTime(simulationFinishedTime).longValue()))
                .averageTravelTime(TimeUnit.MILLISECONDS.toSeconds(statisticsService.getAverageTravelTime(simulationFinishedTime).longValue()))
                .maxWaitingTime(TimeUnit.MILLISECONDS.toSeconds(statisticsService.getMaximumWaitingTime(simulationFinishedTime)))
                .minWaitingTime(TimeUnit.MILLISECONDS.toSeconds(statisticsService.getMinimumWaitingTime(simulationFinishedTime)))
                .build();
    }

    private void generateRandomElevatorCall(RandomGenerator random) {
        int calledFromFloor = random.nextInt(100) + 1;

        int numPassengers = 0;
        if (calledFromFloor == 1) {
            numPassengers = random.nextInt(5) + 1;
        } else {
            numPassengers = generateNumberOfPassengersOutsideLobby(random);
        }

        for (int i = 0; i < numPassengers; i++) {
            int destinationFloor = random.nextInt(100) + 1;
            while (destinationFloor == calledFromFloor) {
                destinationFloor = random.nextInt(100) + 1;
            }
            ElevatorCall elevatorCall = new ElevatorCall(calledFromFloor, destinationFloor);
            personService.addPersonToBuilding(elevatorCall);
            elevatorService.processElevatorCall(elevatorCall);
        }

    }

    private int generateNumberOfPassengersOutsideLobby(RandomGenerator random) {
        double logMean = 1.5; // Adjust as needed
        double logStdDev = 0.8; // Adjust as needed

        LogNormalDistribution logNormalDistribution = new LogNormalDistribution(random, logMean, logStdDev);
        double passengers = logNormalDistribution.sample();

        return Math.min(5, Math.max(1, (int) Math.round(passengers)));
    }

    private void sleep(int milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            log.error("Thread sleep interrupted", e);
            Thread.currentThread().interrupt();
        }
    }
}
