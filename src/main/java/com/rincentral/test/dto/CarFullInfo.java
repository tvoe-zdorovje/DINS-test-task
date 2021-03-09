package com.rincentral.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarFullInfo extends CarInfo {

    private EngineCharacteristics engine;
    private BodyCharacteristics body;

    public CarFullInfo(CarInfo carInfo, EngineCharacteristics engine, BodyCharacteristics body) {
        super(carInfo);
        this.engine = engine;
        this.body = body;
    }
}
