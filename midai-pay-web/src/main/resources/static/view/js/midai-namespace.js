//全局空间
var com={};

com.namespace=function(str){
	var arr=str.split("."),o=com;
	for(var i=(arr[0]=="com")?1:0;i<arr.length;i++){
		o[arr[i]]=o[arr[i]]||{};
		o=o[arr[i]];
	}
}  

//注册名称空间

com.namespace("com.midai.page");


com.midai.page.util={
		initToggle:function(){
			  //单选按钮变化时对应的效果
			 // $('[data-toggle="radio"]').radiocheck();
			  $('input:radio').change(function(event){
			    var $target = $(this);
			    var $showTarget = $target.data("showClass");
			    var $hideTarget = $target.data("hideClass");
			    $($hideTarget).find("input").prop("disabled",true);
			    $($hideTarget).find("select").prop("disabled",true);
			     $($hideTarget).hide();
			     $($showTarget).show();
			     $($showTarget).find("input").prop("disabled",false);
			     $($showTarget).find("select").prop("disabled",false);
			     
			  })
			//下拉框变化时对应的效果
			  $('select').change(function(){
			    if($('option', this).attr('data-value')){
			      var $value = $('option:selected', this).attr('data-value');
			      $($value).hide();
			      if($value.match(/show/g)){
			        var num = $value.indexOf(".",1);
			        var $fnum = $value.substring(0,num);
			        $($fnum).hide();
			        $($value).show();
			      }
			    }
			    else{
			      return;
			    }
			    
			  })
		},
		// 表单提交序列化
		 form2json:function(params){
			 var selector=params.form;
			 var values= $(selector).serializeArray();
			 var obj={};
			 for (var index = 0; index < values.length; ++index)    
             {    
                var temp=obj; //上一级     
                var n=values[index].name;  
                if(n.indexOf(".")>-1){  
                    var arr=n.split(".");  
                    for(var i=0;i<arr.length-1;i++){     
                        if(arr[i].indexOf("[")>-1){  
                            var a=arr[i].substring(0,arr[i].indexOf("["));  
                            temp[a]=temp[a]||[];  
                            var y=arr[i].substring(arr[i].indexOf("[")+1,arr[i].indexOf("]"));  
                            temp[a][y]=temp[a][y]||{};  
                            temp=temp[a][y];  
                        }else{  
                            temp[arr[i]]=temp[arr[i]] || {};     
                            temp=temp[arr[i]];  
                        }  
                    }
                    // console.log(n);
                    if($(selector).find("input[name='"+n+"']").data("money") && $(selector).find("input[name='"+n+"']").data("money") == "money"){
                    	values[index].value = values[index].value.replace(/,/g,"");
                    }
                    temp[arr[arr.length-1]]=values[index].value;       
                }else{  
                    if(obj[n] !==undefined && obj[n]!=null){  
                       if( !$.isArray(obj[n])){  
                           var v=obj[n];  
                           obj[n]=[];  
                           obj[n].push(v);  
                       }
                        if($(selector).find("input[name='"+n+"']").data("money") && $(selector).find("input[name='"+n+"']").data("money") == "money"){
	                    	values[index].value = values[index].value.replace(/,/g,"");
	                    }  
                       obj[n].push(values[index].value);  
                    }else{  
                    	 if($(selector).find("input[name='"+n+"']").data("money") && $(selector).find("input[name='"+n+"']").data("money") == "money"){
	                    	values[index].value = values[index].value.replace(/,/g,"");
	                    }  
                        obj[n]=values[index].value;  
                    }  
                }     
             }   	
			 return obj;
		 },
		 // 表单回显
		 json2form:function(obj){
			 var nodeParent = null;
		 	var value = undefined;
		 	var $el = null;
		 	var nodeName = "";
		     for(var i in obj){
	      		value= obj[i] ;
	            if(value === undefined || value===null){
	                continue;
	            }
		        if(typeof value == 'object'){
		            nodeParent=obj.nodeParent;
		            value.nodeParent=nodeParent?nodeParent+"."+i : i;
					if(value instanceof Array){
						for(var mm=0;mm<value.length;mm++){
							var ms=value[mm];
							if(typeof ms == 'object'){
								nodeParent=ms.nodeParent;
								ms.nodeParent=ms.nodeParent?ms.nodeParent+"."+i+"["+mm+"]":i+"["+mm+"]";
								arguments.callee(ms);
							}

						}

						$el=$("[name='"+i+"']");
						if($el.is(":checkbox")){
							$el.each(function(){
								if($(this).val() == value){
									$(this).prop("checked",true);
								}
							})
						}
						else if($el.is(":radio")){
							$el.each(function(){
								if($(this).val() == value){
									$(this).prop("checked",true);
								}
							})
						}
					}else{  //递归
						arguments.callee(value);
					}
				}
		        else{
		            nodeName=obj.nodeParent?obj.nodeParent+"."+i : i ;

		            $el=$("[name='"+nodeName+"']");
		            if($el.length > 0){
		            // console.log("匹配数据名称："+nodeName+"值："+value);
		                if($el.is(":text")||$el.attr("type")=="hidden"){
		                	if($el.data("money") && $el.data("money") == "money"){
		                		value = outputdollars(value);
		                	}
		                    $el.val(value);
		                      
		               }else if($el.is(":radio")){
		                   $el.each(function(){
		                      if($(this).val()==value){
		                          $(this).prop("checked",true);
		                      }
		                   })
		               }
		               else if($el.is("select")){
		                  $el.find("option").filter(function(){return $(this).val() == obj[i];}).prop("selected",true);
		               }else if($el.is("textarea")){
		                  $el.val(value)
		               }
		            }
		        }
		    }	 
			 
		 },
		 submitForm:function(obj){
		 	var selector = $(obj.form);
		 	var values= com.midai.page.util.form2json(obj);
		 	$.ajax({
		 		url:obj.url,
		 		data:JSON.stringify(values),
		 		success:function(){
		 				/*swal({title:"恭喜",text:"提交成功",showConfirmButton:false,timer:1000});*/
					    // alert('ok!');
		 				if(obj.success && typeof obj.success == "function"){
		 					obj.success();
		 				}
		 		}
		 		
		 	})
		 },

		// 表单提交成功之后跳转到想对应的选项卡
		toOtherPage:function(obj){
			var delUrl = obj.delUrl; //当前要关闭的选项卡
            var toUrl = obj.toUrl; //要跳转的选项卡
			swal({
                title: '<h5 style="font-size:20px;">提示</h5>',
                text: " 提交成功" ,
                showConfirmButton : false,
                timer:1000,
                html:true
            });
            function sleep(time){
			    var cur = Date.now();
			    while(cur + time >= Date.now()){}
			}
			sleep(1000);
            var index = 0;
            $(".container-header").find(".urlBlock").each(function(){
                var thisPage = $(this).data("page");
                if(thisPage.match(delUrl)){
                    $(this).find(".closePage").click();
                }
                else if(thisPage.match(toUrl)){
                    $(this).find(".urlTitle").click();
                    index ++;
                }
            })
            if(index === 0){
                $(toUrl.split(".")[0]).click();
            }
		},
		closeCurrentPage:function(obj){
			var delUrl = obj.delUrl; //当前要关闭的选项卡
            var toUrl = obj.toUrl; //要跳转的选项卡
			var index = 0;
			if(obj.callback && typeof obj.callback == "function"){
            	onePage.afterContainerChangeFnc = obj.callback;
            }
            $(".container-header").find(".urlBlock").each(function(){
                var thisPage = $(this).data("page");
                if(thisPage && thisPage.match(delUrl)){
                    $(this).find(".closePage").click();
                }
                else if(thisPage && thisPage.match(toUrl)){
                    $(this).find(".urlTitle").click();
                    index ++;
                }
            })
            if(index === 0){
                $(toUrl.split(".")[0]).click();
            }
		},
		openNewPage: function(obj){
			var targetUrl = obj.targetUrl;
			var data = obj.data;
			var urlStr = "";
			onePage.changeFlag = 1;
			onePage.initCurrentUrl();
			if(obj.callback && (typeof obj.callback === "function")){
				onePage.afterContainerChangeFnc = obj.callback;
			}
			if(targetUrl == onePage.currentUrl){
				onePage.afterContainerChangeFnc();
			}
			// 分为两种情况1.当前选项卡已经打开 2.重新打开
			var currentUrl = targetUrl;
			var afterUrl = window.location.href.toString().split("!/");
			this.getCurrentTitle(currentUrl);
			for(var i = 1; i < afterUrl.length; i++){
				if(afterUrl[i].match(currentUrl)){
					onePage.changeFlag = 0;
					if(data === undefined){
						onePage.currentUrl = targetUrl;
						onePage.wholeLocalUrl();
						return;
					}
					else{
						UTIL.closeCurrentPage({
							delUrl:currentUrl
						})
						onePage.changeFlag = 1;
						afterUrl[i] = currentUrl + "?"+data;
					}
				}
				urlStr += "!/" + afterUrl[i];
			}
			onePage.currentUrl = targetUrl;
			if(onePage.changeFlag === 0){	
				window.location.href = onePage.param.orginUrl + "#" + onePage.currentUrl + urlStr;
			}
			if(onePage.changeFlag === 1){
				if(data === undefined){
					onePage.pushUrl(onePage.currentUrl);
					onePage.initCurrentUrl();
					onePage.addUrl();
				}
				else{
					onePage.pushUrl(onePage.currentUrl + "?" + data);
					window.location.href = onePage.param.orginUrl + "#" + onePage.currentUrl + onePage.param.afterUrl + "!/" + currentUrl + "?" + data;
				}
			};	
		},
		getCurrentTitle:function(currentUrl){
			for(var i = 0; i < menuItemList.length; i++){
				if(currentUrl === menuItemList[i].url){
					onePage.param.currentTitle = menuItemList[i].title;
				}
			}
		},
		readUrlData: function(obj){
			var currentUrl = obj.currentUrl;
			var afterUrl = window.location.href.toString().split("!/");
			var dataStr = "";
			var dataArr = [];
			var dataJson = {};
			for(var i = 1; i < afterUrl.length; i++){
				if(afterUrl[i].match(currentUrl)){
					dataStr = afterUrl[i].split("?")[1] || "";
				}
			}
			if(dataStr.indexOf("&")){
				dataArr = dataStr.split("&");
			}
			else{
				dataArr.push(dataStr);
			}
			
			for(var i = 0; i < dataArr.length; i++){
				var key = dataArr[i].split("=")[0];
				dataJson[key] = dataArr[i].split("=")[1];
			}
			return dataJson;
		},
		queryNowDate:function(now){
			var year = now.getFullYear();
			var month =now.getMonth()+1;
			var day = now.getDate();
			//var hours = now.getHours();
			//var mins =now.getMinutes();
			//var seconds = now.getSeconds();

			return year+'-'+month+'-'+day;
		},
		outputdollars : function(number) {
			var signFlag = "";
		    var numberStr=""+number;
		    if(numberStr.substr(0,1) == "-"){
		    	signFlag = "-";
		    	numberStr = numberStr.substr(1);
		    }
		    var numberLastStr = "";
		    if(numberStr.match(/\./)){
		        numberStr = numberStr.split(".");
		        numberLastStr = "."+numberStr[1];
		        numberStr = numberStr[0];
		    }
		    else{
		       numberStr = numberStr;
		    }
		    if (numberStr.length <= 3){
		    	numberStr = (numberStr == '' ? '0' : numberStr + numberLastStr);
		        return (signFlag + numberStr);
		}
		    else {
		      var mod = numberStr.length % 3;
		      var output = (mod == 0 ? '' : (numberStr.substring(0, mod)));
		      for (i = 0; i < Math.floor(numberStr.length / 3); i++) {
		        if ((mod == 0) && (i == 0))
		          output += numberStr.substring(mod + 3 * i, mod + 3 * i + 3);
		        else
		          output += ',' + numberStr.substring(mod + 3 * i, mod + 3 * i + 3);
		      }
		      return (signFlag + output+""+numberLastStr);
		    }
		},
		formatMoney : function(s, n)  
			{  

     		   var s=""+s;
     		   if(s === "" || s === undefined || s === null){
     		   	return "";
     		   }
	   			var signFlag = "";
			    if(s.substr(0,1) == "-"){
			    	signFlag = "-";
	              s = s.substr(1);
			    }
			   n = n > 0 && n <= 20 ? n : 2;  
			   s = parseFloat((s + "").replace(/[^\d\.-]/g, "")).toFixed(n) + "";  
			   var l = s.split(".")[0].split("").reverse(),  
			   r = s.split(".")[1];  
			   t = "";  
			   for(i = 0; i < l.length; i ++ )  
			   {  
			      t += l[i] + ((i + 1) % 3 == 0 && (i + 1) != l.length ? "," : "");  
			   }  
			   return signFlag+t.split("").reverse().join("") + "." + r;  
			},
		clickDisappearSwal : function(text, callback){
			swal({
                title : "<h5 style='font-size:20px;font-family: 微软雅黑;font-weight: 400;'>提示</h5>",
                text : '<p style="font-family:微软雅黑 ">'+text+'</p>' ,
                confirmButtonText:"确定",
                showConfirmButton : true
            },function(){
            	if(callback){
            		callback();
            	}
            	else{
            		return;
            	}
            	
            })
		},
		autoDisappearSwal : function(text, callback){
			swal({
                title: '<h5 style="font-size:20px;font-family: 微软雅黑;font-weight: 400">提示</h5>',
                text: '<p style="font-family:微软雅黑 ">'+text+'</p>' ,
                showConfirmButton : false,
                timer:1000,
                html:true
            });
            if(callback){
            	setTimeout(function(){
            		callback();
            	}, 1000)
            }
		},
		normalSwal : function(text, callback){
			swal({
                title: '<h5 style="font-size:20px;font-family: 微软雅黑;font-weight: 400">提示</h5>',
                text: '<p style="font-family:微软雅黑 ">'+text+'</p>' ,
                showCancelButton: true,
                confirmButtonText:"确定",
                cancelButtonText: "取消",
                html:true
            },function(){
            	if(callback){
            		callback();
            	}
            	else{
            		return;
            	}
            	
            })
		}
} 

var UTIL=com.midai.page.util;
var PAGE=com.midai.page;
