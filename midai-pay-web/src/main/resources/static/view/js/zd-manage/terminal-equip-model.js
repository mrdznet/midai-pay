/**
 * Created by ykk on 2016/9/24.
 */

$('[data-toggle="table"]').bootstrapTable();
$(function () {

    var TIP_MODEL = $(".tipModel");

    // 新建modal弹出
    $("#modelNew").on("click", function () {
        $("#modalModal").modal();
    });

    // 新建弹框确认索要新建的设备 信息
    $('#addModelCus')
        .formValidation({
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
                'modelFactory': {
                    validators: {
                        notEmpty: {
                            message: '输入不能为空！'
                        }
                    }

                },
                'modelType': {
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
            $("#modalModal").find('[type="submit"]').addClass('disabled').prop("disabled", true);
            var modelName = $("#modelName").val();
            var modelFactory = $("#modelFactory").find("option:selected");
            var modelType = $("#modelType").find("option:selected");
            var modelStatus = $("#modelStatus").find("option:selected");
            var modelNote = $("#modelNote").val();
            var willmodelNew = {
                name: modelName,
                state: modelStatus.val(),
                factoryId: modelFactory.val(),
                deviceTypeId: modelType.val(),
                note: modelNote
            };
            $.ajax({
                type: "POST",
                url: "../deviceMode/insertDeviceType",
                data: JSON.stringify(willmodelNew),
                success: function (res) {
                    $("#modalModal").modal("hide");
                    if (res.result == "success") {
                        var text = "成功新建终端型号!";
                        UTIL.autoDisappearSwal(text, function () {
                            $("#modelListTable").bootstrapTable("refresh");
                        });
                    } else {
                        text = "新建终端型号失败，请重新操作!";
                        UTIL.clickDisappearSwal(text);
                    }
                }
            });
        });

    //表单重置
    $("#modalModal").on("hide.bs.modal", function () {
        document.getElementById("addModelCus").reset();
        $("#addModelCus").data('formValidation').resetForm();
    });

    //动态添加厂商下拉菜单；
    $.ajax({
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        url: "../factory/queryFactoryInfo",
        success: function (res) {
            for (var i = 0; i < res.length; i++) {
                var idNum = res[i].id;
                var optionStr = "<option value=" + idNum + "> " + res[i].name + "</option>";
                $("#modelFactory").append(optionStr)
            }
        }
    });

    //动态添加终端类型下拉菜单；
    $.ajax({
        type: "GET",
        dataType: "json",
        contentType: "application/json",
        url: "../deviceType/allDeviceType",
        success: function (res) {
            for (var i = 0; i < res.length; i++) {
                var idNum = res[i].id;
                var optionStr = "<option value=" + idNum + "> " + res[i].name + "</option>";
                $("#modelType").append(optionStr)
            }
        }
    });

    // 删除设备
    var modelDeleteIds;
    $("#modelDetele").click(
        function () {
            var willDeleteModel = $('#modelListTable').bootstrapTable(
                "getSelections");
            var willDeleteModel_length = willDeleteModel.length;
            if (willDeleteModel_length == 0) {
                var text = "请选择您要删除的型号的终端!";
                UTIL.clickDisappearSwal(text);
            } else {
                $("#modalModalDelete").modal();
                var typeDeleteNo = [];
                modelDeleteIds = [];
                for (var i = 0; i < willDeleteModel_length; i++) {
                    typeDeleteNo.push(willDeleteModel[i].name);
                    modelDeleteIds.push(willDeleteModel[i].id);
                }
                // 删除合同之前的准备：清空模态框
                $(".modelDeleteNo").html(typeDeleteNo.toString());
            }
        });

    // 确认删除该型号的终端设备,刷新页面
    $("#modalDeleteSure").click(function () {
        $.ajax({
            type: "POST",
            dataType: "json",
            contentType: "application/json",
            url: "../deviceMode/deleteDeviceType",
            data: JSON.stringify(modelDeleteIds),
            success: function (res) {
                $("#modalModalDelete").modal("hide");
                if (res.result == "success") {
                    var text = "成功删除选中型号的终端!";
                    UTIL.autoDisappearSwal(text, function () {
                        $("#modelListTable").bootstrapTable("selectPage", 1);
                    });
                } else {
                    text = "失败删除选中型号的终端,请重新操作!!";
                    UTIL.clickDisappearSwal(text, function () {
                        $("#modalModalDelete").modal("hide");
                    });
                }
            }
        });
    });

});
