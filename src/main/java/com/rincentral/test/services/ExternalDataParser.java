package com.rincentral.test.services;

import com.rincentral.test.models.Car;
import com.rincentral.test.models.Modification;
import com.rincentral.test.models.external.ExternalCar;
import com.rincentral.test.repositories.CarRepository;
import com.rincentral.test.repositories.ModificationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class ExternalDataParser {

    private static final Logger LOGGER = LogManager.getLogger(ExternalDataParser.class);

    private final boolean failFast;

    private final ExternalCarsApiService service;
    private final ModificationRepository modRepository;
    private final CarRepository carRepository;

    public ExternalDataParser(ExternalCarsApiService service,
                              ModificationRepository modRepository,
                              CarRepository carRepository,
                              @Value("${spring.profiles.active}") String profiles) {
        this.service = service;
        this.modRepository = modRepository;
        this.carRepository = carRepository;
        this.failFast = profiles.contains("dev");
    }

    @EventListener(ApplicationReadyEvent.class)
    public void parse() {
        LOGGER.info("Start parsing external data");

        List<ExternalCar> extCars;
        int numOfPages = 0, totalEntities = 0, expectedEntities = 0;
        for (; (extCars = service.loadCars(numOfPages)).size() > 0; numOfPages++) {
            expectedEntities += extCars.size();
            totalEntities += save(extCars);
        }

        LOGGER.info("{} car pages ({}/{} entities) successfully parsed",
                numOfPages, totalEntities, expectedEntities);
    }

    @Transactional
    protected int save(List<ExternalCar> list) {
        int count = 0;
        for (ExternalCar externalCar : list) {
            Optional<Modification> optional = modRepository.find(
                    externalCar.getModification(),
                    externalCar.getGeneration(),
                    externalCar.getModel());
            if (optional.isEmpty()) {
                if (failFast) {
                    throw new NoSuchElementException(
                            String.format("Modification with id=%d not found!", externalCar.getId()));
                }
                continue;
            }
            Car car = new Car();
            car.setId(externalCar.getId());
            car.setModification(optional.get());
            carRepository.save(new Car(externalCar.getId(), optional.get()));
            count++;
        }
        return count;
    }
}