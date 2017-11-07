/**
 * Created by  on 2016/9/24.
 */
//商户通道小票生成
var tdxpData = "";

function tdxpQueryOptions(argms){
	argms.hostTransSsn = tdxpData;
	return argms;
}
function tdxpAjaxFnc(params){
	$.ajax({
        url:"../trade/review/channelList",
        data:params.data,
        success: function(res){
        	if(tdxpData === ""){
        		res.rows = [];
        		res.total = 0;
        		params.success(res);
        	}
        	else{
        		var rowsChangeData = [];
	            var index = 0;
	            rowsChangeData.length = Math.ceil(res.rows.length/4);
	            for(var i = 0; i < rowsChangeData.length; i++){
	                rowsChangeData[i] = {};
	            }
	            for(var i = 0; i < res.rows.length; i++){
	                index = parseInt(i/4);
	                rowsChangeData[index]["list" + ((i)%4)] = res.rows[i];
	            }
	            res.rows = rowsChangeData;
	            res.total = Math.ceil(res.total);
	           	params.success(res);
        	}
	            
        }
    })
}

// formatter
function tdxpDataFormat0(value, row, index){
	if(row.list0 == undefined){
        return "";
    }
    var str = $("#tdxpTemplate").html();
    var data = row.list0;
    var imgSize = "@550h_310w_1e";
    str = str.replace(/\{\{hostTransSsn\}\}/g,data.hostTransSsn).replace(/\{\{smallImgUrl\}\}/g,data.smallImgUrl + imgSize);
    return str;
}
function tdxpDataFormat1(value, row, index){
	if(row.list1 == undefined){
        return "";
    }
    var str = $("#tdxpTemplate").html();
    var data = row.list1;
    var imgSize = "@550h_310w_1e";
    str = str.replace(/\{\{hostTransSsn\}\}/g,data.hostTransSsn).replace(/\{\{smallImgUrl\}\}/g,data.smallImgUrl + imgSize);
    return str;
}
function tdxpDataFormat2(value, row, index){
	if(row.list2 == undefined){
        return "";
    }
    var str = $("#tdxpTemplate").html();
    var data = row.list2;
    var imgSize = "@550h_310w_1e";
    str = str.replace(/\{\{hostTransSsn\}\}/g,data.hostTransSsn).replace(/\{\{smallImgUrl\}\}/g,data.smallImgUrl + imgSize);
    return str;
}
function tdxpDataFormat3(value, row, index){
	if(row.list3 == undefined){
        return "";
    }
    var str = $("#tdxpTemplate").html();
    var data = row.list3;
    var imgSize = "@550h_310w_1e";
    str = str.replace(/\{\{hostTransSsn\}\}/g,data.hostTransSsn).replace(/\{\{smallImgUrl\}\}/g,data.smallImgUrl + imgSize);
    return str;
}
$(function () {
	$('[data-toggle="table"]').bootstrapTable();
	(function(){
		var $table = $("#tdxpTable");
			
		// 点击生成小票
		$("#generataTicketBtn").click(function(){
			tdxpData = $("#tdxpForm .hostTransSsn").val();
			if(!tdxpData){
				UTIL.clickDisappearSwal("请先输入小票号",function(){
					$table.bootstrapTable('removeAll');
				});
			}
			else{
				$table.bootstrapTable("refresh");
			}
				
		});
		// 点击下载
		$("#tdxpDownBtn").click(function(){
			
		})
	})();


});