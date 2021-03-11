package com.rincentral.test.services;

import com.rincentral.test.models.*;
import com.rincentral.test.models.external.ExternalBrand;
import com.rincentral.test.models.external.ExternalCar;
import com.rincentral.test.models.external.ExternalCarInfo;
import com.rincentral.test.repositories.CarRepository;
import com.rincentral.test.repositories.ModificationRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ExternalDataParsingService {

    private static final Logger LOGGER = LogManager.getLogger(ExternalDataParsingService.class);

    private static final AtomicInteger GLOBAL_SEQUENCE = new AtomicInteger(100000);

    private final ExternalCarsApiService externalCarsApiService;
    private final ModificationRepository modRepository;
    private final CarRepository carRepository;

    public ExternalDataParsingService(ExternalCarsApiService externalCarsApiService,
                                      ModificationRepository modRepository,
                                      CarRepository carRepository) {
        this.externalCarsApiService = externalCarsApiService;
        this.modRepository = modRepository;
        this.carRepository = carRepository;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void parse() {
        LOGGER.info("Start parsing external data");

        List<ExternalCar> extCars;
        int numOfPages = 0, totalEntities = 0, expectedEntities = 0;
        for (; (extCars = externalCarsApiService.loadCars(numOfPages)).size() > 0; numOfPages++) {
            expectedEntities += extCars.size();
            totalEntities += parsePage(extCars);
        }
        LOGGER.info("{} car pages ({}/{} entities) successfully parsed",
                numOfPages, totalEntities, expectedEntities);
    }

    // @Transaction does not work!
    private int parsePage(List<ExternalCar> list) {
        int count = 0;
        for (ExternalCar externalCar : list) {
            Optional<Modification> optional = modRepository.find(
                    externalCar.getModification(),
                    externalCar.getGeneration(),
                    externalCar.getModel(),
                    externalCar.getBrandId());
            Car car = new Car(
                    externalCar.getId(),
                    optional.isEmpty() ? createModification(externalCar) : optional.get());
            carRepository.save(car);
            count++;
        }
        carRepository.flush();
        return count;
    }

    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Create new Modification entity and all absent child entities.
     *
     * @param extCar - external car short info.
     * @return created modification.
     * @see ExternalDataParsingService#getGeneration(ExternalCarInfo)
     * @see ExternalDataParsingService#getModel(String, String, Integer)
     * @see ExternalDataParsingService#getBrand(Integer)
     * @see ExternalDataParsingService#getSegment(String)
     * @see ExternalDataParsingService#getCountry(String)
     **/
    private Modification createModification(ExternalCar extCar) {
        LOGGER.info("Modification {} {} (brand id={}) not found. Create new for car with id = {}",
                extCar.getGeneration(), extCar.getModification(), extCar.getBrandId(), extCar.getId());
        ExternalCarInfo externalCarInfo = externalCarsApiService.loadCarInformationById(extCar.getId());
        Modification modification = new Modification();
        modification.setId(GLOBAL_SEQUENCE.getAndIncrement());
        modification.setTitle(externalCarInfo.getModification());
        modification.setHp(externalCarInfo.getHp());
        modification.setMaxSpeed(externalCarInfo.getMaxSpeed());
        modification.setGearboxType(externalCarInfo.getGearboxType());
        modification.setWheelDriveType(externalCarInfo.getWheelDriveType());
        modification.setFuelType(externalCarInfo.getFuelType());
        modification.setEngineType(externalCarInfo.getEngineType());
        modification.setEngineDisplacement(externalCarInfo.getEngineDisplacement());
        modification.setAcceleration(externalCarInfo.getAcceleration().floatValue());
        modification.setBodyStyle(externalCarInfo.getBodyStyle());
        modification.setGeneration(getGeneration(externalCarInfo));
        return modification;
    }

    @SuppressWarnings("unchecked")
    private Generation getGeneration(ExternalCarInfo externalCarInfo) {
        final List<Generation> resultList = entityManager
                .createQuery("SELECT g FROM Generation g " +
                        "WHERE g.title=?1 AND g.model.title=?2 AND g.model.brand.id=?3")
                .setParameter(1, externalCarInfo.getGeneration())
                .setParameter(2, externalCarInfo.getModel())
                .setParameter(3, externalCarInfo.getBrandId())
                .getResultList();
        Generation generation = DataAccessUtils.singleResult(resultList);
        if (generation != null) {
            return generation;
        }
        generation = new Generation();
        generation.setId(GLOBAL_SEQUENCE.getAndIncrement());
        generation.setTitle(externalCarInfo.getGeneration());
        generation.setLength(externalCarInfo.getBodyLength());
        generation.setWidth(externalCarInfo.getBodyWidth());
        generation.setHeight(externalCarInfo.getBodyHeight());
        generation.setYears(externalCarInfo.getYearsRange());
        generation.setModel(getModel(
                externalCarInfo.getModel(), externalCarInfo.getSegment(), externalCarInfo.getBrandId()
        ));

        LOGGER.debug("Generation {} created.", externalCarInfo.getGeneration());
        return generation;
    }

    @SuppressWarnings("unchecked")
    private Model getModel(String modelTitle, String segmentTitle, Integer brandId) {
        final List<Model> resultList = entityManager
                .createQuery("SELECT m FROM Model m WHERE m.title=?1 AND m.brand.id=?2")
                .setParameter(1, modelTitle)
                .setParameter(2, brandId)
                .getResultList();
        Model model = DataAccessUtils.singleResult(resultList);
        if (model != null) {
            return model;
        }
        model = new Model();
        model.setId(GLOBAL_SEQUENCE.getAndIncrement());
        model.setTitle(modelTitle);
        model.setSegment(getSegment(segmentTitle));
        model.setBrand(getBrand(brandId));

        LOGGER.debug("Model {} created.", modelTitle);
        return model;
    }

    @SuppressWarnings("unchecked")
    private Model.Segment getSegment(String title) {
        final List<Model.Segment> resultList = entityManager
                .createQuery("SELECT DISTINCT m.segment FROM Model m WHERE m.segment.title=?1")
                .setParameter(1, title)
                .getResultList();
        Model.Segment segment = DataAccessUtils.singleResult(resultList);
        if (segment != null) {
            return segment;
        }
        segment = new Model.Segment();
        segment.setId(GLOBAL_SEQUENCE.getAndIncrement());
        segment.setTitle(title);

        LOGGER.debug("Segment {} created.", title);
        return segment;
    }

    @SuppressWarnings("unchecked")
    private Brand getBrand(Integer id) {
        final List<Brand> resultList = entityManager
                .createQuery("SELECT b FROM Brand b WHERE b.id=?1")
                .setParameter(1, id)
                .getResultList();
        Brand brand = DataAccessUtils.singleResult(resultList);
        if (brand != null) {
            return brand;
        }
        final ExternalBrand externalBrand = externalCarsApiService.loadBrandById(id);
        brand = new Brand();
        brand.setId(id);
        brand.setCountry(getCountry(externalBrand.getCountry()));
        brand.setTitle(externalBrand.getTitle());

        LOGGER.debug("Brand {} created.", externalBrand.getTitle());
        return brand;
    }

    @SuppressWarnings("unchecked")
    private Brand.Country getCountry(String title) {
        final List<Brand.Country> resultList = entityManager
                .createQuery("SELECT DISTINCT b.country FROM Brand b WHERE b.country.title=?1")
                .setParameter(1, title)
                .getResultList();
        Brand.Country country = DataAccessUtils.singleResult(resultList);
        if (country != null) {
            return country;
        }
        country = new Brand.Country();
        country.setId(GLOBAL_SEQUENCE.getAndIncrement());
        country.setTitle(title);

        LOGGER.debug("Country {} created.", title);
        return country;
    }


}