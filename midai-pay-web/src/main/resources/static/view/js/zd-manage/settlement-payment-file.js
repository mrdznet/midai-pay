//财务划款界面搜索参数设置
$scdfForm = $("#scdfForm");
function scdfSearch(args){
	var jsonData = UTIL.form2json({
		'form' : "#scdfForm"
	});
	for(var value in jsonData){
		args[value] = jsonData[value];
	}
    return args;
}

$(function(){
	//初始化
	$('[data-toggle="table"]').bootstrapTable();

	//时间插件的初始化              
    $('.my_date').datetimepicker({
        "minView" : "month",
        "language" : 'zh-CN',
        "format" : 'yyyy-mm-dd',
        "autoclose" : true,
        "pickerPosition" : "bottom-left"
    });

	//查询按钮事件
	$("#scdfSearchBtn").on('click',function(){
		$("#scdfTable").bootstrapTable('selectPage',1);

	});
	//重置查询条件
	$("#scdfEmptyBtn").on('click',function(){
		document.getElementById("qfqsForm").reset();
		$("#scdfTable").bootstrapTable('selectPage',1);
	});

	//生成TXT
	var merNoSmall = [];
	$("#txtBtn").on('click',function(){
		if($("#txtPath").val() == ""){
			swal("请先选择通道方");
		}else{

		}
		
	});


})