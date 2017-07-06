<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
        <meta charset="UTF-8">
        <title>角色</title>
        <link rel="icon" href="favicon.ico" type="image/x-icon" />
        <link rel="icon" type="image/x-icon" href="favicon.ico" />
        <%@include file="/views/include/pageHeader.jsp"%>
		<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/privilege.css">
</head>
<body id="home" style="background: none;">
<div class="panel-wrap" style="background: none;">
	<div class="table-uil">
		<div class="title-name">客服角色名</div>
		<c:forEach items="${list}" var="item">
			<div class="btn-group">
				<div class="acatar">
					<img src="<%=request.getContextPath()%>/H+3.2/img/profile_small.jpg">
				</div>

				<div style="width:50%;">
					<p>
						<a href="javascript:void(0);" onclick="gotoRoleUser('${item.id}')">${item.name}</a>
						(${item.userCount})
					</p>
					<p>${item.description}</p>
				</div>
				<div class="ar">
					<c:if test="${item.isCustom == '1'}">
						<a href="javascript:void(0);" onclick="deleteRole('${item.id}')" >删除</a>
					</c:if>
					<a href="<%=request.getContextPath()%>/role/copy/${item.id}">复制</a>
					<a href="<%=request.getContextPath()%>/role/edit/${item.id}">编辑</a>
				</div>
			</div>
		</c:forEach>
	</div>
</div>

<script type="text/javascript">
function deleteRole(id) {
	if (confirm("确定要删除吗？删除后不可恢复！")) {
		$.post("<%=request.getContextPath()%>/role/delete/" + id, "", deleteCallBack, 'json');
	}
}
function deleteCallBack(data) {
	if (data.success) {
		notice.alert(data.msg);
		window.location.href = "<%=request.getContextPath()%>/role/list";
	} else {
		notice.alert(data.msg,'alert-danger');
	}
}
function gotoRoleUser(roleId) {
	parent.gotoRoleUser(roleId);
}
</script>
</body>
</html>