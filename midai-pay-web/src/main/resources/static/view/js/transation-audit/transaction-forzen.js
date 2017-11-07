//交易冻结

//交易冻结条件搜索
function jydjSearch(args) {
    var formJson = UTIL.form2json({
        "form": "#jydjForm"
    });
    for (var value in formJson) {
        args[value] = formJson[value];
    }
    return args;
}

//判断是否可以进行解冻操作

function forzenTimeStyle(index,row){
	var unforzenTime = row.unfrozenTime;
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
    $('[data-toggle="table"]').bootstrapTable();
    var jydjTableData = $("#jydjTable").bootstrapTable("getData");
    var $jydjForm = $("#jydjForm");
    //  时间插件的初始化
    $('.my_date').datetimepicker({
        "minView": "month",
        "language": 'zh-CN',
        "format": 'yyyy-mm-dd',
        "autoclose": true,
        "pickerPosition": "bottom-left"
    });

    //点击刷新搜索table
    $("#jydjSearchBtn").click(function (e) {
        e.preventDefault();
        if(jydjTableData.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var jydjTableRefreshUrl = $("#jydjTable").data("url") + "?" + randomNum;
            $("#jydjTable").bootstrapTable("refresh", {
                url: jydjTableRefreshUrl
            });
            jydjTableData = $("#jydjTable").bootstrapTable("getData");
        }
   	 else{
   		 $("#jydjTable").bootstrapTable("selectPage", 1);
   	 }
    });

    //清空搜索条件
    $("#jydjEmptyBtn").click(function (e) {
        e.preventDefault();
        document.getElementById("jydjForm").reset();
        $("#jydjTable").bootstrapTable("selectPage", 1)
    });


    //批量解冻
    var unFrozenArr;  //声明为批量解冻请求
    $("#unFrozenBtn").on('click', function () {
        unFrozenArr = [];
        var getUnFrozen = $('#jydjTable').bootstrapTable("getSelections");
        if(getUnFrozen.length==0){
            var  text = "请选择您需要解冻的流水";
            UTIL.clickDisappearSwal(text);
        }else{
            var hostTransssnArr = [];
            var hostTransssnArr1 = [];
            for(var i=0;i<getUnFrozen.length;i++){
                if(getUnFrozen[i].unfrozenTime == null||getUnFrozen[i].unfrozenTime == ""){
                    hostTransssnArr.push(getUnFrozen[i].hostTransssn);
                    unFrozenArr.push(getUnFrozen[i].id);
                }else{
                    hostTransssnArr1.push(getUnFrozen[i].hostTransssn+"\n");
                }
            }
           // console.log(unFrozenArr);
           // console.log(hostTransssnArr);
            //console.log(hostTransssnArr1);
            if(hostTransssnArr.length!==0&&hostTransssnArr1.length==0){
                $("#jydjModal").modal();
                $(".transactionForzenIds").html(hostTransssnArr.toString());
            }else if(hostTransssnArr.length!==0&&hostTransssnArr1.length!==0){
                var text ="流水"+hostTransssnArr1+"已解冻,该流水不能再进行解冻操作!";
                UTIL.clickDisappearSwal(text);
            }else if(hostTransssnArr.length==0&&hostTransssnArr1.length!==0){
                var text = "流水"+hostTransssnArr1+"已解冻,该流水不能再进行解冻操作!";
                UTIL.clickDisappearSwal(text);
            }else{
                var text = "请选择可以解冻的流水";
                UTIL.clickDisappearSwal(text);
            }
        }
    });


    //  确认解冻选中的商户，刷新页面
    $("#sureUnFrozenBtn").click(function () {
        $.ajax({
            url: "../tradefrozenreason/updateTradeunfrozen",
            data: JSON.stringify(unFrozenArr),
            success: function (res) {
                $("#jydjModal").modal("hide");
                if (res > 0) {
                    var  text = "成功解冻选中的交易";
                    UTIL.autoDisappearSwal(text, function () {
                        $("#jydjTable").bootstrapTable("selectPage", 1);
                        if($("#tranSettleListTable")){
                            $("#tranSettleListTable").bootstrapTable("selectPage", 1);
                        }
                    });
                } else {
                    var text = "解冻失败，请重新操作！";
                    UTIL.clickDisappearSwal(text, function () {
                        $("#jydjModal").modal("hide");
                    });
                }
            }
        });
    });

    // 商户冻结EXCEL下载
    $("#jydjDownBtn").click(function () {
    	 var selectData = $("#jydjTable").bootstrapTable("getData");
    	var selectArr = $("#jydjTable").bootstrapTable("getSelections");
        var imercId = $jydjForm.find("input[name='mercId']").val();
        var imercName = $jydjForm.find("input[name='mercName']").val();
        var ifrozenTimeBegin = $jydjForm.find("input[name='frozenTimeBegin']").val();
        var ifrozenTimeEnd = $jydjForm.find("input[name='frozenTimeEnd']").val();
        var iunfrozenTimeBegin = $jydjForm.find("input[name='unfrozenTimeBegin']").val();
        var iunfrozenTimeEnd = $jydjForm.find("input[name='unfrozenTimeEnd']").val();
        var ihostTransssn = $jydjForm.find("input[name='hostTransssn']").val();
        if(selectData.length>0){
        	  window.location = "../tradefrozenreason/excelExport?mercId=" + imercId
              + "&mercName=" + imercName
              + "&frozenTimeBegin=" + ifrozenTimeBegin
              + "&frozenTimeEnd=" + ifrozenTimeEnd
              + "&unfrozenTimeBegin=" + iunfrozenTimeBegin
              + "&unfrozenTimeEnd=" + iunfrozenTimeEnd
              + "&hostTransssn=" + ihostTransssn
        }else{
        	 UTIL.clickDisappearSwal("暂无数据下载！")
        }

    })


});