package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.dto.common.ApiResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleDateInfoResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleInfoResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleInfoResponse.ConsultingDateInfo;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleInfoResponse.ConsultingAvailableTimeInfo;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleWeekInfoResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleWeekInfoResponse.ConsultingEachDateInfo;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleDateInfoResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleDateInfoResponse.ConsultingEachTimeInfo;
import com.xmonster.howtaxing_admin.model.ConsultingReservationInfo;
import com.xmonster.howtaxing_admin.model.ConsultingScheduleId;
import com.xmonster.howtaxing_admin.model.ConsultingScheduleManagement;
import com.xmonster.howtaxing_admin.repository.consulting.ConsultantInfoRepository;
import com.xmonster.howtaxing_admin.repository.consulting.ConsultingReservationInfoRepository;
import com.xmonster.howtaxing_admin.repository.consulting.ConsultingScheduleManagementRepository;
import com.xmonster.howtaxing_admin.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import static com.xmonster.howtaxing_admin.constant.CommonConstant.*;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class ConsultingService {
    private final ConsultingScheduleManagementRepository consultingScheduleManagementRepository;
    private final ConsultingReservationInfoRepository consultingReservationInfoRepository;
    private final ConsultantInfoRepository consultantInfoRepository;

    private final static String THIS_WEEK = "this";
    private final static String PREVIOUS_WEEK = "previous";
    private final static String NEXT_WEEK = "next";
    private final static String PREVIOUS = "previous";
    private final static String NEXT = "next";
    private final static String DATE = "date";

    // 상담일정 주정보 조회
    public Object getConsultingScheduleWeekInfo(Long consultantId, String currentWeekStartDate, String action) throws Exception{
        log.info(">> [Service]ConsultingService getConsultingScheduleWeekInfo - 상담일정 주정보 조회");

        LocalDate baseDate = LocalDate.now();

        if(currentWeekStartDate != null) {
            LocalDate curWeekStDt = LocalDate.parse(currentWeekStartDate, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if(PREVIOUS.equals(action)){
                baseDate = curWeekStDt.minusWeeks(1);
            }else if(NEXT.equals(action)){
                baseDate = curWeekStDt.plusWeeks(1);
            }
        }

        log.info("baseDate : " + baseDate);

        List<ConsultingEachDateInfo> consultingEachDateInfoList = getConsultingWeekInfo(baseDate);
        LocalDate startDate = consultingEachDateInfoList.get(0).getDate();
        LocalDate endDate = consultingEachDateInfoList.get(consultingEachDateInfoList.size()-1).getDate();

        String consultingWeekInfo = startDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " ~ " + endDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

        // 상담일정관리정보 DB조회
        List<ConsultingScheduleManagement> consultingScheduleManagementList
                = consultingScheduleManagementRepository.findByConsultantIdFromStartDateToEndDate(consultantId, startDate, endDate);

        // 예약가능여부 세팅
        if(consultingScheduleManagementList != null && !consultingScheduleManagementList.isEmpty()){
            if(startDate.equals(consultingScheduleManagementList.get(0).getConsultingScheduleId().getReservationDate())){
                for(ConsultingScheduleManagement consultingScheduleManagement : consultingScheduleManagementList){
                    LocalDate reservationDate = consultingScheduleManagement.getConsultingScheduleId().getReservationDate();

                    for(ConsultingEachDateInfo consultingEachDateInfo : consultingEachDateInfoList){
                        LocalDate consultingDate = consultingEachDateInfo.getDate();
                        if(consultingDate.equals(reservationDate)){
                            consultingEachDateInfo.setIsReservationAvailable(consultingScheduleManagement.getIsReservationAvailable());
                        }
                    }
                }
                /*for(int i=0; i<consultingScheduleManagementList.size(); i++){
                    ConsultingScheduleManagement consultingScheduleManagement = consultingScheduleManagementList.get(i);
                    if(consultingScheduleManagement.getIsReservationAvailable()){
                        consultingEachDateInfoList.get(i).setIsReservationAvailable(true);
                    }
                }*/
            }
        }

        // 선택여부 세팅
        if(!consultingEachDateInfoList.isEmpty()){
            for(int i=0; i<consultingEachDateInfoList.size(); i++){
                if(baseDate.equals(consultingEachDateInfoList.get(i).getDate())){
                    consultingEachDateInfoList.get(i).setIsSelected(true);
                }
            }
        }

        return ApiResponse.success(
                ConsultingScheduleWeekInfoResponse.builder()
                        .consultantId(consultantId)
                        .consultingWeekInfo(consultingWeekInfo)
                        .consultingEachDateInfoList(consultingEachDateInfoList)
                        .build());
    }

    // 상담일정 일자정보 조회
    public Object getConsultingScheduleDateInfo(Long consultantId, String searchDateStr) throws Exception {
        log.info(">> [Service]ConsultingService getConsultingScheduleWeekInfo - 상담일정 일자정보 조회");

        boolean isReservationAvailable = false;
        String reservationAvailableStartTimeHour = EMPTY;
        String reservationAvailableStartTimeMinute = EMPTY;
        String reservationAvailableEndTimeHour = EMPTY;
        String reservationAvailableEndTimeMinute = EMPTY;
        List<ConsultingEachTimeInfo> consultingEachTimeInfoList = null;

        LocalDate searchDate = LocalDate.parse(searchDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        log.info("[GGMANYAR]consultantId : " + consultantId);
        log.info("[GGMANYAR]searchDate : " + searchDate);

        ConsultingScheduleManagement consultingScheduleManagement =
                consultingScheduleManagementRepository.findByConsultingScheduleId(
                        ConsultingScheduleId.builder()
                                .consultantId(consultantId)
                                .reservationDate(searchDate)
                                .build());

        if(consultingScheduleManagement != null){
            isReservationAvailable = consultingScheduleManagement.getIsReservationAvailable();

            if(isReservationAvailable) {
                LocalTime reservationAvailableStartTime = consultingScheduleManagement.getReservationAvailableStartTime();
                LocalTime reservationAvailableEndTime = consultingScheduleManagement.getReservationAvailableEndTime();

                if(reservationAvailableStartTime != null && reservationAvailableEndTime != null){
                    reservationAvailableStartTimeHour = String.format("%02d", reservationAvailableStartTime.getHour());
                    reservationAvailableStartTimeMinute = String.format("%02d", reservationAvailableStartTime.getMinute());
                    reservationAvailableEndTimeHour = String.format("%02d", reservationAvailableEndTime.getHour());
                    reservationAvailableEndTimeMinute = String.format("%02d", reservationAvailableEndTime.getMinute());

                    Integer reservationTimeUnit = consultingScheduleManagement.getReservationTimeUnit();
                    if(reservationTimeUnit != null && reservationAvailableStartTime.isBefore(reservationAvailableEndTime)){
                        consultingEachTimeInfoList = new ArrayList<>();
                        LocalTime consultingTime = reservationAvailableStartTime;

                        while(!consultingTime.equals(reservationAvailableEndTime)){
                            consultingEachTimeInfoList.add(
                                    ConsultingEachTimeInfo.builder()
                                            .consultingTime(consultingTime.format(DateTimeFormatter.ofPattern("HH:mm")))
                                            .consultingTimeUnit(reservationTimeUnit)
                                            .reservationStatus(ONE) // 일단 상담시간에 예약대기:'1' 세팅(예약완료와 예약불가 값은 하단에서 세팅)
                                            .build());
                            consultingTime = consultingTime.plusMinutes(reservationTimeUnit);
                        }
                    }
                }

                // 선택한 상담자의 예약내역을 조회하여 이미 예약된 상담시간에 예약완료:'2' 세팅
                List<ConsultingReservationInfo> consultingReservationInfoList =
                        consultingReservationInfoRepository.findByReservationDate(consultantId, searchDate);

                if(consultingReservationInfoList !=null && !consultingReservationInfoList.isEmpty()){
                    for(ConsultingReservationInfo consultingReservationInfo : consultingReservationInfoList){
                        LocalTime reservationStartTime = consultingReservationInfo.getReservationStartTime();

                        if(consultingEachTimeInfoList != null && !consultingEachTimeInfoList.isEmpty()){
                            for(ConsultingEachTimeInfo consultingEachTimeInfo : consultingEachTimeInfoList){
                                LocalTime consultingTime = LocalTime.parse(consultingEachTimeInfo.getConsultingTime(), DateTimeFormatter.ofPattern("HH:mm"));
                                if(consultingTime.equals(reservationStartTime)){
                                    consultingEachTimeInfo.setReservationStatus(TWO);
                                }
                            }
                        }
                    }
                }

                // 선택한 상담자의 상담일정을 조회하여 상담불가인 상담시간에 예약불가:'3' 세팅
                String reservationUnavailableTimeStr = consultingScheduleManagement.getReservationUnavailableTime();
                String[] reservationUnavailableTimeArr = null;
                if(StringUtils.isNotBlank(reservationUnavailableTimeStr)){
                    reservationUnavailableTimeArr = reservationUnavailableTimeStr.split(",");
                }

                if(reservationUnavailableTimeArr != null){
                    for (String unavailableTime : reservationUnavailableTimeArr) {
                        if (consultingEachTimeInfoList != null && !consultingEachTimeInfoList.isEmpty()) {
                            for (ConsultingEachTimeInfo consultingEachTimeInfo : consultingEachTimeInfoList) {
                                String consultingTime = StringUtils.defaultString(consultingEachTimeInfo.getConsultingTime());
                                if (consultingTime.equals(unavailableTime)) {
                                    consultingEachTimeInfo.setReservationStatus(THREE);
                                }
                            }
                        }
                    }
                }
            }
        }

        return ApiResponse.success(
                ConsultingScheduleDateInfoResponse.builder()
                        .isReservationAvailable(isReservationAvailable)
                        .reservationAvailableStartTimeHour(reservationAvailableStartTimeHour)
                        .reservationAvailableStartTimeMinute(reservationAvailableStartTimeMinute)
                        .reservationAvailableEndTimeHour(reservationAvailableEndTimeHour)
                        .reservationAvailableEndTimeMinute(reservationAvailableEndTimeMinute)
                        .consultingEachTimeInfoList(consultingEachTimeInfoList)
                        .build());
    }

    // 상담일정정보 조회
    public Object getConsultingScheduleInfo(Long consultantId, String searchWeek, LocalDate searchDate) throws Exception {
        log.info(">> [Service]ConsultingService getConsultingScheduleInfo - 상담일정정보 조회");

        //validationCheckForGetConsultingScheduleInfo(searchWeek, searchDate);

        log.info("상담일정정보 조회 요청 : " + consultantId + ", " + searchWeek + ", " + searchDate);

        /*
         * 상담일정정보 조회
         *
         * REQUEST
         * - 조회구분(1:상담일자정보조회, 2:상담시간정보조회)
         *
         * RESPONSE
         * - 상담 날짜 선택 범위(ex.2024.09.04 ~ 2024.09.10)
         * - 상담 주 날짜값 7개(ex.04,05,06,07,08,09,10)
         * - 상담 주 일자 데이터 7개(ex.2024-09-04,2024-09-05,2024-09-06,2024-09-07,2024-09-08,2024-09-09,2024-09-10)
         * - 예약가능여부 값(여,부)
         * - 상담시간설정 데이터(ex.09:00,17:30)
         */

        LocalDate baseDate = null;
        String consultingWeekInfo = EMPTY;
        List<ConsultingDateInfo> consultingDateInfoResponseList = null;
        boolean isReservationAvailable = false;
        String reservationAvailableStartTimeHour = EMPTY;
        String reservationAvailableStartTimeMinute = EMPTY;
        String reservationAvailableEndTimeHour = EMPTY;
        String reservationAvailableEndTimeMinute = EMPTY;
        List<ConsultingAvailableTimeInfo> consultingAvailableTimeInfoResponseList = null;

        // 이번주 정보 조회
        if(EMPTY.equals(searchWeek) || THIS_WEEK.equals(searchWeek)){
            /**
             * 1. 상담 날짜 선택 범위
             *  1) 오늘 날짜
             *  2) 오늘 날짜가 무슨 요일 인지 확인
             *  3) 오늘 날짜가 속한 '주'를 정리
             *  4)
             */

            // 특정일자 조회 X
            if(searchDate == null){
                baseDate = LocalDate.now();
                consultingDateInfoResponseList = getConsultingWeekInfo2(baseDate);

                LocalDate startDate = consultingDateInfoResponseList.get(0).getDate();
                LocalDate endDate = consultingDateInfoResponseList.get(consultingDateInfoResponseList.size()-1).getDate();

                consultingWeekInfo = startDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd")) + " ~ " + endDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

                // 상담일정관리정보 DB조회
                List<ConsultingScheduleManagement> consultingScheduleManagementList =
                        consultingScheduleManagementRepository.findByConsultantIdFromStartDateToEndDate(consultantId, startDate, endDate);

                if(consultingScheduleManagementList != null){
                    if(consultingDateInfoResponseList.size() == consultingScheduleManagementList.size() &&
                            startDate.equals(consultingScheduleManagementList.get(0).getConsultingScheduleId().getReservationDate())){
                        for(int i=0; i<consultingDateInfoResponseList.size(); i++){
                            // 예약가능여부 세팅
                            consultingDateInfoResponseList.get(i).setIsReservationAvailable(consultingScheduleManagementList.get(i).getIsReservationAvailable());

                            // 선택여부 세팅
                            if(baseDate.equals(consultingDateInfoResponseList.get(i).getDate())){
                                consultingDateInfoResponseList.get(i).setIsSelected(true);
                            }
                        }
                    }
                    for(ConsultingScheduleManagement consultingScheduleManagement : consultingScheduleManagementList){
                        LocalDate date = consultingScheduleManagement.getConsultingScheduleId().getReservationDate();

                    }
                }
            }
            // 특정일자 조회 O
            else{
                baseDate = searchDate;
            }

            ConsultingScheduleManagement consultingScheduleManagement =
                    consultingScheduleManagementRepository.findByConsultingScheduleId(
                            ConsultingScheduleId.builder()
                                    .consultantId(consultantId)
                                    .reservationDate(baseDate)
                                    .build());

            if(consultingScheduleManagement != null){
                isReservationAvailable = consultingScheduleManagement.getIsReservationAvailable();
                reservationAvailableStartTimeHour = String.format("%02d", consultingScheduleManagement.getReservationAvailableStartTime().getHour());
                reservationAvailableStartTimeMinute = String.format("%02d", consultingScheduleManagement.getReservationAvailableStartTime().getMinute());
                reservationAvailableEndTimeHour = String.format("%02d", consultingScheduleManagement.getReservationAvailableEndTime().getHour());
                reservationAvailableEndTimeMinute = String.format("%02d", consultingScheduleManagement.getReservationAvailableEndTime().getMinute());
            }




        }else if(PREVIOUS_WEEK.equals(searchWeek)){

        }



        /*
         * 상담일정정보 조회
         *
         * REQUEST
         * - 조회구분(1:상담일자정보조회, 2:상담시간정보조회)
         * - 상담 선택 일자(2024-09-07)
         *
         * RESPONSE
         * - 상담 불가 시간 List(1:예약대기, 2:예약완료, 3:예약불가)
         * - 예약시간단위(30)
         */

        return null;

    }

    private List<ConsultingEachDateInfo> getConsultingWeekInfo(LocalDate baseDate){
        List<ConsultingEachDateInfo> consultingEachDateInfoList = new ArrayList<>();
        LocalDate startDate = getStartDate(baseDate, baseDate.getDayOfWeek());

        for(int i=0; i<7; i++){
            LocalDate date = startDate.plusDays(i);
            String dateStr = String.format("%02d", date.getDayOfMonth());
            String dayOfWeekStr = "토요일";

            switch (i){
                case 1: dayOfWeekStr = "일요일"; break;
                case 2: dayOfWeekStr = "월요일"; break;
                case 3: dayOfWeekStr = "화요일"; break;
                case 4: dayOfWeekStr = "수요일"; break;
                case 5: dayOfWeekStr = "목요일"; break;
                case 6: dayOfWeekStr = "금요일"; break;
                default: dayOfWeekStr = "토요일";
            }

            consultingEachDateInfoList.add(
                    ConsultingEachDateInfo.builder()
                            .date(date)
                            .dateStr(dateStr)
                            .dayOfWeekStr(dayOfWeekStr)
                            .isReservationAvailable(false)
                            .isSelected(false)
                            .build());
        }

        return consultingEachDateInfoList;
    }

    private List<ConsultingDateInfo> getConsultingWeekInfo2(LocalDate baseDate){
        List<ConsultingDateInfo> consultingDateInfoResponseList = new ArrayList<>();
        LocalDate startDate = getStartDate(baseDate, baseDate.getDayOfWeek());

        for(int i=0; i<7; i++){
            LocalDate date = startDate.plusDays(i);
            String dateStr = String.format("%02d", date.getDayOfMonth());
            String dayOfWeekStr = "토요일";

            switch (i){
                case 1: dayOfWeekStr = "일요일";
                case 2: dayOfWeekStr = "월요일";
                case 3: dayOfWeekStr = "화요일";
                case 4: dayOfWeekStr = "수요일";
                case 5: dayOfWeekStr = "목요일";
                case 6: dayOfWeekStr = "금요일";
                default: dayOfWeekStr = "토요일";
            }

            consultingDateInfoResponseList.add(
                    ConsultingDateInfo.builder()
                            .date(date)
                            .dateStr(dateStr)
                            .dayOfWeekStr(dayOfWeekStr)
                            .isReservationAvailable(false)
                            .isSelected(false)
                            .build());
        }

        return consultingDateInfoResponseList;
    }

    private LocalDate getStartDate(LocalDate baseDate, DayOfWeek baseDayOfWeek) {
        LocalDate startDate = null;

        if(DayOfWeek.SATURDAY.equals(baseDayOfWeek)) startDate = baseDate;
        else if(DayOfWeek.SUNDAY.equals(baseDayOfWeek)) startDate = baseDate.minusDays(1);
        else if(DayOfWeek.MONDAY.equals(baseDayOfWeek)) startDate = baseDate.minusDays(2);
        else if(DayOfWeek.TUESDAY.equals(baseDayOfWeek)) startDate = baseDate.minusDays(3);
        else if(DayOfWeek.WEDNESDAY.equals(baseDayOfWeek)) startDate = baseDate.minusDays(4);
        else if(DayOfWeek.THURSDAY.equals(baseDayOfWeek)) startDate = baseDate.minusDays(5);
        else if(DayOfWeek.FRIDAY.equals(baseDayOfWeek)) startDate = baseDate.minusDays(6);

        return startDate;
    }

    private void validationCheckForGetConsultingScheduleInfo(String searchWeek){
        /*if(StringUtils.isBlank(searchWeek)){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_INPUT_ERROR, "상담가능일정 조회를 위한 조회 주 정보가 입력되지 않았습니다.");
        }*/
    }

    private LocalDate[] getThisWeekDateArr(LocalDate baseDate){
        LocalDate[] localDates = new LocalDate[7];

        DayOfWeek dayOfWeek = baseDate.getDayOfWeek();

        if(DayOfWeek.SATURDAY.equals(dayOfWeek)){
            localDates[0] = baseDate;
            localDates[1] = baseDate.plusDays(1);
            localDates[2] = baseDate.plusDays(2);
            localDates[3] = baseDate.plusDays(3);
            localDates[4] = baseDate.plusDays(4);
            localDates[5] = baseDate.plusDays(5);
            localDates[6] = baseDate.plusDays(6);
        }else if(DayOfWeek.SUNDAY.equals(dayOfWeek)){
            localDates[0] = baseDate.minusDays(1);
            localDates[1] = baseDate;
            localDates[2] = baseDate.plusDays(1);
            localDates[3] = baseDate.plusDays(2);
            localDates[4] = baseDate.plusDays(3);
            localDates[5] = baseDate.plusDays(4);
            localDates[6] = baseDate.plusDays(5);
        }else if(DayOfWeek.MONDAY.equals(dayOfWeek)){
            localDates[0] = baseDate.minusDays(2);
            localDates[1] = baseDate.minusDays(1);
            localDates[2] = baseDate;
            localDates[3] = baseDate.plusDays(1);
            localDates[4] = baseDate.plusDays(2);
            localDates[5] = baseDate.plusDays(3);
            localDates[6] = baseDate.plusDays(4);
        }else if(DayOfWeek.TUESDAY.equals(dayOfWeek)){
            localDates[0] = baseDate.minusDays(3);
            localDates[1] = baseDate.minusDays(2);
            localDates[2] = baseDate.minusDays(1);
            localDates[3] = baseDate;
            localDates[4] = baseDate.plusDays(1);
            localDates[5] = baseDate.plusDays(2);
            localDates[6] = baseDate.plusDays(3);
        }else if(DayOfWeek.WEDNESDAY.equals(dayOfWeek)){
            localDates[0] = baseDate.minusDays(4);
            localDates[1] = baseDate.minusDays(3);
            localDates[2] = baseDate.minusDays(2);
            localDates[3] = baseDate.minusDays(1);
            localDates[4] = baseDate;
            localDates[5] = baseDate.plusDays(1);
            localDates[6] = baseDate.plusDays(2);
        }else if(DayOfWeek.THURSDAY.equals(dayOfWeek)){
            localDates[0] = baseDate.minusDays(5);
            localDates[1] = baseDate.minusDays(4);
            localDates[2] = baseDate.minusDays(3);
            localDates[3] = baseDate.minusDays(2);
            localDates[4] = baseDate.minusDays(1);
            localDates[5] = baseDate;
            localDates[6] = baseDate.plusDays(1);
        }else if(DayOfWeek.FRIDAY.equals(dayOfWeek)){
            localDates[0] = baseDate.minusDays(6);
            localDates[1] = baseDate.minusDays(5);
            localDates[2] = baseDate.minusDays(4);
            localDates[3] = baseDate.minusDays(3);
            localDates[4] = baseDate.minusDays(2);
            localDates[5] = baseDate.minusDays(1);
            localDates[6] = baseDate;
        }

        return localDates;
    }
}
