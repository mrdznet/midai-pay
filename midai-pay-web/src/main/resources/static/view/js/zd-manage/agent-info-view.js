$('[data-toggle="table"]').bootstrapTable();
function agentAddParams(argms){
    argms.agentNo = $("#fagent").val();
    return argms;
}
//定义全局form表单
var $form3 = $("#info_view");

$form3.find('#view-register-province').prop("disabled", false);
$form3.find('#view-register-city').prop("disabled", false);

//此处定义全局的变量把控之后的限制
var agent_up=null;
//新增代理商身份识别
$.ajax({
    url: "/agent/getInsertInfo",
    method: 'get',
    async: false,
    success: function (res) {
        if (res.view_parentAgentName == null) {
            $('#searchFagent').show();
        } else {
            $('#searchFagent').hide();
            $('#view_parentAgentName').val(res.parentAgentId);
        }
        //获取全部代理商信息；
        $.each(res, function (i, n) {
            if (res[i] == null) {
                res[i] = 0;
            }
        })
        //设置全局保存的数值；
        agent_up=res;
    }
})
$()
var aUpdate = UTIL.readUrlData({"currentUrl" : "agent-info-view.html"});//获取当前页面的参数
var iagentNo = aUpdate["agentNo"];
var iagentId = aUpdate["agentId"];
var imarkId=aUpdate["markId"];

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
        $('#view-register-province').append(op);
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
        $('#view-register-city').append(op);
    },
    error: function (xhr, textStatus,
                     errorThrown) {
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
        $('#view-register-area').append(op);
    },
    error: function (xhr, textStatus,
                     errorThrown) {
        alert("亲，初始化省列表出错，请联系管理员！");
    }
})



function view_change_city(e,n) {
    $('#view-register-city option:gt(0)').remove();
    $.ajax({
        url: '/system/address/loadAllData/'+e+'/'+n,
        method: 'post',
        success: function (res) {
            var op='';
            $.each(res,function (i,n){
                op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
            })
            $('#view-register-city').append(op);
        },
        error: function (xhr, textStatus,
                         errorThrown) {
            alert("亲，初始化省列表出错，请联系管理员！");
        }
    })
}
function view_change_area(e,n) {
    $('#view-register-area option:gt(0)').remove();
    $.ajax({
        url: '/system/address/loadAllData/'+e+'/'+n,
        method: 'post',
        success: function (res) {
            var op='';
            $.each(res,function (i,n){
                op=op+('<option value ='+res[i].code+'>'+res[i].name+'</option>')
            })
            $('#view-register-area').append(op);
        },
        error: function (xhr, textStatus,
                         errorThrown) {
            alert("亲，初始化省列表出错，请联系管理员！");
        }
    })
}
$("#view-register-province").change(function (){
    var city=$(this).find("option:selected").attr("value")
    var area=$("#view-register-city").find("option:selected").attr("value")
    view_change_city(1,city)
    view_change_area(2,area)
})
$("#view-register-city").change(function (){
    var area=$(this).find("option:selected").attr("value")
    view_change_area(2,area)
})
//===========================================END




//初始化开户总行名称列表
$.ajax({
    url: "/system/bank/loadAllBank",
    method: 'post',
    success: function (res) {
        var $selectBankList = $form3.find('select').filter(
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


//用来显示的数据的
$(function () {
    var aUpdate = UTIL.readUrlData({"currentUrl" : "agent-info-view.html"});//获取当前页面的参数
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
            $("#info_view").json2form(res);
            $("#info_view  .upImgs input").css("border","0px")
            $("#info_view  input").prop("disabled","disabled")
            $("#info_view  select").prop("disabled","disabled")
            //遍历拿取图片
            $("#info_view .upImgs").each(function () {
                $(this).uploadPreview();
            });
        }
    });
})
