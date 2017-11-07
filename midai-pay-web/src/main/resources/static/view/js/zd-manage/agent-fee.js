$(document).ready(function () {
    //新增代理商取消事件
    $("#closeFee").on('click', function () {
        //设置即将跳转到的页面
        UTIL.closeCurrentPage({
            delUrl: "agent-fee.html",
            toUrl: "agent-query.html"
        });

    })
var agent_up=null;
//增加验证
$.ajax({
    url: "/agent/getInsertInfo",
    method: 'get',
    async: false,
    success: function (res) {
        $("#agent_id_hide").val(res.agentNo)
        //获取全部代理商信息；
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
    var agent_up_info=null;
//获取信息
    $.ajax({
        url: "/agent/templet/getAgentTemplet",
        method: 'get',
        async: false,
        success: function (res) {
            $("#agent_id_hide").val(res.agentNo)
            //获取全部代理商信息；
            $.each(res, function (i, n) {
                if (res[i] == null) {
                    res[i] = 0;
                }
            })
            //设置全局保存的数值；
            agent_up_info=res;
            red_val();
        }
    })
    // 表单验证并提交
    $('#FeeForm').formValidation({
        message: '此值无效', //此值无效
        excluded: [":hidden", ":disabled"],
        icon: {
            valid: 'glyphicon',
            invalid: 'glyphicon'
        },
        fields: {
            //借记卡封顶手续费
            'swingCardLimit': {
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
            'swingCardDebitRate': {
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
            'swingCardCreditRate': {
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
            'swingCardSettleFee': {
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
            'nonCardRate': {
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
            'posDebitLimit': {
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
            'posDebitRate': {
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
            'posCreditRate': {
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
            'posSettleFee': {
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
            'scanCodeWxRate': {
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
            'scanCodeZfbRate': {
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
            'scanCodeYlRate': {
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
            'scanCodeMyhbRate': {
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
            'scanCodeOtherRate': {
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
            'scanCodeJdbtRate': {
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
            'terminalSwipeDeposit': {
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
            'terminalSwipeDepositReturn': {
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
            'terminalPosDeposit': {
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
            'terminalPosDepositReturn': {
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
            'terminalPosDepositMonthly': {
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
            },
            //m-pos分润比例
            'profitMpos': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: 'm-pos分润比例不能为空！'
                    },
                    between: {
                        min:0,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
                    }
                }
            },
            //无卡分润比例
            'profitNoCard': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '无卡分润比例不能为空！'
                    },
                    between: {
                        min:0,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
                    }
                }
            },
            //大pos分润比例
            'profitTerminalPos': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '大pos分润比例不能为空！'
                    },
                    between: {
                        min:0,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
                    }
                }
            },
            //扫码分润比例
            'profitScanCode': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '扫码分润比例不能为空！'
                    },
                    between: {
                        min:0,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//0-100数值输入。可带两位小数，且不能为0 way2
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
                    form: "#FeeForm"
                });

                $.ajax({
                    url: "/agent/templet/updateAgentTemplet",
                    data: JSON.stringify(formjson),
                    success: function (res) {
                        hideLoading();
                        UTIL.autoDisappearSwal("更新代理商模板成功!", function () {
                            UTIL.closeCurrentPage({
                                delUrl: "agent-fee.html",
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
    // 此处方法有待改进
    function red() {
        $("#FeeForm input[name='swingCardLimit']").prop("placeholder",'请输入不低于'+agent_up.swingCardLimit+'的数值');
        $("#FeeForm input[name='swingCardDebitRate']").prop("placeholder",'请输入不低于'+agent_up.swingCardDebitRate+'的数值');
        $("#FeeForm input[name='swingCardCreditRate']").prop("placeholder",'请输入不低于'+agent_up.swingCardCreditRate+'的数值');
        $("#FeeForm input[name='swingCardSettleFee']").prop("placeholder",'请输入不低于'+agent_up.swingCardSettleFee+'的数值');
        $("#FeeForm input[name='nonCardRate']").prop("placeholder",'请输入不低于'+agent_up.nonCardCreditRate+'的数值');
        $("#FeeForm input[name='posDebitLimit']").prop("placeholder",'请输入不低于'+agent_up.posDebitLimit+'的数值');
        $("#FeeForm input[name='posDebitRate']").prop("placeholder",'请输入不低于'+agent_up.posDebitRate+'的数值');
        $("#FeeForm input[name='posCreditRate']").prop("placeholder",'请输入不低于'+agent_up.posCreditRate+'的数值');
        $("#FeeForm input[name='posSettleFee']").prop("placeholder",'请输入不低于'+agent_up.posSettleFee+'的数值');
        $("#FeeForm input[name='scanCodeWxRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeWxRate+'的数值');
        $("#FeeForm input[name='scanCodeZfbRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeZfbRate+'的数值');
        $("#FeeForm input[name='scanCodeYlRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeYlRate+'的数值');
        $("#FeeForm input[name='scanCodeMyhbRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeMyhbRate+'的数值');
        $("#FeeForm input[name='scanCodeOtherRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeOtherRate+'的数值');
        $("#FeeForm input[name='scanCodeJdbtRate']").prop("placeholder",'请输入不低于'+agent_up.scanCodeJdbtRate+'的数值');
        $("#FeeForm input[name='terminalSwipeDeposit']").prop("placeholder",'请输入不低于'+agent_up.terminalSwipeDeposit+'的数值');
        $("#FeeForm input[name='terminalSwipeDepositReturn']").prop("placeholder",'请输入不低于'+agent_up.terminalSwipeDepositReturn+'的数值');
        $("#FeeForm input[name='terminalPosDeposit']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDeposit+'的数值');
        $("#FeeForm input[name='terminalPosDepositReturn']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDepositReturn+'的数值');
        $("#FeeForm input[name='terminalPosDepositMonthly']").prop("placeholder",'请输入不低于'+agent_up.terminalPosDepositMonthly+'的数值');
        $("#FeeForm input[name='profitMpos']").prop("placeholder",'请输入不低于'+0+'的数值');
        $("#FeeForm input[name='profitNoCard']").prop("placeholder",'请输入不低于'+0+'的数值');
        $("#FeeForm input[name='profitTerminalPos']").prop("placeholder",'请输入不低于'+0+'的数值');
        $("#FeeForm input[name='profitScanCode']").prop("placeholder",'请输入不低于'+0+'的数值');
    }
    function red_val() {
        $("#FeeForm input[name='swingCardLimit']").val(agent_up_info.swingCardLimit);
        $("#FeeForm input[name='swingCardDebitRate']").val(agent_up_info.swingCardDebitRate);
        $("#FeeForm input[name='swingCardCreditRate']").val(agent_up_info.swingCardCreditRate);
        $("#FeeForm input[name='swingCardSettleFee']").val(agent_up_info.swingCardSettleFee);
        $("#FeeForm input[name='nonCardRate']").val(agent_up_info.nonCardRate);
        $("#FeeForm input[name='posDebitLimit']").val(agent_up_info.posDebitLimit);
        $("#FeeForm input[name='posDebitRate']").val(agent_up_info.posDebitRate);
        $("#FeeForm input[name='posCreditRate']").val(agent_up_info.posCreditRate);
        $("#FeeForm input[name='posSettleFee']").val(agent_up_info.posSettleFee);
        $("#FeeForm input[name='scanCodeWxRate']").val(agent_up_info.scanCodeWxRate);
        $("#FeeForm input[name='scanCodeZfbRate']").val(agent_up_info.scanCodeZfbRate);
        $("#FeeForm input[name='scanCodeYlRate']").val(agent_up_info.scanCodeYlRate);
        $("#FeeForm input[name='scanCodeMyhbRate']").val(agent_up_info.scanCodeMyhbRate);
        $("#FeeForm input[name='scanCodeOtherRate']").val(agent_up_info.scanCodeOtherRate);
        $("#FeeForm input[name='scanCodeJdbtRate']").val(agent_up_info.scanCodeJdbtRate);
        $("#FeeForm input[name='terminalSwipeDeposit']").val(agent_up_info.terminalSwipeDeposit);
        $("#FeeForm input[name='terminalSwipeDepositReturn']").val(agent_up_info.terminalSwipeDepositReturn);
        $("#FeeForm input[name='terminalPosDeposit']").val(agent_up_info.terminalPosDeposit);
        $("#FeeForm input[name='terminalPosDepositReturn']").val(agent_up_info.terminalPosDepositReturn);
        $("#FeeForm input[name='terminalPosDepositMonthly']").val(agent_up_info.terminalPosDepositMonthly);
        $("#FeeForm input[name='profitMpos']").val(agent_up_info.profitMpos);
        $("#FeeForm input[name='profitNoCard']").val(agent_up_info.profitNoCard);
        $("#FeeForm input[name='profitTerminalPos']").val(agent_up_info.profitTerminalPos);
        $("#FeeForm input[name='profitScanCode']").val(agent_up_info.profitScanCode);
    }
})
