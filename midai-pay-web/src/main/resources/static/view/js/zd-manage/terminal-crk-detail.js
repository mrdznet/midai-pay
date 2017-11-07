
/**
 * Created by kk on 2016/9/23.
 * 出入库明细查询 js
 */
$('[data-toggle="table"]').bootstrapTable();
//添加搜索条件
function detailsearchTable(args) {
    args.createTimeBegin = $("#detailTimeBegin").val(); //
    args.createTimeEnd = $("#detailTimeEnd").val();
    args.deviceNoBegin = $("#detailNoBegin").val();
    args.deviceNoEnd = $("#detailNoEnd").val();
    return args;
}

/*//终端出入库明细状态字段显示
function stateVisible(index,row){
    var stateVisibleValue = row.operateState ;
         if(stateVisibleValue==0){
             return "入库"
         }else if(stateVisibleValue==1){
             return "出库"
         }else if(stateVisibleValue==2){
             return "变更"
         }
}*/

$(function(){
	var  crkcxTableData = $("#detailListTable").bootstrapTable("getData");
    /*  时间插件的初始化               */
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });

    //点击刷新搜索table
    $("#detailSearch").click(function (e) {
        e.preventDefault();
        
        if(crkcxTableData.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var crkcxTableRefreshUrl = $("#detailListTable").data("url") + "?" + randomNum;
            $("#detailListTable").bootstrapTable("refresh", {
                url: crkcxTableRefreshUrl
            });
            crkcxTableData = $("#detailListTable").bootstrapTable("getData");
        }
   	 else{
   		 $("#detailListTable").bootstrapTable("selectPage", 1);
   	 }
        
    });

    //清空搜索条件
    $("#detailEmpty").click(function (e) {
        e.preventDefault();
        document.getElementById("crkSearchList").reset();
        $("#detailListTable").bootstrapTable("selectPage", 1)
    });

    //终端设备列表查询列表下载
    $("#detailDownload").click(function () {
    	  var  selectData = $("#detailListTable").bootstrapTable("getData");
    	  var detailNoBegin = $("#detailNoBegin").val();
          var detailNoEnd = $("#detailNoEnd").val();
          var detailTimeBegin = $("#detailTimeBegin").val();
          var detailTimeEnd = $("#detailTimeEnd").val();
          if (selectData.length > 0) {
              window.location = "../device/inout/excelExport?createTimeBegin=" + detailTimeBegin
                  +"&createTimeEnd="+detailTimeEnd
                  +"&deviceNoBegin=" +detailNoBegin
                  +"&deviceNoEnd=" +detailNoEnd
          }
          else {
              UTIL.clickDisappearSwal("暂无数据下载！")
          }
    });

});
