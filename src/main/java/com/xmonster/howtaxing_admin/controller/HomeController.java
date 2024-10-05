package com.xmonster.howtaxing_admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.ui.Model;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "page/common/login";
    }

    @GetMapping("/moveCalculationProcess")
    public String moveCalculationProcess(){
        return "page/data_management/calculation_process";
    }

    @GetMapping("/moveCommonCode")
    public String moveCommonCode(){
        return "page/data_management/common_code";
    }

    @GetMapping("/moveTaxRateInfo")
    public String moveTaxRateInfo(){
        return "page/data_management/tax_rate_info";
    }

    @GetMapping("/moveDeductionInfo")
    public String moveDeductionInfo(){
        return "page/data_management/deduction_info";
    }


    @GetMapping("/moveConsultingScheduleManagement")
    public String moveConsultingScheduleManagement(){
        return "page/consulting_management/consulting_schedule_management";
    }

    @GetMapping("/moveConsultingReservationList")
    public String moveConsultingReservationList(){
        return "page/consulting_management/consulting_reservation_list";
    }

    @GetMapping("/moveConsultingReservationDetail")
    public String moveConsultingReservationDetail(Model model, @RequestParam String consultingReservationId){
        model.addAttribute("consultingReservationId", consultingReservationId);
        return "page/consulting_management/consulting_reservation_detail";
    }

    @GetMapping("/moveLogin")
    public String moveLogin(){
        return "page/common/login";
    }
}
