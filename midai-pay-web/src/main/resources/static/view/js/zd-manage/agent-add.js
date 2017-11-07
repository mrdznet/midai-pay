// $('[data-toggle="table"]').bootstrapTable();
function agentAddParams(argms){
    argms.agentNo = $("#fagent").val();
    return argms;
}
//定义全局form表单
var $form = $("#addAgentForm");

$form.find('#register-province').prop("disabled", false);
$form.find('#register-city').prop("disabled", false);
//此处定义全局的变量把控之后的限制
var agent_up=null;
//新增代理商身份识别
$.ajax({
    url: "/agent/getInsertInfo",
    method: 'get',
    async: false,
    success: function (res) {
        if (res.parentAgentName == null) {
            $('#searchFagent').show();
        } else {
            $('#searchFagent').hide();
            $('#parentAgentName').val(res.name);
            $('#parentAgentId').val(res.agentNo);
            agent_num=res.agentNo;
        }
        $.each(res, function (i, n) {
            if (res[i] == null) {
                res[i] = 0;
            }
        })
        //设置全局保存的数值；
        agent_up=res;
        red();
    }
})
//此处定义全局的变量把控之后的限制
var agent_up2=null;
//新增代理商身份识别
$.ajax({
    url: "/agent/templet/getAgentTemplet",
    method: 'get',
    async: false,
    success: function (res) {
        //获取全部代理商信息；
        $.each(res, function (i, n) {
            if (res[i] == null) {
                res[i] = 0;
            }
        })
        agent_up2=res;
        var box_id=$('#parentAgentId').val().length;
        if(box_id>0){
            red_val()
        }
    }
})
//三级联动start
$.ajax({
    url: "/system/address/loadAllData/0/0",
    method: 'post',
    data:"{'type':0,'code':0}",
    success: function (res) {
            var op='';
            $.each(res,function (i,n){
                op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
            })
        $('#register-province').append(op);
    },
    error: function (xhr, textStatus,
                     errorThrown) {
        alert("亲，初始化省列表出错，请联系管理员！");
    }
})
function change_city(e,n) {
    $('#register-city option:gt(0)').remove();
    $.ajax({
        url: '/system/address/loadAllData/'+e+'/'+n,
        method: 'post',
        success: function (res) {
            var op='';
            $.each(res,function (i,n){
                op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
            })
            $('#register-city').append(op);
        },
        error: function (xhr, textStatus,
                         errorThrown) {
            alert("亲，初始化省列表出错，请联系管理员！");
        }
    })
}
function change_area(e,n) {
    $('#register-area option:gt(0)').remove();
    $.ajax({
        url: '/system/address/loadAllData/'+e+'/'+n,
        method: 'post',
        success: function (res) {
            var op='';
            $.each(res,function (i,n){
                op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
            })
            $('#register-area').append(op);
        },
        error: function (xhr, textStatus,
                         errorThrown) {
            alert("亲，初始化省列表出错，请联系管理员！");
        }
    })
}
// change_city(1,1000)
// change_area(2,1000)
$("#register-province").change(function (){
    console.log(1)
    $('#register-area option:gt(0)').remove();
    var city=$(this).find("option:selected").attr("value")
    var area=$("#register-city").find("option:selected").attr("value")
    change_city(1,city)
    // change_area(2,area)
})
$("#register-city").change(function (){
    var area=$(this).find("option:selected").attr("value")
    change_area(2,area)
})
//===========================================END


$(function () {
    //遍历上传图片，注意form对应
    var add_els_box=$("#addAgentForm .upImgs")
    $.each(add_els_box,function (i,v) {
        $(this).uploadPreview(v);
    });
})

//银行支行模态窗口初始化数据
function merchantnew_addBankBean(argms) {
    argms.bankcode = $("#merchantnew_addbankid  option:selected").data('shortcode');
    argms.branchbankcode = $("#bank-apply-modal input[name='branchbankcode']").val() || "";
    argms.branchbankname = $("#bank-apply-modal input[name='branchbankname']").val() || "";
    return argms;
};



//初始化开户总行名称列表
$.ajax({
    url: "/system/bank/loadAllBank",
    method: 'post',
    success: function (res) {
        var $selectBankList = $form.find('select').filter(
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

    $('#merchantNewBranchbankName').val('');
    $('#merchantNewBranchbankId').val('');
}

// 初始化table
$('[data-toggle="table"]').bootstrapTable();

// 开户行模态框消失时校验状态更新
$("#bank-apply-modal").on('hide.bs.modal', function () {
    $form.data('formValidation').updateStatus('boCustomer.branchbankName', 'NOT_VALIDATED', null);
});


//弹出模态框
$('#add-bank-apply-modal-show').on('click', function () {
    $("#bank-apply-modal").modal("show");
});


//银行支行模态窗
// 银行支行模态框中的数据清空-初始化

$("#bank-apply-modal").on('show.bs.modal', function () {
    //在银行支行显示的时候给予银行名字赋值；
    var bankName=$("#merchantnew_addbankid  option:selected").text();
    $("#add_bankName").val(bankName);
    function merchantnew_addBankBean(argms) {
        argms.bankcode = $("#merchantnew_addbankid  option:selected").data('shortcode');
        argms.branchbankcode = $("#bank-apply-modal input[name='branchbankcode']").val() || "";
        argms.branchbankname = $("#bank-apply-modal input[name='branchbankname']").val() || "";
        return argms;
    };
    $("#applybandTable").bootstrapTable("selectPage", 1);
});


$("#bank-apply-modal").on('hide.bs.modal', function () {
    $("#bank-apply-modal input[name='branchbankId']").val("");
    $("#bank-apply-modal input[name='branchbankName']").val("");
    $("#applybandTable").bootstrapTable("selectPage", 1);
});

// 银行支行查询按钮点击事件
var $sqdlsbhTableData =  $("#applybandTable").bootstrapTable("getData");
$("#bank-apply-modal .bank-btn-query").on("click", function (e) {
    e.preventDefault();
    if($sqdlsbhTableData.length === 0 ){
        var randomNum = parseInt(Math.random()*99999);
        var sqdlsbhTableRefreshUrl =  $("#applybandTable").data("url") + "?" + randomNum;
        $("#applybandTable").bootstrapTable("refresh", {
            url:sqdlsbhTableRefreshUrl
        });
        $sqdlsbhTableData =  $("#applybandTable").bootstrapTable("getData");
    }
    else{
        $("#applybandTable").bootstrapTable("selectPage", 1);
    }

});

//银行支行模态框中查询数据清空
$("#bank-apply-modal").on('hide.bs.modal', function () {
    $("#bank-apply-modal input[name='branchbankcode']").val("");
    $("#bank-apply-modal input[name='branchbankname']").val("");
    $("#bank-apply-modal").bootstrapTable("selectPage", 1);
});
// 银行支行确认按钮点击事件
$("#bank-apply-modal .btn-confirm").on("click", function () {
    var manufacturer = $("#applybandTable").bootstrapTable('getSelections');
    if (manufacturer.length > 0) {
        manufacturer = manufacturer[0];
        $form.find("input[id='merchantnew_addBranchbankId']").val(manufacturer.branchbankcode);
        $form.find("input[id='merchantnew_addBranchbankName']").val(manufacturer.branchbankname);
    }
    $("#bank-apply-modal").modal("hide");
});


        var $dlsxzTableData = $("#fagentTable").bootstrapTable("getData");
        //父代理商查询弹框触发
        $("#searchFagent").on('click', function () {
            $("#fagent_modal").modal("show");
        });

        //父代理商查询事件
        $("#fagentQuery").on('click', function (e) {
            e.preventDefault();

            if ($dlsxzTableData.length === 0) {
                var randomNum = parseInt(Math.random() * 99999);
                var dlsxzTableRefreshUrl = $("#fagentTable").data("url") + "?" + randomNum;
                $("#fagentTable").bootstrapTable("refresh", {
                    url: dlsxzTableRefreshUrl
                });
                $dlsxzTableData = $("#fagentTable").bootstrapTable("getData");
            }
            else {
                $("#fagentTable").bootstrapTable("selectPage", 1);
            }


        });
        //父代理商确定事件
        $("#sureFagent").on('click', function () {
            var tempAgent = $("#fagentTable").bootstrapTable('getSelections');
            if (tempAgent.length > 0) {
                tempAgent = tempAgent[0];
                $("#addAgentForm").find("input[name='agentVo.parentAgentName']").val(tempAgent.name).focus();
                $("#addAgentForm").find("input[name='agentVo.parentAgentId']").val(tempAgent.agentNo);
            }
            $("#fagent_modal").modal("hide");
            get_all_box(tempAgent.agentNo);
            agent_num=tempAgent.agentNo;
            //实例化表格
            $('#agent_code').bootstrapTable('refresh');
            red_val()
        })
        //代理商后期增加需求
        function get_all_box(agNo) {
            //新增代理商身份识别
            $.ajax({
                url: "/agent/templet/getAgentTempletByAgentNo?agentNo="+agNo,
                method: 'get',
                async: false,
                success: function (res) {
                    agent_up=res;
                    agent_up2=res;
                    red();
                }
            })
        }


        // 父代理商模态框中查询数据清空
        $("#fagent_modal").on('hide.bs.modal', function () {
            $("#fagent").val("");
            $("#fagentTable").bootstrapTable("selectPage", 1);//清空选中项
        })

        //父代理商模态框关闭按钮
        $("#closeFagent").on('click', function () {
            $("#fagent_modal").modal("hide");
        });

        //新增代理商取消事件
        $("#closeAdd").on('click', function () {
            //设置即将跳转到的页面
            UTIL.closeCurrentPage({
                delUrl: "agent-add.html",
                toUrl: "agent-query.html"
            });

        })

        //新增代理商提交事件
        $(document).ready(function () {
            //表单验证并提交
            $('#addAgentForm').formValidation({
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
                    'agentVo.parentAgentName': {
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
                                regexp: /^[1-9]\d{5}[1-9]\d{3}((0\d)|(1[0-2]))(([0|1|2]\d)|3[0-1])\d{4}$/,
                                message: '请输入18位正确的身份证号码！'
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
                    //代理商邮编
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
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                                regexp :/^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                                min: agent_up.posSettleFee,
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
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                                message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                            form: "#addAgentForm"
                        });

                        $.ajax({
                            url: "../agent/insertAgent",
                            data: JSON.stringify(formjson),
                            success: function (res) {
                                hideLoading();
                                UTIL.autoDisappearSwal(" 成功新建代理商!", function () {
                                    UTIL.closeCurrentPage({
                                        delUrl: "agent-add.html",
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
// 代理商范围限制；

    // var form_box=$("#addAgentForm input")
    // for (var i = form_box.length - 1; i >= 0; i--) {
    //     if($(form_box[i]).next("span").text().indexOf("元")>=0||$(form_box[i]).next("span").text().indexOf("%")>=0){
    //         var name_box=$(form_box[i]).attr("name");
    //         var  up_name=name_box.substring(8,name_box.length-1).trim();
    //         console.log(up_name)
    //         var min_number="请输入不低于"+agent_up[up_name]+"值"
    //         $(form_box[i]).prop("placeholder",min_number)
    //     }
    // }








//此处方法有待改进
function red() {
    $("#addAgentForm input[name='agentVo.swingCardLimit']").prop("placeholder",'请输入不低于'+agent_up.swingCardLimit+'的数值');
    $("#addAgentForm input[name='agentVo.swingCardDebitRate']").prop("placeholder",'请输入不低于'+agent_up.swingCardDebitRate+'的数值');
    $("#addAgentForm input[name='agentVo.swingCardCreditRate']").prop("placeholder",'请输入不低于'+agent_up.swingCardCreditRate+'的数值');
    $("#addAgentForm input[name='agentVo.swingCardSettleFee']").prop("placeholder",'请输入不低于'+agent_up.swingCardSettleFee+'的数值');
    $("#addAgentForm input[name='agentVo.nonCardCreditRate']").prop("placeholder",'请输入不低于'+agent_up.nonCardCreditRate+'的数值');
    $("#addAgentForm input[name='agentVo.posDebitLimit']").prop("placeholder",'请输入不低于'+agent_up.posDebitLimit+'的数值');
    $("#addAgentForm input[name='agentVo.posDebitRate']").prop("placeholder",'请输入不低于'+agent_up.posDebitRate+'的数值');
    $("#addAgentForm input[name='agentVo.posCreditRate']").prop("placeholder",'请输入不低于'+agent_up.posCreditRate+'的数值');
    $("#addAgentForm input[name='agentVo.posSettleFee']").prop("placeholder",'请输入不低于'+agent_up.posSettleFee+'的数值');
    $("#addAgentForm input[name='agentVo.scanCodeWxRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeWxRate+'的数值');
    $("#addAgentForm input[name='agentVo.scanCodeZfbRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeZfbRate+'的数值');
    $("#addAgentForm input[name='agentVo.scanCodeYlRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeYlRate+'的数值');
    $("#addAgentForm input[name='agentVo.scanCodeMyhbRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeMyhbRate+'的数值');
    $("#addAgentForm input[name='agentVo.scanCodeOtherRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeOtherRate+'的数值');
    $("#addAgentForm input[name='agentVo.scanCodeJdbtRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeJdbtRate+'的数值');
    $("#addAgentForm input[name='agentVo.terminalSwipeDeposit']").prop("placeholder",'请输入不低于'+agent_up.terminalSwipeDeposit+'的数值');
    $("#addAgentForm input[name='agentVo.terminalSwipeDepositReturn']").prop("placeholder",'请输入不低于'+agent_up.terminalSwipeDepositReturn+'的数值');
    $("#addAgentForm input[name='agentVo.terminalPosDeposit']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDeposit+'的数值');
    $("#addAgentForm input[name='agentVo.terminalPosDepositReturn']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDepositReturn+'的数值');
    $("#addAgentForm input[name='agentVo.terminalPosDepositMonthly']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDepositMonthly+'的数值');
}
function red_val() {
    $("#addAgentForm input[name='agentVo.swingCardLimit']").val(agent_up2.swingCardLimit);
    $("#addAgentForm input[name='agentVo.swingCardDebitRate']").val(agent_up2.swingCardDebitRate);
    $("#addAgentForm input[name='agentVo.swingCardCreditRate']").val(agent_up2.swingCardCreditRate);
    $("#addAgentForm input[name='agentVo.swingCardSettleFee']").val(agent_up2.swingCardSettleFee);
    $("#addAgentForm input[name='agentVo.nonCardCreditRate']").val(agent_up2.nonCardRate);
    $("#addAgentForm input[name='agentVo.posDebitLimit']").val(agent_up2.posDebitLimit);
    $("#addAgentForm input[name='agentVo.posDebitRate']").val(agent_up2.posDebitRate);
    $("#addAgentForm input[name='agentVo.posCreditRate']").val(agent_up2.posCreditRate);
    $("#addAgentForm input[name='agentVo.posSettleFee']").val(agent_up2.posSettleFee);
    $("#addAgentForm input[name='agentVo.scanCodeWxRate']").val(agent_up2.scanCodeWxRate);
    $("#addAgentForm input[name='agentVo.scanCodeZfbRate']").val(agent_up2.scanCodeZfbRate);
    $("#addAgentForm input[name='agentVo.scanCodeYlRate']").val(agent_up2.scanCodeYlRate);
    $("#addAgentForm input[name='agentVo.scanCodeMyhbRate']").val(agent_up2.scanCodeMyhbRate);
    $("#addAgentForm input[name='agentVo.scanCodeOtherRate']").val(agent_up2.scanCodeOtherRate);
    $("#addAgentForm input[name='agentVo.scanCodeJdbtRate']").val(agent_up2.scanCodeJdbtRate);
    $("#addAgentForm input[name='agentVo.terminalSwipeDeposit']").val(agent_up2.terminalSwipeDeposit);
    $("#addAgentForm input[name='agentVo.terminalSwipeDepositReturn']").val(agent_up2.terminalSwipeDepositReturn);
    $("#addAgentForm input[name='agentVo.terminalPosDeposit']").val(agent_up2.terminalPosDeposit);
    $("#addAgentForm input[name='agentVo.terminalPosDepositReturn']").val(agent_up2.terminalPosDepositReturn);
    $("#addAgentForm input[name='agentVo.terminalPosDepositMonthly']").val(agent_up2.terminalPosDepositMonthly);
}

