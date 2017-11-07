(function(){
	var $form = $("#updateInfoForm");
	$form.json2form(USERALLINFO);
	$.ajax({
        method:'post',
        url:'/system/org/findorgcatalogue/'+USERALLINFO.orgid,
        dataType:'text',
        success:function(res){
        	if(res){
        		$("#userInfoModifyOrg").val(res);
        	}
        }
	});
	$form.formValidation({
            message: '这个字段还没验证',
            icon: {
                valid: 'glyphicon',
                invalid: 'glyphicon'
            },
            fields :{
                //账号
                    'loginname': {
                        validators: {
                            notEmpty: {
                                message: '输入不能为空！'
                            }
                        }
                    },
                    //姓名
                    'username': {
                        validators: {
                            notEmpty: {
                                message: '输入不能为空！'
                            }
                        }
                    },
                    //邮箱
                    'mail': {
                        validators: {
                            notEmpty: {
                                message: '输入不能为空！'
                            },
                            regexp: {
                                regexp: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
                                        message: '请填写正确的邮箱！'
                            }
                        }
                    },
                    //手机
                    'mobile': {
                        validators: {
                            notEmpty: {
                                message: '输入不能为空！'
                            },
                            regexp: {
                                regexp: /^1[34578]\d{9}$/,
                                message: '请填写11位正确的手机号码！'
                            }
                        }
                    }
            }
        }).on("success.form.fv", function(e){
            e.preventDefault();
            var formData = JSON.stringify(UTIL.form2json({
            	form : "#updateInfoForm"
            }));
            $.ajax({
            	url : "../system/user/updateuserinfo",
            	data : formData,
            	dataType : "text",
            	success : function(res){
            		if(res == 1){
                        UTIL.autoDisappearSwal("提交成功",function(){
                            UTIL.closeCurrentPage({
                                "delUrl":"updateUserInfo.html"
                            });
                        })
            			
            		}
            	}
            })
        })

        // 点击取消按钮
        $("#update-info-cancel-btn").on("click", function(){
        	UTIL.closeCurrentPage({
	            "delUrl":"updateUserInfo.html"
	        });
        })

})();