<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>我的活动</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/normalize.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/enter.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/order.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/activity.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
	<%--<script src="http://123.56.112.31/IM/IM.js"></script>--%>

    <%-- 开发环境 --%>
    <script src='${IM_ROOT}/IM.js'></script> 

    <%-- 测试环境 
    <script src='http://115.28.16.107/IM/IM.js'></script>
    --%>
    
    <style type="text/css">
	.user-setting-form #password-strength:after {
	    width: 0%;
	}
</style>
</head>
<body>
<header class="head">
    <div class="wrap hader-inner">
        <div class="logo">
            <img src="<%=request.getContextPath()%>/static/images/logo.png" />
        </div>
        
       <!-- <a href="javascript:void(0)" id="WebIM" class="support-btn" >帮助</a> --> 
                
        <nav class="header-nav">
            <ul class="header-nav-list">
            	
                <li>
                    <a href="<%=request.getContextPath()%>/newquestion/index">提交新问题</a>
                </li>
                  <li><a href="<%=request.getContextPath()%>/logout">退出</a>
                  </li>
            </ul>
            <div class="user-nav">
            	<% if (request.getAttribute("user") != null) { %>
                <div class="dropdown profile-element" style="padding:0px;">
                    <a data-toggle="dropdown" class="dropdown-toggle user" href="#">
                        <span class="avatar">
                            <img alt="image" class="img-circle" src="<%=request.getContextPath()%>/${photoUrl}" />
                        </span>
  						<span class="text-muted text-xs block username">${user.userName}</span>
                    </a>
              
                </div>
                <%} else { %>
                <div class="user-info">
                    <div class="dropdown-toggle">
							<span class="user-info-name">
								<a href="<%=request.getContextPath()%>/login">登录</a>
							</span>
                    </div>
                </div>
				<%} %>
        
            </div>
        </nav>
    </div>
</header>
<main>
    <section class="content">
        <div class="wrap user-center">
            <nav class="sub-nav">
                <ul>
                    <li><a href="#">首页</a></li>
                    <li> > </li>
                    <li>个人中心</li>
                </ul>
            </nav>
            <section class="user-center-sidebar">
            <nav class="user-center-nav" style="z-index: 888;">
                <ul id="userCenterNavList">
                    <li><a class="active" data-link="#activity-main2" onclick="queryAllData()">我的提问</a></li>
                    <li><a data-link="#activity-main5">我的资料</a></li>
                </ul>
            </nav>
            </section>

            <section class="user-center-main" id="userCenterMainList">
                <!-- 我的消息
                <div id="activity-main1" class="user-center-container">
                    <h2>我的消息<span>（0）</span></h2>
                    <ul class="user-center-msg-list"></ul>
                </div>
                -->
                <!-- 我的提问 -->
                <div id="activity-main2" class="user-center-container">
                    <div class="user-center-ticket-filter">
                        <div class="dropdown">
                            <a id="dLabel" class="title" data-target="#"  data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                <span id="nowStatus">全部</span> 	 
                                <span class="caret"></span>
                            </a>
                            <ul id="OrderStatus" class="dropdown-menu" aria-labelledby="dLabel">
                                <li class="active"><a onclick="queryAllData()">全部</a></li>
                                <li><a onclick="waitHandle()">尚未受理</a></li>
                                <li><a onclick="Handling()">受理中</a></li>
                                <li><a onclick="waitReplay()">等待回复</a></li>
                                <li><a onclick="Handled()">已解决</a></li>
                                <li><a onclick="closed()">已关闭</a></li>
                            </ul>
                        </div>
                        <form  class="ticket-search" method="get">
                            <label>
                                <input type="search"  name="keyword" value="" placeholder="搜索提问">
                            </label>
                        </form>
                    </div>
                <!-- 坐席客服 -->
                    <div  style="display: none;">
                        <table class="ticket-table-list">
                            <thead>
                                <tr>
                                    <th style="">标题</th>
                                    <th style="">问题描述</th>
                                    <th style="width:80px;">状态</th>
                                </tr>
                            </thead>
                            <tbody>
                            <tr >
                                <td>
                                    <div class="field-title" style="width: 388px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;" data-width="388">
                                        <a href="" title="测试测试">测试测试</a>
                                    </div>
                                </td>
                                <td>12345</td>
                                <td><span class="color-label orange0">尚未受理</span></td>
                            </tr>
                            <tr>
                                <td>
                                    <div class="field-title" style="width: 388px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;" data-width="388">
                                        <a href="" title="12345">12345</a>
                                    </div>
                                </td>
                                <td>23额外热体育局</td>
                                <td><span class="color-label green0">已解决</span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>
                    <!-- 普通用户 -->
                    <div>
                        <table class="ticket-table-list">
                            <thead>
                            <tr>
                                <th style="min-width:50px;">编号</th>
                                <th style="">标题</th>
                                <th style="width:60px;">状态</th>
                                <th style="min-width:110px;">创建日期</th>
                                <th style="">受理客服组</th>
                                <th style="">工单受理客服</th>
                            </tr>
                            </thead>
                            <tbody id="myQlist">
                            <!-- <tr>
                                <td>1</td>
                                <td>
                                    <div class="field-title" style="width: 256px; overflow: hidden; white-space: nowrap; text-overflow: ellipsis;" data-width="256">
                                        <a href="" title="lai 您好,您有一个测试工单需要处理">lai 您好,您有一个测试工单需要处理</a>
                                    </div>
                                </td>
                                <td>
                                    <span class="color-label red0">受理中</span>
                                </td>
                                <td >11月03日 15:45</td>
                                <td >qn支持组</td>
                                <td>永</td>
                            </tr> -->
                            </tbody>
                            <tfoot>
                       		 	<tr>
                            	<td colspan="6">
                                	<div id="pagination"></div>
                           		</td>
                        		</tr>
                        	</tfoot>
                        </table>
                    </div>
                </div>
                <!--我的发表 -->
                <div id="activity-main3" class="user-center-container">
                    <!-- Nav tabs -->
                    <nav class="navbar navbar-default">
                        <ul class="nav navbar-nav" role="tablist">
                            <li role="presentation" class="active">
                                <a href="#word" aria-controls="word" role="tab" data-toggle="tab">文档<span>（1）</span></a>
                            </li>
                            <li role="presentation">
                                <a href="#update" aria-controls="update" role="tab" data-toggle="tab">文档回复<span>（0）</span></a>
                            </li>
                            <li role="presentation">
                                <a href="#community" aria-controls="community" role="tab" data-toggle="tab">社区问题<span>（0）</span></a>
                            </li>
                            <li role="presentation">
                                <a href="#discussion" aria-controls="discussion" role="tab" data-toggle="tab">讨论<span>（0）</span></a>
                            </li>
                            <li role="presentation">
                                <a href="#vote" aria-controls="vote" role="tab" data-toggle="tab">文档投票<span>（3）</span></a>
                            </li>
                        </ul>
                    </nav>

                    <!-- Tab panes -->
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane active" id="word">
                            <ul class="question-bbs-list">
                                <li>
                                    <a href="" target="_blank">
                                        <img class="question-bbs-item-pic" src="<%=request.getContextPath()%>/H+3.2/img/profile_small.jpg"></a>
                                    <div class="question-bbs-item-main">
                                        <div class="question-bbs-item-title">
                                            <h3><a href="">来自 01068663352 的语音电话</a></h3>
                                            <p class="info">
                                                <span class="author">
                                                    <a href="" target="_blank">demo</a>
                                                </span> •
                                                <time>发表于 2015年11月29日</time> •
                                                <span>1 个投票 </span> •
                                                <span>0 关注</span>
                                                <!--• <a href="###">意见与反馈</a>-->
                                            </p>
                                        </div>
                                        <div class="question-bbs-item-right">
                                            <span class="number">0 回复</span>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="update">

                        </div>
                        <div role="tabpanel" class="tab-pane" id="community">

                        </div>
                        <div role="tabpanel" class="tab-pane" id="discussion">

                        </div>
                        <div role="tabpanel" class="tab-pane" id="vote">
                            <ul class="user-center-msg-list">
                                <li>
                                    <!--<span class="author">樱桃小丸子</span>在帖子-->
                                    我投票了文档
                                    <a href="">《来自 01068663352 的语音电话》</a>
                                    <time>（2015-11-29 12:47）</time>
                                </li>
                                <li>
                                    <!--<span class="author">樱桃小丸子</span>在帖子-->
                                    我投票了文档
                                    <a href="">《测试文档》</a>
                                    <time>（2015-11-22 22:16）</time>
                                </li>
                                <li>
                                    <!--<span class="author">樱桃小丸子</span>在帖子-->
                                    我投票了文档
                                    <a href="">《测试嫩恩恩额关闭   》</a>
                                    <time>（2015-11-22 22:15）</time>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- 我的关注 -->
                <div id="activity-main4" class="user-center-container">
                    <!-- Nav tabs -->
                    <nav class="navbar navbar-default">
                        <ul class="nav navbar-nav" role="tablist">
                            <li role="presentation" class="active">
                                <a href="#word-attention" aria-controls="word-attention" role="tab" data-toggle="tab">文档<span>（1）</span></a>
                            </li>
                            <li role="presentation">
                                <a href="#community-attention" aria-controls="community-attention" role="tab" data-toggle="tab">社区问题<span>（1）</span></a>
                            </li>
                        </ul>
                    </nav>
                    <!-- Tab panes -->
                    <div class="tab-content">
                        <div role="tabpanel" class="tab-pane active" id="word-attention">
                            <ul class="posts-list">
                                <li>
                                    <span class="posts-item-vote-state">1 赞</span>
                                    <div class="posts-item-content">
                                        <h3>
                                            <a href="">问题测试001</a>
                                        </h3>
                                        <p>
                                            <span>
                                                <a href="" target="_blank">demo</a>
                                            </span> •
                                            <time>发表于 2014年09月16日 16:54</time> •
                                            <span>更新于 3月前</span> •
                                            <span>0 回复</span> •
                                        </p>
                                    </div>
                                    <a href="" class="unfollow-btn" onclick="return confirm('确定取消关注该文档？');">取消关注</a>
                                </li>
                            </ul>
                        </div>
                        <div role="tabpanel" class="tab-pane" id="community-attention">
                            <ul class="question-bbs-list">
                                <li>
                                    <a href="#" target="_blank">
                                        <img  class="question-bbs-item-pic" src="<%=request.getContextPath()%>/H+3.2/img/profile_small.jpg" />
                                    </a>
                                    <div class="question-bbs-item-main">
                                        <div class="question-bbs-item-title">
                                            <h3>
                                                <a href="">测试数据</a>
                                            </h3>
                                            <p class="info">
                                                <span class="author">
                                                    <a href="" target="_blank">demo</a>
                                                </span> •
                                                <time>发表于 2015年12月09日 09:40</time> •
                                                <span>0 赞</span> •
                                                <span>0 踩</span> •
                                                <span>1 关注</span>
                                                <!--• <a href="###">意见与反馈</a>-->
                                            </p>
                                        </div>
                                        <div class="question-bbs-item-right">
                                            <a href="" class="unfollow-btn" onclick="return confirm('确定取消关注该问题？');">取消关注</a>
                                        </div>
                                    </div>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
                <!-- 我的资料-普通用户所现 -->
                <div id="activity-main5" class="user-center-container">
                    <!-- Nav tabs -->
                    <nav class="navbar navbar-default">
                        <ul class="nav navbar-nav" role="tablist">
                            <li role="presentation" class="active">
                                <a href="#information" aria-controls="information" role="tab" data-toggle="tab">基本信息</a>
                            </li>
                            <li role="presentation">
                                <a href="#password" aria-controls="password" role="tab" data-toggle="tab">密码</a>
                            </li>
                            <!-- 
                            <li role="presentation">
                                <a href="#number" aria-controls="number" role="tab" data-toggle="tab">账号与验证</a>
                            </li>
                            -->
                        </ul>
                    </nav>
                    <!-- Tab panes -->
                    <div class="tab-content">
                        <!-- 基本信息 -->
                        <div role="tabpanel" class="tab-pane active" id="information">
                            <form class="user-setting-form"  onsubmit="informationSubmit();return false;" method="post" >
                                <div class="user-setting-upload-img">
                                    <img id="photoUrl" src="<%=request.getContextPath()%>/${photoUrl}" />
                                    <a class="ucuse-upbtn">
                                        <span>修改头像</span>
                                        <input type="file" name="user_photo" id="user_photo">
                                    </a>
                                </div>
                                <div class="user-setting-form-item">
                                    <label>邮箱：</label>
                                    <input type="email" id="email" name="email" value="${user.email}" >
                                </div>
                                <div class="user-setting-form-item">
                                    <label>昵称：</label>
                                    <input name="nickName" id="nickName" type="text" maxlength="50" value="${user.nickName}">
                                </div>
                                <div class="user-setting-form-item">
                                    <label>描述：</label>
                                    <textarea name="userDesc" id="userDesc" data-autosize-on="true" style="overflow: hidden; word-wrap: break-word; resize: horizontal; height: 80px;">${user.userDesc}</textarea>
                                </div>
                                <input type="submit" class="btn btn-raised btn-primary btn-sm" name=""  value="提交">
                            </form>
                        </div>
                        <!-- 密码 -->
                        <div role="tabpanel" class="tab-pane" id="password">
                            <form class="user-setting-form" onsubmit="passwordSubmit();return false;" method="post">
                                <div class="user-setting-form-item">
                                    <label>旧密码：</label>
                                    <input name="password" type="password" id="loginPwd" maxlength="100" value="" />
                                </div>
                                <div class="user-setting-form-item">
                                    <label>新密码：</label>
                                    <input name="newpassword" type="password" id="newLoginPwd" maxlength="30" />
                                </div>
                                <div class="user-setting-form-item">
                                    <div>
                                        <!-- weak,medium,strong -->
                                        <span id="password-strength"></span>
                                    </div>
                                </div>
                                <div class="user-setting-form-item">
                                    <label>确认新密码：</label>
                                    <input name="repassword" type="password" id="repassword" />
                                </div>
                                <input type="submit" class="green0" value="提交">
                            </form>
                        </div>
                        <!-- 账号与验证 -->
                        <div role="tabpanel" class="tab-pane" id="number">
                            <div class="id-verification">
                                <dl>
                                    <dt>邮箱账号</dt>
                                    <dd>
                                        <p><span class="red">${user.email}</span></p>
                                        <p class="hint">这是您的主要登录账号，无法更改</p>
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>微博账号</dt>
                                    <dd>
                                        <p>您不是通过微博授权创建的用户，无需设置微博账号</p>
                                        <p class="hint">如果您是新浪微博提交工单创建的用户，则微博账号自动绑定</p>
                                    </dd>
                                </dl>
                                <dl>
                                    <dt>关联登录账号</dt>
                                    <dd>
                                        <p>您还没有关联登录账号</p>
                                        <p class="hint">如果您是通过第三方关联的方式登录，则在这里可以管理关联登录的账号</p>
                                    </dd>
                                </dl>
                            </div>
                        </div>
                    </div>
                </div>
            </section>

           </div>
    </section>
</main>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/lib/ajaxFileUpload/ajaxupload.3.5.js"></script>
<script type="text/javascript">
$(function(){
	s=document.createElement("style");
	document.body.appendChild(s);
	$("#newLoginPwd").on("input",function(){
		var percent=$(this).val().length/30;
		s.innerHTML=".user-setting-form #password-strength:after{width:"+percent*100+"%;}";
	});
	
	$("#userDesc").val("${user.userDesc}");
	queryAllData();
});
$("#userCenterNavList li a").click(function(){
    var $a = $(this),
        linkId = $a.data("link");
    if($a.is(".active")){
        return ;
    }
    $("#userCenterNavList a.active").removeClass("active");
    $a.addClass("active");
    $("#userCenterMainList>div").hide();
    $(linkId).show(200);
});
$("#OrderStatus li a").click(function(){
   
	var th=$(this);
//	alert("aaa"+th.text());
	$('#nowStatus').text(th.text());
	
	$("#OrderStatus li.active").removeClass("active");
    th.parent().addClass("active");
   
});


/*提交基本信息*/
function informationSubmit(){
	var email=$("#email").val();
	/* if(!mailFormat(email)){
		notice.warning("邮箱格式不正确");
		return;
	} */
	var param=new Object();
	param.userId='${user.userId}';
	param.nickName=$("#nickName").val();
	param.userDesc=$("#userDesc").val();
	param.email=email;
	$.post("<%=request.getContextPath()%>/userManageMongo/updateInformation",param,infoCallBack,'json');
}
function infoCallBack(data){
	if(data.success){
		//$(".username").text(data.rows);
		notice.alert(data.msg);
	}else{
		notice.alert(data.msg);
	}
}

/*提交密码更改*/
function passwordSubmit(){
	if($("#loginPwd").val()==''){
		notice.alert("旧密码不能为空","alert-danger");
		return;
	}
	if($("#newLoginPwd").val()==''){
		notice.alert("新密码不能为空","alert-danger");
		return;
	}
	if($("#repassword").val()==''){
		notice.alert("确认密码不能为空","alert-danger");
		return;
	}
	if($("#newLoginPwd").val()!=$("#repassword").val()){
		notice.alert("新密码和确认密码不一致","alert-danger");
		return;
	}
	var param=new Object();
	param.loginPwd=$("#loginPwd").val();
	param.newLoginPwd=$("#newLoginPwd").val();
	param.userId='${user.userId}';
	$.post("<%=request.getContextPath()%>/userManageMongo/updatePassword",param,passwordCallBack,'json');
}
function passwordCallBack(data){
	if(data.success){
		notice.alert(data.msg);
	}else{
		notice.alert(data.msg);
	}
}

/* 头像上传 */
var bg_upload = new AjaxUpload($("#user_photo"), {
    action: "<%=request.getContextPath()%>/userManageMongo/changePhoto",
    name: "image",
    onSubmit: function(file, ext){
         if (! (ext && /^(jpg|png|jpeg|gif)$/.test(ext))){
        	 notice.alert("仅支持JPG, PNG 或 GIF 格式文件上传");
            return false;
        }
       /*  $("#logo_image_status").html("上传中..."); */
    },
    responseType:'json',
    data:{userId:"${user.userId}"},
    onComplete: function(file,response){
        if(response){
            var data = response;
            if(data.success==true){
            	notice.alert(data.msg);
            	$("#photoUrl").attr("src","<%=request.getContextPath()%>/"+data.rows);
            	$(".img-circle").attr("src","<%=request.getContextPath()%>/"+data.rows);
            }else{
            	notice.alert(data.msg);
            }
        }
    }
});
</script>


<!-- ****************************分割线**************************** -->
<script id="table-my-template" type="text/x-handlebars-template">
	{{#each list}}
		<tr onclick="gotoCheckProblems(this)" style="cursor:pointer">
			<td>{{workId}}</td>
			<td><a title="{{title}}">{{title}}</a></td>
			<td><span class="{{#equal status '0'}}
								status status0
							{{/equal}}
							{{#equal status '1'}}
								status status1
							{{/equal}}
							{{#equal status '2'}}
								status status2
							{{/equal}}
							{{#equal status '3'}}
								status status3
							{{/equal}}
							{{#equal status '4'}}
								status status4
							{{/equal}}">{{#equal status '0'}}
								尚未受理
							{{/equal}}
							{{#equal status '1'}}
								受理中
							{{/equal}}
							{{#equal status '2'}}
								等待回复
							{{/equal}}{{#equal status '3'}}
								已解决
							{{/equal}}
							{{#equal status '4'}}
								已关闭
							{{/equal}}</span></td>
			<td>{{createDate}}</td>
			<td>{{serviceGroupName}}</td>
			<td>{{customServiceName}}</td>
		</tr>
	{{/each}}
</script>
<script type="text/javascript">
//进入我的提问时调用
/*全部*/
function queryAllData(){
	queryData(-1);
}
/*尚未受理*/
function waitHandle(){
	queryData(0);
}
/*受理中*/
function Handling(){
	queryData(1);
}
/*等待回复*/
function waitReplay(){
	queryData(2);
}
/*已解决*/
function Handled(){
	queryData(3);
}
/*已关闭*/
function closed(){
	queryData(4);
}

function queryData(sta){
	<%-- $.ajax({
		url:"http://<%=request.getServerName()%>:${workPath}/queryWorkOrderInfo/queryCreatorAllWorkOrder?sessionKey=${sessionKey}",
		data:{page:1,rows:30,status:sta},
		dataType:"jsonp",
		success:function(data){
			var myTemplate = Handlebars.compile($("#table-my-template").html());
			$('#myQlist').html(myTemplate(data.rows));
		}
	}); --%>
	 getTableData({status:sta,page:1,rows:10});
 }
 
var getTableData = (function(){
    var paramCache = {};

    /**
     * 初始化分页
     */
    var pager = new cri.Pager($("#pagination"),{
        page:1,
        pageSize:10,
        total:0,
        onPage:function(page,pageSize){
            $.extend(paramCache,{page:page,rows:pageSize});
            getTableData(paramCache);
        }
    });
    return function(param,url){
    	$.ajax({
    		url:"http://<%=request.getServerName()%>:${workPath}/queryWorkOrderInfo/queryCreatorAllWorkOrder?sessionKey=${sessionKey}",
    		data:param,
    		dataType:"jsonp",
    		success:function(data){
    			var myTemplate = Handlebars.compile($("#table-my-template").html());
    			$('#myQlist').html(myTemplate(data.rows));
    			pager.update(param.page,param.rows,data.total);
    			$.extend(paramCache,param);
    		}
    	});
    }
})();
 
 
 
 
 
 function gotoCheckProblems(a){
	 var workId=$(a).children("td").first().text();
	 location.href="<%=request.getContextPath()%>/check/problem/"+workId;
 }
</script>
<script>

IM.init({ useLocal : false,entId:'${ccodEntId}',skillGroupId:'${skillId}',username:"${user.webchatId}",type:'IM',customPriority : '1000'});

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