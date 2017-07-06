<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>欢迎</title>
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/${faviconUrl}" />
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/normalize.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/enter.css">
     <script src="http://123.56.112.31/IM/IM.js"></script>
</head>
<body>
<header class="head">
    <div class="wrap hader-inner">
        <div class="logo">
            <img src="<%=request.getContextPath()%>/${logoUrl}" />
<%--             <img src="<%=request.getContextPath()%>/static/images/logo.png" /> --%>
        </div>
        <nav class="header-nav">
            <ul class="header-nav-list">
                <li id="one">
                    <a href="#">讨论社区</a>
                </li>
                <li>
                    <a href="<%=request.getContextPath()%>/newquestion/index">提交新问题</a>
                </li>
            </ul>
            <div class="user-nav">
            	<% if (request.getAttribute("user") != null) { %>
                <div class="dropdown profile-element">
                    <a data-toggle="dropdown" class="dropdown-toggle user" href="#">
                        <span class="avatar">
                            <img alt="image" class="img-circle" src="<%=request.getContextPath()%>/${photoUrl}" />
                        </span>
                        <span class="text-muted text-xs block username">${user.nickName}<b class="caret"></b></span>
                    </a>
                    <ul class="dropdown-menu animated fadeInRight m-t-xs">
                        <li><a class="J_menuItem" href="#">我的消息</a>
                        </li>
                        <li><a class="J_menuItem" href="<%=request.getContextPath()%>/activity/my">我的活动</a>
                        </li>
                        <c:if test="${user.userType != '1'}">
                        <li><a class="J_menuItem" href="<%=request.getContextPath()%>/main">进入控制面板</a>
                        </li>
                        </c:if>
                        <li class="divider"></li>
                        <li><a href="<%=request.getContextPath()%>/logout">退出</a>
                        </li>
                    </ul>
                </div>
                <%} else { %>
                <div class="user-info">
                    <div class="dropdown-toggle">
							<span class="user-info-name">
								<a href="<%=request.getContextPath()%>/login">登录</a>
							</span>
                    </div>
                </div>
				<%} %>
                <div class="user-info">
                    <div class="dropdown-toggle">
                        <span class="user-info-name">
                            <a href="#" style="color:#428bca;">简体中文</a>
                        </span>
                    </div>
                </div>
            </div>
        </nav>
    </div>
</header>
<main>
    <section class="search-box">
        <form class="search">
            <div class="search-match">
                <label style="margin-bottom:0;">
                    <img src="<%=request.getContextPath()%>/static/images/Search.png" />
                    <input type="hidden" name="type" value="kb">
                    <input type="search"  name="keyword" placeholder="输入问题关键字，找到答案" autocomplete="off" x-webkit-speech="">
                </label>
                <ul class="match-list"></ul>
            </div>
            <input type="submit" name="" value="搜索">
        </form>
    </section>
    <section class="content">
        <div class="wrap clearfix">
            <section class="content-main">
                <div class="category-tree">
                    <div class="category">
                        <h2><a href="#">帮助支持</a></h2>
                        <div class="section">
                            <h3><a href="#">公告与新闻</a></h3>
                            <ul class="article-list">
                                <li><a href="#">欢迎来到您的帮助中心</a></li>
                            </ul>
                            <a href="#" class="more">查看全部1篇文档</a>
                        </div>
                        <div class="section">
                            <h3><a href="#">使用说明</a></h3>
                            <ul class="article-list">
                                <li class="promoted">
                                    <img src="<%=request.getContextPath()%>/static/images/Star-yellow.png"></img>
                                    <a href="#">您的帮助中心已经建立成功</a>
                                </li>
                            </ul>
                            <a href="#" class="more">查看全部1篇文档</a>
                        </div>
                        <div class="section">
                            <h3><a href="#">常见问题</a></h3>
                            <ul class="article-list">
                                <li><a href="#">文章分区和分类是干什么用的？</a></li>
                                <li><a href="#">我将如何自定义前台界面？</a></li>
                                <li><a href="#">我还有其他问题如何解决？</a></li>
                            </ul>
                            <a href="#" class="more">查看全部3篇文档</a>
                        </div>
                    </div>
                </div>
            </section>
            <section class="content-sidebar">

                <div class="widget-column">
                    <h3>被推荐的文档</h3>
                    <ul>
                        <li>
                            <a href="#">您的帮助中心已经建立成功</a>
                        </li>
                    </ul>
                </div>
            </section>
        </div>
    </section>
</main>
<a href="javascript:void(0)" id="WebIM" class="support-btn" >帮助</a>
</body>
<script type="text/javascript">
    $(function(){
        $('#entInfo').html("${entName},欢迎您!");
        <% if (request.getAttribute("user") != null) { %>
        window.setInterval(checkLoginName,1000*90);
        <% } %>
    });
    
	// 检查是否在别处登录
    function checkLoginName(){
		$.post("<%=request.getContextPath()%>/sso/checkLoginName", '', checkLoginNameCallback, 'json');
	}
	
	function checkLoginNameCallback(data){
		if(!data.success){
			alert(data.msg);
			window.location.href='<%=request.getContextPath()%>/logout';
		}
	}
</script>

<script>

   var showIM=false;
   $("#WebIM").click(function(){
	   if(!showIM){
		   IM.init({ useLocal : false,entId:"${entId}"});
		   showIM=true;
	   }
	   else{
		   IM.toggle();
	   }
   });

</script>
</html>