// 初始化table
function sbbgAgentSourceQuery(argms){
    argms.agentNo  = $("#sbbg-agent-source-modal input[name='agentNo']").val();
    argms.name = $("#sbbg-agent-source-modal input[name='name']").val();
    return argms;
}
function sbbgAgentMarkQuery(argms){
    argms.agentNo  = $("#sbbg-agent-mark-query input[name='agentNo']").val();
    argms.name = $("#sbbg-agent-mark-query input[name='name']").val();
    argms.agentNor = $("#sbbg-option-form input[name='agentId']").val();
    return argms; 
}
function sbbgAddMrkQuery(argms){
	argms.agentNo = $("#sbbg-option-form input[name='agentId']").val();
	argms.bodyNoStart = $("#sbbg-add-mark-query input[name='bodyNoStart']").val();
	argms.num = $("#sbbg-add-mark-query input[name='num']").val();
	return argms;
}
function sbbgTestTime(row,index){
	var time = new Date();
	function init2num(n){
		if(n<10){
			return "0" + n;
		}
		return "" + n;
	}
	var fullTime = time.getFullYear() + "-" + init2num(time.getMonth()+1) + "-" + init2num(time.getDate()) + " " + init2num(time.getHours()) + ":" + init2num(time.getMinutes()) + ":" + init2num(time.getSeconds());
	return fullTime;
}
$(function(){
	var $sbbgAgentSourceModal = $("#sbbg-agent-source-modal");
	var $sbbgAgentMarkModal = $("#sbbg-agent-mark-modal");
	var $sbbgAgentSourceTable = $("#sbbg-agent-source-table");
	var $sbbgAgentMarkTable = $("#sbbg-agent-mark-table");
	var $sbbgForm = $("#sbbg-option-form");
	var $sbbgTable = $("#sbbg-table");
	var $sbbgAddMarkModal = $("#sbbg-plus-modal");
	var $sbbgAddMarkTable = $("#sbbg-plus-table");
	var sbbgTableData = [];
	var sbbgTableOption = {};
	var sbbgSourceId = "";
	var sbbgMarkId = "";
	var tableAddedData = [];
	var currentTableData = [];
	// 批次号
	$.ajax({
	    url:"../device/receiptno",
	    type:"get",
	    data:{
	        "receiptType" : 3
	    },
	    success:function(res){
	        $("#terminal-sbbg .batchNo").text(res.batchNo);
	         $("#terminal-sbbg input[name='ckNo']").val(res.batchNo);
	         $('[data-toggle="table"]').bootstrapTable();
	    }
	})
	
	$("#sbbg-source-agent-icon").on("click",function(){
		$("#sbbg-agent-source-modal").modal("show");
	})

	// 追加记录
	$("#sbbg-add-mark-btn").click(function(){
		if($sbbgForm.find("input[name='agentId']").val() == ""){
   			UTIL.clickDisappearSwal("请先选择源代理商编号");
		}
		else{
			if($sbbgForm.find("input[name='destagentId']").val() == ""){
  				 UTIL.clickDisappearSwal("请先选择目标代理商编号");
			}
			else{
				document.getElementById("sbbg-add-mark-query").reset();
				$sbbgAddMarkModal.find("input[name='agentNo']").val($sbbgForm.find("input[name='agentId']").val());
				 $sbbgAddMarkTable.bootstrapTable("selectPage", 1);
				$sbbgAddMarkModal.modal("show");
			}	 
		}	
	});
	// 追加记录模态框中的确定按钮
	$sbbgAddMarkModal.find(".confirm-btn").click(function(){
		// debugger;
		tableAddedData = [];
		var value = $sbbgAddMarkTable.bootstrapTable('getSelections');
		var tableValue = $sbbgTable.bootstrapTable('getData');
		

		for(var i = 0; i < value.length; i++){
			// value[i].destagentName = $sbbgForm.find("input[name='destagentName']").val();
			var tempValue = {};
			tempValue.destagentName = $sbbgForm.find("input[name='destagentName']").val();
			tempValue.deviceNo = value[i].deviceNo;
			value[i] = tempValue;
			for(var j = 0; j < tableValue.length; j++){
				if(value[i].deviceNo == tableValue[j].deviceNo){
					if(tableAddedData.length == 0){
						tableAddedData += value[i].deviceNo;
					}
					else{
						tableAddedData += "," + value[i].deviceNo;
					}
					value.splice(i,1);
					i--;
					break;
				}
			}
		}
		$sbbgTable.bootstrapTable("append",value);
		$sbbgAddMarkModal.modal("hide");
		if(tableAddedData.length > 0){
    		UTIL.clickDisappearSwal("机身号为" + tableAddedData + "的设备已存在");

		}
	})

	// 追加记录中的查询按钮
	$sbbgAddMarkModal.find(".check-btn").on("click",function(){
		$sbbgAddMarkTable.bootstrapTable("selectPage", 1);
	})
	    

	     // 源代理商模态框中查询数据清空
	    $sbbgAgentSourceModal.on('hide.bs.modal', function() {
	        $sbbgAgentSourceModal.find("input[name='agentNo']").val("");
	        $sbbgAgentSourceModal.find("input[name='name']").val("");
	        $sbbgAgentSourceTable.bootstrapTable("selectPage", 1);
	    })
	    // 源代理商查询按钮点击事件
	    $sbbgAgentSourceModal.find(".sbbg-agent-source-check-btn").on("click",function(){
	         $sbbgAgentSourceTable.bootstrapTable("selectPage", 1);
	     });
	    // 源代理商确认按钮点击事件
	     $sbbgAgentSourceModal.find(".confirm-btn").on("click",function(){
	         var agent = $sbbgAgentSourceTable.bootstrapTable('getSelections');
	         if(agent.length > 0){
	            agent = agent[0];
	            $sbbgForm.find("input[name='agentName']").val(agent.name);
	            $sbbgForm.find("input[name='agentId']").val(agent.agentNo);
	         }
	         if(sbbgSourceId == ""){
	         	sbbgSourceId = $sbbgForm.find("input[name='agentId']").val();
	         	// $sbbgForm.find("input[name='destagentId']").val("");
	         	// $sbbgForm.find("input[name='destagentName']").val("");
	         }
	         else if(sbbgSourceId !== "" && $sbbgForm.find("input[name='agentId']").val() !== sbbgSourceId){
	         	$sbbgTable.bootstrapTable('removeAll');
	         	sbbgSourceId = $sbbgForm.find("input[name='agentId']").val();
	         	// $sbbgForm.find("input[name='destagentId']").val("");
	         	// $sbbgForm.find("input[name='destagentName']").val("");
	         }

	         $sbbgAgentSourceModal.modal("hide");
	     });
	     // 点击出来目标代理商
	     $("#sbbg-mark-agent-icon").on("click",function(){
	     	$("#sbbg-agent-mark-table").bootstrapTable("selectPage",1);
			$("#sbbg-agent-mark-modal").modal("show");
	     })
	     // 目标代理商模态框中查询数据清空
	    $sbbgAgentMarkModal.on('hide.bs.modal', function() {
	        $sbbgAgentMarkModal.find("input[name='agentNo']").val("");
	        $sbbgAgentMarkModal.find("input[name='name']").val("");
	        $sbbgAgentMarkTable.bootstrapTable("selectPage", 1);
	    })
	    // 目标代理商查询按钮点击事件
	    $sbbgAgentMarkModal.find(".sbbg-agent-mark-check-btn").on("click",function(){
	         $sbbgAgentMarkTable.bootstrapTable("selectPage", 1);
	     });
	    // 目标代理商确认按钮点击事件
	     $sbbgAgentMarkModal.find(".confirm-btn").on("click",function(){
	     	// debugger;
	         var agent = $sbbgAgentMarkTable.bootstrapTable('getSelections');
	         if(agent.length > 0){
	            agent = agent[0];
	            $sbbgForm.find("input[name='destagentId']").val(agent.agentNo);
	            $sbbgForm.find("input[name='destagentName']").val(agent.name);
	         }
	         if(sbbgMarkId == ""){
	         	sbbgMarkId = $sbbgForm.find("input[name='destagentId']").val();
	         }
	         else if(sbbgMarkId !== "" && $sbbgForm.find("input[name='destagentId']").val() !== sbbgMarkId){
	         	$sbbgTable.bootstrapTable('removeAll');
	         	$sbbgTable.bootstrapTable("load",[]);
	         	sbbgMarkId = $sbbgForm.find("input[name='destagentId']").val();
	         }
	         $sbbgAgentMarkModal.modal("hide");
	     });

	     // 提交表单
	     $("#sbbg-table-submit-btn").click(function(){
	     	sbbgTableOption = UTIL.form2json({ form:"#sbbg-option-form" });
	     	sbbgTableOption.outstorageDetailList = $sbbgTable.bootstrapTable("getData");
	     	if(sbbgTableOption.outstorageDetailList.length === 0){  
		        UTIL.clickDisappearSwal("请先添加要变更的设备");
	     	}
	     	else{
	     		$.ajax({
		     		url:"../device/changedevice",
		     		data:JSON.stringify(sbbgTableOption),
		     		dataType:"text",
		     		success:function(res){
		     			if(res == "SUCCESS"){
		     				// 设备变更操作完成后，出入库明细列表刷新
	                        if($("#detailListTable")){
	                            $("#detailListTable").bootstrapTable("refresh");
	                        }
	                        if($("#zdSbListTable")){
	                            $("#zdSbListTable").bootstrapTable("refresh");
	                        }
		     				UTIL.autoDisappearSwal("提交成功", function(){
		     					var delUrl = "terminal-sbbg.html";
	                            $(".container-header").find(".urlBlock").each(function(){
	                                if($(this).data("page").match(delUrl)){
	                                    $(this).find(".closePage").click();
	                                }
	                            })
		     				})
		     			}
		     		}
		     	})
	     	}  	
	     })
	     // 删除操作
	 $("#terminal-sbbg .change-del-equipment-btn").on("click",function(){
        var selectionList = $sbbgTable.bootstrapTable("getSelections");
        var deviceNos = "";
        if(selectionList.length > 0 ){
        	for(var i = 0; i < selectionList.length; i++){
                if(i === 0){
                    deviceNos = selectionList[i].deviceNo;
                }
                else{
                    deviceNos += "," + selectionList[i].deviceNo;
                }
            }
            $("#sbbg-delete-madal .del-deviceNo").text(deviceNos);
            $("#sbbg-delete-madal").modal("show");
        }
        else{
            UTIL.clickDisappearSwal("请先选中之后再进行删除操作！");
        }
        
    });
	 // 删除模态框中的确认按钮
	 $("#sbbg-delete-madal .confirm-btn").click(function(){
	 	 var deviceNos = $.map($sbbgTable.bootstrapTable('getSelections'), function (row,index) {
                return row.deviceNo;
            });
        $sbbgTable.bootstrapTable('remove', {
            field: 'deviceNo',
            values: deviceNos
        });
        currentTableData = $sbbgTable.bootstrapTable("getData");
         $sbbgTable.bootstrapTable("load",currentTableData);

        $("#sbbg-delete-madal").modal("hide");
	 });

	  $("#sbbg-table-cancel-btn").click(function(){
        UTIL.closeCurrentPage({
            "delUrl":"terminal-sbbg.html",
            "toUrl":"terminal-sbcx.html"
        })
    })
})
