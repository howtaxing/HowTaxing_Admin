package com.xmonster.howtaxing_admin.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode {
    /* 사용자 관련 */
    USER_NOT_FOUND(1, HttpStatus.OK, "USER-001", "사용자를 찾을 수 없습니다."),
    USER_LOGOUT_ERROR(1, HttpStatus.OK, "USER-005", "로그아웃 처리 중 오류가 발생했습니다."),
    USER_WITHDRAW_ERROR(1, HttpStatus.OK, "USER-006", "회원탈퇴 처리 중 오류가 발생했습니다."),

    /* 로그인 관련 */
    LOGIN_COMMON_ERROR(1, HttpStatus.OK, "LOGIN-001", "로그인 중 오류가 발생했습니다."),
    LOGIN_HAS_EMAIL_ERROR(1, HttpStatus.OK, "LOGIN-002", "이미 동일한 이메일 계정으로 가입되어 있습니다."),
    INVALID_PASSWORD(1, HttpStatus.OK, "LOGIN-003", "비밀번호가 일치하지 않습니다."),

    /* 주택(취득주택 조회) 관련 */
    HOUSE_JUSOGOV_INPUT_ERROR(1, HttpStatus.OK, "HOUSE-001", "주택 정보 조회를 위한 요청값이 올바르지 않습니다."),
    HOUSE_JUSOGOV_OUTPUT_ERROR(1, HttpStatus.OK, "HOUSE-002", "공공기관에서 검색한 주택 정보를 가져오는 중 오류가 발생했습니다."),
    HOUSE_JUSOGOV_SYSTEM_ERROR(1, HttpStatus.OK, "HOUSE-003", "공공기관의 시스템에 문제가 발생하여 검색한 주택 정보를 가져오는 중 오류가 발생했습니다."),

    /* 주택(보유주택 조회) 관련 */
    HOUSE_HYPHEN_INPUT_ERROR(1, HttpStatus.OK, "HOUSE-004", "보유주택 정보 조회를 위한 간편인증 입력값이 올바르지 않습니다."),
    HOUSE_HYPHEN_OUTPUT_ERROR(1, HttpStatus.OK, "HOUSE-005", "공공기관에서 보유주택 정보를 가져오는 중 오류가 발생했습니다."),
    HOUSE_HYPHEN_SYSTEM_ERROR(1, HttpStatus.OK, "HOUSE-006", "공공기관의 시스템에 문제가 발생하여 보유주택 정보를 가져오는 중 오류가 발생했습니다."),

    /* 주택(양도주택 거주기간 조회) 관련 */
    HYPHEN_STAY_PERIOD_INPUT_ERROR(1, HttpStatus.OK, "HOUSE-007", "주택 거주기간 조회를 위한 입력값이 올바르지 않습니다."),
    HYPHEN_STAY_PERIOD_OUTPUT_ERROR(1, HttpStatus.OK, "HOUSE-008", "공공기관에서 거주기간 정보를 가져오는 중 오류가 발생했습니다."),
    HYPHEN_STAY_PERIOD_SYSTEM_ERROR(1, HttpStatus.OK, "HOUSE-009", "공공기관의 시스템에 문제가 발생하여 거주기간 정보를 가져오는 중 오류가 발생했습니다."),

    /* 공시가격 및 전용면적 관련 */
    HOUSE_VWORLD_INPUT_ERROR(1, HttpStatus.OK, "HOUSE-010", "공시가격 및 전용면적 조회를 위한 요청값이 올바르지 않습니다."),
    HOUSE_VWORLD_OUTPUT_ERROR(1, HttpStatus.OK, "HOUSE-011", "공공기관에서 공시가격 및 전용면적 정보를 가져오는 중 오류가 발생했습니다."),
    HOUSE_VWORLD_SYSTEM_ERROR(1, HttpStatus.OK, "HOUSE-012", "공공기관의 시스템에 문제가 발생하여 공시가격 및 전용면적 정보를 가져오는 중 오류가 발생했습니다."),

    /* 주택(내부 데이터) 관련 */
    HOUSE_NOT_FOUND_ERROR(1, HttpStatus.OK, "HOUSE-013", "해당 주택 정보를 찾을 수 없습니다."),
    HOUSE_REGIST_ERROR(1, HttpStatus.OK, "HOUSE-014", "보유주택 등록 중 오류가 발생했습니다."),
    HOUSE_MODIFY_ERROR(1, HttpStatus.OK, "HOUSE-015", "보유주택 수정 중 오류가 발생했습니다."),
    HOUSE_DELETE_ERROR(1, HttpStatus.OK, "HOUSE-016", "보유주택 삭제 중 오류가 발생했습니다."),
    HOUSE_DELETE_ALL_ERROR(1, HttpStatus.OK, "HOUSE-017", "보유주택 전체 삭제 중 오류가 발생했습니다."),

    /* 주택(청약홈 로드 데이터) 관련 */
    HOUSE_GET_INFO_ERROR(1, HttpStatus.OK, "HOUSE-018", "보유주택 정보 조회를 위한 필수 입력값이 올바르지 않습니다."),
    HOUSE_GET_INFO_NOT_FOUND(1, HttpStatus.OK, "HOUSE-019", "보유주택 정보 조회에 실패하였습니다."),

    /* 주소 관련 오류 */
    ADDRESS_SEPARATE_ERROR(1, HttpStatus.OK, "ADDRESS-001", "주소 데이터 프로세스 중 오류가 발생했습니다."),
    
    /* 추가질의 항목 조회 관련 */
    QUESTION_INPUT_ERROR(1, HttpStatus.OK, "QUESTION-001", "추가질의항목 조회를 위한 입력값이 올바르지 않습니다."),
    QUESTION_OUTPUT_NOT_FOUND(1, HttpStatus.OK, "QUESTION-002", "추가질의항목이 존재하지 않습니다."),
    QUESTION_OUTPUT_ERROR(1, HttpStatus.OK, "QUESTION-003", "추가질의항목 조회 중 오류가 발생했습니다."),

    /* 계산 관련 */
    CALCULATION_BUY_TAX_FAILED(2, HttpStatus.OK, "CALCULATION-001", "취득세 계산 중 오류가 발생했습니다."),
    CALCULATION_SELL_TAX_FAILED(2, HttpStatus.OK, "CALCULATION-002", "양도소득세 계산 중 오류가 발생했습니다."),

    /* 상담 관련 */
    CONSULTING_SCHEDULE_INPUT_ERROR(1, HttpStatus.OK, "CONSULTING-001", "상담가능일정 조회를 위한 입력값이 올바르지 않습니다."),
    CONSULTING_SCHEDULE_OUTPUT_ERROR(1, HttpStatus.OK, "CONSULTING-002", "상담가능일정 조회 중 오류가 발생했습니다."),
    CONSULTING_SCHEDULE_SAVE_ERROR(1, HttpStatus.OK, "CONSULTING-003", "상담 일정 저장 중 오류가 발생했습니다."),
    CONSULTING_CANCEL_INPUT_ERROR(1, HttpStatus.OK, "CONSULTING-007", "상담 예약 취소를 위한 입력값이 올바르지 않습니다."),
    CONSULTING_CANCEL_OUTPUT_ERROR(1, HttpStatus.OK, "CONSULTING-008", "상담 예약 취소 중 오류가 발생했습니다."),
    CONSULTING_LIST_INPUT_ERROR(1, HttpStatus.OK, "CONSULTING-009", "상담 예약 목록 조회를 위한 입력값이 올바르지 않습니다."),
    CONSULTING_LIST_OUTPUT_ERROR(1, HttpStatus.OK, "CONSULTING-010", "상담 예약 목록 조회 중 오류가 발생했습니다."),
    CONSULTING_DETAIL_INPUT_ERROR(1, HttpStatus.OK, "CONSULTING-011", "상담 예약 상세 조회를 위한 입력값이 올바르지 않습니다."),
    CONSULTING_DETAIL_OUTPUT_ERROR(1, HttpStatus.OK, "CONSULTING-012", "상담 예약 상세 조회 중 오류가 발생했습니다."),
    CONSULTING_CONSULTANT_NOT_FOUND(1, HttpStatus.OK, "CONSULTING-016", "해당 상담자가 존재하지 않아요."),
    CONSULTING_RESERVATION_NOT_FOUND(1, HttpStatus.OK, "CONSULTING-017", "해당 상담예약이 존재하지 않아요."),
    CONSULTING_CREATE_INPUT_ERROR(1, HttpStatus.OK, "CONSULTING-018", "상담 예약 정보 생성을 위한 입력값이 올바르지 않습니다."),
    CONSULTING_CREATE_OUTPUT_ERROR(1, HttpStatus.OK, "CONSULTING-019", "상담 예약 정보 생성 중 오류가 발생했습니다."),
    CONSULTING_AVAILABLE_INPUT_ERROR(1, HttpStatus.OK, "CONSULTING-020", "상담 예약 가능여부 조회를 위한 입력값이 올바르지 않습니다."),
    CONSULTING_AVAILABLE_OUTPUT_ERROR(1, HttpStatus.OK, "CONSULTING-021", "상담 예약 가능여부 조회 중 오류가 발생했습니다."),
    CONSULTING_STATUS_INPUT_ERROR(1, HttpStatus.OK, "CONSULTING-022", "상담 상태 변경을 위한 입력값이 올바르지 않습니다."),
    CONSULTING_STATUS_OUTPUT_ERROR(1, HttpStatus.OK, "CONSULTING-023", "상담 상태 변경 중 오류가 발생했습니다."),
    CONSULTING_DATA_INPUT_ERROR(1, HttpStatus.OK, "CONSULTING-024", "상담 데이터 저장을 위한 입력값이 올바르지 않습니다."),
    CONSULTING_DATA_OUTPUT_ERROR(1, HttpStatus.OK, "CONSULTING-025", "상담 데이터 저장 중 오류가 발생했습니다."),

    /* 리뷰 관련 */
    REVIEW_REGIST_ERROR(1, HttpStatus.OK, "REVIEW-001", "리뷰 등록 중 오류가 발생했습니다."),

    /* 메시지 관련 */
    SMS_AUTH_INPUT_ERROR(1, HttpStatus.OK, "MESSAGE-001", "인증번호 발송 입력값이 올바르지 않아요."),
    SMS_AUTH_COUNT_ERROR(1, HttpStatus.OK, "MESSAGE-002", "인증번호는 하루에 10회까지만 발송할 수 있어요."),
    SMS_AUTH_SEND_ERROR(1, HttpStatus.OK, "MESSAGE-003", "인증번호 발송에 실패했어요."),
    SMS_AUTH_CHECK_ERROR(1, HttpStatus.OK, "MESSAGE-004", "인증번호 검증에 실패했어요."),
    SMS_AUTH_MATCH_ERROR(1, HttpStatus.OK, "MESSAGE-005", "인증번호가 정확하지 않아요."),
    SMS_AUTH_TIME_ERROR(1, HttpStatus.OK, "MESSAGE-006", "인증시간이 만료되었어요."),
    SMS_MSG_INPUT_ERROR(1, HttpStatus.OK, "MESSAGE-007", "SMS 발송을 위한 입력값이 올바르지 않아요."),
    SMS_MSG_OUTPUT_ERROR(1, HttpStatus.OK, "MESSAGE-008", "SMS 발송 중 오류가 발생했어요."),
    SMS_MSG_SYSTEM_ERROR(1, HttpStatus.OK, "MESSAGE-009", "SMS 시스템에 오류가 발생하여 메시지를 발송할 수 없어요."),

    /* 상품 관련 */
    PRODUCT_INFO_INPUT_ERROR(1, HttpStatus.OK, "PRODUCT-001", "상품정보 조회를 위한 입력값이 올바르지 않아요."),
    PRODUCT_INFO_OUTPUT_ERROR(1, HttpStatus.OK, "PRODUCT-002", "상품정보 조회 중 오류가 발생했어요."),

    /* 결제 관련 */
    PAYMENT_REQUEST_INPUT_ERROR(1, HttpStatus.OK, "PAYMENT-001", "결제요청 임시저장을 위한 입력값이 올바르지 않아요."),
    PAYMENT_REQUEST_OUTPUT_ERROR(1, HttpStatus.OK, "PAYMENT-002", "결제요청 임시저장 중 오류가 발생했어요."),
    PAYMENT_CONFIRM_INPUT_ERROR(1, HttpStatus.OK, "PAYMENT-003", "결제승인 요청을 위한 입력값이 올바르지 않아요."),
    PAYMENT_CONFIRM_OUTPUT_ERROR(1, HttpStatus.OK, "PAYMENT-004", "결제승인 요청 중 오류가 발생했어요."),
    PAYMENT_HISTORY_NOT_FOUND(1, HttpStatus.OK, "PAYMENT-005", "해당 결제이력을 찾을 수 없어요."),
    PAYMENT_DETAIL_INPUT_ERROR(1, HttpStatus.OK, "PAYMENT-006", "결제상세 조회를 위한 입력값이 올바르지 않아요."),
    PAYMENT_DETAIL_OUTPUT_ERROR(1, HttpStatus.OK, "PAYMENT-006", "결제상세 조회 중 오류가 발생했어요."),
    PAYMENT_CANCEL_INPUT_ERROR(1, HttpStatus.OK, "PAYMENT-006", "결제취소 요청을 위한 입력값이 올바르지 않아요."),
    PAYMENT_CANCEL_OUTPUT_ERROR(1, HttpStatus.OK, "PAYMENT-006", "결제취소 요청 중 오류가 발생했어요."),

    /* 시스템 관련 */
    SYSTEM_UNKNOWN_ERROR(2, HttpStatus.OK, "SYSTEM-001", "알 수 없는 오류가 발생했습니다."),

    /* 기타 */
    ETC_ERROR(2, HttpStatus.OK, "ETC-001", "오류가 발생했습니다.");

    private final int type;                 // (오류)유형 (1:단순 오류 메시지, 2:상담 연결 메시지)
    private final HttpStatus httpStatus;	// HttpStatus (400, 404, 500...)
    private final String code;				// (오류)코드 ("ACCOUNT-001")
    private final String message;			// (오류)설명 ("사용자를 찾을 수 없습니다.")
}