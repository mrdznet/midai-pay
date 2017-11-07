/**
 * Created by yangkaikai on 2016/9/23.
 */

$('[data-toggle="table"]').bootstrapTable();

//添加搜索条件
function searchTable(args) {
    args.deviceNoStart = $("#equipmentNoBegin").val();
    args.deviceNoEnd = $("#equipmentNoEnd").val();
    args.customerId = $("#shopNo").val();
    args.mobile = $("#sbcxMobile").val();
    return args;
}

//终端历史绑定商户
function historyMerchant(index,row){
    var historyTemp = $("#historyBdingMerchant").html().replace("pageurl","terminal-history-bdsh.html")
        .replace("row.deviceNo",row.deviceNo)
        .replace("str",row.historyCustCount);
        if(row.historyCustCount === 0){
           historyTemp = 0;
        }
    return historyTemp;
}


$(function () {
   var zdcsTableData = $("#zdSbListTable").bootstrapTable("getData");
   console.log(zdcsTableData)
    //点击刷新搜索table
    $("#zdSbSearch").click(function (e) {
        e.preventDefault();
        
        if(zdcsTableData.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var zdcsTableRefreshUrl = $("#zdSbListTable").data("url") + "?" + randomNum;
            $("#zdSbListTable").bootstrapTable("refresh", {
                url: zdcsTableRefreshUrl
            });
            zdcsTableData = $("#zdSbListTable").bootstrapTable("getData");
        }
   	 else{
   		 $("#zdSbListTable").bootstrapTable("selectPage", 1);
   	 }
    });

    //清空搜索条件
    $("#emptySearch").click(function (e) {
        e.preventDefault();
        document.getElementById("zdsbSearchList").reset();
        $("#zdSbListTable").bootstrapTable("selectPage", 1);
    });


    //终端设备列表查询列表下载
   $("#zdListDown").click(function () {
	 var selectData =   $("#zdSbListTable").bootstrapTable("getData");
       var equipmentNoBegin = $("#equipmentNoBegin").val();
       var equipmentNoEnd = $("#equipmentNoEnd").val();
       var shopNo = $("#shopNo").val();
       if (selectData.length > 0) {
           window.location = "../device/excelExport?deviceNoStart=" +equipmentNoBegin
               +"&deviceNoEnd=" +equipmentNoEnd
               +"&customerId=" + shopNo;
       }
       else {
           var text = "暂无数据下载！";
           UTIL.clickDisappearSwal(text);
       }
    });

    //终端设备  出入库，变更跳转页面
    //终端变更
    $("#zdChange").click(function () {
        UTIL.openNewPage({
            targetUrl:"terminal-sbbg.html"
        });
        //bootstrap
        $('[data-toggle="table"]')
            .bootstrapTable();
    });
    //终端出库
    $("#zdOutput").click(function () {
        UTIL.openNewPage({
            targetUrl:"terminal-sbck.html"
        });
    	 //bootstrap
        $('[data-toggle="table"]')
            .bootstrapTable();
    });
    //终端入库
    $("#zdInput").click(function () {
        UTIL.openNewPage({
            targetUrl:"terminal-sbrk.html"
        });
    	 $('[data-toggle="table"]')
         .bootstrapTable();
    })
});