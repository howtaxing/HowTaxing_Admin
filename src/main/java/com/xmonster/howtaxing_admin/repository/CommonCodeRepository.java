package com.xmonster.howtaxing_admin.repository;

import com.xmonster.howtaxing_admin.model.CommonCode;
import com.xmonster.howtaxing_admin.model.CommonCodeId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface CommonCodeRepository extends JpaRepository<CommonCode, CommonCodeId> {
    // 대분류코드, 대분류명 목록 조회
    @Query(value = "SELECT main_ct_id, main_ct_name FROM common_code c GROUP BY main_ct_id, main_ct_name", nativeQuery = true)
    List<Map<String, Object>> findMainCtIdAndMainCtName();

    // 대분류코드 목록 조회
    @Query(value = "SELECT main_ct_id FROM common_code c GROUP BY main_ct_id", nativeQuery = true)
    List<Map<String, Object>> findMainCtId();

    // (대분류코드로) 대분류명 조회
    @Query(value = "SELECT main_ct_name FROM common_code c WHERE (c.main_ct_id = :mainCtId) GROUP BY main_ct_name", nativeQuery = true)
    Map<String, Object> findMainCtNameByMainCtId(@Param("mainCtId") String mainCtId);

    // (대분류코드로) 소분류코드, 소분류명 목록 조회
    @Query(value = "SELECT sub_ct_id, sub_ct_name FROM common_code c WHERE (c.main_ct_id = :mainCtId)", nativeQuery = true)
    List<Map<String, Object>> findSubCtIdAndSubCtNameByMainCtId(@Param("mainCtId") String mainCtId);

    // (대분류코드로) 소분류코드 목록 조회
    @Query(value = "SELECT sub_ct_id FROM common_code c WHERE (c.main_ct_id = :mainCtId)", nativeQuery = true)
    List<Map<String, Object>> findSubCtIdByMainCtId(@Param("mainCtId") String mainCtId);

    // (대분류코드와 소분류코드로) 소분류명 조회
    @Query(value = "SELECT sub_ct_name FROM common_code c WHERE (c.main_ct_id = :mainCtId AND c.sub_ct_id = :subCtId)", nativeQuery = true)
    Map<String, Object> findSubCtNameByMainCtIdAndSubCtId(@Param("mainCtId") String mainCtId, @Param("subCtId") String subCtId);

    // 단위 목록 조회
    @Query(value = "SELECT unit FROM common_code c GROUP BY unit", nativeQuery = true)
    List<Map<String, Object>> findUnit();

    Optional<CommonCode> findByCommonCodeId(CommonCodeId commonCodeId);
}
