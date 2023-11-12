package com.tekal.elevatortechtest.controller;

import com.tekal.elevatortechtest.model.Person;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.PersonService;
import com.tekal.elevatortechtest.service.manager.ElevatorServiceManager;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static com.tekal.elevatortechtest.util.ElevatorUtil.createTestPerson;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PersonControllerTest {

    @Mock
    private ElevatorServiceManager elevatorServiceManager;

    @Mock
    private ElevatorService elevatorService;

    @Mock
    private PersonService personService;

    private final PersonController personController;

    public PersonControllerTest() {
        MockitoAnnotations.openMocks(this);
        when(elevatorServiceManager.getActiveElevatorService()).thenReturn(elevatorService);
        personController = new PersonController(elevatorServiceManager, personService);
    }

    @Test
    void addPerson_ShouldReturnPersonAndCallServices() {
        // Arrange
        ElevatorCall elevatorCall = new ElevatorCall(1, 5);
        Person expectedPerson = createTestPerson();

        when(personService.addPersonToBuilding(any(ElevatorCall.class))).thenReturn(expectedPerson);

        // Act
        ResponseEntity<Person> responseEntity = personController.addPerson(elevatorCall);

        // Assert
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(expectedPerson, responseEntity.getBody());
        verify(personService).addPersonToBuilding(elevatorCall);
        verify(elevatorService).processElevatorCall(elevatorCall);
    }
}