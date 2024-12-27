package com.xmonster.howtaxing_admin.repository.calculation;

import com.xmonster.howtaxing_admin.model.CalculationBuyResponseHistory;
import com.xmonster.howtaxing_admin.model.CalculationHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CalculationBuyResponseHistoryRepository extends JpaRepository<CalculationBuyResponseHistory, CalculationHistoryId> {

    //List<CalculationBuyResponseHistory> findByCalculationHistoryId(CalculationHistoryId calculationHistoryId);

    @Query(value = "SELECT * FROM calculation_buy_response_history c WHERE c.calc_history_id = :calcHistoryId", nativeQuery = true)
    List<CalculationBuyResponseHistory> findByCalcHistoryId(@Param("calcHistoryId") Long calcHistoryId);
}
