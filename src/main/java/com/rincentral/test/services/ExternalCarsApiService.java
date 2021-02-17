package com.rincentral.test.services;

import com.rincentral.test.models.external.ExternalCar;
import com.rincentral.test.models.external.ExternalCarInfo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Service
public class ExternalCarsApiService {
    private static final String ALL_CARS_URL = "http://localhost:8084/api/v1/cars";
    private static final String CAR_BY_ID_URL = "http://localhost:8084/api/v1/cars/%d";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<ExternalCar> loadAllCars() {
        ResponseEntity<ExternalCar[]> allCarsResponse = restTemplate.getForEntity(ALL_CARS_URL, ExternalCar[].class);
        if (allCarsResponse.getStatusCode() != HttpStatus.OK || allCarsResponse.getBody() == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(allCarsResponse.getBody());
    }

    public ExternalCarInfo loadCarInformationById(int id) {
        String carUrl = String.format(CAR_BY_ID_URL, id);
        ResponseEntity<ExternalCarInfo> carInfoResponse = restTemplate.getForEntity(carUrl, ExternalCarInfo.class);
        if (carInfoResponse.getStatusCode() != HttpStatus.OK || carInfoResponse.getBody() == null) {
            return null;
        }
        return carInfoResponse.getBody();
    }
}
