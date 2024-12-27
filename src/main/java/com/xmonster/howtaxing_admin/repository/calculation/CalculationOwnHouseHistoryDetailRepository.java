package com.xmonster.howtaxing_admin.repository.calculation;

import com.xmonster.howtaxing_admin.model.CalculationOwnHouseHistoryDetail;
import com.xmonster.howtaxing_admin.model.CalculationOwnHouseHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CalculationOwnHouseHistoryDetailRepository extends JpaRepository<CalculationOwnHouseHistoryDetail, CalculationOwnHouseHistoryId> {

    @Query(value = "SELECT * FROM calculation_own_house_history_detail c WHERE c.own_house_history_id = :ownHouseHistoryId ORDER BY c.buy_date ASC", nativeQuery = true)
    List<CalculationOwnHouseHistoryDetail> findByOwnHouseHistoryId(Long ownHouseHistoryId);

    @Query(value = "SELECT * FROM calculation_own_house_history_detail c WHERE c.own_house_history_id = :ownHouseHistoryId AND c.house_id = :houseId", nativeQuery = true)
    List<CalculationOwnHouseHistoryDetail> findByOwnHouseHistoryIdAndHouseId(Long ownHouseHistoryId, Long houseId);
}
