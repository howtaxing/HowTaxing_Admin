package com.xmonster.howtaxing_admin.dto.consulting;

import com.xmonster.howtaxing_admin.type.ConsultingStatus;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ConsultingDataSaveRequest {
    private Long consultingReservationId;           // [필수] 상담예약ID
    private String consultingContent;               // [선택] 상담내용(세무사 작성)
    private String consultingRemark;                // [선택] 비고
}
