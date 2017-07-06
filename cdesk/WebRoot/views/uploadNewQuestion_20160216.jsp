<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>提交新问题</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/normalize.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/enter.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
	<script src="<%=request.getContextPath()%>/script/lib/summernote/summernote.js" ></script>
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote-zh-CN.js" ></script>
</head>
<body>
<header class="head">
    <div class="wrap hader-inner">
        <div class="logo">
            <img src="<%=request.getContextPath()%>/static/images/logo.png" />
        </div>
        <nav class="header-nav">
            <ul class="header-nav-list">
                <li id="one">
                    <a href="#">讨论社区</a>
                </li>
                <li>
                    <a href="<%=request.getContextPath()%>/newquestion/index">提交新问题</a>
                </li>
            </ul>
            <div class="user-nav">
            	<% if (request.getAttribute("user") != null) { %>
                <div class="dropdown profile-element">
                    <a data-toggle="dropdown" class="dropdown-toggle user" href="#">
                        <span class="avatar">
                            <img alt="image" class="img-circle" src="<%=request.getContextPath()%>/${photoUrl}" />
                        </span>
                        <span class="text-muted text-xs block username">${user.nickName}<b class="caret"></b></span>
                    </a>
                    <ul class="dropdown-menu animated fadeInRight m-t-xs">
                        <li><a class="J_menuItem" href="#">我的消息</a>
                        </li>
                        <li><a class="J_menuItem" href="<%=request.getContextPath()%>/activity/my">我的活动</a>
                        </li>
                        <c:if test="${user.userType != '1'}">
                        <li><a class="J_menuItem" href="<%=request.getContextPath()%>/main">进入控制面板</a>
                        </li>
                        </c:if>
                        <li class="divider"></li>
                        <li><a href="<%=request.getContextPath()%>/logout">退出</a>
                        </li>
                    </ul>
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
                <div class="user-info">
                    <div class="dropdown-toggle">
                        <span class="user-info-name">
                            <a href="#" style="color:#428bca;">简体中文</a>
                        </span>
                    </div>
                </div>
            </div>
        </nav>
    </div>
</header>

<main>
    <header class="main-header">
        <div class="wrap">
            <ul class="breadcrumbs">
                <li><a>首页</a></li>
                <li> > </li>
                <li><a>提交新问题</a></li>
            </ul>
            <from class="main-header-search">
                <label>
                    <input placeholder="输入问题关键字，找到答案" type="search" />
                    <i class="icon-search"></i>
                </label>
            </from>
        </div>
    </header>
    <section class="content">
        <div class="wrap">
            <section class="content-main">
                <div class="form-ui" onsubmit="return false">

                    <div class="form-group label-floating">
                        <label class="control-label">标题<span>*</span></label>
                        <input maxlength="120" type="text" class="form-control" id='title'>
                    </div>
<!-- 
                    <div class="form-group label-floating">
                        <label class="control-label">手机号码 <span>*</span></label>
                        <input maxlength="32" size="20" type="text" class="form-control">
                    </div>
 -->
                    <div class="form-section">
                        <label>问题描述 <span>*</span></label>
                        <!-- 文本输入框 -->
                         <div>
                         	<div></div>
                             <div class="md-editor" id="editor"></div>
                         </div>
                    </div>
                    <div class="form-section">
                        <div  class="uploader">
			            <div id="fileGroup"></div>
				            <form action="/"
			                  class="dropzone"
			                  enctype="multipart/form-data"
			                  id="my-dropzone"
			                  method="post">
            				</form>
       					 </div>
                    </div>
                    <input type="submit" value="提交" onclick="sumbits()">
                </div>
            </section>
        </div>
    </section>
</main>

</body>
<script type="text/javascript">
/* $(function(){
	$("#editor").summernote({
    	lang:"zh-CN",
    	height: 150,
    	toolbar:[
    		 ['style', ['style','bold', 'italic', 'underline', 'clear']],
    	     ['font', ['strikethrough', 'superscript', 'subscript','hr']],
    	     ['fontsize', ['fontsize']],
    	     ['color', ['color']],
    	     ['para', ['ul', 'ol', 'paragraph']],
    	     ['height', ['height']],
    	    ]});

}); */
$(function(){
	//富文本框编辑
	$("#editor").summernote({
		lang:"zh-CN",
		height: 200,
		toolbar:[
			['style', ['style','bold', 'italic', 'underline', 'clear']],
			['font', ['strikethrough', 'superscript', 'subscript','hr']],
			['fontsize', ['fontsize']],
			['color', ['color']],
			['para', ['ul', 'ol', 'paragraph','table']],
			['height', ['height','redo','undo']],
		]});
	//附件上传处理
	Dropzone.options.myDropzone = {
		//指定上传图片的路径
		url: "<%=request.getContextPath()%>/attachments/upload",
		//添加上传取消和删除预览图片的链接，默认不添加
		addRemoveLinks: true,

		//关闭自动上传功能，默认会true会自动上传
		autoProcessQueue: true,
         
		//添加区域提示信息
		dictDefaultMessage: "点击此处进行上传",
		
		sending: function(file, xhr, formData) {
		      formData.append("userId", "${userId}");
		      formData.append("userName","${userName}");
		    },
		init: function () {
			myDropzone = this; // closure
			var fileDesc;
			//当上传完成后的事件，接受的数据为JSON格式
			this.on("complete", function (data) {
				if (this.getUploadingFiles().length === 0 && this.getQueuedFiles().length === 0) {
					var res = eval('(' + data.xhr.responseText + ')');
					if (res.success) {
						var $fileDetail = $("<input type='text' class='file' style='display:none'>");
						$fileDetail.attr("id",fileDesc);
						$fileDetail.val(res.rows[0].fileNew).data("fileInfo",res.rows[0]);
						$("#fileGroup").append($fileDetail);
						notice.alert(res.msg,"alert-success");
					}
					else {
						notice.alert(res.msg,'alert-danger');
					}
				}
			});
			this.on("addedfile",function(file){
				fileDesc = file.lastModified;
			});
			//删除图片的事件，当上传的图片为空时，使上传按钮不可用状态
			this.on("removedfile", function (file) {
				if(file != null){
					$.post("<%=request.getContextPath()%>/attachments/delete",{"newFileName":$("#"+file.lastModified).val()},function(data){
						if(data.success){
							$("#"+file.lastModified).remove();
							notice.alert(data.msg,"alert-success");
						}else{
							notice.alert(data.msg,'alert-danger');
						}
					});
				}
			});
		}
	};
});

//获取上传附件的信息
function getAttachments(){
	var attachement=[];
	$("#fileGroup .file").each(function(){
		var data = $(this).data("fileInfo");
		attachement.push(JSON.stringify(data));
	});
	return "["+attachement.join(",")+"]";
}
</script>
<script type="text/javascript">
//点击提交
function sumbits(){
//	alert($('#title').val());
 	var params=$.extend(
			{content:$('#editor').code()},
			{title:$('#title').val()},
			{creatorId:"${user.userId}"},
			{creatorName:"${user.userName}"},
			{creatorEmail:"${user.email}"},
			{entId:'${enterpriseid}'},
			{source:'0'},
			{issue:$('#editor').code()},
			//获取上传的附件
			{attachment:getAttachments()}
			);
	var info = encodeURI(JSON.stringify(params));
//	alert(params);
	$.ajax({
		url:"http://<%=request.getServerName()%>:${workPath}/workorder/webcreate?sessionKey=${user.sessionKey}",
        dataType : 'jsonp',
		data:{info:info},
		success:function(data){
			if(data.success){
				alert("创建成功");
				
			}else{
				alert("创建失败");
			}
		}
	}) 
}
</script>
</html>