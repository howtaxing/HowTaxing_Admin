package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.dto.common.ApiResponse;
import com.xmonster.howtaxing_admin.dto.deduction_info.DeductionInfoSaveRequest;
import com.xmonster.howtaxing_admin.model.DeductionInfo;
import com.xmonster.howtaxing_admin.repository.calculation.DeductionInfoRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class DeductionInfoService {

    private final DeductionInfoRepository deductionInfoRepository;

    // 공제코드 목록 조회
    public Object getDedCodeList(){
        List<Map<String, Object>> dedCodeList = deductionInfoRepository.findDedCode();

        if(dedCodeList != null && !dedCodeList.isEmpty()){
            return ApiResponse.success(dedCodeList);
        }else{
            return ApiResponse.error("공제코드 목록 조회 결과가 없습니다.");
        }
    }

    // (공제코드로) 공제내용 조회
    public Object getDedContent(String dedCode){
        Map<String, Object> dedContentData = deductionInfoRepository.findDedContentByDedCode(dedCode);

        if(dedContentData != null && !dedContentData.isEmpty()){
            return ApiResponse.success(dedContentData);
        }else{
            return ApiResponse.error("공제내용 조회 결과가 없습니다.");
        }
    }
    
    // 단위 목록 조회
    public Object getUnitList(){
        List<Map<String, Object>> unitList = deductionInfoRepository.findUnit();

        if(unitList != null && !unitList.isEmpty()){
            return ApiResponse.success(unitList);
        }else{
            return ApiResponse.error("단위 목록 조회 결과가 없습니다.");
        }
    }

    // 공제정보 입력값 저장
    public Object saveDeductionInfo(DeductionInfoSaveRequest deductionInfoSaveRequest){
        deductionInfoRepository.saveAndFlush(
                DeductionInfo.builder()
                        .dedCode(deductionInfoSaveRequest.getDedCode())
                        .dedContent(deductionInfoSaveRequest.getDedContent())
                        .unit(deductionInfoSaveRequest.getUnit())
                        .unitDedRate(deductionInfoSaveRequest.getUnitDedRate())
                        .limitYear(deductionInfoSaveRequest.getLimitYear())
                        .limitDedRate(deductionInfoSaveRequest.getLimitDedRate())
                        .remark(deductionInfoSaveRequest.getRemark())
                        .build());

        return ApiResponse.success(Map.of("result", "공제정보가 등록되었습니다."));
    }
}
