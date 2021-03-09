package com.rincentral.test.models.enums;

import lombok.Getter;

public enum FuelType {
    GASOLINE("Gasoline"), DIESEL("Diesel"), HYBRID("Hybrid");

    @Getter
    private final String value;

    FuelType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
