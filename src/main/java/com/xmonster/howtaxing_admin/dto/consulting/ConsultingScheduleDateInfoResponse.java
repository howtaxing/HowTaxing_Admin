package com.xmonster.howtaxing_admin.dto.consulting;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultingScheduleDateInfoResponse {
    private Boolean isReservationAvailable;                             // 에약가능여부
    private String reservationAvailableStartTimeHour;                   // 예약가능시작시간_시
    private String reservationAvailableStartTimeMinute;                 // 예약가능시작시간_분
    private String reservationAvailableEndTimeHour;                     // 예약가능종료시간_시
    private String reservationAvailableEndTimeMinute;                   // 예약가능종료시간_분
    private List<ConsultingEachTimeInfo> consultingEachTimeInfoList;    // 상담개별시간정보리스트

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ConsultingEachTimeInfo {
        private String consultingTime;                                  // 상담시간
        private Integer consultingTimeUnit;                             // 상담시간단위(분단위)
        private String reservationStatus;                               // 예약상태(1:예약대기 2:예약완료 3:예약불가)
    }
}
