//清分清算搜索参数设置
$qfqsForm = $("#qfqsForm");

function qfqsSearch(args){

    var formJson = UTIL.form2json({
        "form" : "#qfqsForm"
    });
    for(var value in formJson){
        args[value] = formJson[value];
    }

    return args;
}

function qfqsStatus(value,row,index){
    if(value == 0){
        return "待审";
    }else if(value == 1){
        return "审核通过";
    }
}

$(function(){
	//初始化
	$('[data-toggle="table"]').bootstrapTable();
    var qfqsTableData = $("#qfqsTable").bootstrapTable("getData");
    //时间插件的初始化               
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });

    

	//查询按钮事件
	$("#qfqsSearchBtn").on('click',function(e){
        e.preventDefault();
        if(qfqsTableData.length == 0){
            var randomNum = parseInt(Math.random()*99999);
            var qfqsTableRefreshUrl = $("#qfqsTable").data("url") + "?" + randomNum;
            $("#qfqsTable").bootstrapTable('refresh',{
                url :　qfqsTableRefreshUrl
            });
            qfqsTableData = $("#qfqsTable").bootstrapTable("getData");
        }
        else{
            $("#qfqsTable").bootstrapTable('selectPage',1);
        }
	});

	//重置查询条件
	$("#qfqsEmptyBtn").on('click',function(e){
        e.preventDefault();
		document.getElementById("qfqsForm").reset();
		if(qfqsTableData.length == 0){
            var randomNum = parseInt(Math.random()*99999);
            var qfqsTableRefreshUrl = $("#qfqsTable").data("url") + "?" + randomNum;
            $("#qfqsTable").bootstrapTable('refresh',{
                url :　qfqsTableRefreshUrl
            });
            qfqsTableData = $("#qfqsTable").bootstrapTable("getData");
        }
        else{
            $("#qfqsTable").bootstrapTable('selectPage',1);
        }
	});

	//通过按钮
	// var mercIds = "";
    var logNos;
	$("#passQsBtn").on('click',function(){
		var tempMerNo = "";
        var templogNo = "";
		var getPassQs = $("#qfqsTable").bootstrapTable("getSelections");
		if(getPassQs.length<1){
			UTIL.clickDisappearSwal("请选择您需要通过的流水");
		}else{
			
            for (var i = 0; i < getPassQs.length; i++) {
                if(i === 0){
                    tempMerNo = getPassQs[i].mercId;
                    templogNo = '"' + getPassQs[i].logNo + '"';
                }
                else{
                    tempMerNo += "," + getPassQs[i].mercId;
                    templogNo += "," + '"' + getPassQs[i].logNo +'"';
                }
            }
         //   console.log(templogNo);
            $(".passQsCont").html(tempMerNo.toString());
            // mercIds = tempMerNo;
            logNos = templogNo;
            $("#passQSModal").modal('show');
		}
		
	});

	//通过确认按钮
	$("#surePassBtn").click(function () {

        $.ajax({
            url: "../getmoney/batchupdate",
            data : logNos,
            success: function (res) {
                $("#passQSModal").modal("hide");
                if (res > 0) {
                    UTIL.autoDisappearSwal("成功通过清算!",function(){
                        $("#qfqsTable").bootstrapTable("refresh");
                    });
                } else {
                    UTIL.clickDisappearSwal("通过清算失败，请重新操作!");
                }
            }
        });
    });



})