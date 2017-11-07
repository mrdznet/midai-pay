// 初始化table
function sbckAgentSourceQuery(argms){
    argms.agentNo  = $("#sbck-agent-source-modal input[name='agentNo']").val();
    argms.name = $("#sbck-agent-source-modal input[name='name']").val();
    return argms;
}
function sbckAgentMarkQuery(argms){
    argms.agentNo  = $("#sbck-agent-mark-query input[name='agentNo']").val();
    argms.name = $("#sbck-agent-mark-query input[name='name']").val();
    argms.agentNor = $("#sbck-option-form input[name='agentId']").val();
    return argms;
}
function sbckAddMrkQuery(argms){
	argms.agentNo = $("#sbck-option-form input[name='agentId']").val();
	argms.bodyNoStart = $("#sbck-add-mark-query input[name='bodyNoStart']").val();
	argms.num = $("#sbck-add-mark-query input[name='num']").val();
	return argms;
}
function sbckTestTime(row,index){
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
	var $sbckAgentSourceModal = $("#sbck-agent-source-modal");
	var $sbckAgentMarkModal = $("#sbck-agent-mark-modal");
	var $sbckAgentSourceTable = $("#sbck-agent-source-table");
	var $sbckAgentMarkTable = $("#sbck-agent-mark-table");
	var $sbckForm = $("#sbck-option-form");
	var $sbckTable = $("#sbck-table");
	var $sbckAddMarkModal = $("#sbck-plus-modal");
	var $sbckAddMarkTable = $("#sbck-plus-table");
	var sbckTableData = [];
	var sbckTableOption = {};
	var sbckSourceId = "";
	var sbckMarkId = "";
	var tableAddedData = [];
	var currentTableData = [];
	// 批次号
	$.ajax({
	    url:"../device/receiptno",
	    type:"get",
	    data:{
	        "receiptType" : 2
	    },
	    success:function(res){
	        $("#terminal-sbck .batchNo").text(res.batchNo);
	         $("#terminal-sbck input[name='ckNo']").val(res.batchNo);
	         if(res.agentId){
	         	var $parentOriginAgent = $("#sbck-orgin-agent-exist").parent();
	         	$("#sbck-orgin-agent-exist").remove();
	         	$parentOriginAgent.append('<input type="text" class="form-control" name="agentId" readonly /> ');
	         	$sbckForm.find("input[name='agentId']").val(res.agentId);
		        $sbckForm.find("input[name='agentName']").val(res.agentName);
	         }
	         else{

	         }
	         $('[data-toggle="table"]').bootstrapTable();
	    }
	});
	

	// 追加记录
	$("#sbck-add-mark-btn").click(function(){
			if($sbckForm.find("input[name='destagentId']").val() == ""){
	    		UTIL.clickDisappearSwal("请先选择目标代理商编号");
			}
			else{
				document.getElementById("sbck-add-mark-query").reset();
				$sbckAddMarkModal.find("input[name='agentNo']").val($sbckForm.find("input[name='agentId']").val());
				 $sbckAddMarkTable.bootstrapTable("selectPage", 1);
				$sbckAddMarkModal.modal("show");
			}	 	
	});
	// 追加记录模态框中的确定按钮
	$sbckAddMarkModal.find(".confirm-btn").click(function(){
		// debugger;
		tableAddedData = [];
		var value = $sbckAddMarkTable.bootstrapTable('getSelections');
		var tableValue = $sbckTable.bootstrapTable('getData');
		

		for(var i = 0; i < value.length; i++){
			// value[i].destagentName = $sbckForm.find("input[name='destagentName']").val();
			var tempValue = {};
			tempValue.destagentName = $sbckForm.find("input[name='destagentName']").val();
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
		$sbckTable.bootstrapTable("append",value);
		$sbckAddMarkModal.modal("hide");
		if(tableAddedData.length > 0){
  			UTIL.clickDisappearSwal("机身号为" + tableAddedData + "的设备已存在");
		}
	});

// 追加记录中的查询按钮
$sbckAddMarkModal.find(".check-btn").on("click",function(){
	$sbckAddMarkTable.bootstrapTable("selectPage", 1);
});
//点击源代理商后面图标出现源代理商模态框
$("#sbck-origin-agent-icon").click(function(){
	$sbckAgentSourceTable.bootstrapTable("selectPage",1);
	$sbckAgentSourceModal.modal('show');
});
// 点击目标代理商后面图标出现目标代理商模态框
$("#sbck-mark-agent-icon").click(function(){
	// $sbckAgentMarkTable.bootstrapTable("selectPage",1);
	$("#sbck-agent-mark-modal").modal("show");
});
 // 源代理商模态框中查询数据清空
	    $sbckAgentSourceModal.on('hide.bs.modal', function() {
	        $sbckAgentSourceModal.find("input[name='agentNo']").val("");
	        $sbckAgentSourceModal.find("input[name='name']").val("");
	        $sbckAgentSourceTable.bootstrapTable("selectPage", 1);
	    })
	    // 源代理商查询按钮点击事件
	    $sbckAgentSourceModal.find(".sbck-agent-source-check-btn").on("click",function(){
	         $sbckAgentSourceTable.bootstrapTable("selectPage", 1);
	     });
	    // 源代理商确认按钮点击事件
	     $sbckAgentSourceModal.find(".confirm-btn").on("click",function(){
	         var agent = $sbckAgentSourceTable.bootstrapTable('getSelections');
	         if(agent.length > 0){
	            agent = agent[0];
	            $sbckForm.find("input[name='agentName']").val(agent.name);
	            $sbckForm.find("input[name='agentId']").val(agent.agentNo);
	         }
	         if(sbckSourceId == ""){
	         	sbckSourceId = $sbckForm.find("input[name='agentId']").val();
	         	// $sbckForm.find("input[name='destagentId']").val("");
	         	// $sbckForm.find("input[name='destagentName']").val("");
	         }
	         else if(sbckSourceId !== "" && $sbckForm.find("input[name='agentId']").val() !== sbckSourceId){
	         	$sbckTable.bootstrapTable('removeAll');
	         	sbckSourceId = $sbckForm.find("input[name='agentId']").val();
	         	// $sbckForm.find("input[name='destagentId']").val("");
	         	// $sbckForm.find("input[name='destagentName']").val("");
	         }
	         $sbckAgentSourceModal.modal("hide");
	     });


	     // 目标代理商模态框中查询数据清空
	    $sbckAgentMarkModal.on('hide.bs.modal', function() {
	        $sbckAgentMarkModal.find("input[name='agentNo']").val("");
	        $sbckAgentMarkModal.find("input[name='name']").val("");
	        $sbckAgentMarkTable.bootstrapTable("selectPage", 1);
	    })
	    // 目标代理商查询按钮点击事件
	    $sbckAgentMarkModal.find(".sbck-agent-mark-check-btn").on("click",function(){
	         $sbckAgentMarkTable.bootstrapTable("selectPage", 1);
	     });
	    // 目标代理商确认按钮点击事件
	     $sbckAgentMarkModal.find(".confirm-btn").on("click",function(){
	     	// debugger;
	         var agent = $sbckAgentMarkTable.bootstrapTable('getSelections');
	         if(agent.length > 0){
	            agent = agent[0];
	            $sbckForm.find("input[name='destagentId']").val(agent.agentNo);
	            $sbckForm.find("input[name='destagentName']").val(agent.name);
	         }
	         if(sbckMarkId == ""){
	         	sbckMarkId = $sbckForm.find("input[name='destagentId']").val();
	         }
	         else if(sbckMarkId !== "" && $sbckForm.find("input[name='destagentId']").val() !== sbckMarkId){
	         	$sbckTable.bootstrapTable("checkAll");
	         	var deviceNos = $.map($sbckTable.bootstrapTable('getAllSelections'), function (row,index) {
		                return row.deviceNo;
		            });
			 	 // alert(deviceNos)
		        $sbckTable.bootstrapTable('remove', {
		            field: 'deviceNo',
		            values: deviceNos
		        });
		        currentTableData = $sbckTable.bootstrapTable("checkAll");
		         $sbckTable.bootstrapTable("load",[]);
	         	sbckMarkId = $sbckForm.find("input[name='destagentId']").val();
	         }
	         $sbckAgentMarkModal.modal("hide");
	     });

	     // 提交表单
	     $("#sbck-table-submit-btn").click(function(){
	     	$(this).prop("disabled",true);
	     	sbckTableOption = UTIL.form2json({ form:"#sbck-option-form" });
	     	sbckTableOption.outstorageDetailList = $sbckTable.bootstrapTable("getData");
	     	if(sbckTableOption.outstorageDetailList.length === 0){ 
	            UTIL.clickDisappearSwal("请先添加要出库的设备");
	     	}
	     	else{
	     		$.ajax({
		     		url:"../device/outdevice",
		     		data:JSON.stringify(sbckTableOption),
		     		dataType:"text",
		     		success:function(res){
		     			if(res == "SUCCESS"){
		     				// 出库操作完成后，出入库明细列表刷新
	                        if($("#detailListTable")){
	                            $("#detailListTable").bootstrapTable("refresh");
	                        }
	                        if($("#zdSbListTable")){
	                            $("#zdSbListTable").bootstrapTable("refresh");
	                        }
				            UTIL.autoDisappearSwal("提交成功", function(){
				            	UTIL.closeCurrentPage({
					            	delUrl:"terminal-sbck.html",
					            	toUrl:"terminal-sbcx.html"
					            })
				            })
		     			}
		     		}
		     	})
	     	}
		     	
	     })
	     // 删除操作
	 $("#terminal-sbck .change-del-equipment-btn").on("click",function(){
        var selectionList = $sbckTable.bootstrapTable("getSelections");
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
            $("#sbck-delete-madal .del-deviceNo").text(deviceNos);
            $("#sbck-delete-madal").modal("show");
        }
        else{
	           UTIL.clickDisappearSwal("请先选中之后再进行删除操作！");
        }
        
    });
	 $("#sbck-delete-madal .confirm-btn").click(function(){
	 	 var deviceNos = $.map($sbckTable.bootstrapTable('getSelections'), function (row,index) {
                return row.deviceNo;
            });
	 	 // alert(deviceNos)
        $sbckTable.bootstrapTable('remove', {
            field: 'deviceNo',
            values: deviceNos
        });
        currentTableData = $sbckTable.bootstrapTable("getData");
         $sbckTable.bootstrapTable("load",currentTableData);
        $("#sbck-delete-madal").modal("hide");
	 });
	 // 取消按钮点击事件
    $("#sbck-table-cancel-btn").click(function(){
        UTIL.closeCurrentPage({
            "delUrl":"terminal-sbck.html",
            "toUrl":"terminal-sbcx.html"
        })
    })
})
