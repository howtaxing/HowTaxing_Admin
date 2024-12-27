package com.xmonster.howtaxing_admin.dto.consulting;

import com.xmonster.howtaxing_admin.type.ConsultingStatus;
import com.xmonster.howtaxing_admin.type.LastModifierType;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class ConsultingReservationInfoDetailResponse {
    ConsultingBaseInfoResponse consultingBaseInfoResponse = null;
    CalculationBuyHouseInfoResponse calculationBuyHouseInfoResponse = null;
    CalculationSellHouseInfoResponse calculationSellHouseInfoResponse = null;
    List<OwnHouseInfoResponseResponse> ownHouseInfoResponseResponseList = null;
    List<CalculationBuyResultInfoResponse> calculationBuyResultInfoResponseList = null;
    List<CalculationSellResultInfoResponse> calculationSellResultInfoResponseList = null;
    List<CalculationCommentaryInfoResponse> calculationCommentaryInfoResponseList = null;
    ConsultingProgressInfoResponse consultingProgressInfoResponse = null;
    ConsultingStatusInfoResponse consultingStatusInfoResponse = null;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 1. 상담기본정보
    public static class ConsultingBaseInfoResponse {
        private Long consultingReservationId;           // 상담예약ID(상담번호)
        private String reservationDate;                 // 예약일자(yyyy.MM.dd)
        private String reservationTime;                 // 예약시간(HH:mm)
        private String consultingType;                  // 상담유형(콤마(,)로 구분 - (01:취득세 02:양도소득세 03:상속세 04:재산세))
        private String customerName;                    // 고객명
        private String customerPhone;                   // 고객전화번호
        private String consultantName;                  // 상담자명
        private String consultingInflowPath;            // 상담유입경로(00:일반 01:취득세계산 02:양도소득세계산)
        private String consultingRequestContent;        // 상담요청내용
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 2-1. (취득세)계산대상주택정보
    public static class CalculationBuyHouseInfoResponse {
        private String houseType;                       // 주택유형
        private String houseName;                       // 주택명
        private String roadAddr;                        // 주소
        private String detailAdr;                       // 상세주소
        private String buyContractDate;                 // 취득계약일자
        private String balanceDate;                     // 잔금지급일자(취득세 계산 대상)
        private String buyDate;                         // 취득일자
        private Long buyPrice;                          // 취득금액
        private Long pubLandPrice;                      // 공시가격(취득세 계산 대상)
        private Double area;                            // 전용면적(취득세 계산 대상)
        private Boolean isDestruction;                  // 멸실여부
        private Boolean isMoveInRight;                  // 입주권여부
        private Integer ownerCnt;                       // 소유자수
        private Integer userProportion;                 // 본인지분비율
        private Boolean isAdjustmentTargetArea;         // 조정대상지역여부
        private Integer ownHouseCnt;                    // 보유주택수
        // TODO. 추가질의및응답내용 추가
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 2-2. (양도소득세)계산대상주택정보
    public static class CalculationSellHouseInfoResponse {
        private Long houseId;                           // 주택ID(양도소득세 계산 대상)
        private String houseType;                       // 주택유형
        private String houseName;                       // 주택명
        private String roadAddr;                        // 주소
        private String detailAdr;                       // 상세주소
        private String buyContractDate;                 // 취득계약일자
        private String buyDate;                         // 취득일자
        private Long buyPrice;                          // 취득금액
        private String sellContractDate;                // 양도계약일자(양도소득세 계산 대상)
        private String sellDate;                        // 양도일자(양도소득세 계산 대상)
        private Long sellPrice;                         // 양도금액(양도소득세 계산 대상)
        private Long necExpensePrice;                   // 필요경비금액(양도소득세 계산 대상)
        private Boolean isDestruction;                  // 멸실여부
        private Boolean isMoveInRight;                  // 입주권여부
        private Integer ownerCnt;                       // 소유자수
        private Integer userProportion;                 // 본인지분비율
        private Boolean isAdjustmentTargetArea;         // 조정대상지역여부
        private Integer ownHouseCnt;                    // 보유주택수
        // TODO. 추가질의및응답내용 추가
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 3. 보유주택정보
    public static class OwnHouseInfoResponseResponse {
        private Integer seq;                            // 번호
        private Long houseId;                           // 주택ID
        private String houseType;                       // 주택유형
        private String houseName;                       // 주택명
        private String detailAdr;                       // 상세주소
        private String buyDate;                         // 취득일자
        private String remark;                          // 비고
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 4-1. (취득세)계산결과정보
    public static class CalculationBuyResultInfoResponse {
        private String userProportion;                  // 본인지분비율
        private String buyPrice;                        // 취득가액
        private String buyTaxRate;                      // 취득세율
        private String buyTaxPrice;                     // 취득세액
        private String eduTaxRate;                      // 지방교육세율
        private String eduTaxPrice;                     // 지방교육세액
        private String eduDiscountPrice;                // 지방교육세감면액
        private String agrTaxRate;                      // 농어촌특별세율
        private String agrTaxPrice;                     // 농어촌특별세액
        private String totalTaxPrice;                   // 총납부세액
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 4-2. (양도소득세)계산결과정보
    public static class CalculationSellResultInfoResponse {
        private String userProportion;                  // 본인지분비율
        private String buyPrice;                        // 취득가액
        private String buyDate;                         // 취득일자
        private String sellPrice;                       // 양도가액
        private String sellDate;                        // 양도일자
        private String necExpensePrice;                 // 필요경비금액
        private String sellProfitPrice;                 // 양도차익금액
        private String retentionPeriod;                 // 보유기간
        private String nonTaxablePrice;                 // 비과세대상양도차익금액
        private String taxablePrice;                    // 과세대상양도차익금액
        private String longDeductionPrice;              // 장기보유특별공제금액
        private String sellIncomePrice;                 // 양도소득금액
        private String basicDeductionPrice;             // 기본공제금액
        private String taxableStdPrice;                 // 과세표준금액
        private String sellTaxRate;                     // 양도소득세율
        private String localTaxRate;                    // 지방소득세율
        private String progDeductionPrice;              // 누진공제금액
        private String sellTaxPrice;                    // 양도소득세액
        private String localTaxPrice;                   // 지방소득세액
        private String totalTaxPrice;                   // 총납부세액
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 5. 해설정보
    public static class CalculationCommentaryInfoResponse {
        private Integer seq;                            // 번호
        private String commentaryContent;               // 해설내용
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 6. 상담진행정보
    public static class ConsultingProgressInfoResponse {
        private ConsultingStatus consultingStatus;      // 상담진행상태(3개 구분 값만 사용 > WAITING:대기, PROGRESS:시작, FINISH:종료)
        private Long paymentAmount;                     // 결제금액
        private String consultingContent;               // 상담내용(상담자 작성)
        private String remark;                          // 비고
    }

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    @ToString
    // 7. 상담현황정보
    public static class ConsultingStatusInfoResponse {
        private String consultingRequestDatetime;       // 상담요청일시
        private String consultingReservationDatetime;   // 상담예약일시
        private String consultingStartDatetime;         // 상담시작일시
        private String consultingEndDatetime;           // 상담완료일시
        private String consultingCancelDatetime;        // 상담취소일시
        private LastModifierType lastModifier;          // 최종변경자(ADMINISTRATOR:관리자, CONSULTANT:상담자, USER:사용자)
    }
}
