package com.xmonster.howtaxing_admin.dto.calculation_process;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CalculationProcessSaveRequest {
    private String calcType;
    private String branchNo;
    private Integer selectNo;
    private String calcName;
    private String branchName;
    private String selectContent;
    private String variableData;
    private String dataMethod;
    private boolean hasNextBranch;
    private String nextBranchNo;
    private String taxRateCode;
    private String dedCode;
    private String remark;
}
