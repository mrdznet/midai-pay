/**
 * Created by yangkaikai on 2016/9/24.
 */
$('[data-toggle="table"]').bootstrapTable();
$(function() {
	// 新建modal弹出
	$("#typeNew").on("click", function() {
		$("#typeModal").modal();
	});

	// 新建弹框确认索要新建的设备 信息
	$('#addTypeCus').formValidation({
		message : '此值无效', // 此值无效
		icon : {
			valid : 'glyphicon',
			invalid : 'glyphicon',
			validating : 'glyphicon'
		},
		fields : {
			'name' : {
				validators : {
					notEmpty : {
						message : '输入不能为空！'
					}
				}

			},
			'state' : {
				validators : {
					notEmpty : {
						message : '输入不能为空！'
					}
				}

			},
			'note' : {
				group : '.form-group',
				validators : {
					stringLength : {
						min : 0,
						max : 260,
						message : '请输入260以内的字符！'
					}
				}

			}
		}
	}).on('success.form.fv', function(e, data) {

		e.preventDefault();
		showLoading();
			var typeName = $("#typeName").val();
			var typeStatus = $("#typeStatus").find("option:selected");
			var typeNote = $("#typeNote").val();
			var willTypeNew = {
				name : typeName,
				state : typeStatus.val(),
				note : typeNote
			};
			$.ajax({
				type : "POST",
				url : "../deviceType/insertDeviceType",
				data : JSON.stringify(willTypeNew),
				success : function(res) {
					hideLoading();
					$("#typeModal").modal("hide");
					if (res.result == "success") {
						var text= "成功新建终端类型!";
						UTIL.autoDisappearSwal(text,function(){
							$("#typeListTable").bootstrapTable("refresh");
						});
					} else {
						text= "新建终端类型失败，请重新操作!";
						UTIL.clickDisappearSwal(text);
					}
				}
			});
	});

	//表单重置
	$("#typeModal").on("hide.bs.modal",function(){
		document.getElementById("addTypeCus").reset();
		$("#addTypeCus").data('formValidation').resetForm();
	});


	// 删除设备
	var willTypeDeleteIds;
	$("#typeDelete").click(
			function() {
				var willDeleteModel = $('#typeListTable').bootstrapTable(
						"getSelections");
				var willDeleteModel_length = willDeleteModel.length;
				if (willDeleteModel_length == 0) {
					var text= "请选择您要删除类型的终端!";
					UTIL.clickDisappearSwal(text);
				} else {
					$("#typeModalDelete").modal();
					var typeDeleteNo = [];
					willTypeDeleteIds = [];
					for (var i = 0; i < willDeleteModel_length; i++) {
						typeDeleteNo.push(willDeleteModel[i].name);
						willTypeDeleteIds.push(willDeleteModel[i].id);
					}
					// 删除合同之前的准备：清空模态框
					$(".typeDeleteNo").html(typeDeleteNo.toString());
				}
			});

	// 确认删除该型号的终端设备,刷新页面
	$("#typeDeleteSure").click(function() {
		$.ajax({
			type : "POST",
			dataType : "json",
			contentType : "application/json",
			url : "../deviceType/deleteDeviceType",
			data : JSON.stringify(willTypeDeleteIds),
			success : function(res) {
				if (res.result == "success") {
					$("#typeModalDelete").modal("hide");
					var text= "成功删除选中类型的终端!";
					UTIL.autoDisappearSwal(text,function(){
						$("#typeListTable").bootstrapTable("selectPage", 1);
					});
				} else {
					$("#typeModalDelete").modal("hide");
					text= "删除的终端类型失败，请重新操作!";
					UTIL.autoDisappearSwal(text);
				}
			}
		});
	});

});
