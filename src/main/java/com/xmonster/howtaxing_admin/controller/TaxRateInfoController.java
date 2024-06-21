package com.xmonster.howtaxing_admin.controller;

import com.xmonster.howtaxing_admin.dto.tax_rate_info.TaxRateInfoSaveRequest;
import com.xmonster.howtaxing_admin.service.TaxRateInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class TaxRateInfoController {
    private final TaxRateInfoService taxRateInfoService;

    // 세율정보 저장
    @PostMapping("/taxRateInfo/save")
    public Object saveTaxRateInfo(@RequestBody TaxRateInfoSaveRequest taxRateInfoSaveRequest) throws Exception {
        log.info(">> [Controller]TaxRateInfoController saveTaxRateInfo - 세율정보 저장");
        return taxRateInfoService.saveTaxRateInfo(taxRateInfoSaveRequest);
    }

    // 세율코드 목록 조회
    @GetMapping("/taxRateInfo/taxRateCodeList")
    public Object getTaxRateCodeList() throws Exception {
        log.info(">> [Controller]TaxRateInfoController getTaxRateCodeList - 세율코드 목록 조회");
        return taxRateInfoService.getTaxRateCodeList();
    }

    // 세율명 조회
    @GetMapping("/taxRateInfo/taxRateName")
    public Object getTaxRateName(@RequestParam String taxRateCode) throws Exception {
        log.info(">> [Controller]TaxRateInfoController getTaxRateName - 세율명 조회");
        return taxRateInfoService.getTaxRateName(taxRateCode);
    }

    // 사용함수 목록 조회
    @GetMapping("/taxRateInfo/usedFuncList")
    public Object getUsedFuncList() throws Exception {
        log.info(">> [Controller]TaxRateInfoController getUsedFuncList - 사용함수 목록 조회");
        return taxRateInfoService.getUsedFuncList();
    }
}
