package com.xmonster.howtaxing_admin.dto.consulting;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConsultingScheduleWeekInfoResponse {
    private Long consultantId;                                          // 상담자ID
    private String consultingWeekInfo;                                  // 상담주정보
    private List<ConsultingEachDateInfo> consultingEachDateInfoList;    // 상담개별일자정보리스트

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ConsultingEachDateInfo {
        private LocalDate date;                                         // 날짜(ex. 2024-10-09)
        private String dateStr;                                         // 날짜값(ex. 09)
        private String dayOfWeekStr;                                    // 요일값(수요일)
        private Boolean isReservationAvailable;                         // 에약가능여부
        private Boolean isSelected;                                     // 선택여부
    }
}
