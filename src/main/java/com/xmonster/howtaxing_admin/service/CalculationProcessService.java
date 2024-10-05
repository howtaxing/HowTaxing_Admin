package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.dto.calculation_process.CalculationProcessSaveRequest;
import com.xmonster.howtaxing_admin.dto.common.ApiResponse;
import com.xmonster.howtaxing_admin.model.CalculationProcess;
import com.xmonster.howtaxing_admin.model.CalculationProcessId;
import com.xmonster.howtaxing_admin.repository.calculation.CalculationProcessRepository;

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
public class CalculationProcessService {

    private final CalculationProcessRepository calculationProcessRepository;

    // 계산구분 및 계산명 목록 조회
    public Object getCalcTypeAndCalcNameList(){
        List<Map<String, Object>> calcTypeAndNameList = calculationProcessRepository.findCalcTypeAndCalcName();

        if(calcTypeAndNameList != null && !calcTypeAndNameList.isEmpty()){
            return ApiResponse.success(calcTypeAndNameList);
        }else{
            return ApiResponse.error("계산구분 및 계산명 목록 조회 결과가 없습니다.");
        }
    }

    // 분기번호 목록 조회
    public Object getBranchNoList(String calcType){
        List<Map<String, Object>> branchNoList = calculationProcessRepository.findBranchNo(calcType);

        if(branchNoList != null && !branchNoList.isEmpty()){
            return ApiResponse.success(branchNoList);
        }else{
            return ApiResponse.error("분기번호 목록 조회 결과가 없습니다.");
        }
    }

    // (계산구분 및 분기번호로) 분기명 조회
    public Object getBranchName(String calcType, String branchNo){
        Map<String, Object> branchName = calculationProcessRepository.findBranchNameByBranchNo(calcType, branchNo);

        if(branchName != null && !branchName.isEmpty()){
            return ApiResponse.success(branchName);
        }else{
            return ApiResponse.error("분기명 조회 결과가 없습니다.");
        }
    }

    // (계산구분 및 분기번호로) 선택번호 목록 조회
    public Object getSelectNoList(String calcType, String branchNo){
        List<Map<String, Object>> selectNoList = calculationProcessRepository.findSelectNo(calcType, branchNo);

        if(selectNoList != null && !selectNoList.isEmpty()){
            return ApiResponse.success(selectNoList);
        }else{
            return ApiResponse.error("선택번호 목록 조회 결과가 없습니다.");
        }
    }

    // (계산구분 및 분기번호 및 선택번호로) 선택내용 조회
    public Object getSelectContent(String calcType, String branchNo, Integer selectNo){
        Map<String, Object> selectContent = calculationProcessRepository.findSelectContentBySelectNo(calcType, branchNo, selectNo);

        if(selectContent != null && !selectContent.isEmpty()){
            return ApiResponse.success(selectContent);
        }else{
            return ApiResponse.error("선택내용 조회 결과가 없습니다.");
        }
    }

    // 계산프로세스 입력값 저장
    public Object saveCalculationProcess(CalculationProcessSaveRequest calculationProcessSaveRequest){
        CalculationProcessId calculationProcessId = new CalculationProcessId(
                calculationProcessSaveRequest.getCalcType(),
                calculationProcessSaveRequest.getBranchNo(),
                calculationProcessSaveRequest.getSelectNo());

        calculationProcessRepository.saveAndFlush(
                CalculationProcess.builder()
                        .calculationProcessId(calculationProcessId)
                        .calcName(calculationProcessSaveRequest.getCalcName())
                        .branchName(calculationProcessSaveRequest.getBranchName())
                        .selectContent(calculationProcessSaveRequest.getSelectContent())
                        .variableData(calculationProcessSaveRequest.getVariableData())
                        .dataMethod(calculationProcessSaveRequest.getDataMethod())
                        .hasNextBranch(calculationProcessSaveRequest.isHasNextBranch())
                        .nextBranchNo(calculationProcessSaveRequest.getNextBranchNo())
                        .taxRateCode(calculationProcessSaveRequest.getTaxRateCode())
                        .dedCode(calculationProcessSaveRequest.getDedCode())
                        .remark(calculationProcessSaveRequest.getRemark())
                        .build());

        return ApiResponse.success(Map.of("result", "계산프로세스가 등록되었습니다."));
    }
}
