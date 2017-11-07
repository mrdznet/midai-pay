//自动打款搜索参数设置
$dksbcxForm = $("#dksbcxForm");
var dksbcxArr = [];
var dksbcxMercId = [];
var ierrorDkPath = "";
function dksbcxSearch(args){
    var jsonData = UTIL.form2json({
        'form' : "#dksbcxForm"
    });
    for(var value in jsonData){
        args[value] = jsonData[value];
    }
    return args;
}

//自动打款样式
function dksbcxHandle(value,row,index){
    var str = "自动打款";
    var temp = $("#setPayErrorTempl").html().replace("row.tixiLogno", row.tixiLogno).replace("row.mercId",row.mercId);
    return temp;
}
function changeSettltmentErrorPayStyle(row,index){
    if(row.payState === 4){
	     return {
	        css: {
	            background: "#FAB3AE"
	        }
	     }
    }
    return {
        css:{}
    }
}

function dksbcxPayStateF(value,row,index){
    var num = row.payState;
    if(num == 0){
        return "<span>" + value + "</span>";
    }else if(num == 5){
        return "<span style='color: #2f8db4'>" + value + "</span>";
    }else if(num == 3){
        return "<span style='color: green'>" + value + "</span>";
    }else if(num == 4){
        return "<span style='color: red'>" + value + "</span>";
    }
    else{
        return value;
    }
}

function dksbcxEvent(temp1,temp2){
    dksbcxArr = [];
    dksbcxMercId = [];
    if($("#errorDkPath").val() == ""){
        UTIL.clickDisappearSwal("请先选择打款通道");
    }else{   
        ierrorDkPath = $("#errorDkPath").val();
        dksbcxArr.push(temp1);
        $(".dksbcxCont").html(temp2);
        $("#payErrorQrdkModal").modal('show');
        $("#payErrorQrdkBtn").prop("disabled",false);
    }
}
function dksbcxNote(value, row, index){
    if(row.payState === 4){
        return "重新打款";
    }
    return "";
}

function errorPayZddkEvent(temp1,temp2){
    dksbcxArr = [];
    dksbcxMercId = [];
    if($("#dkPath").val() == ""){
        UTIL.clickDisappearSwal("请先选择打款通道");
    }else{   
        ierrorDkPath = $("#dkPath").val();
        dksbcxArr.push(temp1);
        $(".dksbcxCont").html(temp2);
        $("#payErrorQrdkModal").modal('show');
        $("#payErrorQrdkBtn").prop("disabled",false);
    }
}

$(function(){
	//初始化
	$('[data-toggle="table"]').bootstrapTable();
    $("#dksbcxTable").find('input[name="btSelectAll"]').remove();
    var errorPayList = '';
    // 标记为打款成功
    $("#dkcgMarkBtn").click(function(){
    	var state = 1;
    	var markPaySuccessList = $("#dksbcxTable").bootstrapTable("getSelections");

        if(markPaySuccessList.length < 1){
            UTIL.clickDisappearSwal("请先选择交易");
            return;
        }
        else {
            errorPayList = "";
            for(var i = 0; i < markPaySuccessList.length; i++){
                if(i === 0){
                    errorPayList += markPaySuccessList[i].tixiLogno;
                }
                else {
                    errorPayList += ',' + markPaySuccessList[i].tixiLogno;
                }
            }
            $("#payErrorMarkPaySuccessModal .dksbcxList").html(errorPayList);
            $('#payErrorMarkPaySuccessModal').modal('show');
        }
    });

    // 确定标记为打款成功按钮点击事件
    $("#markPaySuccessBtn").click(function(){
    	$.ajax({
    		url: '../autopay/signPay/' + errorPayList + '/1',
    		type: 'post',
    		success: function(res){
    			$('#payErrorMarkPaySuccessModal').modal('hide');
				UTIL.clickDisappearSwal("操作成功");
				$('#dksbcxTable').bootstrapTable('refresh');
    		}
    	})
    })

	//批量自动打款
	$("#alldksbcxBtn").on('click',function(){
        dksbcxArr = [];
        dksbcxMercId = [];
		var getdksbcxArr = $("#dksbcxTable").bootstrapTable("getSelections");
		if(getdksbcxArr.length<1){
            UTIL.clickDisappearSwal("请先选择交易");
		}else{
            if($("#errorDkPath").val() == ""){
                UTIL.clickDisappearSwal("请先选择打款通道");
            }else{
                $("#payErrorQrdkModal").modal('show');
                $("#payErrorQrdkBtn").prop("disabled",false);
                ierrorDkPath = $("#errorDkPath").val();
                for (var i = 0; i < getdksbcxArr.length; i++) {
                    dksbcxArr.push(getdksbcxArr[i].tixiLogno);
                    dksbcxMercId.push(getdksbcxArr[i].mercId)
                }
                $(".dksbcxCont").html(dksbcxMercId.toString());
            }
			
		}
		
	});

    // 刷新按钮点击事件
    $("#dksbcxListUpdate").click(function(){
        $("#dksbcxTable").bootstrapTable('selectPage', 1);
    })


	//通过确认按钮
	$("#payErrorQrdkBtn").click(function () {
        $(this).prop("disabled",true);
        $.ajax({
            url: "../autopay/pay/"+dksbcxArr.toString()+"/"+ierrorDkPath,
            success: function (res) {
                $("#payErrorQrdkModal").modal("hide");
                if (res == 1) {
                    UTIL.clickDisappearSwal("成功自动打款!",function(){
                        var randomNum = parseInt(Math.random()*99999);
                        var dksbcxTableRefreshUrl = $("#dksbcxTable").data("url") + "?" + randomNum;
                        $("#dksbcxTable").bootstrapTable("refresh", {
                            url: dksbcxTableRefreshUrl
                        });
                    });
                } else {
                    UTIL.clickDisappearSwal("打款失败，请重新操作!",function(){
                        $("#payErrorQrdkModal").modal("hide");
                    });

                }
            }
        });
    });



})