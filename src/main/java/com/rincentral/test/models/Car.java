package com.rincentral.test.models;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "CAR")
public class Car extends AbstractBaseEntity {

    @ManyToOne(targetEntity = Modification.class, fetch = FetchType.LAZY)
    Modification modification;

    public Car(Integer id, Modification modification) {
        super(id);
        this.modification = modification;
    }
}
