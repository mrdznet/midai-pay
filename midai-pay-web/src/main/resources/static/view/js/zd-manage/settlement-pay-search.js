//打款结果搜索参数设置
$dkjgForm = $("#dkResultForm");
function dkResultSearch(args){
    var jsonData = UTIL.form2json({
        'form' : "#dkResultForm"
    });
    for(var value in jsonData){
        args[value] = jsonData[value].trim();
    }
    return args;
}

function payStateF(value,row,index){
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

$(function(){
    var queryData = {
        "imercId" : $dkjgForm.find("input[name='mercId']").val().trim(),
        "imercName" : $dkjgForm.find("input[name='mercName']").val().trim(),
        "iaccountName" : $dkjgForm.find("input[name='accountName']").val().trim(),
        "iaccountNo" : $dkjgForm.find("input[name='accountNo']").val().trim(),
        "ipayState" : $dkjgForm.find("select[name='payState']").val().trim(),
        "ibranchBankName" : $dkjgForm.find("input[name='branchBankName']").val().trim(),
        "ipayTimeStart" : $dkjgForm.find("input[name='payTimeStart']").val().trim(),
        "ipayTimeEnd" : $dkjgForm.find("input[name='payTimeEnd']").val().trim()
    };
	//初始化
	$('[data-toggle="table"]').bootstrapTable();
       var  dkjgTableData = $("#dkResultTable").bootstrapTable("getData");
    //时间插件的初始化              
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });

	//查询按钮事件
	$("#dkResultSearchBtn").on('click',function(){
        queryData = {
            "imercId" : $dkjgForm.find("input[name='mercId']").val().trim(),
            "imercName" : $dkjgForm.find("input[name='mercName']").val().trim(),
            "iaccountName" : $dkjgForm.find("input[name='accountName']").val().trim(),
            "iaccountNo" : $dkjgForm.find("input[name='accountNo']").val().trim(),
            "ipayState" : $dkjgForm.find("select[name='payState']").val().trim(),
            "ibranchBankName" : $dkjgForm.find("input[name='branchBankName']").val().trim(),
            "ipayTimeStart" : $dkjgForm.find("input[name='payTimeStart']").val().trim(),
            "ipayTimeEnd" : $dkjgForm.find("input[name='payTimeEnd']").val().trim()
        };
        stopUpdateSettleStatusInterval();
		  if(dkjgTableData.length === 0 ){
	            var randomNum = parseInt(Math.random()*99999);
	            var dkjgTableRefreshUrl = $("#dkResultTable").data("url") + "?" + randomNum;
	            $("#dkResultTable").bootstrapTable("refresh", {
	                url: dkjgTableRefreshUrl
	            });
	            dkjgTableData = $("#dkResultTable").bootstrapTable("getData");
	        }
	   	 else{
	   		 $("#dkResultTable").bootstrapTable("selectPage", 1);
	   	 }
	});
	//重置查询条件
	$("#dkResultEmptyBtn").on('click',function(){
		document.getElementById("dkResultForm").reset();
        stopUpdateSettleStatusInterval();
		$("#dkResultTable").bootstrapTable('selectPage',1);
	});


        //代理商查询列表下载
    $("#downDkjgBtn").click(function(){
    	var  selectData = $("#dkResultTable").bootstrapTable("getData");
        // var imercId = $dkjgForm.find("input[name='mercId']").val();
        // var imercName = $dkjgForm.find("input[name='mercName']").val();
        // var iaccountName = $dkjgForm.find("input[name='accountName']").val();
        // var iaccountNo  = $dkjgForm.find("input[name='accountNo']").val();
        // var ipayState = $dkjgForm.find("select[name='payState']").val();
        // var ibranchBankName = $dkjgForm.find("input[name='branchBankName']").val();
        // var ipayTimeStart = $dkjgForm.find("input[name='payTimeStart']").val();
        // var ipayTimeEnd = $dkjgForm.find("input[name='payTimeEnd']").val();
        if(selectData.length>0){
            window.location="../autopay/excelExport?mercId="+queryData.imercId
                +"&mercName="+queryData.imercName
                +"&accountName="+queryData.iaccountName
                +"&accountNo="+queryData.iaccountNo
                +"&payState="+queryData.ipayState
                +"&bankName="+queryData.ibranchBankName
                +"&payTimeStart="+queryData.ipayTimeStart
                +"&payTimeEnd="+queryData.ipayTimeEnd  
        }else{
        	 UTIL.clickDisappearSwal("暂无数据下载！")
        }   
    });

    // 刷新结算状态
    var updateSettleStatusInterval = function(){
        var flag = false;
        $.ajax({
                type: "get",
                url: "../autopay/payQuery",
                async: false,
                success: function(res){
                    $("#dkResultTable").bootstrapTable("refresh");
                    flag = true;
                }
            })
        return flag;
    };
    var stopUpdateSettleStatusInterval = function(){
        $("#autoUpdateSettleStatus").text("自动刷新结算状态");
        if(timer !== null){
            clearInterval(timer);
        }
    }
    var timer = null;
    $("#autoUpdateSettleStatus").click(function(){
        if($(this).text() == "自动刷新结算状态"){
            $(this).text("关闭自动刷新");
            var timerNum = parseInt($("#settltUpdateTime").val()+"000");
            timer = setInterval(updateSettleStatusInterval, timerNum)
        }
        else{
            stopUpdateSettleStatusInterval();
        }      
    })
        


})