<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>新建工单</title>
	<%@include file="/views/include/pageHeader.jsp"%>
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/order.css">
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
	
	<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
	
	<script src="<%=request.getContextPath()%>/script/lib/summernote/summernote.js" ></script>
	<script src="<%=request.getContextPath()%>/script/lib/summernote/summernote-zh-CN.js" ></script>
</head>
<body>
<div id="left-part" data-status="0">
	<header class="part-header">
		<div class="sidebar">新建工单</div>
	</header>
	<div  class="scrollbar-box ps-container">
		<div class="container-fluid">
			<form id="leftForm" class="form-horizontal">
				<div class="ensconce">
					<fieldset>
						<div class="form-group">
							<label class="label-control">客服组：</label>
							<div class="info">财务组</div>
						</div>
						<div class="form-group">
							<label>受理客服：</label>
							<div class="info">-</div>
						</div>
						<div class="form-group">
							<label>类型：</label>
							<div class="info">问题</div>
						</div>
						<div class="form-group">
							<label>优先级：</label>
							<div class="info">紧急</div>
						</div>
						<div class="form-group">
							<label>副本：</label>
							<div class="group-label"></div>
						</div>
					</fieldset>
				</div>
				<div class="show">
					<fieldset>
<!-- 						<div class="form-group">
							<label class="control-label">邮箱地址<a onclick = "addEmailAddr()">以我的名义创建</a></label>
							<input class="form-control" id="mailAddr" maxlength="99" type="text" autocomplete="off">

						</div> -->
						
						<input id="customId" type="hidden">						

						<div class="form-group">
							<label class="control-label">受理客服组</label>
							<select class="form-control" id="serviceGroupName" onchange="chooseGroup(this);">
								<option value="">-</option>
							</select>
						</div>
						<div class="form-group">
							<label class="control-label">受理客服</label>
							<select class="form-control" id="customServiceName">
								<option value="">-</option>
							</select>
						</div>
						<div class="form-group">
							<label class="control-label">抄送副本<a onclick = "$('#copyMailAddr').val('${userName}'+' ${mailAddr}')">抄送给我</a></label>
							<input class="form-control" id="copyMailAddr" maxlength="99" type="text" autocomplete="off">
						</div>
					</fieldset>
					<fieldset>
						<div class="form-group">
							<label class="control-label">工单状态</label>
							<select  class="form-control" id="wfStatus">
								<option value="0">尚未受理</option>
								<option value="1">受理中</option>
								<option value="2">等待回复</option>
								<option value="3">已解决</option>
								<option value="4">已关闭</option>
							</select>
						</div>
						<div class="form-group">
							<label class="control-label">工单类型</label>
							<select class="form-control" id="wfType">
								<option value="">-</option>
								<option value="2">问题</option>
								<option value="3">事务</option>
								<option value="4">故障</option>
								<option value="5">任务</option>
							</select>
						</div>
						<div class="form-group">
							<label class="control-label">优先级 </label>
							<select class="form-control" id="priority">
								<option value="">-</option>
								<option value="1">低</option>
								<option value="2">中</option>
								<option value="3">高</option>
								<option value="4">紧急</option>
							</select>
						</div>
					</fieldset>
				</div>
			</form>
		</div>
	</div>
</div>
<div id="right-part">
	<div class="right-content" style="padding-top:10px;">
		<div class="row">
			<div class="col">			    
			     <div class="user-center-title">
	                    <div class="top">
	                        <div class="user-info fl">
	                            <a class="user-avatar" onclick="viewDetails('${user.userId}','${user.userName}')">                          
	                                <img src="<%=request.getContextPath()%>/${photoUrl}" alt="" title="修改头像" id="userImg">
	                                <form><input type="file" name="user_photo"></form>
	                            </a>
	                            <h4>
	                                <a onclick="viewDetails('${user.userId}','${user.userName}')"> ${user.userName} </a>
	                                            
	                            </h4>
	                            <div class="drop-btn-default fl">
	                                <div class="dropdown">
	                                    <a id="dLabel" data-target="#" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
	                                        状态<span id="span1" class="status green">正常</span>
	                                    </a>
	                                </div>
	                            </div>
	                        </div>
	                        
	                        
	                        <div class="user-else" style="width:calc(100% - 205px);">                       
	                           <div><label>邮箱：</label><span> ${user.email} </span></div>
	                           <div><label>手机：</label><span> ${user.telPhone} </span>
	                                <p class="hint">
	                                    <input type="checkbox" id="bindPhone" data-name="phoneBinded" disabled="true"> 绑定 (可接收短信提醒)
	                                </p>
	                            </div>
	                            <div><label>座机：</label><span> ${user.fixedPhone} </span></div>
	                            
	                            <!-- 分配客服组 -->
	                             <div id="groupName1" class="group-label" >
	                            </div> 
	                            
	                            <span id="nikAndSign" style="display:none">
	                            <div><label>客服昵称：</label><span> ${user.nickName} </span></div>                                
	                            <div><label>客服签名：</label><span> ${user.signature} </span></div>
	                            </span>	                            
	                            <div>
	                                <label>角色：</label>
	                                <select id="typeSelect" onchange="upTypeSelect()" data-name="userType">
	                                    <option value="1">客户</option>
	                                    <option value="2">客服</option>
	                                    <option value="3">管理员</option>
	                                </select>
	                            </div>
	                            
	                             <div id="roleSelect" style="display:none">                                
	                                <label id="roleLabel">角色权限：</label><select id="secondType" onchange="updateSelect(this.id)" data-name="roleId">
	                                </select>
	                            </div> 
	                            
	                            <div><label>用户说明：</label><span> ${user.userDesc} </span></div>                           
	                            <div><label>详细信息：</label><span> ${user.remark} </span></div>
	                            
	                   <!-- 自定义字段     -->
	                   <ul id="userFieldUl">
						<c:forEach items="${activeFieldList}" var="item">
							   <div>
							   <label>${item.name }：</label>						   
	                           <c:choose>                                  
	                            <c:when test="${item.componentType=='1'}">
	                            <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>  
	                               <span> ${fieldKey} </span>                                                                 
	                            </c:when>      
	                                 
	                            <c:when test="${item.componentType=='2'}">   
	                            <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>   
	                                <span> ${fieldKey} </span>                                          
	                            </c:when>       
	                                              
	                            <c:when test="${item.componentType=='3'}">
	                                　　				    <select id="${item.key }" data-name="${item.key }" disabled="true">
	                                    <c:forEach items="${item.candidateValue}" var="selectItems"> 
	                                       <option value="${selectItems }">${selectItems }</option>
	                                    </c:forEach>        
	                                </select>
	                            </c:when> 
	                                                                                                    
	                            <c:when test="${item.componentType=='4'}">   
	                             <div class="checkFieldDiv">                         
	                              <ul>                           
	                               <li id="${item.key}">
	                                <c:forEach items="${item.candidateValue}" var="checkItems"> 
	                                   <input type="checkbox" onclick="checkUserField('${item.key}','${checkItems}',this.checked)" disabled="true"><span>${checkItems}</span>
	                                </c:forEach> 
	                               </li> 
	                              </ul>   
	                             </div>                            
	                            </c:when> 
	                                                        
	                            <c:when test="${item.componentType=='6'}">   
	                            <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>                                
	                                <span> ${fieldKey} </span>
	                            </c:when>
	                            
	                            <c:when test="${item.componentType=='7'}">   
	                            <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>   
	                                <span> ${fieldKey} </span>
	                            </c:when>
	                                                           
	                            <c:when test="${item.componentType=='8'}">
	                            <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set> 
	                            <c:forEach items="${item.candidateValue}" var="reg">
	                                <span> ${fieldKey} </span>
	                            </c:forEach>
	                            </c:when>                            
	                           </c:choose> 
							  </div>
						</c:forEach>
					</ul>           
	                               
	                        </div>
	                    </div>
	                </div>
			    		
				<div class="body">
					<%@include file= "content.jsp" %>
				</div>
				
			</div>
		</div>
	</div>
</div>

<div id="bottom-bar" class="show" >
	<div class="btn-group dropup" id="lidiv">
		<button id="submitFun" data-href="0" type="button" class="btn btn-raised btn-danger" >创建为<span id="submitStr">尚未受理</span></button>
		<button id = "buttonSmall" type="button" class="btn btn-raised btn-danger dropdown-toggle border-le" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
			<span class="caret"></span>
			<span class="sr-only">Toggle Dropdown</span>
		</button>
		<ul class="dropdown-menu" >
			<li class="dropdown-header">创建新工单</li>
			<li role="separator" class="divider"></li>
			<!-- <li><a data-href="0">创建为<span class="status status0">尚未受理</span></a></li> -->
			<li><a data-href="1">创建为<span class="status status1">受理中</span></a></li>
			<li><a data-href="2">创建为<span class="status status2">等待回复</span></a></li>
			<li><a data-href="3">创建为<span class="status status3">已解决</span></a></li>
			<li><a data-href="4">创建为<span class="status status4">已关闭</span></a></li>
		</ul>
	</div>
</div>
<script id="workEvent-template" type="text/x-handlebars-template">
	<article>
		<div class="wrap">
			<a class="user-avatar" href="#">
				<img src="/ems/H+3.2/img/profile_small.jpg" />
			</a>
			<header>
				<a href="#">{{this.[0]}}</a> ·<span></span>·<time>{{this.[1]}}</time>
			</header>
			<div class="comment-body" style="display:none;">
				<div >
					<div >
						<div class="audioplayer" >
							<audio style="width: 0px; height: 0px; visibility: hidden;"></audio>
							<div class="audioplayer-playpause" title="播放">
								<a href="#">播放</a>
							</div>
							<div class="audioplayer-time">00:00</div>
							<div class="audioplayer-bar">
								<div style="width: 100%;"></div>
								<div class="audioplayer-bar-played"></div>
							</div>
							<div class="audioplayer-time audioplayer-time-duration">00:58</div>
							<div class="audioplayer-volume">
								<div class="audioplayer-volume-button" title="音量">
									<a href="#">音量</a>
								</div>
								<div class="audioplayer-volume-adjust">
									<div>
										<div style="height: 100%;"></div>
									</div>
								</div>
							</div></div></div></div>
				<p>去电详情</p>
				<p>呼出电话至： 13608225640</p>
				<p>呼出号码：01065499546</p>
				<p>通话时间：2015年10月29日 14:49</p>
				<p>呼出人：</p>
				<p>通话时长： 58 秒</p>
			</div>
			<div class="comment-body">
				<p>{{this.[3]}}</p>
			</div>
		</div>
	</article>
</script>
<script>
	//全局变量，存储从后台所获取的数据
	var workinfo;
	$(document).ready(function() {	
		loadLeftInfo();
		//$("#mailAddr").val("${selectEmail}");
		$("#customId").val("${customId}");
		$("#wfStatus").change(function(){
			var status = $("#wfStatus>option:selected").val();
		    var statusStr = $("#wfStatus>option:selected").text();
		    setSubmitAttr(status, statusStr);
		});
		
		
        $("#langSelect").val("${user.lang}");
        $("#langSelect").prop("disabled",true);
        
        $("#typeSelect").val("${user.userType}");
        $("#typeSelect").prop("disabled",true);
        
        if("${user.userType}"=="2"){	  
        	var parentId="${user.userType}";		  
		    $.post("<%=request.getContextPath()%>/usrManage/secondLevel?parentId="+parentId, secondLevelCallBack, 'json');	
        }else{
        	$("#roleSelect").css("display","none");
        }
        
        if("${user.userType}"!="1"){	
        	$("#nikAndSign").css("display","block");
                var userId="${user.userId}"; 
                var loginName="${user.loginName}"; 
    		    $("#groupName1").css("display","block");  
                  /* 查询所属客服组 */
                $.post("<%=request.getContextPath()%>/usrManage/belongGroup?userId="+userId+"&loginName="+loginName, belongGroupCallBack, 'json');
        }else{
        	$("#nikAndSign").css("display","none");
        	$("#groupName1").css("display","none");
        }
            
		if("${user.userStatus}"=="1"){
			$("#span1").addClass("status green");   
			$("#span1").html("正常");
		}else if("${user.userStatus}"=="2"){
			$("#span1").addClass("status red");   
			$("#span1").html("暂停");
		}else if("${user.userStatus}"=="3"){
			$("#span1").addClass("status blue");   
			$("#span1").html("未审核");
		}
	});

	function loadLeftInfo(){
		var param = {};
		param.userId="${userId}";
		param.entId="${entId}";
		$.ajax({
			url:"<%=request.getContextPath()%>/userApi/queryGroupAgent",
			type:"post",
			dataType:"json",
			data:param,
			success : function(data){
				setCustomGroupSelected(data.rows);
			}
		});
	}

	function setCustomGroupSelected(data){
		var $serviceGroupName = $("#serviceGroupName");
		$serviceGroupName.html('<option value="">-</option>');
		for(var i=0;i<data.length;i++){
			$serviceGroupName.append('<option value="' + data[i].groupId + '">' + data[i].groupName + '</option>');
		}
	}

	function chooseGroup(param) {
		var groupId = param.value;
		var selectIndex = param.selectedIndex;
		var groupName = param.options[selectIndex].text;
		if (groupId != null && groupId != "undefined" && groupId!="") {
			
			var param = {};
			param.groupId = groupId;
			param.entId = "${entId}";
			$.ajax({
				url:"<%=request.getContextPath()%>/userApi/queryGroupAgent",
				type:"post",
				dataType:"json",
				data:param,
				success : function(data){
					setCustomAgentSelected(data.rows[0].agentList);
				}
			});
		}else{
			
			var $c = $("#customServiceName");
			$c.html('<option value="">-</option>');
		}
	}

	function setCustomAgentSelected(data) {
		var $c = $("#customServiceName");
		$c.html('<option value="">-</option>');
		for (var i = 0; i < data.length; i ++) {
			$c.append('<option value="' + data[i].userId + '">' + data[i].userName + '</option>');
		}
		//默认选中第一条数据
		$c.prop('selectedIndex', 0);
	}

	function workevent(e){
		$(e).siblings().removeClass("active");
		$(e).addClass("active");
		$(".comment-body").hide();
	}

	function workrpy(e){
		$(e).siblings().removeClass("active");
		$(e).addClass("active");
		$(".comment-body").show();
	}

	 $("#bottom-bar .dropdown-menu li a").on("click",function(){
         var spanStyleText = [{style:'status status0',text:'尚未处理'},{style:'status status1',text:'受理中'},{style:'status status2',text:'等待回复'},{style:'status status3',text:'已解决'},{style:'status status4',text:'已关闭'}];
         var $submitFun = $("#submitFun");
         var $this = $(this);
         var thisHref = $this.attr("data-href");
         var submitHref = $submitFun.attr("data-href");
         var thisStyleText = spanStyleText[parseInt(submitHref)];
         $submitFun.attr("data-href",thisHref).find("span").text(spanStyleText[parseInt(thisHref)].text);
         $this.attr("data-href",submitHref).find("span").text(thisStyleText.text).attr("class",thisStyleText.style);
         $("#lidiv").removeClass("open");
         submit(thisHref);
     });

     $("#submitFun").click(function(){
         submit($(this).attr("data-href"));
     }); 


	//提交数据
	function submit(type) {
		var startTime = new Date().getTime();
	
		 //工单状态联动
        $("#wfStatus").val(type);
		//对提交的信息进行简单的验证
		var params = {};
		var text="";
        //var mailAddr = $("#mailAddr").val(),
        var wfTitle  = $.trim($("#wfTitle").val()),
            editor   = $.trim($($("#editor").code()).text()),
            wfStatus = $.trim($("#wfStatus").val()),
            customServiceName = $("#customServiceName").val();
		
		if(!wfTitle){
			text = text+"<p>标题不能为空</p>";
		}
		if(!editor){
			text = text+"<p>问题描述不能为空</p>";
		}
		if(wfStatus == "3"){
			if(!customServiceName){
				text = text+"<p>当工单状态为已解决时受理客服不能为空</p>";
			}
		}
	
		if(text!=""){
			notice.alert(text,'alert-danger');
			return;
		}

		
	    var $body=$("body");
		if($body.data("status")=="1"){
			return;
		}
		$body.data("status","1");
		//判断是否有受理客服
		 
		if($("#customServiceName").val()!=""){
			 if( $("#wfStatus").val()=="0"){
				 $("#wfStatus").val("1");
				 setSubmitAttr("1","受理中")
			 }
		}
		 
		var param = {
			sessionId:'${sessionId}',
			
			ccodEntId:'${ccodEntId}',
			ccodAgentId:'${ccodAgentId}',
			
			//邮箱地址			
			/* creatorEmail : mailAddr, */
			creatorEmail : '${user.email}',			
			//客服组ID及名称
			serviceGroupId : $("#serviceGroupName").val(),
			serviceGroupName : ($("#serviceGroupName option:selected").text()=="-") ? "" : $("#serviceGroupName option:selected").text(),
			//受理客服ID及名称
			customServiceId : $("#customServiceName").val(),
			customServiceName : ($("#customServiceName option:selected").text() == "-") ? "" : $("#customServiceName option:selected").text(),
			//标题
			title : $("#wfTitle").val(),
			//内容
			content : $.trim($("#editor").code()),
			//企业ID
			entId : "${entId}",
			//创建ID
			creatorId : "${userId}",
			//创建人名称
			creatorName : "${userName}",
			//工单来源
			source : "${source}",
			//工单状态
			status : $("#wfStatus").val(),
			//工单类型
			type : $("#wfType").val(),
			//优先级
			priority : $("#priority").val(),
			//是否为座席操作（1：是，0：否）
			isAgent : "1",
			/* isPublicReply:isPublicReply+"", */
			//获取上传的附件
			attachment:getAttachments(),
			
			/* 客户的Id */
			customId : $("#customId").val(),
			/* 客户的Name */
			customName : "${customName}"
			
		};
		var info = encodeURI(JSON.stringify(param));
		params.info = info;
	
		$.ajax({
			url : "http://<%=request.getServerName()%>:"+parent.workBasePath+"/workorder/webcreate?sessionKey="+ $.cookie("sessionKey"),
			type : "post",
			dataType : 'jsonp',
			data : params,
			success : function(data) {
				$body.data("status","0");
				if (data.success) {
					notice.alert(" 工单数据提交成功！");
					//$("#mailAddr").val("");
					$("#copyMailAddr").val("");
					$("#wfTitle").val("");
					$("#editor").code("<p><br></p>");
					$("#wfStatus").val("0");
					$("#wfType").val("");
					$("#priority").val("");
					$("#serviceGroupName").val("");
					$("#customServiceName").val("");
					$("#my-dropzone").empty().append("<div class='dz-default dz-message'><span>点击此处进行上传</span></div>");
					$("#fileGroup").empty();
					
                    //由IM窗口创建工单并成功后，需要回调IMRefreshOrderList函数刷新工单列表视图
                    console.log('[cdesk] 回调刷新工单列表函数,userId:'+param.customId);
                    parent.IMRefreshOrderFunc(param.customId);
                    
                    var customId=param.customId;
                    var url = "<%=request.getContextPath()%>/order/detail?workId=" + data.rows.workId+"&customId="+customId;
					var title = "#" + data.rows.workId + "-" + data.rows.title;
					parent.openTab(url,"order",title);
                    location.reload();
				} else {
					notice.alert("工单数据提交失败！"+data.msg);
				}
			}
		});
	}

	function addEmailAddr(){
		$("#mailAddr").val("${user.email}");
	}
	
	
    //设置提交按钮的显示值和调用方法
    function setSubmitAttr(status, statusStr) {
        var spanStyleText = [{style:'status status0',text:'尚未处理'},{style:'status status1',text:'受理中'},{style:'status status2',text:'等待回复'},{style:'status status3',text:'已解决'},{style:'status status4',text:'已关闭'}],
            $s            = $("#submitFun"),
            submitH       = $s.attr("data-href");

        if(submitH != status){
            var $a = $s.siblings('ul').find('a[data-href='+status+']');
            var thisStyleText = spanStyleText[parseInt(submitH)];
            $a.attr("data-href",submitH).find("span").text(thisStyleText.text).attr("class",thisStyleText.style);
            $s.attr("data-href",status).find("span").text(statusStr);
        }
    }
    
    //验证邮箱是否正确的代码
    function ismail(mail) {
	 var filter  = /^([a-zA-Z0-9_\.\-])+\@(([a-zA-Z0-9\-])+\.)+([a-zA-Z0-9]{2,4})+$/;
	 if (filter.test(mail)) return true;
	 else {
	 return false;}
  }
    
    
    /*  自定义字段下拉框类型赋值 */
    $("#userFieldUl select").each(function(){
   		var id=$(this).attr("id");
   		var u = JSON.parse('${user}');
       	u[id] && $(this).val(u[id]);                    
    }); 
    
    
    /* 自定义字段复选框赋值 */
    $(".checkFieldDiv li").each(function(){
    	var id=$(this).attr("id");
    	var u= JSON.parse('${user}');
    	if(typeof(u[id])!="undefined"){
        	$("#"+id+" input[type=checkbox]").each(function(){
        		var checkValue=$(this).next().text();
        		for(var i=0;i<u[id].length;i++){
        			if(checkValue==u[id][i])
        				$(this).attr("checked",true);              			
        		}
        	});  
    	}     	
    }); 
       
    
    /*加载绑定手机选项 */
    var check="${user.phoneBinded}";
    if(check=="1"){
    	$("#bindPhone").attr("checked",true);
    }
    if(check=="0"){
    	$("#bindPhone").attr("checked",false);
    } 
    
    
    /* 二级下拉框回调函数 */
    function secondLevelCallBack(data){  
 	   if(data.success){
 		  $("#roleSelect").css("display","block");
 	      $("#secondType").prop("disabled",true);
 		 
 		   var $secondType = $("#secondType");
	 		  for(var j=0;j<data.rows.length;j++){ 			  
	 			var sub = data.rows[j];
	 			$secondType.append('<option value="'+sub.id+'">'+sub.name+'</option>');  	 			
	 			if("${user.roleId}"==sub.id){
	 				document.getElementById("secondType").value=sub.id; 				
	 			}	 			
	 	      }	
 	   }else{
 		   notice.danger(data.msg);
 	   }
   }
      
    /* 所属客服组回调函数 */
    function belongGroupCallBack(data){
    	var spanHtml="";
    	if(data.rows.length!=0){
        	for(var i=0;i<data.rows.length;i++){
        		spanHtml+="<span>"+data.rows[i].groupName+"</span>";
      	    }
    	}
       	 $("#groupName1").empty();
    	 $("#groupName1").append(spanHtml);
    }
    
    
    /* 查看用户详情 */
    function viewDetails(userId,userName){
        var url="<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+userId;
        parent.openTab(url,"user",userName);
    }
</script>
</body>
</html>