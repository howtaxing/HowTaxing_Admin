package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.dto.common.ApiResponse;
import com.xmonster.howtaxing_admin.dto.tax_rate_info.TaxRateInfoSaveRequest;
import com.xmonster.howtaxing_admin.model.TaxRateInfo;
import com.xmonster.howtaxing_admin.repository.TaxRateInfoRepository;

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
public class TaxRateInfoService {

    private final TaxRateInfoRepository taxRateInfoRepository;

    // 세율코드 목록 조회
    public Object getTaxRateCodeList(){
        List<Map<String, Object>> taxRateCodeList = taxRateInfoRepository.findTaxRateCode();

        if(taxRateCodeList != null && !taxRateCodeList.isEmpty()){
            return ApiResponse.success(taxRateCodeList);
        }else{
            return ApiResponse.error("세율코드 조회 결과가 없습니다.");
        }
    }

    // (세율코드로) 세율명 조회
    public Object getTaxRateName(String taxRateCode){
        Map<String, Object> taxRateNameData = taxRateInfoRepository.findTaxRateNameByTaxRateCode(taxRateCode);

        if(taxRateNameData != null && !taxRateNameData.isEmpty()){
            return ApiResponse.success(taxRateNameData);
        }else{
            return ApiResponse.error("세율명 조회 결과가 없습니다.");
        }
    }
    
    // 사용함수 목록 조회
    public Object getUsedFuncList(){
        List<Map<String, Object>> usedFuncList = taxRateInfoRepository.findUsedFunc();

        if(usedFuncList != null && !usedFuncList.isEmpty()){
            return ApiResponse.success(usedFuncList);
        }else{
            return ApiResponse.error("사용함수 조회 결과가 없습니다.");
        }
    }

    // 세율정보 입력값 저장
    public Object saveTaxRateInfo(TaxRateInfoSaveRequest taxRateInfoSaveRequest){
        taxRateInfoRepository.saveAndFlush(
                TaxRateInfo.builder()
                        .taxRateCode(taxRateInfoSaveRequest.getTaxRateCode())
                        .taxRateName(taxRateInfoSaveRequest.getTaxRateName())
                        .constYn(taxRateInfoSaveRequest.getConstYn())
                        .usedFunc(taxRateInfoSaveRequest.getUsedFunc())
                        .basePrice(taxRateInfoSaveRequest.getBasePrice())
                        .taxRate1(taxRateInfoSaveRequest.getTaxRate1())
                        .addTaxRate1(taxRateInfoSaveRequest.getAddTaxRate1())
                        .taxRate2(taxRateInfoSaveRequest.getTaxRate2())
                        .addTaxRate2(taxRateInfoSaveRequest.getAddTaxRate2())
                        .remark(taxRateInfoSaveRequest.getRemark())
                        .build());

        return ApiResponse.success(Map.of("result", "세율정보가 등록되었습니다."));
    }
}
