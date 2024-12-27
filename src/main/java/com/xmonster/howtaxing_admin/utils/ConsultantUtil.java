package com.xmonster.howtaxing_admin.utils;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.model.ConsultantInfo;
import com.xmonster.howtaxing_admin.repository.consulting.ConsultantInfoRepository;
import com.xmonster.howtaxing_admin.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ConsultantUtil {
    private final ConsultantInfoRepository consultantInfoRepository;

    public ConsultantInfo findSelectedConsultantInfo(Long consultantId) {
        return consultantInfoRepository.findByConsultantId(consultantId)
                .orElseThrow(() -> new CustomException(ErrorCode.CONSULTING_CONSULTANT_NOT_FOUND));
    }

    public String findConsultantName(Long consultantId) {
        return findSelectedConsultantInfo(consultantId).getConsultantName();
    }
}
