package com.tekal.elevatortechtest.service;

import com.tekal.elevatortechtest.controller.PersonController;
import com.tekal.elevatortechtest.model.Person;
import com.tekal.elevatortechtest.model.PersonState;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;

class PersonServiceTest {
    @Mock
    private Map<Integer, List<Person>> peopleInBuilding;

    @InjectMocks
    private PersonService personService;

    public PersonServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void addPersonToBuilding_ShouldAddPersonToCorrectFloor() {
        ElevatorCall elevatorCall = new ElevatorCall(1, 5);
        when(peopleInBuilding.computeIfAbsent(any(), any())).thenReturn(new ArrayList<>());
        // Act
        Person addedPerson = personService.addPersonToBuilding(elevatorCall);
        when(peopleInBuilding.get(anyInt())).thenReturn(List.of(addedPerson));

        // Assert
        List<Person> peopleAtFloor = peopleInBuilding.get(1);
        assertEquals(1, peopleAtFloor.size());
        assertEquals(addedPerson, peopleAtFloor.get(0));
        assertEquals(PersonState.WAITING, addedPerson.getState());
    }

    @Test
    void movePerson_ShouldMovePersonToCorrectFloor() {
        ElevatorCall elevatorCall = new ElevatorCall(1, 5);
        when(peopleInBuilding.computeIfAbsent(any(), any())).thenReturn(new ArrayList<>());

        Person addedPerson = personService.addPersonToBuilding(elevatorCall);
        when(peopleInBuilding.get(anyInt())).thenReturn(List.of(addedPerson));
        when(peopleInBuilding.containsKey(anyInt())).thenReturn(true);

        // Act
        personService.movePerson(addedPerson);

        // Assert
        assertTrue(peopleInBuilding.containsKey(5));
        List<Person> peopleAtFloor = peopleInBuilding.get(5);
        assertEquals(1, peopleAtFloor.size());
        assertEquals(addedPerson, peopleAtFloor.get(0));
    }

    @Test
    void getPeopleInBuilding_ShouldReturnCorrectMap() {
        // Arrange
        Map<Integer, List<Person>> peopleInBuilding = Map.of(
                1, List.of(Person.builder().personId(UUID.randomUUID()).destinationFloor(5).state(PersonState.WAITING).build()),
                3, List.of(Person.builder().personId(UUID.randomUUID()).destinationFloor(7).state(PersonState.IN_ELEVATOR).build())
        );
        PersonService personService = new PersonService(peopleInBuilding);

        // Act
        Map<Integer, List<Person>> result = personService.getPeopleInBuilding();

        // Assert
        assertEquals(peopleInBuilding, result);
    }
}