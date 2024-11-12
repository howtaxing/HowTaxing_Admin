package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.dto.common.ApiResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleDateInfoResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleDateInfoSaveRequest;
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
import java.util.Map;

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
    public Object getConsultingScheduleWeekInfo(Long consultantId, String currentWeekStartDateStr, String selectedDateStr, String action) throws Exception{
        log.info(">> [Service]ConsultingService getConsultingScheduleWeekInfo - 상담일정 주정보 조회");

        LocalDate baseDate = LocalDate.now();

        if(currentWeekStartDateStr != null) {
            LocalDate curWeekStartDate = LocalDate.parse(currentWeekStartDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            if(PREVIOUS.equals(action)){
                baseDate = curWeekStartDate.minusWeeks(1);
            }else if(NEXT.equals(action)){
                baseDate = curWeekStartDate.plusWeeks(1);
            }else{
                baseDate = curWeekStartDate;
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

        if(!consultingEachDateInfoList.isEmpty()){
            for(ConsultingEachDateInfo consultingEachDateInfo : consultingEachDateInfoList){
                LocalDate consultingDate = consultingEachDateInfo.getDate();

                // 예약가능여부 세팅
                log.info("예약가능여부 세팅");
                if(consultingScheduleManagementList != null && !consultingScheduleManagementList.isEmpty()){
                    for(ConsultingScheduleManagement consultingScheduleManagement : consultingScheduleManagementList){
                        LocalDate reservationDate = consultingScheduleManagement.getConsultingScheduleId().getReservationDate();

                        if(consultingDate.equals(reservationDate)){
                            consultingEachDateInfo.setIsReservationAvailable(consultingScheduleManagement.getIsReservationAvailable());
                        }
                    }
                }

                // 선택여부 세팅
                log.info("선택여부 세팅");
                if(StringUtils.isNotBlank(selectedDateStr)){
                    LocalDate selectedDate = LocalDate.parse(selectedDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    if(consultingDate.equals(selectedDate)){
                        consultingEachDateInfo.setIsSelected(true);
                    }
                }else{
                    if(consultingDate.equals(baseDate)){
                        consultingEachDateInfo.setIsSelected(true);
                    }
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

        if(StringUtils.isBlank(searchDateStr)){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_INPUT_ERROR, "상담일정 일자정보 조회를 위한 조회일자가 입력되지 않았습니다.");
        }

        LocalDate searchDate = LocalDate.parse(searchDateStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));

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

                        while(consultingTime.isBefore(reservationAvailableEndTime) || consultingTime.equals(reservationAvailableEndTime)){
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

    // 상담일정 정보 저장
    public Object saveConsultingScheduleInfo(ConsultingScheduleDateInfoSaveRequest consultingScheduleDateInfoSaveRequest) throws Exception {
        log.info(">> [Service]ConsultingService saveConsultingScheduleInfo - 상담일정 정보 저장");

        validationCheckForSaveConsultingScheduleInfo(consultingScheduleDateInfoSaveRequest);

        ConsultingScheduleManagement consultingScheduleManagement =
                consultingScheduleManagementRepository.findByConsultingScheduleId(
                        ConsultingScheduleId.builder()
                                .consultantId(consultingScheduleDateInfoSaveRequest.getConsultantId())
                                .reservationDate(consultingScheduleDateInfoSaveRequest.getReservationDate())
                                .build());

        // 신규(등록)
        if(consultingScheduleManagement == null){
            consultingScheduleManagement =
                    ConsultingScheduleManagement.builder()
                            .consultingScheduleId(ConsultingScheduleId.builder()
                                    .reservationDate(consultingScheduleDateInfoSaveRequest.getReservationDate())
                                    .consultantId(consultingScheduleDateInfoSaveRequest.getConsultantId())
                                    .build())
                            .isReservationAvailable(consultingScheduleDateInfoSaveRequest.getIsReservationAvailable())
                            .reservationAvailableStartTime(consultingScheduleDateInfoSaveRequest.getReservationAvailableStartTime())
                            .reservationAvailableEndTime(consultingScheduleDateInfoSaveRequest.getReservationAvailableEndTime())
                            .reservationTimeUnit(consultingScheduleDateInfoSaveRequest.getReservationTimeUnit())
                            .reservationUnavailableTime(consultingScheduleDateInfoSaveRequest.getReservationUnavailableTime())
                            .build();
        }
        // 변경(수정)
        else{
            if(consultingScheduleDateInfoSaveRequest.getConsultantId().equals(consultingScheduleManagement.getConsultingScheduleId().getConsultantId())
                    && consultingScheduleDateInfoSaveRequest.getReservationDate().equals(consultingScheduleManagement.getConsultingScheduleId().getReservationDate())){
                consultingScheduleManagement.setIsReservationAvailable(consultingScheduleDateInfoSaveRequest.getIsReservationAvailable());
                consultingScheduleManagement.setReservationAvailableStartTime(consultingScheduleDateInfoSaveRequest.getReservationAvailableStartTime());
                consultingScheduleManagement.setReservationAvailableEndTime(consultingScheduleDateInfoSaveRequest.getReservationAvailableEndTime());
                consultingScheduleManagement.setReservationTimeUnit(consultingScheduleDateInfoSaveRequest.getReservationTimeUnit());
                consultingScheduleManagement.setReservationUnavailableTime(consultingScheduleDateInfoSaveRequest.getReservationUnavailableTime());
            }
        }

        consultingScheduleManagementRepository.saveAndFlush(consultingScheduleManagement);

        return ApiResponse.success(Map.of("result", "상담일정 정보가 저장되었습니다."));
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

    private void validationCheckForSaveConsultingScheduleInfo(ConsultingScheduleDateInfoSaveRequest consultingScheduleDateInfoSaveRequest){
        LocalDate reservationDate = consultingScheduleDateInfoSaveRequest.getReservationDate();
        Long consultantId = consultingScheduleDateInfoSaveRequest.getConsultantId();
        Boolean isReservationAvailable = consultingScheduleDateInfoSaveRequest.getIsReservationAvailable();
        LocalTime reservationAvailableStartTime = consultingScheduleDateInfoSaveRequest.getReservationAvailableStartTime();
        LocalTime reservationAvailableEndTime = consultingScheduleDateInfoSaveRequest.getReservationAvailableEndTime();
        Integer reservationTimeUnit = consultingScheduleDateInfoSaveRequest.getReservationTimeUnit();

        if(reservationDate == null){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_SAVE_ERROR, "상담일정정보 저장을 위한 예약일자가 입력되지 않았습니다.");
        }

        if(consultantId == null){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_SAVE_ERROR, "상담일정정보 저장을 위한 상담자ID가 입력되지 않았습니다.");
        }

        if(isReservationAvailable == null){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_SAVE_ERROR, "상담일정정보 저장을 위한 예약가능여부 값이 입력되지 않았습니다.");
        }

        if(reservationAvailableStartTime == null){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_SAVE_ERROR, "상담일정정보 저장을 위한 예약가능시작시간이 입력되지 않았습니다.");
        }

        if(reservationAvailableEndTime == null){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_SAVE_ERROR, "상담일정정보 저장을 위한 예약가능종료시간이 입력되지 않았습니다.");
        }
        
        if(reservationTimeUnit == null){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_SAVE_ERROR, "상담일정정보 저장을 위한 예약시간단위가 입력되지 않았습니다.");
        }
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
