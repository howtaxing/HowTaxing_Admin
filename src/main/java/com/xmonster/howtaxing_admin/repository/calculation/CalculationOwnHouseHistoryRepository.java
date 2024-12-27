package com.xmonster.howtaxing_admin.repository.calculation;

import com.xmonster.howtaxing_admin.model.CalculationOwnHouseHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalculationOwnHouseHistoryRepository extends JpaRepository<CalculationOwnHouseHistory, Long> {

    CalculationOwnHouseHistory findByCalcHistoryId(Long calcHistoryId);
}
