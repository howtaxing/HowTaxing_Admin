let orgConsultingReservationId = "";
let orgConsultingStatus = "";

$(document).ready(function(){
    const urlParams = new URLSearchParams(window.location.search);
    orgConsultingReservationId = urlParams.get('consultingReservationId');

    gnb3_On();
    side3_On();

    // 이벤트 등록
    registEvent();

    // 상담 상세 정보 세팅
    setConsultingDetailInfo(orgConsultingReservationId);
});

/* 이벤트 등록 */
function registEvent(){
    // 닫기 button
    $(".popCloseBtn").click(function (){
        $("#popup_layer").css("display", "none");
    });

    $(".menu_wrap li").hover(function (){
        side3_Off();
    }, function (){
        side3_On();
    });

    $(".navbar_menu li").hover(function (){
        gnb3_Off();
    }, function (){
        gnb3_On();
    });

    // 상담 진행 select
    /*$("#consulting_status_sel").change(function(){
    });*/

    // 적용 button (상담 진행)
    $("#btn_consulting_change").click(function(){
        const consultingStatus = $("#consulting_status_sel").val();

        if(consultingStatus === "WAITING"){
            $("#consulting_content").prop("disabled", false);
            $("#consulting_remark").prop("disabled", false);
            $("#btn_save").hide();
        }else if(consultingStatus === "PROGRESS"){
            $("#consulting_content").prop("disabled", false);
            $("#consulting_remark").prop("disabled", false);
            $("#btn_save").hide();
        }else if(consultingStatus === "FINISH"){
            //$("#consulting_status_sel").prop("disabled", true);
            //$("#btn_consulting_change").prop("disabled", true);
            $("#consulting_content").prop("disabled", false);
            $("#consulting_remark").prop("disabled", false);
            $("#btn_save").show();
        }

        if(orgConsultingStatus !== consultingStatus){
            orgConsultingStatus = consultingStatus;
            setConsultingStatusInfo(consultingStatus);
            changeConsultingStatus(consultingStatus);
        }
    });
}

// 상담예약상세보기 세팅
function setConsultingDetailInfo(consultingReservationId){
    let params = {};
    params.consultingReservationId = consultingReservationId;

    $.ajax({
        type: "GET",
        url: "/consulting/reservationDetail",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("상담예약상세 조회 : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N"){
                    const baseInfo = data.consultingBaseInfoResponse;
                    const progressInfo = data.consultingProgressInfoResponse;
                    const statusInfo = data.consultingStatusInfoResponse;

                    if(nullChk(baseInfo)){
                        $("#consulting_reservation_id").text(baseInfo.consultingReservationId);
                        $("#reservation_date").text(baseInfo.reservationDate);
                        $("#consulting_type").text(changeEmpty(baseInfo.consultingType, "-"));
                        $("#reservation_time").text(baseInfo.reservationTime);
                        $("#customer_name").text(baseInfo.customerName);
                        $("#customer_phone").text(convertToPhoneFormat(baseInfo.customerPhone));
                        $("#consulting_inflow_path").text(changeEmpty(baseInfo.consultingInflowPath, "-"));
                        $("#consulting_request_content").html(changeEmpty(baseInfo.consultingRequestContent, "-"));
                    }

                    if(nullChk(progressInfo)){
                        orgConsultingStatus = nullChk(progressInfo.consultingStatus) ? progressInfo.consultingStatus : "PAYMENT_COMPLETED";

                        $("#consulting_status_sel").val(orgConsultingStatus);
                        $("#payment_amount").val(changeEmpty(progressInfo.paymentAmount, 0));
                        $("#consulting_content").text(progressInfo.consultingContent);
                        $("#consulting_remark").text(progressInfo.remark);

                        // 상담 상태 값으로 화면 제어
                        if(orgConsultingStatus === "FINISH" || orgConsultingStatus === "PAYMENT_COMPLETED"){
                            $("#consulting_status_sel").prop("disabled", true);
                            $("#btn_consulting_change").prop("disabled", true);
                            $("#consulting_content").prop("disabled", true);
                            $("#consulting_remark").prop("disabled", true);
                        }else{
                            $("#consulting_status_sel").prop("disabled", false);
                            $("#btn_consulting_change").prop("disabled", false);
                            $("#consulting_content").prop("disabled", false);
                            $("#consulting_remark").prop("disabled", false);
                        }

                        setConsultingStatusInfo(orgConsultingStatus);
                        $("#btn_save").hide();
                    }

                    if(nullChk(statusInfo)){
                        $("#consulting_request_datetime").text(changeEmpty(statusInfo.consultingRequestDatetime, "-"));
                        $("#consulting_reservation_datetime").text(changeEmpty(statusInfo.consultingReservationDatetime, "-"));
                        $("#consulting_start_datetime").text(changeEmpty(statusInfo.consultingStartDatetime, "-"));
                        $("#consulting_end_datetime").text(changeEmpty(statusInfo.consultingEndDatetime, "-"));
                        $("#consulting_cancel_datetime").text(changeEmpty(statusInfo.consultingCancelDatetime, "-"));
                        $("#last_modifier").text(changeEmpty(statusInfo.lastModifier, "-"));
                    }
                }else{
                    alert("상담 예약 상세 조회 중 오류가 발생했습니다.(상담 예약 상세 조회-ERROR-1)");
                    //initConsultingReservationInfoList();
                }
            }else{
                alert("상담 예약 상세 조회 중 오류가 발생했습니다.(상담 예약 상세 조회-ERROR-2)");
                //initConsultingReservationInfoList();
            }
        },
        error:function(e){
            alert("상담 예약 상세 조회 중 오류가 발생했습니다.(상담 예약 상세 조회-ERROR-3)");
            console.log("ERROR-3 : " + JSON.stringify(e));
            //initConsultingReservationInfoList();
        }
    });
}

function setConsultingStatusInfo(consultingStatus){
    $("#consulting_status_explain").removeClass("font_red");
    $("#consulting_status_explain").removeClass("font_blue");

    if(consultingStatus === "PAYMENT_COMPLETED"){
        $("#consulting_status_explain").text("결제는 완료되었지만 고객님이 아직 상담 예약을 완료하지 않았습니다..");
    }else if(consultingStatus === "WAITING"){
        $("#consulting_status_sel option[value='PAYMENT_COMPLETED']").remove();
        $("#consulting_status_explain").text("상담 대기 중입니다. 상담을 시작하시려면 '시작'으로 변경 후 [적용] 버튼을 클릭해주세요.");
        $("#consulting_status_explain").addClass("font_blue");
    }else if(consultingStatus === "PROGRESS"){
        $("#consulting_status_sel option[value='PAYMENT_COMPLETED']").remove();
        $("#consulting_status_sel option[value='WAITING']").remove();
        $("#consulting_status_explain").text("상담 진행 중입니다. 상담을 종료하시려면 '종료'로 변경 후 [적용] 버튼을 클릭해주세요.");
        $("#consulting_status_explain").addClass("font_red");
    }else if(consultingStatus === "FINISH"){
        $("#consulting_status_sel option[value='PAYMENT_COMPLETED']").remove();
        $("#consulting_status_sel option[value='WAITING']").remove();
        $("#consulting_status_sel option[value='PROGRESS']").remove();
        $("#consulting_status_explain").text("상담이 종료되었습니다. 반드시 화면 하단의 [저장] 버튼을 클릭하여 상담 내용을 저장해주세요.");
    }
}

// 상담 상태 변경(DB)
function changeConsultingStatus(consultingStatus){
    let params = {};
    params.consultingReservationId = orgConsultingReservationId;
    params.consultingStatus = consultingStatus;

    $.ajax({
        type: "PUT",
        url: "/consulting/changeConsultingStatus",
        contentType : "application/json",
        data: JSON.stringify(params),
        success:function(ret){
            console.log("상담 상태 변경 결과 : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const resultMsg = ret.data.result;

                if(errYn === "N"){
                    console.log("상담 상태 정상 변경 : " + resultMsg);
                }else{
                    alert("상담 상태 변경 중 오류가 발생했습니다.(ERROR-1)");
                    //initConsultingReservationInfoList();
                }
            }else{
                alert("상담 상태 변경 중 오류가 발생했습니다.(ERROR-2)");
                //initConsultingReservationInfoList();
            }
        },
        error:function(e){
            alert("상담 상태 변경 중 오류가 발생했습니다.(ERROR-3)");
            console.log("ERROR-3 : " + JSON.stringify(e));
            //initConsultingReservationInfoList();
        }
    });
}

// 상담 데이터(상담 내용 및 비고) 저장
function saveConsultingData(){
    const consultingContent = $("#consulting_content").val();
    const consultingRemark = $("#consulting_remark").val();

    let params = {};
    params.consultingReservationId = orgConsultingReservationId;
    params.consultingContent = consultingContent;
    params.consultingRemark = consultingRemark;

    $.ajax({
        type: "PUT",
        url: "/consulting/saveConsultingData",
        contentType : "application/json",
        data: JSON.stringify(params),
        success:function(ret){
            console.log("상담 데이터 저장 결과 : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const resultMsg = ret.data.result;

                if(errYn === "N"){
                    console.log(resultMsg);
                    alert("상담 내용이 저장되었습니다.");
                    moveList();
                }else{
                    alert("상담 데이터 저장 중 오류가 발생했습니다.(ERROR-1)");
                    //initConsultingReservationInfoList();
                }
            }else{
                alert("상담 데이터 저장 중 오류가 발생했습니다.(ERROR-2)");
                //initConsultingReservationInfoList();
            }
        },
        error:function(e){
            alert("상담 데이터 저장 중 오류가 발생했습니다.(ERROR-3)");
            console.log("ERROR-3 : " + JSON.stringify(e));
            //initConsultingReservationInfoList();
        }
    });
}

function moveList(){
    location.href = "/moveConsultingReservationList";
}

function gnb2_On() {
    $("#gnb_menu2").attr("class", "on");
    $("#gnb_menu2 a").attr("class", "on");
}

function gnb2_Off() {
    $("#gnb_menu2").attr("class", "");
    $("#gnb_menu2 a").attr("class", "");
}

function side2_On() {
    $("#side_menu2").attr("class", "on");
}

function side2_Off() {
    $("#side_menu2").attr("class", "");
}

// 상담예약하기
function goReservation() {
    $("#popup_layer").css("display", "inline");
}

/*function goDetail(value) {
    alert("goDetail");
    alert($(value).attr('data-value'));
}*/

// 회원 로그인
function goMemberLogin() {
    window.location.href = "/moveLogin";
}

// 비회원 로그인
function goNonMemberLogin() {
    //alert("비회원 상담신청 페이지는 준비중입니다.");
    window.location.href = "/moveConsulting02";
}

// 회원가입
function goJoin() {
    window.location.href = "/moveJoin01";
}

function goPrevious(step) {
    if(step == 2){
        // 이전 버튼
        window.location.href = "/moveConsulting";
    }else if(step == 3){
        // 이전 버튼
        window.location.href = "/moveConsulting02";
    }
}

function goNext(step) {
    if(step == 1){
        // 상담 예약하기 버튼
        window.location.href = "/moveConsulting02"
    }else if(step == 2){
        // 다음 버튼
        window.location.href = "/moveConsulting03"
    }else if(step == 3){
        // 예약신청 완료 버튼
        $("#popup_layer").css("display", "inline");
    }else{
        console.log("존재하지 않는 STEP");
    }
}

function charCount1(obj) {
    var count = $(obj).val().length;
    $("#text_count_consulting_content").html(count + "/1000");
}

function charCount2(obj) {
    var count = $(obj).val().length;
    $("#text_count_remark").html(count + "/255");
}