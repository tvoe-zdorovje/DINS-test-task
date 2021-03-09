package com.rincentral.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EngineCharacteristics {
    private String type;
    private String cylinders;
    private int displacement;
    private int horsepower;
}
