<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<%@include file="/views/include/pageHeader.jsp" %>
	<title>注册成功</title>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/regSuccess.css">
</head>
<body>
<div class="regSuccess-d">
	<div class="zhuce-lo" align="center">
		<img src="<%=request.getContextPath()%>/static/images/CDESK-b.png">
	</div>
	<div class="regsuccess-main">
		<div>
			<h2>恭喜你注册成功</h2>
		</div>
		<div class="reg-b">
			<div id="validate">
				<div>你的登录名为：${email}</div>
				<p>验证信息已发送至注册邮箱</p>
				<span>为了确保你账户的安全，请验证邮箱并完成以下操作</span>
				<a href="#" onclick="gotoEmailWeb();" class="yanzheng-reg">验证邮箱</a>
			</div>
			<div id="resend" style="margin-top:40px;">
				<span>没有收到邮件？</span>
				<a href="#" onclick="emailResend();"  id="resendBtn"> 重新发送</a>
				<span class="time" style="display:none"><a id="time">11</a>s &nbsp 后可重新发送</span>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
function gotoEmailWeb(){
	var email='${email}';
	var split=email.split("@");
	var suffix=split[split.length-1];
	var url="http://mail."+suffix;
	location.href=url;
}
function emailResend(){
	var param=new Object();
	param.code='${code}';
	param.email='${email}';
	$.post("<%=request.getContextPath()%>/userMongo/emailResend",param,resendCallBack,"json");
}
function resendCallBack(data){
	if(data.success){
		notice.success(data.msg);
	}else{
		notice.warning(data.msg);
	}
	$("#resendBtn").css("display","none");
	$(".time").css("display","inline");
	time();
}
function time(){
	changeNum();
	if($("#time").html()!="0"){
		setTimeout(time,1000);
	}else{
		$(".time").css("display","none");
		$("#resendBtn").css("display","inline");
		$("#time").html("11");
	}
}
function changeNum(){
	var num=$("#time").html();
	if(num=="1"){
		$("#time").html("0");
		return;
	}
	num=parseInt(num)-1;
	$("#time").html(num);
}
</script>
</body>
</html>