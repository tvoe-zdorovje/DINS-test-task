package com.rincentral.test.models.params;

import lombok.*;

@Getter
@Setter
@ToString
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
