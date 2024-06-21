package com.xmonster.howtaxing_admin.dto.common_code;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CommonCodeSaveRequest {
    private String mainCtId;
    private String subCtId;
    private String mainCtName;
    private String subCtName;
    private String constYn;
    private String constVal;
    private String unit;
    private String remark;
}
