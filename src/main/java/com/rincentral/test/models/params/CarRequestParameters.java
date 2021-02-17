package com.rincentral.test.models.params;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarRequestParameters {
    private String country;
    private String segment;
    private Double minEngineDisplacement;
    private Integer minEngineHorsepower;
    private Integer minMaxSpeed;
    private String search;
    private Boolean isFull;
    private Integer year;
    private String bodyStyle;
}
