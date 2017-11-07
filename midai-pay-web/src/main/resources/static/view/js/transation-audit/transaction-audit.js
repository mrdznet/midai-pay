
/**
 * Created by  on 2016/10/13.
 */
//交易审核

$('[data-toggle="table"]').bootstrapTable();
function jyshQueryOptions(argms){
    var formJson = UTIL.form2json({
        "form" : "#jyshForm"
    });
    for(var value in formJson){
        argms[value] = formJson[value];
    }
    return argms;
}
function jyshAjaxFnc(params){
    $.ajax({
        url:"../trade/review/list",
        data:params.data,
        success: function(res){
            if($("#factoryStaus").val() === ""){
                res.rows = [];
                res.total = 0;
                params.success(res);
                return ;
            }
            var rowsChangeData = [];
            var index = 0;
            rowsChangeData.length = Math.ceil(res.rows.length/4);
            for(var i = 0; i < rowsChangeData.length; i++){
                rowsChangeData[i] = {};
            }
            for(var i = 0; i < res.rows.length; i++){
                index = parseInt(i/4);
                rowsChangeData[index]["list" + ((i)%4)] = res.rows[i];
            }
            res.rows = rowsChangeData;
            res.total = Math.ceil(res.total);
            params.success(res);
        }
    })
     // params.success({
     //    rows : [{"list1" : "","list2" : "", "list3" : "" ,"list4" : ""},{"list1" : "","list2" : "", "list3" : "" ,"list4" : ""}]
     // });
}
function list0DataFormat(value, row, index){
    if(row.list0 == undefined){
         return '<div class="emptyTicketImgBox"></div>';
    }
    var str = $("#ticketTemplate").html();
    str = checkTranAuditStatus(row.list0.state, str);
    var data = row.list0;
    var imgSize = "@550h_310w_1e";
    // var imgSize = "";
    str = str.replace(/\{\{mercId\}\}/g,data.mercId).replace(/\{\{mercName\}\}/g,data.mercName).replace(/\{\{money\}\}/g,UTIL.outputdollars(data.money)).replace(/\{\{hostTransSsn\}\}/g,data.hostTransSsn).replace(/\{\{state\}\}/g,data.state).replace(/\{\{cardKind\}\}/g,data.cardKind).replace(/\{\{bigImgUrl\}\}/g,data.bigImgUrl).replace(/\{\{smallImgUrl\}\}/g,data.smallImgUrl + imgSize);

    return str;
}
function list1DataFormat(value, row, index){
    if(row.list1 == undefined){
         return '<div class="emptyTicketImgBox"></div>';
    }
    var str = $("#ticketTemplate").html();
    str = checkTranAuditStatus(row.list1.state, str);
    var data = row.list1;
    var imgSize = "@550h_310w_1e";
    str = str.replace(/\{\{mercId\}\}/g,data.mercId).replace(/\{\{mercName\}\}/g,data.mercName).replace(/\{\{money\}\}/g,UTIL.outputdollars(data.money)).replace(/\{\{hostTransSsn\}\}/g,data.hostTransSsn).replace(/\{\{state\}\}/g,data.state).replace(/\{\{cardKind\}\}/g,data.cardKind).replace(/\{\{bigImgUrl\}\}/g,data.bigImgUrl).replace(/\{\{smallImgUrl\}\}/g,data.smallImgUrl + imgSize);
    return str;
}
function list2DataFormat(value, row, index){
    if(row.list2 == undefined){
        return '<div class="emptyTicketImgBox"></div>';
    }
    var str = $("#ticketTemplate").html();
    str = checkTranAuditStatus(row.list2.state, str);
    var data = row.list2;
    var imgSize = "@550h_310w_1e";
    str = str.replace(/\{\{mercId\}\}/g,data.mercId).replace(/\{\{mercName\}\}/g,data.mercName).replace(/\{\{money\}\}/g,UTIL.outputdollars(data.money)).replace(/\{\{hostTransSsn\}\}/g,data.hostTransSsn).replace(/\{\{state\}\}/g,data.state).replace(/\{\{cardKind\}\}/g,data.cardKind).replace(/\{\{bigImgUrl\}\}/g,data.bigImgUrl).replace(/\{\{smallImgUrl\}\}/g,data.smallImgUrl + imgSize);
    return str;
}
function list3DataFormat(value, row, index){
    var str = $("#ticketTemplate").html();
    if(row.list3 == undefined){
         return '<div class="emptyTicketImgBox"></div>';
    }
    var data = row.list3;
    var imgSize = "@550h_310w_1e";
    str = checkTranAuditStatus(row.list3.state, str);
    str = str.replace(/\{\{mercId\}\}/g,data.mercId).replace(/\{\{mercName\}\}/g,data.mercName).replace(/\{\{money\}\}/g,UTIL.outputdollars(data.money)).replace(/\{\{hostTransSsn\}\}/g,data.hostTransSsn).replace(/\{\{state\}\}/g,data.state).replace(/\{\{cardKind\}\}/g,data.cardKind).replace(/\{\{bigImgUrl\}\}/g,data.bigImgUrl).replace(/\{\{smallImgUrl\}\}/g,data.smallImgUrl + imgSize);
    return str;
}
function ticketAdopt(hostTransSsn){
   
    swal({
        title : "<h5 style='font-size:20px;'>提示</h5>",
        text : "您确定流水号为" + hostTransSsn + "的交易通过审核吗？",
        showCancelButton: true,
        confirmButtonText:"确定",
        cancelButtonText: "关闭",
        showConfirmButton : true,
        closeOnConfirm : false
    }, function(){
         $.ajax({
            url: "../trade/review/through",
            data:hostTransSsn,
            dataType:"text",
            success: function(res){
                if(res === "1"){
                    UTIL.autoDisappearSwal("通过成功", function(){
                        $("#jyshTable").bootstrapTable("selectPage", 1);
                    })
                }
            }
        })
    })
}
// 修改拒绝模态框中交易流水中的值
function ticketRefuse(hostTransSsn){
    $("#jyshRejectModalForm input[name='hostTransSsn']").val(hostTransSsn);
    $("#jyshRejectModal").modal("show");
}
// 出现大图模态框
function ticketImgInfo(a){
    $("#ticketImgModal").find("img").attr("src",a);
    $("#ticketImgModal").modal("show");
}
// 单个拒绝方法
function ticketRefuseRequesh(data){
        $.ajax({
        url: "../trade/review/unthrough",
        data:data,
        dataType:"text",
        success: function(res){
            if(res === "1"){
                UTIL.autoDisappearSwal("拒绝成功", function(){
                    $("#jyshTable").bootstrapTable("selectPage", 1);
                })
            }
        }
    })

     
}
// 判断当前属性是通过拒绝还是待审
function checkTranAuditStatus(status , str){
    if(status == "通过"){
        str = str.replace("ticketAdopt('{{hostTransSsn}}')", "void(0);").replace("btn btn-default", "btn btn-default disabled")
        .replace("ticketRefuse('{{hostTransSsn}}')", "void(0);").replace("btn btn-success", "btn btn-success disabled");
        return str;
    }
    else if(status == "拒绝"){
        str = str.replace("ticketRefuse('{{hostTransSsn}}')", "void(0);").replace("btn btn-default", "btn btn-default disabled");
        return str;
    }
    else{
        return str;
    }
}
$(function () {

    /*  时间插件的初始化               */
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });

    $("#tranSettementReject").click(function(){
        $("#transSettlemrnytModal").modal();
    });
    (function(){
        var $toolbarForm = $("#jyshForm");
        var $jyshTable = $("#jyshTable");
        var $jyshTableData =  $jyshTable.bootstrapTable("getData");
        // 查询
        $("#jyshCheckBtn").on("click", function(e){
            e.preventDefault();
            
            if($jyshTableData.length === 0 ){
                var randomNum = parseInt(Math.random()*99999);
                var jyshTableRefreshUrl = $jyshTable.data("url") + "?" + randomNum;
                $jyshTable.bootstrapTable("refresh", {
                    url:jyshTableRefreshUrl
                });
                $jyshTableData =  $jyshTable.bootstrapTable("getData");
            }
       	 else{
       		 $jyshTable.bootstrapTable("selectPage", 1);
       	 }
            $("#jysh-check-all").prop("checked", false);
            // 如果查询的是所有通过的状态，
            if($toolbarForm.find("select[name='state']").val() == 1){
                $("#tranSettementAccess").prop("disabled", true);
                $("#tranSettementReject").prop("disabled", false);
            }
            else if($toolbarForm.find("select[name='state']").val() == 2){
                $("#tranSettementReject").prop("disabled", true);
                $("#tranSettementAccess").prop("disabled", false);
            }
            else{
                $("#tranSettementReject").prop("disabled", false);
                $("#tranSettementAccess").prop("disabled", false);
            }
        })

        // function ff(){
        //     $("#factoryStaus").val("0");
        //     $jyshTable.bootstrapTable("selectPage", 1);
        //     $jyshTable.bootstrapTable("refresh");
        // }
        // ff();

        // 清空查询条件
       $("#jyshEmptySearch").on("click", function(e){
            e.preventDefault();
            document.getElementById("jyshForm").reset();
            $("#factoryStaus").val("0");
            $jyshTable.bootstrapTable("selectPage", 1);
            $("#tranSettementReject").prop("disabled", false);
            $("#tranSettementAccess").prop("disabled", false);
       });
       // 下载按钮点击事件
       $("#jyshDownloadBtn").on("click", function(e){
            e.preventDefault();
       });
       // 拒绝交易审核
       $("#jyshRejectBtn").click(function(e){
            e.preventDefault();
            $("#jyshRejectModal").modal("hide");
            var hostTransSsnStr = hostTransSsnStr;
            var data = JSON.stringify(UTIL.form2json({
                "form" : "#jyshRejectModalForm"
            }));
            ticketRefuseRequesh(data);
       })
       // 批量通过
       $("#tranSettementAccess").on("click", function(){
            var hostTransSsnStr = "";
            var selectDataArr = $jyshTable.find(".ticketImgBox");
            selectDataArr.each(function(index, target){
                var $checkFlag = $(this).find("input[type='checkbox']");
                if($checkFlag.prop("checked")){
                    if(hostTransSsnStr === ""){
                        hostTransSsnStr = $checkFlag.attr("data-hostTransSsn");
                    }
                    else{
                         hostTransSsnStr += "," + $checkFlag.attr("data-hostTransSsn");
                    }
                }
            });
            if(hostTransSsnStr == ""){
                UTIL.clickDisappearSwal("请先选要通过的交易");
            }
            else{
                ticketAdopt(hostTransSsnStr);
            }
       })
       // 批量拒绝
       $("#tranSettementReject").click(function(){
            var hostTransSsnStr = "";
            var selectDataArr = $jyshTable.find(".ticketImgBox");
           selectDataArr.each(function(index, target){
                var $checkFlag = $(this).find("input[type='checkbox']");
                if($checkFlag.prop("checked")){
                    if(hostTransSsnStr === ""){
                        hostTransSsnStr = $checkFlag.attr("data-hostTransSsn");
                    }
                    else{
                         hostTransSsnStr += "," + $checkFlag.attr("data-hostTransSsn");
                    }
                }
            });
            if(hostTransSsnStr == ""){
                UTIL.clickDisappearSwal("请先选要拒绝的交易");
            }
            else{
                ticketRefuse(hostTransSsnStr);
            }
       })

       // 拒绝模态框隐藏时里面的内容重置
       $("#jyshRejectModal").on("show.bs.modal", function(){
            document.getElementById("jyshRejectModalForm").reset();
       });
        // 全选
        $("#jysh-check-all").on("click", function(){
            if($(this).prop("checked")){
                $(".yjsh-check-item").prop("checked", true);
            }
            else{
                $(".yjsh-check-item").prop("checked", false);
            }
        })
    })();
});