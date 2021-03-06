<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>门户登陆</title>
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/${faviconUrl}" />
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/normalize.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/enter.css">

    <%-- 开发环境公网地址 --%>
    <script src='${IM_ROOT}/IM.js'></script>

    <%-- 开发环境内网地址 --%>
    <%--<script src='http://10.130.29.12:80/IM/IM.js'></script>--%>

    <%-- 测试环境 --%>
    <%--<script src='http://115.28.16.107/IM/IM.js'></script>--%>
    
    <style type="text/css">
    	.form-login-box form input.testWrong{
    		border-color: red;
    		box-shadow: 0px 0px 3px red;
    	}
    </style>
</head>
<body>

<header class="head">
    <div class="wrap hader-inner clearfix">
        <div class="logo">
            <img src="<%=request.getContextPath()%>/${logoUrl}" />
        </div>
        <!-- 
        <nav class="header-nav">
            <ul class="header-nav-list clearfix">
                <li id="one">
                    <a href="#">讨论社区</a>
                </li>
                <li>
                    <a href="#">提交新问题</a>
                </li>
            </ul>
            <div class="user-nav">
                <div class="user-info">
                    <div class="dropdown-toggle">
                        <span class="user-info-name">
                            <a href="javascript:void(0);" onclick="goLogin()">登录</a>
                        </span>
                    </div>
                </div>
                <div class="user-info">
                    <div class="dropdown-toggle">
                        <span class="user-info-name">
                            <a href="#" style="color:#428bca;">简体中文</a>
                        </span>
                    </div>
                </div>
            </div>

        </nav>
         -->
    </div>
</header>
<main>
    <!--<header class="main-header">
        <div class="wrap" id="headerTitle">
            <div class="SignIn-1">
            </div>
        </div>
    </header>
    -->
    <section  class="content">
        <!-- 用户登录 -->
        <div class="SignIn-1" id="loginDiv">
            <div class="form-login-box">
                <form action="<%=request.getContextPath()%>/login" onsubmit="return toVaildLogin()" method="post">
                    <input type="hidden" value="channelsoft" name="entId" id="UserLoginForm_endId"/>
                    <div>
                        <label for="UserLoginForm_username">登录账号</label>
                        <input class="text username form-control" placeholder="登录账号" id="UserLoginForm_username" name="loginName" type="text" />
                    </div>
                    <div>
                        <label for="UserLoginForm_password">密码</label>
                        <input class="text password form-control" placeholder="密码" id="UserLoginForm_password" name="password"  type="password" />
                    </div>
                    <p>
                        <input class="checkbox" id="phoneCheckBox" name="usePhone"  value="1" type="checkbox" />
                        <label for="rmCheckBox">启用电话功能</label>
                        <div id = "phoneDiv" style = "display:none">
	                        <label>电话号码</label>
	                        <input class="text form-control" placeholder="请输入手机号" id="phoneNumber" name="phoneNumber"  type="text" />
                    	</div>
                    </p>
                    <p>
                        <input class="checkbox" id="rmCheckBox" name="rememberMe"  value="1" type="checkbox" />
                        <label for="rmCheckBox">在本机记住我，下次自动登录</label>
                    </p>
                    <p style="color:red;" id="errMsg">${errMsg}</p>
                    <input type="submit" name="yt0" value="登录">
                </form>
            </div>
            <div class="form-login-box">
                <p class="tc"><a href="javascript:void(0);" onclick="goForgetPwd()">没有密码？忘记密码？</a></p>
            </div>
            <div class="form-login-box">
                <p class="tc">第一次使用我们的服务？
                    <a href="javascript:void(0);" onClick="goRegisterUser()">请先注册以提交和追踪问题</a>
                </p>
            </div>
        </div>

        <!-- 忘记密码 -->
        <div class="SignIn-2" id="pwdDiv">
            <div class="form-login-box">
                <form class="yw0">
                    <p>请输入您的注册邮箱，我们会发送邮件让您设置密码</p>
                    <div>
                        <label>邮箱地址</label>
                        <input class="text username" placeholder="邮箱地址" name="User[username]" id="emailPwd" type="text" />
                    </div>
                    <div>
                        <label>验证码</label>
                        <input class="text captcha_field" placeholder="验证码" name="User[code]" id="picCodePwd" type="text" />
                        <div class="code-pic">
                            <img id="verificationCodeImgPwd" src="<%=request.getContextPath()%>/CheckCodeServlet" alt="" />
                            <a id="yw1_button" href="javascript:void(0);" onclick="updatePwdCode()">刷新验证码</a>
                        </div>
                    </div>
                    <input class="btn_main" type="button" name="yt0" onclick="goResetPwd()" value="提交" />
                </form>
            </div>
            <div class="form-login-box">
                <p class="tc">如果您已有账号，
                    <a href="javascript:void(0);" onClick="goLogin()">请登录以追踪您提交的问题</a>
                </p>
            </div>
        </div>
        <!--用户注册 -->
        <div class="SignIn-3" id="registDiv">
            <div class="form-login-box">
                <form id="member-form">
                    <div>
                        <label for="User_username">邮箱账号</label>
                        <input class="text username" placeholder="邮箱账号" id="email" name="email"  type="text" />
                    </div>
                    <div>
                        <label for="User_nickName">昵称</label>
                        <input class="text username" placeholder="你的昵称" id="nickName" name="nickName"  type="text"  />
                    </div>
                    <div>
                        <label for="User_code">输入验证码</label>
                        <input class="text captcha_field" placeholder="验证码" name="picCode" id="picCode" type="text"  />
                        <div class="code-pic">
                            <img id="verificationCodeImg" class="yw0" src="<%=request.getContextPath()%>/CheckCodeServlet" alt="" />
                            <a id="yw0_button" href="javascript:void(0);" onclick="updateCode()">看不清，换一张</a>
                        </div>
                    </div>
                    <input class="btn_main" type="button" name="yt0" value="注册" onclick="registerBase()" />
                </form>
            </div>
            <div class="form-login-box">
                <p class="tc">
                    如果您已有账号，<a href="javascript:void(0);" onClick="goLogin()">请登录以追踪您提交的问题</a>
                </p>
            </div>
        	<!-- 注册后跳转页面   -->
        	<form style="display:none" action="<%=request.getContextPath()%>/userMongo/regSuccess" id="registJump" method="post">
        		<input type="text" id="emailForJump" name="email" />
        		<input type="text" id="nickForJump" name="nickName" />
        		<input type="text" id="codeForJump" name="code" />
        	</form>
        </div>
    </section>
     <!-- <a href="javascript:void(0)" id="WebIM" class="support-btn" >帮助</a>  -->
</main>
<script type="text/javascript">
    $(function(){
        if('${regist}'=="yes")
            goRegisterUser();
        else{
            $('#loginDiv').attr("style","display:");
            $('#pwdDiv').attr("style","display:none");
            $('#registDiv').attr("style","display:none");
        }
        getEntId();
        
        //启用电话功能控制
        $("#phoneCheckBox").change(function(){
        	if($(this).prop("checked")){
        		$("#phoneDiv").show();
        	}else{
        		$("#phoneDiv").hide();
        		$("#phoneNumber").val("");
        		$("#phoneNumber").removeClass("testWrong");
        	}
        });
        
        $("#phoneNumber").blur(function(){
        	if($("#phoneCheckBox").prop("checked")){
        		if($(this).val()){
        			if(!validateTel($(this).val())){
       					notice.warning("请输入一个电话号码");
           				$("#phoneNumber").addClass("testWrong");
        			}else{
        				$("#phoneNumber").removeClass("testWrong");
        				if($("#errMsg").text() == "请输入正确的电话号码"){
        					$("#errMsg").text("");
        				}
        			}
        		}
        	}
        });
        
        
        
    });
    
    //验证电话号码
    function validateTel(phone){
    	var regx = /^\d{11,11}$/;
    	if(regx.test(phone)){
    		return true;
    	}
    	return false;
    }
    
    function getEntId() {
        var hostName = location.host;
        var entId = hostName.substring(0, hostName.indexOf("."));
        $("#UserLoginForm_endId").val(entId);
    }
    function goResetPwd(){
    	if(!mailFormat($("#emailPwd").val())){
    		return ;
    	}
    	if($("#emailPwd").val()==""||$("#picCodePwd").val()==""){
    		
    		if($("#emailPwd").val()==""){
    			$("#emailPwd").attr("style","border:solid 3px;border-color:red;");
        		setTimeout('$("#emailPwd").attr("style","border:;border-color:;")',150);
    		}
    		if($("#picCodePwd").val()==""){
    			$("#picCodePwd").attr("style","border:solid 3px;border-color:red;");
        		setTimeout('$("#picCodePwd").attr("style","border:;border-color:;")',150);
    		}
    		return;
    	}
    	var param=new Object();
    	param.email=$("#emailPwd").val();
    	param.picCode=$("#picCodePwd").val();
    	$.post("<%=request.getContextPath()%>/userMongo/forgetPwd",param,forgetCallBack);
    }
    function forgetCallBack(data){
    	if(data.success!=null){
    		if(data.success==true){
        		notice.alert(data.msg);
        	}else{
        		notice.alert(data.msg);
        		updatePwdCode();
        	}
    	}else{
    		notice.alert("no respose!")
    		updatePwdCode();
    	}
    }
    function goForgetPwd(){
    	updatePwdCode();
    	$('#loginDiv').attr("style","display:none");
        $('#pwdDiv').attr("style","display:");
        $('#registDiv').attr("style","display:none");
        $('.SignIn-1 h2').html("忘记密码");
    }
    function goRegisterUser(){
    	updateCode();
        if('${email}'!=null)
            $("#registDiv #email").val('${email}');
        $('#loginDiv').attr("style","display:none");
        $('#pwdDiv').attr("style","display:none");
        $('#registDiv').attr("style","display:");
        $('.SignIn-1 h2').html("用户注册");
    }
    function goLogin(){
    	//updateCode();
        $('#loginDiv').attr("style","display:");
        $('#pwdDiv').attr("style","display:none");
        $('#registDiv').attr("style","display:none");
        $('.SignIn-1 h2').html("用户登录");
    }
    function updateCode(){

        document.getElementById("verificationCodeImg").src = "<%=request.getContextPath()%>/CheckCodeServlet"+ "?id=" + new Date().getTime();
    }
    function updatePwdCode(){
    	document.getElementById("verificationCodeImgPwd").src = "<%=request.getContextPath()%>/CheckCodeServlet"+ "?id=" + new Date().getTime();   	  
    }
    function registerBase(){
    	if(!mailFormat($("#email").val())){
    		return ;
    	}
        if($("#email").val()==""||$("#picCode").val()==""||$("#nickName").val()==""){
            if($("#email").val()==""){
                $("#email").attr("style","border:solid 3px;border-color:red;");
                setTimeout('$("#email").attr("style","border:;border-color:;")',150);
            }
            if($("#picCode").val()==""){
                $("#picCode").attr("style","border:solid 3px;border-color:red;");
                setTimeout('$("#picCode").attr("style","border:;border-color:;")',150);
            }
            if($("#nickName").val()==""){
                $("#nickName").attr("style","border:solid 3px;border-color:red;");
                setTimeout('$("#nickName").attr("style","border:;border-color:;")',150);
            }
            return;
        }
        var param=new Object();
        param.email=$("#email").val();
        param.nickName=$("#nickName").val();
        param.picCode=$("#picCode").val();
        $.post("<%=request.getContextPath()%>/userMongo/registerBase",param,baseCallBack);
    }
    function baseCallBack(data){
    	if(data.success){
    		notice.alert(data.msg);
    		$("#emailForJump").val($("#email").val());
        	$("#nickForJump").val($("#nickName").val());
        	$("#codeForJump").val(data.rows);
        	$("#registJump").submit();
    	}else{
    		notice.warning(data.msg);
    		updateCode();
    	}
        //location.href="<%=request.getContextPath()%>/userMongo/regSuccess";
    }
    
    function toVaildLogin(){
    	var userName=$('#UserLoginForm_username').val();
    	if(!userName){
    	   	$('#errMsg').text("请输入登录帐号");
    		return false;
    	}
 
    	var pwd=$('#UserLoginForm_password').val();
       	if(!pwd){
    		$('#errMsg').text("请输入密码");
    		return false;
    	}
       	
       	var usePhone = $("#phoneCheckBox").prop("checked");
       	if(usePhone){
       		if($("#phoneNumber").val()){
       			if(!validateTel($("#phoneNumber").val())){
       				$('#errMsg').text("请输入正确的电话号码");
       	    		return false;
       			}
       		}
       	}
   
    }
</script>


<script>

IM.init({ useLocal : false,entId:'${ccodEntId}',skillGroupId:'${skillId}',type:'IM',customPriority : '1000'});

/*  邮箱格式校验 */
function mailFormat(email){    
    var patrn = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
    if (patrn.test(email)){
        return true;
    }else{
        notice.warning("请输入正确的邮箱格式！");
        return false;
    }
}
</script>
</body>
</html>