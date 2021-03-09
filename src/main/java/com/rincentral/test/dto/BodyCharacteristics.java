package com.rincentral.test.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BodyCharacteristics {
    private int length;
    private int width;
    private int height;
    private String style;
}
