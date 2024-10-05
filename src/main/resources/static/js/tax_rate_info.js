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

    // 세율코드 select change event
    $("#taxRateCode_Sel").change(function (){
        const selVal = $("#taxRateCode_Sel").val();
        const selText = $("#taxRateCode_Sel").find(":selected").text();

        // 세율코드 선택 값이 있는 경우
        if(nullChk(selVal)){
            $("#taxRateCode_Txt").prop("disabled", true);       // 세율코드 input disable
            $("#taxRateName_Txt").prop("disabled", true);       // 세율명 input disable

            $("#taxRateCode_Txt").val(selText);                 // 세율코드 input 값 세팅
            getTaxRateName();                                   // 세율명 input 값 세팅
        }else{
            $("#taxRateCode_Txt").prop("disabled", false);      // 세율코드 input enable
            $("#taxRateName_Txt").prop("disabled", false);      // 세율명 input enable

            $("#taxRateCode_Txt").val("");                      // 세율코드 input 값 초기화
            $("#taxRateName_Txt").val("");                      // 세율명 input 값 초기화
        }
    });

    // 상수구분 select change event
    $("#constYn_Sel").change(function (){
        const selVal = $("#constYn_Sel").val();

        // 상수
        if(selVal === "Y"){
            $("#usedFunc_Sel").prop("disabled", true);
            $("#usedFunc_Txt").prop("disabled", true);
            $("#basePrice_Txt").prop("disabled", true);
            $("#addTaxRate1_Txt").prop("disabled", true);
            $("#taxRate2_Txt").prop("disabled", true);
            $("#addTaxRate2_Txt").prop("disabled", true);
        }
        // 변수
        else{
            $("#usedFunc_Sel").prop("disabled", false);
            $("#usedFunc_Txt").prop("disabled", false);
            $("#basePrice_Txt").prop("disabled", false);
            $("#addTaxRate1_Txt").prop("disabled", false);
            $("#taxRate2_Txt").prop("disabled", false);
            $("#addTaxRate2_Txt").prop("disabled", false);
        }
    });

    // 사용함수 select change event
    $("#usedFunc_Sel").change(function (){
        const selVal = $("#usedFunc_Sel").val();
        const selText = $("#usedFunc_Sel").find(":selected").text();

        if(nullChk(selVal)){
            $("#usedFunc_Txt").prop("disabled", true);
            $("#usedFunc_Txt").val(selText);
        }else{
            $("#usedFunc_Txt").prop("disabled", false);
            $("#usedFunc_Txt").val("");
        }
    });

    initAllData();
});

function getTaxRateCodeSelList(){
    $.ajax({
        type: "GET",
        url: "/taxRateInfo/taxRateCodeList",
        contentType : "application/json",
        success:function(ret){
            console.log("세율코드 목록 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    for(let i=0; i<data.length; i++){
                        const value = data[i].tax_rate_code;

                        $("#taxRateCode_Sel").append("<option value='" + value + "'>" + value + "</option>");
                    }
                }else{
                    console.log("세율코드 목록 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("세율코드 목록 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("세율코드 목록 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 세율명 조회
function getTaxRateName(){
    const params = {};
    params.taxRateCode = $("#taxRateCode_Txt").val();
    
    $.ajax({
        type: "GET",
        url: "/taxRateInfo/taxRateName",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("세율명 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data != null){
                    $("#taxRateName_Txt").val(data.tax_rate_name);
                }else{
                    console.log("세율명 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("세율명 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("세율명 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 사용함수 조회
function getUsedFuncList(){
    $.ajax({
        type: "GET",
        url: "/taxRateInfo/usedFuncList",
        contentType : "application/json",
        success:function(ret){
            console.log("사용함수 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    for(let i=0; i<data.length; i++){
                        const value = data[i].used_func;

                        $("#usedFunc_Sel").append("<option value='" + value + "'>" + value + "</option>");
                    }
                }else{
                    console.log("사용함수 목록 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("사용함수 목록 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("사용함수 목록 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

function saveData() {
    if(!validationCheckForTaxRateInfoSave()){
        return;
    }

    const params = {};
    params.taxRateCode = $("#taxRateCode_Txt").val();
    params.taxRateName = $("#taxRateName_Txt").val();
    params.constYn = $("#constYn_Sel").val();
    params.usedFunc = $("#usedFunc_Txt").val();
    params.basePrice = $("#basePrice_Txt").val();
    params.taxRate1 = $("#taxRate1_Txt").val();
    params.addTaxRate1 = $("#addTaxRate1_Txt").val();
    params.taxRate2 = $("#taxRate2_Txt").val();
    params.addTaxRate2 = $("#addTaxRate2_Txt").val();
    params.remark = $("#remark_Txt").val();

    $.ajax({
        type: "POST",
        url: "/taxRateInfo/save",
        contentType : "application/json",
        data: JSON.stringify(params),
        success:function(ret){
            console.log("세율정보 저장 완료");
            alert("세율정보가 저장되었습니다.");
            initAllData();
        },
        error:function(e){
            console.log("error : " + JSON.stringify(e));
            alert("세율정보를 저장하지 못했습니다.");
        }
    });
}

// 세율정보 저장 유효값 체크
function validationCheckForTaxRateInfoSave(){
    const taxRateCode = $("#taxRateCode_Txt").val();
    const taxRateName = $("#taxRateName_Txt").val();
    const taxRate1 = $("#taxRate1_Txt").val();
    const addTaxRate1 = $("#addTaxRate1_Txt").val();
    const taxRate2 = $("#taxRate2_Txt").val();
    const addTaxRate2 = $("#addTaxRate2_Txt").val();

    if(!nullChk(taxRateCode)){
        alert("세율코드를 입력하세요.")
        return false;
    }

    if(!nullChk(taxRateName)){
        alert("세율명을 입력하세요.")
        return false;
    }

    if(!nullChk(taxRate1)){
        alert("세율1을 입력하세요.")
        return false;
    }

    if(!nullChk(taxRate2) && nullChk(addTaxRate2)){
        alert("추가세율2만 입력할 수는 없습니다. 세율2도 입력하세요.")
        return false;
    }

    return true;
}

function initAllData(){
    initTaxRateData();      // 세율코드, 세율명 초기화
    initConstData();        // 상수구분 초기화
    initUsedFuncData();     // 사용함수 초기화
    initBasePriceData();    // 기준금액 초기화
    initTaxRate1Data();     // 세율1 초기화
    initTaxRate2Data();     // 세율2 초기화
    initRemarkData();       // 비고 초기화
}

function initTaxRateData(){
    $("#taxRateCode_Sel").empty();
    $("#taxRateCode_Sel").append("<option value=''>직접입력</option>");

    // 세율코드 List 조회하여 세율코드 select에 세팅
    getTaxRateCodeSelList();

    $("#taxRateCode_Txt").val("");
    $("#taxRateName_Txt").val("");
    $("#taxRateCode_Txt").prop("disabled", false);
    $("#taxRateName_Txt").prop("disabled", false);
}

function initConstData(){
    $("#constYn_Sel option:eq(0)").prop("selected", true);
}

function initUsedFuncData(){
    $("#usedFunc_Sel").empty();
    $("#usedFunc_Sel").append("<option value=''>직접입력</option>");
    $("#usedFunc_Sel").prop("disabled", true);
    $("#usedFunc_Txt").prop("disabled", true);

    // 사용함수 List 조회하여 사용함수 select에 세팅
    getUsedFuncList();
    $("#usedFunc_Txt").val("");
}

function initBasePriceData(){
    $("#basePrice_Txt").val("");
    $("#basePrice_Txt").prop("disabled", true);
}

function initTaxRate1Data(){
    $("#taxRate1_Txt").val("");
    $("#addTaxRate1_Txt").val("");
    $("#addTaxRate1_Txt").prop("disabled", true);
}

function initTaxRate2Data(){
    $("#taxRate2_Txt").val("");
    $("#addTaxRate2_Txt").val("");
    $("#taxRate2_Txt").prop("disabled", true);
    $("#addTaxRate2_Txt").prop("disabled", true);
}

function initRemarkData(){
    $("#remark_Txt").val("");
}