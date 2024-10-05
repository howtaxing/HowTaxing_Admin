package com.xmonster.howtaxing_admin.dto.consulting;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultingScheduleInfoResponse {
    private Long consultantId;                          // 상담자ID
    private String consultingSelectDatePeriod;          // 상담날짜선택범위
    private List<String> consultingWeekDayValueList;    // 상담 주 날짜 값(7개)
    private List<String> consultingWeekDateList;        // 상담 주 일자 데이터(7개)
    private Boolean isReservationAvailable;             // 예약가능여부 값(여, 부)
    private String reservationAvailableStartTimeHour;   // 예약가능시작시간_시
    private String reservationAvailableStartTimeMinute; // 예약가능시작시간_분
    private String reservationAvailableEndTimeHour;     // 예약가능종료시간_시
    private String reservationAvailableEndTimeMinute;   // 예약가능종료시간_분
}
