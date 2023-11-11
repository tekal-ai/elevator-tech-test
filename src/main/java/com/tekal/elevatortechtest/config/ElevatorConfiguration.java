package com.tekal.elevatortechtest.config;

import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.factory.ElevatorFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Configuration
public class ElevatorConfiguration {
    @Bean
    public Set<Elevator> elevatorSet() {
        return Stream.of(ElevatorFactory.createElevator(), ElevatorFactory.createElevator(), ElevatorFactory.createElevator())
                .collect(Collectors.toUnmodifiableSet());
    }
}
