package com.xmonster.howtaxing_admin.utils;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.model.PaymentHistory;
import com.xmonster.howtaxing_admin.repository.payment.PaymentHistoryRepository;
import com.xmonster.howtaxing_admin.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentUtil {
    private final PaymentHistoryRepository paymentHistoryRepository;

    public PaymentHistory findPaymentHistory(Long paymentHistoryId){
        return paymentHistoryRepository.findByPaymentHistoryId(paymentHistoryId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_HISTORY_NOT_FOUND));
    }

    public PaymentHistory findPaymentHistoryByConsultingReservationId(Long consultingReservationId){
        return paymentHistoryRepository.findByConsultingReservationId(consultingReservationId)
                .orElseThrow(() -> new CustomException(ErrorCode.PAYMENT_HISTORY_NOT_FOUND));
    }
}
