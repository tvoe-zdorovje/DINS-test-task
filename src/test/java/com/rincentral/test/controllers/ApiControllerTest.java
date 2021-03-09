package com.rincentral.test.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.rincentral.test.dto.CarFullInfo;
import com.rincentral.test.dto.CarInfo;
import com.rincentral.test.models.enums.EngineType;
import com.rincentral.test.models.enums.FuelType;
import com.rincentral.test.models.enums.GearboxType;
import com.rincentral.test.models.enums.WheelDriveType;
import com.rincentral.test.services.ExternalCarsApiService;
import org.assertj.core.api.Assertions;
import org.assertj.core.data.Offset;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ApiControllerTest {

    private static final String URL = "/api/";

    private MockMvc mockMvc;
    private final ExternalCarsApiService externalCarsApiService = new ExternalCarsApiService();

    @Autowired
    private WebApplicationContext webApplicationContext;

    @PostConstruct
    private void postConstruct() {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .addFilter(new CharacterEncodingFilter("utf-8", true))
                .build();
    }

    public ResultActions perform(MockHttpServletRequestBuilder builder) throws Exception {
        return mockMvc.perform(builder);
    }

    @Test
    void getCarsTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "cars"))
                .andExpect(status().isOk())
                .andReturn();
        final List<CarInfo> result = convertResultList(mvcResult, CarInfo.class);
        assertThat(result.size()).isEqualTo(externalCarsApiService.loadAllCars().size());
    }

    @Test
    void getCarsFilteredTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "cars?country=England&segment=E-segment&" +
                "minEngineDisplacement=4.0&minEngineHorsepower=250&minMaxSpeed=200&search=5&year=2010&isFull=true"))
                .andExpect(status().isOk())
                .andReturn();
        final List<CarFullInfo> result = convertResultList(mvcResult, CarFullInfo.class);
        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getEngine()).isNotNull();
        assertThat(result.get(0).getBody()).isNotNull();
    }

    @Test
    void getCarsNotFoundTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "cars?country=Narnia"))
                .andExpect(status().isOk())
                .andReturn();
        final List<CarFullInfo> result = convertResultList(mvcResult, CarFullInfo.class);
        assertThat(result).isEmpty();
    }

    @Test
    void getCarsBadRequestTest() throws Exception {
        perform(get(URL + "cars?minMaxSpeed=over100500"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getFuelTypesTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "fuel-types"))
                .andExpect(status().isOk())
                .andReturn();
        final List<String> result = convertResultList(mvcResult, String.class);
        assertThat(result.size()).isEqualTo(FuelType.values().length);
        Assertions.assertThat(result)
                .contains(Arrays.stream(FuelType.values()).map(FuelType::getValue).toArray(String[]::new));
    }

    @Test
    void getBodyStylesTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "body-styles"))
                .andExpect(status().isOk())
                .andReturn();
        final List<String> result = convertResultList(mvcResult, String.class);
        assertThat(result).isNotEmpty();
        Assertions.assertThat(result)
                .contains("Sedan", "Hatchback", "Cabriolet", "Wagon");
    }

    @Test
    void getEngineTypesTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "engine-types"))
                .andExpect(status().isOk())
                .andReturn();
        final List<EngineType> result = convertResultList(mvcResult, EngineType.class);
        assertThat(result.size()).isEqualTo(EngineType.values().length);
        Assertions.assertThat(result)
                .contains(EngineType.values());
    }

    @Test
    void getWheelDrivesTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "wheel-drives"))
                .andExpect(status().isOk())
                .andReturn();
        final List<WheelDriveType> result = convertResultList(mvcResult, WheelDriveType.class);
        assertThat(result.size()).isEqualTo(WheelDriveType.values().length);
        Assertions.assertThat(result)
                .contains(WheelDriveType.values());
    }

    @Test
    void getGearboxTypesTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "gearboxes"))
                .andExpect(status().isOk())
                .andReturn();
        final List<String> result = convertResultList(mvcResult, String.class);
        assertThat(result.size()).isEqualTo(GearboxType.values().length);
        Assertions.assertThat(result)
                .contains(Arrays.stream(GearboxType.values()).map(GearboxType::getValue).toArray(String[]::new));
    }

    @Test
    void getAvgMaxSpeedByModelTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "max-speed?model=Golf"))
                .andExpect(status().isOk())
                .andReturn();
        final Double result = convertResult(mvcResult, Double.class);
        assertThat(result).isEqualTo(218.0, Offset.offset(0.1));
    }

    @Test
    void getAvgMaxSpeedByBrandTest() throws Exception {
        final MvcResult mvcResult = perform(get(URL + "max-speed?brand=Audi"))
                .andExpect(status().isOk())
                .andReturn();
        final Double result = convertResult(mvcResult, Double.class);
        assertThat(result).isEqualTo(243.8, Offset.offset(0.1));
    }

    @Test
    void getAvgMaxSpeedByNoneTest() throws Exception {
        perform(get(URL + "max-speed"))
                .andExpect(status().isNotFound());
        //      .andExpect(status().isBadRequest()) // see ControllerExceptionHandler.notFoundException(..)
    }

    @Test
    void getAvgMaxSpeedByBothTest() throws Exception {
        perform(get(URL + "max-speed?model=A6&brand=Audi"))
                .andExpect(status().isBadRequest());
    }

    @Autowired
    private ObjectMapper objectMapper;

    private <T> T convertResult(MvcResult result, Class<T> type) throws JsonProcessingException, UnsupportedEncodingException {
        final String json = result.getResponse().getContentAsString();
        return objectMapper.readValue(json, type);
    }

    private <T> List<T> convertResultList(MvcResult result, Class<T> type) throws IOException {
        final String json = result.getResponse().getContentAsString();
        ObjectReader reader = objectMapper.readerFor(type);
        return reader.<T>readValues(json).readAll();
    }

}