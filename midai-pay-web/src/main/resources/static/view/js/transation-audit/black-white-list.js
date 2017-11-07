
/**
 * Created by  on 2016/9/13.
 */
//黑白名单

$('[data-toggle="table"]').bootstrapTable();
function hbmdQueryOptions(argms){
    var formJson = UTIL.form2json({
        "form" : "#hbmdForm"
    });
    for(var value in formJson){
        argms[value] = formJson[value];
    }
    return argms;
}
$(function () {
(function(){
     /*  时间插件的初始化               */
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });
    //删除 加入 黑白名单；
    $("#BWmanyDelete").click(function () {
        
    });
    var $form = $("#hbmdForm");
    var $table = $("#hbmdTable");
    var $deleteModal = $("#blackWhiteDelete");
     $("#emptyHbmdSearch").on("click", function(e){
        e.preventDefault();
        document.getElementById("hbmdForm").reset();
    })
     $("#hbmdSearch").on("click", function(e){
        e.preventDefault();
        $table.bootstrapTable("selectPage", 1);
     });
     // 删除名单
     $("#BWmanyDelete").on("click", function(){
        var selectData = $table.bootstrapTable("getSelections");
        var cardStr = "";
        if(selectData.length === 0){
            swal({
                title : "提示",
                text : "请先选择要删除的名单",
                confirmButtonText : "确定"
            })
        }
        else{
            // for(var i = 0; i < selectData.length; i++){
            //     cardStr = (i == 0 ? selectData[i]. : ("," + selectData[i].))
            // }
            // $deleteModal.find(".factoryDeleteIds").text(cardStr);
            // $("#blackWhiteDelete").modal();
        }
        
     })
     // 加入白名单
     $("#addWhiteList").on("click", function(){
        var selectData = $table.bootstrapTable("getSelections");
        var cardStr = "";
        if(selectData.length === 0){
            swal({
                title : "提示",
                text : "请先选择要加入白名单的名单",
                confirmButtonText : "确定"
            })
        }
        else{
            // $.ajax({
            //     url: "",
            //     data: "",
            //     success: function(res){
            //         if(res){
            //             $table.bootstrapTable("selectPage", 1);
            //         }
            //     }
            // })

        }
     })
     // 加入黑名单
     $("#addBlackList").on("click", function(){
        var selectData = $table.bootstrapTable("getSelections");
        var cardStr = "";
        if(selectData.length === 0){
            swal({
                title : "提示",
                text : "请先选择要加入黑名单的名单",
                confirmButtonText : "确定"
            })
        }
        else{
            // $.ajax({
            //     url: "",
            //     data: "",
            //     success: function(res){
            //         if(res){
            //             $table.bootstrapTable("selectPage", 1);
            //         }
            //     }
            // })
        }
     })


})()
   


});