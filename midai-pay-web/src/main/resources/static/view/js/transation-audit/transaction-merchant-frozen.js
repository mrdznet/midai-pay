/**
 * Created by ykk  on 2016/10/19.
 */
//商户冻结管理

//初始化bootstrap-table
$('[data-toggle="table"]').bootstrapTable();

//商户冻结条件搜索
function merchantSearchTable(args) {
    args.mercId = $("input[name='merchant-forzen-ticket']").val(); // 商户小票号
    args.mercName = $("input[name='merchant-forzen-name']").val();    //商户名称
    args.frozenTimeBegin = $("input[name='merchantf-date-begin']").val();   //冻结日期
    args.frozenTimeEnd = $("input[name='merchantf-date-end']").val();      //冻结日期末
    args.unfrozenTimeBegin = $("input[name='merchant-sf-date-begin']").val();  //解结日期
    args.unfrozenTimeEnd = $("input[name='merchant-sf-date-end']").val();    //解结日期末
    return args;
}


//判断是否可以解冻操作‘
function unfrozenTimeStyle(index,row){
	var unforzenTime = row.unfrozenTime;
	//console.log(typeof _unforzenTime)
	if(!unforzenTime){
		return {
			disabled:false
		}
	}else{
		return {
			disabled:true
		}
	}
}


$(function () {
	var shdjTableData = $("#merchantForzenListTable").bootstrapTable("getData");
    //  时间插件的初始化
    $('.my_date').datetimepicker({
        "minView": "month",
        "language": 'zh-CN',
        "format": 'yyyy-mm-dd',
        "autoclose": true,
        "pickerPosition": "bottom-left"
    });

    //点击刷新搜索table
    $("#merchantForzenSearch").click(function (e) {
        e.preventDefault();
        if(shdjTableData.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var shdjTableRefreshUrl = $("#merchantForzenListTable").data("url") + "?" + randomNum;
            $("#merchantForzenListTable").bootstrapTable("refresh", {
                url: shdjTableRefreshUrl
            });
            shdjTableData = $("#merchantForzenListTable").bootstrapTable("getData");
        }
   	 else{
   		 $("#merchantForzenListTable").bootstrapTable("selectPage", 1);
   	 }
    });

    //清空搜索条件
    $("#merchantForzenEmpty").click(function (e) {
        e.preventDefault();
        $("#merchantForzenForm").find("input").each(function () {
            $(this).val("");
        });
        $("#merchantForzenListTable").bootstrapTable("selectPage", 1);
    });

    //批量解冻
    var merchantForzenIdsString; //声明为批量解冻请求
    $(".shop-solve-frozen").click(function () {
        merchantForzenIdsString = [];
        var willSForzenModel = $('#merchantForzenListTable').bootstrapTable("getSelections");
        var willSForzenModel_length = willSForzenModel.length;
        if (willSForzenModel_length == 0) {
            var text = "请选择您要解冻的商户";
            UTIL.clickDisappearSwal(text);
        } else {
            var merchantForzenIds = [];
            var noMerchantForzenIds = [];
            for (var i = 0; i < willSForzenModel_length; i++) {
                if (willSForzenModel[i].unfrozenTime == null || willSForzenModel[i].unfrozenTime == "") {
                    merchantForzenIds.push(willSForzenModel[i].mercId);
                    merchantForzenIdsString.push("" + willSForzenModel[i].mercId + ""); //批量解冻传值；
                } else {
                    noMerchantForzenIds.push(willSForzenModel[i].mercId+"\n");
                }
            }
            if (merchantForzenIds.length !== 0 && noMerchantForzenIds.length == 0) {
                $("#shopSolveFrozen").modal();
                $(".merchantForzenIds").html(merchantForzenIds.toString());
            } else if (merchantForzenIds.length !== 0 && noMerchantForzenIds.length !== 0) {
                var text ="商户"+noMerchantForzenIds+ "已解冻,不能再进行解冻操作!";
                UTIL.clickDisappearSwal(text);
            } else if (merchantForzenIds.length == 0 && noMerchantForzenIds.length !== 0) {
                var text ="商户"+noMerchantForzenIds+ "已解冻，不能再进行解冻操作!";
                UTIL.clickDisappearSwal(text);
            } else {
                var text = "请您选择可以解冻的商户";
                UTIL.clickDisappearSwal(text);
            }
        }
    });

    //确认解冻选中的商户，刷新页面
  
    $("#merchantForzenSure").click(function () {
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            url: "../frozenreason/customerunfrozen",
            data: JSON.stringify(merchantForzenIdsString),
            success: function (res) {
                $("#shopSolveFrozen").modal("hide");
                if (res > 0) {
                    var text = "成功解冻商户！";
                    UTIL.clickDisappearSwal(text, function () {
                        $("#merchantForzenListTable").bootstrapTable("selectPage", 1);
                        if ($("#tranSettleListTable")) {
                        	/*  var jyjsTableData = $("#tranSettleListTable").bootstrapTable("getData");
                        	 if(jyjsTableData.length === 0 ){
                                 var randomNum = parseInt(Math.random()*99999);
                                 var jyjsTableRefreshUrl = $("#tranSettleListTable").data("url") + "?" + randomNum;
                                 $("#tranSettleListTable").bootstrapTable("refresh", {
                                     url: jyjsTableRefreshUrl
                                 });
                             }*/                                               	
                            $("#tranSettleListTable").bootstrapTable("selectPage", 1);
                        }
                    });
                } else {
                    var text = "解冻商户失败，请重新操作！";
                    UTIL.clickDisappearSwal(text, function () {
                        $("#shopSolveFrozen").modal("hide");
                    });
                }
            }
        });
    });

    //新增冻结
    //手动触发模态框
    $(".shop-many-frozen").click(function () {
        $("#addFrozenModal").modal();
    });

    //新增冻结表单的校验
    $('#addForzenMerchantCus').formValidation({
        message: '此值无效', // 此值无效
        icon: {
            valid: 'glyphicon',
            invalid: 'glyphicon',
            validating: 'glyphicon'
        },
        fields: {
            'mercId': {
                validators: {
                    notEmpty: {
                        message: '输入不能为空！'
                    },
                    callback: {
                        message: '该商户已经冻结！',
                        callback: function (value, validator) {                                     
                            var data = {
                            		frozenTimeBegin:"",
                            		frozenTimeEnd:"",
                            		limit:0,
                            		mercId:"",
                            		mercName:"",
                            		offset:0,
                            		order:"asc",
                            		unfrozenTimeBegin:"",
                            		unfrozenTimeEnd:"",                           	
                            		pageNumber:1
                            }
                            var flag=0;
                               if(value!==""){
                            	   $.ajax({
                                       url: "/frozenreason/list",
                                       type: "post",
                                       async: false,
                                       data:JSON.stringify(data),
                                       success: function (res) {
                                       	  var data = {
                                             		frozenTimeBegin:"",
                                             		frozenTimeEnd:"",
                                             		limit:0,
                                             		mercId:"",
                                             		mercName:"",
                                             		offset:0,
                                             		order:"asc",
                                             		unfrozenTimeBegin:"",
                                             		unfrozenTimeEnd:"",   
                                             		pageSize:res.total,
                                             		pageNumber:1
                                             }
                                       	  $.ajax({
                                             url: "/frozenreason/list",
                                             type: "post",
                                             async: false,
                                             data:JSON.stringify(data),
                                               success: function (res) {
                                       	     //   var mercIdAll=[];
                                               	for(var i=0;i<res.rows.length;i++){                                               
                                               		if(value==res.rows[i].mercId&&!res.rows[i].unfrozenTime){
                                               			flag = res.rows[i].mercId;
                                               		}
                                       	         }                                            	
                                              }
                                           })                                      
                                       }                           	
                                   })                     
                               if(flag==0){
                        		   return true
                        	   }else{
                        		   return false
                        	   }
                               }else{
                            	   return true;
                               }                                                       
                        }
                    }
                }
            },
            'frozenReason': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '输入不能为空！'
                    },
                    stringLength: {
                        min: 0,
                        max: 260,
                        message: '请输入260以内的字符！'
                    }
                }
            }
        }
    }).on('success.form.fv', function (e, data) {
        e.preventDefault();
        showLoading();
        var forzenMerchantName = $("#forzenMerchantName").val();
        var forzenMerchantCause = $("#forzenMerchantCause").val();
        var willforzenMerchantNew = [];
        willforzenMerchantNew.push("" + forzenMerchantName + "");
        $.ajax({
            type: "POST",
            url: "../frozenreason/customerfrozen?frozenReason=" + forzenMerchantCause,
            data: JSON.stringify(willforzenMerchantNew),
            success: function (res) {
                hideLoading();
                $("#addFrozenModal").modal("hide");
                if (res == 1) {
                    var text = "成功新增冻结商户！";
                    UTIL.autoDisappearSwal(text, function () {
                        $("#merchantForzenListTable").bootstrapTable("selectPage", 1);
                    });
                } else {
                    var text = "新增冻结商户失败，请重新操作！";
                    UTIL.clickDisappearSwal(text);
                }
            }
        });
    });
    $("#addFrozenModal").on("hide.bs.modal", function () {
        document.getElementById("addForzenMerchantCus").reset();
        $("#addForzenMerchantCus").data('formValidation').resetForm();
    }); //新增冻结商户结束---------------》

    // 商户冻结EXCEL下载

    $("#merchantForzenDown").click(function () {
    	var selectData = $("#merchantForzenListTable").bootstrapTable("getData");
    	var _merchantForzenListTable = $('#merchantForzenListTable');
        var merchantSfTicket = $("input[name='merchant-forzen-ticket']").val(); // 商户小票号
        var merchantSfName = $("input[name='merchant-forzen-name']").val();    //商户名称
        var merchantfdateBegin = $("input[name='merchantf-date-begin']").val();   //冻结日期
        var merchantfdateEnd = $("input[name='merchantf-date-end']").val();      //冻结日期末
        var merchantSfdateBegin = $("input[name='merchant-sf-date-begin']").val();  //解结日期
        var merchantSfdateEnd = $("input[name='merchant-sf-date-end']").val();    //解结日期末
        if (selectData.length > 0) {
            window.location = "../frozenreason/excelExport?mercId=" + merchantSfTicket
                + "&mercName=" + merchantSfName
                + "&frozenTimeBegin=" + merchantfdateBegin
                + "&frozenTimeEnd=" + merchantfdateEnd
                + "&unfrozenTimeBegin=" + merchantSfdateBegin
                + "&unfrozenTimeEnd=" + merchantSfdateEnd
        }
        else {
            var text = "暂无数据下载！";
            UTIL.clickDisappearSwal(text);
        }
    });  //列表下载结束-------------》


});