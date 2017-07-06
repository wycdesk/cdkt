<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>工单中心</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/order.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
</head>
<body>
<div class="panel-title">
    <div class="main">
        <div class="show">
            <a id="creator" class="btn-sm" href="javascript:void(0);"></a>
            <a id="workOrderId" class="btn-sm" href="#"></a>
        </div>
    </div>
</div>
<div id="left-part" >
    <div class="scrollbar-box ps-container">
        <div class="container-fluid">
            <form class="form-horizontal">
                <fieldset>                
                    <div class="form-group">
					    <label class="control-label">工单自定义字段</label>
							<select class="form-control" id="definedfield" onchange="chooseTemplate(this)">
					    </select>
				    </div>	               
                    <div class="form-group">
                        <label class="control-label">抄送副本<a onclick = "$('#copyMailAddr').val('${userName}'+' ${mailAddr}')">抄送给我</a></label>
                        <input id="copyMailAddr" class="form-control" maxlength="99" type="text" autocomplete="off" disabled>
                    </div>
                       <div id="field"></div>
                </fieldset>                
            </form>
        </div>
    </div>
</div>
<div id="right-part">
    <div class="right-content">
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
                   <a id="openOrClose" onclick="openOrClose()" class="unfold" style="line-height: 35px;padding-top: 10px;">
                      <span id="open">展开</span>
                      <span id="close" style="display: none;">收起</span>
                   </a>	 
                   <ul id="userFieldUl" style="display:none;">
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
                    <div class="ticket-header">
                        <div class="ticket_channel" >
                            <img src="<%=request.getContextPath()%>/static/images/youjian.png" />
                        </div>
                        <div class="info ">
                            <h3>
                                <input id="title" class="modify" maxlength="120" type="text" placeholder="">
                            </h3>
                            <p>
                                <span id="createTime"></span> ·<span id="source"></span>·<span></span>
                            </p>
                        </div>
                        <c:if test="${status!='4' }">
                        	<div class="ticket-header-btns">
                            	<a class="btn btn-sm" data-toggle="modal" data-target="#mergeOrderModal">
                            		合并工单
                            	</a>
                        	</div>
                        </c:if>
                    </div>
                    <article class="ticket-inner">
                        <div class="wrap">
                            <div class="radio-box">
                                <label>
                                    <input class="ember-view ember-radio" type="radio" checked value="0" name="replay-choice">  公开回复 </label>
                                <label>
                                    <input class="ember-view ember-radio" type="radio" value="1" name="replay-choice">  私密回复<span>(仅客服可见)</span>
                                </label>
                            </div>
                            <div class="md-editor" id="editor"></div>
                            <div>
                            	<p align="center"><font color="blue">上传文件类型可为图片或txt格式等，大小不能超过
                            	<font color="red">2m</font>!</font></p>
                            </div>
                            <div>
                                <div class="uploader">
                                    <div id="fileGroup"></div>
                                    <form action="/"
                                          class="dropzone"
                                          enctype="multipart/form-data"
                                          id="my-dropzone"
                                          method="post">
                                    </form>
                                </div>
                            </div>

                        </div>
                    </article>
                    <div>
                        <div class="ticket-comment-header">
                            <h3 id="ticketCommentHeaderTitle">工单回复<span></span></h3>
                            <div class="btn-group fr">
                                <a class="btn btn-sm active" id="orderCommentBtn">工单回复</a>
                                <a class="btn btn-sm btn-br" id="orderEventBtn">工单事件</a>
                            </div>
                        </div>
                        <!-- 工单回复列表 -->
                        <div class="ticket-comment-list" id="orderCommentList"></div>
                        <!-- 工单事件列表 -->
                        <div class="ticket-comment-list" id="eventList" style="display:none;"></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="bottom-bar" class="show" >
    <div class="btn-group dropup" id="lidiv">
        <a id="submitFun" class="btn btn-raised btn-danger" data-href="0">提交为<span id="submitStr" >尚未受理</span></a>
        <button id = "buttonSmall" type="button" class="btn btn-raised btn-danger dropdown-toggle border-le" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false" >
            <span class="caret"></span>
            <span class="sr-only">Toggle Dropdown</span>
        </button>
        <ul class="dropdown-menu" >
            <li class="dropdown-header">更新工单</li>
            <li role="separator" class="divider"></li>
            <li><a data-href="1">提交为<span class="status status1">受理中</span></a></li>
            <li><a data-href="2">提交为<span class="status status2">等待回复</span></a></li>
            <li><a data-href="3">提交为<span class="status status3">已解决</span></a></li>
        </ul>
    </div>    
</div>
<%@include file="mergeOrderModal.jsp"%>
<script id="event-template" type="text/x-handlebars-template">
    <div class="ticket-comment-item">
        <article>
            <div class="wrap">
                <a class="user-avatar" onclick="showUserDetail('{{updatorId}}', '{{updatorName}}')">
                    <img onclick="showUserDetail('{{updatorId}}', '{{updatorName}}')" width="80" height="80" src="<%=request.getContextPath()%>/static/images/avater/{{creatorName}}" onerror="this.src='<%=request.getContextPath()%>/static/images/avater/agent.jpg'"> </a>
                <header>
                    <a class="ember-view" onclick="showUserDetail('{{updatorId}}', '{{updatorName}}')">{{updatorName}}</a>  ·
                    <time>{{contentUpdateDate}}</time>
                </header>
                <ul class="event-list">
                    {{#if content}}
                    <li class="event-comment">
                        <p>{{{content}}}</p>
                    </li>
                    {{/if}}
                    {{#each statuslist}}
                    {{#equal this.eventDisplay '1'}}
                    {{#if this.oldValue}}
                    <li class="event_action"><strong>{{this.fieldName}}</strong> {{this.oldValue}} 改为 {{this.newValue}}</li>
                    {{else}}
                    <li class="event_action"><strong>{{this.fieldName}}</strong> 为 {{this.newValue}}</li>
                    {{/if}}
                    {{/equal}}
                    {{/each}}
                </ul>
            </div>
        </article>
    </div>
</script>
<script id="comment-body-template" type="text/x-handlebars-template">
    <div class="ticket-comment-item">
        <article>
            <div class="wrap">
                <a class="ember-view user-avatar" onclick="showUserDetail('{{updatorId}}', '{{updatorName}}')">
                    <img onclick="showUserDetail('{{updatorId}}', '{{updatorName}}')" src="{{#equal isAgent '1'}}<%=request.getContextPath()%>/static/images/avater/agent.jpg{{/equal}}{{#equal isAgent null}}<%=request.getContextPath()%>/static/images/avater/end_user.jpg{{/equal}}" onerror="this.src='<%=request.getContextPath()%>/static/images/avater/agent.jpg'">
                </a>
                <header>
                    <a class="ember-view" onclick="showUserDetail('{{updatorId}}', '{{updatorName}}')">{{updatorName}}</a>
                    <time>{{#formatDate contentUpdateDate "yyyy年MM月dd日 hh:mm"}}{{/formatDate}}</time>
					{{#equal isAgent '1'}}
						<a class="reassign">分配给他</a>
					{{/equal}}
                    {{#equal isPublicReply "1"}}<span class="private-replay">私有回复</span>{{/equal}}
                </header>
                <div class="comment-body">
                    {{#if content}}
                    <p>{{{content}}}</p>
                    {{/if}}
                </div>
                {{#each attachment}}
                <div>
                    <a href="<%=request.getContextPath()%>/attachments/download?newFileName={{fileNew}}">{{originalName}}</a>
                </div>
                {{/each}}

				{{#if sessionId}}
					{{#equal source "5"}}
                		<div>
                    		<span>录音：</span>
							<span class="audio-phone"><audio src="" controls="controls" id="recordUrl"></audio></span>
                		</div>
					{{/equal}}
					{{#equal source "6"}}
                		<div>
                    		<span>录音：</span>
							<span class="audio-phone"><audio src="" controls="controls" id="recordUrl"></audio></span>
                		</div>
					{{/equal}}
				{{/if}}

            </div>
        </article>
    </div>
</script>
<script src="<%=request.getContextPath()%>/script/lib/summernote/summernote.js" ></script>
<script src="<%=request.getContextPath()%>/script/lib/summernote/summernote-zh-CN.js" ></script>
<script>	
    //定义优先级常量
    var priortyArray=["-","低","中","高","紧急"];
    var typeArray = ["-","问题","事务","故障","任务"];
    var sourceStr = ["网页表单","WEBCHAT","API接口","邮件","手机端","电话呼入","电话呼出"];
    //定义所获取的工单类型
    var typeText;
    //工单详情的原始值集合
    var originalInfo = {};

	//接收自定义字段的key值
	var field=[];
	/* 接收自定义字段的key对应的type */
	var fieldType={};
	/* 记录复选框自定义字段初始值 */
	var fields=[];
	/* 自定义字段格式校验 */
	var check_bool = {};
	
    $(document).ready(function() {
		/* 自定义字段模板下拉框 */
		setDefinedFiled();
    	
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
		}else if("${user.userStatus}"=="4"){
			$("#span1").addClass("status red");   
			$("#span1").html("停用");
		}
    	    	
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

        //绑定提交按钮组点击事件
        $("#bottom-bar .dropdown-menu li a").on("click",function(){
            var spanStyleText = [{style:'status status1',text:'尚未处理'},{style:'status status1',text:'受理中'},{style:'status status2',text:'等待回复'},{style:'status status3',text:'已解决'}];
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
    });

    /**
     * 获取客服组下拉框信息
     */
    function getAgentGroupInfo(){

        var $serviceGroupName = $("#serviceGroupName");
        var param = {
            userId:'${userId}',
            entId:'${entId}'
        };
        $.post("<%=request.getContextPath()%>/userApi/queryGroupAgent",param,function(data){
            if(data.result == "0"){
                var data = data.rows,
                    $s   = $("#serviceGroupName"),
                    opts = ['<option value="">-</option>'];
                for(var i=0;i<data.length;i++){
                    opts.push('<option value="' + data[i].groupId + '">' + data[i].groupName + '</option>');
                }
                $s.html(opts.join(""));
                loadRightInfo();
            }
        });
        //为客服组下拉菜单绑定onchange事件
        $serviceGroupName.change(function(){
            getAgentInfo($(this).val());
        });
    }

    /**
     * 获取客服下拉框信息
     */
    function getAgentInfo(groupId) {    	
        if (groupId != null && groupId != "undefined") {
            var param = {
                entId:'${entId}',
                groupId:groupId
            },
            $c   = $("#customServiceName");

            if(groupId==""){
                $c.html('<option value="">-</option>');
                return;
            }
            $.post("<%=request.getContextPath()%>/userApi/queryGroupAgent",param, function(data){
                if(data.result == "0"){
                    var data = data.rows[0].agentList,
                        opts = ['<option value="">-</option>'];
                    for (var i = 0; i < data.length; i ++) {
                        opts.push('<option value="' + data[i].userId + '">' + data[i].userName + '</option>');
                    }
                    $c.html(opts.join(""));
                    $c.val($c.data("original-value"));
                }
            });
        }
    }

    /* 加载字段信息 */
    function loadRightInfo() {
        var params={};
        var param = {};
        param.workId=parseInt("${workId}");
        params.info=JSON.stringify(param);
        $.ajax({
            url : "http://<%=request.getServerName()%>:"+parent.workBasePath+"/workorder/detail?sessionKey="+ $.cookie("sessionKey"),
            type : "post",
            dataType : 'jsonp',
            data : params,
            success : function(data) {
                if (data.success) {
                    //查询成功
                    console.dir(data);
                    var leftObj = data.rows[0];
                    if (leftObj != null && leftObj != undefined) {
                    	var tempId = leftObj.tempId || "";                   	
                        var serviceGroupId = leftObj.serviceGroupId || "";
                        var customServiceId = leftObj.customServiceId || "";
                        var status = leftObj.status || "";
                        var type= leftObj.type || "";
                        var priority = leftObj.priority || "";
                        var title = leftObj.title || "";

                        fields=[];
                        /* 工单自定义字段的处理 */
                        for(var i=0;i<field.length;i++){
                        	var f = field[i];
                        	f = leftObj[field[i]] || "";   
                        	/* 记录原始工单自定义字段信息 */
                        	originalInfo[field[i]] = f;  
                        	
                        	/* 复选框类型的自定义字段 */
                        	if(fieldType[field[i]]=="4"){                        		
                        	    /* 复选框赋值 */	                        	    
                        	    $("#"+field[i]+" input[type=checkbox]").each(function(){                       	       	
                        	       var checkValue=$(this).next().text();
                        	       for(var i=0;i<f.length;i++){
                        	          if(checkValue==f[i]){
                        	             $(this).attr("checked",true);
                        	             
                        	             fields.push($(this).val());
                        	          }                     	          
                        	       }                       	       
                        	    }); 
                        	    $("#"+field[i]).val(fields).data("original-value",fields).data("field",field[i]).data("type","4"); 
                        	}else{
                                /* 设置工单自定义字段信息 */
                                $("#"+field[i]).val(f).data("original-value",f).data("field",field[i]).data("type",fieldType[field[i]]); 
                        	}                    	
                        }
                                               
                        //如果工单状态不为‘尚未受理’，则将工单状态下拉菜单中的‘尚未受理’项移除。
                        if (status > 0) {
                            $("#wfStatus>option[value='0']").remove();   
                        }

                        //记录原始工单信息
                        originalInfo["tempId"] = tempId;
                        originalInfo["serviceGroupId"] = serviceGroupId;
                        originalInfo["customServiceId"] = customServiceId;
                        originalInfo["copyMailAddr"] = $("#copyMailAddr").val();
                        originalInfo["status"] = status;
                        originalInfo["type"] = type;
                        originalInfo["priority"] = priority;
                        originalInfo["labelStr"] = $("#labelStr").val();
                        originalInfo["title"] = title;

                        /* 设置模板信息 */
                        $("#definedfield").data("original-value",tempId).data("field","tempId").data("type","3");
                        
                        //设置客服组信息
                        $("#serviceGroupName").val(serviceGroupId).data("original-value",serviceGroupId).data("field","serviceGroupId").data("type","3");

                        $("#groupName").html(leftObj.serviceGroupName || "-");

                        $("#acceptcustom").html(leftObj.customServiceName || "-");
                        //设置客服信息
                        $("#customServiceName").val(customServiceId).data("original-value",customServiceId).data("field","customServiceId").data("type","3");
                        //工单状态
                        $("#wfStatus").val(status).data("original-value",status).data("field","status").data("type","3");
                        //工单类型
                        $("#wfType").val(type).data("original-value",type).data("field","type").data("type","3");
                        //优先级
                        $("#priority").val(priority).data("original-value",priority).data("field","priority").data("type","3");
                        //工单标题
                        $("#title").val(title).data("original-value",title).data("field","title").data("type","1");

                        if(isNaN(parseInt(priority))){
                            $("#prioritydiv").html(priortyArray[0]);
                        }else{
                            $("#prioritydiv").html(priortyArray[parseInt(priority)]);
                        }
                        if(isNaN(parseInt(type))){
                            $("#worktype").html(typeArray[0]);
                            typeText = typeArray[0];

                        }else{
                            $("#worktype").html(typeArray[parseInt(type)]);
                            typeText = typeArray[parseInt(type)];
                        }
                    }
                    createTop(data.rows[0]);
                    
                    createReply(data.rows[0].event);
                    
                    $("#serviceGroupName").change();

                    //把订单详情信息写入到工单合并弹出框
                    var eventList = data.rows[0].event;
                    var _lastContent = '';
                    for(var i = eventList.length-1;i>=0;i--){
                        if(eventList[i].content){
                            _lastContent = eventList[i].content;
                        }
                    }
                    $("#mergeOrderModal .order-title").text('#'+leftObj.workId + ',' + title);
                    _lastContent = '#'+leftObj.workId+' “' + title + '” 已合并至此工单，并在此工单进行讨论。<br/>'+ '#'+leftObj.workId+' 的最后回复：'+_lastContent;
                    $("#order2Content").code(_lastContent);

                } else {
                    notice.alert("左侧数据查询失败！"+data.msg);
                }
            }
        });
    }

    function createTop(data){
        var $creator = $("#creator");
        var createDate = cri.formatDate(new Date(data.createDate),"yyyy-MM-dd HH:mm:ss");
        var $wfOps = $("#wfStatus>option:selected");
        var status = $wfOps.val();
        var statusStr = $wfOps.text();
        //0：尚未受理、1：受理中、2：等待客户回复、3：已解决、4：已关闭
        var ss = ['status0','status1','status2','status3','status4'];

        $creator.text("工单发起人：" + data.creatorName + " " + data.creatorEmail + "");
        $creator.click(function(){
            <%-- parent.openTab("<%=request.getContextPath()%>/usrManage/userDetails?userId="+data.creatorId,'user',data.creatorName); --%>
            
            var url="<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+data.creatorId;
            parent.openTab(url,"user",data.creatorName,true);
        });

        $("#createTime").html("创建于 "+ createDate);
        $("#source").html("通过 "+sourceStr[data.source]);
        $("#workOrderId").html("# "+data.workId + " 工单").append("<span id='workOrderStatus' class='status " + ss[status] + "'>"+ statusStr +"</span>");
        $("#replyNum").html("("+data.event.length+")");
        //设置提交按钮的显示值和调用方法
        setSubmitAttr(status, statusStr);
    }

    //设置提交按钮的显示值和调用方法
    function setSubmitAttr(status, statusStr) {
        var spanStyleText = [{style:'status status1',text:'尚未受理'},{style:'status status1',text:'受理中'},{style:'status status2',text:'等待回复'}],
            $s            = $("#submitFun"),
            submitH       = $s.attr("data-href");
                
        if(submitH != status){
            var $a = $s.siblings('ul').find('a[data-href='+status+']');
            //var thisStyleText = spanStyleText[parseInt(submitH)];
            //$a.attr("data-href",submitH).find("span").text(thisStyleText.text).attr("class",thisStyleText.style);
            
            if(status==null){
            	 $s.attr("data-href",status).find("span").text("已关闭");
            }else{
            	$s.attr("data-href",status).find("span").text(statusStr);
            }         
        }
    }

    //选择工单状态后触发的方法——提交按钮相应变更
    function chooseWfStatus() {
        var _s = $("#wfStatus>option:selected");
        setSubmitAttr(_s.val(), _s.text());
    }

    /**
     *创建工单回复事件
     */
    var createReply = (function(){
        var eventTemp = Handlebars.compile($("#event-template").html());
        var commentTemp = Handlebars.compile($("#comment-body-template").html());

        var $workreply = $("#orderCommentList");
        var $eventList = $("#eventList");

        return function(eventList){
            var eventHtml = [];
            var commentHtml = [];
            for(var i= 0,len=eventList.length; i<len;i++){
            	if(!eventList[i].content){
            		continue;
            	}
                var e = eventList[i];
                eventHtml.push(eventTemp(e));
                if(e.content){              	        	
                	console.log(typeof e.attachment);
                	if(typeof e.attachment == 'string'){
                		e.attachment = $.parseJSON(e.attachment);
                	}
                	
                	var $tmp=$(commentTemp(e));
                	if(e.sessionId&&(e.source=='5'||e.source=='6')){
                		$audio=$($tmp.find("audio")[0]);
                		var url="<%=request.getContextPath()%>/communicate/getRecordUrl"
                		$.ajax({
                            url :url ,
                            type : "post",
                            dataType : 'json',
                            data:{
                            	"sessionId":e.sessionId, 
                            	"ccodEntId":e.ccodEntId,
                            	"ccodAgentId":e.ccodAgentId
                            },
                            success : function(data) {
                                if (data.success) {
                                    var url=data.rows;
                                    $audio.on("error",function(){
                       					if($audio.siblings("font").length<=0){
                       						$audio.css("display","none");
                       						$audio.after("<font color='red'>录音资源不存在</font>");
                       					}
                       				});
                                    $audio.attr("src",url);
                                } else {
                                  notice.warning(data.msg);
                                }
                            }
                        });
                	}
                	commentHtml.push($tmp);
                }
            }
            $workreply.empty().html(commentHtml);
            $eventList.empty().html(eventHtml);
            $("#orderCommentBtn").click(function(e){
                $("#orderEventBtn").removeClass("active");
                $(this).addClass("active");
                $("#orderCommentList").show();
                $("#eventList").hide();
                $("#ticketCommentHeaderTitle").html("工单回复");
            });

            $("#orderEventBtn").click(function(e){
                $("#orderCommentBtn").removeClass("active");
                $(this).addClass("active");
                $("#orderCommentList").hide();
                $("#eventList").show();
                $("#ticketCommentHeaderTitle").html("工单事件");
            });
        };
    })();

    /**
     * @param type 0:尚未处理,1:受理中,2:等待回复,3:已解决
     */
    function submit(type){   	
		/* 自定义字段格式校验 */		
		for(var i=0;i<field.length;i++){
 			if(check_bool[field[i]]=="false"){
				notice.warning($("#"+field[i]).data("fields")+" 格式不正确!");  
				return;
			}	 
		}
		
        //是否公开回复标记
        var isPublicReply = $("input[name='replay-choice']:checked").val();
        var $wfStatus = $("#wfStatus");
        var $customServiceName = $("#customServiceName");
        var content = $("#editor").code();
        var contentText = $.trim($(content).text());
        //工单状态联动
        $wfStatus.val(type);
        //数据提交之前的校验
        var inputArr = [$("#definedfield"),$("#serviceGroupName"),$customServiceName,$wfStatus,$("#wfType"),$("#priority"),$("#title")];
        for(var i=0;i<field.length;i++){
        	inputArr.push($("#"+field[i]));           	
        }
        
        if (inputArr[1].find('option:selected').val() == "" || inputArr[1].find('option:selected').val() == undefined) {
            notice.alert('受理客服组不能为空！','alert-danger');
            return;
        }
        //工单状态不为‘尚未受理’时，受理人不能为空！
        if (type != 0 && (inputArr[2].find('option:selected').val() == "" || inputArr[2].find('option:selected').val() == undefined)) {
            notice.alert('受理客服不能为空！','alert-danger');
            return;
        }
        /* 标题不能为空 */
        if ($("#title").val()=="") {
            notice.alert('标题不能为空！','alert-danger');
            return;
        }      
   	    //当有客服人的时候，工单状态不能为受理中
   	    if($customServiceName.val()!=""){
     	   if($wfStatus.val()=="0"){
     		   $wfStatus.val("1");
     	   }
        }
   	    if($wfStatus.val()=="" || $wfStatus.val()==null){
   	        notice.alert('状态不能为空！','alert-danger');
            return;
   	    }
    	    	    
        //左侧信息和右侧信息都没有变动，不能提交
         /* 自定义字段处理 */
         var bfield=true;        
         for(var i=0;i<field.length;i++){        	 
        	if(fieldType[field[i]] == "4"){      		
        		var c_fields=[];//存放自定义复选框提交参数   	        		
                $("#"+field[i]+" input[type=checkbox]:checked").each(function(){  				
                	c_fields.push($(this).val());
            	});
                if(originalInfo[field[i]] != c_fields+"")
                	bfield=false;          	                   
        	}else if(fieldType[field[i]] == "3"){        		
             	 if(originalInfo[field[i]]!=inputArr[7+i].find('option:selected').val())
             		bfield=false;
        	}else{
        		if(originalInfo[field[i]]!=inputArr[7+i].val())
        			bfield=false;           	
        	}    	
        } 
                 
        if (originalInfo["tempId"] == inputArr[0].find('option:selected').val() &&
            originalInfo["serviceGroupId"] == inputArr[1].find('option:selected').val() &&
            originalInfo["customServiceId"] == inputArr[2].find('option:selected').val() &&
            originalInfo["copyMailAddr"] == $("#copyMailAddr").val() &&
            originalInfo["status"] == inputArr[3].find('option:selected').val() &&
            originalInfo["type"] == inputArr[4].find('option:selected').val() &&
            originalInfo["priority"] == inputArr[5].find('option:selected').val() &&
            originalInfo["labelStr"] == $("#labelStr").val() &&
            originalInfo["title"] == inputArr[6].val() && 
            contentText == "" && bfield) {
            notice.info("工单还未进行任何更改，不能提交更新！");
            return;
        }
             
        var formValue = {};
        formValue["content"] = content;
        formValue["tempName"] = $("#definedfield").find("option:selected").text();
        
        inputArr.forEach(function($item){
        	/* 复选框类型的字段需要单独处理 */
        	var originalValue = "" + $item.data("original-value");
        	var value="";       	
        	var checkField=[];//存放自定义复选框提交参数   	
        	
        	if($item.data("type")=="4"){       		           		
                $("#"+$item.data("field")+" input[type=checkbox]:checked").each(function(){  				
            		checkField.push($(this).val());
            	});          	
                value = checkField || "";              
        	}else{     		
        		value = $item.val() || "";
        	}
 
            if(originalValue != value){
                if($item.attr("id") == "serviceGroupName"){
                    formValue[$item.attr("id")] = $item.find("option:selected").text();
                    formValue[$item.data("field")] = $item.val();
                }
                if($item.attr("id") == "customServiceName"){
                    formValue[$item.attr("id")] = $item.find("option:selected").text();
                    formValue[$item.data("field")] = $item.val();
                }              
                if($item.data("type")=="4"){     
                	formValue[$item.data("field")] = checkField;
                }else{
                	formValue[$item.data("field")] = $item.val();
                }              
            }            
        });
             
		//{isAgent:"1"}表示当前为座席操作
        ajax($.extend(formValue,{workId:parseInt('${workId}')},{updatorId:"${userId}"},{updatorName:"${userName}"},{sourceMail:"${mailAddr}"},{sourceTitle:$("#title").val()},{isPublicReply:isPublicReply+""},{isAgent:"1"},{attachment:getAttachments()}));
    }
    /*  更新工单 */
    function ajax(params){
        var info = encodeURI(JSON.stringify(params));
        $.ajax({
            url : "http://<%=request.getServerName()%>:"+parent.workBasePath+"/workorder/update?sessionKey="+ $.cookie("sessionKey"),
            type : "post",
            dataType : 'jsonp',
            data:{info:info, entId:"${entId}"} ,
            success : function(data) {
                if (data.success) {
                    notice.alert("工单数据提交成功！","alert-success");
                    location.reload();
                } else {
                    notice.alert("工单数据提交失败！","alert-danger");
                }
            }
        });
    }

    //获取上传附件的信息
    function getAttachments(){
        var attachement=[];
        $("#fileGroup .file").each(function(){
            var data = $(this).data("fileInfo");
            attachement.push(JSON.stringify(data));
        });
        return "["+attachement.join(",")+"]";
    }
    
    function showUserDetail(updatorId, updatorName){
        var url="<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+updatorId;
        parent.openTab(url,"user",updatorName,true);
    }
    
    /*  自定义字段下拉框类型赋值 */
    $("#userFieldUl select").each(function(){
   	var id=$(this).attr("id");
   	var u=${user};
       $(this).val(u[id]);                    
   }); 
    
    /* 自定义字段复选框赋值 */
    $(".checkFieldDiv li").each(function(){
    	var id=$(this).attr("id");
    	var u=${user};
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
        parent.openTab(url,"user",userName,true);
    }
       
    /* 自定义字段输入内容展开或收起 */
    function openOrClose(){
    	if($("#userFieldUl").css("display")=="none"){
    		$("#userFieldUl").css("display","block");
    		$("#open").css("display","none");
    		$("#close").css("display","block");
    	}       
    	else if($("#userFieldUl").css("display")=="block"){
    		$("#userFieldUl").css("display","none");
    		$("#open").css("display","block");
    		$("#close").css("display","none");
    	}
    }
       
    /* 自定义字段模板下拉框 */
	function setDefinedFiled(){
		$.ajax({
			url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/workorder/queryTemplates?sessionKey="+ $.cookie("sessionKey"),			
			type:"post",
			dataType:"jsonp",
		    data: {tempId : "${tempId}"} ,
			success : function(data){
				if(data.success){
					var sub = data.rows;
					var $definedfield = $("#definedfield");
					for(var i=0;i<sub.length;i++){
						$definedfield.append('<option value="' + sub[i].tempId + '">' + sub[i].tempName + '</option>');
					}					
					var tempId = "${tempId}";
					$definedfield.val(tempId);
					
			 		var param = document.getElementById("definedfield");		
					chooseTemplate(param); 
				}
			}
		});
	}
       
    /* 选择自定义字段模板,加载模板字段 */
	function chooseTemplate(param) {
    	field=[];
    	fieldType={};
    	
		var tempId = param.value;
		if (tempId != null && tempId != "undefined" && tempId!="") {				
			var param = {};
			param.tempId = tempId;
			$.ajax({
				url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/workorder/queryTemplateField?sessionKey="+ $.cookie("sessionKey"),		
				type:"post",
				dataType:"jsonp",
				data:param,
				success : function(data){
					if(data.success){
						$("#field").empty();
				        var fieldDiv = $("#field");				        
				        var dataField = data.rows.field; 
				        var dataGroup = data.rows.group;
				        
				        for(var i=0;i<dataField.length;i++){
				        	var el="";
				        	if(dataField[i].componentType=="1"){
				        		if(dataField[i].key!="title"){
					        		el='<div class="form-group"><label class="control-label">'+dataField[i].name+
					        		'</label><input class="form-control" name='+dataField[i].key+' id='+dataField[i].key+
					        		' placeholder="-" type="text" size="30" data-name='+dataField[i].key+'</div>';
				        		    
					        		field.push(dataField[i].key);
					        		fieldType[dataField[i].key]=dataField[i].componentType;
				        		}else{
				        			$("#title").html(dataField[i].name+"*");
				        		}
				        	}				        	
				        	/* 文本区域 */
				        	else if(dataField[i].componentType=="2"){
				        		if(dataField[i].key!="content"){
					        		el='<div class="form-group"><label class="control-label">'+dataField[i].name+
					        		'</label><textarea class="form-control" name='+dataField[i].key+' id='+dataField[i].key+
					        		' data-name='+dataField[i].key+'</div>';
				        		
					        		field.push(dataField[i].key);
					        		fieldType[dataField[i].key]=dataField[i].componentType;
				        		}else{
				        			$("#content").html(dataField[i].name);
				        		}
				        	}				        	
				        	/* 下拉框 */
				        	else if(dataField[i].componentType=="3"){					        		
				        		if(dataField[i].key=="serviceGroupName"){			        			
					        		el='<div class="form-group"><label class="control-label">'+dataField[i].name+
					        		'</label><select class="form-control" id='+dataField[i].key+
					        		' name='+dataField[i].key+' data-name='+dataField[i].key+'></select></div>';
				        		
				        		}else if(dataField[i].key=="customServiceName"){					        			
					        		el='<div class="form-group"><label class="control-label">'+dataField[i].name+
					        		'</label><select class="form-control" id='+dataField[i].key+' name='+dataField[i].key+
					        		' data-name='+dataField[i].key+'></select></div>';		
					        		
				        		}else if(dataField[i].key=="status"){				        			
					        		el='<div class="form-group"><label class="control-label">'+dataField[i].name+
					        		'</label><select onchange="chooseWfStatus();" class="form-control" id="wfStatus" name='+dataField[i].key+' data-name='+dataField[i].key+'>'+				        		
                                    '<option value="0">尚未受理</option>'+
                                    '<option value="1">受理中</option>'+
                                    '<option value="2">等待回复</option>'+
                                    '<option value="3">已解决</option></select></div>';				        			
				        		}else if(dataField[i].key=="type"){				        			
					        		el='<div class="form-group"><label class="control-label">'+dataField[i].name+
					        		'</label><select class="form-control" id="wfType" name='+dataField[i].key+' data-name='+dataField[i].key+'><option value="">-</option>'+				        		
                                    '<option value="1">问题</option>'+
                                    '<option value="2">事务</option>'+
                                    '<option value="3">故障</option>'+
                                    '<option value="4">任务</option></select></div>';                                   
				        		}else if(dataField[i].key=="priority"){				        			
					        		el='<div class="form-group"><label class="control-label">'+dataField[i].name+
					        		'</label><select class="form-control" id='+dataField[i].key+' name='+dataField[i].key+' data-name='+dataField[i].key+'><option value="">-</option>'+				        		
                                    '<option value="1">低</option>'+
                                    '<option value="2">中</option>'+
                                    '<option value="3">高</option>'+
                                    '<option value="4">紧急</option></select></div>';                                     
				        		}else{					        			
					        		el='<div class="form-group"><label class="control-label">'+dataField[i].name+
					        		'</label><select class="form-control" id='+dataField[i].key+' name='+dataField[i].key+' data-name='+dataField[i].key+'><option value="">-</option>';					        		
					        		
					        		for(var j=0;j<dataField[i].candidateValue.length;j++){
					        			el+='<option value='+dataField[i].candidateValue[j]+'>'+dataField[i].candidateValue[j]+'</option>';
					        		}
					        		el+='</select></div>';
					        		
					        		field.push(dataField[i].key);
					        		fieldType[dataField[i].key]=dataField[i].componentType;
				        		}			        		
				        	}				        	
				        	/* 复选框 */
				        	else if(dataField[i].componentType=="4"){
				        		el='<div class="form-group"><input class='+dataField[i].key+' type="hidden"><label class="control-label">'+dataField[i].name+'</label><ul><li id='+dataField[i].key+'>';				        		
				        		for(var j=0;j<dataField[i].candidateValue.length;j++){
				        			el+='<input onclick="" name='+dataField[i].key+' value='+dataField[i].candidateValue[j]+' type="checkbox" ><span>'+dataField[i].candidateValue[j]+'</span>';
				        		}				        		
				        		el+='</li></ul></div>';
				        		
				        		field.push(dataField[i].key);			        		
				        		fieldType[dataField[i].key]=dataField[i].componentType;				        						        	
				        	}	
				        	/* 数字 */
				        	else if(dataField[i].componentType=="6"){
				        		el='<div class="form-group"><label class="control-label">'+ dataField[i].name+
				        		'</label><input onchange="checkInput(this.id, \'int\')" class="form-control" name='+dataField[i].key+' id='+dataField[i].key+
				        		' placeholder="-" type="text" size="30" data-name='+dataField[i].key+' data-fields="'+dataField[i].name+'"</div>';
				        	    
				        		field.push(dataField[i].key);
				        		fieldType[dataField[i].key]=dataField[i].componentType;
				        	}
				        	/* 小数 */
				        	else if(dataField[i].componentType=="7"){
				        		el='<div class="form-group"><label class="control-label">'+ dataField[i].name+
				        		'</label><input onchange="checkInput(this.id, \'float\')" class="form-control" name='+dataField[i].key+' id='+dataField[i].key+
				        		' placeholder="-" type="text" size="30" data-name='+dataField[i].key+' data-fields="'+dataField[i].name+'"</div>';
				        	    
				        		field.push(dataField[i].key);
				        		fieldType[dataField[i].key]=dataField[i].componentType;
				        	}
				        	/* 正则匹配 */
				        	else if(dataField[i].componentType=="8"){
				        		el='<div class="form-group"><label class="control-label">'+ dataField[i].name+
				        		'</label><input onchange="checkInput(this.id, \'customized\')" class="form-control" name='+dataField[i].key+' id='+dataField[i].key+
				        		' placeholder="-" type="text" size="30" data-name='+dataField[i].key+' data-fields="'+dataField[i].name+'"</div>';
				        	    
				        		field.push(dataField[i].key);
				        		fieldType[dataField[i].key]=dataField[i].componentType;
				        	}
				        	/* 电话号码 */
				        	else if(dataField[i].componentType=="9"){
				        		el='<div class="form-group"><label class="control-label">'+ dataField[i].name+
				        		'</label><input onchange="checkInput(this.id, \'phoneNum\')" class="form-control" name='+dataField[i].key+' id='+dataField[i].key+
				        		' placeholder="-" type="text" size="30" data-name='+dataField[i].key+' data-fields="'+dataField[i].name+'"</div>';
				        	    
				        		field.push(dataField[i].key);
				        		fieldType[dataField[i].key]=dataField[i].componentType;
				        	}				        	
				        	fieldDiv.append(el);
				        }				        
				        getAgentGroupInfo();
					}
				}
			});
		}
	}
    
    /* 自定义字段类型校验 */
    function checkInput(data, type){
        var nubmer = $("#"+data).val();
        var name = $("#"+data).data("fields");       
        var re = "" ;
        var re1 = "" ;
        var desc = "" ;
        
        if(type=="int"){
            re =  /^\d+$/ ;
            re1 = /^((-\d+)|(0+))$/;
            desc = " 必须是一个整数";
        }
        if(type=="float"){
            re =/^\d+(\.\d+)?$/;
            re1 =/^((-\d+(\.\d+)?)|(0+(\.0+)?))$/;
            desc = " 必须是一个小数";
        }
        if(type=="customized"){
            var re = new RegExp($("#"+data).data("reg"));
            re1 =re;
            desc = " 不符合规则";
        }
        if(type=="phoneNum"){
            var re = new RegExp($("#"+data).data("reg"));
            re1 =re;
            desc = " 不符合规则";
        }
        
        if (!re.test(nubmer) && !re1.test(nubmer) && nubmer!=""){
            notice.warning(name + desc);                 
            check_bool[data] = "false";
        }else{
        	check_bool[data] = "true";
        }
    }
     
</script>
</body>
</html>