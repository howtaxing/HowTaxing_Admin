package com.xmonster.howtaxing_admin.repository.payment;

import com.xmonster.howtaxing_admin.model.PaymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PaymentHistoryRepository extends JpaRepository<PaymentHistory, Long> {

    Optional<PaymentHistory> findByPaymentHistoryId(Long paymentHistoryId);

    Optional<PaymentHistory> findByConsultingReservationId(Long consultingReservationId);

    List<PaymentHistory> findByUserIdOrderByApprovedAtDesc(Long userId);

    @Query(value = "SELECT * " +
                    "FROM payment_history p " +
                    "WHERE p.user_id = :userId " +
                    "AND p.status = 'DONE' " +
                    "ORDER BY approved_at DESC", nativeQuery = true)
    List<PaymentHistory> findCompleteListByUserId(Long userId);
}
