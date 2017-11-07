// $('[data-toggle="table"]').bootstrapTable();
function agentAddParams(argms){
    argms.agentNo = $("#fagent").val();
    return argms;
}
//定义全局form表单
var $form2 = $("#editAgentForm");
var agent_num=null;
$form2.find('#edit-register-province').prop("disabled", false);
$form2.find('#edit-register-city').prop("disabled", false);
// $("#editAgentForm input[name='agentVo.name']").prop("disabled", false);
//此处定义全局的变量把控之后的限制
var agent_up=null;
//新增代理商身份识别
$.ajax({
    url: "/agent/getInsertInfo",
    method: 'get',
    async: false,
    success: function (res) {
        if (res.edit_parentAgentName == null) {
            $('#edit_searchFagent').show();
        } else {
            $('#edit_searchFagent').hide();
            $('#edit_parentAgentName').val(res.edit_parentAgentName);
        }
        $.each(res, function (i, n) {
            if (res[i] == null) {
                res[i] = 0;
            }
        })

        agent_up=res;
        red_upifo();
    }
})

var aUpdate = UTIL.readUrlData({"currentUrl" : "agent-update-info.html"});//获取当前页面的参数
var iagentNo = aUpdate["agentNo"];
var iagentId = aUpdate["agentId"];
var imarkId=aUpdate["markId"];
agent_num=iagentNo;
if(imarkId==2){
    $("#regcostRate").attr("readonly",true);
    $("#edit_searchFagent").css("display",'none');
}else if(imarkId==3||imarkId==1){
    $('#editAgentForm input').attr('disabled','disabled')
    $('#editAgentForm select').attr('disabled','disabled')
}
if(imarkId==3||imarkId==4){
    $('#fot_box').css('display','none')
}

var city_code=null;
var area=null;
// 页面加载的时候先把城市和区填完整
$.ajax({
    type:'get',
    url:'../agent/getUpdateInfo/?id='+iagentId,
    async: false,
    success:function(res){
         city_code=res.agentVo.provinceCode
         area=res.agentVo.cityCode
    }
});

//页面加载默先加载省，三级联动start
$.ajax({
    url: "/system/address/loadAllData/0/0",
    method: 'post',
    data:"{'type':0,'code':0}",
    success: function (res) {
        var op='';
        $.each(res,function (i,n){
            op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
        })
        $('#edit-register-province').append(op);
    },
    error: function (xhr, textStatus,
                     errorThrown) {
        alert("亲，初始化省列表出错，请联系管理员！");
    }
})

$.ajax({
    url:  '/system/address/loadAllData/'+1+'/'+city_code,
    method: 'post',
    data:"{'type':0,'code':0}",
    success: function (res) {
        var op='';
        $.each(res,function (i,n){
            op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
        })
        $('#edit-register-city').append(op);
    },
    error: function (xhr, textStatus, errorThrown) {
        alert("亲，初始化省列表出错，请联系管理员！");
    }
})

$.ajax({
    url:  '/system/address/loadAllData/'+2+'/'+area,
    method: 'post',
    data:"{'type':0,'code':0}",
    success: function (res) {
        var op='';
        $.each(res,function (i,n){
            op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
        })
        $('#edit-register-area').append(op);
    },
    error: function (xhr, textStatus, errorThrown) {
        alert("亲，初始化省列表出错，请联系管理员！");
    }
})



function edit_change_city(e,n) {
    $('#edit-register-city option:gt(0)').remove();
    $.ajax({
        url: '/system/address/loadAllData/'+e+'/'+n,
        method: 'post',
        success: function (res) {
            var op='';
            $.each(res,function (i,n){
                op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
            })
            $('#edit-register-city').append(op);
        },
        error: function (xhr, textStatus,
                         errorThrown) {
            alert("亲，初始化省列表出错，请联系管理员！");
        }
    })
}
function edit_change_area(e,n) {
    $('#edit-register-area option:gt(0)').remove();
    $.ajax({
        url: '/system/address/loadAllData/'+e+'/'+n,
        method: 'post',
        success: function (res) {
            var op='';
            $.each(res,function (i,n){
                op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
            })
            $('#edit-register-area').append(op);
        },
        error: function (xhr, textStatus,
                         errorThrown) {
            alert("亲，初始化省列表出错，请联系管理员！");
        }
    })
}
$("#edit-register-province").change(function (){
    var city=$(this).find("option:selected").attr("value");
    var area=$("#edit-register-city").find("option:selected").attr("value");
    edit_change_city(1,city)
    edit_change_area(2,area)
})
$("#edit-register-city").change(function (){
    var area=$(this).find("option:selected").attr("value");
    edit_change_area(2,area)
})
//===========================================END

//遍历上传图片，注意form对应
$("#editAgentForm .upImgs").each(function () {
    $(this).uploadPreview();
});


//银行支行模态窗口初始化数据
function merchantNewBankBean(argms) {
    argms.bankcode = $("#edit_merchantNewbankid  option:selected").data('shortcode');
    argms.branchbankcode = $("#edit-bank-apply-modal input[name='branchbankcode']").val() || "";
    argms.branchbankname = $("#edit-bank-apply-modal input[name='branchbankname']").val() || "";
    return argms;
}
;



//初始化开户总行名称列表
$.ajax({
    url: "/system/bank/loadAllBank",
    method: 'post',
    success: function (res) {
        var $selectBankList = $form2.find('select').filter(
            function () {
                return ($(this).attr('id') || '').toLowerCase()
                        .indexOf('bankid') >= 0;
            });
        $selectBankList.empty();
        $selectBankList.each(function (index) {
            for (var i = 0; i < res.length; i++) {
                var str = $("#bankTmpl").html();
                $(this)
                    .append(
                        //bankcode shortcode showname
                        str.replace(/\{\{code\}\}/g,
                            res[i].bankcode).replace(/\{\{bankcode\}\}/g, res[i].bankcode).replace(/\{\{shortcode\}\}/g, res[i].shortcode)
                            .replace(/\{\{showname\}\}/g, res[i].showname));
            }
            changeBank($(this));
            $(this).change(function () {
                var $that = $(this);
                changeBank($that);
            });
        });
    },
    error: function (xhr, textStatus,
                     errorThrown) {
        alert("亲，初始化开户总行名称列表出错，请联系管理员！");
    }
});

function changeBank($that, success) {
    var shortcodeCode = $('option:selected', $that).data('shortcode')
        .toString();

    $('#edit_merchantNewBranchbankName').val('');
    $('#edit_merchantNewBranchbankId').val('');
}

// 初始化table
$('[data-toggle="table"]').bootstrapTable();

// 开户行模态框消失时校验状态更新
$("#edit-bank-apply-modal").on('hide.bs.modal', function () {
    $form2.data('formValidation').updateStatus('boCustomer.branchbankName', 'NOT_VALIDATED', null);
});


//弹出模态框
$('#edit-bank-apply-modal-show').on('click', function () {
    $("#edit-bank-apply-modal").modal("show");
});


//银行支行模态窗
// 银行支行模态框中的数据清空-初始化

$("#edit-bank-apply-modal").on('show.bs.modal', function () {
    //在银行支行显示的时候给予银行名字赋值；
    var bankName=$("#edit_merchantNewbankid  option:selected").text();
    $("#edit_bankName").val(bankName);
    function merchantNewBankBean(argms) {
        argms.bankcode = $("#edit_merchantNewbankid  option:selected").data('shortcode');
        argms.branchbankcode = $("#edit-bank-apply-modal input[name='branchbankcode']").val() || "";
        argms.branchbankname = $("#edit-bank-apply-modal input[name='branchbankname']").val() || "";
        return argms;
    };
    $("#edit_applybandTable").bootstrapTable("selectPage", 1);
});


$("#edit-bank-apply-modal").on('hide.bs.modal', function () {
    $("#edit-bank-apply-modal input[name='branchbankId']").val("");
    $("#edit-bank-apply-modal input[name='branchbankName']").val("");
    $("#edit_applybandTable").bootstrapTable("selectPage", 1);
});

// 银行支行查询按钮点击事件
var $sqdlsbhTableData =  $("#edit_applybandTable").bootstrapTable("getData");
$("#edit-bank-apply-modal .bank-btn-query").on("click", function (e) {
    e.preventDefault();
    if($sqdlsbhTableData.length === 0 ){
        var randomNum = parseInt(Math.random()*99999);
        var sqdlsbhTableRefreshUrl =  $("#edit_applybandTable").data("url") + "?" + randomNum;
        $("#edit_applybandTable").bootstrapTable("refresh", {
            url:sqdlsbhTableRefreshUrl
        });
        $sqdlsbhTableData =  $("#edit_applybandTable").bootstrapTable("getData");
    }
    else{
        $("#edit_applybandTable").bootstrapTable("selectPage", 1);
    }

});
//银行支行模态框中查询数据清空
$("#edit-bank-apply-modal").on('hide.bs.modal', function () {
    $("#edit-bank-apply-modal input[name='branchbankcode']").val("");
    $("#edit-bank-apply-modal input[name='branchbankname']").val("");
    $("#edit-bank-apply-modal").bootstrapTable("selectPage", 1);
});
// 银行支行确认按钮点击事件
$("#edit-bank-apply-modal .btn-confirm").on("click", function () {
    var manufacturer = $("#edit_applybandTable").bootstrapTable('getSelections');
    if (manufacturer.length > 0) {
        manufacturer = manufacturer[0];
        $form2.find("input[id='edit_merchantNewBranchbankId']").val(manufacturer.branchbankcode);
        $form2.find("input[id='edit_merchantNewBranchbankName']").val(manufacturer.branchbankname);
    }
    $("#edit-bank-apply-modal").modal("hide");
});


var $dlsxzTableData = $("#edit_fagentTable").bootstrapTable("getData");
//父代理商查询弹框触发
$("#edit_searchFagent").on('click', function () {
    $("#edit_fagent_modal").modal("show");

});

//父代理商查询事件
$("#fagentQuery").on('click', function (e) {
    e.preventDefault();

    if ($dlsxzTableData.length === 0) {
        var randomNum = parseInt(Math.random() * 99999);
        var dlsxzTableRefreshUrl = $("#edit_fagentTable").data("url") + "?" + randomNum;
        $("#edit_fagentTable").bootstrapTable("refresh", {
            url: dlsxzTableRefreshUrl
        });
        $dlsxzTableData = $("#edit_fagentTable").bootstrapTable("getData");
    }
    else {
        $("#edit_fagentTable").bootstrapTable("selectPage", 1);
    }


});
//父代理商确定事件
$("#edit_sureFagent").on('click', function () {
    var tempAgent = $("#edit_fagentTable").bootstrapTable('getSelections');
    console.log('ac')
    if (tempAgent.length > 0) {
        tempAgent = tempAgent[0];
        $("#editAgentForm").find("input[name='agentVo.parentAgentName']").val(tempAgent.name).focus();
        $("#editAgentForm").find("input[name='agentVo.parentAgentId']").val(tempAgent.agentNo);
    }
    $("#edit_fagent_modal").modal("hide");
})

// 父代理商模态框中查询数据清空
$("#edit_fagent_modal").on('hide.bs.modal', function () {
    $("#fagent").val("");
    $("#edit_fagentTable").bootstrapTable("selectPage", 1);//清空选中项
})

//父代理商模态框关闭按钮
$("#edit_closeFagent").on('click', function () {
    $("#edit_fagent_modal").modal("hide");
});

//新增代理商取消事件
$("#edit_closeAdd").on('click', function () {
    //设置即将跳转到的页面
    UTIL.closeCurrentPage({
        delUrl: "agent-update-info.html",
        toUrl: "agent-query.html"
    });

})

//代理商更新提交事件
$(document).ready(function () {
    //表单验证并提交
    $('#editAgentForm').formValidation({
        message: '此值无效', //此值无效
        excluded: [":hidden", ":disabled"],
        icon: {
            valid: 'glyphicon',
            invalid: 'glyphicon'
        },
        fields: {
            //机构名称
            "agentVo.name": {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '机构名称不能为空！'
                    }
                }
            },
            //登陆用户名
            'agentVo.shortName': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '登陆用户名不能为空！'
                    },
                    regexp: {
                        regexp: /[a-zA-Z0-9]{4,10}/,
                        message: '用户名只能是4到10位英文和数字！'
                    }
                }
            },
            //代理商简称
            'agentVo.edit_parentAgentName': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '代理商不能为空！'
                    }
                }
            },
            //营业执照号
            'agentVo.licenceNo': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '营业执照号不能为空！'
                    },
                    stringLength:{
                        min:0,
                        max:19,
                        message: '请输入合法的营业执照号！'
                    },
                }
            },
            //税务登记号
            'agentVo.taxId': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '税务登记号不能为空！'
                    },
                    stringLength:{
                        min:0,
                        max:21,
                        message: '请输入合法的税务登记号！'
                    },
                    regexp: {
                        regexp: /^\d{15}|\d{20}$/,
                        message: '请输入至少15位的税务登记号！'
                    }
                }
            },
            //法人代表
            'agentVo.corpRepr': {
                validators: {
                    notEmpty: {
                        message: '法人代表不能为空!'
                    }
                }
            },
            //法人代表身份证号
            'agentVo.corpReprNo': {
                validators: {
                    notEmpty: {
                        message: '法人代表身份证号不能为空!'
                    },
                    regexp: {
                        regexp: /^[1-9]{1}[0-9]{14}$|^[1-9]{1}[0-9]{16}([0-9]|[xX])$/,
                        message: '请输入15或18位正确的身份证号码！'
                    }
                }
            },
            //代理商联系人
            'agentVo.agentContact': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '代理商联系人不能为空!'
                    }
                }
            },
            //代理商电话
            'agentVo.mobile': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '代理商电话不能为空！'
                    },
                    regexp: {
                        regexp: /^1[34578]\d{9}$/,
                        message: '请填写正确的手机号码！'
                    }
                }
            },
            // //代理商邮箱
            'agentVo.agentEmail': {
                group: '.form-group',
                validators: {
                    regexp: {
                        regexp: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
                        message: '请填写正确格式的邮箱！'
                    },
                }
            },
            // //代理商邮编
            'agentVo.postalCode': {
                group: '.form-group',
                validators: {
                    regexp: {
                        regexp: /^[1-9][0-9]{5}$/,
                        message: '请填写正确格式的邮编！'
                    }
                },
            },
            //代理商省
            'agentVo.provinceCode': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '请选择省！'
                    },
                    between: {
                        min: 1,
                        max: 88888888888,
                        message: '请选择省'
                    }
                }
            },
            //代理商市
            'agentVo.cityCode': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '请选择市！'
                    },
                    between: {
                        min: 1,
                        max: 88888888888,
                        message: '请选择市'
                    }
                }
            },
            //代理商区
            'agentVo.areaCode': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '请选择地区'
                    },
                    between: {
                        min: 1,
                        max: 88888888888,
                        message: '请选择区'
                    }
                }
            },
            //代理商详细地址
            'agentVo.address': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '代理商详细地址不能为空！'
                    }
                }
            },
            //结算开户行名称
            'agentVo.bankName': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '输入不能为空！'
                    }
                }
            },
            //结算支行名称
            'agentVo.bankBranchName': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '结算支行名称不能为空！'
                    }
                }
            },
            //结算账号名称
            'agentVo.accountName': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '结算账号名称不能为空！'
                    }
                }
            },
            //结算账号
            'agentVo.accountCardNo': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '结算账号不能为空！'
                    },
                    regexp: {
                        regexp:/^[1-9][0-9]{14,18}$/,
                        message: '请输入15到19位卡号！'
                    }
                }
            },
            //结算方式
            'agentVo.settlingMethod': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '结算方式不能为空！'
                    }
                }
            },
            //提现限额
            'agentVo.withdrawCashT0': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '提现限额不能为空！'
                    },
                    between: {
                        min: 0,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //借记卡封顶手续费
            'agentVo.swingCardLimit': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '借记卡封顶手续费不能为空！'
                    },
                    between: {
                        min: agent_up.swingCardLimit,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //借记卡手续费
            'agentVo.swingCardDebitRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '借记卡手续费不能为空！'
                    },
                    between: {
                        min: agent_up.swingCardDebitRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //贷记卡手续费
            'agentVo.swingCardCreditRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '贷记卡手续费不能为空！'
                    },
                    between: {
                        min: agent_up.swingCardCreditRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //刷卡结算手续费
            'agentVo.swingCardSettleFee': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '刷卡结算手续费不能为空！'
                    },
                    between: {
                        min: agent_up.swingCardSettleFee,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //无卡手续费
            'agentVo.nonCardCreditRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '无卡手续费不能为空！'
                    },
                    between: {
                        min: agent_up.nonCardCreditRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //借记卡封顶手续费
            'agentVo.posDebitLimit': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '借记卡封顶手续费不能为空！'
                    },
                    between: {
                        min: agent_up.posDebitLimit,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //借记卡手续费
            'agentVo.posDebitRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '借记卡手续费不能为空！'
                    },
                    between: {
                        min:agent_up.posDebitRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //贷记卡手续费
            'agentVo.posCreditRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '贷记卡手续费不能为空！'
                    },
                    between: {
                        min: agent_up.posCreditRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //交易结算手续费
            'agentVo.posSettleFee': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '交易结算手续费不能为空！'
                    },
                    between: {
                        min: agent_up.posDebitRate,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //微信手续费
            'agentVo.scanCodeWxRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '微信手续费不能为空！'
                    },
                    between: {
                        min: agent_up.scanCodeWxRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //支付宝手续费
            'agentVo.scanCodeZfbRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '支付宝手续费不能为空！'
                    },
                    between: {
                        min: agent_up.scanCodeZfbRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //银联扫码手续费
            'agentVo.scanCodeYlRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '银联扫码手续费不能为空！'
                    },
                    between: {
                        min: agent_up.scanCodeYlRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //花呗手续费
            'agentVo.scanCodeMyhbRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '花呗手续费不能为空！'
                    },
                    between: {
                        min: agent_up.scanCodeMyhbRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //其他手续费
            'agentVo.scanCodeOtherRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '其他手续费不能为空！'
                    },
                    between: {
                        min:agent_up.scanCodeOtherRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                    
                }
            },
            //京东白条手续费
            'agentVo.scanCodeJdbtRate': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '京东白条手续费不能为空！'
                    },
                    between: {
                        min: agent_up.scanCodeJdbtRate,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //刷卡机具押金
            'agentVo.terminalSwipeDeposit': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '刷卡机具押金不能为空！'
                    },
                    between: {
                        min: agent_up.terminalSwipeDeposit,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //刷卡机具返还条件
            'agentVo.terminalSwipeDepositReturn': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '刷卡机具返还条件不能为空！'
                    },
                    between: {
                        min: agent_up.terminalSwipeDepositReturn,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //传统pos机局具押金
            'agentVo.terminalPosDeposit': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '传统pos机局具押金不能为空！'
                    },
                    between: {
                        min:agent_up.terminalPosDeposit ,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //传统pos机具返还条件
            'agentVo.terminalPosDepositReturn': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '传统pos机具返还条件不能为空！'
                    },
                    between: {
                        min:agent_up.terminalPosDepositReturn ,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            },
            //传统pos月押金
            'agentVo.terminalPosDepositMonthly': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '传统pos月押金不能为空！'
                    },
                    between: {
                        min:agent_up.terminalPosDepositMonthly ,
                        max: 1000000,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^((?:-?0)|(?:-?[1-9]\d*))(?:\.\d{1,2})?$/,
                        message : '只可带两位小数'//
                    }
                }
            }
        }
    })
    .on('err.validator.fv',
        function(e, data) {
            e.preventDefault();
            data.element
                .data('fv.messages')
                .find('.help-block[data-fv-for="' + data.field + '"]').hide()
                .filter('[data-fv-validator="' + data.validator + '"]').show();
        })
        .on(
            'success.form.fv',
            function (e, data) {
                e.preventDefault();
                showLoading();
                var formjson = UTIL.form2json({
                    form: "#editAgentForm"
                });

                $.ajax({
                    url: "../agent/updateAgent",
                    data: JSON.stringify(formjson),
                    success: function (res) {
                        hideLoading();
                        UTIL.autoDisappearSwal(" 成功更新代理商!", function () {
                            UTIL.closeCurrentPage({
                                delUrl: "agent-update-info.html",
                                toUrl: "agent-query.html"
                            });
                        });
                    },
                    error: function (res) {
                        hideLoading();
                        if (res.responseText.length > 200) {
                            UTIL.clickDisappearSwal("系统出错，请联系管理员");
                        }
                        else {
                            var errorMsg = JSON.parse(res.responseText).errorMsg;
                            UTIL.clickDisappearSwal(errorMsg);
                        }
                    }
                });
            })
})
// 旋转图片
$("#up-showImg-madal .rotateImg").click(function(){
    ROTATEIMGFLAG = (++ROTATEIMGFLAG) % 4;
    $(this).siblings('img').css({'transform': 'rotate(' + ROTATEIMGFLAG*90 + 'deg)'});
})
//用来显示的数据的
$(function () {
    var aUpdate = UTIL.readUrlData({"currentUrl" : "agent-update-info.html"});//获取当前页面的参数
    var iagentNo = aUpdate["agentNo"];
    var iagentId = aUpdate["agentId"];
    var imarkId=aUpdate["markId"];

    var iObj = {};
    iObj.agentNo = iagentNo;
    $("#edit_agentNo").val(iagentNo);
    $("#edit_id").val(iagentId);
    $("#edit_markId").val(imarkId);

// 回显更新页面的数据
    $.ajax({
        type:'get',
        url:'../agent/getUpdateInfo/?id='+iagentId,
        success:function(res){
            $("#editAgentForm").json2form(res);
            //遍历拿取图片
            up_info();
        }
    });
})

function up_info() {
    var els_box=$("#editAgentForm .upImgs");
   $.each(els_box,function () {
        $(this).uploadPreview();
    });
}


//=============================================================二维码添加==================================================================================
//实例化表格
$('#agent_code').bootstrapTable();
// 查询搜索参数设置
function agent_code_searh(args){
    args.agentNo=agent_num;
    return args;
}
//删除查看事件
function agent_code_some(index,row){
    merchantTemp = ['<a href="javascript:void(0)" style="margin-left: 10px;"title=""><img src="../imgs/del-sm.png"  alt=""  style="margin-right: 10px;" onclick="modal_show_imgcode(\''+ row.id + '\')"><img src="../imgs/img_code.png"  alt="" onclick="modal_show_imgcode_down(\'' + row.qrcodePath + '\')"></a>'
    ]
    return merchantTemp;
}
//删除事件
function modal_show_imgcode(rp){
    $("#qrCodeId").text(rp);
    $("#img_del_model").modal("show");
}
$("#agent_img_code_btn_del").on('click',function () {
    var qrCodeId= $("#qrCodeId").text();
    $("#img_del_model").modal("hide");
    showLoading();
    var del_qid=[qrCodeId];
    $.ajax({
            url: "/qrcode/agent/delete",
            type: 'POST',

            data:JSON.stringify(del_qid),
            success: function (res) {
                hideLoading();
                //实例化表格
                if (res.result=='success'){
                    UTIL.clickDisappearSwal("删除二维码成功");
                    $('#agent_code').bootstrapTable('refresh');
                }else{
                    UTIL.clickDisappearSwal(res.msg);
                }
            },
            error: function (res) {
                hideLoading();
                UTIL.clickDisappearSwal("删除二维码失败，请联系管理员");
            }
        });
})

//    二维码查看
function modal_show_imgcode_down(rp) {
    var img_url = OSSHOST + rp
    $("#agent_key").text(rp);
    $("#agent_mer_imgcode").attr('src', img_url)
    $("#agent_img_uplod_updown").modal('show')
}
//下载
$("#agent_profit_btn_code").click(function () {
    var simpKey =  $("#agent_key").text();
    $.ajax({
        type: "POST",
        dataType: "text",
        contentType: "application/json",
        url: "/auth/setOssMeta.json?key=" + simpKey,
        success: function (res) {
            $("#agent_img_uplod_updown").modal('hide')
            if (res) {
                window.location = OSSHOST + res;
            } else {
                UTIL.clickDisappearSwal("文件下载失败");
            }
        }
    });
});
//多条删除二维码
$("#del_imgs_code").on('click',function () {
    var imgs_code_box=$("#agent_code").bootstrapTable('getSelections');
    if(imgs_code_box.length<1){
        UTIL.clickDisappearSwal("请先选择一个二维码！");
        //    获取其中的选中的id;
    }else{
        var new_id_row=[]
        $.each(imgs_code_box,function (i,n){
            new_id_row.push(imgs_code_box[i].id)
        })

//            删除代理商二维码
        $.ajax({
            type : "post",
            dataType : "json",
            contentType : "application/json",
            url : "/qrcode/agent/delete",
            data:JSON.stringify(new_id_row),
            success : function(res) {
                UTIL.clickDisappearSwal("批量删除二维码成功")
                $("#agent_code").bootstrapTable("selectPage", 1);
            },
            error : function() {
                hideLoading();
                UTIL.clickDisappearSwal("删除失败!")
            }
        });
    }
})
//新增二维码
$("#add_imgs_code").on('click',function () {
    $("#imgs_code_all").modal("show")
    $("#agent_code_add_table").bootstrapTable('refresh');
})
$("#btn_add_img_code").on('click',function () {
   var num_imgs= $("#num_imgs").val();
    var regObj=new RegExp(/^([0-4]?\d{1}|50)$/g);
    if(regObj.test(num_imgs)){
        showLoading();
        var add_data={'agentNo':agent_num,'count':num_imgs};
        $.ajax({
            type : "post",
            dataType : "json",
            contentType : "application/json",
            url : "/qrcode/agent/save",
            data:JSON.stringify(add_data),
            success : function(res) {
                hideLoading();
                if (res.result=='success'){
                    $("#imgs_code_all").modal("hide")
                    UTIL.clickDisappearSwal("二维码生成成功")
                    $("#agent_code").bootstrapTable("refresh");
                }else{
                    UTIL.clickDisappearSwal("二维码生成失败")
                }
            },
            error : function() {
                    hideLoading();
                    UTIL.clickDisappearSwal("二维码生成失败!")
            }
        });
    }else{
        UTIL.clickDisappearSwal("请输入正确的二维码个数!")
    }
})
//输入验证
$("#num_imgs").on('blur',function () {
    var val_imgs=$(this).val()
    var regObj=new RegExp(/^([0-4]?\d{1}|50)$/g);
    if(regObj.test(val_imgs)){
          $("#btn_add_img_code").removeAttr('disabled')
    }else{
        $("#btn_add_img_code").prop('disabled','true')
    }
})
//批量下载二维码
$("#bnts_img_down").on('click', function () {
    var box_row = $("#agent_code_add_table").bootstrapTable("getSelections");
    var all_box_row = [];
    if (box_row.length > 0) {
        $.each(box_row, function (i, v) {
                all_box_row.push(v.qrcodePath);
        })
            $.ajax({
                type: "POST",
                dataType: "text",
                contentType: "application/json",
                url: "/auth/oss-zip-file?key=" + all_box_row,
                success: function (res) {
                    var zipPaths = JSON.parse(res).zipPath;
                    if (res) {
                        window.location = OSSHOST + zipPaths;
                    } else {
                        UTIL.clickDisappearSwal("文件下载失败");
                    }
                }
            });
    } else {
        UTIL.clickDisappearSwal("请选择二维码");
    }

})
$("#imgs_code_all").on('hide.bs.modal', function () {
   $("#num_imgs").val('');
});
//此处方法有待改进
function red_upifo() {
    $("#editAgentForm input[name='agentVo.swingCardLimit']").prop("placeholder",'请输入不低于'+agent_up.swingCardLimit+'的数值');
    $("#editAgentForm input[name='agentVo.swingCardDebitRate']").prop("placeholder",'请输入不低于'+agent_up.swingCardDebitRate+'的数值');
    $("#editAgentForm input[name='agentVo.swingCardCreditRate']").prop("placeholder",'请输入不低于'+agent_up.swingCardCreditRate+'的数值');
    $("#editAgentForm input[name='agentVo.swingCardSettleFee']").prop("placeholder",'请输入不低于'+agent_up.swingCardSettleFee+'的数值');
    $("#editAgentForm input[name='agentVo.nonCardCreditRate']").prop("placeholder",'请输入不低于'+agent_up.nonCardCreditRate+'的数值');
    $("#editAgentForm input[name='agentVo.posDebitLimit']").prop("placeholder",'请输入不低于'+agent_up.posDebitLimit+'的数值');
    $("#editAgentForm input[name='agentVo.posDebitRate']").prop("placeholder",'请输入不低于'+agent_up.posDebitRate+'的数值');
    $("#editAgentForm input[name='agentVo.posCreditRate']").prop("placeholder",'请输入不低于'+agent_up.posCreditRate+'的数值');
    $("#editAgentForm input[name='agentVo.posSettleFee']").prop("placeholder",'请输入不低于'+agent_up.posSettleFee+'的数值');
    $("#editAgentForm input[name='agentVo.scanCodeWxRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeWxRate+'的数值');
    $("#editAgentForm input[name='agentVo.scanCodeZfbRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeZfbRate+'的数值');
    $("#editAgentForm input[name='agentVo.scanCodeYlRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeYlRate+'的数值');
    $("#editAgentForm input[name='agentVo.scanCodeMyhbRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeMyhbRate+'的数值');
    $("#editAgentForm input[name='agentVo.scanCodeOtherRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeOtherRate+'的数值');
    $("#editAgentForm input[name='agentVo.scanCodeJdbtRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeJdbtRate+'的数值');
    $("#editAgentForm input[name='agentVo.terminalSwipeDeposit']").prop("placeholder",'请输入不低于'+agent_up.terminalSwipeDeposit+'的数值');
    $("#editAgentForm input[name='agentVo.terminalSwipeDepositReturn']").prop("placeholder",'请输入不低于'+agent_up.terminalSwipeDepositReturn+'的数值');
    $("#editAgentForm input[name='agentVo.terminalPosDeposit']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDeposit+'的数值');
    $("#editAgentForm input[name='agentVo.terminalPosDepositReturn']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDepositReturn+'的数值');
    $("#editAgentForm input[name='agentVo.terminalPosDepositMonthly']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDepositMonthly+'的数值');
}