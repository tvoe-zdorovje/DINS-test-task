package com.rincentral.test.models.enums;

import lombok.Getter;

public enum GearboxType {
    AUTO("Auto"), MANUAL("Manual"), ROBOTIC("Robotic"), CVT("CVT");

    @Getter
    private final String value;

    GearboxType(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}
