package com.xmonster.howtaxing_admin.repository.calculation;

import com.xmonster.howtaxing_admin.model.CalculationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CalculationHistoryRepository extends JpaRepository<CalculationHistory, Long> {

    CalculationHistory findByCalcHistoryId(Long calcHistoryId);
}
