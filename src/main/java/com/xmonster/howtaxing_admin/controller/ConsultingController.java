package com.xmonster.howtaxing_admin.controller;

import com.xmonster.howtaxing_admin.dto.consulting.*;
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
                                                @RequestParam(value="currentWeekStartDate", required = false) String currentWeekStartDateStr,
                                                @RequestParam(value="selectedDate", required = false) String selectedDateStr,
                                                @RequestParam(value="action", required = false) String action) throws Exception {
        log.info(">> [Controller]ConsultingController getConsultingScheduleWeekInfo - 상담일정 주정보 조회");
        return consultingService.getConsultingScheduleWeekInfo(consultantId, currentWeekStartDateStr, selectedDateStr, action);
    }

    // 상담일정 일자정보 조회
    @GetMapping("/consulting/dateInfo")
    public Object getConsultingScheduleDateInfo(@RequestParam(value="consultantId") Long consultantId,
                                                @RequestParam(value="searchDate") String searchDateStr) throws Exception {
        log.info(">> [Controller]ConsultingController getConsultingScheduleDateInfo - 상담일정 일자정보 조회");
        return consultingService.getConsultingScheduleDateInfo(consultantId, searchDateStr);
    }

    // 상담일정 정보 저장
    @PostMapping("/consulting/saveInfo")
    public Object saveConsultingScheduleInfo(@RequestBody ConsultingScheduleDateInfoSaveRequest consultingScheduleDateInfoSaveRequest) throws Exception {
        log.info(">> [Controller]ConsultingController saveConsultingScheduleInfo - 상담일정 정보 저장");
        return consultingService.saveConsultingScheduleInfo(consultingScheduleDateInfoSaveRequest);
    }

    // 상담예약정보 목록 조회
    @PostMapping("/consulting/reservationList")
    public Object getConsultingReservationInfoList(@RequestBody ConsultingReservationInfoListRequest consultingReservationInfoListRequest) throws Exception {
        log.info(">> [Controller]ConsultingController getConsultingReservationInfoList - 상담예약정보 목록 조회");
        return consultingService.getConsultingReservationInfoList(consultingReservationInfoListRequest);
    }

    // 상담예약정보 상세 조회
    @GetMapping("/consulting/reservationDetail")
    public Object getConsultingReservationInfoDetail(@RequestParam Long consultingReservationId) throws Exception {
        log.info(">> [Controller]ConsultingController getConsultingReservationInfoDetail - 상담예약정보 상세 조회");
        return consultingService.getConsultingReservationInfoDetail(consultingReservationId);
    }

    // 상담 상태 변경
    @PutMapping("/consulting/changeConsultingStatus")
    public Object changeConsultingStatus(@RequestBody ConsultingStatusChangeRequest consultingStatusChangeRequest) throws Exception {
        log.info(">> [Controller]ConsultingController changeConsultingStatus - 상담 상태 변경");
        return consultingService.changeConsultingStatus(consultingStatusChangeRequest);
    }

    // 상담 데이터 저장
    @PutMapping("/consulting/saveConsultingData")
    public Object saveConsultingData(@RequestBody ConsultingDataSaveRequest consultingDataSaveRequest) throws Exception {
        log.info(">> [Controller]ConsultingController saveConsultingData - 상담 데이터 저장");
        return consultingService.saveConsultingData(consultingDataSaveRequest);
    }
}
