//通道列表搜索参数设置
$instForm = $("#instForm");

function instStatus(value,row,index){
    if(value == 0){
        return "启动";
    }else if(value == 1){
        return "暂停";
    }
}
function instEdit(value,row,index){
    var str = "编辑";
    var temp = $("#instTempl").html().replace("row.instCode", row.instCode);
    return temp;
}

function zddkEvent(temp1){
	if ($("#instTable")) {
        $("#instTable").bootstrapTable("selectPage", 1);
    }
	UTIL.openNewPage({
        "targetUrl" : "Institution-deploy.html",
         "data":"instCode="+temp1
    })
}

function instSearch(args){

    var formJson = UTIL.form2json({
        "form" : "#instForm"
    });
    for(var value in formJson){
        args[value] = formJson[value];
    }

    return args;
}
$(function(){
	//初始化
	$('[data-toggle="table"]').bootstrapTable();
    var instTableData = $("#instTable").bootstrapTable("getData");

	//查询按钮事件
    $("#instSearchBtn").click(function(e) {
        e.preventDefault();
        if(instTableData.length == 0){
            var randomNum = parseInt(Math.random()*99999);
            var instTableRefreshUrl = $("#instTable").data("url") + "?" + randomNum;
            $("#instTable").bootstrapTable('refresh',{
                url :　instTableRefreshUrl
            });
            instTableData = $("#instTable").bootstrapTable("getData");
        }
        else{
            $("#instTable").bootstrapTable('selectPage',1);
        }
	});

	//重置查询条件
	 $("#instEmptyBtn").click(function(e) {
        e.preventDefault();
		document.getElementById("instForm").reset();
		if(instTableData.length == 0){
            var randomNum = parseInt(Math.random()*99999);
            var instTableRefreshUrl = $("#instTable").data("url") + "?" + randomNum;
            $("#instTable").bootstrapTable('refresh',{
                url :　instTableRefreshUrl
            });
            instTableData = $("#instTable").bootstrapTable("getData");
        }
        else{
            $("#instTable").bootstrapTable('selectPage',1);
        }
	});
	
	$("#instStartBtn").on('click',function(e){
        e.preventDefault();
        var instArray = [];
		var inst_list = $("#instTable")
				.bootstrapTable("getSelections");

		for ( var i in inst_list) {
			instArray.push(inst_list[i].instCode);
		}
        $.ajax({
			type : "post",
			dataType : "json",
			contentType : "application/json",
			url : "/inst/batchUpdate/0.json",
			data : JSON.stringify(instArray),
			success : function(res) {
				if (res) {
					$("#instTable")
							.bootstrapTable("selectPage", 1);
					UTIL.autoDisappearSwal("修改成功！", function() {
						$('#instTable').bootstrapTable(
								'refresh', {
									silent : true
								});
					});
				} else {
					UTIL.clickDisappearSwal("修改失败！");
				}

			}
		});
		
	});
	$("#instStopBtn").on('click',function(e){
        e.preventDefault();
        var instArray = [];
		var inst_list = $("#instTable")
				.bootstrapTable("getSelections");

		for ( var i in inst_list) {
			instArray.push(inst_list[i].instCode);
		}
        $.ajax({
			type : "post",
			dataType : "json",
			contentType : "application/json",
			url : "/inst/batchUpdate/1.json",
			data : JSON.stringify(instArray),
			success : function(res) {
				if (res) {
					$("#instTable")
							.bootstrapTable("selectPage", 1);
					UTIL.autoDisappearSwal("修改成功！", function() {
						$('#instTable').bootstrapTable(
								'refresh', {
									silent : true
								});
					});
				} else {
					UTIL.clickDisappearSwal("修改失败！");
				}

			}
		});
		
	});


})