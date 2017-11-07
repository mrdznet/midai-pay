/**
 * Created by swq on 2016/10/9.
 */
//组织机构填满剩余高度
(function (){
    var oheight = $('#sidebar-content').height();
    var headerHeight = $('.container-header').height();
    var orgDivHeight = oheight - headerHeight;
    $('.role-aside').css({minHeight:orgDivHeight});
})();
//     数据对象setting-------树的样式、事件、访问路径等都在这里配置
//    zTreeNodes---------使用Json格式保存了树的所有信息
function loadRoleZtree(){
    var setting = {
        view:{
            selectedMulti : false
        },
        data:{
            simpleData : {
                enable : true,
                idKey : 'id',
                pIdKey : 'pid'
            }
        },
        edit:{
            enable:true
        },
        callback:{
            onExpand:onExpandRole,
            onCollapse:onCollapseRole,
            onClick:onClickRole
        }
    };
    var zNodes = null;
    $.ajax({
            async : false,
            method:"post",
            url:'/system/role/loadall'
        }
    ).done(function(response){
        zNodes = response;
        zNodes.unshift({
            "id": 0,
            level:-1,
            "name": "角色组织",
            orgType:0,
            "open": true,
            "pid": 0
        });
    });
    $.fn.zTree.init($("#treeDemo-role"), setting, zNodes);

    //ztree初始化收起展开方向标
    $('#treeDemo-role span.button.ico_close').parent().append('<span class="icon icon-left"></span>');
    $('#treeDemo-role span.button.ico_open').parent().append('<span class="icon icon-down"></span>');

        var treeObj = $.fn.zTree.getZTreeObj("treeDemo-role");  
        var childNodes = treeObj.transformToArray(treeObj.getNodes());
     //   console.log(childNodes) ;
        $.each(childNodes,function(k,v){
            var jqueryId = v.tId + '_span'; //tId是li的id  ,  拼接后获取的是文字的那个节点
                switch(v.orgType){
                    case 0 ://根目录
                             // $('#'+jqueryId).before('<img src="../imgs/icon-edit.png" class=""/>');
                             break;
                    case 1 : //公司
                            if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-company.png" style="margin-right: 10px;" />');
                            }
                            break;
                    case 2 ://部门
                           if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-part.png" style="margin-right: 10px;" />');
                            }
                            break;
                    case 3 ://人
                            if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-agent.png" style="margin-right: 10px;" />');
                            }
                            break;
                    case 4 ://人
                            if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-merchant.png" style="margin-right: 10px;" />');
                            }
                            break;
                    default:
                        if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-user.png" style="margin-right: 10px;" />');
                            }
                        break;
                }
                
        })
}
loadRoleZtree();



//节点展开监听
function onExpandRole(event, treeId, treeNode){
//    重新初始化展开折叠状态图标
    
    var treeObj = $.fn.zTree.getZTreeObj("treeDemo-role");  
        var childNodes = treeObj.transformToArray(treeObj.getNodesByParam("pid", treeNode.id, null));
     //   console.log(childNodes) ;
        $.each(childNodes,function(k,v){ 
            var jqueryId = v.tId + '_span'; //tId是li的id  ,  拼接后获取的是文字的那个节点
            if(!$('#'+jqueryId).next().is('.icon')){
                $('#'+jqueryId).parent().append('<span class="icon"></span>');
            }
                switch(v.orgType){
                    case 0 ://根目录
                             // $('#'+jqueryId).before('<img src="../imgs/icon-edit.png" class=""/>');
                             break;
                    case 1 : //公司
                            if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-company.png" style="margin-right: 10px;" />');
                            }
                            break;
                    case 2 ://部门
                           if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-part.png" style="margin-right: 10px;" />');
                            }
                            break;
                    case 3 ://人
                            if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-agent.png" style="margin-right: 10px;" />');
                            }
                            break;
                    case 4 ://人
                            if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-merchant.png" style="margin-right: 10px;" />');
                            }
                            break;
                    default:
                        if(!$('#'+jqueryId).prev().is('img')){
                                $('#'+jqueryId).before('<img src="../imgs/icon-user.png" style="margin-right: 10px;" />');
                            }
                        break;
                }

            $('#treeDemo-role span.button.ico_close').siblings('.icon').attr('class','icon icon-left');
            $('#treeDemo-role span.button.ico_open').siblings('.icon').attr('class','icon icon-down');

            })
}

//节点收起监听
function onCollapseRole(){
//    重新初始化展开折叠状态图标
    $('#treeDemo-role span.button.ico_close').siblings('.icon').attr('class','icon icon-left');
    $('#treeDemo-role span.button.ico_open').siblings('.icon').attr('class','icon icon-down');
}
//单击组织节点table展示信息
var roleValue ; //-----------------------------------点击角色组织树用于保存它的id
function onClickRole(event, treeId, treeNode, clickFlag) {
    // console.log(treeNode.orgType);
    if(!(treeNode.orgType == 1 || treeNode.orgType == 2)){
        $("#role-box").hide();
    }else{
        $("#role-box").show();
        $("#roleHandleTable").load(
        "system-role-manage-table.html",
        function() {
            roleValue = treeNode.id;
            function queryParams(params) {  //配置后台分页参数
                params.pid =roleValue;
                return params;
            }
            $('#roleTable').bootstrapTable('destroy');
            $('#roleTable').bootstrapTable({
                method:'post',
                url: "/system/role/querybypid",
                dataType: "json",
                striped: true,//隔行变色
                cache: false, // 不使用缓存
                pagination: true, //分页
                pageSize: 10,//每页行数
                singleSelect: false,
                queryParams:queryParams,
                sidePagination: "server", //服务端处理分页
                paginationPreText: "上一页",
                paginationNextText : "下一页",
                columns: [
                    {
                        title: '',
                        checkbox:true,
                        align: 'center'
                    },
                    {
                        title: '名称',
                        field: 'name',
                        align: 'left'
                    },
                    {
                        title: '编辑',
                        field: 'id',
                        align: 'left',
                        formatter:function(value,row,index){
                            var eidtBtn = '<a onclick="editRole(\''+ row.id + '\')"><img src="../imgs/edit-sign.png" alt=""/></a> ';//-------------------给所有的修改按钮绑定编辑函数
                            return eidtBtn;
                        }
                    },
                    {
                        title: '删除',
                        field: 'id1',
                        align: 'left',
                        formatter:function(value,row,index){
                            var delBtn = $("#delRoleTemp").html().replace("row.id",row.id).replace("row.name",row.name);//-------------------给所有的修改按钮绑定编辑函数
                            return delBtn;
                        }
                    },
                ]
            });
           //  console.log(treeNode);
             if(!(treeNode.orgType == 1 || treeNode.orgType == 2)){
                    $('#createNewRole').hide();
             }else{
                $('#createNewRole').show();
             }

            $("#roleHandleTable").show();
            $("#roleHandle").hide();
        });

    }
    

}
//----------------默认显示根目录信息
    var oRoleTreeNode = {id:0};
    onClickRole('','',oRoleTreeNode,'');

var editingRole;  //----------------- 用于保存正在编辑的角色信息
function rolePageInit(){  //---------------------生成角色编辑模板
    $.ajax({
        method:'post',
        url:'/system/module/load',
        async:false
    }).done(function(response){ //----------------------------拼接菜单权限页面模块
        var module_html = '';
        $.each(response,function(key,value){
            module_html += "<div class='panel panel-default'><div class='panel-heading'>";
            module_html += "<input type='checkbox' value='"+value.moduleId+"'><label>"+value.moduleName+"</label></div><div class='panel-body'>";
            var module_html_child = '';
            $.each(value.list,function(k,v){
                module_html_child += "<div class='col-lg-3'><input type='checkbox' value='"+ v.moduleId+"'><label >"+ v.moduleName+"</label></div>";
            });
            module_html += module_html_child;
            module_html += "</div></div>";
        });
        $('#module').append(module_html);
        $("#module").find(".panel").each(function(){
            var $that = $(this);

            var $checkAll = $that.find(".panel-heading input[type='checkbox']");
            var $subCheck = $that.find(".panel-body input[type='checkbox']");
            var curLength = $that.find(".panel-body input[type='checkbox']").length;

            //父联动子
            $checkAll.click(function(){
                if($(this).prop("checked")){
                    $subCheck.prop("checked",true);
                }else{
                    $subCheck.prop("checked",false);
                }
            });

            //子联动父
            $subCheck.click(function(){
                $checkAll.prop("checked",curLength == $that.find(".panel-body input[type='checkbox']:checked").length?true:false);
            });

        });
    });
    $.ajax({
        method:'post',
        url:'/system/process/load',
        async:false
    }).done(function(response){ //-------------------拼接工作流页面模块
        var process_html ='' ;
        $.each(response,function(key,value){
            process_html += "<div class='panel panel-default'><div class='panel-heading'>";
            process_html += " <input type='checkbox' class='processNotNeed'> <label>"+value.name+"</label></div><div class='panel-body'>";
            var process_html_child = '';
            $.each(value.list,function(k,v){
                process_html_child += "<div class='col-lg-3'><input type='checkbox' value='"+ v.id+"'><label >"+ v.name+"</label></div>";
            });
            process_html += process_html_child;
            process_html += "</div></div>";
        });
        $('#process_executor_config').append(process_html);
        $("#process_executor_config").find(".panel").each(function(){
            var $that = $(this);

            var $checkAll = $that.find(".panel-heading input[type='checkbox']");
            var $subCheck = $that.find(".panel-body input[type='checkbox']");
            var curLength = $that.find(".panel-body input[type='checkbox']").length;

            //父联动子
            $checkAll.click(function(){
                if($(this).prop("checked")){
                    $subCheck.prop("checked",true);
                }else{
                    $subCheck.prop("checked",false);
                }
            });

            //子联动父
            $subCheck.click(function(){
                $checkAll.prop("checked",curLength == $that.find(".panel-body input[type='checkbox']:checked").length?true:false);
            });


        });
    });

}
//生成创建角色页面
$(document).on('click','#createNewRole',function(){
    $("#roleHandle").load(
        "system-role-manage-create.html",
        function() {
            rolePageInit();
            
            $("#roleHandleTable").hide();
            $("#roleHandle").show();

        /*    var hH = $(document).height();
            $(".role-aside").css("height",hH);*/
        })
});

function delRole(id,iname){
    var delName = [];
    delName = iname;
    $("#delRoleMsg").text(delName);
    $("#delRoleModal").modal("show");

    $("#sureDelRoleBtn").off("click").on('click',function(){
      $.ajax({
          url : "../system/role/del/"+id,
          success : function(){
            $("#delRoleModal").modal("hide");
            $("#roleTable").bootstrapTable("selectPage",1)
            loadRoleZtree();  
            UTIL.clickDisappearSwal("删除成功");
              
          }
      });
  });
}
function editRole(id){  //-----------------------回显修改用户信息
    $("#roleHandle").load(
        "system-role-manage-modify.html",
        function() {
            rolePageInit();
            $.ajax({
                method:'post',
                url:'/system/role/edit/'+id+'/'+roleValue,
                async:false
            }).done(function(response){
                editingRole = response;
                $('#roleDescription').val(response.description);
                $('#roleName').val(response.rolename);
                $("input[name='roleType']").eq(response.system).click();
            //    console.log($('#process_executor_config input:checkbox'));
                var smvIds = response.smvIds.split(',');
                $.each($('#module input:checkbox'),function(key,value){
                  //  console.log(value);
                    if(smvIds.indexOf($(value).val()) != -1){
                        $(value).attr('checked','true')
                    }
                });
                var specIds = response.specIds.split(',');
                $.each($('#process_executor_config input:checkbox'),function(key,value){
                    if(specIds.indexOf($(value).val()) != -1){
                        $(value).attr('checked','true');
                    }
                });
                $("#roleHandleTable").hide();
                $("#roleHandle").show();

           /*     var hH = $(document).height();
                $(".role-aside").css("height",hH);*/
                
            }).fail(function(response){
                // swal('用户数据请求失败');
                UTIL.clickDisappearSwal("用户数据请求失败");
            });
        });
}
//-----------------------关闭新建更新角色页面
$(document).on('click','#close_rolePage',function(){
    $("#roleHandleTable").show();
    $("#roleHandle").hide();
});

/*var hH = $(document).height();
$(".role-aside").css("height",hH);*/

