<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<%@include file="/views/include/pageHeader.jsp"%>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/systemSetting/all.css">
<title>企业注销</title>
</head>
<body>
<div class="field field-operation">
	<input type="submit" name="yt0" value="企业注销" onclick="enterpriseCancel()" style="float:left;font-size:18px">
</div>
<span class="b" style="font-size:18px">
<font color="red">注意：</font>
企业注销成功后，平台库中的该企业信息和该企业对应的数据库将被删除且不能恢复。</span>
<script type="text/javascript">
function enterpriseCancel(){
	//alert('${entId}');
	var param=new Object();
	param.entId='${entId}';
	param.userType='${userType}';
	if(confirm("确定要注销企业${entId}?"))
		$.post("<%=request.getContextPath()%>/systemSetting/goCancel",param,cancelCallBack,'json');
}
function cancelCallBack(data){
	if(data.success){
		notice.alert(data.msg);
		var ent=location.host;
		if(ent.indexOf(data.rows)>=0){
			ent=ent.replace(data.rows,"www");
		}
		var url="http://"+ent+"<%=request.getContextPath()%>";
		parent.parent.location.href=url;
	}else{
		notice.alert(data.msg);
	}
}
</script>
</body>
</html>