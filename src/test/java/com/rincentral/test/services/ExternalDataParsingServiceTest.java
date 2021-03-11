package com.rincentral.test.services;

import com.rincentral.test.models.Car;
import com.rincentral.test.models.Model;
import com.rincentral.test.repositories.CarRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@SpringBootTest
class ExternalDataParsingServiceTest {

    @Autowired
    ExternalCarsApiService externalCarsApiService;
    @Autowired
    CarRepository carRepository;

    @Test
    @Transactional(readOnly = true)
    void parseTest() {
        // .parse() invoked on startup
        long expectedSize = externalCarsApiService.loadAllCars().size();
        long actualSize = carRepository.count();
        Assertions.assertEquals(expectedSize, actualSize);

        if (externalCarsApiService.getClass() == MockExternalCarsApiService.class) {
            final List<Car> all = carRepository.findAll();
            Assertions.assertTrue(all.size() >= 166);
            Assertions.assertEquals(all.get(164).getModification().getTitle(), "1.4 Joker");
            Assertions.assertEquals(all.get(164).getModification().getGeneration().getTitle(), "XYZ gen");
            final Model model = all.get(164).getModification().getGeneration().getModel();
            Assertions.assertEquals(model.getTitle(), "model");
            Assertions.assertEquals(model.getBrand().getTitle(), "The Brand");
            Assertions.assertEquals(model.getBrand().getCountry().getTitle(), "Belarus");
            Assertions.assertEquals(model.getSegment().getTitle(), "Z-segment");
        }
    }
}