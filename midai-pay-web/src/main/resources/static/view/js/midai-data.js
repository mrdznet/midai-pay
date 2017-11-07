$.ajaxSetup({
	type:"POST",
	contentType:"application/json",
	dataType:"json",
	error: function(xhr, textStatus, errorThrown) {
		hideLoading();
		if(xhr.responseText.length > 200){
			msg = "系统出错，请联系管理员！";
			UTIL.clickDisappearSwal(msg);
		}
		else{
			var msg=jQuery.parseJSON(xhr.responseText).errorMsg || "系统出错，请联系管理员！";
			if(msg === 'session-out'){
				window.location.href = "/login.html";
			}else if(xhr.status===408){
           		var ossText = jQuery.parseJSON(msg);
           		UTIL.clickDisappearSwal("登陆超时 !", function(){
           			window.location.href = "/login.html";
           		});
           }else if(xhr.status===666){
           		msg = "任务不存在";
           		UTIL.clickDisappearSwal(msg);
           }
           else if(/^[\u4e00-\u9fa5]*$/.test(xhr.responseText.substr(0,2))){
           		msg = xhr.responseText;
           		UTIL.clickDisappearSwal(msg);
           }
           else{
           		UTIL.clickDisappearSwal(msg);
           }
		}
	}
});

