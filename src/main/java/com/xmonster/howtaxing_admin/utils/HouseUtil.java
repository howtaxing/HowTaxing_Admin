package com.xmonster.howtaxing_admin.utils;

import com.xmonster.howtaxing_admin.CustomException;
import com.xmonster.howtaxing_admin.model.House;
import com.xmonster.howtaxing_admin.repository.house.HouseRepository;
import com.xmonster.howtaxing_admin.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class HouseUtil {
    private final HouseRepository houseRepository;

    public House findSelectedHouse(Long houseId) {
        return houseRepository.findByHouseId(houseId)
                .orElseThrow(() -> new CustomException(ErrorCode.HOUSE_NOT_FOUND_ERROR));
    }

    public List<House> findOwnHouseList(Long userId) {
        return houseRepository.findByUserId(userId);
    }

    public long countOwnHouse(Long userId) {
        return houseRepository.countByUserId(userId);
    }
}
