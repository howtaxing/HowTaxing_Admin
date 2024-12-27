package com.xmonster.howtaxing_admin.dto.consulting;

import com.xmonster.howtaxing_admin.type.ConsultingStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ConsultingReservationInfoListResponse {
    private Integer listCnt;
    private List<ConsultingReservationInfoResponse> consultingReservationInfoResponseList;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    public static class ConsultingReservationInfoResponse {
        private Integer seq;                        // 번호
        private Long consultingReservationId;       // 상담예약ID(상담번호)
        private String reservationDate;             // 예약일자(yyyy.MM.dd)
        private String reservationTime;             // 예약시간(HH:mm)
        private String consultingType;              // 상담종류
        private String customerName;                // 고객명
        private Long consultantId;                  // 상담자ID
        private String consultantName;              // 상담자명
        private String consultingStatus;            // 상담상태(상담진행)
    }
}
