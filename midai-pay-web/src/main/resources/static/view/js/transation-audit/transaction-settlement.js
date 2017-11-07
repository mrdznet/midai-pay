/**
 * Created by  on 2016/9/13.
 */
//交易结算

//交易详情
function settlementDetail(index, row) {
    var str = "详情";
    var hrefLocation = '<a  style="color:#3968c6;text-decoration:none;" target="_blank" href="/view/transaction-settlement-detail.html?mercId=' + row.mercId + '" title="查看详情">' + str + '</a>';
    return hrefLocation;
}

//商户冻结条件搜索
function tranSettleSearchTable(args) {
    args.mercId = $("input[name='tranSettle-ticket']").val(); // 商户小票号
    args.mercName = $("input[name='tranSettle-name']").val();    //商户名称
    args.tixianAmtStart = $("input[name='tranSettle-money-begin']").val();   //提现金额
    args.tixianAmtEnd = $("input[name='tranSettle-money-end']").val();      //提现金额末
    args.logNo = $("input[name='tranSettle-sliverNo']").val();  //流水号
    args.accountNo = $("input[name='tranSettle-cardNo']").val();    //入卡账号
    args.startTime = $("input[name='tranSettlesf-date-begin']").val();    //提现日期
    args.endTime = $("input[name='tranSettlesf-date-end']").val();    //提现日期末
    args.checkState = $("#tranSettleStaus").find("option:selected").val();    //审核状态
    return args;
}

//判断在待审核状态流水，商户冻结的的 通过的条件
function buttonDisabled() {
    $("#tranSettleListTable").bootstrapTable("selectPage", 1);
    var tranSettleStaus = $('#tranSettleStaus');
    if (tranSettleStaus.val() == "0") {
        $(".tranSettle-forzen").prop("disabled", false);
    } else if (tranSettleStaus.val() == "2" || tranSettleStaus.val() == "4") {
        $(".tranSettle-forzen").prop("disabled", true);
    }
}

//交易结算--清空筛选条件
$("#tranSettleEmpty").click(function (e) {
    e.preventDefault();
    document.getElementById("tranSettleForm").reset();
    buttonDisabled();

});



//交易结算--不同审核状态下列表的可选与否
function _trade$tate(index, row) {
    var trade$tateCheck = row.checkState;
    if (trade$tateCheck == "0") {
        return {
            disabled: false
        }
    } else if (trade$tateCheck == "2") {
        return {
            disabled: true
        }
    } else if (trade$tateCheck == "4") {
        return {
            disabled: true
        }
    }
}

//bootstrap --table初始化

$(function () {
	$('[data-toggle="table"]').bootstrapTable();
	var jyjsTableData = $("#tranSettleListTable").bootstrapTable("getData");
    /*  时间插件的初始化               */
    $('.my_date').datetimepicker({
        "minView": "month",
        "language": 'zh-CN',
        "format": 'yyyy-mm-dd',
        "autoclose": true,
        "pickerPosition": "bottom-left"
    });
    
  //交易结算 --条件查询
    
    $("#tranSettleSearch").click(function (e) {
    	 e.preventDefault();
    	 if(jyjsTableData.length === 0 ){
             var randomNum = parseInt(Math.random()*99999);
             var jyjsTableRefreshUrl = $("#tranSettleListTable").data("url") + "?" + randomNum;
             $("#tranSettleListTable").bootstrapTable("refresh", {
                 url: jyjsTableRefreshUrl
             });
             jyjsTableData = $("#tranSettleListTable").bootstrapTable("getData");
         }
    	 else{
    		 $("#tranSettleListTable").bootstrapTable("selectPage", 1);
    	 }
       
        buttonDisabled();
    });

    //冻结流水/商户/通过按钮的选择
    var tranSettleForzenNo;
    var tranlogNos;
    var tranlogNostring;
    var tranSettleMerForzenIds;
    $(".tranSettle-forzen").click(function () {
        tranSettleForzenNo = [];
        tranlogNos = [];
        tranlogNostring = [];
        tranSettleMerForzenIds = [];
        var tranSettleForzenShop = $('#tranSettleListTable').bootstrapTable("getSelections");
        var tranSettleForzenShop_length = tranSettleForzenShop.length;
        for (var i = 0; i < tranSettleForzenShop_length; i++) {
            tranSettleForzenNo.push(tranSettleForzenShop[i].mercName); //商户号
            tranSettleMerForzenIds.push(tranSettleForzenShop[i].mercId); //商户小票号
            tranlogNos.push(tranSettleForzenShop[i].logNo); //流水号
            tranlogNostring.push("\u0027" + tranSettleForzenShop[i].logNo + "\u0027");     //批量解冻传值-流水号；
        }
        if ($(this).hasClass("tranSettle-forzen-shop")) {
            //交易结算--冻结商户--
            if (tranSettleForzenShop_length == 0) {
                var text = "请选择您要冻结的商户!";
                UTIL.clickDisappearSwal(text);
            } else {
                $("#settlementFrozenShop").modal();
                $(".settlementForzenIdshop").html(tranSettleForzenNo.toString());

            }
        } else if ($(this).hasClass("tranSettle-forzen-water")) {
            //交易结算---冻结流水--
            if (tranSettleForzenShop_length == 0) {
                var text = "请选择您要冻结的流水号!";
                UTIL.clickDisappearSwal(text);
            } else {
                $("#settlementFrozenWater").modal();
                $(".settlementForzenIdSliver").html(tranlogNos.toString());
            }
        } else {
            if (tranSettleForzenShop_length == 0) {
                var text = "请选择您要审批通过的流水!";
                UTIL.clickDisappearSwal(text);
            }
            else {
                //交易结算---通过---begin
                $("#settlementTranPass").modal();
                $(".settlementForzenIdPass").html(tranlogNos.toString());

            }
        }
    });


    
    $('#settlementFrozenShop').on('hide.bs.modal', function () {
            document.getElementById("settlMerchantforzenCause").reset();
            $("#settlMerchantforzenCause").data('formValidation').resetForm();
})

    //确认冻结选中的商户，刷新页面
    $('#settlMerchantforzenCause').formValidation({
        message: '此值无效', // 此值无效
        icon: {
            valid: 'glyphicon',
            invalid: 'glyphicon',
            validating: 'glyphicon'
        },
        fields: {
            'frozenReason': {
                group: '.form-group',
                validators: {
                    notEmpty: {
                        message: '输入不能为空！'
                    },
                    stringLength: {
                        min: 0,
                        max: 260,
                        message: '请输入260以内的字符！'
                    }
                }
            }
        }
    }).on('success.form.fv', function (e, data) {
        e.preventDefault();
        showLoading();
        var frozenReason = $("#settlMerchantforzenCause").find("textarea[name='frozenReason']").val();
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            url: "../frozenreason/customerfrozen/?frozenReason=" + frozenReason,
            data: JSON.stringify(tranSettleMerForzenIds),
            success: function (res) {
                hideLoading();
                $("#settlementFrozenShop").modal("hide");
                if (res == 1) {
                    var text = "成功冻结商户!";
                    UTIL.autoDisappearSwal(text, function () {
                        $("#tranSettleListTable").bootstrapTable("selectPage", 1);
                        if ($("#merchantForzenListTable")) {
                            $("#merchantForzenListTable").bootstrapTable("selectPage", 1);
                        }
                    });
                } else {
                    var text = "冻结商户失败，请重新操作!";
                    UTIL.autoDisappearSwal(text, function () {
                        $("#settlementFrozenShop").modal("hide");
                    });
                }
            }
        });
    });
    
    

    //确认冻结流水
    $("#tranSettleForzenSliver").click(function (e) {
        e.preventDefault();
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            url: "../tradefrozenreason/updateTradefrozen",
            data: JSON.stringify(tranlogNos),
            success: function (res) {
                $("#settlementFrozenWater").modal("hide");
                if (res > 0) {
                    var text = "成功冻结流水!";
                    UTIL.autoDisappearSwal(text, function () {
                        $("#tranSettleListTable").bootstrapTable("selectPage", 1);
                        if ($("#jydjTable")) {
                            $("#jydjTable").bootstrapTable("selectPage", 1);
                        }
                    });
                } else {
                    var text = "冻结流水失败，请重新操作!";
                    UTIL.clickDisappearSwal(text, function () {
                        $("#settlementFrozenWater").modal("hide");
                    });
                }
            }
        });
    });

    //确认通过交易
    $("#tranSettlePass").click(function () {
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            url: "../trade/settle/update/",
            data: tranlogNostring.toString(),
            success: function (res) {
                $("#settlementTranPass").modal("hide");
                if (res > 0) {
                    var text = "成功通过!";
                    UTIL.autoDisappearSwal(text, function () {
                        $("#tranSettleListTable").bootstrapTable("selectPage", 1);
                        if ($("#qfqsTable")) {
                            $("#qfqsTable").bootstrapTable("selectPage", 1);
                        }
                    });
                } else {
                    var text = "审批通过失败，请重新操作!";
                    UTIL.clickDisappearSwal(text, function () {
                        $("#settlementTranPass").modal("hide");
                    });
                }
            }
        });//交易结算---通过---end
    })

});