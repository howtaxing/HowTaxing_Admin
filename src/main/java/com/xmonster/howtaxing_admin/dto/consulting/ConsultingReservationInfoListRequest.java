package com.xmonster.howtaxing_admin.dto.consulting;

import com.xmonster.howtaxing_admin.type.ConsultingStatus;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ConsultingReservationInfoListRequest {
    private Long consultantId;                      // [선택] 상담자ID
    private LocalDate reservationDate;              // [선택] 예약일자
    private ConsultingStatus consultingStatus;      // [선택] 상담진행상태(PAYMENT_READY:결제대기, PAYMENT_COMPLETE:결제완료, WAITING:상담대기, CANCEL:상담취소, PROGRESS:상담중, FINISH:상담종료)
    private String consultingType;                  // [선택] 상담유형(01:취득세, 02:양도소득세, 03:상속세, 04:재산세)
    private String customerName;                    // [선택] 고객명
}
