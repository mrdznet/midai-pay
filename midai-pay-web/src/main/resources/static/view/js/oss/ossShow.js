/**
 * Created by abc on 2016/10/13 0013.
 */


/*oss预览下载*/
jQuery.fn.extend({

/*    <div class="showImg" style="position: relative;width:180px;overflow: hidden;">
    <a id="cs-acc-del-1" class="show-btu show-normal" href="javascript:void(0);" style="display: block;"></a>
    <div class="ossfile main-div">
    <img src="../imgs/upload-bg.gif" id="cs-acc-1" width="180" height="156" class="ImgPr image" style="height: 154px !important;">
    </div>
    <span style="display:block; height:24px;text-align: center; font-weight: bold; line-height: 24px;">补充照片</span>
    <input type="hidden" name="imgs[7].type" value="7">
    <input type="hidden" class="fileUrl" name="imgs[7].url">
    </div>*/

    imgPreview: function (obj) {
        var file_img = $(this).find('.ImgPr');     //图片
        var showBtn = $(this).find('.show-btu');    //预览按钮
        var downLoad_btu = $(this).find('.downLoadbBtn'); //下载按钮
        var imgType = $(this).find('.fileType');   //图片类型
        var imgUrl = $(this).find('.fileUrl');     //图片路径
        var imgTitle = $(this).find('span').html();
        var imgUrlVal;


        //初始化
        (function(){
            //判断图片路径是否存在
            var imgUrl_var = imgUrl.val();
            if(imgUrl_var){
                imgUrlVal = imgUrl_var;
                showBtn.show();
                downLoad_btu.show();
            }




        })();

        //下载
        downLoad_btu.click(function(){
                var simpKey = imgUrlVal.substring(imgUrlVal.indexOf('/')+1,imgUrlVal.indexOf('@'));
                $.ajax({
                    type : "POST",
                    dataType : "text",
                    contentType : "application/json",
                    url : "/auth/setOssMeta.json?key="+imgUrlVal,
                    success : function(res) {
                        if(res){
                            window.location = 'http://midaitest001.oss-cn-shanghai.aliyuncs.com/'+res;
                        }else{
                            // swal('文件下载失败');
                            UTIL.clickDisappearSwal("文件下载失败");
                        }
                    }
                });

        });


        //预览按钮悬浮事件
        showBtn.hover(
            function () {
                showBtn.removeClass('show-normal').addClass('show-active');
            },
            function () {
                showBtn.removeClass('show-active').addClass('show-normal');
            }
        );

        //预览图片
        showBtn.click(function(){
            $('#cs-showImg-madal').find('img').attr('src','');
            $('#cs-showImg-madal').find('h4').html('');
            $('#cs-showImg-madal').modal("show");
            $('#cs-showImg-madal').find('img').attr('src','http://midaitest001.oss-cn-shanghai.aliyuncs.com/'+imgUrlVal);
            $('#cs-showImg-madal').find('h4').html(imgTitle);
        });




    }
});


