<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
        <meta charset="UTF-8">
        <title>复制角色</title>
        <link rel="icon" href="favicon.ico" type="image/x-icon" />
        <link rel="icon" type="image/x-icon" href="favicon.ico" />
        <%@include file="/views/include/pageHeader.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/privilege.css">
</head>
<body id="home">
<div class="panel-wrap">
<ul class="breadcrumbs">
	<li>
		<a href="<%=request.getContextPath()%>/role/list">客服权限和角色</a>
	</li>
	<li>编辑角色</li>
</ul>
<form action="<%=request.getContextPath()%>/role/update/${role.id}" method="post" id="roleForm">
<div class="field t-col">
	<h4>角色名称</h4>
	<div class="t-col-content mb20"><input type="text" value="复制：${role.name}" name="name" id="roleName" /></div>
</div>
<div class="field t-col">
	<h4>角色描述</h4>
	<div class="t-col-content">
		<textarea class="shuru" rows="3" cols="80" name="description" id="roleDesc">${role.description}</textarea>
		<p class="hint">对该设定的角色进行描述，将显示在具体用户选择角色的页面</p>
	</div>
</div>
</form>
<form id="permissionForm">
<c:forEach items="${permissionList}" var="item">
	<div class="field t-col">
		<h4>
		<label>
		<c:choose>
			<c:when test="${item.check}"><input type="checkbox" checked="checked" name="${item.id}" /></c:when>
			<c:otherwise><input type="checkbox" name="${item.id}" /></c:otherwise>
		</c:choose>
		${item.name}
		</label>
		</h4>
		<div class="t-col-content mb20">
		<c:forEach items="${item.children}" var="item2">
			<div class="mb5">
			<label>
			<c:choose>
				<c:when test="${item2.check}"><input type="checkbox" checked="checked" name="${item2.id}" /></c:when>
				<c:otherwise><input type="checkbox" name="${item2.id}" /></c:otherwise>
			</c:choose>
			${item2.name}
			</label>
			</div>
		</c:forEach>
		</div>
	</div>
	<hr />
</c:forEach>
<div class="field field-operation">
	<input type="button" value="提交" id="formSubmit" />
</div>
</form>
</div>
<script type="text/javascript">
$("#formSubmit").click(function () {
	var name = $("#roleName").val();
	var description = $("#roleDesc").val();
	var permissionIds = "";
	var pFormArr = $("#permissionForm").serializeArray();
	for (var i = 0; i < pFormArr.length; i++) {
		var item = pFormArr[i];
		if (i < pFormArr.length - 1) {
			permissionIds += item.name + ",";
		} else {
			permissionIds += item.name;
		}
	}
	$.post("<%=request.getContextPath()%>/role/copyRole/" + permissionIds, {name:name, description: description}, updateCallBack, 'json');
});

function updateCallBack(data) {
	if (data.success) {
		notice.alert(data.msg);
		window.location.href = "<%=request.getContextPath()%>/role/list";
	} else {
		notice.alert(data.msg,'alert-danger');
	}
}
</script>
</body>
</html>