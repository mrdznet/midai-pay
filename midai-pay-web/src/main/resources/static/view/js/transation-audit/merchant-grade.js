/**
 * Created by ykk on 2016/10/28.
 */

/*
$('[data-toggle="table"]').bootstrapTable();

function searchTable(args){
    return args;
}

$(function(){



   $("#gmerchantInput").click(function(){
       $("#merchantGradeModal").modal();
   });

    $("#merchantGradeForm").formValidation({
        message: '此值无效', // 此值无效
        icon: {
            valid: 'glyphicon',
            invalid: 'glyphicon',
            validating: 'glyphicon'
        },
        fields: {
            'name': {
                validators: {
                    notEmpty: {
                        message: '输入不能为空！'
                    }
                }

            },
            'state': {
                validators: {
                    notEmpty: {
                        message: '输入不能为空！'
                    }
                }

            },
            'note': {
                group: '.form-group',
                validators: {
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
        var factoryName = $("#factoryName").val();
        var factoryStaus = $("#factoryStaus").find("option:selected");
        var factoryAddress = $("#factoryAddress").val();
        var factoryNote = $("#factoryNote").val();
        var willfactoryNew = {
            name: factoryName,
            address: factoryAddress,
            state: factoryStaus.val(),
            note: factoryNote
        };
        $.ajax({
            type: "POST",
            url: "../factory/add",
            data: JSON.stringify(willfactoryNew),
            success: function (res) {
                // bootstrap 添加新的行
                $('#factoryListTable').bootstrapTable('insertRow', {
                    index: 0,
                    row: {
                        name: factoryName,
                        address: factoryAddress,
                        state: factoryStaus.text(),
                        note: factoryNote
                    }
                });
                $("#fasctoryModal").modal("hide");
                if (res == 1) {
                    var text="成功新建厂商!";
                    UTIL.autoDisappearSwal(text,function(){
                        $("#factoryListTable").bootstrapTable("refresh");
                    });
                } else {
                    text= "新建厂商失败，请重新操作!";
                    UTIL.clickDisappearSwal(text);
                }
            }
        });
    });


//等级修改
    $("#gmerchantChange").click(function(){
        $("#changeGradeModal").modal();
    });
    $("#merchantGradeForm").formValidation({
        message: '此值无效', // 此值无效
        icon: {
            valid: 'glyphicon',
            invalid: 'glyphicon',
            validating: 'glyphicon'
        },
        fields: {
            'name': {
                validators: {
                    notEmpty: {
                        message: '输入不能为空！'
                    }
                }

            },
            'state': {
                validators: {
                    notEmpty: {
                        message: '输入不能为空！'
                    }
                }

            },
            'note': {
                group: '.form-group',
                validators: {
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
        var factoryName = $("#factoryName").val();
        var factoryStaus = $("#factoryStaus").find("option:selected");
        var factoryAddress = $("#factoryAddress").val();
        var factoryNote = $("#factoryNote").val();
        var willfactoryNew = {
            name: factoryName,
            address: factoryAddress,
            state: factoryStaus.val(),
            note: factoryNote
        };
        $.ajax({
            type: "POST",
            url: "../factory/add",
            data: JSON.stringify(willfactoryNew),
            success: function (res) {
                // bootstrap 添加新的行
                $('#factoryListTable').bootstrapTable('insertRow', {
                    index: 0,
                    row: {
                        name: factoryName,
                        address: factoryAddress,
                        state: factoryStaus.text(),
                        note: factoryNote
                    }
                });
                $("#fasctoryModal").modal("hide");
                if (res == 1) {
                    var text="成功新建厂商!";
                    UTIL.autoDisappearSwal(text,function(){
                        $("#factoryListTable").bootstrapTable("refresh");
                    });
                } else {
                    text= "新建厂商失败，请重新操作!";
                    UTIL.clickDisappearSwal(text);
                }
            }
        });
    });



//删除等级
    var willDeteleGradeNo;
    $("#gmerchantDelete").click(function(){
        willDeteleGradeNo=[];
        var willgradeDelete =  $("#merchantGradeListTable").bootstrapTable("getSelections");
         if(willgradeDelete.length==0){
             var text= "请选择需要删除选中等级的商户！"
             UTIL.clickDisappearSwal(text)
         }else{
             $("#gradeModalDelete").modal();
             for(var i=0;i<willgradeDelete.length;i++){
                 willDeteleGradeNo.push(willgradeDelete[i].deviceNo);
             }
             $(".gradeDeleteIds").html(willDeteleGradeNo.toString())
         }
    });

//确认删除；
    $("#gradeDeleteSure").click(function(){
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            url: "../factory/del/" + willDeteleGradeNo,
            success: function (res) {
                $("#gradeModalDelete").modal("hide");
                if (res == 1) {
                    var text= "成功删除商户等级!";
                    UTIL.autoDisappearSwal(text,function(){
                        $("#merchantGradeListTable").bootstrapTable("refresh");
                    });
                } else {
                    text= "删除商户等级失败，请重新操作!";
                    UTIL.clickDisappearSwal(text,function(){
                        $("#gradeModalDelete").modal("hide");
                    });
                }
            }
        });
    })
});*/
