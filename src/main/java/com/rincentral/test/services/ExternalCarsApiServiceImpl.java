package com.rincentral.test.services;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.rincentral.test.models.external.ExternalBrand;
import com.rincentral.test.models.external.ExternalCar;
import com.rincentral.test.models.external.ExternalCarInfo;
import lombok.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Profile;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Profile("prod")
@Service
public class ExternalCarsApiServiceImpl implements ExternalCarsApiService {
    private static final Logger LOGGER = LogManager.getLogger(ExternalCarsApiServiceImpl.class);

    private static final String ALL_CARS_URL = "http://localhost:8084/api/v1/cars";
    private static final String CAR_BY_ID_URL = "http://localhost:8084/api/v1/cars/%d";
    private static final String ALL_BRANDS_URL = "http://localhost:8084/api/v1/brands";
    private static final String BRAND_BY_ID_URL = "http://localhost:8084/api/v1/brands/%d";
    private static final String PAGED = "/paged?page={page}";
    private final RestTemplate restTemplate = new RestTemplate();

    public List<ExternalCar> loadAllCars() {
        try {
            ResponseEntity<ExternalCar[]> allCarsResponse =
                    restTemplate.getForEntity(ALL_CARS_URL, ExternalCar[].class);
            if (allCarsResponse.getStatusCode() != HttpStatus.OK ||
                    allCarsResponse.getBody() == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(allCarsResponse.getBody());
        } catch (RestClientException restClientException) {
            LOGGER.error("Error when trying to load all cars", restClientException);
            return Collections.emptyList();
        }
    }

    public List<ExternalCar> loadCars(int page) {
        try {
            ResponseEntity<ExternalEntityList<ExternalCar>> responseEntity =
                    restTemplate.exchange(
                            ALL_CARS_URL + PAGED,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<>() {
                            },
                            page
                    );
            if (responseEntity.getStatusCode() != HttpStatus.OK ||
                    responseEntity.getBody() == null || responseEntity.getBody().getList() == null) {
                return Collections.emptyList();
            }
            return responseEntity.getBody().getList();
        } catch (RestClientException restClientException) {
            LOGGER.error("Error when trying to load all cars", restClientException);
            return Collections.emptyList();
        }
    }

    public ExternalCarInfo loadCarInformationById(int id) {
        String carUrl = String.format(CAR_BY_ID_URL, id);
        try {
            ResponseEntity<ExternalCarInfo> carInfoResponse = restTemplate.getForEntity(carUrl, ExternalCarInfo.class);
            if (carInfoResponse.getStatusCode() != HttpStatus.OK || carInfoResponse.getBody() == null) {
                return null;
            }
            return carInfoResponse.getBody();
        } catch (RestClientException restClientException) {
            LOGGER.error("Error when trying to load car with id {}", id, restClientException);
            return null;
        }
    }

    public List<ExternalBrand> loadAllBrands() {
        try {
            ResponseEntity<ExternalBrand[]> allBrandsResponse =
                    restTemplate.getForEntity(ALL_BRANDS_URL, ExternalBrand[].class);
            if (allBrandsResponse.getStatusCode() != HttpStatus.OK || allBrandsResponse.getBody() == null) {
                return Collections.emptyList();
            }
            return Arrays.asList(allBrandsResponse.getBody());
        } catch (RestClientException restClientException) {
            LOGGER.error("Error when trying to load all brands", restClientException);
            return Collections.emptyList();
        }
    }

    public List<ExternalBrand> loadBrands(int page) {
        try {
            ResponseEntity<ExternalEntityList<ExternalBrand>> responseEntity =
                    restTemplate.exchange(
                            ALL_CARS_URL + PAGED,
                            HttpMethod.GET,
                            null,
                            new ParameterizedTypeReference<>() {
                            },
                            page
                    );
            if (responseEntity.getStatusCode() != HttpStatus.OK ||
                    responseEntity.getBody() == null || responseEntity.getBody().getList() == null) {
                return Collections.emptyList();
            }
            return responseEntity.getBody().getList();
        } catch (RestClientException restClientException) {
            LOGGER.error("Error when trying to load all cars", restClientException);
            return Collections.emptyList();
        }
    }

    public ExternalBrand loadBrandById(int id) {
        String brandUrl = String.format(BRAND_BY_ID_URL, id);
        try {
            ResponseEntity<ExternalBrand> brandResponse = restTemplate.getForEntity(brandUrl, ExternalBrand.class);
            if (brandResponse.getStatusCode() != HttpStatus.OK || brandResponse.getBody() == null) {
                return null;
            }
            return brandResponse.getBody();
        } catch (RestClientException restClientException) {
            LOGGER.error("Error when trying to load brand with id {}", id, restClientException);
            return null;
        }
    }

    private static class ExternalEntityList<T> {
        @JsonProperty("content")
        @Getter
        private List<T> list;
    }
}
