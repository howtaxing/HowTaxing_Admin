package com.xmonster.howtaxing_admin.model;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class CommonCode extends DateEntity implements Serializable {
    @EmbeddedId
    private CommonCodeId commonCodeId;

    private String mainCtName;      // 대분류명
    private String subCtName;       // 소분류명
    private String constYn;         // 상수여부
    private String constVal;        // 상수값
    private String unit;            // 단위
    private String remark;          // 비고
}
