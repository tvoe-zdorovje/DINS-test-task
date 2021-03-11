package com.rincentral.test.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "BRAND")
public class Brand extends AbstractBaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(targetEntity = Country.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Country country;

    @Getter
    @Setter
    @ToString(callSuper = true)
    @Entity
    @Table(name = "COUNTRY")
    public static class Country extends AbstractBaseEntity {

        @Column(name = "title", nullable = false)
        private String title;
    }
}
