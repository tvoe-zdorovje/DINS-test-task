package com.rincentral.test.services;

import com.rincentral.test.models.external.ExternalBrand;
import com.rincentral.test.models.external.ExternalCar;
import com.rincentral.test.models.external.ExternalCarInfo;

import java.util.List;

public interface ExternalCarsApiService {
    
     List<ExternalCar> loadAllCars();

     List<ExternalCar> loadCars(int page);

     ExternalCarInfo loadCarInformationById(int id);

     List<ExternalBrand> loadAllBrands();

     List<ExternalBrand> loadBrands(int page);

     ExternalBrand loadBrandById(int id);
}
