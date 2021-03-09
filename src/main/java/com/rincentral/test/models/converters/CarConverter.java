package com.rincentral.test.models.converters;

import com.rincentral.test.dto.BodyCharacteristics;
import com.rincentral.test.dto.CarFullInfo;
import com.rincentral.test.dto.CarInfo;
import com.rincentral.test.dto.EngineCharacteristics;
import com.rincentral.test.models.Car;
import com.rincentral.test.models.Generation;
import com.rincentral.test.models.Model;
import com.rincentral.test.models.Modification;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CarConverter {

    public static List<CarInfo> convert(Collection<Car> cars, boolean isFull) {
        return cars.stream().map(car -> convert(car, isFull)).collect(Collectors.toList());
    } 
    
    public static CarInfo convert(Car car, boolean isFull) {
        final Modification modification = car.getModification();
        final Generation generation = modification.getGeneration();
        final Model model = generation.getModel();
        final CarInfo carInfo = CarInfo.builder()
                .id(car.getId())
                .modification(modification.getTitle())
                .generation(generation.getTitle())
                .model(model.getTitle())
                .brand(model.getBrand().getTitle())
                .segment(model.getSegment().getTitle())
                .country(model.getBrand().getCountry().getTitle())
                .build();
        if (!isFull) {
            return carInfo;
        }
        final EngineCharacteristics engineCharacteristics = new EngineCharacteristics(
                modification.getFuelType().getValue(),
                modification.getEngineType().name(),
                modification.getEngineDisplacement(),
                modification.getHp()
        );
        final BodyCharacteristics bodyCharacteristics = new BodyCharacteristics(
                generation.getLength(),
                generation.getWidth(),
                generation.getHeight(),
                modification.getBodyStyle()
        );
        return new CarFullInfo(
                carInfo,
                engineCharacteristics,
                bodyCharacteristics
        );
    }
}
