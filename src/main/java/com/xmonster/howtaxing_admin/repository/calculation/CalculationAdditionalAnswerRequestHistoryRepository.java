package com.xmonster.howtaxing_admin.repository.calculation;

import com.xmonster.howtaxing_admin.model.CalculationAdditionalAnswerRequestHistory;
import com.xmonster.howtaxing_admin.model.CalculationHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalculationAdditionalAnswerRequestHistoryRepository extends JpaRepository<CalculationAdditionalAnswerRequestHistory, CalculationHistoryId> {

    CalculationAdditionalAnswerRequestHistory findByCalculationHistoryId(CalculationHistoryId calculationHistoryId);

    @Query(value = "SELECT * FROM calculation_additional_answer_request_history c WHERE c.calc_history_id = :calcHistoryId", nativeQuery = true)
    List<CalculationAdditionalAnswerRequestHistory> findByCalcHistoryId(@Param("calcHistoryId") Long calcHistoryId);
}
