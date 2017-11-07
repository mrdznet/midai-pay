/**
 * Created by zhao on 2017/7/20.
 */
//搜索参数
function transationOnlineSearch(args) {
    var jsonData = UTIL.form2json({
        "form":"#transationOnlineForm"
    });

    for(var value in jsonData){
        args[value] = jsonData[value].trim();
    }
    return args;
};
//交易查询
function agentPaySearch(index,row){
    return [
        '<button class="btn btn-success" onclick="pay_modal_show(\''+ row.transChannelCode+","+ row.seqId+ '\')" title="查询">',
        '查询',
        '</button>  '
    ].join('');
    return agentTemp;
}
//pos交易卡类型
function cardKind(index,row) {
    var data_code = row.cardKind;
    switch (data_code) {
        case '01':
            return [
                '<span class="">',
                '借记卡',
                '</span>  '
            ].join('');
            console.log(1)
            break;

        case '02':
            return [
                '<span class="">',
                '贷记卡',
                '</span>  '
            ].join('');
            break;

    }
}
//pos交易方式
function pay_modal_show(e) {
    showLoading();
    var arry_box =e .split(",");
    var data_transChannelCode = arry_box[0];
    var data_seqId = arry_box[1];
    var pay_data={
        "payType": "",
        "sendSeqId": data_seqId,
        "transType": data_transChannelCode
    }
    $.ajax({
        url: "/dealtotal/quick/checktrans",
        type: 'POST',
        data:JSON.stringify(pay_data),
        success: function (res) {
            var result=res.result;
            var payResult=result.payResult;
            var payDesc=result.payDesc
            hideLoading();
            $("#pay_status_mol").modal('show');
            $("#pay_order").text(result.orgSendSeqId)
            $("#pay_status").text(result.payDesc)
        },
        error: function (res) {
            hideLoading();
            UTIL.clickDisappearSwal("查询失败，请联系管理员");
        }
    });
}
//pos交易类型
function transType(index,row){
    var data_code=row.transCode;
    switch (data_code) {
        case '0200':
            return [
                '<span class="">',
                '消费',
                '</span>  '
            ].join('');
            console.log(1)
            break;

        case '0400':
            return [
                '<span class="">',
                '消费冲正',
                '</span>  '
            ].join('');
            break;

        case '0210':
            return [
                '<span class="">',
                '余额查询',
                '</span>  '
            ].join('');
            break;
        case '0500':
            return [
                '<span class="">',
                '提现申请',
                '</span>  '
            ].join('');
            break;
        case '0700':
            return [
                '<span class="">',
                '转账',
                '</span>  '
            ].join('');
            break;
        case '0600':
            return [
                '<span class="">',
                '信用卡还款',
                '</span>  '
            ].join('');
            break;
        case '0220':
            return [
                '<span class="">',
                '退款',
                '</span>  '
            ].join('');
            break;
    }
}
$('.my_date').datetimepicker({
    "minView": "month",
    "language": 'zh-CN',
    "format": 'yyyy-mm-dd',
    "autoclose": true,
    "pickerPosition": "bottom-left"
});
//初始化
$('[data-toggle="table"]').bootstrapTable();


//定义查询条件字段
var transaction_search_data = {
    "hostTransSsn" : "",
    "transTime":'',
    'transTimeEnd':'',
    'transCode':'',
    'transAmtBegin':'',
    'transAmtEnd':'',
    'mchntName':'',
    'cardKind':'',
    'payMode':''
}
var transationOnlineTableData = $("#transationOnlineTable").bootstrapTable("getData");

//代理商查询按钮事件
$("#online_search").click(function(e){
    e.preventDefault();
    // transaction_search_data.hostTransSsn=$("#agent-query-search-agcondition  input[name='hostTransSsn']").val().trim();
    // transaction_search_data.transTime=$("#agent-query-search-agcondition  input[name='transTime']").val().trim();
    // transaction_search_data.transTimeEnd=$("#agent-query-search-agcondition  select[name='transTimeEnd']").val().trim();
    // transaction_search_data.transCode=$("#agent-query-search-agcondition  select[name='transTimeEnd']").val().trim();
    // transaction_search_data.transAmtBegin=$("#agent-query-search-agcondition  select[name='transTimeEnd']").val().trim();
    // transaction_search_data.transAmtEnd=$("#agent-query-search-agcondition  select[name='transTimeEnd']").val().trim();
    // transaction_search_data.mchntName=$("#agent-query-search-agcondition  select[name='mchntName']").val().trim();
    // transaction_search_data.cardKind=$("#agent-query-search-agcondition  select[name='cardKind']").val().trim();
    // transaction_search_data.payMode=$("#agent-query-search-agcondition  select[name='payMode']").val().trim();
    if(transationOnlineTableData.length === 0 ){
        var randomNum = parseInt(Math.random()*99999);
        var dlscxTableRefreshUrl = $("#transationOnlineTable").data("url") + "?" + randomNum;
        $("#transationOnlineTable").bootstrapTable("refresh", {
            url: dlscxTableRefreshUrl
        });
        transationOnlineTableData = $("#transationOnlineTable").bootstrapTable("getData");
    }else{
        $("#transationOnlineTable").bootstrapTable("selectPage",1);
    }
});
//重置查询条件
$("#online_empty").on('click',function(){
    document.getElementById("transationOnlineForm").reset();
    $("#transationOnlineTable").bootstrapTable('selectPage',1);
});