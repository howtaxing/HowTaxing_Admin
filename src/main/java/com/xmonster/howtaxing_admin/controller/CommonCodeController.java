package com.xmonster.howtaxing_admin.controller;

import com.xmonster.howtaxing_admin.dto.common_code.CommonCodeSaveRequest;
import com.xmonster.howtaxing_admin.service.CommonCodeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
public class CommonCodeController {
    private final CommonCodeService commonCodeService;
    
    // 공통코드 저장
    @PostMapping("/commonCode/save")
    public Object saveCommonCode(@RequestBody CommonCodeSaveRequest commonCodeSaveRequest) throws Exception {
        log.info(">> [Controller]CommonCodeController saveCommonCode - 공통코드 저장");
        return commonCodeService.saveCommonCode(commonCodeSaveRequest);
    }

    // 대분류코드, 대분류명 목록 조회
    @GetMapping("/commonCode/mainCtList")
    public Object getMainCtList() throws Exception {
        log.info(">> [Controller]CommonCodeController getMainCtList - 대분류코드, 대분류명 목록 조회");
        return commonCodeService.getMainCtInfo();
    }

    // 대분류코드 목록 조회
    @GetMapping("/commonCode/mainCtIdList")
    public Object getMainCtIdList() throws Exception {
        log.info(">> [Controller]CommonCodeController getMainCtIdList - 대분류코드 목록 조회");
        return commonCodeService.getMainCtIdList();
    }

    // 대분류명 조회
    @GetMapping("/commonCode/mainCtName")
    public Object getMainCtName(@RequestParam String mainCtId) throws Exception {
        log.info(">> [Controller]CommonCodeController getMainCtName - 대분류명 조회");
        return commonCodeService.getMainCtName(mainCtId);
    }

    // 소분류코드, 소분류명 목록 조회
    @GetMapping("/commonCode/subCtList")
    public Object getSubCtList(@RequestParam String mainCtId) throws Exception {
        log.info(">> [Controller]CommonCodeController getSubCtList - 소분류 리스트 조회");
        return commonCodeService.getSubCtInfo(mainCtId);
    }

    // 소분류코드 목록 조회
    @GetMapping("/commonCode/subCtIdList")
    public Object getSubCtIdList(@RequestParam String mainCtId) throws Exception {
        log.info(">> [Controller]CommonCodeController getSubCtIdList - 소분류코드 목록 조회");
        return commonCodeService.getSubCtIdList(mainCtId);
    }

    // 소분류명 조회
    @GetMapping("/commonCode/subCtName")
    public Object getSubCtName(@RequestParam String mainCtId, @RequestParam String subCtId) throws Exception {
        log.info(">> [Controller]CommonCodeController getSubCtName - 소분류명 조회");
        return commonCodeService.getSubCtName(mainCtId, subCtId);
    }

    // 단위 목록 조회
    @GetMapping("/commonCode/unitList")
    public Object getUnitList() throws Exception {
        log.info(">> [Controller]CommonCodeController getUnitList - 단위 목록 조회");
        return commonCodeService.getUnitInfo();
    }
}
