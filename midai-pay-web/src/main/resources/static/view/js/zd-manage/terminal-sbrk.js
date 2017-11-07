            // 厂商列表参数
function sbrkManufacturerQuery(argms){
    argms.factoryName = $("#sbrk-manufacturer-modal input[name='factoryName']").val();
    argms.modeName = $("#sbrk-manufacturer-modal input[name='modeName']").val();
    return argms;
};
function sbrkAgentQuery(argms){
    argms.agentNo  = $("#sbrk-agent-modal input[name='agentNo']").val();
    argms.name = $("#sbrk-agent-modal input[name='name']").val();
    return argms;
}


$(function(){
// 定义对象放table里面的数据
    var $table = $("#sbrk-detail");
    var $sbrkForm = $("#sbrkForm");
    var tableData = [];  //用来存放设备入库table里面的数据
    var sbrkBeforeBodyNos = "";
    var sbrkTableOption = {};
    $.ajax({
        url:"../device/receiptno",
        type:"get",
        data:{
            "receiptType" : 1
        },
        success:function(res){
            $("#terminal-sbrk .batchNo").text(res.batchNo);
             $sbrkForm.find("input[name='rkNo']").val(res.batchNo);
        }
    })
    $sbrkForm.find(".manufacturer-modal-show").click(function(){
        $("#sbrk-manufacturer-modal").modal("show");
    })
     $sbrkForm.find(".agent-modal-show").click(function(){
        $("#sbrk-agent-modal").modal("show");
    })

// 初始化table
    $('[data-toggle="table"]').bootstrapTable();
    // 删除操作
    $("#terminal-sbrk .delete-equipment-btn").on("click",function(){
        var selectionList = $table.bootstrapTable("getSelections");
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
            $("#sbrk-delete-record .del-deviceNo").text(deviceNos);
            $("#sbrk-delete-record").modal("show");
        }
        else{
           UTIL.clickDisappearSwal("请先选中之后再进行删除操作！");
        }  
    });
    // 删除模态框确定按钮点击事件
    $("#sbrk-delete-record .confirm-btn").click(function(){
         var deviceNos = $.map($table.bootstrapTable('getSelections'), function (row) {
            return row.deviceNo;
        });
        $table.bootstrapTable('remove', {
            field: 'deviceNo',
            values: deviceNos
        });
        $("#sbrk-delete-record").modal("hide");
         tableData = $table.bootstrapTable("getData");
         sbrkBeforeBodyNos = "";
         for(var i = 0; i < tableData.length; i++){
            if(i == 0){
                 sbrkBeforeBodyNos += tableData[i].deviceNo;
            }
            else{
                sbrkBeforeBodyNos += "," + tableData[i].deviceNo;
            }
         }
    });
    // 厂商模态框中的数据清空-初始化
    $("#sbrk-manufacturer-modal").on('hide.bs.modal', function() {
        $("#sbrk-manufacturer-modal input[name='factoryName'").val("");
        $("#sbrk-manufacturer-modal input[name='modeName'").val("");
        $("#sbrkManufacturerTable").bootstrapTable("selectPage", 1);
    })
    // 厂商查询按钮点击事件
    $("#sbrk-manufacturer-modal .manufacturer-check-btn").on("click",function(){
         $("#sbrkManufacturerTable").bootstrapTable("selectPage", 1);
     });
    // 厂商确认按钮点击事件
    $("#sbrk-manufacturer-modal .confirm-btn").on("click",function(){
         var manufacturer = $("#sbrkManufacturerTable").bootstrapTable('getSelections');
         if(manufacturer.length > 0){
            manufacturer = manufacturer[0];
            $sbrkForm.find("input[name='factoryId']").val(manufacturer.factoryId);
            $sbrkForm.find("input[name='factoryName']").val(manufacturer.factoryName).focus();
            $sbrkForm.find("input[name='deviceTypeName']").val(manufacturer.typeName).focus();
            $sbrkForm.find("input[name='deviceTypeId']").val(manufacturer.typeId);
            $sbrkForm.find("input[name='deviceModeName']").val(manufacturer.modeName).focus();
            $sbrkForm.find("input[name='deviceModeId']").val(manufacturer.modeId);
         }
         $("#sbrk-manufacturer-modal").modal("hide");
     });

    // 代理商模态框中查询数据清空
    $("#sbrk-agent-modal").on('hide.bs.modal', function() {
        $("#sbrk-agent-modal input[name='agentNo'").val("");
        $("#sbrk-agent-modal input[name='name'").val("");
        $("#sbrkAgentTable").bootstrapTable("selectPage", 1);
    })
    // 代理商查询按钮点击事件
    // sbrk-agent-check-btn
    $("#sbrk-agent-modal .sbrk-agent-check-btn").on("click",function(){
         $("#sbrkAgentTable").bootstrapTable("selectPage", 1);
     });
    // 代理商确认按钮点击事件
     $("#sbrk-agent-modal .confirm-btn").on("click",function(){
         var agent = $("#sbrkAgentTable").bootstrapTable('getSelections');
         if(agent.length > 0){
            agent = agent[0];
            $sbrkForm.find("input[name='agentName']").val(agent.name).focus();
            $sbrkForm.find("input[name='agentId']").val(agent.agentNo);
         }
         $("#sbrk-agent-modal").modal("hide");
     });
     // 增加记录按钮点击事件
     // 增加记录表单验证
     $sbrkForm.formValidation({
            message: '这个字段还没验证',
            icon: {
                valid: 'glyphicon',
                invalid: 'glyphicon'
            },
            fields :{
                "factoryName":{
                    group: '.form-group',
                    validators : {
                        notEmpty: {
                            message: '厂商名称不能为空'
                        }
                    }
                },
                "deviceTypeName":{
                    group: '.form-group',
                    validators : {
                        notEmpty: {
                            message: '设备类型不能为空'
                        }
                    }
                },
                "deviceModeName":{
                    group: '.form-group',
                    validators : {
                        notEmpty: {
                            message: '终端型号不能为空'
                        }
                    }
                },
                "agentName":{
                    group: '.form-group',
                    validators : {
                        notEmpty: {
                            message: '代理商名称不能为空'
                        }
                    }
                },
                "bodyNoStart":{
                    group: '.form-group',
                    validators : {
                        notEmpty: {
                            message: '机身号始不能为空'
                        },
                       regexp : {
                             regexp : /^[0-9a-zA-Z][0-9a-zA-Z\-]{15}$/ ,
                            message : '请输入由数字或字母开头，并由数字字母和“-”组成的16位字符'
                        }
                    }
                },
                "num":{
                    group: '.form-group',
                    validators : {
                        notEmpty: {
                            message: '累加数量不能为空'
                        },
                       regexp : {
                            regexp : /^[1-9]\d{0,2}$/ ,
                            message : '请输入正确的1-3位数字'
                        }
                    }
                }
            }
        }).on("success.form.fv", function(e){
            e.preventDefault();
            showLoading();
                // if(!$sbrkForm.find("input[name='factoryName']").val()){
                //     UTIL.clickDisappearSwal("请先选择一个厂商", function(){

                //     });
                // }
                // else{
                    // if(!$sbrkForm.find("input[name='agentName']").val()){
                    //     UTIL.clickDisappearSwal("请先选择一个代理商");

                    // }
                    // else{
                        // if($sbrkForm.data("formValidation").isValid()){
                            sbrkTableOption = UTIL.form2json({ form:"#sbrkForm" });
                            sbrkTableOption.beforeBodyNos = sbrkBeforeBodyNos;
                            $.ajax({
                                url:"../device/instorage/check",
                                data:JSON.stringify(sbrkTableOption),
                                success:function(res){
                                    hideLoading();
                                    if(res.result === "FAIL"){
                                        var outPut = res.existsResult.join(",\n");
                                        UTIL.clickDisappearSwal(outPut + "已存在");
                                    }
                                    else if(res.result === "SUCCESS"){
                                        for(var i = 0; i < res.successResult.length; i++){
                                            if(sbrkBeforeBodyNos !== ""){
                                                sbrkBeforeBodyNos += ","+res.successResult[i].deviceNo;
                                            }
                                            else{
                                                sbrkBeforeBodyNos += res.successResult[i].deviceNo;
                                            }
                                        }
                                        sbrkTableOption.beforeBodyNos = sbrkBeforeBodyNos;
                                        tableData = tableData.concat(res.successResult);
                                        $("#sbrk-detail").bootstrapTable("append",res.successResult);
                                    }
                                }
                            })
                        // }
                    // }
                // }

                
        })  


    // 入库设备明细提交
    $("#sbrk-table-submit-btn").click(function(){
        sbrkTableOption.deviceDetailList = tableData;
        if(tableData.length === 0){ 
            UTIL.clickDisappearSwal("请先添加要入库的设备");
        }
        else{
            $.ajax({
                url:"../device/instorage",
                type:"post",
                data:JSON.stringify(sbrkTableOption),
                dataType:"text",
                success:function(res){
                    if(res == "SUCCESS"){
                        // 入库操作完成后，出入库明细列表刷新
                        if($("#detailListTable")){
                            $("#detailListTable").bootstrapTable("refresh");
                        }
                        if($("#zdSbListTable")){
                            $("#zdSbListTable").bootstrapTable("refresh");
                        }
                        UTIL.autoDisappearSwal("提交成功");
                        setTimeout(function(){
                            UTIL.toOtherPage({
                                delUrl : "terminal-sbrk.html",
                                toUrl : "terminal-sbcx.html"
                            });
                        }, 1000)                           
                    }
                    else{
                        UTIL.clickDisappearSwal("出bug了，请联系系统管理员");
                    }
                }
            })
        }   
    })
    // 取消按钮点击事件
    $("#sbrk-table-cancel-btn").click(function(){
        UTIL.closeCurrentPage({
            "delUrl":"terminal-sbrk.html",
            "toUrl":"terminal-sbcx.html"
        });
    })   
});