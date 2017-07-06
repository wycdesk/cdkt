<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>用户注册</title>
<%@include file="/views/include/pageHeader.jsp"%>
<%--      <script type="text/javascript" src="<%=request.getContextPath()%>/script/lib/jquery/jquery-1.9.1.js" ></script> --%>
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/static/css/normalize.css">
<link rel="stylesheet" type="text/css"
	href="<%=request.getContextPath()%>/static/css/register.css">
</head>
<body>
<div id="section_mailbox" class="conve regist-panel sign-panel">
	<form id="register">
		<div class="form-item">
			<label><input id="email" type="text" name="email" value
					autocomplete="off" placeholder="您的邮箱账号" />
			</label>
		</div>
		<div class="form-item">
			<input id="nickName" type="text" value name="nickName"
					autocomplete="off" placeholder="你的昵称" />
		</div>
		<div class="form-item">
			<label> <input id="picCode" type="text" value name="picCode"
				autocomplete="off" placeholder="请输入图片中的验证码" />
			</label>
		</div>

		<div class="form-item">
			<label for="remember" class="yanzheng"> <img
				id="verificationCodeImg"
				src="${pageContext.request.contextPath}/CheckCodeServlet"
				alt="这里是验证码" /> <a href="javascript:void(0);" id="changeImageLink"
				onclick="updateCode()">看不清，换一张</a>
			</label>
		</div>
		<div class="form-item">
                <a id="btn_bind_mailbox" onclick="addUser();" class="conve-next">提交申请</a>             
            </div>
	</form>
</div>
<script type="text/javascript">
if('${email}')
	$("#email").val('${email}');
function updateCode(){
	  document.getElementById("verificationCodeImg").src = "<%=request.getContextPath()%>/CheckCodeServlet"+ "?id=" + new Date().getTime();   	  
}
function addUser(){
	var param=new Object();
	param.email=$("#email").val();
	param.nickName=$("#nickName").val();
	param.picCode=$("#picCode").val();
	param.entId='${entId}';
	param.entName='${entName}';
	$.post("<%=request.getContextPath()%>/user/addUser",param,addCallBack);
}
function addCallBack(data){
	
	if(data.success==true)
		location.href="<%=request.getContextPath()%>/user/register?success=true";
	else{
		notice.warning(data.msg);
		updateCode();
	}
		
}
</script>
</body>
</html>