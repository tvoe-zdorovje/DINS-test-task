package com.rincentral.test.models.converters;

import com.rincentral.test.dto.CarFullInfo;
import com.rincentral.test.dto.CarInfo;
import com.rincentral.test.models.*;
import com.rincentral.test.models.enums.EngineType;
import com.rincentral.test.models.enums.FuelType;
import com.rincentral.test.models.enums.GearboxType;
import com.rincentral.test.models.enums.WheelDriveType;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

class CarConverterTest {

    static Car car;

    static final int CAR_ID = 100001;
    static final float ACCELERATION = 7.5f;
    static final String BODY_STYLE = "Sedan";
    static final EngineType ENGINE_TYPE = EngineType.L5;
    static final int ENGINE_DISPLACEMENT = 2200;
    static final FuelType FUEL_TYPE = FuelType.GASOLINE;
    static final GearboxType GEARBOX_TYPE = GearboxType.MANUAL;
    static final int HP = 200;
    static final int MAX_SPEED = 231;
    static final String MODIFICATION_TITLE = "2.2 turbo";
    static final WheelDriveType WHEELDRIVE_TYPE = WheelDriveType.AWD;
    static final String GENERATION_TITLE = "C3";
    static final String YEARS = "1998-1990";
    static final int LENGTH = 4807;
    static final int WIDTH = 1814;
    static final int HEIGHT = 1422;
    static final String MODEL_TITLE = "200 II";
    static final String SEGMENT_TITLE = "E-class";
    static final String BRAND_TITLE = "Audi";
    static final String COUNTRY_TITLE = "Germany";

    @BeforeAll
    static void setUp() {
        final Modification modification = new Modification();
        car = new Car(CAR_ID, modification);
        car.setModification(modification);
        modification.setTitle(MODIFICATION_TITLE);
        modification.setAcceleration(ACCELERATION);
        modification.setBodyStyle(BODY_STYLE);
        modification.setEngineType(ENGINE_TYPE);
        modification.setEngineDisplacement(ENGINE_DISPLACEMENT);
        modification.setFuelType(FUEL_TYPE);
        modification.setGearboxType(GEARBOX_TYPE);
        modification.setHp(HP);
        modification.setMaxSpeed(MAX_SPEED);
        modification.setWheelDriveType(WHEELDRIVE_TYPE);
        final Generation generation = new Generation();
        modification.setGeneration(generation);
        generation.setTitle(GENERATION_TITLE);
        generation.setYears(YEARS);
        generation.setLength(LENGTH);
        generation.setWidth(WIDTH);
        generation.setHeight(HEIGHT);
        final Model model = new Model();
        generation.setModel(model);
        model.setTitle(MODEL_TITLE);
        final Model.Segment segment = new Model.Segment();
        model.setSegment(segment);
        segment.setTitle(SEGMENT_TITLE);
        final Brand brand = new Brand();
        model.setBrand(brand);
        brand.setTitle(BRAND_TITLE);
        final Brand.Country country = new Brand.Country();
        brand.setCountry(country);
        country.setTitle(COUNTRY_TITLE);
    }

    @Test
    void convertToCarInfoTest() {
        final CarInfo info = CarConverter.convert(car, false);
        Assertions.assertEquals(CAR_ID, info.getId());
        Assertions.assertEquals(MODIFICATION_TITLE, info.getModification());
        Assertions.assertEquals(GENERATION_TITLE, info.getGeneration());
        Assertions.assertEquals(BRAND_TITLE, info.getBrand());
        Assertions.assertEquals(MODEL_TITLE, info.getModel());
        Assertions.assertEquals(SEGMENT_TITLE, info.getSegment());
        Assertions.assertEquals(COUNTRY_TITLE, info.getCountry());
    }

    @Test
    void convertToCarFullInfoTest() {
        final CarFullInfo info = (CarFullInfo) CarConverter.convert(car, true);
        Assertions.assertEquals(CAR_ID, info.getId());
        Assertions.assertEquals(MODIFICATION_TITLE, info.getModification());
        Assertions.assertEquals(GENERATION_TITLE, info.getGeneration());
        Assertions.assertEquals(BRAND_TITLE, info.getBrand());
        Assertions.assertEquals(MODEL_TITLE, info.getModel());
        Assertions.assertEquals(SEGMENT_TITLE, info.getSegment());
        Assertions.assertEquals(COUNTRY_TITLE, info.getCountry());
        Assertions.assertEquals(LENGTH, info.getBody().getLength());
        Assertions.assertEquals(WIDTH, info.getBody().getWidth());
        Assertions.assertEquals(HEIGHT, info.getBody().getHeight());
        Assertions.assertEquals(BODY_STYLE, info.getBody().getStyle());
        Assertions.assertEquals(FUEL_TYPE.getValue(), info.getEngine().getType());
        Assertions.assertEquals(ENGINE_TYPE.name(), info.getEngine().getCylinders());
        Assertions.assertEquals(ENGINE_DISPLACEMENT, info.getEngine().getDisplacement());
        Assertions.assertEquals(HP, info.getEngine().getHorsepower());


    }
}