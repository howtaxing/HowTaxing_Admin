$(document).ready(function(){
    gnb3_On();
    side3_On();

    // 닫기
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

    // 상담번호 클릭
    $('.url_link').on('click', function(){
        const id = $(this).attr('data-value');
        location.href = "/moveConsultingReservationDetail?consultingReservationId=" + id;
    });

    init();
});

function init() {
    $("#consulting_reservation_id").empty();
    getConsultingReservationInfoList();
}

function getConsultingReservationInfoList(){

    let listHtml = "";
    const params = {};
    //params.consultantId = 1;

    $.ajax({
        type: "POST",
        url: "/consulting/reservationList",
        contentType : "application/json",
        data: JSON.stringify(params),
        success:function(ret){
            console.log("상담예약정보 목록 조회 : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;
                const listCnt = data.listCnt;
                const list = data.consultingReservationInfoResponseList;

                if(errYn === "N"){
                    if(listCnt > 0){
                        list.forEach(function(item, index){
                            let consultantName = "-";
                            if(item.consultantId === 1){
                                consultantName = "이민정음 세무사";
                            }else if(item.consultantId === 2){
                                consultantName = "박지은 세무사";
                            }else if(item.consultantId === 2){
                                consultantName = "윤준수 세무사";
                            }

                            let consultingType = "-";
                            if(nullChk(item.consultingType)){
                                consultingType = item.consultingType;
                            }

                            let consultingStatus = item.consultingStatus;

                            let fontStyle = "font_general";
                            if(consultingStatus === "결제완료"){
                                fontStyle = "font_blue";
                            }else if(consultingStatus === "상담대기"){
                                fontStyle = "font_orange";
                            }else if(consultingStatus === "상담중"){
                                fontStyle = "font_red";
                            }

                            listHtml += "<tr>";
                            listHtml += "   <td>" + item.seq + "</td>";
                            //listHtml += "   <td><a class='url_link' data-value='" + item.consultingReservationId + "'>" + item.consultingReservationId + "</td>";
                            listHtml += "   <td><a class='url_link bold' onclick='goDetail(" + item.consultingReservationId + ")'>" + item.consultingReservationId + "</td>";
                            listHtml += "   <td>" + item.reservationDate + "</td>";
                            listHtml += "   <td>" + item.reservationTime + "</td>";
                            listHtml += "   <td>" + consultingType + "</td>";
                            listHtml += "   <td>" + item.customerName + "</td>";
                            listHtml += "   <td>" + consultantName + "</td>";
                            listHtml += "   <td class='" + fontStyle + "'>" + consultingStatus + "</td>";
                            listHtml += "</tr>";
                        });

                        $("#consulting_reservation_list").html(listHtml);
                    }else{
                        initConsultingReservationInfoList();
                    }
                }else{
                    alert("상담 일정 저장 중 오류가 발생했습니다.(상담 일정 저장-ERROR-1)");
                    initConsultingReservationInfoList();
                }
            }else{
                alert("상담 일정 저장 중 오류가 발생했습니다.(상담 일정 저장-ERROR-2)");
                initConsultingReservationInfoList();
            }
        },
        error:function(e){
            alert("상담 일정 저장 중 오류가 발생했습니다.(상담 일정 저장-ERROR-3)");
            console.log("ERROR-3 : " + JSON.stringify(e));
            initConsultingReservationInfoList();
        }
    });
}

function goDetail(consultingReservationId){
    location.href = "/moveConsultingReservationDetail?consultingReservationId=" + consultingReservationId;
}

function initConsultingReservationInfoList(){
    listHtml += "<tr>";
    listHtml += "<td colspan='8'>상담 예약 현황 조회 결과가 존재하지 않습니다.</td>";
    listHtml += "</tr>";

    $("#consulting_reservation_list").html(listHtml);
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