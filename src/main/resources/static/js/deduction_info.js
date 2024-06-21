$(document).ready(function(){
    gnb4_On();
    side4_On();

    // 팝업 닫기
    $(".popCloseBtn").click(function (){
        $("#popup_layer").css("display", "none");
    });

    // LNB
    $(".menu_wrap li").hover(function (){
        side4_Off();
    }, function (){
        side4_On();
    });

    // GNB
    $(".navbar_menu li").hover(function (){
        gnb4_Off();
    }, function (){
        gnb4_On();
    });

    // 공제코드 select change event
    $("#dedCode_Sel").change(function (){
        const selVal = $("#dedCode_Sel").val();
        const selText = $("#dedCode_Sel").find(":selected").text();

        // 공제코드 선택 값이 있는 경우
        if(nullChk(selVal)){
            $("#dedCode_Txt").prop("disabled", true);           // 공제코드 input disable
            $("#dedContent_Txt").prop("disabled", true);        // 공제내용 input disable

            $("#dedCode_Txt").val(selText);                     // 공제코드 input 값 세팅
            getDedContent();                                    // 공제내용 input 값 세팅
        }else{
            $("#dedCode_Txt").prop("disabled", false);          // 공제코드 input enable
            $("#dedContent_Txt").prop("disabled", false);       // 공제내용 input enable

            $("#dedCode_Txt").val("");                          // 공제코드 input 값 초기화
            $("#dedContent_Txt").val("");                       // 공제내용 input 값 초기화
        }
    });

    // 단위 select change event
    $("#unit_Sel").change(function (){
        const selVal = $("#unit_Sel").val();
        const selText = $("#unit_Sel").find(":selected").text();

        if(nullChk(selVal)){
            $("#unit_Txt").prop("disabled", true);
            $("#unit_Txt").val(selText);
        }else{
            $("#unit_Txt").prop("disabled", false);
            $("#unit_Txt").val("");
        }
    });

    initAllData();
});

function getDedCodeSelList(){
    $.ajax({
        type: "GET",
        url: "/deductionInfo/dedCodeList",
        contentType : "application/json",
        success:function(ret){
            console.log("공제코드 목록 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    for(let i=0; i<data.length; i++){
                        const value = data[i].ded_code;

                        $("#dedCode_Sel").append("<option value='" + value + "'>" + value + "</option>");
                    }
                }else{
                    console.log("공제코드 목록 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("공제코드 목록 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("공제코드 목록 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 공제내용 조회
function getDedContent(){
    const params = {};
    params.dedCode = $("#dedCode_Txt").val();

    $.ajax({
        type: "GET",
        url: "/deductionInfo/dedContent",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("공제내용 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data != null){
                    $("#dedContent_Txt").val(data.ded_content);
                }else{
                    console.log("공제내용 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("공제내용 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("공제내용 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 단위 조회
function getUnitList(){
    $.ajax({
        type: "GET",
        url: "/deductionInfo/unitList",
        contentType : "application/json",
        success:function(ret){
            console.log("단위 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    for(let i=0; i<data.length; i++){
                        const value = data[i].unit;

                        $("#unit_Sel").append("<option value='" + value + "'>" + value + "</option>");
                    }
                }else{
                    console.log("단위 목록 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("단위 목록 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("단위 목록 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

function gnb4_On() {
    $("#gnb_menu4").attr("class", "on");
    $("#gnb_menu4 a").attr("class", "on");
}

function gnb4_Off() {
    $("#gnb_menu4").attr("class", "");
    $("#gnb_menu4 a").attr("class", "");
}

function side4_On() {
    $("#side_menu4").attr("class", "on");
}

function side4_Off() {
    $("#side_menu4").attr("class", "");
}

function saveData() {
    if(!validationCheckForDeductionInfoSave()){
        return;
    }

    const params = {};
    params.dedCode = $("#dedCode_Txt").val();
    params.dedContent = $("#dedContent_Txt").val();
    params.unit = $("#unit_Txt").val();
    params.unitDedRate = $("#unitDedRate_Txt").val();
    params.limitYear = $("#limitYear_Txt").val();
    params.limitDedRate = $("#limitDedRate_Txt").val();
    params.remark = $("#remark_Txt").val();

    $.ajax({
        type: "POST",
        url: "/deductionInfo/save",
        contentType : "application/json",
        data: JSON.stringify(params),
        success:function(ret){
            console.log("공제정보 저장 완료");
            alert("공제정보가 저장되었습니다.");
            initAllData();
        },
        error:function(e){
            console.log("error : " + JSON.stringify(e));
            alert("공제정보를 저장하지 못했습니다.");
        }
    });
}

// 공제정보 저장 유효값 체크
function validationCheckForDeductionInfoSave(){
    const dedCode = $("#dedCode_Txt").val();
    const dedContent = $("#dedContent_Txt").val();
    const unit = $("#unit_Txt").val();
    const unitDedRate = $("#unitDedRate_Txt").val();

    if(!nullChk(dedCode)){
        alert("공제코드를 입력하세요.")
        return false;
    }

    if(!nullChk(dedContent)){
        alert("공제내용을 입력하세요.")
        return false;
    }

    if(!nullChk(unit)){
        alert("단위를 입력하세요.")
        return false;
    }

    if(!nullChk(unitDedRate)){
        alert("단위공제율을 입력하세요.")
        return false;
    }

    return true;
}

function initAllData(){
    initDedCodeAndContentData();    // 공제코드 및 공제내용 초기화
    initUnitData();                 // 단위 초기화
    initUnitDedRateData();          // 단위공제율 초기화
    initLimitYearData();            // 한도연수 초기화
    initLimitDedRateData();         // 한도공제율 초기화
    initRemarkData();               // 비고 초기화
}

function initDedCodeAndContentData(){
    $("#dedCode_Sel").empty();
    $("#dedCode_Sel").append("<option value=''>직접입력</option>");
    getDedCodeSelList(); // 공제코드 List 조회하여 공제코드 select에 세팅

    $("#dedCode_Txt").val("");
    $("#dedCode_Txt").prop("disabled", false);

    $("#dedContent_Txt").val("");
    $("#dedContent_Txt").prop("disabled", false);
}

function initUnitData(){
    $("#unit_Sel").empty();
    $("#unit_Sel").append("<option value=''>직접입력</option>");
    getUnitList(); // 단위 List 조회하여 단위 select에 세팅

    $("#unit_Txt").val("");
}

function initUnitDedRateData(){
    $("#unitDedRate_Txt").val("");
}

function initLimitYearData(){
    $("#limitYear_Txt").val("");
}

function initLimitDedRateData(){
    $("#limitDedRate_Txt").val("");
}

function initRemarkData(){
    $("#remark_Txt").val("");
}