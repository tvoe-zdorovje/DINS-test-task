package com.rincentral.test.controllers;

import com.rincentral.test.repositories.specification.CarSpecification;
import com.rincentral.test.dto.CarInfo;
import com.rincentral.test.models.Car;
import com.rincentral.test.models.converters.CarConverter;
import com.rincentral.test.models.enums.EngineType;
import com.rincentral.test.models.enums.FuelType;
import com.rincentral.test.models.enums.GearboxType;
import com.rincentral.test.models.enums.WheelDriveType;
import com.rincentral.test.models.params.CarRequestParameters;
import com.rincentral.test.models.params.MaxSpeedRequestParameters;
import com.rincentral.test.repositories.CarRepository;
import com.rincentral.test.repositories.ModificationRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class ApiController {

    private static final Logger LOGGER = LogManager.getLogger(ApiController.class);

    private final CarRepository carRepository;
    private final ModificationRepository modificationRepository;

    @GetMapping("/cars")
    public ResponseEntity<List<? extends CarInfo>> getCars(CarRequestParameters requestParameters) {
        LOGGER.info("Find by: {}", requestParameters);
        final List<Car> carList = carRepository.findAll(new CarSpecification(requestParameters));
//        if (carList.isEmpty()) {
//            throw new NoSuchElementException("No matching entities");
//        }
        final List<CarInfo> carInfoList = CarConverter.convert(
                carList,
                requestParameters.getIsFull() != null && requestParameters.getIsFull());
        return ResponseEntity.ok(carInfoList);
    }

    @GetMapping("/fuel-types")
    public ResponseEntity<List<String>> getFuelTypes() {
        LOGGER.info("Get fuel types.");
        final List<String> list = carRepository.findFuelTypes().stream()
                .map(FuelType::getValue)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/body-styles")
    public ResponseEntity<List<String>> getBodyStyles() {
        LOGGER.info("Get body styles.");
        return ResponseEntity.ok(carRepository.findBodyStyles());
    }

    @GetMapping("/engine-types")
    public ResponseEntity<List<String>> getEngineTypes() {
        LOGGER.info("Get engine types.");
        final List<String> list = carRepository.findEngineTypes().stream()
                .map(EngineType::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/wheel-drives")
    public ResponseEntity<List<String>> getWheelDrives() {
        LOGGER.info("Get wheel drivers");
        final List<String> list = carRepository.findWheelDriverTypes().stream()
                .map(WheelDriveType::name)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/gearboxes")
    public ResponseEntity<List<String>> getGearboxTypes() {
        LOGGER.info("Get gearbox types.");
        final List<String> list = carRepository.findGearboxTypes().stream()
                .map(GearboxType::getValue)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    @GetMapping("/max-speed")
    public ResponseEntity<Double> getAvgMaxSpeed(MaxSpeedRequestParameters requestParameters) {
        LOGGER.info("Get avg max speed by: {}", requestParameters);
        if (requestParameters.getBrand() != null && requestParameters.getModel() != null) {
            return ResponseEntity.badRequest().build();
        }
        Double avg;
        if (requestParameters.getBrand() != null) {
            avg = modificationRepository.getAvgMaxSpeedByBrand(requestParameters.getBrand());
        } else {
            avg = modificationRepository.getAvgMaxSpeedByModel(requestParameters.getModel());
        }
        if (avg == null) {
            throw new NoSuchElementException("The specified model / brand was not found");
        }
        return ResponseEntity.ok(avg);
    }
}