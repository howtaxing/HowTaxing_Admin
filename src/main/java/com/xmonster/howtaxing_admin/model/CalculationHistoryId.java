package com.xmonster.howtaxing_admin.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
@Embeddable
@Getter
@Builder
public class CalculationHistoryId implements Serializable {
    private Long calcHistoryId;         // 상담자ID
    private Integer detailHistorySeq;   // 상세이력번호
}
