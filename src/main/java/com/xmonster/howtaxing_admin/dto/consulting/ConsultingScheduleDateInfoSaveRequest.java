package com.xmonster.howtaxing_admin.dto.consulting;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultingScheduleDateInfoSaveRequest {
    private LocalDate reservationDate;                  // 예약일자
    private Long consultantId;                          // 상담자ID
    private Boolean isReservationAvailable;             // 예약가능여부
    private LocalTime reservationAvailableStartTime;    // 예약가능시작시간
    private LocalTime reservationAvailableEndTime;      // 예약가능종료시간
    private Integer reservationTimeUnit;                // 예약시간단위(분단위)
    private String reservationUnavailableTime;          // 예약불가시간(콤마(,)로 구분)
}
