<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>平台首页</title>
    <link rel="icon" href="favicon.ico" type="image/x-icon" />
    <link rel="icon" type="image/x-icon" href="favicon.ico" />
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/normalize.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/register.css">
</head>
<body id="home">
<header class="header">
    <div class="loge">
        <img src="<%=request.getContextPath()%>/static/images/CDESK-w.svg" style="height: 60px;"/>
    </div>
    <div class="header-nav"></div>
</header>
<div class="info">
    <h2>客服，就用青牛云客服</h2>
    <p>售前售中售后的企业级云客服</p>
    <%-- <a href="<%=request.getContextPath()%>/register/gotoRegister">免费注册CDESK</a> --%>
</div>
</body>
</html>