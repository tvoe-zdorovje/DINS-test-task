package com.rincentral.test.services;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

// requires external service enabled if profile is "prod"
@ActiveProfiles("dev")
@SpringBootTest
class ExternalCarsApiServiceTest {

    @Autowired
    private ExternalCarsApiService externalCarsApiService;

    @Test
    void loadAllCarsTest() {
        Assertions.assertFalse(externalCarsApiService.loadAllCars().isEmpty());
    }

    @Test
    void loadCarsTest() {
        Assertions.assertFalse(externalCarsApiService.loadCars(0).isEmpty());
        Assertions.assertTrue(externalCarsApiService.loadCars(99999).isEmpty());
    }

    @Test
    void loadCarInformationByIdTest() {
        Assertions.assertNotNull(externalCarsApiService.loadCarInformationById(1));
    }

    @Test
    void loadAllBrandsTest() {
        Assertions.assertFalse(externalCarsApiService.loadAllBrands().isEmpty());
    }

    @Test
    void loadBrandByIdTest() {
        Assertions.assertNotNull(externalCarsApiService.loadBrandById(1));
    }

    @Test
    void loadBrandsTest() {
        Assertions.assertFalse(externalCarsApiService.loadBrands(0).isEmpty());
        Assertions.assertTrue(externalCarsApiService.loadBrands(99999).isEmpty());
    }
}