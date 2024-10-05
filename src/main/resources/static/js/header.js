var toggleClicked = false;

$(document).ready(function(){
    $("#closeBtn").css("display", "none");
    //$(".sidebar_menu").css("display", "none");

    $("#openBtn").click(function (){
        $('.menu_bg').show();
        /*$('.sidebar_menu').show().animate({
            right:0
        });*/
        toggleClicked = true;
    });

    $("#close_btn").click(function (){
        $('.menu_bg').hide();
        /*$('.sidebar_menu').animate({
            right: '-' + 50 + '%'
        },function() {
            $('.sidebar_menu').hide();
        });*/
        toggleClicked = false;
    });

    $(window).resize(function (){
        if(toggleClicked && window.outerWidth > 768) {
            $(".bg-shadow").css("display", "none");
            toggleClicked = false;
        }
    });
});
