//代理商查询搜索参数设置
function searchData(args){
    args.name=$("#agent-query-search-agcondition  input[name='agentInfo.name']").val().trim();
    args.agentNo=$("#agent-query-search-agcondition  input[name='agentInfo.agentNo']").val().trim();
    args.level=$("#agent-query-search-agcondition  select").val().trim();
    return args;
}

//代理商信息更新修改点击
function agentUpdataDetail(index,row){
     var  agentTemp = [$("#agentQueryUpdata").html().replace("pageurl","agent-update-info.html").replace("row.agentNo",row.agentNo).replace("row.id",row.id).replace("row.markId",row.markId),
                        $("#agentInfoView").html().replace("pageurl","agent-info-view.html").replace("row.agentNo",row.agentNo).replace("row.id",row.id).replace("row.markId",row.markId)
         ].join("");
    return agentTemp;
}


$(function(){
    //初始化
    $('[data-toggle="table"]').bootstrapTable();
    var selectAgent=[];
    var dlscxTableData = $("#agentListTable").bootstrapTable("getData");
    var queryAgentParams = {
        "name" : "",
        "level":'0',
        'agentNo':''
    }
    
    
    //代理商查询按钮事件
    $("#agentSearch").click(function(e){
    	  e.preventDefault();
        queryAgentParams.name=$("#agent-query-search-agcondition  input[name='agentInfo.name']").val().trim();
        queryAgentParams.agentNo=$("#agent-query-search-agcondition  input[name='agentInfo.agentNo']").val().trim();
        queryAgentParams.level=$("#agent-query-search-agcondition  select").val().trim();
        if(dlscxTableData.length === 0 ){
            var randomNum = parseInt(Math.random()*99999);
            var dlscxTableRefreshUrl = $("#agentListTable").data("url") + "?" + randomNum;
            $("#agentListTable").bootstrapTable("refresh", {
                url: dlscxTableRefreshUrl
            });
            dlscxTableData = $("#agentListTable").bootstrapTable("getData");
        }else{
        	$("#agentListTable").bootstrapTable("selectPage",1);
        }      
        
    });

    //清空筛选条件按钮事件
    $("#agentQueryEmptySearch").click(function(){
        queryAgentParams.agentNo="";
        document.getElementById("agent-query-search-agcondition").reset();
        $("#agentListTable").bootstrapTable("refresh");
    });

    //批量修改扣率按钮弹框事件,获取他的id
    $("#lotEditRate").on('click',function(){
       
        selectAgent=$("#agentListTable").bootstrapTable("getSelections");
        if(selectAgent.length<1){
            UTIL.clickDisappearSwal("请先选择一个代理商！");
        //    获取其中的选中的id;
        }else{
            var op=[];
            $.each(selectAgent,function (i,n){
                 op.push(selectAgent[i].id);
            })
            $("#agentsId").val(op);
            $("#edit_descrate_modal").modal("show");
        }
        
    });
    // debugger;
    //批量修改扣率确定事件
    $('#editRateNew').formValidation({
        message : '此值无效', //此值无效
        icon : {
            valid: 'glyphicon',
            invalid: 'glyphicon'
        },
        fields : {
            //借记卡封顶手续费
            'swingCardLimit' : {
                validators : {
                    notEmpty : {
                        message : '借记卡封顶手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
            //借记卡手续费
            'swingCardDebitRate' : {
                validators : {
                    notEmpty : {
                        message : '借记卡手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //贷记卡手续费
            'swingCardCreditRate' : {
                validators : {
                    notEmpty : {
                        message : '贷记卡手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //交易结算手续费
            'swingCardSettleFee' : {
                validators : {
                    notEmpty : {
                        message : '输入不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
            //无卡手续费
            'nonCardCreditRate' : {
                validators : {
                    notEmpty : {
                        message : '无卡手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //借记卡封顶手续费
            'posDebitLimit' : {
                validators : {
                    notEmpty : {
                        message : '借记卡封顶手续费不能为空!'
                    },
                    between: {
                        min:0,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
            //借记卡手续费
            'posDebitRate' : {
                validators : {
                    notEmpty : {
                        message : '借记卡手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //贷记卡手续费
            'posCreditRate' : {
                validators : {
                    notEmpty : {
                        message : '贷记卡手续费输入不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //交易结算手续费
            'posSettleFee' : {
                validators : {
                    notEmpty : {
                        message : '交易结算手续费输入不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
            //微信手续费
            'scanCodeWxRate' : {
                validators : {
                    notEmpty : {
                        message : '微信手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //支付宝手续费
            'scanCodeZfbRate' : {
                validators : {
                    notEmpty : {
                        message : '支付宝手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //银联扫码手续费
            'scanCodeYlRate' : {
                validators : {
                    notEmpty : {
                        message : '银联扫码手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //花呗手续费
            'scanCodeMyhbRate' : {
                validators : {
                    notEmpty : {
                        message : '花呗手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //其他手续费
            'scanCodeOtherRate' : {
                validators : {
                    notEmpty : {
                        message : '其他手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //京东白条手续费
            'scanCodeJdbtRate' : {
                validators : {
                    notEmpty : {
                        message : '京东白条手续费不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100,
                        message: '请输入正确的数值'
                    },
                    regexp : {
                        regexp : /^(((\d|[1-9]\d)(\.\d{1,2})?)|100|100.0|100.00)$/,
                        message : '数值必须为0-100可带两位小数'//
                    }
                }
            },
            //刷卡机具押金
            'terminalSwipeDeposit' : {
                validators : {
                    notEmpty : {
                        message : '刷卡机具押金不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
            //刷卡机具返还条件
            'terminalSwipeDepositReturn' : {
                validators : {
                    notEmpty : {
                        message : '刷卡机具返还条件不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
            //传统pos机具押金
            'terminalPosDeposit' : {
                validators : {
                    notEmpty : {
                        message : '传统pos机具押金不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
            //传统pos机具押金返还条件
            'terminalPosDepositReturn' : {
                validators : {
                    notEmpty : {
                        message : '传统pos机具押金返还条件不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
            //传统pos月押金
            'terminalPosDepositMonthly' : {
                validators : {
                    notEmpty : {
                        message : '传统pos月押金不能为空!'
                    },
                    between: {
                        min:0 ,
                        max: 100000000000000,
                        message: '请输入正确的数值'
                    }
                }
            },
        }
    }).on('success.form.fv', function (e, data) {
        e.preventDefault();
        showLoading();
        // // $("#sureEditRate").off('click').on('click',function(){
        //     var dataRate=[];
        //     var icostRate=parseFloat($("#costRate").val());
        //     var imerchantRate=parseFloat($("#merchantRate").val());
        //     // debugger;
        //     for (var i = 0; i < selectAgent.length; i++) {
        //         var dataO ={};
        //         var selectAgentNo = selectAgent[i].id;
        //         dataO.id=selectAgentNo;
        //         dataO.costRate=icostRate;
        //         dataO.merchantRate=imerchantRate;
        //         dataRate.push(dataO);
        //
        //     }
                var formjson = UTIL.form2json({
                    form: "#editRateNew"
                });
            $("#edit_descrate_modal").modal("hide");//防止连续提交

            $.ajax({
                    type : "post",
                    dataType : "json",
                    contentType : "application/json",
                    url : "../agent/updateRate",
                    data : JSON.stringify(formjson),
                    success : function(res) {
                        hideLoading();
                        if (res.result == "success") {
                            UTIL.clickDisappearSwal("成功修改扣率!",function(){
                                $("#agentListTable").bootstrapTable('refresh');
                            });
                         
                        } else {
                            var text = res.errorMsg;
                            UTIL.clickDisappearSwal(text)
                        }
                    },
                    error : function() {
                        hideLoading();
                        UTIL.clickDisappearSwal("修改扣率失败!")
                    }
                });
            // })
        })


    //批量修改扣率弹框关闭
    $("#closeEditRate").on('click',function(){
        $("#edit_descrate_modal").modal("hide");
    })

    //批量修改扣率模态框关闭后清空事件
    $("#edit_descrate_modal").on('hide.bs.modal', function() {
        $("#editRateNew").find("input").val("");
        // $("#editRateNew").find("input[name='merchantRate']").val("");
        $("#editRateNew").data('formValidation').resetForm();//清空验证
    }) 

    //代理商查询列表下载
    $("#agentListDown").click(function(){
       var selectAgent=$("#agentListTable").bootstrapTable("getSelections");
       if(selectAgent.length<1){
            var data = $("#agentListTable").bootstrapTable("getData");
            if(data.length > 0){
                window.location="../agent/excelExport?name="+queryAgentParams.name+"&level="+ queryAgentParams.level+"&agentNo="+ queryAgentParams.agentNo
            }
            else{
                UTIL.clickDisappearSwal("暂无数据下载!");
            }
       }else{
           var ids = [];
           for (var i = 0; i < selectAgent.length; i++) {
               ids.push(selectAgent[i].id);
           }

           window.location="../agent/excelExportChecked?ids="+ids;

       }
        
    })

    //代理商查询列表点击事件
   /* $('#agentListTable').on(
        'dbl-click-row.bs.table',
            function($element, row, field) {
                // var id = row.id;

                // var iObj = {};
                // iObj.agentNo = row.agentNo;

                // UTIL.openNewPage({
                //     targetUrl : "agent-update-info.html"
                // });

                UTIL.openNewPage({
                            "targetUrl" : "agent-update-info.html",
                            "data" : "agentNo="+row.agentNo+"&agentId="+row.id
                        });

                // 回显更新页面的数据
                // $.ajax({
                //     type:'get',
                //     url:'../agent/getUpdateInfo/?id='+id,
                //     success:function(res){
                //         console.log(res);
                //         $("#reAgent_baseInfo").json2form(res);
                //     }
                // });  


                //加载商户签约扣率表格
                // $.ajax({
                //     type:'post',
                //     url:"../agent/queryAgtMerRateList",
                //     data:JSON.stringify(iObj),      
                //     success:function(res){
                //         $('[data-toggle="table"]').bootstrapTable();       
                //         $("#agentSignTable").bootstrapTable('load',res.rows);
                //         // $("#agentSignTable").bootstrapTable('refresh');
                //     }

                // });   

                    
        }); 
*/

    //新建代理商按钮事件
    $("#newAgent").on('click',function(){
        UTIL.openNewPage({
                targetUrl : "agent-add.html"
            });
    }); 



})