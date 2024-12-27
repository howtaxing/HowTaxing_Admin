package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.dto.common.ApiResponse;
import com.xmonster.howtaxing_admin.dto.consulting.*;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleWeekInfoResponse.ConsultingEachDateInfo;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleDateInfoResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingScheduleDateInfoResponse.ConsultingEachTimeInfo;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingReservationInfoListResponse.ConsultingReservationInfoResponse;
import com.xmonster.howtaxing_admin.dto.consulting.ConsultingReservationInfoDetailResponse.*;
import com.xmonster.howtaxing_admin.model.*;
import com.xmonster.howtaxing_admin.repository.calculation.*;
import com.xmonster.howtaxing_admin.repository.consulting.ConsultantInfoRepository;
import com.xmonster.howtaxing_admin.repository.consulting.ConsultingReservationInfoRepository;
import com.xmonster.howtaxing_admin.repository.consulting.ConsultingScheduleManagementRepository;
import com.xmonster.howtaxing_admin.repository.user.UserRepository;
import com.xmonster.howtaxing_admin.type.ConsultingStatus;
import com.xmonster.howtaxing_admin.type.ErrorCode;
import com.xmonster.howtaxing_admin.utils.*;
import com.xmonster.howtaxing_admin.vo.ConsultingReservationInfoVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
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
    private final CalculationAdditionalAnswerRequestHistoryRepository calculationAdditionalAnswerRequestHistoryRepository;
    private final CalculationBuyRequestHistoryRepository calculationBuyRequestHistoryRepository;
    private final CalculationBuyResponseHistoryRepository calculationBuyResponseHistoryRepository;
    private final CalculationCommentaryResponseHistoryRepository calculationCommentaryResponseHistoryRepository;
    private final CalculationHistoryRepository calculationHistoryRepository;
    private final CalculationOwnHouseHistoryRepository calculationOwnHouseHistoryRepository;
    private final CalculationOwnHouseHistoryDetailRepository calculationOwnHouseHistoryDetailRepository;
    private final CalculationSellRequestHistoryRepository calculationSellRequestHistoryRepository;
    private final CalculationSellResponseHistoryRepository calculationSellResponseHistoryRepository;
    private final UserRepository userRepository;

    private final ConsultantUtil consultantUtil;
    private final ConsultingReservationUtil consultingReservationUtil;
    private final HouseUtil houseUtil;
    private final UserUtil userUtil;
    private final PaymentUtil paymentUtil;

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

        log.info("consultingScheduleDateInfoSaveRequest : " + consultingScheduleDateInfoSaveRequest);

        LocalDate reservationDate = consultingScheduleDateInfoSaveRequest.getReservationDate();
        Long consultantId = consultingScheduleDateInfoSaveRequest.getConsultantId();
        Boolean isReservationAvailable = consultingScheduleDateInfoSaveRequest.getIsReservationAvailable();
        LocalTime reservationAvailableStartTime = consultingScheduleDateInfoSaveRequest.getReservationAvailableStartTime();
        LocalTime reservationAvailableEndTime = consultingScheduleDateInfoSaveRequest.getReservationAvailableEndTime();
        Integer reservationTimeUnit = consultingScheduleDateInfoSaveRequest.getReservationTimeUnit();
        String reservationUnavailableTime = consultingScheduleDateInfoSaveRequest.getReservationUnavailableTime();

        ConsultingScheduleManagement consultingScheduleManagement =
                consultingScheduleManagementRepository.findByConsultingScheduleId(
                        ConsultingScheduleId.builder()
                                .consultantId(consultantId)
                                .reservationDate(reservationDate)
                                .build());

        // 신규(등록)
        if(consultingScheduleManagement == null){
            consultingScheduleManagement =
                    ConsultingScheduleManagement.builder()
                            .consultingScheduleId(ConsultingScheduleId.builder()
                                    .reservationDate(reservationDate)
                                    .consultantId(consultantId)
                                    .build())
                            .isReservationAvailable(isReservationAvailable)
                            .reservationAvailableStartTime(reservationAvailableStartTime)
                            .reservationAvailableEndTime(reservationAvailableEndTime)
                            .reservationTimeUnit(reservationTimeUnit)
                            .reservationUnavailableTime(reservationUnavailableTime)
                            .build();
        }
        // 변경(수정)
        else{
            if(consultantId.equals(consultingScheduleManagement.getConsultingScheduleId().getConsultantId())
                    && reservationDate.equals(consultingScheduleManagement.getConsultingScheduleId().getReservationDate())){
                consultingScheduleManagement.setIsReservationAvailable(isReservationAvailable);
                consultingScheduleManagement.setReservationAvailableStartTime(reservationAvailableStartTime);
                consultingScheduleManagement.setReservationAvailableEndTime(reservationAvailableEndTime);
                consultingScheduleManagement.setReservationTimeUnit(reservationTimeUnit);
                consultingScheduleManagement.setReservationUnavailableTime(reservationUnavailableTime);
            }
        }

        consultingScheduleManagementRepository.saveAndFlush(consultingScheduleManagement);

        return ApiResponse.success(Map.of("result", "상담일정 정보가 저장되었습니다."));
    }

    // 상담예약정보 목록 조회
    public Object getConsultingReservationInfoList(ConsultingReservationInfoListRequest consultingReservationInfoListRequest) throws Exception {
        log.info(">> [Service]ConsultingService getConsultingReservationInfoList - 상담예약정보 목록 조회");

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        Long consultantId = consultingReservationInfoListRequest.getConsultantId();
        LocalDate reservationDate = consultingReservationInfoListRequest.getReservationDate();
        ConsultingStatus consultingStatus = consultingReservationInfoListRequest.getConsultingStatus();
        String consultingType = consultingReservationInfoListRequest.getConsultingType();
        String customerName = consultingReservationInfoListRequest.getCustomerName();

        int listCnt = 0;
        List<ConsultingReservationInfoResponse> consultingReservationInfoResponseList = null;

        List<ConsultingReservationInfo> consultingReservationInfoList =
                consultingReservationInfoRepository.findConsultingReservationInfoList(consultantId, reservationDate, consultingStatus, consultingType, customerName);

        if(consultingReservationInfoList != null){
            consultingReservationInfoResponseList = new ArrayList<>();

            int seqCnt = 1;
            for(ConsultingReservationInfo consultingReservationInfo : consultingReservationInfoList){
                String resConsultingStatus = EMPTY;
                if(ConsultingStatus.PAYMENT_READY.equals(consultingReservationInfo.getConsultingStatus())){
                    resConsultingStatus = "결제대기";
                }else if(ConsultingStatus.PAYMENT_COMPLETED.equals(consultingReservationInfo.getConsultingStatus())){
                    resConsultingStatus = "결제완료";
                }else if(ConsultingStatus.WAITING.equals(consultingReservationInfo.getConsultingStatus())){
                    resConsultingStatus = "상담대기";
                }else if(ConsultingStatus.CANCEL.equals(consultingReservationInfo.getConsultingStatus())){
                    resConsultingStatus = "상담취소";
                }else if(ConsultingStatus.PROGRESS.equals(consultingReservationInfo.getConsultingStatus())){
                    resConsultingStatus = "상담중";
                }else if(ConsultingStatus.FINISH.equals(consultingReservationInfo.getConsultingStatus())){
                    resConsultingStatus = "상담종료";
                }

                consultingReservationInfoResponseList.add(
                        ConsultingReservationInfoResponse.builder()
                                .seq(seqCnt)
                                .consultingReservationId(consultingReservationInfo.getConsultingReservationId())
                                .reservationDate(consultingReservationInfo.getReservationDate().format(dateFormatter))
                                .reservationTime(consultingReservationInfo.getReservationStartTime().format(timeFormatter))
                                .consultingType(getConsultingTypeStr(consultingReservationInfo.getConsultingType()))
                                .customerName(consultingReservationInfo.getCustomerName())
                                .consultantId(consultingReservationInfo.getConsultantId())
                                //.consultantName(consultingReservationInfo.getConsultantName())
                                .consultingStatus(resConsultingStatus)
                                .build());
                seqCnt++;
            }

            listCnt = consultingReservationInfoResponseList.size();
        }

        return ApiResponse.success(
                ConsultingReservationInfoListResponse.builder()
                        .listCnt(listCnt)
                        .consultingReservationInfoResponseList(consultingReservationInfoResponseList)
                        .build());
    }

    // 상담예약정보 상세 조회(GGMANYAR)
    public Object getConsultingReservationInfoDetail(Long consultingReservationId) throws Exception {
        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - 상담예약정보 상세 조회");

        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm:ss");
        DateTimeFormatter dateTimeFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        DateTimeFormatter dateFormatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        if(consultingReservationId == null){
            throw new CustomException(ErrorCode.CONSULTING_DETAIL_INPUT_ERROR, "상담예약ID가 입력되지 않았습니다.");
        }

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-01");

        /* 데이터 조회 */
        // 1. 상담예약정보
        ConsultingReservationInfo consultingReservationInfo = consultingReservationInfoRepository.findByConsultingReservationId(consultingReservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONSULTING_DETAIL_INPUT_ERROR, "존재하지 않는 상담예약ID 입니다."));

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-02");

        // 2. 사용자정보
        long userId = consultingReservationInfo.getUserId();

        // 3. 상담자정보
        //ConsultantInfo consultantInfo = consultantUtil.findSelectedConsultantInfo(consultingReservationInfo.getConsultantId());

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-03");

        // 4. 계산이력
        Long calcHistoryId = consultingReservationInfo.getCalcHistoryId();
        String calcType = null;

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-04");

        // 5. 계산요청이력(취득세, 양도소득세 구분)
        // 6. 계산응답이력(취득세, 양도소득세 구분)
        // 7. 계산추가답변요청이력
        // 8. 계산해설응답이력
        // 9. 계산보유주택이력(상세포함)
        CalculationHistory calculationHistory = null;
        CalculationBuyRequestHistory calculationBuyRequestHistory = null;
        CalculationSellRequestHistory calculationSellRequestHistory = null;
        List<CalculationAdditionalAnswerRequestHistory> calculationAdditionalAnswerRequestHistoryList = null;
        List<CalculationBuyResponseHistory> calculationBuyResponseHistoryList = null;
        List<CalculationSellResponseHistory> calculationSellResponseHistoryList = null;
        List<CalculationCommentaryResponseHistory> calculationCommentaryResponseHistoryList = null;
        CalculationOwnHouseHistory calculationOwnHouseHistory = null;
        List<CalculationOwnHouseHistoryDetail> calculationOwnHouseHistoryDetailList = null;

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-05");

        // 계산 결과 존재
        if(calcHistoryId != null){
            calculationAdditionalAnswerRequestHistoryList = new ArrayList<>();
            calculationCommentaryResponseHistoryList = new ArrayList<>();

            calculationHistory = calculationHistoryRepository.findByCalcHistoryId(calcHistoryId);

            calcType = calculationHistory.getCalcType();
            if(CALC_TYPE_BUY.equals(calcType)){
                calculationBuyResponseHistoryList = new ArrayList<>();

                calculationBuyRequestHistory = calculationBuyRequestHistoryRepository.findByCalcHistoryId(calcHistoryId);
                calculationBuyResponseHistoryList = calculationBuyResponseHistoryRepository.findByCalcHistoryId(calcHistoryId);
            }else if(CALC_TYPE_SELL.equals(calcType)){
                calculationSellResponseHistoryList = new ArrayList<>();

                calculationSellRequestHistory = calculationSellRequestHistoryRepository.findByCalcHistoryId(calcHistoryId);
                calculationSellResponseHistoryList = calculationSellResponseHistoryRepository.findByCalcHistoryId(calcHistoryId);
            }

            calculationAdditionalAnswerRequestHistoryList = calculationAdditionalAnswerRequestHistoryRepository.findByCalcHistoryId(calcHistoryId);
            calculationCommentaryResponseHistoryList = calculationCommentaryResponseHistoryRepository.findByCalcHistoryId(calcHistoryId);
            calculationOwnHouseHistory = calculationOwnHouseHistoryRepository.findByCalcHistoryId(calcHistoryId);

            if(calculationOwnHouseHistory != null && calculationOwnHouseHistory.getOwnHouseHistoryId() != null){
                calculationOwnHouseHistoryDetailList = new ArrayList<>();
                calculationOwnHouseHistoryDetailList = calculationOwnHouseHistoryDetailRepository.findByOwnHouseHistoryId(calculationOwnHouseHistory.getOwnHouseHistoryId());
            }
        }

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-06");

        ConsultingBaseInfoResponse consultingBaseInfoResponse = null;
        CalculationBuyHouseInfoResponse calculationBuyHouseInfoResponse = null;
        CalculationSellHouseInfoResponse calculationSellHouseInfoResponse = null;
        List<OwnHouseInfoResponseResponse> ownHouseInfoResponseResponseList = null;
        List<CalculationBuyResultInfoResponse> calculationBuyResultInfoResponseList = null;
        List<CalculationSellResultInfoResponse> calculationSellResultInfoResponseList = null;
        List<CalculationCommentaryInfoResponse> calculationCommentaryInfoResponseList = null;
        ConsultingProgressInfoResponse consultingProgressInfoResponse = null;
        ConsultingStatusInfoResponse consultingStatusInfoResponse = null;

        /* 응답값에 세팅 */
        // 1. 상담기본정보
        LocalDate reservationDate = consultingReservationInfo.getReservationDate();
        LocalTime reservationTime = consultingReservationInfo.getReservationStartTime();
        String reservationDateStr = null;
        String reservationTimeStr = null;
        if(reservationDate != null) reservationDateStr = reservationDate.format(dateFormatter2);
        if(reservationTime != null) reservationTimeStr = reservationTime.format(timeFormatter);

        consultingBaseInfoResponse =
                ConsultingBaseInfoResponse.builder()
                        .consultingReservationId(consultingReservationInfo.getConsultingReservationId())
                        .reservationDate(reservationDateStr)
                        .reservationTime(reservationTimeStr)
                        .consultingType(getConsultingTypeStr(consultingReservationInfo.getConsultingType()))
                        .customerName(consultingReservationInfo.getCustomerName())
                        .customerPhone(consultingReservationInfo.getCustomerPhone())
                        .consultingInflowPath(getConsultingInflowPath(consultingReservationInfo.getConsultingInflowPath()))
                        .consultingRequestContent(consultingReservationInfo.getConsultingRequestContent())
                        .build();

        if(consultingBaseInfoResponse == null){
            throw new CustomException(ErrorCode.CONSULTING_DETAIL_OUTPUT_ERROR, "상담기본정보 조회에 실패했습니다.");
        }

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-07");

        // 계산 결과 존재
        if(calcHistoryId != null){
            // TODO. 2/3/4/5
            // 2. 계산대상주택정보
            // 3. 보유주택정보(계산시점 기준)
            // 4. 계산결과정보
            // 5. 해설정보(일단 제외)
        }

        // 6. 상담진행정보
        consultingProgressInfoResponse =
                ConsultingProgressInfoResponse.builder()
                        .consultingStatus(consultingReservationInfo.getConsultingStatus())
                        .paymentAmount(consultingReservationInfo.getPaymentAmount())
                        .consultingContent(consultingReservationInfo.getConsultingContent())
                        .remark(consultingReservationInfo.getRemark())
                        .build();

        if(consultingProgressInfoResponse == null){
            throw new CustomException(ErrorCode.CONSULTING_DETAIL_OUTPUT_ERROR, "상담진행정보 조회에 실패했습니다.");
        }

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-08");

        // 7. 상담현황정보
        LocalDateTime consultingRequestDatetime = consultingReservationInfo.getConsultingRequestDatetime();
        LocalDateTime consultingStartDatetime = consultingReservationInfo.getConsultingStartDatetime();
        LocalDateTime consultingEndDatetime = consultingReservationInfo.getConsultingEndDatetime();
        LocalDateTime consultingCancelDatetime = consultingReservationInfo.getConsultingCancelDatetime();
        String consultingRequestDatetimeStr = null;
        String consultingReservationDatetimeStr = null;
        String consultingStartDatetimeStr = null;
        String consultingEndDatetimeStr = null;
        String consultingCancelDatetimeStr = null;
        if(consultingRequestDatetime != null) consultingRequestDatetimeStr = consultingRequestDatetime.format(dateTimeFormatter2);
        consultingReservationDatetimeStr = reservationDateStr + SPACE + reservationTimeStr;
        if(consultingStartDatetime != null) consultingStartDatetimeStr = consultingStartDatetime.format(dateTimeFormatter2);
        if(consultingEndDatetime != null) consultingEndDatetimeStr = consultingEndDatetime.format(dateTimeFormatter2);
        if(consultingCancelDatetime != null) consultingCancelDatetimeStr = consultingCancelDatetime.format(dateTimeFormatter2);

        consultingStatusInfoResponse =
                ConsultingStatusInfoResponse.builder()
                        .consultingRequestDatetime(consultingRequestDatetimeStr)
                        .consultingReservationDatetime(consultingReservationDatetimeStr)
                        .consultingStartDatetime(consultingStartDatetimeStr)
                        .consultingEndDatetime(consultingEndDatetimeStr)
                        .consultingCancelDatetime(consultingCancelDatetimeStr)
                        .lastModifier(consultingReservationInfo.getLastModifier())
                        .build();

        if(consultingStatusInfoResponse == null){
            throw new CustomException(ErrorCode.CONSULTING_DETAIL_OUTPUT_ERROR, "상담현황정보 조회에 실패했습니다.");
        }

        log.info(">> [Service]ConsultingService getConsultingReservationInfoDetail - STEP-09");

        return ApiResponse.success(
                ConsultingReservationInfoDetailResponse.builder()
                        .consultingBaseInfoResponse(consultingBaseInfoResponse)
                        .calculationBuyHouseInfoResponse(calculationBuyHouseInfoResponse)
                        .calculationSellHouseInfoResponse(calculationSellHouseInfoResponse)
                        .ownHouseInfoResponseResponseList(ownHouseInfoResponseResponseList)
                        .calculationBuyResultInfoResponseList(calculationBuyResultInfoResponseList)
                        .calculationSellResultInfoResponseList(calculationSellResultInfoResponseList)
                        .calculationCommentaryInfoResponseList(calculationCommentaryInfoResponseList)
                        .consultingProgressInfoResponse(consultingProgressInfoResponse)
                        .consultingStatusInfoResponse(consultingStatusInfoResponse)
                        .build());
    }

    // 상담 상태 변경
    public Object changeConsultingStatus(ConsultingStatusChangeRequest consultingStatusChangeRequest) throws Exception {
        log.info(">> [Service]ConsultingService changeConsultingStatus - 상담 상태 변경");

        validationCheckForChangeConsultingStatus(consultingStatusChangeRequest);

        Long consultingReservationId = consultingStatusChangeRequest.getConsultingReservationId();
        ConsultingStatus consultingStatus = consultingStatusChangeRequest.getConsultingStatus();

        ConsultingReservationInfo consultingReservationInfo = consultingReservationUtil.findConsultingReservationInfo(consultingReservationId);

        ConsultingStatus orgConsultingStatus = consultingReservationInfo.getConsultingStatus();

        // 변경되는 상담 상태가 상담이 진행되는 방향이 아닐 때 오류
        if(orgConsultingStatus.ordinal() >= consultingStatus.ordinal()){
            throw new CustomException(ErrorCode.CONSULTING_STATUS_INPUT_ERROR, "상담 상태 변경은 역방향으로 진행될 수 없습니다.");
        }

        consultingReservationInfo.setConsultingStatus(consultingStatus);

        // 상담 시작
        if(ConsultingStatus.PROGRESS.equals(consultingStatus)){
            consultingReservationInfo.setConsultingStartDatetime(LocalDateTime.now());
        }
        // 상담 종료
        else if(ConsultingStatus.FINISH.equals(consultingStatus)){
            consultingReservationInfo.setConsultingEndDatetime(LocalDateTime.now());
        }

        try{
            consultingReservationInfoRepository.saveAndFlush(consultingReservationInfo);
        }catch(Exception e){
            throw new CustomException(ErrorCode.CONSULTING_STATUS_OUTPUT_ERROR, "상담 상태 DB 업데이트 중 오류가 발생했습니다.");
        }

        return ApiResponse.success(Map.of("result", "상담 상태가 업데이트 되었습니다."));
    }

    // 상담 데이터 저장
    public Object saveConsultingData(ConsultingDataSaveRequest consultingDataSaveRequest) throws Exception {
        log.info(">> [Service]ConsultingService saveConsultingData - 상담 데이터 저장");

        if(consultingDataSaveRequest == null){
            throw new CustomException(ErrorCode.CONSULTING_DATA_INPUT_ERROR);
        }

        Long consultingReservationId = consultingDataSaveRequest.getConsultingReservationId();
        String consultingContent = consultingDataSaveRequest.getConsultingContent();
        String consultingRemark = consultingDataSaveRequest.getConsultingRemark();

        if(consultingReservationId == null){
            throw new CustomException(ErrorCode.CONSULTING_DATA_INPUT_ERROR, "상담예약ID가 입력되지 않았습니다.");
        }

        ConsultingReservationInfo consultingReservationInfo = consultingReservationUtil.findConsultingReservationInfo(consultingReservationId);

        if(StringUtils.isNotBlank(consultingContent)){
            consultingReservationInfo.setConsultingContent(consultingContent);
        }

        if(StringUtils.isNotBlank(consultingRemark)){
            consultingReservationInfo.setRemark(consultingRemark);
        }

        try{
            consultingReservationInfoRepository.saveAndFlush(consultingReservationInfo);
        }catch(Exception e){
            throw new CustomException(ErrorCode.CONSULTING_STATUS_OUTPUT_ERROR, "상담 데이터 DB 저장 중 오류가 발생했습니다.");
        }

        return ApiResponse.success(Map.of("result", "상담 데이터 저장이 완료되었습니다."));
    }

    // 상담유형 문자열 변환하여 가져오기
    private String getConsultingTypeStr(String consultingType){
        StringBuilder resultSb = new StringBuilder(EMPTY);

        if(StringUtils.isNotBlank(consultingType)){
            String[] consultingTypeArr = consultingType.split(COMMA);

            for(String consultingTypeUnit : consultingTypeArr){
                if(!EMPTY.contentEquals(resultSb)){
                    resultSb.append(COMMA);
                }

                if(CONSULTING_TYPE_BUY.equals(consultingTypeUnit)){
                    resultSb.append("취득세");
                }else if(CONSULTING_TYPE_SELL.equals(consultingTypeUnit)){
                    resultSb.append("양도소득세");
                }else if(CONSULTING_TYPE_INHERIT.equals(consultingTypeUnit)){
                    resultSb.append("상속세");
                }else if(CONSULTING_TYPE_PROPERTY.equals(consultingTypeUnit)){
                    resultSb.append("재산세");
                }
            }
        }

        return resultSb.toString();
    }

    // 상담유입경로 문자열 변환하여 가져오기
    private String getConsultingInflowPath(String consultingInflowPath){
        String resultStr = EMPTY;

        if(StringUtils.isNotBlank(consultingInflowPath)){
            if(CONSULTING_TYPE_GEN.equals(consultingInflowPath)){
                resultStr = "일반";
            }else if(CONSULTING_TYPE_BUY.equals(consultingInflowPath)){
                resultStr = "취득세 계산";
            }else if(CONSULTING_TYPE_SELL.equals(consultingInflowPath)){
                resultStr = "양도소득세 계산";
            }
        }

        return resultStr;
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
        }else{
            if(isReservationAvailable){
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
        }
    }

    // 상담 상태 변경 유효값 체크
    private void validationCheckForChangeConsultingStatus(ConsultingStatusChangeRequest consultingStatusChangeRequest){
        if(consultingStatusChangeRequest == null){
            throw new CustomException(ErrorCode.CONSULTING_STATUS_INPUT_ERROR);
        }

        Long consultingReservationId = consultingStatusChangeRequest.getConsultingReservationId();
        ConsultingStatus consultingStatus = consultingStatusChangeRequest.getConsultingStatus();

        if(consultingReservationId == null){
            throw new CustomException(ErrorCode.CONSULTING_STATUS_INPUT_ERROR, "상담예약ID가 입력되지 않았습니다.");
        }

        if(consultingStatus == null){
            throw new CustomException(ErrorCode.CONSULTING_STATUS_INPUT_ERROR, "상담 상태 값이 입력되지 않았습니다.");
        }else{
            if(!ConsultingStatus.PROGRESS.equals(consultingStatus) && !ConsultingStatus.FINISH.equals(consultingStatus)){
                throw new CustomException(ErrorCode.CONSULTING_STATUS_INPUT_ERROR, "상담 상태 값이 올바르지 않습니다.");
            }
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
