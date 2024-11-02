let consultantId;
let consultingTimeUnit;
let consultingScheduleDateInfo;

$(document).ready(function(){
    gnb3_On();
    side3_On();

    // 전역변수 초기화
    initGlobalVariables();

    // 이벤트 설정
    setEvent();
    
    // 전체 데이터 및 컴포넌트 초기화
    initAllDataAndComponent();

    // 상담 날짜 선택 조회(오늘 날짜 기준)
    getConsultingScheduleWeekInfo(consultantId, null, null);
});

// 전역변수 초기화
function initGlobalVariables(){
    consultantId = 1;
    consultingTimeUnit = 30;
    consultingScheduleDateInfo = null;
}

// 이벤트 설정
function setEvent(){
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

    // 상담날짜선택 이벤트
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

    // 상담날짜선택 이전 날짜 보기('<') 이벤트
    $("#date_arrow_left").click(function(){
        initAllDataAndComponent();
        getConsultingScheduleWeekInfo(1, $("#date_1").val(), "previous");
    });

    // 상담날짜선택 이후 날짜 보기('>') 이벤트
    $("#date_arrow_right").click(function(){
        initAllDataAndComponent();
        getConsultingScheduleWeekInfo(1, $("#date_1").val(), "next");
    });

    // 예약가능여부 변경 이벤트
    $("#is_reservation_available_sel").change(function(){
        // 예약가능여부 : 부
        if($("#is_reservation_available_sel").val() === "0") {
            if(checkReservedValidation()){
                // 상담시간설정 초기화
                setReservationAvailableTime(null, false);

                // 상담불가시간설정 초기화
                setReservationUnavailableTime(null, false);
            }else{
                $("#is_reservation_available_sel").val("1").prop("selected", true);
            }
        }
        // 예약가능여부 : 여
        else{
            // 상담시간설정 세팅
            setReservationAvailableTime(consultingScheduleDateInfo, true);

            // 상담불가시간설정 세팅
            setReservationUnavailableTime(consultingScheduleDateInfo, true);
        }
    });

    // 상담시간설정 변경 이벤트
    $("#start_hour_sel").change(function(){
        if(checkTimeValidation()){
            consultingScheduleDateInfo.reservationAvailableStartTimeHour = $("#start_hour_sel").val();
        }else{
            $("#start_hour_sel").val(consultingScheduleDateInfo.reservationAvailableStartTimeHour).prop("selected", true);
        }
    });
    $("#start_minute_sel").change(function(){
        if(checkTimeValidation()){
            consultingScheduleDateInfo.reservationAvailableStartTimeMinute = $("#start_minute_sel").val();
        }else{
            $("#start_minute_sel").val(consultingScheduleDateInfo.reservationAvailableStartTimeMinute).prop("selected", true);
        }
    });
    $("#end_hour_sel").change(function(){
        if(checkTimeValidation()){
            consultingScheduleDateInfo.reservationAvailableEndTimeHour = $("#end_hour_sel").val();
        }else{
            $("#end_hour_sel").val(consultingScheduleDateInfo.reservationAvailableEndTimeHour).prop("selected", true);
        }
    });
    $("#end_minute_sel").change(function(){
        if(checkTimeValidation()){
            consultingScheduleDateInfo.reservationAvailableEndTimeMinute = $("#end_minute_sel").val();
        }else{
            $("#end_minute_sel").val(consultingScheduleDateInfo.reservationAvailableEndTimeMinute).prop("selected", true);
        }
    });

    // 상담불가시간설정 이벤트
    $('.each_time').each(function(index){
        $(this).attr('time-index', index);
    }).click(function(){
        var index = $(this).attr('time-index');
        if($('.each_time[time-index=' + index + ']').hasClass('clicked') === true){
            $('.each_time[time-index=' + index + ']').removeClass('clicked');
        }else{
            $('.each_time[time-index=' + index + ']').addClass('clicked');
        }
    });
}

// 예약 유효성 체크
function checkReservedValidation(){
    const timeInfoList = consultingScheduleDateInfo.consultingEachTimeInfoList;

    if(timeInfoList != null && timeInfoList.length > 0){
        for(let i=0; i<timeInfoList.length; i++){
            const reservationStatus = timeInfoList[i].reservationStatus;

            // '예약완료'인 경우
            if(reservationStatus === "2"){
                alert("예약완료 건이 존재하여 예약가능여부를 '부'로 변경할 수 없습니다.");
                return false;
            }
        }
    }

    return true;
}

// 시간 유효성 체크
function checkTimeValidation(){
    const startTime = parseInt($("#start_hour_sel").val() + $("#start_minute_sel").val());
    const endTime = parseInt($("#end_hour_sel").val() + $("#end_minute_sel").val());

    if(startTime >= endTime){
        alert("상담시작시간은 상담종료시간보다 이전이어야 합니다.");
        return false;
    }

    const timeInfoList = consultingScheduleDateInfo.consultingEachTimeInfoList;

    if(timeInfoList != null && timeInfoList.length > 0){
        for(let i=0; i<timeInfoList.length; i++){
            const consultingTime = parseInt((timeInfoList[i].consultingTime).replace(':', ''));
            const reservationStatus = timeInfoList[i].reservationStatus;

            if(startTime > consultingTime || endTime < consultingTime){
                // '예약완료'인 경우
                if(reservationStatus === "2"){
                    alert("예약완료 건이 존재하여 해당 시간으로 상담시간을 변경할 수 없습니다.");
                    return false;
                }
            }
        }
    }

    return true;
}

// 전체 데이터 및 컴포넌트 초기화
function initAllDataAndComponent(){
    initConsultingScheuduleWeekInfo();  // 상담날짜선택 초기화
    initIsReservationAvailable();       // [상담상세설정] 예약가능여부 초기화
    initReservationAvailableTime();     // [상담상세설정] 상담시간설정 초기화
    initReservationUnavailableTime();   // 상담불가시간설정 초기화
}

// 상담 날짜 선택 초기화
function initConsultingScheuduleWeekInfo(){
    $('.date').each(function(index){
        if($('.date[date-index=' + index + ']').hasClass('clicked') === true){
            $('.date[date-index=' + index + ']').removeClass('clicked');
        }
        if($('.date[date-index=' + index + ']').hasClass('disabled') === false){
            $('.date[date-index=' + index + ']').addClass('disabled');
        }
    });
}

// [상담 상세 설정] 예약가능여부 초기화
function initIsReservationAvailable(){
    // 예약가능여부 '부'로 세팅
    $("#is_reservation_available_sel").val("0").prop("selected", true);
}

// [상담 상세 설정] 상담시간설정 초기화
function initReservationAvailableTime(){
    // 상담시간설정 컴포넌트 초기화
    $("#start_hour_sel").prop("disabled", true);
    $("#start_minute_sel").prop("disabled", true);
    $("#end_hour_sel").prop("disabled", true);
    $("#end_minute_sel").prop("disabled", true);

    // 상담시간설정 데이터 초기화
    $("#start_hour_sel").val("09").prop("selected", true);
    $("#start_minute_sel").val("00").prop("selected", true);
    $("#end_hour_sel").val("17").prop("selected", true);
    $("#end_minute_sel").val("30").prop("selected", true);
}

// 상담불가시간설정 초기화
function initReservationUnavailableTime(){
    $('.each_time').each(function(index){
        if($('.each_time[time-index=' + index + ']').hasClass('clicked') === true){
            $('.each_time[time-index=' + index + ']').removeClass('clicked');
        }
        if($('.each_time[time-index=' + index + ']').hasClass('disabled') === false){
            $('.each_time[time-index=' + index + ']').addClass('disabled');
        }
    });
}

// 상담 날짜 선택(일주일 범위의 날짜 정보) 조회
function getConsultingScheduleWeekInfo(consultantId, currentWeekStartDate, action) {
    console.log("getConsultingScheduleWeekInfo : 상담 날짜 선택(일주일 범위의 날짜 정보) 조회");

    //initConsultingScheuduleWeekInfo();

    const params = {};
    params.consultantId = consultantId;
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
                                if($('.date[date-index='+i+']').hasClass('disabled') === true){
                                    $('.date[date-index='+i+']').removeClass('disabled');
                                }
                            }

                            // Default 선택 날짜 세팅
                            if(isSelected){
                                if($('.date[date-index='+i+']').hasClass('clicked') === false){
                                    $('.date[date-index='+i+']').addClass('clicked');
                                }
                                selectedDate = date;
                            }
                        }

                        // 상담 상세 설정(예약가능여부 및 상담시간설정)
                        getConsultingScheduleDateInfo(consultantId, selectedDate);
                    }
                }else{
                    alert("상담 일정 조회 중 오류가 발생했습니다.(상담 날짜 선택(일주일 범위의 날짜 정보) 조회-ERROR-1)");
                }
            }else{
                alert("상담 일정 조회 중 오류가 발생했습니다.(상담 날짜 선택(일주일 범위의 날짜 정보) 조회-ERROR-2)");
            }
        },
        error:function(e){
            alert("상담 일정 조회 중 오류가 발생했습니다.(상담 날짜 선택(일주일 범위의 날짜 정보) 조회-ERROR-3)");
            console.log("ERROR-3 : " + JSON.stringify(e))
        }
    });
}

// 상담상세설정 및 상담불가시간 설정 조회
function getConsultingScheduleDateInfo(consultantId, searchDate) {
    console.log("getConsultingScheduleDateInfo : 상담상세설정 및 상담불가시간 설정 조회");

    const params = {};
    params.consultantId = consultantId;
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
                    consultingScheduleDateInfo = data;

                    const isReservationAvailable = data.isReservationAvailable;

                    // 예약가능여부 세팅
                    if(isReservationAvailable){
                        $("#is_reservation_available_sel").val("1");    // 예약가능여부 : 여
                    }else{
                        $("#is_reservation_available_sel").val("0");    // 예약가능여부 : 부
                    }

                    // 상담시간설정 초기화
                    initReservationAvailableTime();

                    // 상담시간설정 세팅
                    setReservationAvailableTime(consultingScheduleDateInfo, isReservationAvailable);
                    
                    // 상담불가시간설정 세팅
                    initReservationUnavailableTime();
                    setReservationUnavailableTime(consultingScheduleDateInfo, isReservationAvailable);
                }else{
                    alert("상담 일정 조회 중 오류가 발생했습니다.(상담상세설정 및 상담불가시간 설정 조회-ERROR-1)");
                }
            }else{
                alert("상담 일정 조회 중 오류가 발생했습니다.(상담상세설정 및 상담불가시간 설정 조회-ERROR-2)");
            }
        },
        error:function(e){
            alert("상담 일정 조회 중 오류가 발생했습니다.(상담상세설정 및 상담불가시간 설정 조회-ERROR-3)");
            console.log("ERROR-3 : " + JSON.stringify(e));
        }
    });
}

// 상담시간설정 세팅
function setReservationAvailableTime(data, flag){
    if(flag){
        // 상담시간설정 활성화 처리
        $("#start_hour_sel").prop("disabled", false);
        $("#start_minute_sel").prop("disabled", false);
        $("#end_hour_sel").prop("disabled", false);
        $("#end_minute_sel").prop("disabled", false);

        const reservationAvailableStartTimeHour = data.reservationAvailableStartTimeHour;
        const reservationAvailableStartTimeMinute = data.reservationAvailableStartTimeMinute;
        const reservationAvailableEndTimeHour = data.reservationAvailableEndTimeHour;
        const reservationAvailableEndTimeMinute = data.reservationAvailableEndTimeMinute;

        if(nullChk(reservationAvailableStartTimeHour) && nullChk(reservationAvailableStartTimeMinute) && nullChk(reservationAvailableEndTimeHour) && nullChk(reservationAvailableEndTimeMinute)){
            $("#start_hour_sel").val(reservationAvailableStartTimeHour).prop("selected", true);
            $("#start_minute_sel").val(reservationAvailableStartTimeMinute).prop("selected", true);
            $("#end_hour_sel").val(reservationAvailableEndTimeHour).prop("selected", true);
            $("#end_minute_sel").val(reservationAvailableEndTimeMinute).prop("selected", true);
        }else{
            $("#start_hour_sel").val("09").prop("selected", true);
            $("#start_minute_sel").val("00").prop("selected", true);
            $("#end_hour_sel").val("17").prop("selected", true);
            $("#end_minute_sel").val("30").prop("selected", true);

            consultingScheduleDateInfo.reservationAvailableStartTimeHour = $("#start_hour_sel").val();
            consultingScheduleDateInfo.reservationAvailableStartTimeMinute = $("#start_minute_sel").val();
            consultingScheduleDateInfo.reservationAvailableEndTimeHour = $("#end_hour_sel").val();
            consultingScheduleDateInfo.reservationAvailableEndTimeMinute = $("#end_minute_sel").val();
        }
    }else{
        $("#start_hour_sel").prop("disabled", true);
        $("#start_minute_sel").prop("disabled", true);
        $("#end_hour_sel").prop("disabled", true);
        $("#end_minute_sel").prop("disabled", true);
    }
}


// 상담불가시간설정 세팅
function setReservationUnavailableTime(date, flag){
    const timeInfoList = (date != null) ? date.consultingEachTimeInfoList : null;
    const startTime = parseInt($("#start_hour_sel").val() + $("#start_minute_sel").val());
    const endTime = parseInt($("#end_hour_sel").val() + $("#end_minute_sel").val());

    if(flag){
        $('.each_time').each(function(index){
            const thisTime = parseInt($('.each_time[time-index=' + index + ']').text().replace(':',''));

            if(startTime <= thisTime && endTime >= thisTime){
                if($('.each_time[time-index=' + index + ']').hasClass('disabled') === true){
                    $('.each_time[time-index=' + index + ']').removeClass('disabled');
                }
            }
        });

        if(timeInfoList != null && timeInfoList.length > 0){
            for(let i=0; i<timeInfoList.length; i++){
                const consultingTime = timeInfoList[i].consultingTime;
                const consultingTimeUnit = timeInfoList[i].consultingTimeUnit;
                const reservationStatus = timeInfoList[i].reservationStatus;

                const consultingTimeStr = consultingTime.replace(':', '');
                const consultingTimeNumber = parseInt(consultingTimeStr);

                if(startTime <= consultingTimeNumber && endTime >= consultingTimeNumber){
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
    }else{
        initReservationUnavailableTime();
    }
}

// 상담 일정 초기화
function initData(){
    // 전체 데이터 및 컴포넌트 초기화
    initAllDataAndComponent();

    // 상담 날짜 선택 조회(오늘 날짜 기준)
    getConsultingScheduleWeekInfo(consultantId, null, null);
}

// 상담 일정 저장
function saveData(){
    let reservationDate = "";
    let isReservationAvailable = false;
    let reservationAvailableStartTime = "";
    let reservationAvailableEndTime = "";
    let reservationUnavailableTime = "";

    for(let i=1; i<=7; i++){
        if($("#dateStr_"+i).hasClass("clicked")){
            reservationDate = $("#date_"+i).val();
            break;
        }
    }

    isReservationAvailable = $("#is_reservation_available_sel").val() !== "0";
    reservationAvailableStartTime = $("#start_hour_sel").val() + ":" + $("#start_minute_sel").val() + ":00";
    reservationAvailableEndTime = $("#end_hour_sel").val() + ":" + $("#end_minute_sel").val() + ":00";

    $('.each_time').each(function(index){
        if($('.each_time[time-index=' + index + ']').hasClass('clicked') === true){
            if(nullChk(reservationUnavailableTime)){
                reservationUnavailableTime += ",";
            }
            reservationUnavailableTime += $('.each_time[time-index=' + index + ']').text();
        }
    });

    const params = {};
    params.reservationDate = reservationDate;
    params.consultantId = consultantId;
    params.isReservationAvailable = isReservationAvailable;
    if(isReservationAvailable){
        params.reservationAvailableStartTime = reservationAvailableStartTime;
        params.reservationAvailableEndTime = reservationAvailableEndTime;
        params.reservationTimeUnit = consultingTimeUnit;
        params.reservationUnavailableTime = reservationUnavailableTime;
    }

    $.ajax({
        type: "POST",
        url: "/consulting/saveInfo",
        contentType : "application/json",
        data: JSON.stringify(params),
        success:function(ret){
            console.log("상담일정 정보 저장 : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N"){
                    if(nullChk(data.result)){
                        alert(data.result);
                    }else{
                        alert("상담 일정 저장 완료(메시지 출력 오류)");
                    }
                }else{
                    alert("상담 일정 저장 중 오류가 발생했습니다.(상담 일정 저장-ERROR-1)");
                }
            }else{
                alert("상담 일정 저장 중 오류가 발생했습니다.(상담 일정 저장-ERROR-2)");
            }
        },
        error:function(e){
            alert("상담 일정 저장 중 오류가 발생했습니다.(상담 일정 저장-ERROR-3)");
            console.log("ERROR-3 : " + JSON.stringify(e));
        }
    });
}