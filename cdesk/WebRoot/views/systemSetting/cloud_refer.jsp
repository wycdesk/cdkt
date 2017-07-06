<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/views/include/pageHeader.jsp"%>
<link rel="stylesheet" type="text/css"href="<%=request.getContextPath()%>/static/css/systemSetting/all.css">
<title>云客服帐号</title>
</head>
<body class="page">
    <div class="panel-frame">
                <div class="panel-wrap">
            <h2>云客服账号</h2>
<nav class="nav-tabs">
    <ul>
        <li><a href="<%=request.getContextPath()%>/cloud/account">增值服务</a></li>
        <li><a href="<%=request.getContextPath()%>/cloud/company">公司信息</a></li>
        <li><a href="<%=request.getContextPath()%>/cloud/financial">财务信息</a></li>
        <li><a href="<%=request.getContextPath()%>/cloud/invoice">发票管理</a></li>
        <li class="active"><a href="<%=request.getContextPath()%>/cloud/refer">推广计划</a></li>
    </ul>
</nav>

<div class="alert alert-warning">
    通过分享下面推广链接，成功邀请用户开通平台后，您的平台账户即可自动获赠100元费用
    <p>
        <a href="https://www.kf5.com/register?referrer=RRoY" target="_blank">https://www.kf5.com/register?referrer=RRoY</a>
    </p>
</div>

<header class="kf5-list-header">
    <h3>推广记录</h3>
</header>

    <div class="empty_block">没有内容</div>
        </div>
    </div>
    <script type="text/javascript" src="/site/page_stat/"></script>
<script type="text/javascript">
/*<![CDATA[*/

            $(".panel-frame").perfectScrollbar();
            $(".light-box-body").perfectScrollbar();
            //触发器，自动化小提示
            tip();
        
/*]]>*/
</script>
</body>
</html>