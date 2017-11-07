//代理商交易明细查询搜索参数设置
    function searchATData(args){
        var jsonData = UTIL.form2json({
            'form' : "#agentTransForm"
        });
        for(var value in jsonData){
            args[value] = jsonData[value];
        }
        return args;
    }
//交易类型返回字段
    function transType(value,row,index){
        if(value == "0200"){
            return "消费";
        }else if(value == "0400"){
            return "消费冲正";
        }else if(value == 2){
            return "余额查询";
        }else if(value == 3){
            return "提现申请";
        }

    }

//交易状态返回字段
    function transStatus(value,row,index){
        if(value == 0){
            return "成功";
        }else if(value == 1){
            return "已上传";
        }else if(value == 5){
            return "失败";
        }

    }
//卡类型返回字段
    function historyCardType(index,row){
        var historyCardType = row.cardKind;
        if(historyCardType == "01"){
            return "借记卡";
        }else if(historyCardType == "02"){
            return "贷记卡";
        }
    }


$(function(){
    var jsonData = UTIL.form2json({
            'form' : "#agentTransForm"
        });
    //table初始化
    $('[data-toggle="table"]').bootstrapTable();
    var	dlsjyTableData= $("#agentTransTable").bootstrapTable("selectPage", 1);
    //时间插件的初始化              
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });

    //代理商交易明细查询按钮事件
    $("#searchATBtn").on('click',function(e){
        e.preventDefault();
        jsonData = UTIL.form2json({
            'form' : "#agentTransForm"
        });
        if(dlsjyTableData.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var dlsjyTableRefreshUrl = $("#agentTransTable").data("url") + "?" + randomNum;
            $("#agentTransTable").bootstrapTable("refresh", {
                url: dlsjyTableRefreshUrl
            });
            dlsjyTableData = $("#agentTransTable").bootstrapTable("getData");
        }
   	 else{
   		 $("#agentTransTable").bootstrapTable("selectPage", 1);
   	 }

    });

    //清空代理商交易明细查询条件按钮事件
    $("#emptyATBtn").on('click',function(e){
        e.preventDefault();
        document.getElementById("agentTransForm").reset();
        $("#agentTransTable").bootstrapTable('selectPage',1);
    });


    //代理商交易明细查询列表下载
    $("#downATbtn").on('click',function(){
        var data = $("#agentTransTable").bootstrapTable("getData");
        if(data.length>0){
            window.location="../dealtotal/agentExcelExport?hostTransSsn="+jsonData.hostTransSsn
                +"&mchntCodeIn="+jsonData.mchntCodeIn
                +"&deviceNoIn="+jsonData.deviceNoIn
                +"&transStatus="+jsonData.transStatus
                +"&transCode="+jsonData.transCode
                +"&transCardNo="+jsonData.transCardNo
                +"&transTimeBegin="+jsonData.transTimeBegin
                +"&transTimeEnd="+jsonData.transTimeEnd
                +"&mchntName="+jsonData.mchntName
                +"&mchntCodeOut="+jsonData.mchntCodeOut
                +"&transAmtBegin="+jsonData.transAmtBegin
                +"&transAmtEnd="+jsonData.transAmtEnd
                +"&mobile="+jsonData.mobile
                +"&routInstIdCd="+jsonData.routInstIdCd

        }
        else{
        	 UTIL.clickDisappearSwal("暂无数据下载!");
        }        
    });
});