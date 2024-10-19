package com.xmonster.howtaxing_admin.dto.consulting;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultingScheduleInfoResponse {
    private Long consultantId;                                                                      // 상담자ID
    private String consultingWeekInfo;                                                              // 상담주정보
    private List<ConsultingDateInfoResponse> consultingDateInfoResponseList;                        // 상담일정보목록
    private Boolean isReservationAvailable;                                                         // 에약가능여부
    private String reservationAvailableStartTimeHour;                                               // 예약가능시작시간_시
    private String reservationAvailableStartTimeMinute;                                             // 예약가능시작시간_분
    private String reservationAvailableEndTimeHour;                                                 // 예약가능종료시간_시
    private String reservationAvailableEndTimeMinute;                                               // 예약가능종료시간_분
    private List<ConsultingAvailableTimeInfoResponse> consultingAvailableTimeInfoResponseList;      // 상담가능시간정보목록

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ConsultingDateInfoResponse {
        private LocalDate date;                                 // 날짜(ex. 2024-10-09)
        private String dateStr;                                 // 날짜값(ex. 09)
        private String dayOfWeekStr;                            // 요일값(수요일)
        private Boolean isReservationAvailable;                 // 에약가능여부
        private Boolean isSelected;                             // 선택여부
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ConsultingAvailableTimeInfoResponse {
        private String consultingTime;                          // 상담시간
        private Integer consultingTimeUnit;                     // 상담시간단위(분단위)
        private String reservationStatus;                       // 예약상태(1:예약대기 2:예약완료 3:예약불가)
    }
}
