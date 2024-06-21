package com.xmonster.howtaxing_admin.controller;

import com.xmonster.howtaxing_admin.dto.calculation_process.CalculationProcessSaveRequest;
import com.xmonster.howtaxing_admin.service.CalculationProcessService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CalculationProcessController {
    private final CalculationProcessService calculationProcessService;

    // 계산프로세스 저장
    @PostMapping("/calculationProcess/save")
    public Object saveCalculationProcess(@RequestBody CalculationProcessSaveRequest calculationProcessSaveRequest) throws Exception {
        log.info(">> [Controller]CalculationProcessController saveCalculationProcess - 계산프로세스 저장");
        return calculationProcessService.saveCalculationProcess(calculationProcessSaveRequest);
    }

    // 계산구분 및 계산명 목록 조회
    @GetMapping("/calculationProcess/calcTypeAndCalcNameList")
    public Object getCalcTypeAndCalcNameList() throws Exception {
        log.info(">> [Controller]CalculationProcessController getCalcTypeAndCalcNameList - 계산구분 및 계산명 목록 조회");
        return calculationProcessService.getCalcTypeAndCalcNameList();
    }

    // 분기번호 목록 조회
    @GetMapping("/calculationProcess/branchNoList")
    public Object getBranchNoList(String calcType) throws Exception {
        log.info(">> [Controller]CalculationProcessController getBranchNoList - 분기번호 목록 조회");
        return calculationProcessService.getBranchNoList(calcType);
    }

    // 분기명 조회
    @GetMapping("/calculationProcess/branchName")
    public Object getBranchName(@RequestParam String calcType, @RequestParam String branchNo) throws Exception {
        log.info(">> [Controller]CalculationProcessController getBranchName - 분기명 조회");
        return calculationProcessService.getBranchName(calcType, branchNo);
    }

    // 선택번호 목록 조회
    @GetMapping("/calculationProcess/selectNoList")
    public Object getSelectNoList(@RequestParam String calcType, @RequestParam String branchNo) throws Exception {
        log.info(">> [Controller]CalculationProcessController getSelectNoList - 선택번호 목록 조회");
        return calculationProcessService.getSelectNoList(calcType, branchNo);
    }

    // 선택내용 조회
    @GetMapping("/calculationProcess/selectContent")
    public Object getSelectContent(@RequestParam String calcType, @RequestParam String branchNo, @RequestParam Integer selectNo) throws Exception {
        log.info(">> [Controller]CalculationProcessController getSelectContent - 선택내용 조회");
        return calculationProcessService.getSelectContent(calcType, branchNo, selectNo);
    }
}
