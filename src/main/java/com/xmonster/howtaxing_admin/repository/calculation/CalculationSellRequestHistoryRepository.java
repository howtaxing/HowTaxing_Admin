package com.xmonster.howtaxing_admin.repository.calculation;

import com.xmonster.howtaxing_admin.model.CalculationSellRequestHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationSellRequestHistoryRepository extends JpaRepository<CalculationSellRequestHistory, Long> {

    CalculationSellRequestHistory findByCalcHistoryId(Long calcHistoryId);
}
