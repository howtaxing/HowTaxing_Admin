package com.xmonster.howtaxing_admin.controller;

import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleDateInfoSaveRequest;
import com.xmonster.howtaxing_admin.service.ConsultingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ConsultingController {
    private final ConsultingService consultingService;

    // 상담일정 주정보 조회
    @GetMapping("/consulting/weekInfo")
    public Object getConsultingScheduleWeekInfo(@RequestParam(value="consultantId") Long consultantId,
                                                @RequestParam(value="currentWeekStartDate", required = false) String currentWeekStartDate,
                                                @RequestParam(value="action", required = false) String action) throws Exception {
        log.info(">> [Controller]ConsultingController getConsultingScheduleWeekInfo - 상담일정 주정보 조회");
        return consultingService.getConsultingScheduleWeekInfo(consultantId, currentWeekStartDate, action);
    }

    // 상담일정 일자정보 조회
    @GetMapping("/consulting/dateInfo")
    public Object getConsultingScheduleDateInfo(@RequestParam(value="consultantId") Long consultantId,
                                                @RequestParam(value="searchDate") String searchDate) throws Exception {
        log.info(">> [Controller]ConsultingController getConsultingScheduleDateInfo - 상담일정 일자정보 조회");
        return consultingService.getConsultingScheduleDateInfo(consultantId, searchDate);
    }

    // 상담일정 정보 저장
    @PostMapping("/consulting/saveInfo")
    public Object saveConsultingScheduleInfo(@RequestBody ConsultingScheduleDateInfoSaveRequest consultingScheduleDateInfoSaveRequest) throws Exception {
        log.info(">> [Controller]ConsultingController saveConsultingScheduleInfo - 상담일정 정보 저장");
        return consultingService.saveConsultingScheduleInfo(consultingScheduleDateInfoSaveRequest);
    }
}
