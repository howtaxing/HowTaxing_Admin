package com.xmonster.howtaxing_admin.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class CalculationProcessId implements Serializable {
    private String calcType;    // 계산구분
    private String branchNo;    // 분기번호
    private Integer selectNo;   // 선택번호
}
