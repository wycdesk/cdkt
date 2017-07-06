<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>搜索</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link href="<%=request.getContextPath()%>/static/css/usermanage.css" rel="stylesheet">
    <link href="<%=request.getContextPath()%>/static/css/search.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote.js" ></script>
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote-zh-CN.js" ></script>
     <link href="<%=request.getContextPath()%>/static/css/datapicker/datepicker3.css" rel="stylesheet">
    <script src="<%=request.getContextPath()%>/script/lib/datapicker/bootstrap-datepicker.js"></script>
</head>
<body>
<div class="panel-title">
    <div class="main">聚合搜索</div>
</div>
<section class="panel-main">
    <div class="row">
        <div class="col">
            <div class="search-page-title">
                <div class="top search-all">
                    <div  class="drop-btn-default fl" title="模糊搜索">
                        <button id="dLabel" class="drop-btn dropdown" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">模糊搜索<i class="fa fa-sort-desc"></i>
                        </button>
                        <ul class="dropdown-menu" aria-labelledby="dLabel" id="fieldKeyId">
                        </ul>
                    </div>

                    <input class="ember-text-field" placeholder="请输入搜索关键字" type="search" autocomplete="on" id="searchId">
                    <input type="submit" data-ember-action="2608" value="搜索" onclick="searchBtn()">
                </div>
                <div class="bottom" id="bottom">
                    <!-- Nav tabs -->
                    <ul  class="tab-title1" role="tablist">
                        <li role="presentation" id="workRoleId" class="active"><a href="#search-order" aria-controls="search-order" role="tab" data-toggle="tab">工单</a></li>
                        <li role="presentation" id="userRoleId" ><a href="#search-user" aria-controls="search-user" role="tab" data-toggle="tab">用户</a></li>
                    </ul>
                </div>
            </div>
            <div class="sub-switch-body">
                <div class="body">

                    <div class="sub-switch-menu" id="timeSelect">

                        <span  class="title">时间：</span>

                        <ul>
                            <li class="active" id="allTime"><a>所有时间</a></li>
                        </ul>
                        <ul>
                            <li id="lastWeek"><a>最近一周</a></li>
                        </ul>
                        <ul>
                            <li id="lastMonth"><a>最近一月</a></li>
                        </ul>
                        <ul>
                            <li id="lastThreeMonthes"><a>最近三月</a></li>
                        </ul>
                        <ul>
                            <li id="customTime"><a>自定义时间</a></li>
                        </ul>
                        <ul class="timeing">
                            <div id="datepicker" style="display:none">
                                <input type="text" id="begin_date" name="start" onblur="datepickerCheck()" />
                                <span>&nbsp; 到 &nbsp; </span>
                                <input type="text" id="finish_date" name="end" onblur="datepickerCheck()"/>
                            </div>
                        </ul>
                    </div>
                    <div>
                        <div class="body">
                            <!-- Tab panes -->
                            <div class="tab-content">
                                <!-- 工单列表 -->
                                <div role="tabpanel" class="tab-pane active" id="search-order">
                                    <table class="table" cellspacing="0" cellpadding="0">
                                        <thead>
                                            <tr class="order" id="userTableTr">
                                                <th><input class="all-checkbox" type="checkbox" id="allOrderSelect"> </th>
                                                <th>编号</th>
                                                <th>标题</th>
                                                <th>工单发起人</th>
                                                <th>创建日期</th>
                                                <th>受理客服组</th>
                                                <th>工单受理客服</th>
                                                <th>工单优先级</th>
                                                <th>类型</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                        <tfoot>
                                            <tr id="paginationOrderTR">
                                                <td colspan="9">
                                                    <div id="pagination">
                                                    </div>
                                                </td>
                                            </tr>
                                        </tfoot>
                                    </table>
                                </div>
                                
                                <!-- 没有工单-->
                                <div class="sub-switch-menu" id="noOrderGrid" style="display:none">
                                                                                                  没有工单 
                                </div>  
                                
                               <!-- 用户列表 -->
                                <div role="tabpanel" class="tab-pane" id="search-user">
                                    <table class="table" cellspacing="0" cellpadding="0">
                                        <thead>
                                            <tr class="order" id="userTableTr">
                                                <th><input class="all-checkbox" type="checkbox" id="allSelect"> </th>
                                                <th>用户名</th>
                                                <th>邮箱</th>
                                                <th>电话</th>
                                                <th>创建时间</th>
                                                <th>最后登录</th>
                                            </tr>
                                        </thead>
                                        <tbody>
                                        </tbody>
                                        <tfoot>
                                            <tr id="paginationTR">
                                                <td colspan="6">
                                                    <div id="pagination1" align="right">
                                                    </div>
                                                </td>
                                            </tr>
                                        </tfoot>
                                    </table>
                                </div>
                                
                                <!-- 没有用户 -->
                                <div class="sub-switch-menu" id="noUserGrid" style="display:none">
                                                                                                  没有用户 
                                </div>                                
                            </div>
                        </div>
                                        
                    </div>
                </div>
            </div>
        </div>
    </div>
</section>
<div id="toolbar" >
    <button id="deleteBtn" type="button" class="btn btn-raised left-btn btn-danger">删除</button>
    <button id="cancelBtn" type="button" class="btn btn-raised btn-default btn-dark" onclick="cancel()">取消</button>
    <button id="editBtn" type="button" class="btn btn-raised btn-primary" data-toggle="modal" data-target="#editUserModal">编辑用户</button>
</div>

<!-- 编辑用户 -->
<div id="editUserModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">批量编辑用户</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-sm-10 col-sm-offset-1">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">账号状态 </label>
                                    <select class="form-control" id="statusSelect">
                                        <option value="">不改变</option>
                                        <option value="1">正常</option>
                                        <option value="2">暂停</option>
                                        <option value="3">未核审</option>
                                    </select>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="editUserSubmit()" id="editUserSubmit">提交</button>
            </div>
        </div>
    </div>
</div>

<!-- 删除客户 -->
<div id="deleteUserModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">删除用户</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-sm-10 col-sm-offset-1">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <span>注意 </span>
                                </div>
                                <div class="form-group" id="delSingleUser">
                                   <p>
                                                                                                            是否确认删除用户
                                    <span class="green" id="delUser"></span>
                                                                                                             ？删除后，跟此用户
                                    <span class="red">相关工单</span>
                                                                                                              和
                                    <span class="red">文档</span>
                                                                                                             会被一起删除，且
                                    <span class="red">无法恢复</span>
                                                                                                              ，请慎重操作！
                                    </p>                                                               
                                </div>
                                
                                <div class="form-group" id="delMultipleUser" style="display:none">
                                   <p>
                                                                                                            是否确认删除
                                    <span class="red" id="userNum"></span>
                                                                                                            个用户 ？删除后，跟此用户
                                    <span class="red">相关工单</span>
                                                                                                              和
                                    <span class="red">文档</span>
                                                                                                             会被一起删除，且
                                    <span class="red">无法恢复</span>
                                                                                                              ，请慎重操作！
                                    </p>                                                               
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="deleteUserSubmit()" id="deleteUserSubmit">确认删除</button>
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal" onclick="cancel()">取消</button>
            </div>
        </div>
    </div>
</div>

<!-- 删除客服/管理员 -->
<div id="deleteAgentModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">删除用户</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-sm-10 col-sm-offset-1">
                          <form class="form-horizontal">
                           <ul>
                            <li>
                                <div class="form-group">
                                <span>注意 </span>
                                </div>
                                <div class="form-group" id="delSingleAgent">
                                   <p>
                                                                                                            是否确认删除用户
                                    <span class="green" id="delAgent"></span>
                                                                                                             ？删除后，跟此用户
                                    <span class="red">相关工单</span>
                                                                                                              和
                                    <span class="red">文档</span>
                                                                                                             会被一起删除，且
                                    <span class="red">无法恢复</span>
                                                                                                              ，请慎重操作！
                                    </p>                                                               
                                </div>
                                
                                <div class="form-group" id="delMultipleAgent" style="display:none">
                                   <p>
                                                                                                            是否确认删除
                                    <span class="red" id="agentNum"></span>
                                                                                                            个用户 ？删除后，跟此用户
                                    <span class="red">相关工单</span>
                                                                                                              和
                                    <span class="red">文档</span>
                                                                                                             会被一起删除，且
                                    <span class="red">无法恢复</span>
                                                                                                              ，请慎重操作！
                                    </p>                                                               
                                </div>
                            </li>                           
                            <li>
                                <div class="form-group">
                                    <span class="red">警告 </span>
                                </div>
                                <div class="form-group">
                                   <input type="text" class="form-control" id="inputYes" placeholder="确认删除请在此输入：yes">                          
                                   <p class="red" id="tishi1">您将要删除的用户是客服用户！</p>
                                   
                                   <p class="red" id="tishi2" style="display:none">您将要删除的用户中包含
                                   <span class="red" id="agentNum1"></span>
                                                                                                          个客服用户！
                                   <br>
                                   <span class="green" id="agentDetails" style="display:none"></span>                                                                       
                                   </p>
                                </div>
                            </li>
                           </ul>
                          </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="deleteAgentSubmit()" id="deleteAgentSubmit">确认删除</button>
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
            </div>
        </div>
    </div>
</div>

<%@include file="/views/order/editWf.jsp" %>  
<!-- 用户模板 -->
<script id="table-tr-template" type="text/x-handlebars-template">
    <tr>
        <td>{{#equal userType '普通用户'}}<input type="checkbox">{{/equal}}
        {{#equal userType '坐席客服'}}<input type="checkbox">{{/equal}}
        </td>

        <td style="display:none">{{userId}}</td>
        <td style="display:none">{{userLabel}}</td>
        <td data-prop="userName" class="user">
            <a class="avatar"><img src="<%=request.getContextPath()%>/{{photoUrl}}" onclick="viewDetails('{{userId}}','{{userName}}')"></a>
            <div class="user-right">
                <a class="name" href="#" onclick="viewDetails('{{userId}}','{{userName}}')">{{userName}}

                    <span class="{{#equal phoneBinded '手机已绑定'}}status blue{{/equal}}">{{phoneBinded}}</span>
                </a>

                <span class="{{#equal userStatus '暂停'}}status red{{/equal}}{{#equal userStatus '未核审'}}status orange{{/equal}}">{{userStatus}}</span>
                <span class="privilege">{{userType}}</span>
            </div>
        </td>
       
		<td data-prop="email">{{email}}</td>
        <td data-prop="telPhone">{{telPhone}}</td>
        <td data-prop="createTime">{{createTime}}</td>
        <td data-prop="loginTime">{{loginTime}}</td>
    </tr>
</script>

<script id="table-tr-template-agent" type="text/x-handlebars-template">
    <tr>
        <td >{{#equal userType '普通用户'}}<input type="checkbox">{{/equal}}</td>

        <td  style="display:none">{{userId}}</td>
        <td style="display:none">{{userLabel}}</td>
        <td data-prop="userName" class="user">
            <a class="avatar"><img src="<%=request.getContextPath()%>/{{photoUrl}}" onclick="viewDetails('{{userId}}','{{userName}}')"></a>
            <div class="user-right">
                <a class="name" href="#" onclick="viewDetails('{{userId}}','{{userName}}')">{{userName}}

                    <span class="{{#equal phoneBinded '手机已绑定'}}status blue{{/equal}}">{{phoneBinded}}</span>
                </a>

                <span class="{{#equal userStatus '暂停'}}status red{{/equal}}{{#equal userStatus '未核审'}}status orange{{/equal}}">{{userStatus}}</span>
                <span class="privilege">{{userType}}</span>
            </div>
        </td>
        <td data-prop="email">{{email}}</td>
        <td data-prop="telPhone">{{telPhone}}</td>
        <td data-prop="createTime">{{createTime}}</td>
        <td data-prop="loginTime">{{loginTime}}</td>
		
    </tr>
</script>

<script id="table-tr-template-founder" type="text/x-handlebars-template">
    <tr>
        <td>{{#equal userType '普通用户'}}<input type="checkbox">{{/equal}}
        {{#equal userType '坐席客服'}}<input type="checkbox">{{/equal}}
        {{#equal userType '管理员'}}<input type="checkbox">{{/equal}}
        </td>

        <td style="display:none">{{userId}}</td>
        <td style="display:none">{{userLabel}}</td>
        <td data-prop="userName" class="user">
            <a class="avatar"><img src="<%=request.getContextPath()%>/{{photoUrl}}" onclick="viewDetails('{{userId}}','{{userName}}')"></a>
            <div class="user-right">
                <a class="name" href="#" onclick="viewDetails('{{userId}}','{{userName}}')">{{userName}}

                    <span class="{{#equal phoneBinded '手机已绑定'}}status blue{{/equal}}">{{phoneBinded}}</span>
                </a>

                <span class="{{#equal userStatus '暂停'}}status red{{/equal}}{{#equal userStatus '未核审'}}status orange{{/equal}}">{{userStatus}}</span>
                <span class="privilege">{{userType}}</span>
            </div>
        </td>
       
		<td data-prop="email">{{email}}</td>
        <td data-prop="telPhone">{{telPhone}}</td>
        <td data-prop="createTime">{{createTime}}</td>
        <td data-prop="loginTime">{{loginTime}}</td>
    </tr>
</script>

<!-- 工单模板 -->
<script id="table-tr-template-order" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td> <input class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td>
        <td>{{workId}}</td>
        <td class="title"><a>{{title}}</a></td>
        <td>{{creatorName}}</td>
        <td>{{createDate}}</td>
        <td>{{serviceGroupName}}</td>
        <td>{{customServiceName}}</td>
        <td>{{priority}}</td>
        <td>{{type}}</td>
    </tr>
</script>

<script id="tooltip-template" type="text/x-handlebars-template">
    <div class="tip-content panel panel-default">
        <div class="tip-header panel-heading">
            <span class="col-12"># {{workId}}</span><span class="tip-order-state">{{statusStr}}</span>
        </div>
        <div class="tip-row panel-body">
            <div class="container-fluid">
                <div class="row">
                    <span class="col-xs-3">标题</span><span class="col-xs-9">{{title}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">时间</span><span class="col-xs-9">{{createDate}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">发起人</span><span class="col-xs-9">{{creatorName}}</span>
                </div>
            </div>
            {{#if event.[0]}}
            <div class="container-fluid">
                <div class="row">
                    <span class="col-xs-12">最新回复</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">内容</span><span class="col-xs-9">{{event.[0].[0]}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">回复人</span><span class="col-xs-9">{{event.[0].[3]}}</span>
                </div>
            </div>
            {{/if}}
        </div>
    </div>
</script>

<!-- Data picker -->
<script src="<%=request.getContextPath()%>/H+3.2/js/plugins/datapicker/bootstrap-datepicker.js"></script>
<script type="text/javascript">
  var wfStatus = ["尚未受理","受理中","等待客户回复","已解决","已关闭"];

   var now = new Date();
   var beforeWeek = new Date(now.getTime() - 7*24*3600*1000);
   var beforeMonth = new Date(now.getTime() - 30*24*3600*1000);
   var beforeThreeMonth = new Date(now.getTime() - 3*30*24*3600*1000);

   $(function(){
	   $('#searchId').val("${key}");
	   if("${type}"=="user"){
		   $(".tab-title1  li").removeClass("active");
		   $('#userRoleId').addClass("active");
           
		   $("#search-user").show();
		   $("#search-order").hide();
		   switchField("userRoleId");
		   
		   if("${key}"!=""){
			   searchBtn();
		   }else{
			   $("#search-user").hide();
			   $("#noUserGrid").show();
		   }
	   }
	   else if("${type}"=="work"){
		   $(".tab-title1  li").removeClass("active");
		   $('#workRoleId').addClass("active");
		   $("#search-user").hide();
		   switchField("workRoleId");

		   if("${key}"!=""){
			   $("#search-order").show();
			   searchBtn();
		   }else{
			   $("#search-order").hide();
			   $("#noOrderGrid").show();			   
		   }		  
	   }
	  
	   
	   $(".tab-title1  li").click(function(){
		   $(".tab-title1  li").removeClass("active");
           $(this).addClass("active");
            var id=$(this).context.id;
            switchField(id);
            
            if($(this).attr("id")=="userRoleId"){
               var t1="模糊搜索"+"<i class='fa fa-sort-desc'></i>";
               $("#dLabel").html(t1);
            	
     		   $("#search-user").hide();
    		   $("#search-order").hide();
    		   $("#noUserGrid").show();
    		   $("#noOrderGrid").hide();     		   
    	       $("#search-user input[type=checkbox]").each(function(){
    	           $(this).attr("checked",false);
    	       });
    	       $("#toolbar").removeClass("show");
    		   searchBtn();
            }else{
                var t1="模糊搜索"+"<i class='fa fa-sort-desc'></i>";
                $("#dLabel").html(t1);
            	
      		   $("#search-user").hide();
    		   $("#search-order").hide();
    		   $("#noOrderGrid").show();
    		   $("#noUserGrid").hide();  
    	       $("#search-order input[type=checkbox]").each(function(){
    	           $(this).attr("checked",false);
    	       });
    	       $("#toolbar").removeClass("show");
    		   searchBtn();
            }
	   });
	   	    
       $("#search-order th input[type=checkbox]").click(function(){
           $("#search-order td input[type=checkbox]").prop("checked",$(this).prop("checked"));
       });
            
	   $(".sub-switch-menu  li").click(function(){
		   $(".sub-switch-menu  li").removeClass("active");
           $(this).addClass("active");
           if($(this).attr("id")=="customTime"){
        	   $("#datepicker").show();
        	   if($("#begin_date").datepicker({ dateFormat: "yy-mm-dd" }).val()=="")
        	        $("#begin_date").datepicker({ dateFormat: "yy-mm-dd" }).val(beforeWeek.Format("yyyy-MM-dd"));
        	   if($("#finish_date").datepicker({ dateFormat: "yy-mm-dd" }).val()=="")
        	        $("#finish_date").datepicker({ dateFormat: "yy-mm-dd" }).val(now.Format("yyyy-MM-dd"));
           }else{
        	   $("#datepicker").hide();
           }
           searchBtn();
	   });
	    
       $("#toolbar").removeClass("show");
   });

   function switchField(id){
	   $('#fieldKeyId').empty();
       if(id=="userRoleId"){
       	$('#fieldKeyId').append(userOpt);
       }
       if(id=="workRoleId"){
       	$('#fieldKeyId').append(workOpt);
       }
   }
   var workOpt="<li id='li1'><a onclick='getContent(this)'>模糊搜索</a></li><li><a onclick='getContent(this)'>工单编号</a></li><li><a onclick='getContent(this)'>工单标题</a></li><li><a onclick='getContent(this)'>工单优先级</a></li><li><a onclick='getContent(this)'>类型</a></li><li><a onclick='getContent(this)'>工单发起人</a></li><li><a onclick='getContent(this)'>受理客服</a></li><li><a onclick='getContent(this)'>受理客服组</a></li>";
   var userOpt="<li id='li2'><a onclick='getContent(this)'>模糊搜索</a></li><li><a onclick='getContent(this)'>邮箱</a></li><li><a onclick='getContent(this)'>手机号</a></li><li><a onclick='getContent(this)'>用户名</a></li><li><a onclick='getContent(this)'>昵称</a></li>";
   
  function getContent(obj){	  
	  $("#dLabel").html(obj.innerHTML+"<i class='fa fa-sort-desc'></i>");	  
  }
   
   /* 模糊搜索按钮 */
   function searchBtn(){
   	var keyword=$("#searchId").val();
   	var startTime="";
   	var endTime="";
   	
    $("#search-user input[type=checkbox]").each(function(){
        $(this).attr("checked",false);
    });
    $("#search-order input[type=checkbox]").each(function(){
        $(this).attr("checked",false);
    });
    $("#toolbar").removeClass("show");
   	
   	$("#timeSelect li").each(function(){
   		if($(this).attr("class")=="active"){
   			if($(this).attr("id")=="lastWeek"){   			   	
   			    startTime=cri.formatDate(beforeWeek,"yyyy-MM-dd");
			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
   			}
   			if($(this).attr("id")=="lastMonth"){
   			    startTime=cri.formatDate(beforeMonth,"yyyy-MM-dd");
   			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
   			}
   			if($(this).attr("id")=="lastThreeMonthes"){
   			    startTime=cri.formatDate(beforeThreeMonth,"yyyy-MM-dd");
   			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
   			}
   			if($(this).attr("id")=="allTime"){
   			    startTime="";
   			   	endTime="";
   			}
   			if($(this).attr("id")=="customTime"){
   			    startTime=$("#begin_date").datepicker({ dateFormat: "yy-mm-dd" }).val();
   			   	endTime=$("#finish_date").datepicker({ dateFormat: "yy-mm-dd" }).val();
   			}  			
   		}
   	});   	
   	var searchType=$("#dLabel").html();  	
    searchType=searchType.split("<")[0]; 
    
    var url="";
    
    if($("#userRoleId").attr("class")=="active" && $("#searchId").val()!=""){
    	$("#editBtn").attr("data-target","#editUserModal");   	
       	url="<%=request.getContextPath()%>/msearch/fuzzyQueryUsers";
       	
       	getUserTableData({page:1,rows:10,keyword:keyword,startTime:startTime,endTime:endTime,searchType:searchType},url);
    }
    
    if($("#workRoleId").attr("class")=="active" && $("#searchId").val()!=""){
    	$("#editBtn").attr("data-target","#editOrderModal");
       	url="<%=request.getContextPath()%>/msearch/fuzzyQueryOrders";
       	
       	getTableData({page:1,rows:10,keyword:keyword,startTime:startTime,endTime:endTime,searchType:searchType},url);
    } 
  }
  
   /**
    * 向后台请求数据(工单)
    */
   var getTableData = (function(){       
       /**
        * 初始化分页
        */
        var pager = new cri.Pager($("#pagination"),{
           page:1,
           pageSize:10,
           total:0,
           onPage:function(page,pageSize){
           	var url="";  
           	var startTime="";
           	var endTime="";
           	var keyword=$("#searchId").val();
             	$("#timeSelect li").each(function(){
           		var style=$(this).attr("class");
            		if(style=="active"){
               			if($(this).attr("id")=="lastWeek"){
               			    startTime=cri.formatDate(beforeWeek,"yyyy-MM-dd");
               			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
               			}
               			if($(this).attr("id")=="lastMonth"){
               			    startTime=cri.formatDate(beforeMonth,"yyyy-MM-dd");
               			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
               			}
               			if($(this).attr("id")=="lastThreeMonthes"){
               			    startTime=cri.formatDate(beforeThreeMonth,"yyyy-MM-dd");
               			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
               			}
               			if($(this).attr("id")=="allTime"){
               			    startTime="";
               			   	endTime="";
               			}   
               			if($(this).attr("id")=="customTime"){
               			    startTime=$("#begin_date").datepicker({ dateFormat: "yy-mm-dd" }).val();
               			   	endTime=$("#finish_date").datepicker({ dateFormat: "yy-mm-dd" }).val();
               			}               		    
               		    url="<%=request.getContextPath()%>/msearch/fuzzyQueryOrders";
           	        } 
           	    });            	
               var searchType=$("#dLabel").html();  	
               searchType=searchType.split("<")[0];  
               
               getTableData({page:page,rows:pageSize,keyword:keyword,startTime:startTime,endTime:endTime,searchType:searchType},url);
           }
       });
       return function(param,url){
           $.post(url,param,function(data){
               if(data.rows.length=="0"){
        		   $("#search-order").hide();
        		   $("#noOrderGrid").show();
               }else{          	
        		   $("#search-order").show();
        		   $("#noOrderGrid").hide();       		   
        		   createOrderTable(data.rows);
        		   
                   pager.update(param.page,param.rows,data.total);
               }
           });
       }
   })(); 
        
   /**
    * 向后台请求数据(用户)
    */
   var getUserTableData = (function(){       
       /**
        * 初始化分页
        */
        var pager = new cri.Pager($("#pagination1"),{
           page:1,
           pageSize:10,
           total:0,
           onPage:function(page,pageSize){
           	var url="";  
           	var startTime="";
           	var endTime="";
           	var keyword=$("#searchId").val();
             	$("#timeSelect li").each(function(){
           		var style=$(this).attr("class");
            		if(style=="active"){
               			if($(this).attr("id")=="lastWeek"){
               			    startTime=cri.formatDate(beforeWeek,"yyyy-MM-dd");
               			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
               			}
               			if($(this).attr("id")=="lastMonth"){
               			    startTime=cri.formatDate(beforeMonth,"yyyy-MM-dd");
               			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
               			}
               			if($(this).attr("id")=="lastThreeMonthes"){
               			    startTime=cri.formatDate(beforeThreeMonth,"yyyy-MM-dd");
               			   	endTime=cri.formatDate(now,"yyyy-MM-dd");
               			}
               			if($(this).attr("id")=="allTime"){
               			    startTime="";
               			   	endTime="";
               			}
               			if($(this).attr("id")=="customTime"){
               			    startTime=$("#begin_date").datepicker({ dateFormat: "yy-mm-dd" }).val();
               			   	endTime=$("#finish_date").datepicker({ dateFormat: "yy-mm-dd" }).val();
               			}
               		    url="<%=request.getContextPath()%>/msearch/fuzzyQueryUsers";
           	        } 
           	    });            	
               var searchType=$("#dLabel").html();  	
               searchType=searchType.split("<")[0];  
               
               getUserTableData({page:page,rows:pageSize,keyword:keyword,startTime:startTime,endTime:endTime,searchType:searchType},url);
           }
       });
       return function(param,url){
           $.post(url,param,function(data){
               if(data.rows.length=="0"){           	   
                   $("#search-user").hide();
                   $("#noUserGrid").show();
               }else{            	   
                   $("#search-user").show();
                   $("#noUserGrid").hide();                   
                   for(var i=0;i<data.rows.length;i++){
                       if(data.rows[i].loginTime==null)
                           data.rows[i].loginTime="未登录";
                   }
                   createTable(data.rows);
            	   
                   pager.update(param.page,param.rows,data.total);
               }
           });
       }
   })(); 
   
         
   /**
    * 刷新用户表格
    */
   var createTable = (function(){
       var trTemp = Handlebars.compile($("#table-tr-template").html());
       var trTempAgent = Handlebars.compile($("#table-tr-template-agent").html());
       var trTempFounder = Handlebars.compile($("#table-tr-template-founder").html());
       var $table = $("#search-user");
       
       return function(tableData, type){
           var html = [];
           var checkCount = tableData.length;

           for(var i= 0,len=tableData.length; i<len;i++){
               if(tableData[i].userStatus=="2"){
                   tableData[i].userStatus="暂停";

               }else if(tableData[i].userStatus=="3"){
                   tableData[i].userStatus='未核审';

               }else{
                   tableData[i].userStatus="";
               }

               if(tableData[i].phoneBinded=="1"){
                   tableData[i].phoneBinded="手机已绑定";
               }else{
                   tableData[i].phoneBinded="";
               }  
               var $tr="";
               if("${userType1}"=="3" && "${isFounder}"!="1"){
                   if(tableData[i].userType=="管理员")
                	   checkCount=checkCount-1;                 	
               	   if(tableData[i].founder=="1")
                       tableData[i].userType="创始人";
                   $tr = $(trTemp(tableData[i])); 
               }                
               if("${userType1}"=="3" && "${isFounder}"=="1"){
              	   if(tableData[i].founder=="1"){
                   	   tableData[i].userType="创始人";
                   	   checkCount=checkCount-1;
                   }
                   $tr = $(trTempFounder(tableData[i])); 
              }                
               if("${userType1}"=="2"){
                   if(tableData[i].userType!="普通用户")
               	       checkCount=checkCount-1; 
                   if(tableData[i].founder=="1")
                       tableData[i].userType="创始人";
                   $tr = $(trTempAgent(tableData[i]));
               }
               $tr.data("data",tableData[i]);
               html.push($tr);     
           }
           if(checkCount<=0){
               $("#allSelect").css("display","none");
           }else{
               $("#allSelect").css("display","block");
           }  
         
           $table.find("tbody").empty().append(html);

           $table.find("input[type=checkbox]").change(function(){
           	var $t = $(this).parent();
           	if($t.is("th")){
                   $table.find("td input[type=checkbox]").prop("checked",$(this).prop("checked"));
               }
           	var length = $table.find('td input[type=checkbox]:checked').length;
           	
            if(!$t.is("th")){
           	if(checkCount==length){
           		$("#allSelect").prop("checked",true);
           	}
           	else{
           		$("#allSelect").prop("checked",false);
           	}
             }
            	
               if($table.find('td input[type=checkbox]:checked').length){
               	$("#editBtn").text("编辑 "+length+" 个用户");
                   $("#toolbar").addClass("show");
               }
               else{
                   $("#toolbar").removeClass("show");
               }
           });
           
       };
   })();
   
   /* 查看用户详情 */
   function viewDetails(userId,userName){
       var url="<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+userId;
       parent.openTab(url,"user",userName,true);
   }
   
   /* 批量编辑用户 */
   function editUserSubmit(){
       var trArr = [];
       $("#search-user td input[type=checkbox]:checked").each(function(){
           var $tr = $(this).closest("tr");
           trArr.push($tr.data("data").userId);
       });             
       var userLabel=$("#newLabel").val();
       var userStatus=document.getElementById("statusSelect").value;
       var entId='${entId}';
       var userInfos = "{'userLabel':'"+userLabel+"','userStatus':'"+userStatus+"','entId':'"+entId+"'}";  
       
       $.post("<%=request.getContextPath()%>/userManageMongo/updateBatch?ids="+trArr+"&userInfos="+userInfos, updateCallBack, 'json');
   }
   
   /* 批量编辑回调函数 */
   function updateCallBack(data){
       if(data.success){
           notice.success("用户更新成功！");
           //document.getElementById("newLabel").value="";
           $("#editUserModal").modal('hide');  
           $("#toolbar").removeClass("show");
           $("#allSelect").prop("checked",false);
           searchBtn();
       }else{
           notice.danger(data.msg);
       }
   }
   
   /* 取消按钮 */
   function cancel(){
       $("#search-user input[type=checkbox]").each(function(){
           $(this).attr("checked",false);
       });
       $("#search-order input[type=checkbox]").each(function(){
           $(this).attr("checked",false);
       });
       $("#toolbar").removeClass("show");
   }
      
   
   /**
    * 删除行数据
    */
    $("#deleteBtn").click(function deleteRow(){  
    	
       if($("#userRoleId").attr("class")=="active"){
           var trArr = [];
           var trArr1 = [];
           var trArr2 = [];
           var trArr3 = [];
           var agentCount = 0;
           var userCount = 0;           
           $("#search-user td input[type=checkbox]:checked").each(function(){
               var $tr = $(this).closest("tr");
               trArr.push($tr.data("data").userId);
               trArr1.push($tr.data("data").userType);
               trArr2.push($tr.data("data").userName);
           });            
           for(var i=0;i<trArr1.length;i++){
           	if(trArr1[i]=="管理员"){
           		if("${isFounder}"!="1"){
                  		notice.warning("你不是创始人，没有权限删除管理员！");                   		
               		return false;
           		}
           	}
           }
           /* 单选删除 */
           if(trArr.length == 1){
           	/* 单选删除客户的弹窗 */
           	if(trArr1[0]=="普通用户"){
           		$("#delUser").text(trArr2[0]);
           		$("#delSingleUser").css("display","block");
           		$("#delMultipleUser").css("display","none");           		
           		$("#deleteUserModal").modal('show');
           	}
           	/* 单选删除客服的弹窗 */
           	else{
           		$("#delAgent").text(trArr2[0]);
           		$("#delSingleAgent").css("display","block");
           		$("#delMultipleAgent").css("display","none");
           		$("#agentDetails").css("display","none");
           		$("#tishi1").css("display","block");
           		$("#tishi2").css("display","none");     
           		$("#deleteAgentModal").modal('show');
           	}           	
           }
           /* 多选删除 */
           if(trArr.length > 1){
           	for(var i=0;i<trArr.length;i++){
           		if(trArr1[i]!="普通用户"){
           			agentCount+=1;
           			trArr3.push(trArr2[i]);
           		}
           	}
           	/* 含有客服的弹窗和全是客户的弹窗 */
           	if(agentCount >= 1){
           		$("#delSingleAgent").css("display","none");
           		$("#delMultipleAgent").css("display","block");
           		$("#agentNum").text(trArr.length);                		
           		$("#tishi1").css("display","none");
           		$("#tishi2").css("display","block");
           		$("#agentNum1").text(agentCount);  
           		$("#agentDetails").text(trArr3);  
           		$("#agentDetails").css("display","block");           		
           		$("#deleteAgentModal").modal('show'); 
           	}else{
           		$("#delSingleUser").css("display","none");
           		$("#delMultipleUser").css("display","block");
           		$("#userNum").text(trArr.length);           		          		
           		$("#deleteUserModal").modal('show');  
           	}
           }
 	   }
 	   if($("#workRoleId").attr("class")=="active"){
           var data=getSelected();
           var idStr=[];
           for(var i=0;i<data.length;i++){
               idStr.push(data[i].workId)
           }          
           //提交删除参数
           idStr = idStr.join(",");
           if (confirm("确定要删除选中的工单！")){
        	   goDelete(idStr);
           }          
 	   }
   }); 
   
   /* 删除普通用户 */
   function deleteUserSubmit(){
   	var trArr = [];
       $("#search-user td input[type=checkbox]:checked").each(function(){
           var $tr = $(this).closest("tr");
           trArr.push($tr.data("data").userId);
       });
       
   	$.ajax({
           url : "<%=request.getContextPath()%>/userManageMongo/deleteUser?ids="+trArr,
           type : "post",
           dataType : "json",
           data : {
               entId :   "${entId}",
           },
           success : function (data) {
               if (data.success) {
                   var startTime="";
                   var endTime="";
                   $("#timeSelect li").each(function(){
		                   if($(this).attr("class")=="active"){
			                   if($(this).attr("id")=="lastWeek"){   			   	
			                       startTime=cri.formatDate(beforeWeek,"yyyy-MM-dd");
		   	                   endTime=cri.formatDate(now,"yyyy-MM-dd");
			                   }
			                   if($(this).attr("id")=="lastMonth"){
			                       startTime=cri.formatDate(beforeMonth,"yyyy-MM-dd");
			   	                   endTime=cri.formatDate(now,"yyyy-MM-dd");
			                   }
			                   if($(this).attr("id")=="lastThreeMonthes"){
			                       startTime=cri.formatDate(beforeThreeMonth,"yyyy-MM-dd");
			   	                   endTime=cri.formatDate(now,"yyyy-MM-dd");
			                   }
			                   if($(this).attr("id")=="allTime"){
			                       startTime="";
			   	                   endTime="";
			                   }
		                   }
	                  });
                      var keyword=$("#searchId").val(); 
	                  var url="<%=request.getContextPath()%>/msearch/fuzzyQueryUsers";
	                  var searchType=$("#dLabel").html();  	
	                  searchType=searchType.split("<")[0]; 
	                  //getTableData({page:1,rows:10,keyword:keyword,startTime:startTime,endTime:endTime,searchType:searchType},url);
            	      
	                  getUserTableData({page:1,rows:10,keyword:keyword,startTime:startTime,endTime:endTime,searchType:searchType},url);
	                  
                   $("#toolbar").removeClass("show");
                   $("#deleteUserModal").modal('hide');  
                   
                   $("#allSelect").prop("checked",false);
                   notice.success("用户删除成功！");
               } else {
                   notice.danger("删除失败！"+data.msg);
               }
           }
       });
   }  
   
   /* 删除客服/管理员*/
   function deleteAgentSubmit(){
   	var trArr = [];
       $("#search-user td input[type=checkbox]:checked").each(function(){
           var $tr = $(this).closest("tr");
           trArr.push($tr.data("data").userId);
       });
       
   	if($("#inputYes").val()=="yes"){
       	$.ajax({
               url : "<%=request.getContextPath()%>/userManageMongo/deleteUser?ids="+trArr,
               type : "post",
               dataType : "json",
               data : {
                   entId :   "${entId}",
               },
               success : function (data) {
                   if (data.success) {
                       var startTime="";
                       var endTime="";
                       $("#timeSelect li").each(function(){
   		                   if($(this).attr("class")=="active"){
   			                   if($(this).attr("id")=="lastWeek"){   			   	
   			                       startTime=cri.formatDate(beforeWeek,"yyyy-MM-dd");
			   	                   endTime=cri.formatDate(now,"yyyy-MM-dd");
   			                   }
   			                   if($(this).attr("id")=="lastMonth"){
   			                       startTime=cri.formatDate(beforeMonth,"yyyy-MM-dd");
   			   	                   endTime=cri.formatDate(now,"yyyy-MM-dd");
   			                   }
   			                   if($(this).attr("id")=="lastThreeMonthes"){
   			                       startTime=cri.formatDate(beforeThreeMonth,"yyyy-MM-dd");
   			   	                   endTime=cri.formatDate(now,"yyyy-MM-dd");
   			                   }
   			                   if($(this).attr("id")=="allTime"){
   			                       startTime="";
   			   	                   endTime="";
   			                   }
   		                   }
   	                  });
                      var keyword=$("#searchId").val(); 
   	                  var url="<%=request.getContextPath()%>/msearch/fuzzyQueryUsers";
   	              	  var searchType=$("#dLabel").html();  	
   	                  searchType=searchType.split("<")[0]; 
   	                  //getTableData({page:1,rows:10,keyword:keyword,startTime:startTime,endTime:endTime,searchType:searchType},url);
                       
   	                   getUserTableData({page:1,rows:10,keyword:keyword,startTime:startTime,endTime:endTime,searchType:searchType},url);
                       $("#toolbar").removeClass("show");
                       $("#deleteAgentModal").modal('hide');  
                       
                       $("#toolbar").removeClass("show");
                       $("#allSelect").prop("checked",false);
                       notice.success("用户删除成功！");
                   } else {
                       notice.danger("删除失败！"+data.msg);
                   }
               }
           });
   	  }
   }   

   
   /**
    * 刷新工单表格
    */
   var createOrderTable = (function(){
       var trTemp = Handlebars.compile($("#table-tr-template-order").html());
       var tooltipTemp = Handlebars.compile($("#tooltip-template").html());
       var $table = $("#search-order");

       return function(tableData){
           var html = [];
           for(var i= 0,len=tableData.length; i<len;i++){
               var $tr = $(trTemp(tableData[i]));
               $tr.data("row",i);
               var $a = $tr.find("a");

               if((tableData[i].status !=null) && (0<=tableData[i].status)&&(tableData[i].status<=5)){
                   tableData[i].statusStr = wfStatus[tableData[i].status];
               }else{
                   tableData[i].statusStr = "-"
               }
               
               var $tip = $(tooltipTemp(tableData[i]));
               switch(tableData[i].status)
               {
                   case "0":
                       $tip.find(".tip-order-state").addClass("orange");
                       break;
                   case "1":
                       $tip.find(".tip-order-state").addClass("red");
                       break;
                   case "2":
                       $tip.find(".tip-order-state").addClass("blue");
                       break;
                   case "3":
                       $tip.find(".tip-order-state").addClass("green");
                       break;
                   case "4":
                       $tip.find(".tip-order-state").addClass("black");
                       break;
                   default:
                       $tip.find(".tip-order-state").addClass("red");
               }

               $a.tooltipster({
                   content: $tip,
                   theme: 'tooltipster-shadow'
               });
               $a.data("data",tableData[i]);

               $a.click(function(){
                   var data = $(this).data("data");
                   console.dir(data);
                   var url = "<%=request.getContextPath()%>/order/detail?workId="+data.workId;
                   var title = "#" + data.workId + "-" + data.title;
                   parent.openTab(url,"order",title,true);
               });
               html.push($tr);
           }
           $table.find("tbody").empty().append(html);
           $table.find("input[type=checkbox]").change(function(){
               var data =  getSelected();
               //比较所选的checkbox的个数与所请求的数据的个数是否相同，如果相同则选中表格头的checkbox，若不相同则不选择表格头中的checkbox；
               if(data.length == tableData.length){
            	   $("thead input[type=checkbox]").prop("checked",true);
               }else if(data.length < tableData.length){
            	   $("thead input[type=checkbox]").prop("checked",false);
               }

               if($table.find('td input[type=checkbox]:checked').length){
                   $("#editBtn").text("编辑 "+data.length+" 张工单");
                   $("#toolbar").addClass("show");
               }
               else{
                   $("#toolbar").removeClass("show");
               }
           });
           window.getSelected=function(){
               var data=[];
               $("#search-order td input[type=checkbox]").each(function(){
                   if($(this).prop("checked")){
                       var index=parseInt($(this).closest("tr").data("row"));
                       data.push(tableData[index]);
                   }
               });
               return data;
           };
       };
   })();
   
   
   //提交按钮事件
   function sumitContent(){
       $("#editOrderModal").modal("hide");
       $("#toolbar").removeClass("show");
       $("#cancelBtn").click();
   }
   
   function goDelete(idStr){
       var param={};
       param.deleteId=idStr;
       $.ajax({
           url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/deleteWorkOrder?sessionKey="+ $.cookie("sessionKey"),
           dataType:'jsonp',
           data:param,
           success:function(data){
               if(data.success){
                   notice.alert("工单数据删除成功！","alert-success");
                   var keyword=$("#searchId").val();
                   var searchType=$("#dLabel").html();  	
                   searchType=searchType.split("<")[0]; 
                   var url="<%=request.getContextPath()%>/msearch/fuzzyQueryOrders";
                   getTableData({page:1,rows:10,keyword:keyword,startTime:"",endTime:"",searchType:searchType},url);
                   
                   $("#toolbar").removeClass("show");
                   $("#allOrderSelect").prop("checked",false);
               }else{
                   notice.alert("工单数据删除失败！","alert-danger");
               }
           }
       });
   }
  
 /* 日期时间格式化 */
   Date.prototype.Format = function (fmt) { //author: meizz 
	    var o = {
	        "M+": this.getMonth() + 1, //月份 
	        "d+": this.getDate(), //日 
	        "h+": this.getHours(), //小时 
	        "m+": this.getMinutes(), //分 
	        "s+": this.getSeconds(), //秒 
	        "q+": Math.floor((this.getMonth() + 3) / 3), //季度 
	        "S": this.getMilliseconds() //毫秒 
	    };
	    if (/(y+)/.test(fmt)) fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
	    for (var k in o)
	    if (new RegExp("(" + k + ")").test(fmt)) fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
	    return fmt;
	};
   
	
	function datepickerCheck(){
		if($("#begin_date").val()==""){
			$("#begin_date").datepicker({ dateFormat: "yy-mm-dd" }).val(beforeWeek.Format("yyyy-MM-dd"));
		}
		if($("#finish_date").val()==""){
			$("#finish_date").datepicker({ dateFormat: "yy-mm-dd" }).val(now.Format("yyyy-MM-dd"));
		}
	}
	
</script>

</body>
</html>