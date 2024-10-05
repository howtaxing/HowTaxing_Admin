$(document).ready(function(){
    gnb1_On();
    side1_On();

    // 팝업 닫기
    $(".popCloseBtn").click(function (){
        $("#popup_layer").css("display", "none");
    });

    // LNB
    $(".menu_wrap li").hover(function (){
        side1_Off();
    }, function (){
        side1_On();
    });

    // GNB
    $(".navbar_menu li").hover(function (){
        gnb1_Off();
    }, function (){
        gnb1_On();
    });

    // 대분류 select
    $("#mainCtId_Sel").change(function (){
        const selVal = $("#mainCtId_Sel").val();
        const selText = $("#mainCtId_Sel").find(":selected").text();
        const usingVal = selText.split("-")[0];
        const usingText = selText.split("-")[1];

        if(nullChk(selVal)){
            $("#mainCtId_Txt").prop("disabled", true);
            $("#mainCtName_Txt").prop("disabled", true);

            $("#mainCtId_Txt").val(usingVal);
            $("#mainCtName_Txt").val(usingText);
        }else{
            $("#mainCtId_Txt").prop("disabled", false);
            $("#mainCtName_Txt").prop("disabled", false);

            $("#mainCtId_Txt").val("");
            $("#mainCtName_Txt").val("");
        }

        initSubCtData();
        getSubCtSelList();
    });

    // 소분류 select
    $("#subCtId_Sel").change(function (){
        const selVal = $("#subCtId_Sel").val();
        const selText = $("#subCtId_Sel").find(":selected").text();
        const usingVal = selText.split("-")[0];
        const usingText = selText.split("-")[1];

        if(nullChk(selVal)){
            $("#subCtId_Txt").prop("disabled", true);
            $("#subCtName_Txt").prop("disabled", true);

            $("#subCtId_Txt").val(usingVal);
            $("#subCtName_Txt").val(usingText);

        }else{
            $("#subCtId_Txt").prop("disabled", false);
            $("#subCtName_Txt").prop("disabled", false);

            $("#subCtId_Txt").val("");
            $("#subCtName_Txt").val("");
        }
    });

    // 상수구분 select
    $("#constYn_Sel").change(function (){
        const selVal = $("#constYn_Sel").val();

        if(selVal === "Y"){
            $("#constVal_Txt").prop("disabled", false);
            $("#constVal_Txt").val("");
        }else{
            $("#constVal_Txt").prop("disabled", true);
            $("#constVal_Txt").val("가변");
        }
    });

    // 단위 select
    $("#unit_Sel").change(function (){
        const selVal = $("#unit_Sel").val();
        const selText = $("#unit_Sel").find(":selected").text();

        if(selVal !== ""){
            $("#unit_Txt").prop("disabled", true);
            $("#unit_Txt").val(selText);
        }else{
            $("#unit_Txt").prop("disabled", false);
            $("#unit_Txt").val("");
        }
    });

    init();
});

function init() {
    initAllData();
    getMainCtSelList();
    getUnitSelList();
}

// 대분류 리스트 조회
function getMainCtSelList(){
    $.ajax({
        type: "GET",
        url: "/commonCode/mainCtList",
        contentType : "application/json",
        success:function(ret){
            console.log("대분류 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    initMainCtData();

                    for(let i=0; i<data.length; i++){
                        const value = data[i].main_ct_id;
                        const text = data[i].main_ct_name;

                        $("#mainCtId_Sel").append("<option value='" + value + "'>" + value + "-" + text + "</option>");
                    }
                }else{
                    console.log("대분류 리스트 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("대분류 리스트 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("대분류 리스트 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 소분류 리스트 조회
function getSubCtSelList(){
    const params = {};
    params.mainCtId = $("#mainCtId_Txt").val();
    
    $.ajax({
        type: "GET",
        url: "/commonCode/subCtList",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("소분류 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    initSubCtData();

                    for(let i=0; i<data.length; i++){
                        const value = data[i].sub_ct_id;
                        const text = data[i].sub_ct_name;

                        $("#subCtId_Sel").append("<option value='" + value + "'>" + value + "-" + text + "</option>");
                    }
                }else{
                    console.log("소분류 리스트 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("소분류 리스트 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("소분류 리스트 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 단위 리스트 조회
function getUnitSelList(){
    $.ajax({
        type: "GET",
        url: "/commonCode/unitList",
        contentType : "application/json",
        success:function(ret){
            console.log("단위 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    $("#unit_Sel").empty();
                    $("#unit_Sel").append("<option value=''>직접입력</option>");

                    for(let i=0; i<data.length; i++){
                        const value = data[i].unit;

                        $("#unit_Sel").append("<option value='" + value + "'>" + value + "</option>");
                    }
                }else{
                    console.log("단위 리스트 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("단위 리스트 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("단위 리스트 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
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

function saveData() {
    if(!validationCheckForCommonCodeSave()){
        return;
    }

    const params = {};
    params.mainCtId = $("#mainCtId_Txt").val();
    params.mainCtName = $("#mainCtName_Txt").val();
    params.subCtId = $("#subCtId_Txt").val();
    params.subCtName = $("#subCtName_Txt").val();
    params.constYn = $("#constYn_Sel").val();
    params.constVal = $("#constVal_Txt").val();
    params.unit = $("#unit_Txt").val();
    params.remark = $("#remark_Txt").val();

    $.ajax({
        type: "POST",
        url: "/commonCode/save",
        contentType : "application/json",
        data: JSON.stringify(params),
        success:function(ret){
            // 저장 후 계속 입력할지 확인 팝업 호출
            //$("#popup_layer").css("display", "inline");
            console.log("공통코드 저장 완료");
            alert("공통코드가 저장되었습니다.");
            init();
        },
        error:function(e){
            console.log("error : " + JSON.stringify(e));
            alert("공통코드를 저장하지 못했습니다.");
        }
    });
}

// 공통코드 저장 유효값 체크
function validationCheckForCommonCodeSave(){
    const mainCtId = $("#mainCtId_Txt").val();
    const mainCtName = $("#mainCtName_Txt").val();
    const subCtId = $("#subCtId_Txt").val();
    const subCtName = $("#subCtName_Txt").val();
    const constYn = $("#constYn_Sel").val();
    const constVal = $("#constVal_Txt").val();
    const unit = $("#unit_Txt").val();
    const remark = $("#remark_Txt").val();

    if(!nullChk(mainCtId)){
        alert("대분류코드를 입력하세요.")
        return false;
    }

    if(!nullChk(mainCtName)){
        alert("대분류명을 입력하세요.")
        return false;
    }

    if(!nullChk(subCtId)){
        alert("소분류코드를 입력하세요.")
        return false;
    }

    if(!nullChk(subCtName)){
        alert("소분류명을 입력하세요.")
        return false;
    }

    if(constYn === "Y" && !nullChk(constVal)){
        alert("상수값을 입력하세요.")
        return false;
    }

    return true;
}

function initAllData(){
    initMainCtData();
    initSubCtData();
    initConstData();
    initUnitData();
    initRemarkData();
}

function initMainCtData(){
    $("#mainCtId_Sel option:eq(0)").prop("selected", true);
    $("#mainCtId_Txt").val("");
    $("#mainCtName_Txt").val("");
    $("#mainCtId_Txt").prop("disabled", false);
    $("#mainCtName_Txt").prop("disabled", false);

    $("#mainCtId_Sel").empty();
    $("#mainCtId_Sel").append("<option value=''>직접입력</option>");
}

function initSubCtData(){
    $("#subCtId_Sel option:eq(0)").prop("selected", true);
    $("#subCtId_Txt").val("");
    $("#subCtName_Txt").val("");
    $("#subCtId_Txt").prop("disabled", false);
    $("#subCtName_Txt").prop("disabled", false);

    $("#subCtId_Sel").empty();
    $("#subCtId_Sel").append("<option value=''>직접입력</option>");
}

function initConstData(){
    $("#constYn_Sel option:eq(0)").prop("selected", true);
    $("#constVal_Sel option:eq(0)").prop("selected", true);
    $("#constVal_Txt").val("");
    $("#constVal_Txt").prop("disabled", false);
}

function initUnitData(){
    $("#unit_Sel option:eq(0)").prop("selected", true);
    $("#unit_Txt").val("");
    $("#unit_Txt").prop("disabled", false);
}

function initRemarkData(){
    $("#remark_Txt").val("");
}