package com.rincentral.test.models;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@Entity
@Table(name = "CAR")
public class Car extends AbstractBaseEntity {

    @ManyToOne(targetEntity = Modification.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Modification modification;

    public Car(Integer id, Modification modification) {
        super(id);
        this.modification = modification;
    }
}
