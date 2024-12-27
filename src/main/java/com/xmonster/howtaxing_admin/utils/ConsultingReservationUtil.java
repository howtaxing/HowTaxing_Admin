package com.xmonster.howtaxing_admin.utils;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.model.ConsultingReservationInfo;
import com.xmonster.howtaxing_admin.repository.consulting.ConsultingReservationInfoRepository;
import com.xmonster.howtaxing_admin.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultingReservationUtil {
    private final ConsultingReservationInfoRepository consultingReservationInfoRepository;

    public ConsultingReservationInfo findConsultingReservationInfo(Long consultingReservationId) {
        return consultingReservationInfoRepository.findByConsultingReservationId(consultingReservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONSULTING_RESERVATION_NOT_FOUND));
    }
}
