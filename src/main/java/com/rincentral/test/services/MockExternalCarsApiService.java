package com.rincentral.test.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.rincentral.test.models.external.ExternalBrand;
import com.rincentral.test.models.external.ExternalCar;
import com.rincentral.test.models.external.ExternalCarInfo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Profile({"test", "dev"})
@Service
public class MockExternalCarsApiService implements ExternalCarsApiService {

    private static final Logger LOGGER = LogManager.getLogger(MockExternalCarsApiService.class);

    private static final URL CARS_JSON_URL = ClassLoader.getSystemResource("json/cars.json");
    private static final URL BRANDS_JSON_URL = ClassLoader.getSystemResource("json/brands.json");
    private static final URL CAR_INFO_JSON_URL = ClassLoader.getSystemResource("json/carInfo.json");

    private final ObjectMapper objectMapper;
    private List<ExternalCar> cache;

    public MockExternalCarsApiService(ObjectMapper objectMapper) throws URISyntaxException {
        this.objectMapper = objectMapper;
    }

    @Override
    public List<ExternalCar> loadAllCars() {
        return read(CARS_JSON_URL, ExternalCar.class);
    }

    @Override
    public List<ExternalCar> loadCars(int page) {
        if (cache == null) {
            cache = loadAllCars();
        }
        List<ExternalCar> result = cache.stream().skip(page * 10L).limit(10).collect(Collectors.toList());
        if (result.isEmpty()) {
            cache = null; // evict cache when last page +1
        }
        return result;
    }

    @Override
    public ExternalCarInfo loadCarInformationById(int id) {
        return read(CAR_INFO_JSON_URL, ExternalCarInfo.class).stream()
                .filter(externalCarInfo -> externalCarInfo.getId()==id)
                .findFirst().orElse(null);
    }

    @Override
    public List<ExternalBrand> loadAllBrands() {
        return read(BRANDS_JSON_URL, ExternalBrand.class);
    }

    @Override
    public List<ExternalBrand> loadBrands(int page) {
        return loadAllBrands().stream().skip(page * 10L).limit(10).collect(Collectors.toList());
    }

    @Override
    public ExternalBrand loadBrandById(int id) {
        return loadAllBrands().stream()
                .filter(externalBrand -> externalBrand.getId() == id)
                .findFirst()
                .orElse(null);
    }

    private <T> List<T> read(URL url, Class<T> type) {
        try {
            ObjectReader reader = objectMapper.readerFor(type);
            return reader.<T>readValues(url).readAll();
        } catch (IOException e) {
            LOGGER.error("Failed to load data from file.", e);
        }
        return Collections.emptyList();
    }
}
