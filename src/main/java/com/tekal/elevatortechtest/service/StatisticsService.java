package com.tekal.elevatortechtest.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.mutable.MutableLong;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class StatisticsService {

    private List<Pair<UUID, MutableLong>> waitingTimes = new ArrayList<>();
    private List<Pair<UUID, MutableLong>> travelTimes = new ArrayList<>();

    public void recordWaitingTime(UUID personId, Long waitingTime) {
        waitingTimes.add(Pair.of(personId, new MutableLong(waitingTime)));
        log.info("Person " + personId + " starts waiting at " + waitingTime + "ms");
    }

    public void stopWaitingTime(UUID personId, Long waitingTime) {
        waitingTimes.stream()
                .filter(p -> p.getLeft().equals(personId))
                .findFirst()
                .ifPresent(p -> p.getRight().subtract(waitingTime));
        log.info("Person " + personId + " stops waiting at " + waitingTime + "ms");
    }

    public void recordTravelTime(UUID personId, Long travelTime) {
        travelTimes.add(Pair.of(personId, new MutableLong(travelTime)));
        log.info("Person " + personId + " starts travelling at " + travelTime + "ms");
    }

    public void stopTravelTime(UUID personId, Long travelTime) {
        travelTimes.stream()
                .filter(p -> p.getLeft().equals(personId))
                .findFirst()
                .ifPresent(p -> p.getRight().subtract(travelTime));
        log.info("Person " + personId + " stops traveling at " + travelTime + "ms");
    }

    public Double getAverageWaitingTime() {
        return calculateAverage(waitingTimes
                .stream()
                .map(p -> p.getRight().longValue())
                .toList());
    }

    public Double getAverageTravelTime() {
        return calculateAverage(waitingTimes
                .stream()
                .map(p -> p.getRight().longValue())
                .toList());
    }

    public Long getMaximumWaitingTime() {
        return calculateMax(waitingTimes
                .stream()
                .map(p -> p.getRight().longValue())
                .toList());
    }

    public Long getMinimumWaitingTime() {
        return calculateMin(waitingTimes
                .stream()
                .map(p -> p.getRight().longValue())
                .toList());
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
