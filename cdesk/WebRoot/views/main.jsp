<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>主页</title>
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/${faviconUrl}" />
    <%@include file="/views/include/pageHeader.jsp"%>
    <link href="<%=request.getContextPath()%>/static/css/outerFrame.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
    <style>
		#formId{
			width:600px;
		}
		#formId .form-group{
			width:45%;
			margin-right:15px;
			display:inline-block;
		}
		#userFieldUl .form-group{
			vertical-align: top;
		}
	</style>
</head>
<body>
<header id="header">
    <nav class="navbar" role="navigation" style="margin-bottom: 0">
        <div class="container-fluid">
            <div class="nav navbar-nav navbar-left logo">
                <a class="logo-icon" href="#"> </a>
            </div>
            <div class="nav navbar-nav">
                <ul class="header-tag-list" id="tagList">
                    <li class="newOrder">
                        <a data-toggle="modal" onclick="addUserInmain()" data-target="#addUserModal">创建用户</a>
                    </li>
                </ul>
            </div>
            <div class="collapse navbar-collapse navbar-right">
                <form role="search" class="nav navbar-nav navbar-form" method="post" action="javascript:goSerach('work');">
                    <div class="form-group">
                        <div class="input-group dropdown">
                            <span class="input-group-addon"><i class="fa fa-search"></i></span>
                            <input type="text" placeholder="搜索" class="form-control" name="top-search" id="top-search" data-toggle="dropdown"  aria-haspopup="true" aria-expanded="false">
                            <ul class="dropdown-menu" style="width: 170px;" aria-labelledby="top-search">
                                <li><a onclick="goSerach('work')">在“工单”中搜索</a></li>
                                <li><a onclick="goSerach('user')">在“用户”中搜索</a></li>
                           </ul>
                        </div>
                    </div>
                </form>
                <ul class="nav navbar-nav icons">
                    <li id="setReadyToggle">
                        <div class="togglebutton">
                            <label>
                                <span id="currentStatus">准备中...</span>
                                <input type="checkbox">
                            </label>
                        </div>
                    </li>
                    <li id="phoneBtn"><i class="fa fa-phone"></i></li>
                    <li id="IMBtn"><i class="fa fa-comment" ></i><span class="notice-number" id="noticeNumber">0</span></li>
                </ul>
                <div class="nav navbar-nav">
                    <div class="dropdown profile-element">
                        <a data-toggle="dropdown" class="dropdown-toggle user" href="#">
                            <span class="avatar">
                                <img alt="image" class="img-circle" src="<%=request.getContextPath()%>/${photoUrl}" />
                            </span>
                            <span class="text-muted text-xs block username">${user.userName}<b class="caret"></b></span>
                        </a>
                        <ul class="dropdown-menu animated fadeInRight m-t-xs">
                            <li><a class="J_menuItem"  onclick="showUserDetail()">个人设置</a></li>
                            <li><a id="logoutBtn" href="javascript:void(0);">退出</a></li>
                        </ul>
                    </div>
                </div>
            </div>
        </div>
    </nav>
</header>
<div id="wrapper">
    <!--左侧导航开始-->
    <nav id="leftMenu" class="navbar-default navbar-static-side sidebar" role="navigation">
        <div class="sidebar-collapse">
            <ul class="nav sidebar-top left-menu">
            	<c:forEach items="${permissionList}" var="item">
            		<c:if test="${item.isTop == '1'}">
            			<li>
		                    <a data-href="${item.url}" href="javascript:void(0);">
		                        <i class="fa menu${item.id}"></i>
		                        <span class="nav-label">${item.name}</span>
		                    </a>
		                </li>
            		</c:if>
            	</c:forEach>
            </ul>
            <ul class="nav sidebar-bottom left-menu">
                <c:forEach items="${permissionList}" var="item">
            		<c:if test="${item.isTop == '0'}">
            			<li>
		                    <a data-href="${item.url}" href="javascript:void(0);">
		                        <i class="fa menu${item.id}"></i>
		                        <span class="nav-label">${item.name}</span>
		                    </a>
		                </li>
            		</c:if>
            	</c:forEach>
            </ul>
        </div>
    </nav>
    <!--左侧导航结束-->

    <!--右侧部分开始-->
    <div class="page-wrapper" id="content-main">
        <iframe name="iframe" id="rightContent" width="100%" height="100%" frameborder="0" seamless></iframe>
    </div>
    <!--右侧部分结束-->
</div>

<%--添加用户弹出框--%>
<%@include file="/views/addUserModal.jsp"%>

<%--IM TAB页内容--%>
<%@include file="/views/im/index.jsp"%>

<form id="switchUser" style="display:none" method="post" action="<%=request.getContextPath()%>/switchUser">
	<input type="text" name="loginName" id="loginNameSwitch" />
	<input type="text" name="loginPwd" id="loginPwdSwitch" />
</form> 
<script>
    $.cookie("sessionKey",'${user.sessionKey}');
    var workBasePath="${workPath}";
    var locationStack = [];
    
    $(function(){
    	/* 添加用户,根据权限动态加载用户类型 */
    	setTypeSelect();
    	
        $("#wrapper .left-menu>li>a").click(function(){
            var href = $(this).data("href");
            
            //IM菜单地址配置为空
            if(href==""){
                $("#IMBtn").click();
            }
            else{
	            $('#content-main iframe').hide();
	            var $rc = $("#rightContent");
	            if($rc.attr('src') !== href){
	                var ls = locationStack.length && locationStack[locationStack.length-1];
	                /**
	                 * 如果是在左侧菜单的切换，则直接替换菜单url,
	                 * 如果是在左侧菜单与tab页的切换，在url后面append地址
	                 */
	                if(ls === 0 || /^id_\d+$/i.test(ls)){
	                    locationStack.push(href);
	                }
	                else{
	                    locationStack[locationStack.length-1] = href;
	                }
	                $rc.attr('src',href);
	            }
	            $rc.show();
	
	            $("#tagList li.active").removeClass("active");
	            $("#leftMenu li.selected").removeClass('selected');
	            $(this).parent().addClass('selected');
	            $("#header").removeClass("tab-opened");
            }
        });
        $("#wrapper li a").eq(0).click();
        window.setInterval(checkLoginName,1000*90);
    });

	// 检查是否在别处登录
    function checkLoginName(){
		$.ajax({
            url: "<%=request.getContextPath()%>/sso/checkLoginName",
            success: function (data) {
                if (!data.success) {
                   // notice.warning(data.msg);
                    alert(data.msg);
                    window.location.href = '<%=request.getContextPath()%>/logout';
                }
            },
            dataType: 'json',
            global: false
        });
	}

    /**
     * 创建工单成功后，刷新对应用户的工单列表
     */
    var IMRefreshOrderFunc = function(userId){
        var $custom = $("#IMWindowContainer .right-container").find('[userid='+userId+']');
        var pager = $custom.find('[data-id=paginationLately]').data('pager');
        pager &&pager._page(1);
    };
    var tabManager = function(){
        var MAX_LENGTH = 5;
        var $tabList = $('#tagList');
        var $contentContainer = $("#content-main");

        var isUserDetail = function(url){
        	return /userManageMongo\/userDetails$|userManageMongo\/userDetails[\/,?]|communicate\/goComm/.test(url);
        };

        var isOrderDetail = function(url){
        	return /order\/detail/.test(url);
        };

        var isEqual = function(c1,c2){
            if(c1 instanceof jQuery && c2 instanceof jQuery){
                return c1.is(c2);
            }
            else{
                if(isUserDetail(c2) && isUserDetail(c1)){
                	var userId1 = c2.match(/userId=\w+/) || [];
                    var userId2 = c1.match(/userId=\w+/) || [];
                    if(userId1.length && userId2.length){
                        return userId1[0] === userId2[0];
                    }
                    else{
                    	return false;
                    }
                }
                else if(isOrderDetail(c2) && isOrderDetail(c1)){
                    var workId1 = c2.match(/workId=\w+/) || [];
                    var workId2 = c1.match(/workId=\w+/) || [];
                    if(workId1.length && workId2.length){
                        return workId1[0] === workId2[0];
                    }
                    else{
                        return false;
                    }
                }
                else{
                    return c1 === c2;
                }
            }
        };

        var Tab = function(options){
            var timeShame = +new Date();
            this._id = 'id_' + timeShame;
            this._contentId = 'contentId_' + timeShame;
            this.$tab = null;
            this.$content = null;

            var defaultOptions = {
                title:'New Tab',
                content:'#',
                forceLoad:false,
                closeAble:true,
                isIframe:true,
                onTabClose:null,
                type:null//order:工单 user:客户 im:多渠道
            };
            this.options = $.extend(defaultOptions,options);
            this._create();
            this.focus();
            var self = this;
            this.$tab.click(function(){
                self.focus();
            });
        };

        Tab.prototype = {
            _create:function(){
                var self  = this,
                    op    = this.options,
                    title = op.title,
                    type  = op.type,
                    closeAble = op.closeAble;

                var $tab = this.$tab = $('<li class="tag"><a>' + title + '</a></li>');
                type && $tab.addClass('tag-' + type);
                $tab.attr('id',this._id);
                if(closeAble){
                    var $closeBtn = $('<i class="fa fa-close"></i>');
                    $tab.append($closeBtn);
                    $closeBtn.click(function(){
                        self.closeTab();
                    });
                }
                $tabList.append($tab);
                this._load();
            }
            ,closeTab:function(){
                this.options.onTabClose && this.options.onTabClose.call(this);
                this.$tab.remove();
                this.$content.remove();
            }
            ,_load:function(){
                var id = this._contentId,
                    $container = $contentContainer,
                    content = this.options.content,
                    isFormServer = true,
                    that = this;
                //jQuery
                if(content instanceof jQuery){
                    isFormServer = false;
                }
                //HTML
                else if(/^<\w+>.*/g.test(content)){
                    isFormServer = false;
                }
                if(isFormServer){
                    if(this.options.isIframe){
                        var $iframe = this.$content = $('<iframe name="iframe" width="100%" height="100%" src="'+content+'" frameborder="0" seamless id="'+id+'"></iframe>');
                        var iframeNode = $iframe[0];
                        if(iframeNode.attachEvent){
                            iframeNode.attachEvent("onload", function(){
                                that.options.onLoad && that.options.onLoad.call();
                            });
                        }
                        else {
                            iframeNode.onload = function(){
                                that.options.onLoad && that.options.onLoad.call();
                            };
                        }
                        $container.append($iframe);
                    }
                    else{
                        var $div = $('<div></div>').attr('id',id).load(this.options.content,function(){
                            that.options.onLoad && that.options.onLoad.call();
                        });
                        $container.append($div);
                        this.$content = $div;
                    }
                }
                else{
                    var $div = $('<div></div>').attr('id',id).append(content);
                    $container.append($div);
                    this.options.onLoad && this.options.onLoad.call();
                    this.$content = $div;
                }
            }
            ,getContent:function(){
                return this.options.content;
            }
            ,focus:function(){
                $tabList.find('li.active').removeClass('active');
                this.$tab.addClass('active');
                if(this.options.forceLoad){
                    this.$content.remove();
                    this._load();
                }
                $contentContainer.children().hide();
                this.$content.show();
            }
        };

        var tabQueue = [];
        return {
            /**
             * options.title:'New Tab' 标题
             * options.content:'#' tab页内容 可以为html片段 jquery url
             * options.forceLoad:false 点击到tab标签是否强制加载内容
             * options.closeAble:true, 是否在tab标签上显示关闭按钮
             * options.isIframe:true, 当content为url时，是否使用iframe加载内容还是使用jquery.load
             * options.onClose:null, 关闭时回调
             * options.type:null//order:工单 user:客户 im:多渠道
             */
            open:function(options){
                $("#leftMenu li.selected").removeClass('selected');
                for(var i=0,len=tabQueue.length;i<len;i++){
                    var tab = tabQueue[i];
                    if(isEqual(options.content,tab.getContent())){
                        tab.options = $.extend(tab.options,options);
                        tab.focus();
                        return true;
                    }
                }
                if(tabQueue.length === MAX_LENGTH){
                    for(var j=0,l=tabQueue.length;j<l;j++){
                        var t = tabQueue[j];
                        if(t.options.closeAble){
                            t.closeTab();
                            tabQueue.splice(j,1);
                            break;
                        }
                    }
                }

                options.onTabClose = function(){
                    var id = this._id;
                    for(var j=0,l=tabQueue.length;j<l;j++){
                        var tab = tabQueue[j];
                        if(this === tab){
                            tabQueue.splice(j,1);
                            break;
                        }
                    }
                    $.each(locationStack,function(index,item){
                        if(id === item){
                            locationStack.splice(index,1);
                            return false;
                        }
                    });
                    if(this.$tab.is('.active')){
                        var lt = locationStack.length && locationStack[locationStack.length - 1];
                        //如果为tab的id,显示tab,否则显示左侧菜单栏的页面
                        if(lt && /^id_\d+$/i.test(lt)){
                            $.each(tabQueue,function(i){
                                if(tabQueue[i]._id === lt){
                                    tabQueue[i].focus();
                                }
                            });
                        }
                        else{
                            $contentContainer.children().hide();
                            $("#rightContent")[0].contentWindow.location.reload();
                            //$("#rightContent").document.location.reload();
                            $("#rightContent").show();
                            $("#wrapper .left-menu>li>a").each(function(){
                                var href = $(this).data("href");
                                if(lt === href){
                                    $("#wrapper .left-menu li.selected").removeClass('selected');
                                    $(this).parent().addClass('selected');
                                }
                            });
                        }
                    }
                    options.onClose && options.onClose.call(this);
                };
                var tab = new Tab(options);
                locationStack.push(tab._id);
                tabQueue.push(tab);
            }
        };
    }();
    var openTab = function(url,type,title,reload){
        tabManager.open({
            content:url,
            type:type,
            title:title,
            forceLoad:reload
        });
    };

    function showUserDetail(){
    	var url="<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+'${user.userId}'+"&loginTime="+'${loginTime}';
        openTab(url,"user",'${user.userName}',true);
    }

    function switchUser(loginName,loginPwd){
    	$("#loginNameSwitch").val(loginName);
		$("#loginPwdSwitch").val(loginPwd);
		$("form#switchUser").submit();
    }

    /**
     * 用户详情页创建新工单
     */
    function newwork(curEmail,curUserId,curUserName,param) {
    	tabManager.open({
            content:'<%=request.getContextPath()%>/newwork/index?customId='+curUserId+'&customName='+curUserName +'&selectEmail=' + curEmail +'&param='+param,
            type:'order',
            closeAble:true,
            title:'新建工单'
        });
    	
    	<%-- 
    	console.log('[cdesk-main] 创建工单,curEmail:'+curEmail+',curUserId:'+curUserId+',curUserName:'+curUserName+',param:'+param);
    	$('#content-main iframe').hide();
    	$('#IMContainer').hide();
        if($("#newOrderIframe").length >= 0){
            $("#content-main").append('<iframe name="iframeNewOrder" id="newOrderIframe" width="100%" height="100%" src="<%=request.getContextPath()%>/newwork/index?customId='+curUserId+'&customName='+curUserName +'&selectEmail=' + curEmail +'&param='+param+ '" frameborder="0" data-id="index_v1.html" seamless></iframe>');
        }else{
        	$("#mailAddr", window.frames["iframeNewOrder"].document).val(curEmail);
        	$("#customId", window.frames["iframeNewOrder"].document).val(curUserId);
            $("#newOrderIframe").show();
        }
        $("#tagList li.active").removeClass("active");
        $("#newOrder").parent().addClass("active");
        --%>
    }

    /**
     * 是否发邮件提醒
     */
    var send="false";
    $("#receiveEmail").click(function(){
        var isChecked=$(this).prop("checked");
        if(isChecked==true ){
        	if($("#inputPassword3").val()!="")
        		send="true";
        	else{
        		notice.warning("请先填写邮箱！");
        		$(this).attr("checked",false);
        		return;
        	}
        }   
    });
    
    function closeThisTab(){
    	$('#tagList .active i').click();
    }
</script>
<script src="http://123.56.112.31/agent/Agent.js"></script>
<script>
    $(function(){
        $("#logoutBtn").on('click',function(){
            logoutCCODMultiChannel();
        });
    });
    /* 客服类型二级下拉框 */
    function secondLevel(){
        var parentId=document.getElementById("typeSelect").value;
        if(parentId==2){
  		    $("#secondType").css("display","block");
		    $("#roleLabel").css("display","block"); 

    		$("#star").css("display","inline-block");
    		$("#need").css("display","inline-block");
    		$("#addUserNameN").attr("placeholder","管理员或客服必填");
		    
    		var id = "";
    		if("${userType}"=="2" && "${roleId}"=="5"){
    			id = "4";
    		} 
            $.post("<%=request.getContextPath()%>/usrManage/secondLevel?parentId="+parentId+"&id="+id, secondLevelCallBack, 'json');
        }else if(parentId==3){
            $("#secondType").css("display","none");
            $("#roleLabel").css("display","none");
            
    		$("#star").css("display","inline-block");
    		$("#need").css("display","inline-block");
    		$("#addUserNameN").attr("placeholder","管理员或客服必填");
        }else{
            $("#secondType").css("display","none");
            $("#roleLabel").css("display","none");
            
    		$("#star").css("display","none");
    		$("#need").css("display","none");
    		$("#addUserNameN").attr("placeholder","");
        }
    }
    /* 客服类型二级下拉框回调函数 */
    function secondLevelCallBack(data){
        if(data.success){
        	$("#secondType").empty();
            var $secondType = $("#secondType");
            for(var j=0;j<data.rows.length;j++){
                var sub = data.rows[j];
                $secondType.append('<option value="'+sub.id+'">'+sub.name+'</option>');
            }
        }else{
            notice.alert(data.msg);
        }
    }

    function addUserInmain(){
    	$("#typeSelect").val("1");
    	secondLevel();
    	$("#inputEmail3").val("");
    	$("#inputPassword3").val("");
    }
    
    function goSerach(type){
        var key=$("#top-search").val();
        var title="搜索";
        var url="<%=request.getContextPath()%>/msearch/?type="+encodeURIComponent(type)+"&key="+encodeURIComponent(key);
        if(key){
          title=key;
        }
        parent.openTab(url,"search",title);
    }
    
    /* 添加用户,根据权限动态加载用户类型 */
    function setTypeSelect(){
    	var selectHTML = "";
    	
        if("${isFounder}"=="1"){
        	selectHTML = '<select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">'+
    		'<option value="1">客户</option><option value="2">客服</option><option value="3">管理员</option></select>';      
    	}else if("${userType}"=="3" && "${roleId}"=="3" && "${isFounder}"!="1"){
    		selectHTML = '<select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">'+
    		'<option value="1">客户</option><option value="2">客服</option></select>';       
    	}else if("${userType}"=="2" && "${roleId}"=="4"){
    		selectHTML = '<select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">'+
    		'<option value="1">客户</option></select>';       
    	}else if("${userType}"=="2" && "${roleId}"=="5"){
    		selectHTML = '<select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">'+
    		'<option value="1">客户</option><option value="2">客服</option></select>'; 
    	}    	
        $("#type").append(selectHTML + '<p class="help-block">管理员和客服人员是处理工单的的用户角色，普通用户是外部提交工单的用户角色</p>');   	
    }
    
</script>
<%@include file="/views/include/webAgent.jsp"%>
</body>
</html>