package com.tekal.elevatortechtest.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@AllArgsConstructor
@Getter
@Setter
@Builder
public class Person {
    private UUID personId;
    private int destinationFloor;
}
