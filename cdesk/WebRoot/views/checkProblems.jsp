<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>查看问题  #${workId} 工单</title>
    <link rel="icon" type="image/x-icon" href="<%=request.getContextPath()%>/${faviconUrl}" />
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
            	<li>
            	   <a class="J_menuItem" href="<%=request.getContextPath()%>/activity/my">我的活动</a>
                </li>
                <li>
                    <a href="<%=request.getContextPath()%>/newquestion/index">提交新问题</a>
                </li>
                 <li>
                   <a href="<%=request.getContextPath()%>/logout">退出</a>
                 </li>
            </ul>
            <div class="user-nav">
            	<% if (request.getAttribute("user") != null) { %>
                <div class="dropdown profile-element">
                    <a data-toggle="dropdown" class="dropdown-toggle user" href="#">
                        <span class="avatar">
                            <img alt="image" class="img-circle" src="<%=request.getContextPath()%>/${photoUrl}" />
                        </span>
                       <span class="text-muted text-xs block username">${user.userName}</b></span>
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
    <section class="search-box">
        <form class="search">
            <div class="search-match">
                <label style="margin-bottom:0;">
                    <img src="<%=request.getContextPath()%>/static/images/Search.png" />
                    <input type="hidden" name="type" value="kb">
                    <input type="search"  name="keyword" placeholder="输入问题关键字，找到答案" autocomplete="off" x-webkit-speech="">
                </label>
                <ul class="match-list"></ul>
            </div>
            <input type="submit" name="" value="搜索">
        </form>
    </section>
	<section class="content">
        <div class="wrap">
            <section class="content-main">
                <nav class="sub-nav">
                    <ul class="breadcrumbs">
                        <li> <a href="<%=request.getContextPath()%>/activity/my">首页</a></li>
                        <li>></li>
                        <li> <a href="<%=request.getContextPath()%>/activity/my">我的提问</a></li>
                        <li>></li>
                        <li> 查看问题</li>
                    </ul>
                </nav>
                <article class="request-comment">
                    <h2>#${workId} 工单 <span id="title"></span>
                        <!-- <a class="request-push" >催一下</a> -->
                    </h2>
                    <ul id="replys" class="request-comment-list">
                        <%-- <li>
                            <img class="article-comments-form-pic" src="<%=request.getContextPath()%>/${photoUrl}" />
                            <div class="article-comments-item-header">
                                <span class="author">我</span> •
                                <time>2015年12月10日 15:21</time>
                            </div>
                            <div>
                                <div><p>答复</p></div>
                            </div>
                            <div class="signature"></div>
                        </li> --%>
	                </ul>
                    <div class="comment-form">
                        <li>
                            <img  class="article-comments-form-pic" src="<%=request.getContextPath()%>/${photoUrl}" />
                            <div class="form-ui">
                                <div onsubmit="return false">
                                    <div class="form-section request-comment">
                                        <label class="required">回复内容
                                            <span class="red">*</span>
                                        </label>
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
                                    <div onclick="sumbits()">
                                    	<input type="submit" value="提交">
                                    </div>
                                </div>
                            </div>
                        </li>
                    </div>
                </article>
            </section>
            <section class="content-sidebar">
                <div class="category">
                    <h3>详细信息</h3>
                    <ul class="request-info-list">
                        <li>
                            <h4>提交时间： <span id="createDate"></span></h4>
                        </li>
                        <li>
                            <h4>受理时间： <span id="acceptDate"></span></h4>
                        </li>
                        <li>
                            <h4>受理客服组： <span id="serviceGroupName"></span></h4>
                        </li>
                        <li>
                            <h4>受理客服： <span id="customServiceName"></span></h4>
                        </li>
                        <li>
                            <h4>工单状态： <span id="status"></span></h4>
                        </li>
                    </ul>
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
<script id="ul-template" type="text/x-handlebars-template">
	{{#each event}}
	<li>
		<img class="article-comments-form-pic" src="<%=request.getContextPath()%>/${photoUrl}" />
		<div class="article-comments-item-header">
    		<span class="author">
				{{#equal updatorName '${user.userName}'}}
						我
				{{else}}
					{{updatorName}}
				{{/equal}}
			</span> •
   			 <time>{{contentUpdateDate}}</time>
		</div>
		<div>
    	<div>{{{content}}}</div>
		</div>
		<div class="signature"></div>
	</li>
	{{/each}}
</script>
<script type="text/javascript">
$(function(){loadData()})

function loadData(){
		var wfStatus = ["尚未受理","受理中","等待回复","已解决","已关闭"];
		var workid=${workId};
		$.ajax({
			url:"http://<%=request.getServerName()%>:${workPath}/workorder/detailforworkid?sessionKey=${user.sessionKey}",
			data:{
				workId:workid,
				isPublicReply:"0"
			},
			dataType:"jsonp",
			success:function(data){
				var list=data.rows[0];
				$('#title').text(list.title);
				//详细信息列表
				$('#createDate').text(list.createDate);
				if(list.acceptDate==null||list.acceptDate==""){
					$('#acceptDate').text("-");
				}else{
					$('#acceptDate').text(list.acceptDate);
				}
				if(list.serviceGroupName==null||list.serviceGroupName==""){
					$('#serviceGroupName').text("-");
				}else{
					$('#serviceGroupName').text(list.serviceGroupName);
				}
				if(list.customServiceName==null||list.customServiceName==""){
					$('#customServiceName').text("-");
				}else{
					$('#customServiceName').text(list.customServiceName);
				}
				switch(list.status)
				{
				case "0":
					$('#status').addClass("color-label orange0");
		            break;
		        case "1":
		        	$('#status').addClass("color-label red0");
		            break;
		        case "2":
		        	$('#status').addClass("color-label blue0");
		            break;
		        case "3":
		        	$('#status').addClass("color-label green0");
		            break;
		        case "4":
		        	$('#status').addClass("color-label gray0");
		            break;
		        default:
				}
				$('#status').text(wfStatus[list.status]);
				//回复块
				var myTemplate = Handlebars.compile($("#ul-template").html());
				$('#replys').html(myTemplate(list));
			}
		})
		$('#editor').code('');
}
	
function sumbits(){
	
	var params=$.extend(
			{content:$('#editor').code()},
			{workId:parseInt('${workId}')},
			{updatorId:"${user.userId}"},
			{updatorName:"${user.userName}"},
			{attachment:getAttachments()}
			);
	var info = encodeURI(JSON.stringify(params));
//	alert(params);
	$.ajax({
		url:"http://<%=request.getServerName()%>:${workPath}/workorder/update?sessionKey=${user.sessionKey}",
        dataType : 'jsonp',
		data:{entId:'${enterpriseid}',info:info},
		success:function(data){
			if(data.success){
			//	alert("回复成功");
				<%-- location.href="<%=request.getContextPath()%>/check/problem/"+${workId}; --%>
				loadData();
			}else{
				alert("回复失败");
			}
		}
	})
}
</script>
</html>