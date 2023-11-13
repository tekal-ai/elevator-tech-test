package com.tekal.elevatortechtest.service;

import com.tekal.elevatortechtest.controller.Response.SimulationResult;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.model.request.SimulationRequest;
import com.tekal.elevatortechtest.service.manager.ElevatorServiceManager;
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
    private final ElevatorServiceManager elevatorServiceManager;
    private final PersonService personService;
    private final StatisticsService statisticsService;

    @Autowired
    public SimulationService(ElevatorServiceManager elevatorServiceManager, PersonService personService, StatisticsService statisticsService) {
        this.elevatorServiceManager = elevatorServiceManager;
        this.personService = personService;
        this.statisticsService = statisticsService;
    }

    public SimulationResult runSimulation(SimulationRequest simulationRequest) {
        RandomGenerator randomGenerator = new Well19937c(simulationRequest.simulationSeed());

        long simulationEndTime = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(simulationRequest.durationInSeconds());

        while (System.currentTimeMillis() < simulationEndTime) {
            generateRandomElevatorCall(randomGenerator);
            sleep();
        }

        log.info("Simulation complete, calculating statistics");

        Long simulationFinishedTime = System.currentTimeMillis();

        return SimulationResult.builder()
                .simulationId(statisticsService.generateSimulationId().toString())
                .seed(simulationRequest.simulationSeed())
                .durationInSeconds(simulationRequest.durationInSeconds())
                .activeElevatorService(elevatorServiceManager.getActiveElevatorService().getClass().getSimpleName())
                .averageWaitingTime(convertToSeconds(statisticsService.getAverageWaitingTime(simulationFinishedTime).longValue()))
                .averageTravelTime(convertToSeconds(statisticsService.getAverageTravelTime(simulationFinishedTime).longValue()))
                .maxWaitingTime(convertToSeconds(statisticsService.getMaximumWaitingTime(simulationFinishedTime)))
                .minWaitingTime(convertToSeconds(statisticsService.getMinimumWaitingTime(simulationFinishedTime)))
                .maximumTravelTime(convertToSeconds(statisticsService.getMaximumTravelTime(simulationFinishedTime)))
                .minimumTravelTime(convertToSeconds(statisticsService.getMinimumTravelTime(simulationFinishedTime)))
                .countFinishedTravels(statisticsService.countFinishedTravels())
                .countUnfinishedTravels(statisticsService.countUnfinishedTravels())
                .totalPeople(statisticsService.countTotalPeople())
                .peopleFinishedTravels(statisticsService.getPeopleFinishedTravels())
                .build();
    }

    private void generateRandomElevatorCall(RandomGenerator random) {
        int calledFromFloor = random.nextInt(100) + 1;

        int numPassengers;
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
            elevatorServiceManager.getActiveElevatorService().processElevatorCall(elevatorCall);
        }

    }

    private int generateNumberOfPassengersOutsideLobby(RandomGenerator random) {
        double logMean = 1.5; // Adjust as needed
        double logStdDev = 0.8; // Adjust as needed

        LogNormalDistribution logNormalDistribution = new LogNormalDistribution(random, logMean, logStdDev);
        double passengers = logNormalDistribution.sample();

        return Math.min(5, Math.max(1, (int) Math.round(passengers)));
    }

    private void sleep() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            log.error("Thread sleep interrupted", e);
            Thread.currentThread().interrupt();
        }
    }

    private Long convertToSeconds(Long milliseconds) {
        return TimeUnit.MILLISECONDS.toSeconds(milliseconds);
    }
}
