package com.tekal.elevatortechtest.controller;

import com.tekal.elevatortechtest.model.Person;
import com.tekal.elevatortechtest.model.request.ElevatorCall;
import com.tekal.elevatortechtest.service.ElevatorService;
import com.tekal.elevatortechtest.service.PersonService;
import com.tekal.elevatortechtest.service.manager.ElevatorServiceManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/people")
public class PersonController {

    private final ElevatorService elevatorService;
    private final PersonService personService;

    @Autowired
    public PersonController(ElevatorServiceManager elevatorServiceManager, PersonService personService) {
        this.elevatorService = elevatorServiceManager.getActiveElevatorService();
        this.personService = personService;
    }

    @PostMapping("/add")
    public ResponseEntity<Person> addPerson(@RequestBody ElevatorCall elevatorCall) {
        Person newPerson = personService.addPersonToBuilding(elevatorCall);
        elevatorService.processElevatorCall(elevatorCall);

        return ResponseEntity.ok(newPerson);
    }

}
