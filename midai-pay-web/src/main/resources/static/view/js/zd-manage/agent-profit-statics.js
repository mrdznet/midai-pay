//自动打款搜索参数设置
function profitSearch(args){
    var jsonData = UTIL.form2json({
        'form' : "#agent_profit_static_form"
    });
    for(var value in jsonData){
        args[value] = jsonData[value];
    }
    return args;
}
//单条分润
function agentDetailUpdata(value, row, index) {
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
function age(value,row,index){
//此处对value值做判断，不然value为空就会报错
    value=value?value:'';
    var length=value.length;
    if(length&&length>7){
        length=7;
    }
    return"<div style='width: 150px;' title ='"+value+"' >"+value+"</div>"
}
function age2(value,row,index){
//此处对value值做判断，不然value为空就会报错
    value=value?value:'';
    var length=value.length;
    if(length&&length>7){
        length=7;
    }
    return"<div style='width: 150px;' title ='"+value+"' >"+value+"</div>"
}
function modal_show(a,n) {
    $("#agernt_profit_modal").modal("show");
    var arry_box=a.split(",");
    $("#profit_agentid").text(arry_box[0]);
    $("#profit_settlementTime").text(arry_box[1]);
    $("#profit_code").text(arry_box[2]);
}

$("#agent_profit_btn").on('click',function(){
   var  sub_profit_agentid= $("#profit_agentid").text();
    var  sub_profit_settlementTime= $("#profit_settlementTime").text();
    var  sub_profit_code= $("#profit_code").text();
    var  profit_data=[{
        "agentId":sub_profit_agentid,
        "settlementTime":sub_profit_settlementTime,
        "code":sub_profit_code
    }]
    $.ajax({
        url: "/agentprofit/statistics/confirm",
        data:JSON.stringify(profit_data),
        type:'POST',
        datatype:'json',
        success: function (res) {
            $("#agernt_profit_modal").modal("hide");
            UTIL.clickDisappearSwal("分润成功");
            $("#profitTable").bootstrapTable("refresh");
        },
        error: function (res) {
            UTIL.clickDisappearSwal("分润系统出错，请联系管理员");
        }
    });
})
//存放数据
var agent_profit_time_box=[];
Array.prototype.remove = function(val) {
    var index = this.indexOf(val);
    if (index > -1) {
        this.splice(index, 1);
    }
};
Array.prototype.remove=function(dx)
{
    if(isNaN(dx)||dx>this.length){return false;}
    for(var i=0,n=0;i<this.length;i++)
    {
        if(this[i]!=this[dx])
        {
            this[n++]=this[i]
        }
    }
    this.length-=1
}
// 批量分润时提交的数据
$("#agent_profit_more").on('click',function () {
    console.log(agent_profit_time_box)
    if (agent_profit_time_box.length<1){
        UTIL.clickDisappearSwal("请选择代理商");
    }else{
        $("#agernt_profit_modal_ids").modal("show");
    }
})
$("#agent_profit_btn_ids").on('click',function(){
    var  profit_data=agent_profit_time_box;
    $.ajax({
        url: "/agentprofit/statistics/confirm",
        data:JSON.stringify(profit_data),
        type:'POST',
        datatype:'json',
        success: function (res) {
            $("#profitTable").bootstrapTable('selectPage',1);
            $("#agernt_profit_modal_ids").modal("hide");
            UTIL.clickDisappearSwal("批量分润成功");
            agent_profit_time_box=[];
        },
        error: function (res) {
            UTIL.clickDisappearSwal("批量分润成功出错，请联系管理员");
        }
    });
})
$(function(){
    //初始化
    $('[data-toggle="table"]').bootstrapTable(
        {
            checkboxHeader:false,
            detailView: true,
            //无线循环取子表，直到子表里面没有记录
            onExpandRow: function (index, row, $Subdetail) {
                InitSubTable(index, row, $Subdetail);
            },
            onLoadSuccess:function (data) {
                $('[data-toggle="table"]').removeClass('table-hover')
                $.each(data.rows,function (i,v) {
                    if(v.settlementStatus==1){
                        $("table").children('tbody').children('tr').eq(i).children('td').eq(1).children('input').css('display','none');
                    }
                })
               var btns= $('button.disabled')
                $.each(btns,function (i,v) {
                   $(v).parent().siblings().eq(1).children('input').css('display','none');
                })
            },
            onCheck:function (row) {
                console.log(row);
                var json={};
                json.agentId=row.agentId;
                json.settlementTime=row.settlementTime;
                json.code=row.code;
                agent_profit_time_box.push(json);
            },
            onUncheck:function (row) {
                console.log(agent_profit_time_box)
                if (agent_profit_time_box.length>0){
                    $.each(agent_profit_time_box,function (i,v) {
                        if (row.code==v.code&&row.agentId==v.agentId&&row.settlementTime==v.settlementTime){
                             agent_profit_time_box.remove(i);
                        }
                    })
                }
            }
        });
});
    //初始化子表格(无线循环)
    InitSubTable = function (index, row, $detail) {
        $($detail).css('padding','0');
        var agentId = row.agentId;
        var settlementTime = row.settlementTime;
        var cur_table = $detail.html("<table id='pow"+index+"'></table>").find('table');
        $(cur_table).bootstrapTable({
            url: '/agentprofit/statistics/query',
            method: 'post',
            checkboxHeader:false,
            queryParams:{ agentId: agentId ,settlementTime:settlementTime},
            ajaxOptions:{ agentId: agentId ,settlementTime:settlementTime},
            clickToSelect: true,
            detailView: true,//父子表
            uniqueId: "MENU_ID",
            pageSize: 10,
            pageList: [10, 25],
            columns: [{
                checkbox: true
            }, {
                field: 'settlementTime',
                title: '清算日期'
            }, {
                field: 'agentId',
                title: '代理商编号',
                formatter:function(value,row,index){
//此处对value值做判断，不然value为空就会报错
                    value=value?value:'';
                    var length=value.length;
                    if(length&&length>7){
                        length=7;
                    }
                    return"<div style='width: 150px;' title ='"+value+"' >"+value+"</div>"
                }
            },
                {
                field: 'parentAgentId',
                title: '父代理商编号',
                    formatter:function(value,row,index){
//此处对value值做判断，不然value为空就会报错
                        value=value?value:'';
                        var length=value.length;
                        if(length&&length>7){
                            length=7;
                        }
                        return"<div style='width: 150px;' title ='"+value+"' >"+value+"</div>"
                    }
            }, {
                field: 'transCount',
                title: '交易笔数'
            },
                {
                    field: 'transAmt',
                    title: '交易金额（元）'
                },
                {
                    field: 'profitTotal',
                    title: '总利润（元）'
                },
                {
                    field: 'profit',
                    title: '本级分润（元）'
                },
                {
                    field: 'profitOut',
                    title: '本级已发利润（元）'
                },
                {
                    field: 'profitSubOut',
                    title: '下级已发利润（元）'
                },
                {field:"",title:"",align:"center",formatter:function(value,row,index){
                if (row.settlementStatus==1){
                    return [
                        '<button class="btn btn-success disabled" onclick="modal_show(\''+ row.agentId+","+ row.settlementTime+","+ row.code+  '\')" title="分润">',
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

                },edit:false},
            ],
            onLoadSuccess:function (data) {
                 lg2=  $('table').bootstrapTable('getSelections');
                $(cur_table).removeClass('table-hover')
                if (data.length<1){
                    $(cur_table).css('background-color', '#b2e5ed')
                }else {
                    var data_code = (data[0].code).split('-').length;
                    switch (data_code) {

                        case 1:

                            $(cur_table).css('background-color', '#4fa0de')

                            break;

                        case 2:
                            $(cur_table).css('background-color', '#4fa0de')
                            break;

                        case 3:
                            $(cur_table).css('background-color', '#67b3de')

                            break;
                        case 4:
                            $(cur_table).css('background-color', '#82c5e4')

                            break;
                        case 5:
                            $(cur_table).css('background-color', '#b2e5ed')

                            break;

                    }
                    var btns = $('button.disabled')
                    $.each(btns, function (i, v) {
                        $(v).parent().siblings().eq(1).children('input').css('display', 'none');
                    })
                }
            },
            //无线循环取子表，直到子表里面没有记录
            onExpandRow: function (index, row, $Subdetail) {
               InitSubTable(index, row, $Subdetail);
            },
            onCheck:function (row) {
                var json={};
                json.agentId=row.agentId;
                json.settlementTime=row.settlementTime;
                json.code=row.code
                agent_profit_time_box.push(json);
            },
            onUncheck:function () {
                console.log(agent_profit_time_box)
                if (agent_profit_time_box.length>0){
                    $.each(agent_profit_time_box,function (i,v) {
                        if (row.code==v.code&&row.agentId==v.agentId&&row.settlementTime==v.settlementTime){
                            agent_profit_time_box.remove(i);
                        }
                    })
                }
            }
        });
    };

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
    var agentName=null;
    var settlementTimeStart=null;
    var settlementTimeEnd=null;
    var flag=0;
    var new_profit_data={
        'agentId':agentId,
        'agentName':agentName,
        'flag':flag,
        'settlementTimeStart':settlementTimeStart,
        'settlementTimeEnd':settlementTimeEnd
    }
    function get_profits_info(profit_data) {
        $.ajax({
            url: "/agentprofit/statistics/querystatisticssum",
            data:JSON.stringify(profit_data),
            type:'POST',
            datatype:'json',
            success: function (res) {
                $("#profitTotalAmount").text(res.profitTotalAmount);
                $("#transCount").text(res.transCount);
                $("#profitRetainedAmount").text(res.profitRetainedAmount);

            },
            error: function (res) {
                UTIL.clickDisappearSwal("利润详情系统出错，请联系管理员");
            }
        });
    }
    get_profits_info(new_profit_data)

    //查询按钮事件
    $("#agent_profit_search").on('click',function(){
        agentId=$("#agent_profit_static_form input[name=agentId]").val()
        agentName=$("#agent_profit_static_form input[name=agentName]").val()
        flag=$("#agent_profit_static_form select[name=flag]").val()
        settlementTimeStart=$("#agent_profit_static_form input[name=settlementTimeStart]").val()
        settlementTimeEnd=$("#agent_profit_static_form input[name=settlementTimeEnd]").val()
        var profit_data={
            'agentId':agentId,
            'agentName':agentName,
            'flag':flag,
            'settlementTimeStart':settlementTimeStart,
            'settlementTimeEnd':settlementTimeEnd
        }
        get_profits_info(profit_data);
        if(profitTable.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var zddkTableRefreshUrl = $("#profitTable").data("url") + "?" + randomNum;
            $("#profitTable").bootstrapTable("refresh", {
                url: zddkTableRefreshUrl
            });
        }
        else{
            $("#profitTable").bootstrapTable('selectPage',1);
        }


    });
    //重置查询条件
    $("#agent_profit_remove").on('click',function(){
        document.getElementById("agent_profit_static_form").reset();
        $("#profitTable").bootstrapTable('selectPage',1);
    });

    //代理商查询列表下载
    $("#agent_profit_static_down").click(function(){
        var agentId=$("input[name=agentId]").val()
        var agentName=$("input[name=agentName]").val()
        var flag=$("select[name=flag]").val()
        var settlementTimeStart=$("input[name=settlementTimeStart]").val()
        var settlementTimeEnd=$("input[name=settlementTimeEnd]").val()
        var selectAgent=$("#profitTable").bootstrapTable("getSelections");
        if(selectAgent.length<1){
            var data = $("#profitTable").bootstrapTable("getData");
            if(data.length > 0){
                window.location="/agentprofit/exportstatistics?agentId="+agentId
                    +"&agentName="+ agentName+"&settlementTimeEnd="+ settlementTimeEnd+"&settlementTimeStart="+ settlementTimeStart+"&flag="+ flag
            }
            else{
                UTIL.clickDisappearSwal("暂无数据下载!");
            }
        }else{
            var ids = [];
            for (var i = 0; i < selectAgent.length; i++) {
                ids.push(selectAgent[i].id);
            }
            window.location="/agentprofit/exportstatistics?agentId="+agentId
                +"&agentName="+ agentName+"&settlementTimeEnd="+ settlementTimeEnd+"&settlementTimeStart="+ settlementTimeStart+"&flag="+ flag
            // window.location="/agentprofit/exportstatistics?ids="+ids;

        }

    })


