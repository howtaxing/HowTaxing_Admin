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

    // 계산구분 select
    $("#calcType_Sel").change(function (){
        const selVal = $("#calcType_Sel").val();
        const selText = $("#calcType_Sel").find(":selected").text();

        if(nullChk(selVal)){
            const usingVal = selText.split("-")[0];
            const usingText = selText.split("-")[1];

            $("#calcType_Txt").prop("disabled", true);
            $("#calcName_Txt").prop("disabled", true);
            $("#calcType_Txt").val(usingVal);
            $("#calcName_Txt").val(usingText);

            getBranchNoList();
        }else{
            $("#calcType_Txt").prop("disabled", false);
            $("#calcName_Txt").prop("disabled", false);
            $("#calcType_Txt").val("");
            $("#calcName_Txt").val("");

            initBranchNoData();
            initSelectNoData();
        }
    });

    // 분기번호 select change event
    $("#branchNo_Sel").change(function (){
        const selVal = $("#branchNo_Sel").val();
        const selText = $("#branchNo_Sel").find(":selected").text();

        // 분기번호 선택 값이 있는 경우
        if(nullChk(selVal)){
            $("#branchNo_Txt").prop("disabled", true);          // 분기번호 input disable
            $("#branchName_Txt").prop("disabled", true);        // 분기명 input disable

            $("#branchNo_Txt").val(selText);                    // 분기번호 input 값 세팅
            getBranchName();                                    // 분기명 input 값 세팅
            initSelectNoData();
            getSelectNoList();
        }else{
            $("#branchNo_Txt").prop("disabled", false);         // 분기번호 input enable
            $("#branchName_Txt").prop("disabled", false);       // 분기명 input enable
            $("#branchNo_Txt").val("");                         // 분기번호 input 값 초기화
            $("#branchName_Txt").val("");                       // 분기명 input 값 초기화

            initSelectNoData();
        }
    });

    // 선택번호 select change event
    $("#selectNo_Sel").change(function (){
        const selVal = $("#selectNo_Sel").val();
        const selText = $("#selectNo_Sel").find(":selected").text();

        // 선택번호 선택 값이 있는 경우
        if(nullChk(selVal)){
            $("#selectNo_Txt").prop("disabled", true);          // 선택번호 input disable
            $("#selectContent_Txt").prop("disabled", true);     // 선택내용 input disable

            $("#selectNo_Txt").val(selText);                    // 선택번호 input 값 세팅
            getSelectContent();                                 // 선택내용 input 값 세팅
        }else{
            $("#selectNo_Txt").prop("disabled", false);         // 선택번호 input enable
            $("#selectContent_Txt").prop("disabled", false);    // 선택내용 input enable

            $("#selectNo_Txt").val("");                         // 선택번호 input 값 초기화
            $("#selectContent_Txt").val("");                    // 선택내용 input 값 초기화
        }
    });

    // 다음분기여부 select change event
    $("#hasNextBranch_Sel").change(function (){
        const selVal = $("#hasNextBranch_Sel").val();

        if(selVal === "Y"){
            $("#nextBranchNo_Txt").prop("disabled", false);

            $("#taxRateCode_Sel").prop("disabled", true);
            $("#taxRateCode_Sel option:eq(0)").prop("selected", true);
            $("#dedCode_Sel").prop("disabled", true);
            $("#dedCode_Sel option:eq(0)").prop("selected", true);
            $("#taxRateName_Txt").val("");
            $("#dedContent_Txt").val("");
        }else{
            $("#nextBranchNo_Txt").prop("disabled", true);
            $("#nextBranchNo_Txt").val("");

            $("#taxRateCode_Sel").prop("disabled", false);
            $("#dedCode_Sel").prop("disabled", false);
        }
    });

    // 세율코드 select change event
    $("#taxRateCode_Sel").change(function (){
        const selVal = $("#taxRateCode_Sel").val();

        if(nullChk(selVal)){
            getTaxRateName();
        }else{
            $("#taxRateName_Txt").val("");
        }
    });

    // 공제코드 select change event
    $("#dedCode_Sel").change(function (){
        const selVal = $("#dedCode_Sel").val();

        if(nullChk(selVal)){
            getDedContent();
        }else{
            $("#dedContent_Txt").val("");
        }
    });

    initAllData();
});

// 계산구분 및 계산명 목록 조회
function getCalcTypeAndCalcNameList(){
    $.ajax({
        type: "GET",
        url: "/calculationProcess/calcTypeAndCalcNameList",
        contentType : "application/json",
        success:function(ret){
            console.log("계산구분 및 계산명 목록 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    for(let i=0; i<data.length; i++){
                        const value = data[i].calc_type;
                        const text = data[i].calc_name;

                        $("#calcType_Sel").append("<option value='" + value + "'>" + value + "-" + text + "</option>");
                    }
                }else{
                    console.log("계산구분 및 계산명 목록 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("계산구분 및 계산명 목록 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("계산구분 및 계산명 목록 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 분기번호 목록 조회
function getBranchNoList(){
    const params = {};
    params.calcType = $("#calcType_Txt").val();

    $.ajax({
        type: "GET",
        url: "/calculationProcess/branchNoList",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("분기번호 목록 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    for(let i=0; i<data.length; i++){
                        const value = data[i].branch_no;

                        $("#branchNo_Sel").append("<option value='" + value + "'>" + value + "</option>");
                    }
                }else{
                    console.log("분기번호 목록 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("분기번호 목록 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("분기번호 목록 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 분기명 조회
function getBranchName(){
    const params = {};
    params.calcType = $("#calcType_Txt").val();
    params.branchNo = $("#branchNo_Txt").val();

    $.ajax({
        type: "GET",
        url: "/calculationProcess/branchName",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("분기명 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data != null){
                    $("#branchName_Txt").val(data.branch_name);
                }else{
                    console.log("분기명 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("분기명 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("분기명 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 선택번호 목록 조회
function getSelectNoList(){
    const params = {};
    params.calcType = $("#calcType_Txt").val();
    params.branchNo = $("#branchNo_Txt").val();

    $.ajax({
        type: "GET",
        url: "/calculationProcess/selectNoList",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("선택번호 목록 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data.length > 0){
                    for(let i=0; i<data.length; i++){
                        const value = data[i].select_no;

                        $("#selectNo_Sel").append("<option value='" + value + "'>" + value + "</option>");
                    }
                }else{
                    console.log("선택번호 목록 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("선택번호 목록 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("선택번호 목록 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 선택내용 조회
function getSelectContent(){
    const params = {};
    params.calcType = $("#calcType_Txt").val();
    params.branchNo = $("#branchNo_Txt").val();
    params.selectNo = $("#selectNo_Txt").val();

    $.ajax({
        type: "GET",
        url: "/calculationProcess/selectContent",
        contentType : "application/json",
        data: params,
        success:function(ret){
            console.log("선택내용 select : " + JSON.stringify(ret));

            if(nullChk(ret) && nullChk(ret.errYn) && nullChk(ret.data)){
                const errYn = ret.errYn;
                const data = ret.data;

                if(errYn === "N" && data != null){
                    $("#selectContent_Txt").val(data.select_content);
                }else{
                    console.log("선택내용 조회 중 오류가 발생했습니다(1).");
                }
            }else{
                console.log("선택내용 조회 중 오류가 발생했습니다(2).");
            }
        },
        error:function(e){
            console.log("선택내용 조회 중 오류가 발생했습니다(3)-" + JSON.stringify(e));
        }
    });
}

// 세율코드 목록 조회
function getTaxRateCodeList(){
    $.ajax({
        type: "GET",
        url: "/taxRateInfo/taxRateCodeList",
        contentType : "application/json",
        success:function(ret){
            console.log("세율코드 select : " + JSON.stringify(ret));

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
    params.taxRateCode = $("#taxRateCode_Sel").val();

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

// 공제코드 목록 조회
function getDedCodeList(){
    $.ajax({
        type: "GET",
        url: "/deductionInfo/dedCodeList",
        contentType : "application/json",
        success:function(ret){
            console.log("공제코드 select : " + JSON.stringify(ret));

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
    params.dedCode = $("#dedCode_Sel").val();

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

// 계산프로세스 저장
function saveData() {
    if(!validationCheckForCalculationProcessSave()){
        return;
    }

    const calcType = $("#calcType_Txt").val();
    const branchNo = $("#branchNo_Txt").val();
    const selectNo = $("#selectNo_Txt").val();
    const calcName = $("#calcName_Txt").val();
    const branchName = $("#branchName_Txt").val();
    const selectContent = $("#selectContent_Txt").val();
    const variableData = $("#variableData_Txt").val();
    const dataMethod = $("#dataMethod_Txt").val();
    const hasNextBranch = ($("#hasNextBranch_Sel").val() === "Y");
    const nextBranchNo = $("#nextBranchNo_Txt").val();
    const taxRateCode = $("#taxRateCode_Sel").val();
    const dedCode = $("#dedCode_Sel").val();
    const remark = $("#remark_Txt").val();

    const params = {};
    params.calcType = calcType;
    params.branchNo = branchNo;
    params.selectNo = selectNo;
    params.calcName = calcName;
    params.branchName = branchName;
    params.selectContent = selectContent;
    if(nullChk(variableData)) params.variableData = variableData;
    if(nullChk(dataMethod)) params.dataMethod = dataMethod;
    if(nullChk(hasNextBranch)) params.hasNextBranch = hasNextBranch;
    if(nullChk(nextBranchNo)) params.nextBranchNo = nextBranchNo;
    if(nullChk(taxRateCode)) params.taxRateCode = taxRateCode;
    if(nullChk(dedCode)) params.dedCode = dedCode;
    if(nullChk(remark)) params.remark = remark;

    $.ajax({
        type: "POST",
        url: "/calculationProcess/save",
        contentType : "application/json",
        data: JSON.stringify(params),
        success:function(ret){
            console.log("계산프로세스 저장 완료");
            alert("계산프로세스가 저장되었습니다.");
            initAllData();
        },
        error:function(e){
            console.log("error : " + JSON.stringify(e));
            alert("계산프로세스를 등록하지 못했습니다.");
        }
    });
}

// 계산프로세스 저장 유효값 체크
function validationCheckForCalculationProcessSave(){
    const calcType = $("#calcType_Txt").val();
    const calcName = $("#calcName_Txt").val();
    const branchNo = $("#branchNo_Txt").val();
    const branchName = $("#branchName_Txt").val();
    const selectNo = $("#selectNo_Txt").val();
    const selectContent = $("#selectContent_Txt").val();

    if(!nullChk(calcType)){
        alert("계산구분코드를 입력하세요.")
        return false;
    }

    if(!nullChk(calcName)){
        alert("계산명을 입력하세요.")
        return false;
    }

    if(!nullChk(branchNo)){
        alert("분기번호를 입력하세요.")
        return false;
    }

    if(!nullChk(branchName)){
        alert("분기명을 입력하세요.")
        return false;
    }

    if(!nullChk(selectNo)){
        alert("선택번호를 입력하세요.")
        return false;
    }

    if(!nullChk(selectContent)){
        alert("선택명을 입력하세요.")
        return false;
    }

    return true;
}

function initAllData(){
    initCalcTypeData();         // 계산구분 및 계산명 초기화
    initBranchNoData();         // 분기번호 및 분기명 초기화
    initSelectNoData();         // 선택번호 및 선택내용 초기화
    initVariableData();         // 가변데이터 초기화
    initDataMethodData();       // 데이터함수 초기화
    initNextBranchNo();         // 다음분기여부 및 다음분기번호 초기화
    initTaxRateCodeData();      // 세율코드 및 세율명 초기화
    initDedCodeData();          // 공제코드 및 공제내용 초기화
    initRemarkData();           // 비고 초기화
}

function initCalcTypeData(){
    $("#calcType_Sel").empty();
    $("#calcType_Sel").append("<option value=''>직접입력</option>");
    getCalcTypeAndCalcNameList();

    $("#calcType_Txt").val("");
    $("#calcType_Txt").prop("disabled", false);

    $("#calcName_Txt").val("");
    $("#calcName_Txt").prop("disabled", false);
}

function initBranchNoData(){
    $("#branchNo_Sel").empty();
    $("#branchNo_Sel").append("<option value=''>직접입력</option>");

    $("#branchNo_Txt").val("");
    $("#branchNo_Txt").prop("disabled", false);

    $("#branchName_Txt").val("");
    $("#branchName_Txt").prop("disabled", false);
}

function initSelectNoData(){
    $("#selectNo_Sel").empty();
    $("#selectNo_Sel").append("<option value=''>직접입력</option>");

    $("#selectNo_Txt").val("");
    $("#selectNo_Txt").prop("disabled", false);

    $("#selectContent_Txt").val("");
    $("#selectContent_Txt").prop("disabled", false);
}

function initVariableData(){
    $("#variableData_Txt").val("");
}

function initDataMethodData(){
    $("#dataMethod_Txt").val("");
}

function initNextBranchNo(){
    $("#hasNextBranch_Sel option:eq(0)").prop("selected", true);
    $("#nextBranchNo_Txt").prop("disabled", false);
    $("#nextBranchNo_Txt").val("");
}

function initTaxRateCodeData(){
    $("#taxRateCode_Sel").prop("disabled", true);
    $("#taxRateCode_Sel").empty();
    $("#taxRateCode_Sel").append("<option value=''>선택</option>");
    getTaxRateCodeList();

    $("#taxRateName_Txt").val("");
}

function initDedCodeData(){
    $("#dedCode_Sel").prop("disabled", true);
    $("#dedCode_Sel").empty();
    $("#dedCode_Sel").append("<option value=''>선택</option>");
    getDedCodeList();

    $("#dedContent_Txt").val("");
}

function initRemarkData(){
    $("#remark_Txt").val("");
}