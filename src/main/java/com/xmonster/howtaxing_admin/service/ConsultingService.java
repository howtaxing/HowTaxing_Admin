package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.CustomException;
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

    // 상담일정정보 조회
    public Object getConsultingScheduleInfo(Long consultantId, String searchType, String searchDate) throws Exception {
        log.info(">> [Service]ConsultingService getConsultingScheduleInfo - 상담일정정보 조회");

        validationCheckForGetConsultingScheduleInfo(searchType, searchDate);

        log.info("상담일정정보 조회 요청 : " + consultantId + ", " + searchType + ", " + searchDate);

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

        // 상담일자정보 조회
        if(ONE.equals(searchType)){
            /**
             * 1. 상담 날짜 선택 범위
             *  1) 오늘 날짜
             *  2) 오늘 날짜가 무슨 요일인지 확인
             *  3) 오늘 날짜가 속한 '주'를 정리
             *  4)
             */

            LocalDate[] thisWeekDateArr = getThisWeekDateArr();


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

    private void validationCheckForGetConsultingScheduleInfo(String searchType, String searchDate){
        if(StringUtils.isBlank(searchType)){
            throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_INPUT_ERROR, "상담가능일정 조회를 위한 조회구분이 입력되지 않았습니다.");
        }else{
            if(TWO.equals(searchType)){
                if(StringUtils.isBlank(searchDate)){
                    throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_INPUT_ERROR, "상담가능일정 조회를 위한 조회일자가 입력되지 않았거나 값이 올바르지 않습니다.");
                }
            }else{
                if(!ONE.equals(searchType)){
                    throw new CustomException(ErrorCode.CONSULTING_SCHEDULE_INPUT_ERROR, "상담가능일정 조회를 위한 조회구분 입력값이 올바르지 않습니다.");
                }
            }
        }
    }

    private LocalDate[] getThisWeekDateArr(){
        LocalDate[] localDates = new LocalDate[7];

        LocalDate today = LocalDate.now();
        DayOfWeek dayOfWeek = today.getDayOfWeek();

        if(DayOfWeek.SATURDAY.equals(dayOfWeek)){
            localDates[0] = today;
            localDates[1] = today.plusDays(1);
            localDates[2] = today.plusDays(2);
            localDates[3] = today.plusDays(3);
            localDates[4] = today.plusDays(4);
            localDates[5] = today.plusDays(5);
            localDates[6] = today.plusDays(6);
        }else if(DayOfWeek.SUNDAY.equals(dayOfWeek)){
            localDates[0] = today.minusDays(1);
            localDates[1] = today;
            localDates[2] = today.plusDays(1);
            localDates[3] = today.plusDays(2);
            localDates[4] = today.plusDays(3);
            localDates[5] = today.plusDays(4);
            localDates[6] = today.plusDays(5);
        }else if(DayOfWeek.MONDAY.equals(dayOfWeek)){
            localDates[0] = today.minusDays(2);
            localDates[1] = today.minusDays(1);
            localDates[2] = today;
            localDates[3] = today.plusDays(1);
            localDates[4] = today.plusDays(2);
            localDates[5] = today.plusDays(3);
            localDates[6] = today.plusDays(4);
        }else if(DayOfWeek.TUESDAY.equals(dayOfWeek)){
            localDates[0] = today.minusDays(3);
            localDates[1] = today.minusDays(2);
            localDates[2] = today.minusDays(1);
            localDates[3] = today;
            localDates[4] = today.plusDays(1);
            localDates[5] = today.plusDays(2);
            localDates[6] = today.plusDays(3);
        }else if(DayOfWeek.WEDNESDAY.equals(dayOfWeek)){
            localDates[0] = today.minusDays(4);
            localDates[1] = today.minusDays(3);
            localDates[2] = today.minusDays(2);
            localDates[3] = today.minusDays(1);
            localDates[4] = today;
            localDates[5] = today.plusDays(1);
            localDates[6] = today.plusDays(2);
        }else if(DayOfWeek.THURSDAY.equals(dayOfWeek)){
            localDates[0] = today.minusDays(5);
            localDates[1] = today.minusDays(4);
            localDates[2] = today.minusDays(3);
            localDates[3] = today.minusDays(2);
            localDates[4] = today.minusDays(1);
            localDates[5] = today;
            localDates[6] = today.plusDays(1);
        }else if(DayOfWeek.FRIDAY.equals(dayOfWeek)){
            localDates[0] = today.minusDays(6);
            localDates[1] = today.minusDays(5);
            localDates[2] = today.minusDays(4);
            localDates[3] = today.minusDays(3);
            localDates[4] = today.minusDays(2);
            localDates[5] = today.minusDays(1);
            localDates[6] = today;
        }

        return localDates;
    }
}
