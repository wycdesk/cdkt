<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>常见正则表达式范例</title>
    <%@include file="/views/include/pageHeader.jsp" %>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/print.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/userField.css">
</head>
<body>
<div class="panel-frame ps-container">
<div class="placeholder_intro">
<div class="placeholder_title">下面是一些常用的正则表达式范例</div>
<dl><dt>^[1-9]\d{4,9}$</dt><dd>QQ号码</dd></dl>
<dl><dt>^(http|https):\/\/[a-z0-9]+([\-\.]{1}[a-z0-9]+)*\.[a-z]{2,5}(([0-9]{1,5})?\/.*)?$</dt><dd>URL地址</dd></dl>
<dl><dt>^[1-9]\d{5}$</dt><dd>6位的邮编号码</dd></dl>
<dl><dt>^\b([0-9]{4})-(1[0-2]|0?[1-9])-(3[0-1]|[1-2][0-9]|0?[1-9])\b$</dt><dd>yyyy-mm-dd 格式的日期</dd></dl>
<dl><dt>^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$</dt><dd>Email邮箱地址</dd></dl>
<dl><dt>^\d{3}-\d{8}|\d{4}-\d{7}$</dt><dd>国内电话号码</dd></dl>
<dl><dt>^\d{15}|\d{18}$</dt><dd>身份证号码 中国的身份证为15位或18位</dd></dl>
<dl><dt>^\d+\.\d+\.\d+\.\d+$</dt><dd>IP地址</dd></dl>
<dl><dt>^[A-Za-z]+$</dt><dd>匹配由26个英文字母组成的字符串</dd></dl>
<dl><dt>^[A-Z]+$</dt><dd>匹配由26个英文字母的大写组成的字符串</dd></dl>
<dl><dt>^[a-z]+$</dt><dd>匹配由26个英文字母的小写组成的字符串</dd></dl>
<dl><dt>^[A-Za-z0-9]+$</dt><dd>匹配由数字和26个英文字母组成的字符串</dd></dl>
<dl><dt>^\w+$</dt><dd>匹配由数字、26个英文字母或者下划线组成的字符串</dd></dl>
</div>
</div>

<script src="<%=request.getContextPath()%>/script/userField/all.min.js"></script>   
<script type="text/javascript">
    $(".panel-frame").perfectScrollbar();
</script>
</body>
</html>