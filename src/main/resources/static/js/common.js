
$(document).ready(function(){
    console.log("common.js 호출");
});

function nullChk(str){
    return str != null && str !== "" && str !== undefined && str !== "undefined";
}

function gnb1_On() {
    $("#gnb_menu1").attr("class", "on");
    $("#gnb_menu1 a").attr("class", "on");
}

function gnb1_Off() {
    $("#gnb_menu1").attr("class", "");
    $("#gnb_menu1 a").attr("class", "");
}

function gnb2_On() {
    $("#gnb_menu2").attr("class", "on");
    $("#gnb_menu2 a").attr("class", "on");
}

function gnb2_Off() {
    $("#gnb_menu2").attr("class", "");
    $("#gnb_menu2 a").attr("class", "");
}

function gnb3_On() {
    $("#gnb_menu3").attr("class", "on");
    $("#gnb_menu3 a").attr("class", "on");
}

function gnb3_Off() {
    $("#gnb_menu3").attr("class", "");
    $("#gnb_menu3 a").attr("class", "");
}

function gnb4_On() {
    $("#gnb_menu4").attr("class", "on");
    $("#gnb_menu4 a").attr("class", "on");
}

function gnb4_Off() {
    $("#gnb_menu4").attr("class", "");
    $("#gnb_menu4 a").attr("class", "");
}

function side1_On() {
    $("#side_menu1").attr("class", "on");
}

function side1_Off() {
    $("#side_menu1").attr("class", "");
}

function side2_On() {
    $("#side_menu2").attr("class", "on");
}

function side2_Off() {
    $("#side_menu2").attr("class", "");
}

function side3_On() {
    $("#side_menu3").attr("class", "on");
}

function side3_Off() {
    $("#side_menu3").attr("class", "");
}

function side4_On() {
    $("#side_menu4").attr("class", "on");
}

function side4_Off() {
    $("#side_menu4").attr("class", "");
}