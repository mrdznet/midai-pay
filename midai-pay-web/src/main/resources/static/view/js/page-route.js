var HOST = "";
var menuItemList = [
	{"url":"terminal-sbcx.html","title":"终端查询"}, 
	{"url":"terminal-sbrk.html","title":"终端入库"},
	{"url":"terminal-sbck.html","title":"终端出库"},
	{"url":"terminal-sbbg.html","title":"终端变更"}, 
	{"url":"terminal-history-bdsh.html","title":"历史绑定商户"}, 
	{"url":"terminal-crk-detail.html","title":"出入库明细查询"},
	{"url":"system-organization-manage.html","title":"组织机构管理"}, 
	{"url":"system-role-manage.html","title":"角色权限管理"}, 
	{"url":"terminal-changsgl.html","title":"厂商管理"},
    //交易查询
    {"url":"query-pos.html","title":"pos交易查询"},
    {"url":"agent-pos.html","title":"代理商pos交易查询"},
    {"url":"query-online.html","title":"线上交易查询"},
    {"url":"agent-online.html","title":"代理商线上交易查询"},

	{"url":"terminal-equip-model.html","title":"终端型号管理"}, 
	{"url":"agent-add.html","title":"代理商申请"}, 
	{"url":"agent-query.html","title":"代理商查询"}, 
	{"url":"agent-merchant-search.html","title":"代理商商户查询"},
	{"url":"agent-own-query.html","title":"代理商隶属查询"},
	{"url":"agent-update-info.html","title":"代理商数据更新"},
    {"url":"agent-fee.html","title":"代理商扣率模板"},
    {"url":"terminal-equipment-type.html","title":"终端类型管理"},
	{"url":"merchant-new-apply.html","title":"商户申请"}, 
	{"url":"merchant-update-apply.html","title":"商户信息修改"},
	{"url":"merchant-query.html","title":"商户查询"},
	{"url":"merchant-cs-apply.html","title":"商户初审"}, 
	{"url":"merchant-risk-apply.html","title":"商户风控"}, 
	{"url":"terminal-mygl.html","title":"密钥生成"},
	{"url":"black-white-list.html","title":"黑白名单"},
	{"url":"transaction-forzen.html","title":"交易冻结管理"},
	{"url":"transaction-settlement.html","title":"交易结算"},
	{"url":"transaction-audit.html","title":"交易审核"},
	{"url":"transaction-channel-ticket.html","title":"通道商户小票生成"},
	{"url":"transaction-merchant-frozen.html","title":"商户冻结管理"},
	{"url":"settlement-clear.html","title":"清算清分"},
	{"url":"settlement-payment-file.html","title":"生成代付文件"},
	{"url":"settlement-auto-pay.html","title":"自动打款"},
	{"url":"settlement-pay-search.html","title":"打款结果查询"},
	{"url":"bussinss-manage.html","title":"待办事项"},
	{"url":"updateUserInfo.html","title":"修改个人资料"},
	{"url":"merchant-updata-apply-hb.html","title":"海贝信息修改"},
	{"url":"merchant-cs-apply-hb.html","title":"海贝信息审核"},
	{"url":"merchant-info-review.html","title":"标准商户信息查看"},


	{"url":"settlement-pay-error-query.html","title":"打款失败查询"},
	{"url":"Institution-deploy.html","title":"配置通道"},
	{"url":"Institution-list.html","title":"通道列表"},
    {"url":"merchant-level.html","title":"商户等级"},
    {"url":"agent-info-view.html","title":"代理商信息预览"},
	{"url":"merchant-creatApply.html","title":"新建商户"},


    {"url":"agent-profit-statistics.html","title":"代理商分润统计"},
    {"url":"agent-profit-list.html","title":"代理商分润明细"},


	//新建
	{"url":"merchant-creatApply_NoHand.html","title":"新建Mpos商户"},
	{"url":"merchant-creatApply_Hand.html","title":"新建标准商户"},
	//预览
	{"url":"merchant-mposinfo-review.html","title":"预览Mpos商户"},
	{"url":"merchant-normalinfo-review.html","title":"预览标准商户"},
	//初审
	{"url":"merchant-csMpos-apply.html","title":"初审Mpos商户"},
	{"url":"merchant-csNormal-apply.html","title":"初审标准商户"},
	//风控
	{"url":"merchant-riskMpos-apply.html","title":"风控Mpos商户"},
	{"url":"merchant-riskNormal-apply.html","title":"风控标准商户"},
	//更新
	{"url":"merchant-Mposinfo-update.html","title":"更新mpos商户"},
	{"url":"merchant-Normalinfo-update.html","title":"更新标准商户"},


	{"url":"merchant-deduction-rate.html","title":"商户扣率模板"}

];


var titleUrl = [];
function OnePage(obj){
	var obj = obj || {};
	this.changeFlag = 0; //0只是改变当前URL，1是增加url，2是删除url
	this.header = obj.header || ".container-block";
	this.container = obj.container || ".container-main";
	this.sideList = obj.sideList || ".onePageList";
	this.currentUrl = obj.currentUrl;
	this.urlArr = [];
	// this.titleArr = [];
	this.historyArr = [];
	this.headerAddFncFlag = obj.headerAddFncFlag || false;
	this.headerRemoveFncFlag = obj.headerRemoveFncFlag || false;
	this.headerApendCallback = obj.headerApendCallback || function(){ return false;};
	this.headerDelCallback = obj.headerDelCallback || function(){ return false};
	this.param = {
		localWholeUrl:"",
		orginUrl : "",
		afterUrl : "",
		currentTitle : ""
	};
	this.afterChangeUrlIndex = -1;
	this.deleteUrl = "";
	this.prevPageList = "";
	this.afterContainerChangeFnc = obj.afterContainerChangeFnc || function(){};
	// 初始化时给左边的菜单添加事件
	this.initPageListEvent = function(){
		var obj = this;
		var $sideList = $(this.sideList);
		var value = "";
		$sideList.each(function(){
			$(this).on("click",function(){
				if(obj.prevPageList !== ""){
					obj.prevPageList.removeClass('current');
				}
				$(this).parent().addClass('current');
				obj.currentUrl = $(this).data("url").split("?")[0];
				if(obj.currentUrl && obj.currentUrl != ""){
					obj.param.currentTitle = $(this).data("title");
					if(isInArray(obj.currentUrl, obj.urlArr, true)){
						obj.changeFlag = 0;
						obj.initCurrentUrl();
						obj.wholeLocalUrl();
					}
					else{
						obj.pushUrl(obj.currentUrl);
						obj.initCurrentUrl();
						obj.addUrl();
					}
				}
				obj.prevPageList = $(this).parent();	
			});
		})
	};
	this.historyUrlInitial = function(){
		var curInUrlLocation = this.initCurrentUrl();
		var initUrl = window.location.hash;
		var arr = initUrl.split("!/");
		this.historyArr = this.param.afterUrl.split("!/");
		for(var i = 0; i < this.historyArr.length; i++){
			if(this.historyArr.length > this.urlArr.length){ //地址栏增加
				this.addUrl();
				onePage.appendHeader();
				onePage.appendContainer();
				onePage.afterContainerChangeFnc()
			}
			else if(this.historyArr.length < this.urlArr.length){ //地址栏减少
				this.deleteUrl();
				onePage.delHeader();
				onePage.delContainer();
			}
			else if(this.historyArr.length == this.urlArr.length){
				
			}
			this.currentUrl = arr[0].substr(1);
			onePage.initHeadStyle();
			onePage.showCurHtml();
		}


	}
	this.initial = function(){
		var initUrl = window.location.hash;
		var arr = initUrl.split("!/");
		var curTitle = "";
		$(this.container).empty();
		$(this.header).empty();
		this.urlArr = [];
		if(initUrl){
			for(var i = 1; i < arr.length; i++){
				this.currentUrl = arr[i];
				for(var j = 0; j < menuItemList.length; j++){
					if(arr[i].match(menuItemList[j].url)){
					// if(menuItemList[j].url == arr[i]){
						this.param.currentTitle = menuItemList[j].title;
						break;
					}
				}
				this.urlArr.push(arr[i]);
				this.initCurrentUrl();
				this.appendHeader();
				this.appendContainer();
				this.afterContainerChangeFnc()
			}
			this.currentUrl = arr[0].substr(1);
			for(var i = 0; i < menuItemList.length; i++){
				if(menuItemList[i].url == this.currentUrl){
					this.param.currentTitle = menuItemList[i].title;
				}
			}
			this.initHeadStyle();
			this.showCurHtml();
		}	
	}
	this.wholeLocalUrl = function(){
		if(this.urlArr.length == 0){
			window.location.href = this.param.orginUrl;
		}
		else{
			window.location.href = this.param.orginUrl + "#" + this.currentUrl + this.param.afterUrl;
		}
	};
	this.initHeadStyle = function(){
		var obj = this;
		$(this.header).find(".urlBlock").each(function(){
			if($(this).data("page").match(obj.currentUrl)){
				$(this).addClass("curPageStyle");

			}
			else{
				if($(this).hasClass('curPageStyle')){
					$(this).removeClass('curPageStyle');
				}
			}
		})
	}
	this.initCurrentUrl = function(){
		this.param.localWholeUrl = window.location.toString(); //当前地址栏的整个字符串
		this.param.orginUrl = HOST + location.pathname;
		this.param.afterUrl = (function(){					//当前页面中所有选项卡
			var temp = "";
			var str = window.location.hash.substr(1).split("!/");
			for(var i = 1; i < str.length; i++){
				temp += "!/" + str[i];
			}
			return temp;
		})();
	};
	this.pushUrl = function(currentUrl){
		var obj = this;
		this.urlArr.push(currentUrl);
	};
	this.delUrlInArray = function(){
		var obj = this;
		var arr = obj.urlArr;
		var index = 0;
		for(var i = 0; i < arr.length; i++){
			if(arr[i].match(obj.deleteUrl)){
				obj.afterChangeUrlIndex = i;
				obj.urlArr.splice(i,1);
			}
		}
	}
	this.appendHeader = function(){
		var obj = this;
		$(this.header).append($("#page-header").html().replace(/{{pageUrl}}/g,obj.currentUrl).replace(/{{title}}/g,obj.param.currentTitle));
		if(obj.headerAddFncFlag){
			obj.headerApendCallback();
		}
		$(this.header).find(".urlBlock").last().find(".closePage").click(function(){
			obj.deleteUrl = $(this).parent(".urlBlock").data("page").split("?")[0];
			obj.delUrl();
			
		});
		$(this.header).find(".urlBlock:last").find(".urlTitle").click(function(){
			obj.changeFlag = 0;
			obj.currentUrl = $(this).parent(".urlBlock").data("page").split("?")[0];
			obj.initCurrentUrl();
			obj.wholeLocalUrl();
		})
	};
	this.delHeader = function(){
		var obj = this;
		$(this.header).find(".urlBlock").each(function(){
			if($(this).data("page").match(obj.deleteUrl)){
				$(this).remove();
			}
		})
		var headerWidth =  $("#content>.container").width();
		var $currentHeader = $("#content>.container>.container-header>.container-block");
		var currentWidth =  $currentHeader.width();
		if(currentWidth < headerWidth){
			$(".header-wheel").addClass('hide-default');
		}
		if(obj.headerRemoveFncFlag){
			obj.headerDelCallback();
		}
	};
	this.appendContainer = function(){
		var obj = this;
		var str = obj.currentUrl.split("?")[0];
		$(this.container).append("<div data-page=" + str + " id='" + str + "' class='containerPage'></div>");
		$(this.container).find(".containerPage:last").load(str, function(){
			if($(this).html().indexOf("invalidSession")>-1){
				window.location.href="/login.html";
			}
			// 如果在资源分配的全局变量中有这个属性就遍历
			if(RESOURCEDETAIL[str]){
				for(var i = 0; i < RESOURCEDETAIL[str].length; i++){
					$(this).find("."+RESOURCEDETAIL[str][i]).removeClass('hide-default');
				}
				// 如果后台没有传值，就遍历页面中所有的按钮，如果这些按钮里面有hide-default这个属性，就移除元素
				$(this).find("button").each(function(){
					if($(this).hasClass('hide-default')){
						$(this).remove();
					}
				});
				$(this).find("input[type='button']").each(function(){
					if($(this).hasClass('hide-default')){
						$(this).remove();
					}
				});
			}
		});
	}
	this.delContainer = function(){
		var obj = this;
		$(this.container).find(".containerPage").each(function(){
			if($(this).data("page") == obj.deleteUrl){
				$(this).remove();
			}
		});
	};
	this.addUrl = function(){
		this.changeFlag = 1;
		this.param.orginUrl = HOST + location.pathname;
		this.param.afterUrl = this.param.afterUrl + "!/" + this.currentUrl;
		this.wholeLocalUrl();
	};
	this.changeUrl = function(){

	};
	this.delUrl = function(){
		this.changeFlag = 2;
		var obj = this;
		// var str = this.param.afterUrl.split("!/" + obj.deleteUrl);
		var str = "";
		for(var i = 0; i < obj.urlArr.length; i++){
			if(!obj.urlArr[i].match(obj.deleteUrl)){
				str += "!/" + obj.urlArr[i];
			}
		} 
		this.delUrlInArray();
		if(obj.deleteUrl.indexOf(obj.currentUrl) != -1){
			if(this.afterChangeUrlIndex == this.urlArr.length && this.urlArr.length !== 0){
				this.afterChangeUrlIndex -= 1;
			}
			if(this.urlArr.length !== 0){
				this.currentUrl = this.urlArr[this.afterChangeUrlIndex].split("?")[0];
			}
			
		}
		this.param.afterUrl = str;
		obj.wholeLocalUrl();
	};
	this.showCurHtml = function(){
		var obj = this;
		$(this.container).find(".containerPage").each(function(){
			if($(this).data("page") === obj.currentUrl){
				$(this).show();
			}
			else{
				$(this).hide();
			}
		})
	};
}

var onePage = new OnePage({
	headerAddFncFlag : true,
	headerRemoveFncFlag : true,
	headerApendCallback : function(){
		var headerWidth =  $("#content>.container").width();
		var $currentHeader = $("#content>.container>.container-header>.container-block");
		var currentWidth =  $currentHeader.width();
		var currentPositon = $currentHeader.position().left;
		if($(".header-wheel").hasClass("hide-default") && currentWidth >= headerWidth){
			$(".header-wheel").removeClass("hide-default");
		}
		if(currentWidth > headerWidth && currentPositon > (-(currentWidth - headerWidth)-121) ){
			$currentHeader.animate({"left":currentPositon-121});
		}
	},
	headerDelCallback : function(){
		var headerWidth =  $("#content>.container").width();
		var $currentHeader = $("#content>.container>.container-header>.container-block");
		var currentPositon = $currentHeader.position().left;
		var currentWidth =  $currentHeader.width();
		if(!$(".header-wheel").hasClass("hide-default") && currentWidth < headerWidth){
			$(".header-wheel").addClass("hide-default");
		}
		if(currentPositon < 0){
			$currentHeader.animate({"left":currentPositon+121 > 0 ? 0 :currentPositon+121});
		}
	},
	initCurListStyle : function(){
		
	}

});
onePage.initial();
onePage.initPageListEvent();
window.onhashchange = function(){
	if(onePage.changeFlag === 0){
		onePage.initHeadStyle();
		onePage.showCurHtml();
	}
	else if(onePage.changeFlag === 1){
		onePage.appendHeader();
		onePage.initHeadStyle();
		onePage.appendContainer();
		onePage.showCurHtml();
		this.afterChangeUrlIndex = -1;
	}
	else if(onePage.changeFlag === 2){
		onePage.delHeader();
		onePage.delContainer();
		onePage.initHeadStyle();
		onePage.showCurHtml();
	}
	else{
		console.log(onePage.changeFlag);
		onePage.initial();
	}
	onePage.changeFlag = "";
	onePage.afterContainerChangeFnc();
	onePage.afterContainerChangeFnc = function(){};
};

// 页面路由
