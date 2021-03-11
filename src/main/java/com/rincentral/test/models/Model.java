package com.rincentral.test.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "MODEL")
public class Model extends AbstractBaseEntity {

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(targetEntity = Brand.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Brand brand;

    @ManyToOne(targetEntity = Segment.class, fetch = FetchType.LAZY, cascade = CascadeType.MERGE)
    private Segment segment;

    @Getter
    @Setter
    @ToString(callSuper = true)
    @Entity
    @Table(name = "SEGMENT")
    public static class Segment extends AbstractBaseEntity {

        @Column(name = "title", nullable = false)
        private String title;
    }
}
