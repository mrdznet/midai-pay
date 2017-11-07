//搜索参数设置
function profitSearchList(args){
    var jsonData = UTIL.form2json({
        'form' : "#agent_profit_list_form"
    });
    for(var value in jsonData){
        args[value] = jsonData[value];
    }
    return args;
}

function type_list(value, row, index) {
    var transType=row.transType
    switch (transType) {
        case 1:
            return [
                '<span>',
                'MPOS',
                '</span>  '
            ].join('');
            break;

        case 2:
            return [
                '<span>',
                '无卡',
                '</span>  '
            ].join('');
            break;

        case 3:
            return [
                '<span>',
                '传统POS',
                '</span>  '
            ].join('');

            break;
        case 4:
            return [
                '<span>',
                '扫码-微信',
                '</span>  '
            ].join('');

            break;
        case 5:
            return [
                '<span>',
                '扫码-支付宝',
                '</span>  '
            ].join('');
            break;
        case 6:
            return [
                '<span>',
                '扫码-银联',
                '</span>  '
            ].join('');
            break;
        case 7:
            return [
                '<span>',
                '扫码-花呗',
                '</span>  '
            ].join('');
            break;
        case 8:
            return [
                '<span>',
                '扫码-其他',
                '</span>  '
            ].join('');
            break;
        case 9:
            return [
                '<span>',
                '扫码-京东',
                '</span>  '
            ].join('');
            break;
    }
    if (row.settlementStatus==1){
        return [
            '<button class="btn btn-success disabled" onclick="modal_show(\''+ row.agentId+","+ row.settlementTime+ ","+ row.code+ '\')" title="分润">',
            '分润',
            '</button>  '
        ].join('');
    }else{
        return [
            '<button class="btn btn-success" onclick="modal_show(\''+ row.agentId+","+ row.settlementTime+ ","+ row.code+ '\')" title="分润">',
            '分润',
            '</button>  '
        ].join('');
    }
}


$(function(){
    //初始化
    $('[data-toggle="table"]').bootstrapTable();
    var profitTable = $("#profitTable_list").bootstrapTable("getData");
    //时间插件的初始化
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });
    //二次查询条件
    var agentId=null;
    var mercNo=null;
    var settlementTimeStart=null;
    var settlementTimeEnd=null;
    var new_profit_data={
        'agentId':agentId,
        'mercNo':mercNo,
        'settlementTimeStart':settlementTimeStart,
        'settlementTimeEnd':settlementTimeEnd
    }
    function get_profits_info(profit_data) {
        $.ajax({
            url: "/agentprofit/detail/querydetailsum",
            data:JSON.stringify(profit_data),
            type:'POST',
            datatype:'json',
            success: function (res) {
               $("#profitTotalAmount_list").text(res.profitTotalAmount);
                $("#transCount_List").text(res.transCount);
                $("#profitRetainedAmount_list").text(res.profitRetainedAmount);
            },
            error: function (res) {
                UTIL.clickDisappearSwal("利润详情系统出错，请联系管理员");
            }
        });
    }
    get_profits_info(new_profit_data)

    //查询按钮事件
    $("#agent_profit_search_list").on('click',function(){
        agentId=$("#agent_profit_list_form input[name=agentId]").val()
        mercNo=$("#agent_profit_list_form input[name=mercNo]").val()
        settlementTimeStart=$("#agent_profit_list_form input[name=settlementTimeStart]").val()
        settlementTimeEnd=$("#agent_profit_list_form input[name=settlementTimeEnd]").val()
        var profit_data={
            'agentId':agentId,
            'mercNo':mercNo,
            'settlementTimeStart':settlementTimeStart,
            'settlementTimeEnd':settlementTimeEnd
        }
        get_profits_info(profit_data);
        if(profitTable.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var zddkTableRefreshUrl = $("#profitTable_list").data("url") + "?" + randomNum;
            $("#profitTable_list").bootstrapTable("refresh", {
                url: zddkTableRefreshUrl
            });
        }
        else{
            $("#profitTable_list").bootstrapTable('selectPage',1);
        }


    });
    //重置查询条件
    $("#agent_profit_list_remove").on('click',function(){
        document.getElementById("agent_profit_list_form").reset();
        //$("#profitTable_list").bootstrapTable('selectPage',1);

        var profit_data={
            'agentId':"",
            'mercNo':"",
            'settlementTimeStart':"",
            'settlementTimeEnd':""
        };
        get_profits_info(profit_data);
        $("#profitTable_list").bootstrapTable('selectPage',1);

    });

    //代理商查询列表下载
    $("#agent_profit_down").click(function(){
        var selectAgent=$("#profitTable_list").bootstrapTable("getSelections");
        var agentId=$("input[name=agentId]").val()
        var mercNo=$("input[name=mercNo]").val()
        var settlementTimeStart=$("input[name=settlementTimeStart]").val()
        var settlementTimeEnd=$("input[name=settlementTimeEnd]").val()
        if(selectAgent.length<1){
            var data = $("#profitTable_list").bootstrapTable("getData");
            if(data.length > 0){
                window.location="/agentprofit/detail/exportdetail?agentId="+agentId
                    +"&mercNo="+ mercNo+"&settlementTimeStart="+ settlementTimeStart+"&settlementTimeEnd="+ settlementTimeEnd
            }
            else{
                UTIL.clickDisappearSwal("暂无数据下载!");
            }
        }else{
            var ids = [];
            for (var i = 0; i < selectAgent.length; i++) {
                ids.push(selectAgent[i].id);
            }

            window.location="/agentprofit/detail/exportdetail?ids="+ids;

        }

    })



})
