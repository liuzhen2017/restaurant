var pathUrl ="https://meokbang.com.hk:447/";
var appUpload ="https://dl.pconline.com.cn/html_2/1/59/id=37973&pn=0.html";
//var pathUrl ='';
// 上传图片
function uploadFile() {
	var fileUrl;
	var files = document.getElementById("file");
	var filePath = $("#file").val(), // 获取到input的value，里面是文件的路径
	fileFormat = filePath.substring(filePath.lastIndexOf(".")).toLowerCase();
	if (!/.(gif|jpg|jpeg|png|GIF|JPG|png)$/.test(fileFormat)) {
		$.modal.alertError("圖片必須是.gif,jpeg,jpg,png格式..");
		return false;
	}
	var formData = new FormData();
	formData.append("file", files.files[0]);
	$.ajax({
		type : "POST",
		async:false,
		url : ctx + "common/upload",
		data : formData,
		type : "post",
		processData : false,
		contentType : false,
		success : function(result) {
			if (result.code == web_status.SUCCESS) {
				fileUrl = result.url;
			} else {
				$.modal.alertError(result.msg);

			}
		},
		error : function(error) {
			$.modal.alertWarning("图片上传失败。");

		}
	});
	console.log('文件路徑2:'+fileUrl);
	return fileUrl;
}
/**
 * 打開圖片
 * 
 * @param value
 */
function openImg(value){
	layer.open({
		  type: 1,
		  title: false,
		  closeBtn: 0,
		  area: '516px',
		  skin: 'layui-layer-nobg', // 没有背景色
		  shadeClose: true,
		  content: "<img  src='"+value+"' />"
		});
	  
}
function getyyyyMMdd(d){
    var curr_date = d.getDate();
    var curr_month = d.getMonth() + 1; 
    var curr_year = d.getFullYear();
    String(curr_month).length < 2 ? (curr_month = "0" + curr_month): curr_month;
    String(curr_date).length < 2 ? (curr_date = "0" + curr_date): curr_date;
    var yyyyMMdd = curr_year + "年" + curr_month +"月"+ curr_date+"日";
    return yyyyMMdd;
}    
// 60s倒计时实现逻辑
var countdown = 60;
function setTime() {
	var obj =$("#getSMS");
    if (countdown == 0) {
        obj.prop('disabled', false);
        obj.text("点击获取验证码");
        countdown = 60;// 60秒过后button上的文字初始化,计时器初始化;
        return;
    } else {
        obj.prop('disabled', true);
        obj.text("("+countdown+"s)后重新发送") ;
        countdown--;
    }
    setTimeout(function() { setTime(obj) },1000) // 每1000毫秒执行一次
}

// paraName 等找参数的名称
function GetUrlParam(paraName) {
　　var url = document.location.toString();
　　var arrObj = url.split("?");

　　if (arrObj.length > 1) {
　　　　var arrPara = arrObj[1].split("&");
　　　　var arr;

　　　　for (var i = 0; i < arrPara.length; i++) {
　　　　　　arr = arrPara[i].split("=");

　　　　　　if (arr != null && arr[0] == paraName) {
　　　　　　　　return arr[1];
　　　　　　}
　　　　}
　　　　return "";
　　}
　　else {
　　　　return "";
　　}
}
function openPage(title,url){
	$.modal.open(title,url,1050,800);
}
/**
 * 發送郵件
 * 
 * @param phone
 * @returns {Boolean}
 */
function sendSMS(phone){
	if(!phone){
		phone= $("#phone").val();
	}
	if(!phone){
		msg('手機號不能爲空!');
		return '';
	}
	var success=false;
	$.ajax({
		method : "POST",
		type:"POST",
		async:false,
		url : pathUrl + "/api/sendMessager/sendSMS.do",
		data : {phone,phone},
		type : "post",
		async: false,
		jsonpCallback:'cd',
		success : function(result) {
			if (result.code =='0') {
				success =true;
				msg('短訊已經發送,請注意查收');
				setTime();
			} else {
				msg(result.msg);
			}
		},
		error : function(error) {
			msg("網絡連接錯誤!"+JSON.stringify(error));

		}
	});
	return success;
}
/**
 * 推出登錄
 */
function loginOut(){
	localStorage.removeItem("token");
	localStorage.removeItem("tokent");
	localStorage.removeItem("showAdv");
	window.location.href = "index.html";
	msg('退出成功!')
}
/**
 * 打開谷歌地圖
 * 
 * @param startAddress
 */
function openApp(startAddress) {
	var u = navigator.userAgent;
	var device =""; // 当前设备信息
	if (u.indexOf('Android') > -1 || u.indexOf('Linux') > -1) {// 安卓手机
	    device = "Android";
	} else if (u.indexOf('iPhone') > -1) {// 苹果手机
	    device = "iPhone";
	} else if (u.indexOf('Windows Phone') > -1) {// winphone手机
	    device = "WindowsPhone";
	}
	   if (iPhone) {
		   window.location.href = "https://www.google.com/maps/dir/?api=1&origin="+startAddress; 
		   } else  {
			   window.location.href = "https://www.google.com/maps/dir/?api=1&origin="+startAddress;
		}
}

function getUrlParmat(path){
	path=path.substring(path.lastIndexOf('upload/')+7,path.length);
	return pathUrl+"/common/download?fileName="+path;
}
/**
 * 校驗驗證碼
 * 
 * @param phone
 * @param code
 * @returns {Boolean}
 */
function vaildSMS(phone,code){
	if(!phone){
		phone= $("#phone").val();
	}
	if(!code){
		code= $("#code").val();
	}
	if(!phone && !code){
		msg("手機號和驗證碼不能爲空!");
		return false;
	}
	var success=false;
	$.ajax({
		type : "POST",
		async:false,
		url : pathUrl + "/api/members/vaildCode.do",
		data : {phone:phone,code:code},
		async: false,
		success : function(result) {
			if (result.code =='0') {
				success =true;
			} else {
				msg(result.msg);
			}
		},
		error : function(error) {
			msg("網絡連接錯誤!");

		}
	});
	return success;
}
/**
 * 檢查登錄
 */
function checkLogin(){
	if(!window.localStorage.token || window.localStorage.token == 'undefined'){
    layer.open({
        content: '用戶沒有登陸,請登錄后再操作'
        ,btn: ['登陸', '稍後自己登陸']
        ,yes: function(index){
          window.location.href = "Login.html";
          layer.close(index);
        },no:function(index){
             window.location.href = "index.html";
        }
      });
    	return false;
	}else{
		return true;
	}
}
/**
 * 獲取當前登錄用戶信息
 * 
 * @returns
 */
function getMembersInfo(){
	if(!window.localStorage.token || window.localStorage.token =='undefined'){
		return null;
	}
	var userInfo =null;
		$.ajax({
			method : "POST",
			async:false,
			url : pathUrl + "/api/members/getMenbersInfo.do",
			data : {token:window.localStorage.token},
			type : "post",
			async: false,
			jsonpCallback:'cd',
			success : function(result) {
				if (result.code =='0') {
					userInfo= result.data.membersInfo;
					$('.showScord').html(userInfo.score);
				}
			},
			error : function(error) {
				msg("網絡連接錯誤!"+JSON.stringify(error));

			}
		});
		return userInfo;
}
/**
 * 獲取系統消息
 */
function getNotiInfo(){
	if(!window.localStorage.token || window.localStorage.token =='undefined'){
		return null;
	}
		$.ajax({
			method : "POST",
			async:false,
			url : pathUrl + "/api/noticeInfo/queryNoSeeNum.do",
			data : {token:window.localStorage.token},
			type : "post",
			async: false,
			jsonpCallback:'cd',
			success : function(result) {
				if (result.code =='0') {
					$("#message").html(result.data);
				}
			},
			error : function(error) {
				msg("網絡連接錯誤!"+JSON.stringify(error));

			}
		});
}
// 添加收藏
function addOrRemove(tableId,type,picUrl){
	checkLogin();
		$.ajax({
			method : "POST",
			async:false,
			url : pathUrl + "/api/myCollection/addOrRemove.do",
			data : {tableId:tableId,type:type,picUrl:picUrl},
			type : "post",
			async: false,
			jsonpCallback:'cd',
			success : function(result) {
				msg(result.msg);
				if(result.code==0){
					window.location.reload();
				}
			},
			error : function(error) {
				msg("網絡連接錯誤!"+JSON.stringify(error));

			}
		});
}

function typeTime(time){
	if(time){
 	 var vipEnd=time.split(" ")[0].split("-");
	 return vipEnd[0]+"年"+vipEnd[1]+"月"+vipEnd[2]+"日";
	}
}
function typeTimeStr(time){
	if(time){
 	 var vipEnd=time.split(" ")[0];
	 return vipEnd[0]+vipEnd[1]+vipEnd[2]+vipEnd[3]+"年"+vipEnd[4]+vipEnd[5]+"月"+vipEnd[6]+vipEnd[7]+"日";
	}
}

function getQueryString(name) { 
	  var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i"); 
	  var r = window.location.search.substr(1).match(reg); 
	  if (r != null) return decodeURI(r[2]); return null; 
}
/**
 * 分享
 * 
 * @param content
 */
function whatsapp(content){
	var fakeLink = document.createElement('a');
	var shareUrl =encodeURIComponent(content);
	fakeLink.setAttribute('href', 'whatsapp://send?text='+shareUrl);
	fakeLink.setAttribute('data-action', 'share/whatsapp/share');
	fakeLink.click();
}
function msg(msg){
	
	layer.open({
		content: msg
		,skin: 'msg'
		,time: 2 // 2秒后自动关闭
  });
}

function openMap(){
	let browser = {
	        versions: function () {
	            let u = navigator.userAgent,
	                app = navigator.appVersion;
	            return {
	                trident: u.indexOf('Trident') > -1, /*IE内核*/
	                presto: u.indexOf('Presto') > -1, /*opera内核*/
	                webKit: u.indexOf('AppleWebKit') > -1, /*苹果、谷歌内核*/
	                gecko: u.indexOf('Gecko') > -1 && u.indexOf('KHTML') == -1, /*火狐内核*/
	                mobile: !!u.match(/AppleWebKit.*Mobile.*/), /*是否为移动终端*/
	                ios: !!u.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/), /*ios终端*/
	                // android: u.indexOf('Android') > -1 || u.indexOf('Linux') > -1, /*android终端或者uc浏览器*/
	                android: u.indexOf('Android') > -1 || u.indexOf('Adr') > -1,
	                iPhone: u.indexOf('iPhone') > -1, /*是否为iPhone或者QQHD浏览器*/
	                iPad: u.indexOf('iPad') > -1, /*是否iPad*/
	                webApp: u.indexOf('Safari') == -1, /*是否web应该程序，没有头部与底部*/
	                souyue: u.indexOf('souyue') > -1,
	                superapp: u.indexOf('superapp') > -1,
	                weixin: u.toLowerCase().indexOf('micromessenger') > -1,
	                Safari: u.indexOf('Safari') > -1,
	                uc: u.indexOf('Linux') > -1
	            };

	        }(),
	        language: (navigator.browserLanguage || navigator.language).toLowerCase()
	    };
	 if (browser.versions.ios) {
		 window.location.href = "https://www.google.com/maps/dir/?api=1&origin="+startAddress+"&destination="+endAddress+"&travelmode=driving";
		 } else if (browser.versions.android)
		 { window.location.href = "https://www.google.com/maps/dir/?api=1&origin="+startAddress+"&destination="+endAddress+"&travelmode=driving"; 
		 }
}

