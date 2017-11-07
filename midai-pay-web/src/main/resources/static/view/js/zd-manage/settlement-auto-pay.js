//自动打款搜索参数设置
$zddkForm = $("#zddkForm");
var zddkArr = [];
var zddkMercId = [];
var idkPath = "";
function zddkSearch(args){
    var jsonData = UTIL.form2json({
        'form' : "#zddkForm"
    });
    for(var value in jsonData){
        args[value] = jsonData[value];
    }
    return args;
}

function FormatDisabledPayAuto(value,row, index){
    if(row.payState === 4){
         return {
            disabled: true
         }
    }
     return {
            disabled: false
         }
}

//自动打款样式
function zddkHandle(value,row,index){
    if(row.payState === 4){
        return "";
    }
    var str = "自动打款";
    var temp = $("#setAutoPayTempl").html().replace("row.tixiLogno", row.tixiLogno).replace("row.mercId",row.mercId);
    return temp;
}

function zddkEvent(temp1,temp2){
    zddkArr = [];
    zddkMercId = [];
    if($("#dkPath").val() == ""){
        UTIL.clickDisappearSwal("请先选择打款通道");
    }else{   
        idkPath = $("#dkPath").val();
        zddkArr.push(temp1);
        $(".zddkCont").html(temp2);
        $("#qrdkModal").modal('show');
        $("#qrdkBtn").prop("disabled",false);
    }
}
function changeSettltmentPayStyle(row , index){
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
function zddkNote(value, row, index){
    if(row.payState === 4){
        return "重新打款";
    }
    return "";
}

$(function(){
	//初始化
	$('[data-toggle="table"]').bootstrapTable();
    var zddkTableData = $("#zddkTable").bootstrapTable("getData");

    //时间插件的初始化              
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    }); 

	//查询按钮事件
	$("#zddkSearchBtn").on('click',function(){
        if(zddkTableData.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var zddkTableRefreshUrl = $("#zddkTable").data("url") + "?" + randomNum;
            $("#zddkTable").bootstrapTable("refresh", {
                url: zddkTableRefreshUrl
            });
            zddkTableData = $("#zddkTable").bootstrapTable("getData");
        }
        else{
            $("#zddkTable").bootstrapTable('selectPage',1);
        }
		
	});
	//重置查询条件
	$("#zddkEmptyBtn").on('click',function(){
		document.getElementById("zddkForm").reset();
		$("#zddkTable").bootstrapTable('selectPage',1);
	});

	//批量自动打款

	$("#allZddkBtn").on('click',function(){
        zddkArr = [];
        zddkMercId = [];
		var getZddkArr = $("#zddkTable").bootstrapTable("getSelections");
		if(getZddkArr.length<1){
            UTIL.clickDisappearSwal("请先选择交易");
		}else{
            if($("#dkPath").val() == ""){
                UTIL.clickDisappearSwal("请先选择打款通道");
            }else{
                $("#qrdkModal").modal('show');
                $("#qrdkBtn").prop("disabled",false);
                idkPath = $("#dkPath").val();
                for (var i = 0; i < getZddkArr.length; i++) {
                    zddkArr.push(getZddkArr[i].tixiLogno);
                    zddkMercId.push(getZddkArr[i].mercId)
                }
                $(".zddkCont").html(zddkMercId.toString());
            }
			
		}
		
	});

    //自动打款按钮
    
    // $("#zddkTable").on("load-success.bs.table",function(){
    //     $(".zddkBtn").on('click',function(){
    //         var getZddkOne = $("#zddkTable").bootstrapTable("getSelections");
    //         if($("#dkPath").val() == ""){
    //                 swal("请先选择打款通道");
    //             }else{   
    //                 zddkArr.push(getZddkArr[0].tixiLogno);
    //                 $(".passQsCont").html(zddkArr.toString());
    //                 $("#qrdkModal").model('show');
    //             }
    //     });
    // })

	//通过确认按钮
	$("#qrdkBtn").click(function () {
        $(this).prop("disabled",true);
        $.ajax({
            url: "../autopay/pay/"+zddkArr.toString()+"/"+idkPath,
            success: function (res) {
                $("#qrdkModal").modal("hide");
                if (res == 1) {
                    UTIL.clickDisappearSwal("成功自动打款!",function(){
                        var randomNum = parseInt(Math.random()*99999);
                        var zddkTableRefreshUrl = $("#zddkTable").data("url") + "?" + randomNum;
                        $("#zddkTable").bootstrapTable("refresh", {
                            url: zddkTableRefreshUrl
                        });
                    });
                } else {
                    UTIL.clickDisappearSwal("打款失败，请重新操作!",function(){
                        $("#qrdkModal").modal("hide");
                    });

                }
            }
        });
    });



})