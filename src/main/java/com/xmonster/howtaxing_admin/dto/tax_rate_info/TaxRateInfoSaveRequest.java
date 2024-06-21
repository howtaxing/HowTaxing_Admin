package com.xmonster.howtaxing_admin.dto.tax_rate_info;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaxRateInfoSaveRequest {
    private String taxRateCode;
    private String taxRateName;
    private String constYn;
    private String usedFunc;
    private Long basePrice;
    private String taxRate1;
    private String addTaxRate1;
    private String taxRate2;
    private String addTaxRate2;
    private String remark;
}
