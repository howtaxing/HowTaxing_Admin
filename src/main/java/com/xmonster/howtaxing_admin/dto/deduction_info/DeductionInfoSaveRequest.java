package com.xmonster.howtaxing_admin.dto.deduction_info;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DeductionInfoSaveRequest {
    private String dedCode;
    private String dedContent;
    private String unit;
    private Float unitDedRate;
    private Integer limitYear;
    private Float limitDedRate;
    private String remark;
}
