package com.xmonster.howtaxing_admin.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CommonCodeId implements Serializable {
    private String mainCtId;        // 대분류코드
    private String subCtId;         // 소분류코드
}
