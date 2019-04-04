//rem  ≈‰
var cssE1 = document.createElement('style');
document.documentElement.firstElementChild.appendChild(cssE1);
function setPxPerRem(){
    var dpr = 1;
    var pxPerRem = document.documentElement.clientWidth * dpr / 10;
    cssE1.innerHTML = 'html{font-size:' + pxPerRem + 'px!important;}';
}
setPxPerRem();


/*
$(".dzfp-menuchild-tab li").click(function () {
    $(this).addClass("active").siblings("li").removeClass("active");
    $(".dzfp-listchild-tab").eq($(this).index()).show().siblings(".dzfp-listchild-tab").hide();
});*/
