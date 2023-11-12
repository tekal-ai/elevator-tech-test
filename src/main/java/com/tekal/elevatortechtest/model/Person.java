package com.tekal.elevatortechtest.model;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Builder
@Data
public class Person{
    private UUID personId;
    private Integer destinationFloor;
    private PersonState state;
}

