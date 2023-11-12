package com.tekal.elevatortechtest.service.config;

import com.tekal.elevatortechtest.model.Person;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Configuration
public class PeopleInBuildingConfig {

    private final Map<Integer, List<Person>> peopleInBuilding;

    public PeopleInBuildingConfig() {
        this.peopleInBuilding = new HashMap<>();
    }

    @Bean
    public Map<Integer, List<Person>> peopleInBuilding() {
        return Objects.requireNonNullElseGet(peopleInBuilding, HashMap::new);
    }
}
