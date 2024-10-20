let consultingEachTimeInfoList = null;

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

    // 1. 상담 날짜 선택 event
    $('.date').each(function(index){
        $(this).attr('date-index', index);
    }).click(function(){
        var index = $(this).attr('date-index');
        $('.date[date-index=' + index + ']').addClass('clicked');
        $('.date[date-index!=' + index + ']').removeClass('clicked');

        const selectedDate = $("#date_"+(parseInt(index)+1)).val();
        getConsultingScheduleDateInfo(1, selectedDate);
        console.log("selectedDate : " + selectedDate);
    });

    // 2. 상담 상세 설정 event
    $('.each_time').each(function(index){
        $(this).attr('time-index', index);
    }).click(function(){
        var index = $(this).attr('time-index');
        if($('.each_time[time-index=' + index + ']').hasClass('clicked') === true){
            $('.each_time[time-index=' + index + ']').removeClass('clicked');
        }else{
            $('.each_time[time-index=' + index + ']').addClass('clicked');
        }
        //$('.each_time[time-index=' + index + ']').addClass('clicked');
        //$('.each_time[time-index!=' + index + ']').removeClass('clicked');
    });

    /**
     * 상담 일정 정보 조회
     *
     * REQUEST
     * - 조회구분(1:상담일자정보조회, 2:상담시간정보조회)
     *
     * RESPONSE
     * - 상담 날짜 선택 범위 타이틀(ex.2024.09.04 ~ 2024.09.10)
     * - 상담 주 날짜값 7개(ex.04,05,06,07,08,09,10)
     * - 상담 주 일자 데이터 7개(ex.2024-09-04,2024-09-05,2024-09-06,2024-09-07,2024-09-08,2024-09-09,2024-09-10)
     * - 예약가능여부 값(여,부)
     * - 상담시간설정 데이터(ex.09:00,17:30)
    */

    /**
     * 상담 일정 정보 조회
     *
     * REQUEST
     * - 조회구분(1:상담일자정보조회, 2:상담시간정보조회)
     * - 상담 선택 일자(2024-09-07)
     *
     * RESPONSE
     * - 상담 불가 시간 List(1:예약대기, 2:예약완료, 3:예약불가)
     * - 예약시간단위(30)
     */

    // 상담날짜선택 이전 날짜 보기
    $("#date_arrow_left").click(function(){
        getConsultingScheduleWeekInfo(1, $("#date_1").val(), "previous");
    });

    // 상담날짜선택 이후 날짜 보기
    $("#date_arrow_right").click(function(){
        getConsultingScheduleWeekInfo(1, $("#date_1").val(), "next");
    });

    $("#is_reservation_available_sel").change(function(){
        if($("#is_reservation_available_sel").val() === "0"){
            $("#start_hour_sel").prop("disabled", true);
            $("#start_minute_sel").prop("disabled", true);
            $("#end_hour_sel").prop("disabled", true);
            $("#end_minute_sel").prop("disabled", true);

            $('.each_time').each(function(index){
                if($('.each_time[time-index=' + index + ']').hasClass('disabled') === false){
                    $('.each_time[time-index=' + index + ']').addClass('disabled');
                }
            });
        }else{
            $("#start_hour_sel").prop("disabled", false);
            $("#start_minute_sel").prop("disabled", false);
            $("#end_hour_sel").prop("disabled", false);
            $("#end_minute_sel").prop("disabled", false);

            // 상담불가시간설정 세팅
            setConsultingUnavailableTime();
            /*$('.each_time').each(function(index){
                if($('.each_time[time-index=' + index + ']').hasClass('disabled') === true){
                    $('.each_time[time-index=' + index + ']').removeClass('disabled');
                }
            });*/
        }
    });

    // 상담날짜 선택 초기화
    getConsultingScheduleWeekInfo(1, null, null);
});

function initConsultingScheuduleWeekInfo(){
    for(let i=0; i<7; i++) {
        $('.date[date-index='+i+']').addClass('disabled');
        $('.date[date-index='+i+']').removeClass('clicked');
    }
}

function getConsultingScheduleWeekInfo(consultantId, currentWeekStartDate, action) {
    console.log("getConsultingScheduleWeekInfo called");

    initConsultingScheuduleWeekInfo();

    const params = {};
    params.consultantId = nullChk(consultantId) ? consultantId : 1;
    if(nullChk(currentWeekStartDate)) params.currentWeekStartDate = currentWeekStartDate;
    if(nullChk(action)) params.action = action;

    $.ajax({
        type: "GET",
        url: "/consulting/weekInfo",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("상담일정 주정보 조회 : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N"){
                    $("#period").text(data.consultingWeekInfo);
                    const list = data.consultingEachDateInfoList;
                    let selectedDate = "";

                    if(list != null && list.length > 0){
                        for(let i=0; i<list.length; i++) {
                            const dateStr = list[i].dateStr;
                            const dayOfWeekStr = list[i].dayOfWeekStr;
                            const date = list[i].date;
                            const isReservationAvailable = list[i].isReservationAvailable;
                            const isSelected = list[i].isSelected;
                            const seq = i+1;

                            $("#dateStr_"+seq).text(dateStr);
                            $("#dayOfWeekStr_"+seq).text(dayOfWeekStr);
                            $("#date_"+seq).val(date);

                            // 예약가능여부 세팅
                            if(isReservationAvailable){
                                $('.date[date-index='+i+']').removeClass('disabled');
                            }

                            // Default 선택 날짜 세팅
                            if(isSelected){
                                $('.date[date-index='+i+']').addClass('clicked');
                                selectedDate = date;
                            }
                        }

                        getConsultingScheduleDateInfo(consultantId, selectedDate);
                    }
                }else{
                    alert("ERROR-1");
                }
            }else{
                alert("ERROR-2");
            }
        },
        error:function(e){
            console.log("오류가 발생했습니다 - " + JSON.stringify(e));
            alert("ERROR-3");
        }
    });
}

function initConsultingScheuduleDateInfo(){
    $("#is_reservation_available_sel").val("0");
    $("#start_hour_sel").prop("disabled", true);
    $("#start_minute_sel").prop("disabled", true);
    $("#end_hour_sel").prop("disabled", true);
    $("#end_minute_sel").prop("disabled", true);

    $('.each_time').each(function(index){
        $('.each_time[time-index=' + index + ']').addClass('disabled');
    })
}

function getConsultingScheduleDateInfo(consultantId, searchDate) {
    console.log("getConsultingScheduleDateInfo called");

    initConsultingScheuduleDateInfo();

    const params = {};
    params.consultantId = nullChk(consultantId) ? consultantId : 1;
    params.searchDate = searchDate;

    $.ajax({
        type: "GET",
        url: "/consulting/dateInfo",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("상담일정 일자정보 조회 : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N"){
                    const isReservationAvailable = data.isReservationAvailable;
                    const reservationAvailableStartTimeHour = data.reservationAvailableStartTimeHour;
                    const reservationAvailableStartTimeMinute = data.reservationAvailableStartTimeMinute;
                    const reservationAvailableEndTimeHour = data.reservationAvailableEndTimeHour;
                    const reservationAvailableEndTimeMinute = data.reservationAvailableEndTimeMinute;
                    consultingEachTimeInfoList = data.consultingEachTimeInfoList;

                    // 예약가능여부 세팅
                    if(isReservationAvailable){
                        $("#is_reservation_available_sel").val("1");

                        $("#start_hour_sel").prop("disabled", false);
                        $("#start_minute_sel").prop("disabled", false);
                        $("#end_hour_sel").prop("disabled", false);
                        $("#end_minute_sel").prop("disabled", false);
                    }else{
                        $("#is_reservation_available_sel").val("0");

                        $("#start_hour_sel").prop("disabled", true);
                        $("#start_minute_sel").prop("disabled", true);
                        $("#end_hour_sel").prop("disabled", true);
                        $("#end_minute_sel").prop("disabled", true);
                    }
                    
                    // 상담시간설정 세팅
                    $("#start_hour_sel").val(reservationAvailableStartTimeHour);
                    $("#start_minute_sel").val(reservationAvailableStartTimeMinute);
                    $("#end_hour_sel").val(reservationAvailableEndTimeHour);
                    $("#end_minute_sel").val(reservationAvailableEndTimeMinute);
                    
                    // 상담불가시간설정 세팅
                    setConsultingUnavailableTime();
                    
                    const list = data.consultingEachDateInfoList;

                    if(list != null && list.length > 0){
                        for(let i=0; i<list.length; i++) {
                            const dateStr = list[i].dateStr;
                            const dayOfWeekStr = list[i].dayOfWeekStr;
                            const date = list[i].date;
                            const isReservationAvailable = list[i].isReservationAvailable;
                            const isSelected = list[i].isSelected;
                            const seq = i+1;

                            $("#dateStr_"+seq).text(dateStr);
                            $("#dayOfWeekStr_"+seq).text(dayOfWeekStr);
                            $("#date_"+seq).val(date);

                            // 예약가능여부 세팅
                            if(isReservationAvailable){
                                $('.date[date-index='+i+']').removeClass('disabled');
                            }

                            // Default 선택 날짜 세팅
                            if(isSelected){
                                $('.date[date-index='+i+']').addClass('clicked');
                            }
                        }
                    }
                }else{
                    alert("ERROR-1");
                }
            }else{
                alert("ERROR-2");
            }
        },
        error:function(e){
            console.log("오류가 발생했습니다 - " + JSON.stringify(e));
            alert("ERROR-3");
        }
    });
}

// 상담불가시간설정 세팅
function setConsultingUnavailableTime(){
    if(consultingEachTimeInfoList != null && consultingEachTimeInfoList.length > 0){
        for(let i=0; i<consultingEachTimeInfoList.length; i++){
            const consultingTime = consultingEachTimeInfoList[i].consultingTime;
            const consultingTimeUnit = consultingEachTimeInfoList[i].consultingTimeUnit;
            const reservationStatus = consultingEachTimeInfoList[i].reservationStatus;

            let consultingTimeStr = consultingTime.replace(':', '');

            // 예약대기
            if(reservationStatus === "1"){
                if($("#time_"+consultingTimeStr).hasClass('disabled') === true){
                    $("#time_"+consultingTimeStr).removeClass('disabled');
                }
            }
            // 예약완료
            else if(reservationStatus === "2"){
                if($("#time_"+consultingTimeStr).hasClass('disabled') === false){
                    $("#time_"+consultingTimeStr).addClass('disabled');
                }
            }
            // 예약불가
            else if(reservationStatus === "3"){
                if($("#time_"+consultingTimeStr).hasClass('disabled') === true){
                    $("#time_"+consultingTimeStr).removeClass('disabled');
                }
                if($("#time_"+consultingTimeStr).hasClass('clicked') === false){
                    $("#time_"+consultingTimeStr).addClass('clicked');
                }
            }
        }
    }
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