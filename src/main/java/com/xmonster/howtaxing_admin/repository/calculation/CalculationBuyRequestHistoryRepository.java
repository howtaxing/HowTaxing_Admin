package com.xmonster.howtaxing_admin.repository.calculation;

import com.xmonster.howtaxing_admin.model.CalculationBuyRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationBuyRequestHistoryRepository extends JpaRepository<CalculationBuyRequestHistory, Long> {

    CalculationBuyRequestHistory findByCalcHistoryId(Long calcHistoryId);
}
