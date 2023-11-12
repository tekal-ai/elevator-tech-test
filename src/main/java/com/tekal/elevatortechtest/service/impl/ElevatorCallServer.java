package com.tekal.elevatortechtest.service.impl;

import com.tekal.elevatortechtest.model.Elevator;
import com.tekal.elevatortechtest.model.Person;
import com.tekal.elevatortechtest.model.PersonState;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.service.PersonService;
import com.tekal.elevatortechtest.service.StatisticsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Instant;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

@Slf4j
public abstract class ElevatorCallServer {

    private final PersonService personService;

    private final StatisticsService statisticsService;

    @Autowired
    protected ElevatorCallServer(PersonService personService, StatisticsService statisticsService) {
        this.personService = personService;
        this.statisticsService = statisticsService;
    }

    protected abstract void serveElevatorCalls();

    protected void serveElevatorCall(Elevator elevator, ElevatorCall elevatorCall) {
        performElevatorRoutine(elevator, elevatorCall.getCalledFromFloor());
        performElevatorRoutine(elevator, elevatorCall.getDestinationFloor());
    }

    private void performElevatorRoutine(Elevator elevator, Integer destinationFloor) {
        if (Objects.equals(elevator.getCurrentFloor(), destinationFloor)) {
            log.info("Elevator " + elevator.getElevatorId() + " is already on floor " + destinationFloor);
            updatePersonStates(elevator);
            return;
        }

        log.info("Elevator " + elevator.getElevatorId() + " is closing its doors");
        elevator.closeDoors();

        log.info("Elevator " + elevator.getElevatorId() + " is moving to floor " + destinationFloor);
        elevator.move(destinationFloor);

        log.info("Elevator " + elevator.getElevatorId() + " has opened its doors");
        elevator.openDoors();
        updatePersonStates(elevator);

    }

    private void updatePersonStates(Elevator elevator) {
        List<Person> persons = personService.getPeopleInBuilding().get(elevator.getCurrentFloor());
        if (persons == null) {
            return;
        }

        Iterator<Person> iterator = persons.iterator();
        while (iterator.hasNext()) {
            Person person = iterator.next();
            if (person.getState() == PersonState.WAITING && personService.getPeopleInBuilding().get(elevator.getCurrentFloor()).contains(person)) {
                elevator.addPassenger(person);
                statisticsService.stopWaitingTime(person.getPersonId(), Instant.now().toEpochMilli());
                statisticsService.recordTravelTime(person.getPersonId(), Instant.now().toEpochMilli());
                person.setState(PersonState.IN_ELEVATOR);
                iterator.remove(); // Safe removal during iteration
                log.info("Person " + person.getPersonId() + " is now in elevator " + elevator.getElevatorId());
                personService.movePerson(person);
            } else if (person.getState() == PersonState.IN_ELEVATOR && Objects.equals(person.getDestinationFloor(), elevator.getCurrentFloor())) {
                person.setState(PersonState.ARRIVED);
                elevator.removePassenger(person);
                statisticsService.stopTravelTime(person.getPersonId(), Instant.now().toEpochMilli());
                log.info("Person " + person.getPersonId() + " has arrived at floor " + elevator.getCurrentFloor());
                iterator.remove(); // Safe removal during iteration
            }
        }
    }
}