package com.tekal.elevatortechtest.service;

import com.tekal.elevatortechtest.model.Person;
import com.tekal.elevatortechtest.model.PersonState;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Slf4j
public class PersonService {
    private final Map<Integer, List<Person>> peopleInBuilding;

    private final StatisticsService statisticsService;

    @Autowired
    public PersonService(Map<Integer, List<Person>> peopleInBuilding, StatisticsService statisticsService) {
        this.peopleInBuilding = peopleInBuilding;
        this.statisticsService = statisticsService;
    }

    public Person addPersonToBuilding(ElevatorCall elevatorCall) {
        Person person = Person.builder()
                .personId(UUID.randomUUID())
                .destinationFloor(elevatorCall.getDestinationFloor())
                .state(PersonState.WAITING)
                .build();

        log.info("Adding person " + person.getPersonId() + " to floor " + elevatorCall.getCalledFromFloor());
        List<Person> peopleAtFloor = peopleInBuilding.computeIfAbsent(elevatorCall.getCalledFromFloor(), k -> new ArrayList<>());
        peopleAtFloor.add(person);
        statisticsService.recordWaitingTime(person.getPersonId(), Instant.now().toEpochMilli());

        return person;
    }

    public void movePerson(Person person) {
        List<Person> peopleAtFloor = peopleInBuilding.computeIfAbsent(person.getDestinationFloor(), k -> new ArrayList<>());
        peopleAtFloor.add(person);
        log.info("Person " + person.getPersonId() + " moving to " + person.getDestinationFloor());
    }

    public Map<Integer, List<Person>> getPeopleInBuilding() {
        return peopleInBuilding;
    }
}
