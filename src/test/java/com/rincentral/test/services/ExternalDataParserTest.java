package com.rincentral.test.services;

import com.rincentral.test.repositories.CarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
class ExternalDataParserTest {

    @Autowired
    ExternalCarsApiService externalCarsApiService;
    @Autowired
    CarRepository carRepository;

    @Test
    void parseTest() {
        // .parse() invoked on startup
        long expectedSize = externalCarsApiService.loadAllCars().size();
        long actualSize = carRepository.count();
        Assertions.assertEquals(expectedSize, actualSize);
    }
}