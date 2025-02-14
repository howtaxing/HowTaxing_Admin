package com.xmonster.howtaxing_admin.repository.house;

import com.xmonster.howtaxing_admin.model.House;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HouseRepository extends JpaRepository<House, Long> {

    Optional<House> findByHouseId(Long houseId);

    List<House> findByUserId(Long userId);

    void deleteByHouseId(Long houseId);

    void deleteByUserId(Long userId);

    void deleteByUserIdAndSourceType(Long userId, String sourceType);

    Long countByUserId(Long userId);
}
