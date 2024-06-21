package com.xmonster.howtaxing_admin.controller;

import com.xmonster.howtaxing_admin.dto.deduction_info.DeductionInfoSaveRequest;
import com.xmonster.howtaxing_admin.service.DeductionInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class DeductionInfoController {
    private final DeductionInfoService deductionInfoService;

    // 공제정보 저장
    @PostMapping("/deductionInfo/save")
    public Object saveDeductionInfo(@RequestBody DeductionInfoSaveRequest deductionInfoSaveRequest) throws Exception {
        log.info(">> [Controller]DeductionInfoController saveDeductionInfo - 공제정보 저장");
        return deductionInfoService.saveDeductionInfo(deductionInfoSaveRequest);
    }

    // 공제코드 목록 조회
    @GetMapping("/deductionInfo/dedCodeList")
    public Object getDedCodeList() throws Exception {
        log.info(">> [Controller]DeductionInfoController getDedCodeList - 공제코드 목록 조회");
        return deductionInfoService.getDedCodeList();
    }

    // 공제내용 조회
    @GetMapping("/deductionInfo/dedContent")
    public Object getDedContent(@RequestParam String dedCode) throws Exception {
        log.info(">> [Controller]DeductionInfoController getDedContent - 공제내용 조회");
        return deductionInfoService.getDedContent(dedCode);
    }

    // 단위 목록 조회
    @GetMapping("/deductionInfo/unitList")
    public Object getUnitList() throws Exception {
        log.info(">> [Controller]DeductionInfoController getUnitList - 단위 목록 조회");
        return deductionInfoService.getUnitList();
    }
}
