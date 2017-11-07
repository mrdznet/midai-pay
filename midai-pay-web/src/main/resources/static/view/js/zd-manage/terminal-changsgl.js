/**
 * Created by yangkaikai on 2016/9/24.
 */

$('[data-toggle="table"]').bootstrapTable();

//判断是否暂停厂商
function stopFactoryStyle(index,row){
	if(row.state=="暂停"){
		return {
			disabled:true
		}
	}
}


$(function () {
    //新建modal弹出
    $("#fasctoryNew").on("click", function () {
        $("#fasctoryModal").modal();
    });

    // 新建弹框确认索要新建的设备 信息
    $('#addFactoryCus').formValidation({
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
        showLoading();
        // $("#factoryName").find('[type="submit"]').addClass('disabled').prop("disabled", true);
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
                hideLoading();
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
    $("#fasctoryModal").on("hide.bs.modal",function(){
        document.getElementById("addFactoryCus").reset();
        $("#addFactoryCus").data('formValidation').resetForm();
    });

    //删除设备
    var factoryDeleteIds;
    $("#fasctoryDelete").click(function () {
        var willDeleteModel = $('#factoryListTable').bootstrapTable("getSelections");
        var willDeleteModel_length = willDeleteModel.length;
        if (willDeleteModel_length == 0) {
            var text= "请选择您要暂停的厂商!";
            UTIL.clickDisappearSwal(text);
        } else {
            $("#factoryModalDelete").modal();
            var factoryDeleteNo = [];
            factoryDeleteIds = [];
            for (var i = 0; i < willDeleteModel_length; i++) {
                factoryDeleteNo.push(willDeleteModel[i].name);
                factoryDeleteIds.push(willDeleteModel[i].id);
            }
            $(".factoryDeleteIds").html(factoryDeleteNo.toString());
        }
    });

    //确认删除该型号的厂商,刷新页面
    $("#factoryDeleteSure").click(function () {
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            url: "../factory/del/" + factoryDeleteIds,
            success: function (res) {
                $("#factoryModalDelete").modal("hide");
                if (res == 1) {
                    var text= "成功暂停的厂商!";
                    UTIL.autoDisappearSwal(text,function(){
                        $("#factoryListTable").bootstrapTable("selectPage", 1);
                    });
                } else {
                     text= "暂停的厂商失败，请重新操作!";
                    UTIL.clickDisappearSwal(text,function(){
                        $("#factoryModalDelete").modal("hide");
                    });
                }
            }
        });
    });


});