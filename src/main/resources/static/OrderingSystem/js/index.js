/**
 * Created by Administrator on 2018/11/15.
 */
var count = 1000;//���ýӿڻ�ȡ��ֵ
var COUNT_MAN = 3000;//������������ֵ
function toPercent1(data){
    var strData = parseFloat(data)*100;
    var ret = strData.toString()+"%";
    return ret;
}
function toPercent2(data){
    var strData = parseFloat(data)*100-2.5;
    var ret = strData.toString()+"%";
    return ret;
}
$('#consumption_amount2').css("width",toPercent1(count/COUNT_MAN));
$('#irow').css("left",toPercent2(count/COUNT_MAN));
$('#score_butt').on('click',function(){
    var pxPerRem = document.documentElement.clientWidth / 10;
    var score_width_suzi = 4.76 * pxPerRem;
    var score_width = score_width_suzi+"px";
    if($(this).css("width")=="60px"){
        $(this).css("width",score_width);
        $(".span1").show();
    }else{
        $(this).css("width","60px");
        $(".span1").hide();
    }
});
//�˵��л�
$(document).ready(function () {
    $(".list-ul li").click(function () {
        $(this).addClass("active").siblings("li").removeClass("active");
        $(".tab").eq($(this).index()).show().siblings(".tab").hide();
    });
})
//�ײ��˵��л�
$(document).ready(function () {
    $(".tab-menu li").click(function () {
        $(this).addClass("active").siblings("li").removeClass("active");
        $(".tab").eq($(this).index()).show().siblings(".tab").hide();
    });
})
//�Ӽ�
$(".addbox").click(function() {
    var t = $(this).parent().find('input[class*=text_box]');
    t.val(parseInt(t.val()) + 1)
    setTotal();
});
$(".minbox").click(function() {
    var t = $(this).parent().find('input[class*=text_box]');
    t.val(parseInt(t.val()) - 1)
    if (parseInt(t.val()) < 0) {
        t.val(0);
    }
    setTotal();
});

function setTotal() {
    var s = 0;
    $("#add").each(function() {
        s += parseInt($(this).find('input[class*=text_box]').val());
    });
    $("#total").html(s.toFixed(0));
}
setTotal();

//ģ��select
$(document).ready(function () {
$(".top-date-menu").click(function(){
    $(this).siblings(".date-select-ul").slideDown();
    $(".header").css("z-index","99");
    $(".canpai-filter").css("display","block");
})
$(".date-select-ul li").click(function(){
    $(this).parent(".date-select-ul").siblings(".top-date-menu").text($(this).text());
    $(this).parent(".date-select-ul").slideUp();
    $(".filter-bg").css("display","none");
})
});


/*
*vipCenter.html
* */
$(".item-box").on("click",function(){
        window.location.href = 'couponDetail02.html';
});
$(".yyq-list .item-box").on("click",function(){
    window.location.href = 'couponDetail.html';
})


/*ͷ����ť start*/
$(".return-con").on("click",function(){
    window.history.go(-1);
});
$(".rt-btn").on("click",function(){
    window.location.href = 'message_list.html';
});
$(".menu-con").on("click",function(){
    $(".filter-bg").show();
    $(".setUp-wrap").show();
    $(".header").css("z-index","1");
    $(".top-menu").css("z-index","1");
    $(".date-top-select").css("z-index","9");
});
/*ͷ����ť  end*/


$(".jxsp-product-ul li").on("click",function(){
    window.location.href = 'changeThings.html';
});

$(".swiper-product-ul li").on("click",function(){
    window.location.href = 'barbecueProducts.html';
});
/*����footer��� start*/
$(".tab-menu li").on("click",function(){
    switch ($(this).index()){
        case 0:
            window.location.href="index.html";
            break;
        case 1:
            window.location.href="QR.html";
            break;
        case 2:
            window.location.href="vipCenter.html";
            break;
        case 3:
            window.location.href="shopLocation.html";
            break;
        case 4:
            window.location.href="canPai.html";
            break;
    }
});
/*����footer��� end*/

/*indexsetting start*/
$(".filter-bg").on("click",function(){
    $(".filter-bg").css("display","none");
    $(".date-select-ul").css("display","none");
    $(".shen-gaoji").css("display","none");
    $(".chanSuc").css("display","none");
    $(".score-box").css("display","none");
    $(".date-top-select").css("z-index","999");
});
$(".banner-div").on("click",function(){
    event.stopPropagation();
});
$(".item").on("click",function(){
    event.stopPropagation();
});
/*indexsetting end*/
/*
* */
//$(".ljbl-btn").on("click",function(){
//    if($(".text_box").val()>2){
//        $(".score-box").css("display","block");
//    }else{
//        $(".chanSuc").css("display","block");
//    }
//});

/*register ȷ������*/
//$(".btn-next").on("click",function(){
//    $(".confirmEmail").css("display","block");
//});

/*�����*/
$("#moreIco").click(function(){

});
/*�����б�*/
$(".canting-list-pic").on("click",function(){
    window.location.href = 'restaurant_Detail.html';
});
/*��������*/
$(".canting-address").on("click",function(){
    $(".restaurant-Detail-filter").css("display","block");
});
/*$(".special-ul li").on("click",function(){
    window.location.href="detail.html";
});*/
/*ce*/
$(".setUp-ul .setUp-ul-li").on("click",function(){
    switch ($(this).index()){
        case 0:
            break;
        case 1:
            window.location.href="vipAuth.html";
            break;
        case 2:
        	window.location.href="vipCenter.html?loadType=coupon";
            break;
        case 3:
            window.location.href="myCollection.html";
            break;
        case 4:
        	  window.location.href="upgradeVIP.html";
            break;
        case 5:
        	var fakeLink = document.createElement('a');
        	var shareUrl =encodeURIComponent('您的好友分享了一個優惠多,又好玩的APP,請點擊下載APP,一起使用'+appUpload);
        	fakeLink.setAttribute('href', 'whatsapp://send?text='+shareUrl);
        	fakeLink.setAttribute('data-action', 'share/whatsapp/share');
        	fakeLink.click();
            break;
        case 6:
      	  window.location.href="showPricePro.html?key=about";
          break;    
        case 7:
        	var show = $('#myBargain').css('display');
        	$('#myBargain').css('display',show =='none'?'block':'none');
        	$(this).attr('src',show =='none'?'imgs/index/close-img.png':'imgs/index/next-icon.png');
        	break;
    }
});

$("#myBargain li").on("click",function(){
    switch ($(this).index()){
        case 0:
			window.location.href="showPricePro.html?title=隱私政策&key=frequentlyAskedques";
            break;
        case 1:
            window.location.href="showPricePro.html?title=APP條款及細則&key=termsConditions";
            break;
        case 2:
			window.location.href="showPricePro.html?title=消息設置";
            break;
        case 3:
            
            break;
    }
});
///*QR*/
//$(".QRcode-content").on("click",function(){
//    window.location.href = 'changeSuc.html';
//});

/*�տ���Ʒ*/
$(".bar-cai-name").on("click",function(){
    window.location.href = 'changeThings.html';
});
/*changeThings.html*/
$(".shaohou-yong").on("click",function(){
    $(".chanSuc").css("display","none");
});
/*shopLocation.html*/
$(".address-selectTop").on("click",function () {
    $(".shopLocation-filter").css("display","block");
});
/*message_list.html*/
$(".item-box").on("click",function(){
    window.location.href = 'detail.html';
});