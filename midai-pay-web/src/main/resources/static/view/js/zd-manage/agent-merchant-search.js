//查询参数设置
    function agentMecData(args) {
        var jsonData = UTIL.form2json({
         "form":"#agentMecForm"
         });

         for(var value in jsonData){
         args[value] = jsonData[value].trim();
         }
         return args;

    };
//商户信息修改点击
    function agentMecUpdate(index,row){
        var  merchantTemp;
        var  agentNameHb = row.inscode;

        var  tarURl=null;
        var viewTarURl=null;
        if(row.merAuto==0){ //Mpos商户
            merchantTemp = [];
            tarURl="merchant-Mposinfo-update.html";//其实是标准商户
            viewTarURl="merchant-mposinfo-review.html";
        }else{
            merchantTemp = ['<a href="javascript:void(0)" style="margin-left: 10px;"title="二维码查看" onclick="ag_fu(\'' + row.mercNo + "," + row.qrCodeFlag + '\')">',
                '<img src="../imgs/img_code.png"  alt="" />',
                '</a>']
            tarURl="merchant-Normalinfo-update.html";
            viewTarURl="merchant-normalinfo-review.html";
        }
    /*    if(row.state == 4){ //审核通过的

            if(row.markId==1){
                merchantTemp = [$("#AMUpdate").html().replace("pageurl",tarURl).replace("row.mercNo",row.mercNo).replace("row.agentId",row.agentId),
                    $("#unAMDetailCan").html().replace("pageurl",viewTarURl).replace("row.mercNo",row.mercNo).replace("row.agentId",row.agentId)
                ].join("");
            }else{
                merchantTemp = [$("#AMUpdate").html().replace("pageurl",tarURl).replace("row.mercNo",row.mercNo).replace("row.agentId",row.agentId),
                                $("#unAMDetailCan").html().replace("pageurl",viewTarURl).replace("row.mercNo",row.mercNo).replace("row.mercId",row.mercId),
                                ].join("");
            }

            return merchantTemp;
        }else{
           if(agentNameHb=="101003"||""){
                merchantTemp = [$("#unAMUpdate").html(),
                                //$("#unAMUpdateHb").html(),
                                $("#unAMDetailCan").html().replace("pageurl",viewTarURl).replace("row.mercNo",row.mercNo).replace("row.mercId",row.mercId),
                                ].join("");
            }else{
                merchantTemp = [$("#unAMUpdate").html(),
                                $("#unAMDetailCan").html().replace("pageurl",viewTarURl).replace("row.mercNo",row.mercNo).replace("row.mercId",row.mercId),
                                ].join("");
            }
            return merchantTemp;
        }*/

        if(row.state == 4&&row.markId==1){ //审核通过的

                merchantTemp = [$("#AMUpdate").html().replace("pageurl",tarURl).replace("row.mercNo",row.mercNo).replace("row.agentId",row.agentId),
                    $("#unAMDetailCan").html().replace("pageurl",viewTarURl).replace("row.mercNo",row.mercNo).replace("row.agentId",row.agentId)
                ].concat(merchantTemp).join("");

            return merchantTemp;
        }else{
                merchantTemp = [$("#unAMUpdate").html(),
                    $("#unAMDetailCan").html().replace("pageurl",viewTarURl).replace("row.mercNo",row.mercNo).replace("row.mercId",row.mercId),
                ].concat(merchantTemp).join("");
            return merchantTemp;
        }
        
    }

//审核状态类型格式化
    function stateFmt(value, row, index) {
        if (value == 0) {
            return "申请中";

        }else if (value == 1) {
            return "初审中";

        }
        else if (value == 2) {
            return "复审中";

        } else if (value == 3) {
            return "初审未通过";

        } else if (value == 4) {
            return "审核通过";

        }
        else if (value == 5) {
            return "更新已提交";

        }
        else if (value == 6) {
            return "更新审核中";

        }
        else if (value == 7) {
            return "更新审核未通过";

        }
    }

    $(function(){
        //新建商户页面跳转
        $('#createAMBtn2').click(function(){
            UTIL.openNewPage({
                "targetUrl" : "merchant-creatApply_Hand.html"
            });
        });

        $('#createAMBtn_Mpos2').click(function(){
            UTIL.openNewPage({
                "targetUrl" : "merchant-creatApply_NoHand.html"
            });
        });
        // 初始化table
        $('[data-toggle="table"]').bootstrapTable();
          var  dlsshTableData = $("#agentMecTable").bootstrapTable("getData");
        // 点击查询
        $("#searchAMBtn").click(function(e) {
            e.preventDefault();
            if(dlsshTableData.length === 0 ){
                var randomNum = parseInt(Math.random()*99999);
                var dlsshTableRefreshUrl = $("#agentMecTable").data("url") + "?" + randomNum;
                $("#agentMecTable").bootstrapTable("refresh", {
                    url: dlsshTableRefreshUrl
                });
                dlsshTableData = $("#agentMecTable").bootstrapTable("getData");
            }
       	 else{
       		 $("#agentMecTable").bootstrapTable("selectPage", 1);
       	 }
            return false;
        });

        // 重置搜索框
        $("#emptyAMBtn").click(function(e) {
            e.preventDefault();
            document.getElementById("agentMecForm").reset();
            $("#agentMecTable").bootstrapTable("selectPage", 1);
            return false;

        });
        //    二维码模态框
        function ag_fu(rp,c) {
            showLoading();
            var arry_box = rp.split(",");
            var data_rp = arry_box[0];
            var data_code = arry_box[1]
            if (data_code == 0) {
                UTIL.clickDisappearSwal("没有报备的通道，请联系管理员");
            } else {
                $.ajax({
                    url: "/customer/showCustomerQrCode/" + data_rp,
                    type: 'POST',
                    success: function (res) {
                        hideLoading();
                        $("#file_img_box").val(res.imgpath)
                        var img_url = OSSHOST + res.imgpath
                        $("#mer_imgcode").attr('src', img_url)
                        $("#img_uplod_updown").modal('show')
                    },
                    error: function (res) {
                        UTIL.clickDisappearSwal("获取二维码失败，请联系管理员");
                    }
                });
            }
        }
        $("#agent_img_codes").on('click', function () {
            var box_row = $("#agentMecTable").bootstrapTable("getSelections");
            var all_box_row = [];
            if (box_row.length > 0) {
                $.each(box_row, function (i, v) {
                    if (v.qrCodeAddr != null) {
                        all_box_row.push(v.qrCodeAddr);
                    }
                })
                if (all_box_row.length > 0) {
                    $.ajax({
                        type: "POST",
                        dataType: "text",
                        contentType: "application/json",
                        url: "/auth/oss-zip-file?key=" + all_box_row,
                        success: function (res) {
                            var zipPaths = JSON.parse(res).zipPath;
                            if (res) {
                                window.location = OSSHOST + zipPaths;
                            } else {
                                UTIL.clickDisappearSwal("文件下载失败");
                            }
                        }
                    });
                } else {
                    UTIL.clickDisappearSwal("选中的商户还没有二维码");
                }

            } else {
                UTIL.clickDisappearSwal("请选择商户");
            }

        })
        //下载
        $("#agent_profit_btn_ig").click(function () {
            var simpKey = $("#file_img_box").val();
            $.ajax({
                type: "POST",
                dataType: "text",
                contentType: "application/json",
                url: "/auth/setOssMeta.json?key=" + simpKey,
                success: function (res) {
                    if (res) {
                        window.location = OSSHOST + res;
                    } else {
                        UTIL.clickDisappearSwal("文件下载失败");
                    }
                }
            });
        });
        //excel下载
        $("#downAMBtn").on('click',function() {

            var data = $("#agentMecTable").bootstrapTable("getData");
            if(data.length > 0){
                var $form = $("#agentMecForm");

                var imercName = $form.find("input[name='mercName']").val();
                var imercNo = $form.find("input[name='mercNo']").val();
                //var imobile = $form.find("input[name='mobile']").val();
                //var iagentName = $form.find("input[name='agentName']").val();
                var istate = $form.find("select[name='state']").val();
                var ilevel = $form.find("input[name='level']").val();

                window.location = "../customer/agentlistExcelExport?mercName="+ imercName
                    +"&mercNo="+imercNo
                    +"&state="+istate
                    +"&level="+ilevel;
            }
            else{
                UTIL.clickDisappearSwal("暂无数据下载!");
            }
        });
    });


