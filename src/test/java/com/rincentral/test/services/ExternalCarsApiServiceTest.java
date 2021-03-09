package com.rincentral.test.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ExternalCarsApiServiceTest {

    @Autowired
    private ExternalCarsApiService externalCarsApiService;

    @Test
    void loadAllCars() {
        Assertions.assertFalse(externalCarsApiService.loadAllCars().isEmpty());
    }

    @Test
    void loadCars() {
        Assertions.assertFalse(externalCarsApiService.loadCars(0).isEmpty());
        Assertions.assertTrue(externalCarsApiService.loadCars(99999).isEmpty());
    }

    @Test
    void loadCarInformationById() {
        Assertions.assertNotNull(externalCarsApiService.loadCarInformationById(1));
    }

    @Test
    void loadAllBrands() {
        Assertions.assertFalse(externalCarsApiService.loadAllBrands().isEmpty());
    }

    @Test
    void loadBrands() {
        Assertions.assertFalse(externalCarsApiService.loadBrands(0).isEmpty());
        Assertions.assertTrue(externalCarsApiService.loadBrands(99999).isEmpty());
    }
}