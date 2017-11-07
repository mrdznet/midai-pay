jQuery.fn.extend({

    uploadPreview: function (obj) {
        var readOnly = $(this).data('readonly') || false;
        var self_this = $(this);
        var host = '';
        var policyBase64 = '';
        var accessid = '';
        var signature = '';
        var expire = 0;
        var key = '';
        var fileUrl = $(this).find('.fileUrl'); //隐藏域值
        var file_obj = $(this).find('.file');      //文件域
        var file_id =   $(this).find('.file').attr('id');
        var file_title = $(this).find('span').html(); //文件标题

        var file_img = $(this).find('.ImgPr');     //图片
        var progress_bar = $(this).find('.progress-bar'); //进度条
        var upload_btu = $(this).find('.showbtu'); //上传按钮
        var upload_btu_id = '#'+upload_btu.attr('id');

        var download_btu = $(this).find('.downloadbtn'); //下载按钮
        var download_btu_id = '#'+download_btu.attr('id');


        var del_btu = $(this).find('.del-btu');    //删除按钮
        var del_btu_id = '#'+del_btu.attr('id');

        var show_btu = $(this).find('.show-btu');    //预览按钮
        var show_btu_id = '#'+show_btu.attr('id');

        var showmodal_id = $(this).data('showmodal') || '';



        var img_src;  //图片真实路径
        var isDelFlag = false;

        var baseHtml;
        var g_object_name = '';
        var filename = '';
        var now = timestamp = Date.parse(new Date()) / 1000;


        var ImgType = ["gif", "jpeg", "jpg", "bmp", "png"]; //可以上传的图片类型
        var fileSize = 2048;
        var _self = file_obj;

        uploadInit = function(){
            baseHtml = self_this.html();

            //判断隐藏域是否有值
            if(fileUrl.val() != null && fileUrl.val() != '' && fileUrl.val() != 'undefined' && isDelFlag == false ){
                if(readOnly == true){
                    $('#'+file_id).attr("disabled", "disabled");
                    //显示预览、下载按钮
                    show_btu.show();
                    download_btu.show();
                    var randomNum1 = Math.ceil(Math.random()*1000);
                    file_img.attr('src',OSSHOSTIMG+fileUrl.val()+'@87h_87w_1e?v=' + randomNum1);
                }else{
                    show_btu.show();
                    download_btu.show();
                    var randomNum2 = Math.ceil(Math.random()*1000);
                    file_img.attr('src',OSSHOSTIMG+fileUrl.val()+'@87h_87w_1e?v=' + randomNum2);
                }
            }else{
                show_btu.hide();
                download_btu.hide();
            }
        };
        uploadInit();

        _self.getObjectURL = function (file) {
            var url = null;
            if (window.createObjectURL != undefined) {
                url = window.createObjectURL(file);
            } else if (window.URL != undefined) {
                url = window.URL.createObjectURL(file);
            } else if (window.webkitURL != undefined) {
                url = window.webkitURL.createObjectURL(file);
            }
            return url
        };

        //file按钮change事件
        file_obj.change(function () {

            if (this.value) {
                filename = this.value;
                if (!RegExp("\.(" + ImgType.join("|") + ")$", "i").test(this.value.toLowerCase())) {
                    // swal("选择文件错误,图片类型必须是" + ImgType.join("，") + "中的一种");
                     UTIL.clickDisappearSwal("选择文件错误,图片类型必须是" + ImgType.join("，") + "中的一种");
                    file_obj.value = "";
                    return false
                }
                if ($.support) {
                    try {
                        img_src =  _self.getObjectURL(this.files[0]);
                        file_img.attr('src', img_src);
                    } catch (e) {
                        var src = "";
                        var div = file_img.parent("div")[0];
                        _self.select();
                        if (top != self) {
                            window.parent.document.body.focus();
                        } else {
                            _self.blur();
                        }
                        img_src = document.selection.createRange().text;

                        document.selection.empty();
                        file_img.hide();
                        file_img.parent("div").css({
                            'filter': 'progid:DXImageTransform.Microsoft.AlphaImageLoader(sizingMethod=scale)',
                            'width': 180 + 'px',
                            'height': 154 + 'px'
                        });
                        div.filters.item("DXImageTransform.Microsoft.AlphaImageLoader").src = img_src;
                    }
                } else {
                    img_src =  _self.getObjectURL(this.files[0]);
                    file_img.attr('src',img_src);
                }

                show_btu.hide();
                download_btu.hide();
                del_btu.hide();

                upload_btu.show();


                upload_btu.show();  //显示上传按钮
                progress_bar.css("width","0px");
                $('#progress-div').show();

                //1,获取上传凭证
                $.ajax({
                    type : "get",
                    dataType : "json",
                    contentType : "application/json",
                    url : "/auth/oss-upload/merchant.json",
                    async : false,
                    success : function(res) {

                        host = res.host;
                        policyBase64 = res.policy;
                        accessid = res.accessid;
                        signature = res.signature;
                        expire = parseInt(res.expire);
                        key = res.dir;

                        g_object_name = key;
                        if (filename != '') {
                            suffix = get_suffix(filename);
                            calculate_object_name(filename);
                            key = g_object_name;
                        }
                        console.log(host + policyBase64 + accessid + signature + expire + key);
                    },
                    error : function(){
                       UTIL.clickDisappearSwal("获取OSS上传凭证失败！");
                    }
                });
            }
        });


        //下载文件
        $(document).on('click',download_btu_id,function(){
            var fileUrl_val = fileUrl.val();
            var simpKey = fileUrl_val.substring(fileUrl_val.indexOf('/')+1,fileUrl_val.indexOf('@'));
            $.ajax({
                type : "POST",
                dataType : "text",
                contentType : "application/json",
                url : "/auth/setOssMeta.json?key="+fileUrl_val,
                success : function(res) {
                    if(res){
                        window.location = OSSHOST+res;
                    }else{
                        // swal('文件下载失败');
                        UTIL.clickDisappearSwal("文件下载失败");
                    }
                }
            });
        });

        //上传文件
        upload_btu.click(function(){
            //表单异步提交
            var glo_image_data = {
                    OSSAccessKeyId:accessid,
                    policy:policyBase64,
                    signature:signature,
                    success_action_status:'200',
                    key:key
                },
                glo_image_upload_url = OSSHOST.substring(0, OSSHOST.length-1);

            //oss文件文件上传
            doUploadImage(glo_image_upload_url,glo_image_data,file_id);

        });

        //删除按钮悬浮事件
        del_btu.hover(
            function () {
                del_btu.removeClass('del-normal').addClass('del-active');
            },
            function () {
                del_btu.removeClass('del-active').addClass('del-normal');
            }
        );

        //删除文件
        $(document).on('click',del_btu_id,function(){
            if(key.length === 0){
                 self_this.uploadPreview();
                return;
            }
            var simpKey = key.substring(key.indexOf('/')+1,key.length)+'@87h_87w_1e';
            $.ajax({
                type : "POST",
                dataType : "json",
                contentType : "application/json",
                url : "/auth/oss-del-list/"+simpKey+".json",
                success : function(res) {
                    if(res){
                        isDelFlag = true;
                        self_this.html(baseHtml);
                        self_this.find("input[type='file']").prop("disabled", false);
                        self_this.find(".showbtu").prop("disabled", false);
                        self_this.find('.fileUrl').val('');
                        self_this.find('img').attr("src","../imgs/upload-bg.gif");
                        self_this.uploadPreview();

                    }

                }
            });

        });

        //预览按钮悬浮事件
        show_btu.hover(
            function () {
                show_btu.removeClass('show-normal').addClass('show-active');
            },
            function () {
                show_btu.removeClass('show-active').addClass('show-normal');
            }
        );

        //预览图片
        $(document).on('click',show_btu_id,function(){
            ROTATEIMGFLAG = 0;
            $('#'+showmodal_id).find('img').css({'transform': 'rotate(' + ROTATEIMGFLAG*90 + 'deg)'});
             $('#'+showmodal_id).find('img').attr('src','');
             $('#'+showmodal_id).find('h4').html('');
             $('#'+showmodal_id).modal("show");
             $('#'+showmodal_id).find('img').attr('src',OSSHOST+fileUrl.val());
            $('#'+showmodal_id).find('h4').html(file_title);
        });


        function random_string(len) {
            len = len || 32;
            var chars = 'ABCDEFGHJKMNPQRSTWXYZabcdefhijkmnprstwxyz2345678';
            var maxPos = chars.length;
            var pwd = '';
            for (i = 0; i < len; i++) {
                pwd += chars.charAt(Math.floor(Math.random() * maxPos));
            }
            return pwd;
        }

        //获取文件后缀名
        function get_suffix(filename) {
            pos = filename.lastIndexOf('.')
            suffix = '';
            if (pos != -1) {
                suffix = filename.substring(pos);
            }
            return suffix;
        }

        //获取最新生成的文件名
        function calculate_object_name(filename)
        {
            suffix = get_suffix(filename);
            g_object_name = key +queryNowDate(new Date()) +random_string(10) + suffix;

        }

        //获取当前日期的年月日字符串
        function queryNowDate(now){
            var year = now.getFullYear();
            var month =now.getMonth()+1;
            var day = now.getDate();
            return year+'-'+month+'-'+day;
        }

        //oss postObject上传
        function doUploadImage(url,data,fileId){
            var oMyForm = new FormData();
            for(var field_name in data){
                oMyForm.append(field_name,data[field_name]);
            }

            oMyForm.append("file", document.getElementById(fileId).files[0]);

            var oReq = new XMLHttpRequest();
            //上传进度监听
            oReq.upload.onprogress = function (e) {
                if(e.type=='progress'){
                    var percent = Math.round(e.loaded/e.total*100,2)+'%';  //获取oss上传进度
                    progress_bar.parent().parent().show();  //显示进度条

                    progress_bar.css("width","0px");
                    progress_bar.css("width",percent);
                    if(percent == '100%'){
                        del_btu.show();     //显示删除按钮
                        UTIL.clickDisappearSwal('上传成功！');
                    }
                }
            };
            //上传结果
            oReq.onreadystatechange = function(e){
                if(oReq.readyState == 4){
                    if(oReq.status==200 || oReq.status==0){
                        ossCurrentUpDelBtn = del_btu_id;
                        try{
                            $("#imgTest").attr("src", OSSHOSTIMG+key+'@87h_87w_1e');
                        }catch(e){
                            $(del_btu_id).click();
                        }

                        del_btu.show();     //显示删除按钮
                        fileUrl.val('').val(key);
                         oReq = null;
                    }else{
                        UTIL.clickDisappearSwal(file_title+'上传失败');
                        del_btu.hide();
                        oReq = null;
                    }

                }
            };
            oReq.error = function(err){
                if (err.code == -600) {
                     UTIL.clickDisappearSwal('上传的文件过大');
                }
                else if (err.code == -601) {
                     UTIL.clickDisappearSwal('文件类型不合法');
                }
                else if (err.code == -602) {
                     UTIL.clickDisappearSwal('该文件已上传');
                }
                else {
                     UTIL.clickDisappearSwal(err.response);
                }
            };
            oReq.open("POST", url);
            oReq.send(oMyForm);
           
        }
    }
});