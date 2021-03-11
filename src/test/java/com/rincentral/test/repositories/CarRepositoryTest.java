package com.rincentral.test.repositories;

import com.rincentral.test.repositories.specification.CarSpecification;
import com.rincentral.test.models.Car;
import com.rincentral.test.models.params.CarRequestParameters;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@ActiveProfiles("dev")
@Transactional(readOnly = true)
@SpringBootTest
class CarRepositoryTest {

    static final String COUNTRY = "England";
    static final String SEGMENT = "E-segment";
    static final Integer HP = 200;
    static final Integer MAX_SPEED = 200;
    static final String BODY_STYLE = "Sedan";
    static final Double ENGINE_DISPLACEMENT = 3.0;
    static final Integer YEAR = 2010;
    static final String MODIFICATION = "5";
    static final String GENERATION = "I generation";
    static final String MODEL = "XF";

    @Autowired
    CarRepository repository;

    @Test
    void findAllWithCountryTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setCountry(COUNTRY);
        CarSpecification specification = new CarSpecification(parameters);
        final List<Car> cars = repository.findAll(specification);

        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getGeneration().getModel().getBrand().getCountry().getTitle())
                    .isEqualTo(COUNTRY);
        }
    }

    @Test
    void findAllWithSegmentTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setSegment(SEGMENT);
        CarSpecification specification = new CarSpecification(parameters);
        final List<Car> cars = repository.findAll(specification);

        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getGeneration().getModel().getSegment().getTitle())
                    .isEqualTo(SEGMENT);
        }
    }

    @Test
    void findAllWithMinEngineDisplacementTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setMinEngineDisplacement(ENGINE_DISPLACEMENT);
        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getEngineDisplacement())
                    .isGreaterThanOrEqualTo((int) (ENGINE_DISPLACEMENT * 1000));
        }
    }

    @Test
    void findAllWithMinEngineHPTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setMinEngineHorsepower(HP);
        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getHp()).isGreaterThanOrEqualTo(HP);
        }
    }

    @Test
    void findAllWithMinMaxSpeedTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setMinMaxSpeed(MAX_SPEED);
        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getMaxSpeed()).isGreaterThanOrEqualTo(MAX_SPEED);
        }
    }

    @Test
    void findAllWithModelTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setSearch(MODEL);
        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getGeneration().getModel().getTitle()).isEqualTo(MODEL);
        }
    }

    @Test
    void findAllWithGenerationTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setSearch(GENERATION);
        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getGeneration().getTitle()).isEqualTo(GENERATION);
        }
    }

    @Test
    void findAllWithModificationTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setSearch(MODIFICATION);
        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getTitle()).isEqualTo(MODIFICATION);
        }
    }

    @Test
    void findAllWithYearTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setYear(YEAR);
        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            String[] years = car.getModification().getGeneration().getYears().split("-");
            int from = Integer.parseInt(years[0]);
            Assertions.assertThat(from).isLessThanOrEqualTo(YEAR);
            if (!years[1].equals("present")) {
                int to = Integer.parseInt(years[1]);
                Assertions.assertThat(to).isGreaterThanOrEqualTo(YEAR);
            }
        }
    }

    @Test
    void findAllWithBodyStyleTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setBodyStyle(BODY_STYLE);
        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getBodyStyle()).isEqualTo(BODY_STYLE);
        }
    }

    @Test
    void findAllWithSpecificationTest() {
        CarRequestParameters parameters = new CarRequestParameters();
        parameters.setCountry(COUNTRY);
        parameters.setYear(YEAR);
        parameters.setBodyStyle(BODY_STYLE);
        parameters.setSegment(SEGMENT);
        parameters.setMinEngineDisplacement(ENGINE_DISPLACEMENT);
        parameters.setMinEngineHorsepower(HP);
        parameters.setMinMaxSpeed(MAX_SPEED);
        parameters.setSearch(GENERATION);

        CarSpecification specification = new CarSpecification(parameters);

        final List<Car> cars = repository.findAll(specification);
        Assertions.assertThat(cars).isNotEmpty();
        System.out.println(cars);
        for (Car car : cars) {
            Assertions.assertThat(car.getModification().getHp()).isGreaterThanOrEqualTo(HP);
            Assertions.assertThat(car.getModification().getMaxSpeed()).isGreaterThanOrEqualTo(MAX_SPEED);
            Assertions.assertThat(car.getModification().getGeneration().getModel().getTitle()).isEqualTo(MODEL);
            Assertions.assertThat(car.getModification().getGeneration().getTitle()).isEqualTo(GENERATION);
            Assertions.assertThat(car.getModification().getTitle()).isEqualTo(MODIFICATION);
            Assertions.assertThat(car.getModification().getBodyStyle()).isEqualTo(BODY_STYLE);
            Assertions.assertThat(car.getModification().getGeneration().getModel().getBrand().getCountry().getTitle())
                    .isEqualTo(COUNTRY);
            Assertions.assertThat(car.getModification().getEngineDisplacement())
                    .isGreaterThanOrEqualTo((int) (ENGINE_DISPLACEMENT * 1000));
            Assertions.assertThat(car.getModification().getGeneration().getModel().getSegment().getTitle())
                    .isEqualTo(SEGMENT);
            String[] years = car.getModification().getGeneration().getYears().split("-");
            int from = Integer.parseInt(years[0]);
            Assertions.assertThat(from).isLessThanOrEqualTo(YEAR);
            if (!years[1].equals("present")) {
                int to = Integer.parseInt(years[1]);
                Assertions.assertThat(to).isGreaterThanOrEqualTo(YEAR);
            }
        }
    }

}