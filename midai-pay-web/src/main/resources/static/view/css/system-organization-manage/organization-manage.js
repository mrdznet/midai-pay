/**
 * Created by swq on 2016/10/9.
 */


//初始化省市联动
$.ajax({
    url : "/system/address/loadprovince.json",
    method : 'post',
    success : function(res) {
        var $selectList = $('select').filter(
            function() {
                return ($(this).attr('id') || '').toLowerCase()
                        .indexOf('province') >= 0;
            });
        $selectList.empty();
        $selectList.each(function(index) {
            for (var i = 0; i < res.length; i++) {
                var str = $("#optionTmpl").html();
                $(this)
                    .append(
                        str.replace(/\{\{code\}\}/g,
                            res[i].code).replace(
                            /\{\{id\}\}/g, res[i].code)
                            .replace(/\{\{name\}\}/g,
                                res[i].name));
            }
            changeProvince($(this));
            $(this).change(function() {
                var $that = $(this);
                changeProvince($that);
            })
        });
    },
    error : function(xhr, textStatus,
                     errorThrown) {
        alert("亲，初始化省列表出错，请联系管理员！");
    }
});
// 省市区的数据联动
function changeProvince($that, success) {
    var nameStr = $that.attr("id").toLowerCase().replace("province",
        "city");
    var provinceCode = $('option:selected', $that).data('code')
        .toString();
    var provsValue = $that.find("option:selected").data("value");
    var provincialCityFlag = "";
    if (provsValue.substr(provsValue.length - 1, 1) == "市") {
        provincialCityFlag = "area";
        provinceCode = provinceCode.substr(0, provinceCode.length - 3)
            + "100";
    } else {
        provincialCityFlag = "city";
    }
    // console.log(nameStr);
    // 找到和身份对应的城市
    var $selectCity = $('select').filter(function() {
        return ($(this).attr('id') || '').toLowerCase() == nameStr;
    });
    $.ajax({
        url : "/system/address/loadcity/"
        + provinceCode + "/" + provincialCityFlag + ".json",
        method : 'post',
        success : function(res) {
            $selectCity.empty();
            for (var i = 0; i < res.length; i++) {
                var cityStr = $('#cityTmpl').html();

                var cityName = cityStr.replace(/\{\{code\}\}/g,
                    res[i].code).replace(/\{\{name\}\}/g,
                    res[i].name).replace(/\{\{father\}\}/g,
                    provinceCode);
                $selectCity.append(cityName);
            }

            if (success && typeof success === 'function') {
                success();
            }

        }
    })
}
//组织机构填满剩余高度
    (function (){
        var oheight = $('#sidebar-content').height();
        var headerHeight = $('.container-header').height();
        var orgDivHeight = oheight - headerHeight;
        $('.org-aside').css({minHeight:orgDivHeight});
        })();
//    数据对象setting-------树的样式、事件、访问路径等都在这里配置
//    zNodes---------使用Json格式保存了树的所有信息
    function loadZtree(){
        var setting = {
            view:{
                selectedMulti : true
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
                onExpand:onExpand,
                onCollapse:onCollapse,
                onClick:onClick
            }
        };
        var zNodes = null;
        $.ajax({
                async : false,
                method:"post",
                url:'/system/org/loadall'
            }
        ).done(function(response){
            zNodes = response;
            zNodes.unshift({
                "id": 0,
                "level": 0,
                "name": "组织机构",
                "open": true,
                "orgType": 0,
                "pid": 0
            });
        });
        $.fn.zTree.init($("#treeDemo-org"), setting, zNodes);
        //ztree初始化收起展开方向标
        $('#treeDemo-org span.button.ico_close').parent().append('<span class="icon icon-left"></span>');
        $('#treeDemo-org span.button.ico_open').parent().append('<span class="icon icon-down"></span>');
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo-org");  
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
    loadZtree();

//节点展开监听
    function onExpand(event, treeId, treeNode){
   
    //    重新初始化展开折叠状态图标
        // if(!$('#treeDemo-org span.button.ico_close').siblings('.icon')){
        //     $('#treeDemo-org span.button.ico_close').parent().append('<span class="icon icon-left"></span>');
        //     $('#treeDemo-org span.button.ico_open').parent().append('<span class="icon icon-down"></span>');
        // }
   
        var treeObj = $.fn.zTree.getZTreeObj("treeDemo-org");  
        var childNodes = treeObj.transformToArray(treeObj.getNodesByParam("pid", treeNode.id, null));
      //  console.log(childNodes) ;
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
                     $('#treeDemo-org span.button.ico_close').siblings('.icon').attr('class','icon icon-left');
        $('#treeDemo-org span.button.ico_open').siblings('.icon').attr('class','icon icon-down');
        })
    }
//节点收起监听
    function onCollapse(){
    //    重新初始化展开折叠状态图标
        $('#treeDemo-org span.button.ico_close').siblings('.icon').attr('class','icon icon-left');
        $('#treeDemo-org span.button.ico_open').siblings('.icon').attr('class','icon icon-down');
    }
//单击组织节点table展示信息
    var orgValue = 0,
        orglevel = 0;//-----------------------------------点击组织机构树用于保存它的id和level,同时也是table界面节点的pid,默认根目录
    function onClick(event, treeId, treeNode, clickFlag) {
        // console.log(treeNode.orgType);
        if(!(treeNode.orgType == 1 || treeNode.orgType == 2 || treeNode.orgType == 0)){
            $("#org-box").hide();
        }else{
            $("#org-box").show();
            $("#orgHandleTable").load(
            "system-organization-manage-orgTable.html",
            function() {
                orgValue = treeNode.id;
                orglevel = treeNode.level;
                function queryParams(params) {  //配置后台分页参数
                    params.pid =orgValue;
                    return params;
                }
                $('#org-table').bootstrapTable('destroy');
                $('#org-table').bootstrapTable({
                    method:'post',
                    url: "/system/org/querybyp" +
                    "id",
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
                            title: '',
                            field: 'id',
                            visible:false,
                            align: 'left'
                        },
                        {
                            title: '名称',
                            field: 'name',
                            align: 'left'
                        },
                        {
                            title: '操作',
                            field: 'id',
                            align: 'left',
                            formatter:function(value,row,index){
                                //if(row.id)
                                var editBtn = '<a onclick="edit(\''+ row.id + '\')"><img src="../imgs/edit-sign.png" alt=""/></a> ';//-------------------给所有的修改按钮绑定编辑函数
                                return editBtn;
                            }
                        },
                        {
                            title: '删除',
                            field: 'id1',
                            align: 'left',
                            formatter:function(value,row,index){
                                //if(row.id)
                                var delBtn = $("#delCompanyTemp").html().replace("row.id",row.id).replace("row.name",row.name);//-------------------给所有的修改按钮绑定编辑函数
                                return delBtn;
                            }
                        }
                    ]
                });
                if(treeNode.level == 0 ){ //------------------------------根据组织level显示可操作按钮
                    $('#handle').hide();
                    $('#selectHandle').hide();
                    $('#create-company').show();
                    $('#delete-company').show();
                    $('#create-part').hide();
                    $('#create-person').hide();

                }
                else if(treeNode.orgType == 1){
                    $('#handle').show();
                    $('#selectHandle').show();
                    $('#create-company').hide();
                    $('#delete-company').hide();
                    $('#create-part').show();
                    $('#create-person').hide();
                    $('#org-table').bootstrapTable("hideColumn","id1");
                }
                else if(treeNode.orgType == 2){
                    $('#handle').show();
                    $('#selectHandle').show();
                    $('#create-company').hide();
                    $('#delete-company').hide();
                    $('#create-part').show();
                    $('#create-person').show();
                    $('#org-table').bootstrapTable("hideColumn","id1");
                }else{
                    $('#handle').hide();
                    $('#selectHandle').hide();
                    $('#create-company').hide();
                    $('#delete-company').hide();
                    $('#create-part').hide();
                    $('#create-person').hide();
                    $('#org-table').bootstrapTable("hideColumn","id1");
                }
                $("#orgHandleTable").show();
                $("#orgHandle").hide();
            });

        }

    }
    var oTreeNode = {  //----------------------------------------默认显示根目录
        id:0,
        level:0,
        orgType:0
    };
    onClick('','' , oTreeNode,'');//默认显示根目录。根目录组织机构的orgType==0.

//    修改组织机构信息
    var editingMsg; //----------------------------保存正在编辑组织机构信息
    var editingUser; //----------------------------保存正在编辑组织机构user信息
    function edit(id){
        if(id.charAt(0) == 'u'){   //-------------------------修改用户进入请求
            $("#orgHandle").load(
                "system-organization-manage-modifyUser.html",
                function() {           //------------------------请求需要修改组织机构用户的信息
                    var editMsgDtd = $.ajax({
                        method:'post',
                        url:'/system/user/edit/'+id.split('_')[1]
                    });
                    var getOrgParentDtd = getOrgParent();
                    var getRoleListDtd = $.ajax({
                        method:'post',
                        url:'/system/role/list/'+orgValue
                    });
                    $.when(editMsgDtd,getOrgParentDtd,getRoleListDtd)
                        .done(function(promise1,promise2,promise3){
                            editingUser = JSON.parse(promise1[2].responseText);
                            var orgParent = promise2[2].responseText;
                            var roleList = JSON.parse(promise3[2].responseText);
                            var hasRoleList = editingUser.roleIds.split(',');
                            //            展示信息
                            $('#modifyUserParent').val(orgParent);
                            $('#modifyUserLoginName').val(editingUser.loginname);
                            $('#modifyUserUsername').val(editingUser.username);
                            $('#modifyUserMail').val(editingUser.mail);
                            $('#modifyUserMobile').val(editingUser.mobile);
                            $("#modifyUserRoleMsg").find("option").remove();
                            $.each(roleList,function(m,value){
                                var html = "<option value='"+value.id+"' id='roleID"+value.id+"'>"
                                    + value.description
                                    + "</option>";
                                $("#modifyUserRoleMsg").append(html);
                            });
                            for(var i = 0;i<hasRoleList.length;i++){
                                $.each(roleList,function(m,value){
                                    if(value.id == hasRoleList[i]){
                                        $('#modifyUserRoleMsg').find("#roleID"+value.id).attr('selected','selected');
                                    }
                                });
                            }
                            $('#modifyUserRoleMsg').multiSelect({});
                            $("#orgHandleTable").hide();
                            $("#orgHandle").show();
                    });
                });
        }else if(orgValue == 0){   //---------------------修改公司进入请求
            $.ajax({
                method: 'post',
                url: '/system/org/edit/' + id
                }
            ).done(function(response){
                editingMsg = response;
                //        初始化编辑界面信息
                if(editingMsg.provinceCode && editingMsg.cityCode){
                    $('#modifyCompanyModalProvince').val(editingMsg.provinceCode);
                    changeProvince( $('#modifyCompanyModalProvince'), function(){
                        $('#modifyCompanyModalCity').val(editingMsg.cityCode);
                    })

                }
                $('#modifyCompanyModalName').val(editingMsg.organizationName);
            });
            $('#modifyCompanyModal').modal();
        }else{           //-----------------------------修改部门进入请求
            var editMsgDtd  = $.ajax({
                method:'post',
                url:'/system/org/edit/'+id
            });
            var getOrgParentDtd = getOrgParent();
             $.when(editMsgDtd,getOrgParentDtd)
                 .done(function(promise1,promise2){
                     //------------------初始化界面信息
                     editingMsg = JSON.parse(promise1[2].responseText);
                     var orgParent = promise2[2].responseText;
                     $('#modifyPartModalParent').val(orgParent);
                     $('#modifyPartModalName').val(editingMsg.organizationName);
                     if(editingMsg.provinceCode && editingMsg.cityCode){
                         // $('#modifyPartModalProvince').val(editingMsg.provinceCode);
                         // $('#modifyPartModalCity').val(editingMsg.cityCode);
                         // $('#modifyPartModalProvince').change();
                         $('#modifyPartModalProvince').val(editingMsg.provinceCode);
                            changeProvince( $('#modifyPartModalProvince'), function(){
                                $('#modifyPartModalCity').val(editingMsg.cityCode);
                            })

                          $('#modifyCompanyModalProvince').val(editingMsg.provinceCode);
                            changeProvince( $('#modifyCompanyModalProvince'), function(){
                                $('#modifyCompanyModalCity').val(editingMsg.cityCode);
                            })
                     }
                     $('#modifyPartModal').modal();
                 });
        }
    }
    function getOrgParent(){  //---------------------获取隶属组织机构信息
        return $.ajax({
            method:'post',
            url:'/system/org/findorgcatalogue/'+orgValue,
            dataType:'text'
        })
    }

//公司更新验证并提交
    $('#modifyCompanyForm')
        .formValidation({
            message : '此值无效', //此值无效
            excluded : [ "#modifyCompanyModalProvince", "#modifyCompanyModalCity"],
            icon : {
                valid: 'glyphicon',
                invalid: 'glyphicon'
            },
            fields : {
                //公司名称
                'modifyCompanyModalName' : {
                    validators : {
                        notEmpty : {
                            message : '输入不能为空！'
                        }
                    }
                }
            }
        })
        .on(
            'success.form.fv',
            function(e, data) {
                e.preventDefault();
                var data = {
                    "cityCode": $('#modifyCompanyModalCity>option:selected').data('code'),
                    "cityName": $('#modifyCompanyModalCity>option:selected').html(),
                    "level": editingMsg.level,
                    "orgType": editingMsg.orgType,
                    "organizationId": editingMsg.organizationId,
                    "organizationName":$('#modifyCompanyModalName').val(),
                    "parentId": editingMsg.parentId,
                    "provinceCode":$('#modifyCompanyModalProvince>option:selected').data('code'),
                    "provinceName": $('#modifyCompanyModalProvince>option:selected').html()
                };
                $.ajax({
                    method:'post',
                    url:'/system/org/update',
                    data:JSON.stringify(data),
                    dataType:'json'
                }).done(function(response){
                    // swal('','修改成功','success');
                    UTIL.autoDisappearSwal("修改成功");
                    $('#modifyCompanyModal').modal('hide');
                    $('#org-table').bootstrapTable('refresh');
                    loadZtree();
                });
            });
//关闭新增公司重置表单验证
    $('#modifyCompanyModal').on("hide.bs.modal",function(){
        $('#modifyCompanyForm').data('formValidation').resetForm();
        document.getElementById("modifyCompanyForm").reset();
        $('#modifyCompanyProvince').val('110000');
        $('#modifyCompanyProvince').change();
        $('#modifyCompanyCity').val('110101');

    });

//部门更新验证并提交
    $('#modifyPartForm')
        .formValidation({
            message : '此值无效', //此值无效
            excluded : [ "#modifyPartModalProvince", "#modifyPartModalCity"],
            icon : {
                valid: 'glyphicon',
                invalid: 'glyphicon'
            },
            fields : {
                //部门名称
                'modifyPartModalName' : {
                    validators : {
                        notEmpty : {
                            message : '输入不能为空！'
                        }
                    }
                }
            }
        })
        .on(
            'success.form.fv',
            function(e, data) {
                e.preventDefault();
                var data = {
                    "cityCode": $('#modifyPartModalCity>option:selected').data('code'),
                    "cityName": $('#modifyPartModalCity>option:selected').html(),
                    "orgType": editingMsg.orgType,
                    "level": editingMsg.level,
                    "organizationId": editingMsg.organizationId,
                    "organizationName":$('#modifyPartModalName').val(),
                    "parentId": editingMsg.parentId,
                    "provinceCode":$('#modifyPartModalProvince>option:selected').data('code'),
                    "provinceName": $('#modifyPartModalProvince>option:selected').html()
                };
                $.ajax({
                    method:'post',
                    url:'/system/org/update',
                    data:JSON.stringify(data),
                    dataType:'json'
                }).done(function(response){
                    // swal('','修改成功','success');
                    UTIL.autoDisappearSwal("修改成功");
                    $('#modifyPartModal').modal('hide');
                    $('#org-table').bootstrapTable('refresh');
                    loadZtree();
                });
            });
//关闭新增部门重置表单验证
    $('#modifyPartModal').on("hide.bs.modal",function(){
        $('#modifyPartForm').data('formValidation').resetForm();
        document.getElementById("modifyPartForm").reset();
        $('#modifyPartProvince').val('110000');
        $('#modifyPartCity').val('110101');
        $('#modifyPartProvince').change();
    });


//关闭新增公司重置表单验证
    $('#createCompanyModal').on("hide.bs.modal",function(){
        $('#createCompanyForm').data('formValidation').resetForm();
        document.getElementById("createCompanyForm").reset();
        $('#createPartProvince').val('110000');
        $('#createPartCity').val('110101');
        $('#createPartProvince').change();
    });
//新建公司验证并提交
    $('#createCompanyForm')
        .formValidation({
            message : '此值无效', //此值无效
            excluded : [ "#createCompanyProvince", "#createCompanyCity"],
            icon : {
                valid: 'glyphicon',
                invalid: 'glyphicon'
            },
            fields : {
                //公司名称
                'createCompanyName' : {
                    validators : {
                        notEmpty : {
                            message : '输入不能为空！'
                        }
                    }
                }
            }
        })
        .on(
            'success.form.fv',
            function(e, data) {
                e.preventDefault();
                var data = {
                    cityCode: $('#createCompanyCity>option:selected').data('code'),
                    cityName: $('#createCompanyCity>option:selected').html(),
                    level: orglevel+1,
                    orgType:1,
                    organizationName: $('#createCompanyName').val(),
                    parentId: orgValue,
                    provinceCode: $('#createCompanyProvince>option:selected').data('code'),
                    provinceName:  $('#createCompanyProvince>option:selected').data('value')
                };
                $.ajax({
                        method:'post',
                        url:'/system/org/save',
                        data:JSON.stringify(data),
                        dataType:'json'
                    }
                ).done(function(response){
                    // swal('','公司新建成功','success');
                    UTIL.autoDisappearSwal("公司新建成功");
                    $('#createCompanyModal').modal('hide');
                    $('#org-table').bootstrapTable('selectPage',1);
                    loadZtree();
                });
            });

//新建部门弹出操作页面事件绑定
    $('#createPartModal').on("show.bs.modal",function(){
        $('#createPartName').val('');   //-----------------初始化页面
        getOrgParent().done(function(response){
            $('#createPartParent').val(response)
        });
    });
//关闭新增部门重置表单验证
    $('#createPartModal').on("hide.bs.modal",function(){
        $('#createPartForm').data('formValidation').resetForm();
        document.getElementById("createPartForm").reset();
        $('#createPartProvince').val('110000');
        $('#createPartCity').val('110101');
        $('#createPartProvince').change();
    });
//新建部门验证并提交
    $('#createPartForm').formValidation({
            message : '此值无效', //此值无效
            excluded : [ "#createCompanyProvince", "#createCompanyCity"],
            icon : {
                valid: 'glyphicon',
                invalid: 'glyphicon'
            },
            fields : {
                //部门名称
                'createPartName' : {
                    validators : {
                        notEmpty : {
                            message : '输入不能为空！'
                        }
                    }
                }
            }
        })
        .on(
            'success.form.fv',
            function(e, data) {
                e.preventDefault();
                var data = {
                    cityCode: $('#createPartCity>option:selected').data('code'),
                    cityName: $('#createPartCity>option:selected').html(),
                    level: orglevel+1,
                    orgType:2,
                    organizationName: $('#createPartName').val(),
                    parentId: orgValue,
                    provinceCode: $('#createPartProvince>option:selected').data('code'),
                    provinceName:  $('#createPartProvince>option:selected').data('value')
                };
                $.ajax({
                        method:'post',
                        url:'/system/org/save',
                        data:JSON.stringify(data),
                        dataType:'json',
                        error :function(errorThrown){
                            UTIL.clickDisappearSwal(errorThrown.responseText);
                        }
                    }
                ).done(function(response){
                    $('#org-table').bootstrapTable('refresh');//解决初建部门不刷新问题
                    loadZtree();
                    $('#createPartModal').modal('hide');
                    UTIL.autoDisappearSwal("部门新建成功"); 
                });
            });
//新建用户弹出操作页面事件绑定

    $('#createPersonModal').on("show.bs.modal",function(){
        var getOrgParentDtd = getOrgParent();
        var getRoleListDtd = $.ajax({  //-----------------加载可选角色列表
            async:false,
            method:'post',
            url:'/system/role/list/'+orgValue
        });
        $.when(getOrgParentDtd,getRoleListDtd).done(function(promise1,promise2){
            var orgParent = promise1[2].responseText;
            var roleList = JSON.parse(promise2[2].responseText);
            $('#createUserParent').val(orgParent);
            
            $("#createUserRoleMsg").empty();
            $.each(roleList,function(m,value){
                var html = "<option value='"+value.id+"' id='roleID"+value.id+"'>"
                    + value.description
                    + "</option>";
                $("#createUserRoleMsg").append(html);
            });
            $('#createUserRoleMsg').multiSelect({});
            $('#createUserRoleMsg').multiSelect("refresh");
        });
    });
//关闭新增用户重置表单验证
    $('#createPersonModal').on("hide.bs.modal",function(){
        $('#createUserForm').data('formValidation').resetForm();
        document.getElementById("createUserForm").reset();
    });
//新建用户验证并提交
    $('#createUserForm').formValidation({
            message : '此值无效', //此值无效
            excluded : [ "#createCompanyProvince", "#createCompanyCity"],
            icon : {
                valid: 'glyphicon',
                invalid: 'glyphicon'
            },
            fields : {
                //账号
                'createUserLoginName' : {
                    validators : {
                        notEmpty : {
                            message : '输入不能为空！'
                        }
                    }
                },
                //姓名
                'createUserUsername' : {
                    validators : {
                        notEmpty : {
                            message : '输入不能为空！'
                        }
                    }
                },
                //邮箱
                'createUserMail' : {
                    validators : {
                        notEmpty : {
                            message : '输入不能为空！'
                        },
                        regexp: {
                            regexp: /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/,
                            message: '请填写正确的邮箱！'
                        }
                    }
                },
                //手机
                'createUserMobile' : {
                    validators : {
                        notEmpty : {
                            message : '输入不能为空！'
                        },
                        regexp: {
                            regexp: /^1[34578]\d{9}$/,
                            message: '请填写11位正确的手机号码！'
                        },callback: {
                            message: '该手机号已被注册！',
                            callback: function (value, validator) {
                                var reg = /^1[34578]\d{9}$/;
                                var flag = 0;
                                if (reg.test(value)) {
                                    $.ajax({
                                        url: "/system/user/selectbymobile/" + value,
                                        type: "get",
                                        async: false,
                                        success: function (res) {
                                            flag = res;
                                        }
                                    })
                                    if (flag == 0) {
                                        return true;
                                    }
                                    else {
                                        return false;
                                    }
                                }
                                else {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        })
        .on(
            'success.form.fv',
            function(e, data) {
                e.preventDefault();
                var roleIds = $('#createUserRoleMsg option:selected').map(function(){
                    return $(this).val()
                }).get().join(',');
                var data = {
                    "loginname":$('#createUserLoginName').val(),
                    "mail": $('#createUserMail').val(),
                    "mobile": $('#createUserMobile').val(),
                    "orgid": orgValue,
                    "roleIds": roleIds,
                    "username": $('#createUserUsername').val(),
                    "orgNames": $('#createUserParent').val()
                };
                $.ajax({
                    url:'/system/user/save',
                    type:'post',
                    data:JSON.stringify(data),
                    dataType : "text",
                    success: function(res){
                         // swal('用户新建成功');
                        UTIL.autoDisappearSwal("用户新建成功");
                        $('#createPersonModal').modal('hide');
                        $('#org-table').bootstrapTable('refresh');
                        loadZtree();
                    }
                })
            });

function delCompany(id,iname){
    var delName = [];
    delName = iname;
    $("#delCompanyMsg").text(delName);
    $("#delCompanyModal").modal("show");

    $("#sureDelCompanyBtn").click(function(){
      $.ajax({
          url : "../system/org/del/"+id,
          success : function(){
              UTIL.clickDisappearSwal("删除成功");
              $("#delCompanyModal").modal("hide");
              $("#org-table").bootstrapTable("selectPage",1)
              loadZtree();
              
          }
      });
  });
}

/*var hH = $(document).height();
$(".org-aside").css("height",hH);*/