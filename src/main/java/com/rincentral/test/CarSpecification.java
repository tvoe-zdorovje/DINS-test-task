package com.rincentral.test;

import com.rincentral.test.models.*;
import com.rincentral.test.models.params.CarRequestParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.*;

@RequiredArgsConstructor
public class CarSpecification implements Specification<Car> {

    private final CarRequestParameters parameters;

    @Override
    public Predicate toPredicate(Root<Car> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
        final Join<Car, Modification> modification = root.join("modification");
        final Join<Modification, Generation> generation = modification.join("generation");
        final Join<Generation, Model> model = generation.join("model");
        final Join<Model, Brand> brand = model.join("brand");
        final Join<Model, Model.Segment> segment = model.join("segment");
        final Join<Brand, Brand.Country> country = brand.join("country");

        Predicate predicate = cb.conjunction();
        if (parameters.getSearch() != null) {
            predicate.getExpressions().add(
                    cb.or(
                            cb.equal(model.get("title"), parameters.getSearch()),
                            cb.equal(generation.get("title"), parameters.getSearch()),
                            cb.equal(modification.get("title"), parameters.getSearch())
                    )
            );
        }
        if (parameters.getYear() != null) {
            final Path<String> yearsPath = generation.get("years");
            predicate.getExpressions().add(
                    cb.and(
                            cb.le(cb.substring(yearsPath, 1, 4).as(Integer.class), parameters.getYear()),
                            cb.or(
                                    cb.equal(cb.substring(yearsPath, 6), "present"),
                                    cb.ge(cb.substring(yearsPath, 6).as(Integer.class), parameters.getYear())
                            )
                    )
            );
        }
        if (parameters.getCountry() != null) {
            predicate.getExpressions().add(cb.equal(country.get("title"), parameters.getCountry()));
        }
        if (parameters.getSegment() != null) {
            predicate.getExpressions().add(cb.equal(segment.get("title"), parameters.getSegment()));
        }
        if (parameters.getMinEngineDisplacement() != null) {
            predicate.getExpressions().add(
                    cb.ge(modification.get("engineDisplacement"), parameters.getMinEngineDisplacement() * 1000)
            );
        }
        if (parameters.getMinEngineHorsepower() != null) {
            predicate.getExpressions().add(cb.ge(modification.get("hp"), parameters.getMinEngineHorsepower()));
        }
        if (parameters.getMinMaxSpeed() != null) {
            predicate.getExpressions().add(cb.ge(modification.get("maxSpeed"), parameters.getMinMaxSpeed()));
        }

        if (parameters.getBodyStyle() != null) {
            predicate.getExpressions().add(cb.equal(modification.get("bodyStyle"), parameters.getBodyStyle()));
        }

        return predicate;
    }


}
