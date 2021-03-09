package com.rincentral.test.models;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Setter
@ToString(callSuper = true)
@Entity
@Table(name = "GENERATION")
public class Generation extends AbstractBaseEntity{

    @Column(name = "title", nullable = false)
    private String title;

    @ManyToOne(targetEntity = Model.class, fetch = FetchType.LAZY)
    private Model model;

    @Column(name = "years", nullable = false)
    private String years;

    @Column(name = "length", nullable = false)
    private Integer length;

    @Column(name = "width", nullable = false)
    private Integer width;

    @Column(name = "height", nullable = false)
    private Integer height;
}
