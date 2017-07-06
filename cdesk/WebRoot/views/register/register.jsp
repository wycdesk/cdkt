<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/register.css">
     <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/console.css">
</head>
<body class="zhuce">
<div class="slides">
    <div id="section_mailbox" class="conve regist-panel sign-panel">
        <div class="loge">
            <img src="../static/images/logo.png" alt="青牛CDESK"/>
            <span>企业注册</span>
        </div>

        <!-- 注册第一步 -->
        <form  id="registStep1">
            <div class="form-item">
                <label>
                    <input id="email" type="text" autocomplete="off" placeholder="您的邮箱账号"/>
                    <div class="tongguo">
                        <img src="../static/images/Check.png" />
                    </div>
                </label>
            </div>
            <div class="form-item">
                <label>
                    <input id="picCode" type="text" value autocomplete="off" placeholder="请输入图片中的验证码"/>
                    <div class="tongguo">
                        <img src="../static/images/Check.png" />
                    </div>
                </label>
            </div>

            <div class="form-item">
                <label class="yanzheng">
                    <img id="verificationCodeImg" src="${pageContext.request.contextPath}/CheckCodeServlet" alt="这里是验证码"/>

                    <a href="javascript:void(0);" id="changeImageLink" onclick="updateCode()">看不清，换一张</a>
                </label>
            </div>

            <div class="form-item">
                <p class="terms">"请阅读青牛CDESK"
                    <a target="_blank" href="<%=request.getContextPath()%>/register/policy">《服务条款》</a>
                </p>
                <a id="btn_bind_mailbox" onclick="bindMailBox1()" class="conve-next">同意服务条款并注册</a>
            </div>
        </form>

        <!--    注册第二步 -->
        <form action="<%=request.getContextPath()%>/login" id="loginDt" method="post" style="display:none">
        		<input type="text" name="entId" id="entIdDis" />
        		<input type="text" name="loginName" id="loginNameDis" />
        		<input type="text" name="password" id="passwordDis" />
        		<input type="text" name="email" id="emailDis" />
        </form>       
        <form  id="registStep2" >
            <div class="form-item" >
                <label>
                    <input id="register" type="text" value autocomplete="off" placeholder="姓名" />
                    <div class="tongguo">
                        <img src="../static/images/Check.png" />
                    </div>
                </label>
            </div>
            <div class="form-item">
                <div class="guding">
                    <span id="mailbox"></span>
                    <div class="tongguo">
                        <img src="../static/images/Check.png" />
                    </div>
                </div>
            </div>
            <div class="form-item">
                <input id="password" type="password" value autocomplete="off" placeholder="密码" />

            </div>
            <div class="form-item">
                <label>
                    <input id="entName" type="text" value autocomplete="off" placeholder="企业名称" />
                    <div class="tongguo">
                        <img src="../static/images/Check.png" />
                    </div>
                </label>
            </div>
            <div class="form-item " >
                <label class="yuming">
					<span style="position:relative;">
						<input id="domainName" type="text" value autocomplete="off" placeholder="域名" />
						<span>${domainLast}</span>
					</span>
                    <p id="tishi1" class="terms">
                    </p>
                </label>
            </div>
            <div class="form-item">
                <a id="btn_bind_mailbox" onclick="bindMailBox2()" class="conve-next">完成注册</a>
            </div>
        </form>

        <!-- 注册第三步 -->
        <form action="<%=request.getContextPath()%>/register/registerStep3" id="jump" method="post" style="display:none">
        		<input type="text" name="register" id="registerDis" />
        		<input type="text" name="email" id="emailDis1" />
        		<input type="text" name="password" id="passwordDis1" />
        		<input type="text" name="entName" id="entNameDis" />
        		<input type="text" name="domainName" id="domainNameDis" />
        		<input type="text" name="activeCode" id="activeCodeDis" />       		
        </form>            
        
        <div class="tishi" id="registStep3">
            正在<span><span id=eId></span></span>上创建您的云客服平台，请稍等几秒钟，请不要刷新网页！
        </div>

    </div>
</div>

<script type="text/javascript">
    $(function(){  	
        document.getElementById('email').onblur = function(){
            var email=$("#email").val();
            $.post("<%=request.getContextPath()%>/register/checkEmail?email="+email, checkEmailCallBack, 'json');
        };
        /*  通过后台返回值控制页面样式*/
        if("${step}"=="step1"){
            $('#registStep1').show();
        }else if("${step}"=="step2"){
            $('#registStep2').show();
            $("#tishi1").html("请设置您的客服平台域名地址，如 company" + "${domainLast}" + " ,设置后不能更改");
            var activeCode="${activeCode}";
            getEmail();       
           /* 点击的非最新的激活码链接时 */
            if("${outTime}"== "true"){
                alert("此链接已过期，请点击最新的链接或重新获取激活码!");
                /* 返回注册首页 */
                window.location.href="<%=request.getContextPath()%>/register/gotoRegister";
            }
           /*  最新的激活码链接已超时 */
            if("${invalid}"== "true"){
                alert("激活码已超时，请重新注册获取激活码!");
                /* 返回注册首页 */
                window.location.href="<%=request.getContextPath()%>/register/gotoRegister";
            }   
        }else if("${step}"=="step3"){         
           var check = "{check}";
           if("${check}"=="true"){
        	    notice.warning("此邮箱已注册，请更换邮箱！");
            	 /* 已注册,跳转至注册首页 */
                window.location.href="<%=request.getContextPath()%>/register/gotoRegister";
           }else if("${check}"=="false"){     	
            $('#registStep3').show();

            var domainName="${domainName}";
            var domainLast="${domainLast}";
            document.getElementById("eId").innerText = domainName+domainLast ;
            $("#eId").html(domainName+domainLast);
            
            gotoRegister();
           } 
        }
    });

    /* 检查邮箱回调函数 */
    function checkEmailCallBack(data){
        if(!data.success){
        	notice.danger(data.msg);
        }
    }

    /* 通过激活码获取邮箱 */
    function getEmail(){
        var activeCode="<%=request.getParameter("activeCode")%>";
        $.post("<%=request.getContextPath()%>/register/queryEmail?activeCode="+activeCode, emailCallBack, 'json');
    }

    /* 获取邮箱回调函数 */
    function emailCallBack(data){
        if(data.success){
            var email=data.rows;
            document.getElementById("mailbox").innerText = email ;
            $("#mailbox").html(email);
        }
    }

    /*  改变验证码     */
    function updateCode(){
        document.getElementById("verificationCodeImg").src = "<%=request.getContextPath()%>/CheckCodeServlet"+ "?id=" + new Date().getTime();
    }

    /* 同意条款并注册 */
    function bindMailBox1(){
    	/* 邮箱格式校验 */
    	var email = $("#email").val();       
        if(!mailFormat(email)){
        	return;
        }
        $.ajax({
            url : "<%=request.getContextPath()%>/register/registerStep1",
            type : "post",
            dataType : "json",
            data : {
                email :   $("#email").val(),
                picCode : $("#picCode").val(),
                ip:       "${ip}"
            },
            success : function (data) {
                if (data.success) {
                    notice.success("激活邮件已发送至您的邮箱，请在30分钟内点击激活链接完成注册！");
                } else {
                    notice.danger("注册失败！"+data.msg);
                }
            }
        });
    }

    /* 跳转处理页面  */
    function bindMailBox2(){
        var register = $("#register").val();
        var email = $("#mailbox").html();
        var password = $("#password").val();
        var entName = $("#entName").val();
        var domainName = $("#domainName").val();
        var activeCode = "${activeCode}";        
        /* 输入框校验 */
        if(checkInput(register,password,entName,domainName)=="false"){
        	return;
        }
        /* 域名校验 */
 	    if(!checkDomain(domainName)){
		   return;
	    }       
		$("#registerDis").val(register);
		$("#emailDis1").val(email);
		$("#passwordDis1").val(password);				
		$("#entNameDis").val(entName);
		$("#domainNameDis").val(domainName);
		$("#activeCodeDis").val(activeCode);

		$("#jump").submit();        	 
    }

    /* 注册企业用户,处理完毕跳转登录页面 */
    function gotoRegister(){ 	
        $.ajax({
            url : '<%=request.getContextPath()%>/register/register2',
            type : "post",
            dataType : "json",
            data : {
                register :  "${register}",
                email :     "${email}",
                password :  "${password}",
                entName :   "${entName}",
                domainName :"${domainName}",
                activeCode :"${activeCode}"
            },
            success : function (data) {
                    if (data.success) {                  	   
                        /*处理完毕2秒后跳转到登录页面 */
                        var rows=JSON.parse(data.rows);                         
                        var entId=rows.entId;
                        var loginName=rows.loginName;
                        var password=rows.password;
                        var email=rows.email;     
                        
                       setTimeout(function(){                          
                    		$("#entIdDis").val(rows.entId);
                    		$("#loginNameDis").val(rows.loginName);
                    		$("#passwordDis").val(rows.password);
                    		$("#emailDis").val(rows.email);
                    		//企业开户成功后自动登录
                    		var plat=window.location.host;
                    		if(plat.indexOf("www")>=0){
                    			plat=plat.replace("www",rows.entId);
                    		}                                          	
                    		var url="http://"+plat+"<%=request.getContextPath()%>/login";   
                    		$("#loginDt").attr("action",url);
                    		$("#loginDt").submit();
                        },2000);                       
                    }else {
                    alert("注册失败！"+data.msg);
                    /* 注册失败，返回注册首页 */
                      window.location.href="<%=request.getContextPath()%>/register/gotoRegister";
                    }
            }
        });
    }
       
   /*  域名校验 */
   function checkDomain(domain)
   {
    var patrn=/^[a-zA-Z0-9][-a-zA-Z0-9]{0,20}$/;
    if (!patrn.test(domain)){
        notice.warning("域名必须由英文字母，数字和-组成，且第一个字母不能是-,长度不能大于20！");
        return false;
    }else{
    	return true;
    }   	
   }

   /* 输入框校验 */
   function checkInput(register,password,entName){
	   if(register =="" || register.trim().length==0){
		   notice.warning("创建者姓名不能为空！");
		   return "false";
	   }
	   if(password == ""){
		   notice.warning("密码不能为空！");
		   return "false";
	   }
	   if(password.length<6){
		   notice.warning("密码长度必须大于等于6位！");
	       return "false";
	   }
	   if(entName =="" || entName.trim().length==0){
		   notice.warning("企业名不能为空！");
		   return "false";
	   }
	   if(domainName ==""){
		   notice.warning("域名不能为空！");
		   return "false";
	   }
	   else{
		   return "true";
	   }
   }
   
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