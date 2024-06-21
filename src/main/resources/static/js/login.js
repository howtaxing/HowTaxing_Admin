$(document).ready(function(){
    gnb5_On();
    side5_On();

    $(".menu_wrap li").hover(function (){
        side5_Off();
    }, function (){
        side5_On();
    });

    $(".navbar_menu li").hover(function (){
        gnb5_Off();
    }, function (){
        gnb5_On();
    });
});

function gnb5_On() {
    $("#gnb_menu5").attr("class", "on");
    $("#gnb_menu5 a").attr("class", "on");
}

function gnb5_Off() {
    $("#gnb_menu5").attr("class", "");
    $("#gnb_menu5 a").attr("class", "");
}

function side5_On() {
    $("#side_menu5").attr("class", "on");
}

function side5_Off() {
    $("#side_menu5").attr("class", "");
}

// 회원가입
function goJoin() {
    window.location.href = "/moveJoin01";
}