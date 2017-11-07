    //历史交易明细查询搜索参数设置
    function searchHisData(args){
        var jsonData = UTIL.form2json({
            'form' : "#hisSearchForm"
        });
        for(var value in jsonData){
            args[value] = jsonData[value].trim();
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
            'form' : "#hisSearchForm"
        });
    //table初始化
    $('[data-toggle="table"]').bootstrapTable();
    var  lsjymxTableData = $("#hisTransTable").bootstrapTable("getData");
    //时间插件的初始化              
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });

    //历史交易明细查询按钮事件
    $("#searchHisBtn").on('click',function(e){
        e.preventDefault();
        jsonData = UTIL.form2json({
            'form' : "#hisSearchForm"
        });
        if(lsjymxTableData.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var lsjymxTableRefreshUrl = $("#hisTransTable").data("url") + "?" + randomNum;
            $("#hisTransTable").bootstrapTable("refresh", {
                url:lsjymxTableRefreshUrl
            });
            lsjymxTableData = $("#hisTransTable").bootstrapTable("getData");
        }
   	 else{
   		 $("#hisTransTable").bootstrapTable("selectPage", 1);
   	 }

    });

    //清空历史交易明细查询条件按钮事件
    $("#emptyHisSearch").on('click',function(e){
        e.preventDefault();
        document.getElementById("hisSearchForm").reset();
        $("#hisTransTable").bootstrapTable('selectPage',1);
    });


    //历史交易明细查询列表下载 
    $("#downHisbtn").on('click',function(){

        var data = $("#hisTransTable").bootstrapTable("getData");
        if(data.length>0){
            window.location="../dealtotal/excelExport?hostTransSsn="+jsonData.hostTransSsn
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
            UTIL.clickDisappearSwal("暂无数据下载！");
        }     
    });

});