package com.xmonster.howtaxing_admin.dto.consulting;

import com.xmonster.howtaxing_admin.type.ConsultingStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ConsultingStatusChangeRequest {
    private Long consultingReservationId;           // [필수] 상담예약ID
    private ConsultingStatus consultingStatus;      // [필수] 상담진행상태(PROGRESS:상담중, FINISH:상담종료)
}
