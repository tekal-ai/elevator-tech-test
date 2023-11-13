package com.tekal.elevatortechtest.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class StatisticsService {

    private final List<Pair<UUID, MutablePair<Long, Long>>> waitingTimes = new ArrayList<>();
    private final List<Pair<UUID, MutablePair<Long, Long>>> travelTimes = new ArrayList<>();

    public UUID generateSimulationId() {
        return UUID.randomUUID();
    }

    public void recordWaitingTime(UUID personId, Long startTime) {
        waitingTimes.add(Pair.of(personId, new MutablePair<>(startTime, 0L)));
        log.info("Person " + personId + " starts waiting at " + startTime + "ms");
    }

    public void stopWaitingTime(UUID personId, Long endTime) {
        waitingTimes.stream()
                .filter(p -> p.getLeft().equals(personId))
                .findFirst()
                .ifPresent(p -> p.getRight().setRight(endTime));
        log.info("Person " + personId + " stops waiting at " + endTime + "ms");
    }

    public void recordTravelTime(UUID personId, Long startTime) {
        travelTimes.add(Pair.of(personId, new MutablePair<>(startTime, 0L)));
        log.info("Person " + personId + " starts traveling at " + startTime + "ms");
    }

    public void stopTravelTime(UUID personId, Long endTime) {
        travelTimes.stream()
                .filter(p -> p.getLeft().equals(personId))
                .findFirst()
                .ifPresent(p -> p.getRight().setRight(endTime));
        log.info("Person " + personId + " stops traveling at " + endTime + "ms");
    }

    public Double getAverageWaitingTime(Long timeSimulationStopped) {
        return calculateAverage(waitingTimes.stream()
                .map(pair -> !pair.getRight().getRight().equals(0L) ? pair.getRight().getRight() - pair.getRight().getLeft() : timeSimulationStopped - pair.getRight().getLeft())
                .toList());
    }

    public Double getAverageTravelTime(Long timeSimulationStopped) {
        return calculateAverage(travelTimes.stream()
                .map(pair -> !pair.getRight().getRight().equals(0L) ? pair.getRight().getRight() - pair.getRight().getLeft() : timeSimulationStopped - pair.getRight().getLeft())
                .toList());
    }

    public Long getMaximumWaitingTime(Long timeSimulationStopped) {
        return calculateMax(waitingTimes.stream()
                .map(pair -> !pair.getRight().getRight().equals(0L) ? pair.getRight().getRight() - pair.getRight().getLeft() : timeSimulationStopped - pair.getRight().getLeft())
                .toList());
    }

    public Long getMinimumWaitingTime(Long timeSimulationStopped) {
        return calculateMin(waitingTimes.stream()
                .map(pair -> !pair.getRight().getRight().equals(0L) ? pair.getRight().getRight() - pair.getRight().getLeft() : timeSimulationStopped - pair.getRight().getLeft())
                .toList());
    }

    public Long getMaximumTravelTime(Long timeSimulationStopped) {
        return calculateMax(travelTimes.stream()
                .map(pair -> !pair.getRight().getRight().equals(0L) ? pair.getRight().getRight() - pair.getRight().getLeft() : timeSimulationStopped - pair.getRight().getLeft())
                .toList());
    }

    public Long getMinimumTravelTime(Long timeSimulationStopped) {
        return calculateMin(travelTimes.stream()
                .map(pair -> !pair.getRight().getRight().equals(0L) ? pair.getRight().getRight() - pair.getRight().getLeft() : timeSimulationStopped - pair.getRight().getLeft())
                .toList());
    }

    public Integer countFinishedTravels() {
        return travelTimes.stream().filter(pair -> !pair.getRight().getRight().equals(0L)).toList().size();
    }

    public Integer countUnfinishedTravels() {
        return travelTimes.stream().filter(pair -> pair.getRight().getRight().equals(0L)).toList().size();
    }

    public Integer countTotalPeople() {
        return waitingTimes.size();
    }

    public List<Pair<UUID, Long>> getPeopleFinishedTravels() {
        return travelTimes.stream()
                .filter(pair -> !pair.getRight().getRight().equals(0L))
                .map(entry -> Pair.of(entry.getLeft(), entry.getRight().getRight() - entry.getRight().getLeft()))
                .toList();
    }

    // Add other methods as needed

    private Double calculateAverage(List<Long> times) {
        return times.stream()
                .mapToLong(Long::valueOf)
                .average()
                .orElse(0.0);
    }

    private Long calculateMax(List<Long> times) {
        return times.stream()
                .mapToLong(Long::valueOf)
                .max()
                .orElse(0L);
    }

    private Long calculateMin(List<Long> times) {
        return times.stream()
                .mapToLong(Long::valueOf)
                .min()
                .orElse(0L);
    }
}
