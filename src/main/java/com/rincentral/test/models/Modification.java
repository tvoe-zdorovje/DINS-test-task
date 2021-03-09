package com.rincentral.test.models;

import com.rincentral.test.models.enums.EngineType;
import com.rincentral.test.models.enums.FuelType;
import com.rincentral.test.models.enums.GearboxType;
import com.rincentral.test.models.enums.WheelDriveType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "MODIFICATION")
public class Modification extends AbstractBaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(targetEntity = Generation.class, fetch = FetchType.LAZY)
    private Generation generation;

    @Column(name = "fuel_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private FuelType fuelType;

    @Column(name = "engine_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private EngineType engineType;

    private Integer engineDisplacement;

    @Column(name = "gearbox_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private GearboxType gearboxType;

    @Column(name = "wheeldrive_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private WheelDriveType wheelDriveType;

    @Column(name = "body_style")
    private String bodyStyle;

    @Column(name = "acceleration")
    private float acceleration;

    @Column(name = "hp")
    private Integer hp;

    @Column(name = "max_speed")
    private Integer maxSpeed;
}
