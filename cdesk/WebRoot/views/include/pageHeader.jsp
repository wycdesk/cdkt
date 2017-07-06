<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!--[if lt IE 8
    <script>
        alert('已不支持IE6-8，请使用谷歌、火狐等浏览器\n或360、QQ等国产浏览器的极速模式浏览本页面！');
    </script>
<![endif]-->
<!--<link rel="stylesheet" href="http://fonts.useso.com/css?family=Roboto:300,400,500,700" type="text/css">-->
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/normalize.css">
<link href="<%=request.getContextPath()%>/H+3.2/css/bootstrap.min.css?v=3.4.0" rel="stylesheet">
<link href="<%=request.getContextPath()%>/H+3.2/css/animate.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/script/lib/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/script/lib/bootstrap-material-design-master/dist/css/bootstrap-material-design.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/script/lib/bootstrap-material-design-master/dist/css/ripples.min.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/script/lib/sweetalert-master/dist/sweetalert.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/script/lib/tooltipster-master/css/tooltipster.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/script/lib/dropzone/dropzone.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/static/css/common.css" rel="stylesheet">

<script src="<%=request.getContextPath()%>/H+3.2/js/jquery-2.1.1.min.js" ></script>
<script src="<%=request.getContextPath()%>/script/lib/tooltipster-master/js/jquery.tooltipster.js" ></script>
<script src="<%=request.getContextPath()%>/H+3.2/js/bootstrap.min.js?v=3.4.0" ></script>
<script src="<%=request.getContextPath()%>/script/lib/bootstrap-material-design-master/dist/js/material.js" ></script>
<script src="<%=request.getContextPath()%>/script/lib/q.js" ></script>
<!--
<script src="<%=request.getContextPath()%>/script/lib/bootstrap-material-design-master/dist/js/ripples.js" ></script>
-->
<script src="<%=request.getContextPath()%>/script/lib/sweetalert-master/dist/sweetalert.min.js" ></script>
<script src="<%=request.getContextPath()%>/script/lib/handlebars.js" ></script>
<script src="<%=request.getContextPath()%>/script/lib/jquery.cookie.js" ></script>
<script src="<%=request.getContextPath()%>/script/lib/dropzone/dropzone.js" ></script>
<script src="<%=request.getContextPath()%>/script/lib/func.js" ></script>
<script type="text/javascript"> 
   //判断是否需要重新登陆
    function goToLogin(msg){
    	if(msg){
    		if(msg=="没有登录或会话过期,请重新登录"){
    			alert('没有登录或会话过期,请重新登录');
    		  	top.location.href='<%=request.getContextPath()%>/logout';
    		  	return true;
    		}
    	}	
      return false;
    }
</script>
