package com.xmonster.howtaxing_admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "page/login";
    }

    @GetMapping("/moveCalculationProcess")
    public String moveCalculationProcess(){
        return "page/calculation_process";
    }

    @GetMapping("/moveCommonCode")
    public String moveCommonCode(){
        return "page/common_code";
    }

    @GetMapping("/moveTaxRateInfo")
    public String moveTaxRateInfo(){
        return "page/tax_rate_info";
    }

    @GetMapping("/moveDeductionInfo")
    public String moveDeductionInfo(){
        return "page/deduction_info";
    }

    @GetMapping("/moveLogin")
    public String moveLogin(){
        return "page/login";
    }
}
