package com.rincentral.test.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CarInfo {
    @JsonProperty("id")
    private Integer id;

    @JsonProperty("segment")
    private String segment;

    @JsonProperty("brand")
    private String brand;

    @JsonProperty("model")
    private String model;

    @JsonProperty("country")
    private String country;

    @JsonProperty("generation")
    private String generation;

    @JsonProperty("modification")
    private String modification;

    public CarInfo(CarInfo carInfo) {
        this.id = carInfo.getId();
        this.modification = carInfo.modification;
        this.brand = carInfo.getBrand();
        this.country = carInfo.getCountry();
        this.generation = carInfo.getGeneration();
        this.model = carInfo.getModel();
        this.segment = carInfo.getSegment();
    }
}
