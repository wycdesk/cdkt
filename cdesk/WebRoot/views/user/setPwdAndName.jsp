<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<title>设置密码</title>
<%@include file="/views/include/pageHeader.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/normalize.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/enter.css">
</head>
<body>
<main>
    <header class="main-header">
        <div class="wrap">
            <div>
                <h2>重置密码</h2>
            </div>
        </div>
    </header>
    <section  class="content">
        <div>
        	<form action="<%=request.getContextPath()%>/login" method="post" id="loginDt" style="display:none">
        		<input type="text" name="entId" id="entIdDis" />
        		<input type="text" name="loginName" id="loginNameDis" />
        		<input type="text" name="password" id="passwordDis" />
        	</form>
            <div class="form-login-box">
                <form>
                    <p>请在下面设置您的昵称和密码，设置后自动登录。</p>
                    <div>
                        <label>昵称</label>
                        <input type="text" maxlength="100" name="nickName" id="nickName" value="">
                    </div>
                    <div>
                        <label>用户名</label>
                        <input type="text" maxlength="100" name="userName" id="userName" value="">
                    </div>
                    <div>
                        <label>密码</label>
                        <input type="password" maxlength="100" name="password" id="password" value="">
                        <div class="password-strength">
                            <span id="password-strength"></span>
                        </div>
                    </div>
                    <div>
                        <label>请重输一次密码</label>
                        <input type="password" name="rePassword" id="rePassword" />
                    </div>
                    <input type="button" onclick="setPassword()" value="提交">
                </form>
            </div>
            <div class="form-login-box">
                <p class="tc">如果您已有账号，
                    <a href="#">请登录以追踪您提交的问题</a>
                </p>
            </div>
        </div>
    </section>
</main>

<script type="text/javascript">
function setPassword(){
	if($("#password").val()==""||$("#rePassword").val()==""||$("#nickName").val()==""||$("#userName").val()==""){
		if($("#userName").val()==""){
			$("#userName").attr("style","border:solid 3px;border-color:red;");
    		setTimeout('$("#userName").attr("style","border:;border-color:;")',150);
		}
		if($("#nickName").val()==""){
			$("#nickName").attr("style","border:solid 3px;border-color:red;");
    		setTimeout('$("#nickName").attr("style","border:;border-color:;")',150);
		}
		if($("#password").val()==""){
			$("#password").attr("style","border:solid 3px;border-color:red;");
    		setTimeout('$("#password").attr("style","border:;border-color:;")',150);
		}
		if($("#rePassword").val()==""){
			$("#rePassword").attr("style","border:solid 3px;border-color:red;");
    		setTimeout('$("#rePassword").attr("style","border:;border-color:;")',150);
		}
		return;
	}
	if($("#password").val()!=$("#rePassword").val()){
		notice.warning("前后输入密码不一致");
		return;
	}
	var param=new Object();
	param.password=$("#password").val();
	param.code='${code}';
	param.nickName=$("#nickName").val();
	param.userName=$("#userName").val();
	$.post("<%=request.getContextPath()%>/user/setPwd",param,passwordCallBack);
}
function passwordCallBack(data){
	if(data.success!=null){
		if(data.success==true){
    		notice.success(data.msg);
    		$("#entIdDis").val('${entId}');
    		$("#loginNameDis").val('${loginName}');
    		$("#passwordDis").val($("#password").val());
    		$("#loginDt").submit();
    	}else{
    		notice.warning(data.msg);
    	}
	}else{
		notice.danger("no respose!")
		updateCode();
	}
}
</script>
</body>
</html>