package com.xmonster.howtaxing_admin.service;

import com.xmonster.howtaxing_admin.dto.common.ApiResponse;
import com.xmonster.howtaxing_admin.dto.common_code.CommonCodeSaveRequest;
import com.xmonster.howtaxing_admin.model.CommonCode;
import com.xmonster.howtaxing_admin.model.CommonCodeId;
import com.xmonster.howtaxing_admin.repository.CommonCodeRepository;
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
public class CommonCodeService {

    private final CommonCodeRepository commonCodeRepository;

    // 대분류코드, 대분류명 목록 조회
    public Object getMainCtInfo(){
        List<Map<String, Object>> mainCtList = commonCodeRepository.findMainCtIdAndMainCtName();

        if(mainCtList != null && !mainCtList.isEmpty()){
            return ApiResponse.success(mainCtList);
        }else{
            return ApiResponse.error("대분류코드, 대분류명 목록 조회 결과가 없습니다.");
        }
    }

    // 대분류코드 목록 조회
    public Object getMainCtIdList(){
        List<Map<String, Object>> mainCtIdList = commonCodeRepository.findMainCtId();

        if(mainCtIdList != null && !mainCtIdList.isEmpty()){
            return ApiResponse.success(mainCtIdList);
        }else{
            return ApiResponse.error("대분류코드 목록 조회 결과가 없습니다.");
        }
    }

    // (대분류코드로) 대분류명 조회
    public Object getMainCtName(String mainCtId){
        Map<String, Object> mainCtName = commonCodeRepository.findMainCtNameByMainCtId(mainCtId);

        if(mainCtName != null && !mainCtName.isEmpty()){
            return ApiResponse.success(mainCtName);
        }else{
            return ApiResponse.error("대분류명 조회 결과가 없습니다.");
        }
    }

    // (대분류코드로) 소분류코드, 소분류명 목록 조회
    public Object getSubCtInfo(String mainCtId){
        List<Map<String, Object>> subCtList = commonCodeRepository.findSubCtIdAndSubCtNameByMainCtId(mainCtId);

        if(subCtList != null && !subCtList.isEmpty()){
            return ApiResponse.success(subCtList);
        }else{
            return ApiResponse.error("소분류코드, 소분류명 조회 결과가 없습니다.");
        }
    }

    // (대분류코드로) 소분류코드 목록 조회
    public Object getSubCtIdList(String mainCtId){
        List<Map<String, Object>> subCtIdList = commonCodeRepository.findSubCtIdByMainCtId(mainCtId);

        if(subCtIdList != null && !subCtIdList.isEmpty()){
            return ApiResponse.success(subCtIdList);
        }else{
            return ApiResponse.error("소분류코드 목록 조회 결과가 없습니다.");
        }
    }

    // (대분류코드와 소분류코드로) 소분류명 조회
    public Object getSubCtName(String mainCtId, String subCtId){
        Map<String, Object> subCtName = commonCodeRepository.findSubCtNameByMainCtIdAndSubCtId(mainCtId, subCtId);

        if(subCtName != null && !subCtName.isEmpty()){
            return ApiResponse.success(subCtName);
        }else{
            return ApiResponse.error("소분류명 조회 결과가 없습니다.");
        }
    }

    // 단위 목록 조회
    public Object getUnitInfo(){
        List<Map<String, Object>> unitList = commonCodeRepository.findUnit();

        if(unitList != null && !unitList.isEmpty()){
            return ApiResponse.success(unitList);
        }else{
            return ApiResponse.error("단위 조회 결과가 없습니다.");
        }
    }

    // 공통코드 입력값 저장
    public Object saveCommonCode(CommonCodeSaveRequest commonCodeSaveRequest){
        CommonCodeId commonCodeId = new CommonCodeId(commonCodeSaveRequest.getMainCtId(), commonCodeSaveRequest.getSubCtId());

        commonCodeRepository.saveAndFlush(
                CommonCode.builder()
                        .commonCodeId(commonCodeId)
                        .mainCtName(commonCodeSaveRequest.getMainCtName())
                        .subCtName(commonCodeSaveRequest.getSubCtName())
                        .constYn(commonCodeSaveRequest.getConstYn())
                        .constVal(commonCodeSaveRequest.getConstVal())
                        .unit(commonCodeSaveRequest.getUnit())
                        .remark(commonCodeSaveRequest.getRemark())
                        .build());

        return ApiResponse.success(Map.of("result", "공통코드가 등록되었습니다."));
    }
}
