<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户用户组管理首页</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/dropzone/dropzone.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
	<style>
		#formId{
			width:560px;
			
		}
		#formId .form-group{
			width:45%;
			margin-right:15px;
			display:inline-block;
		}
		#userFieldUl .form-group{
			vertical-align: top;
		}
		.ulcenter{
			text-align:center;
		}
	</style>
</head>
<body>
<div id="left-part">
    <header class="part-header">
        <div class="sidebar">用户与组</div>
    </header>
    <div class="left-content">
        <c:if test="${hasGroup1}">
            <div class="left-content-panel">
                <div class="left-content-panel-header">用户 <a onClick = "newUserTab();">+新增用户</a><a data-toggle="modal" data-target="#addUserModal">+用户</a> <a data-toggle="modal" onClick="goImport();">批量导入</a></div>
                <ul class="left-content-panel-body left-part-list" id="group1Menu">
                    <c:forEach items="${permissionList}" var="item">
                        <c:if test="${item.groupId == '1'}">
                            <li id="li${item.id}" data-href="${item.url}" data-isajax="${item.isAjax}"><a>${item.name}</a></li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        <!-- 
        <div class="left-content-panel">
            <div class="left-content-panel-header">自定义用户列表<a >+添加列表</a></div>
            <ul class="left-content-panel-body left-part-list">
                <li><a>创建工单数大于10</a></li>
                <li><a>未保存的新筛选器</a></li>
            </ul>
        </div>
         -->
        <c:if test="${hasGroup3}">
            <div class="left-content-panel">
                <div class="left-content-panel-header">组
                    <a data-toggle="modal" data-target="#addGroupModal" onclick="addNewGroup()">+客服组</a></div>
                <ul class="left-content-panel-body left-part-list" id="group3Menu">
                    <c:forEach items="${permissionList}" var="item">
                        <c:if test="${item.groupId == '3'}">
                            <li id="li${item.id}" data-href="${item.url}" data-isajax="${item.isAjax}"><a>${item.name}</a></li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
        
<!--         <ul class="left-content-panel-body left-part-list">
           <li id="department"><a onclick="department()">部门管理</a></li>
        </ul> -->
               
    </div>
</div>
<div id="right-part">
    <header class="part-header">
        <span>最新用户</span>
        <div class="dropdown">
            <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true" style="border: 1px solid #eee;">
                自定义列<span class="caret"></span>
            </button>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenu1" id="dropdown1Select">
                <li><label><input type="checkbox" value="userName"   name="checkbox" checked="checked">用户名</label></li>
                <li><label><input type="checkbox" value="email"      name="checkbox" checked="checked">邮箱</label></li>
                <li><label><input type="checkbox" value="telPhone"   name="checkbox" checked="checked">电话</label></li>
                <li><label><input type="checkbox" value="createTime" name="checkbox" checked="checked">创建时间</label></li>
                <li><label><input type="checkbox" value="loginTime"  name="checkbox" checked="checked">最后登录</label></li>
                <c:forEach items="${activeFieldList}" var="item">
                    <li><label><input type="checkbox" value="${item.key}" name="checkbox" >${item.name}</label></li>
                </c:forEach>
            </ul>
        </div>
    </header>

    <div class="right-content" id="rightContainer">
        <iframe name="iframe" id="rightIframe" style="display:none" width="100%" height="100%" src="" frameborder="0" data-id="index_v1.html" seamless></iframe>
        <div class="right-content-panel container" id="rightDiv" >
            <div class="table-content">
                <div class="col-12 grid" style="overflow-x:auto;">
                    <div class="sub-switch-menu clearfix" id="status">
                        <ul>
                            <li id="li1" class="active"><a>所有</a></li>
                            <li id="li2"><a>正常</a></li>
                            <li id="li3"><a>未核审</a></li>
                            <li id="li4"><a>已停用</a></li>
                        </ul>
                    </div>

                    <div class="sub-switch-menu" id="noUserGrid">
                        <!-- 没有用户 -->
                    </div>
                    <table class="table" cellspacing="0" cellpadding="0" id="grid" style="table-layout:fixed;">
                        <thead>
                        <tr class="order" id="userTableTr">
                            <th width="40"><input class="ember-view ember-checkbox all-checkbox" type="checkbox" id="allSelect"> </th>
                            <th width="180">用户名</th>
                            <th width="150">邮箱</th>
                            <th width="150">电话</th>
                            <th width="150">创建时间</th>
                            <th width="150">最后登录</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                        <tfoot>
                            <tr id="paginationTR">
                                <td colspan="6"><div id="pagination"></div></td>
                            </tr>
                            <tr id="paginationForgTR" style="display:none">
                                <td colspan="6"><div id="paginationForg"></div></td>
                            </tr>
                        </tfoot>
                    </table>

                    <table class="table" cellspacing="0" cellpadding="0" id="gridGroup" style="display:none">
                        <thead>
                        <tr class="order">
                            <th>客服组</th>
                            <th>客服组成员</th>
                            <th></th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                        <tfoot>
                        <tr>
                            <td colspan="6"><div id="paginationGroup"></div></td>
                        </tr>
                        </tfoot>
                    </table>

                </div>
            </div>
        </div>
    </div>
    <div id="toolbar">
        <button id="deleteBtn" type="button" class="btn btn-raised left-btn btn-danger">删除</button>
        <button id="cancelBtn" type="button" class="btn btn-raised btn-default btn-dark" onclick="cancel()">取消</button>
        <button id="editBtn" type="button" class="btn btn-raised btn-primary" data-toggle="modal" data-target="#editUserModal">编辑用户</button>
    </div>
</div>

<!-- 添加用户 -->
<div id="addUserModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog" style="width:600px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">添加新用户</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid" >
                    <div class="row">
                        <div class="col-sm-12">
                            <form id="formId" class="form-horizontal">
                                <!-- 角色权限    -->                     
                                <div class="form-group div-user-right" style="margin-bottom: 27px;" id="type">
                                    <label for="inputPassword3" class="control-label">用户角色:<span style="color:red">*</span>（必选）</label>
<!--                                     <select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">
                                        <option value="1">客户</option>
                                        <option value="2">客服</option>
                                        <option value="3">管理员</option>
                                    </select> -->
                                </div>
                                <div class="form-group div-user-right">
                                    <label for="inputPassword3" id="roleLabel" class="control-label" style="float:left;display:none">角色权限:<span style="color:red">*</span>（必选）</label>
                                    <select class="form-control" id="secondType" style="display:none"></select>
                                </div>
                                <div style="display:none">
                                	<input type="text" class="form-control" name="roleId" id="roleId">
                                </div>
                               
                                 
                               <!-- 账号密码(必填) -->
                                <div class="form-group div-user-right">
                                    <label for="inputPassword3" class="control-label">登录账号：<span style="color:red">*</span>（必填）</label>
                                    <input type="text" class="form-control" name="loginName" id="loginName" placeholder="请输入用户邮箱或手机号">
                                </div>                                
                                <div class="form-group div-user-right">
                                    <label for="inputPassword3" class="control-label">密码：<span style="color:red">*</span>（必填）</label>
                                    <input type="password" class="form-control" name="loginPwd" id="passWord" placeholder="请设置用户初始密码">
                                </div>
                                
                                <!-- 其他信息(可选) -->
                                <div class="form-group div-user-right">
                                    <label for="inputPassword3" class="control-label">邮箱:</label>
                                    <input type="email" class="form-control" name="email" id="inputPassword3" placeholder="">
                                    <input type="checkbox" id="receiveEmail" > (可接收邮件提醒)
                                </div>
                                
                                <!-- 用户姓名(必填) -->
                               <div class="form-group div-user-right">
                                    <label for="inputPassword3" class="control-label">用户姓名：<span id="star" style="color:red;display:none">*</span><span id="need" style="display:none">（必填）</span></label>
                                    <input type="text" class="form-control" name="userName" id="addUserNameN" placeholder="">
                               </div> 
                                                               
                                <div class="form-group div-user-right" style="vertical-align: top;">
                                    <label for="inputPassword3" class="control-label">手机:</label>
                                    <input type="text" class="form-control" name="telPhone" id="addUserPhone" placeholder="">
                                </div>
                                <div class="form-group div-user-right">
                                    <label for="inputPassword3" class="control-label">座机:</label>
                                    <input type="text" class="form-control" name="fixedPhone" id="fixedPhone" placeholder="">
                                </div>                           
                                <div class="form-group div-user-right">
                                    <label for="inputPassword3" class="control-label">用户说明:</label>
                                    <input type="text" class="form-control" name="userDesc" id="userDesc" placeholder="">
                                </div>
                                <div class="form-group div-user-right">
                                    <label for="inputPassword3" class="control-label">详细信息:</label>
                                    <input type="text" class="form-control" name="remark" id="remark" placeholder="">
                                </div>
									<!-- 自定义字段     -->
									<div>
                                        <a id="openOrClose" onclick="openOrClose()" class="unfold">
                                            <span id="open">展开</span>
                                            <span id="close" style="display:none">收起</span>
                                        </a>
                                        <div id="userFieldUl" style="display: none;">
                                            <c:forEach items="${activeFieldList}" var="item">
                                                <div class="form-group div-user-right">
                                                    <label for="inputPassword3" class="control-label">${item.name }：</label>
                                                    <c:choose>
                                                        <c:when test="${item.componentType=='1'}">
                                                            <c:set value="${user[item.key]}" var="fieldKey"
                                                                   scope="request"></c:set>
                                                            <input class="form-control" name="${item.key }" id="${item.key }" placeholder="-" type="text"
                                                                   size="30" onchange="update(this.id,'text')"
                                                                   data-name="${item.key }" value="${fieldKey}">
                                                        </c:when>

                                                        <c:when test="${item.componentType=='2'}">
                                                            <c:set value="${user[item.key]}" var="fieldKey"
                                                                   scope="request"></c:set>
														<textarea class="form-control" name="${item.key }" id="${item.key }"
                                                                  onchange="update(this.id,'textarea')"
                                                                  data-name="${item.key }">${fieldKey}</textarea>
                                                        </c:when>

                                                        <c:when test="${item.componentType=='3'}">
                                                            <select class="form-control" name="${item.key }" id="${item.key }" data-name="${item.key }">
                                                                <option value="">-</option>
                                                                <c:forEach items="${item.candidateValue}" var="selectItems">  
                                                                    <option value="${selectItems }">${selectItems }</option>
                                                                </c:forEach>
                                                            </select>
                                                        </c:when>

                                                        <c:when test="${item.componentType=='4'}">
                                                            <div class="checkFieldDiv min-div">
                                                                <ul>
                                                                    <li id="${item.key}">
                                                                        <c:forEach items="${item.candidateValue}" var="checkItems">
                                                                            <input  name="${item.key }" value="${checkItems}" type="checkbox" >
                                                                            <span>${checkItems}</span>
                                                                        </c:forEach>
                                                                    </li>
                                                                </ul>
                                                            </div>
                                                        </c:when>

                                                        <c:when test="${item.componentType=='6'}">
                                                            <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>
                                                            <input class="form-control" name="${item.key }" id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'int')" data-name="${item.key }" data-field="${item.name }" value="${fieldKey}">
                                                        </c:when>

                                                        <c:when test="${item.componentType=='7'}">
                                                            <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>
                                                            <input class="form-control" name="${item.key }" id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'float')" data-name="${item.key }" data-field="${item.name }" value="${fieldKey}">
                                                        </c:when>

                                                        <c:when test="${item.componentType=='8'}">
                                                            <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>
                                                            <c:forEach items="${item.candidateValue}" var="reg">
                                                                <input class="form-control" name="${item.key }" id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'customized')" data-name="${item.key }" data-field="${item.name }" data-reg="${reg}" value="${fieldKey}">
                                                            </c:forEach>
                                                        </c:when>
                                                    </c:choose>
                                                </div>
                                            </c:forEach>
                                        </div>
                                    </div>
								</form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="addUserSubmit()" id="addUserSubmit">提交</button>
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
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
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
                                        <!-- <option value="2">暂停</option> -->
                                        <option value="3">未核审</option>
                                        <option value="4">停用</option>
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

<!-- 添加客户组 -->
<div id="addGroupModal" class="modal fade" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">添加客服组</span>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <div class="form-group">
                        <label for="inputEmail3" class="col-sm-3 control-label">客服组名称:</label>
                        <div class="col-sm-7">
                            <input type="text" name="groupId" id="edit_groupId" style="display:none">
                            <input type="text" class="form-control" placeholder="请输入客服组名称" name="groupName" id="edit_groupName">
                        </div>
                    </div>
                </form>
                <div class="container-fluid">
                    <div class="row">
                        <span class="col-sm-7">点击添加客服</span><span class="col-sm-5">已选择客服</span>
                    </div>
                    <div class="row select-box-content">
                        <div class="col-sm-5 select-box left">
                            <div class="search-box"><input id="user-search" name="search" placeholder="输入客服姓名或客服ID搜索" type="search"></div>
                            <ul id="unselected" class="ulcenter">
                            </ul>
                        </div>
                        <i class="icon fa fa-arrow-right"></i>
                        <div class="col-sm-5 select-box right">
                            <ul id="selected" class="ulcenter"></ul>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal" id="edit_cancel">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" id="deleteGroup" style="display:none" onclick="deleteSubmit()">删除</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="editSubmit()">提交</button>
            </div>
        </div>
    </div>
</div>

<%@include file="./userImport.jsp"%>

<script id="table-tr-template" type="text/x-handlebars-template">
    <tr id="userFieldRows">
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
                <span class="{{#equal userStatus '停用'}}status red{{/equal}}{{#equal userStatus '未核审'}}status orange{{/equal}}">{{userStatus}}</span>
                <span class="privilege">{{userType}}</span>
            </div>
        </td>
        <td data-prop="email">{{email}}</td>
               
        <td data-prop="telPhone">{{telPhone}} 
        </td>
                
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

                <span class="{{#equal userStatus '停用'}}status red{{/equal}}{{#equal userStatus '未核审'}}status orange{{/equal}}">{{userStatus}}</span>
                <span class="privilege">{{userType}}</span>
            </div>
        </td>
        <td data-prop="email">{{email}}</td>

        <td data-prop="telPhone">{{telPhone}} 

        </td> 
        
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

                <span class="{{#equal userStatus '停用'}}status red{{/equal}}{{#equal userStatus '未核审'}}status orange{{/equal}}">{{userStatus}}</span>
                <span class="privilege">{{userType}}</span>
            </div>
        </td>
        <td data-prop="email">{{email}}</td>

        <td data-prop="telPhone">{{telPhone}} 
 
        </td>  
        
 		<td data-prop="createTime">{{createTime}}</td>
        <td data-prop="loginTime">{{loginTime}}</td>
    </tr>
</script>

<script id="tableGroup-tr-template" type="text/x-handlebars-template">
    <tr>
        <td><a href="#" onclick="showAgents('{{groupId}}')">{{groupName}}</a></td>
        <td>{{count}}</td>
        <td><a onclick="edit('{{groupName}}','{{groupId}}')" data-toggle="modal" data-target="#addGroupModal" >编辑</a></td>
    </tr>
</script>

<script>
    var lid=3;
    
    $(function(){
    	/* 添加用户,根据权限动态加载用户类型 */
    	setTypeSelect();
    	
        $("#left-part .left-content-panel li").click(function(){
            lid=$(this).attr("id");
            $("#left-part .left-content-panel li").removeClass("active");
            $(this).addClass("active");
            $("#right-part .part-header>span").text($(this).find("a").text());
            if($(this).parent().attr("id")!="group3Menu"){
                $("#status").css("display","block");
                if ($(this).data("isajax") == '1') {
                    $("#rightDiv").show();
                    $("#rightIframe").hide().parent().css("overflow","auto");
                    $("#gridGroup").hide();
                    $("#paginationTR").show();
                    $("#paginationForgTR").hide();
                    $("#toolbar").removeClass("show");
                    $("#li1").click();
                }
                else {
                    $("#rightDiv").hide();
                    $("#rightIframe").show().attr('src',$(this).data("href")).parent().css("overflow","hidden");
                    $("#toolbar").removeClass("show");
                }
            }else{
                $("#toolbar").removeClass("show");
                $("#status").css("display","none");
                if($(this).data("isajax") == '1'){
                    $("#rightDiv").show();
                    $("#rightIframe").hide().parent().css("overflow","auto");
                    $("#grid").hide();
                    getTableDataGroup({page:1,rows:10,entId:'${entId}'},$(this).data("href"));
                }
                else {
                    $("#rightDiv").hide();
                    $("#rightIframe").show().attr('src',$(this).data("href")).parent().css("overflow","hidden");
                    $("#toolbar").removeClass("show");
                }
            }
            $("#allSelect").prop("checked",false);
        });
        $("#group1Menu li:first-child").click();
        initAddGridModal();
    });

    function gotoRoleUser(roleId) {
        $("#rightDiv").show();
        $("#rightIframe").hide().parent().css("overflow","auto");
        $("#gridGroup").hide();
        $("#grid").hide();
        $("#group1Menu li.active").data("roleId",roleId);
        getTableData({page:1,rows:10,entId:'${entId}',roleId:roleId}, "<%=request.getContextPath()%>/userManageMongo/queryUser/7");
    }

    /**
     * 向后台请求数据
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
                var userStatus="";
                var url="";
                $("#status li").each(function(){
                    var style=$(this).attr("class");
                    if(style=="active"){
                        userStatus=getUserStatus($(this).text());
                    }
                });

                var roleId=null;
                $("#group1Menu li").each(function(){
                    var style=$(this).attr("class");
                    if(style=="active"){
                        url=$(this).data("href");
                        if(url.indexOf("role/list")>=0){
                        	url="<%=request.getContextPath()%>/userManageMongo/queryUser/7";
                        	roleId=$(this).data("roleId");
                        }
                    }
                });
                getTableData({page:page,rows:pageSize,entId:'${entId}',roleId:roleId,userStatus:userStatus},url);
            }
        });
        return function(param,url){
            $.post(url,param,function(data){
                if(data.rows.length=="0"){
                    $("#grid").hide();
                    $("#noUserGrid").show();
                }else{
                    $("#grid").show();
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

    var getTableDataForg = (function(){
        var pager=null;
        var groupId=null;
        return function(param,url){
            $("#paginationTR").hide();
            $("#paginationForgTR").show();
            if(param.groupId!=undefined&&param.page==1&&param.groupId!=groupId){
                groupId=param.groupId;
                if(pager==null){
                    pager = new cri.Pager($("#paginationForg"),{
                        page:1,
                        pageSize:10,
                        total:0,
                        onPage:function(page,pageSize){
                            var userStatus="";
                            $("#status li").each(function(){
                                var style=$(this).attr("class");
                                if(style=="active"){
                                    userStatus=getUserStatus($(this).text());
                                }
                            });
                            getTableDataForg({page:page,rows:pageSize,entId:'${entId}',groupId:param.groupId,userStatus:userStatus},"<%=request.getContextPath()%>/groupMongo/getDetailMembers");
                        }
                    });
                }else{
                    var userStatus="";
                    $("#status li").each(function(){
                        var style=$(this).attr("class");
                        if(style=="active"){
                            userStatus=getUserStatus($(this).text());
                        }
                    });
                    pager.options.onPage=function(page,pageSize){
                        getTableDataForg({page:page,rows:pageSize,entId:'${entId}',groupId:param.groupId,userStatus:userStatus},"<%=request.getContextPath()%>/groupMongo/getDetailMembers");
                    };
                }
            }
            url = url || "";
            $.ajax({
                url:url,
                dataType:'json',
                data:param,
                success:function(data){
                    for(var i=0;i<data.rows.length;i++){
                        if(data.rows[i].loginTime==null)
                            data.rows[i].loginTime="未登录";
                    }
                    createTable(data.rows);
                    pager.update(param.page,param.rows,data.total);
                    $("#grid").show();
                }
            });
        }
    })();

    var getTableDataGroup = (function(){
        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#paginationGroup"),{
            page:1,
            pageSize:10,
            total:0,
            onPage:function(page,pageSize){
                getTableDataGroup({page:page,rows:pageSize,entId:'${entId}'},"<%=request.getContextPath()%>/groupMongo/getGroups");
            }
        });
        return function(param,url){
            url = url || "";
            $.ajax({
                url:url,
                dataType:'json',
                data:param,
                success:function(data){
                    createTableGroup(data.rows);
                    pager.update(param.page,param.rows,data.total);
                    $("#gridGroup").show();
                }
            });
        }
    })();
    /**
     * 刷新表格
     */
    var fieldNames={userName:"用户名",email:"邮箱",telPhone:"电话",createTime:"创建时间",loginTime:"最后登陆"};
    var defaultFieldNames=fieldNames;
    var createTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template").html());
        var trTempAgent = Handlebars.compile($("#table-tr-template-agent").html());
        var trTempFounder = Handlebars.compile($("#table-tr-template-founder").html());
        var $table = $("#grid");

        return function(tableData, type){
            var html = [];
            var checkCount = tableData.length;

            for(var i= 0,len=tableData.length; i<len;i++){
                if(tableData[i].userStatus=="4"){
                    tableData[i].userStatus="停用";

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
                var fns = $.extend({},fieldNames);

                $tr.find("td[data-prop]").each(function(){
                    var $this = $(this);
                    var prop = $this.data("prop");
                    if(!fns[prop]){
                        $this.hide();
                        fns[prop] = undefined;
                    }
                });
                for(var p in fns){
                    if(fns[p]&&!defaultFieldNames[p]){
                        var _t = tableData[i][p] || "";
                        $tr.append('<td data-prop="'+p+'">'+_t+'</td>');
                    }
                }
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

    var createTableGroup = (function(){
        var trTemp = Handlebars.compile($("#tableGroup-tr-template").html());
        var $table = $("#gridGroup");

        return function(tableData){
            var html = [];
            for(var i= 0,len=tableData.length; i<len;i++){
                var $tr = $(trTemp(tableData[i]));
                html.push($tr);
            }
            $table.find("tbody").empty().append(html);
            $table.find("input[type=checkbox]").change(function(){
                var $t = $(this).parent();
                if($t.is("th")){
                    $table.find("td input[type=checkbox]").prop("checked",$(this).prop("checked"));
                }
                if($table.find('td input[type=checkbox]:checked').length){
                    $("#toolbar").addClass("show");
                }
                else{
                    $("#toolbar").removeClass("show");
                }
            });
        };
    })();
    /**
     * 初始化添加客服组对话框
     */
    function initAddGridModal(){
        $("#user-search").keyup(function(){
            var value = this.value;
            if(value != "") {
                $("#unselected li").each(function () {
                    var $li = $(this);
                    if ($li.text().indexOf(value) < 0) {
                        $li.hide();
                    }
                    else{
                        $li.show();
                    }
                });
            }
            else{
                $("#unselected li").show();
            }
        });

        $("#addGroupModal .select-box-content").find("li").click(function(){
            var $other = $(this).closest(".select-box").siblings(".select-box").find("ul");
            var $li = $(this).detach();
            $other.append($li);
        });
    }
    /**
     *编辑客服组
     */
    function edit(groupName,groupId){
        $("#deleteGroup").show();
        $("#edit_groupName").val(groupName);
        $("#edit_groupId").val(groupId);
        $.post("<%=request.getContextPath()%>/groupMongo/getAgents",{"groupId":groupId},editCallBack,"json");
    }
    function editCallBack(data){
        var rows=data.rows;
        $("#addGroupModal .select-box-content ul#unselected").html("");
        $("#addGroupModal .select-box-content ul#selected").html("");
        for(var i=0;i<rows.length;i++){
            var li="<li><input style='display:none;' value='"+rows[i].userId+"' />"+rows[i].userId+"&lt;"+rows[i].userName+"&gt;</li>";
            if(rows[i].groupId==$("#edit_groupId").val()){
                $("#addGroupModal .select-box-content ul#selected").append(li);
            }else{
                $("#addGroupModal .select-box-content ul#unselected").append(li);
            }
        }

        $("#addGroupModal .select-box-content").find("li").click(function(){
            var $other = $(this).closest(".select-box").siblings(".select-box").find("ul");
            var $li = $(this).detach();
            $other.append($li);
        });
    }
    /**
     *客服组删除
     */
    function deleteSubmit(){
        var groupId=$("#edit_groupId").val();
        if(confirm("确定要删除该客服组吗？删除后无法恢复哦！"))
            $.post("<%=request.getContextPath()%>/groupMongo/deleteGroup",{"groupId":groupId},deleteSubmitCallBack,"json");
    }
    function deleteSubmitCallBack(data){
        if(data.success==true){
            $("#edit_cancel").click();
            $("#deleteGroup").hide();
            getTableDataGroup({page:1,rows:10,entId:'${entId}'},"<%=request.getContextPath()%>/groupMongo/getGroups");
            notice.success(data.msg);
        }else{
            notice.danger(data.msg);
        }
    }
    /**
     *提交编辑结果
     */
    function editSubmit(){
        var param=new Object();
        param.groupId=$("#edit_groupId").val();
        param.groupName=$("#edit_groupName").val();
        param.userIds="";
        var inputs=$("#addGroupModal .select-box-content ul#selected").find("input");

        if(inputs.length>0){
        	inputs.each(function(){
            	if(param.userIds){
            		param.userIds=param.userIds+","+$(this).val();
            	}else{
            		param.userIds=$(this).val();
            	}
            });
        }
        //param.agents=param.agents.substring(0,param.agents.length-1);
        if(param.groupId!="")
            $.post("<%=request.getContextPath()%>/groupMongo/updateGroup",param,editSubmitCallBack,"json");
        else
            $.post("<%=request.getContextPath()%>/groupMongo/addGroup",param,addSubmitCallBack,"json");
    }
    function editSubmitCallBack(data){
        if(data.success==true){
            $("#edit_cancel").click();
            $("#deleteGroup").hide();
            getTableDataGroup({page:1,rows:10,entId:'${entId}'},"<%=request.getContextPath()%>/groupMongo/getGroups");
            notice.success(data.msg);
        }else{
            notice.danger(data.msg);
        }
    }
    function addSubmitCallBack(data){
        if(data.success==true){
            $("#edit_cancel").click();
            if($("#gridGroup").is(':hidden')==false)
                getTableDataGroup({page:1,rows:10,entId:'${entId}'},"<%=request.getContextPath()%>/groupMongo/getGroups");
            notice.success(data.msg);
        }else{
            notice.danger(data.msg);
        }
    }
    /**
     *展示指定groupId下的客服列表
     */
    function showAgents(groupId){
        $("#rightDiv").show();
        $("#rightIframe").hide();
        $("#rightIframe").parent().css("overflow","auto");
        $("#gridGroup").hide();
        getTableDataForg({page:1,rows:10,entId:'${entId}',groupId:groupId},"<%=request.getContextPath()%>/groupMongo/getDetailMembers");
    }
    /**
     *添加客服组
     */
    function addNewGroup(){
        $("#addGroupModal input").each(function(){
            $(this).val("");
        });
        $("#deleteGroup").hide();
        $.post("<%=request.getContextPath()%>/groupMongo/getAgents",null,addGroupCallBack,"json");

    }
    function addGroupCallBack(data){
        var rows=data.rows;
        $("#addGroupModal .select-box-content ul#unselected").html("");
        $("#addGroupModal .select-box-content ul#selected").html("");
        for(var i=0;i<rows.length;i++){
        	 var li="<li><input style='display:none;' value='"+rows[i].userId+"' />"+rows[i].userId+"&lt;"+rows[i].userName+"&gt;</li>";
            $("#addGroupModal .select-box-content ul#unselected").append(li);
        }
        $("#addGroupModal .select-box-content").find("li").click(function(){
            var $other = $(this).closest(".select-box").siblings(".select-box").find("ul");
            var $li = $(this).detach();
            $other.append($li);
        });
    }
    /**
     * 删除行数据
     */
    $("#deleteBtn").click(function deleteRow(){
        var trArr = [];
        var trArr1 = [];
        var trArr2 = [];
        var trArr3 = [];
        var agentCount = 0;
        var userCount = 0;
        $("#grid td input[type=checkbox]:checked").each(function(){
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
    });
    /* 删除普通用户 */
    function deleteUserSubmit(){
        var trArr = [];
        $("#grid td input[type=checkbox]:checked").each(function(){
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
                    var url=$("#"+lid).data("href");
                    getTableData({page:1,rows:10,entId:'${entId}'},url);
                    $("#toolbar").removeClass("show");
                    $("#deleteUserModal").modal('hide');
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
        $("#grid td input[type=checkbox]:checked").each(function(){
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
                        var url=$("#"+lid).data("href");
                        getTableData({page:1,rows:10,entId:'${entId}'},url);
                        $("#toolbar").removeClass("show");
                        $("#deleteAgentModal").modal('hide');
                        notice.success("用户删除成功！");
                    } else {
                        notice.danger("删除失败！"+data.msg);
                    }
                }
            });
        }
    }

    function importSubmit(){
        var param=new Object();
        param.addUserFlag = '1';
        param.updateUserFlag = '1';

        $.ajaxFileUpload({
            url : "<%=request.getContextPath()%>/userImport/upload",// url
            secureuri : false,
            fileElementId : 'fileImport',// 上传控件的id
            dataType : 'json',
            data : param, // 其它请求参数
            success : function(data){
                importSubmitCallBack(data);
            },
            handleError : function(data, status, e) {
                notice.danger("导入失败");
            }
        });
    }
    function importSubmitCallBack(data){
        $("#edit_cancel").click();
        notice.success(data.msg);
    }
    
    /* 是否发邮件提醒 */
    var send="false";
    $("#receiveEmail").click(function(){
        var isChecked=$(this).prop("checked");
        if(isChecked==true ){
        	if($("#inputPassword3").val()!="")
        		send="true";
        	else{
        		notice.warning("请先填写邮箱！");
        		$(this).attr("checked",false);
        		return;
        	}
        }   
    });
       
    
    /*  添加用户 */
    function addUserSubmit(){ 
        var addType=$("#typeSelect").val();
        if($("#secondType").val()==null){
            $("#roleId").val(addType);
        }else{
        	$("#roleId").val($("#secondType").val());
        }
    	        
        /* 登录账号不能为空且不能全为空格 */
        var loginName=$("#loginName").val();
        if(loginName == ""){
            notice.warning("登录账号不能为空！");
            return false;
        }else if(loginName.trim().length==0){
            notice.warning("登录账号不能全为空格！");
            return false;
        }
        
        /* 确认账号登录方式 */       
        accountFormat(loginName);
     
        
        /* 密码必须大于等于6位且不能全为空格 */
        var password=$("#passWord").val();
        if(password.length<6){
            notice.warning("密码长度必须大于等于6位！");
            return false;
        }else if(password.trim().length==0){
            notice.warning("密码不能全为空格！");
            return false;
        }
        
        /* 邮箱格式校验 */
        var email = $("#inputPassword3").val();
        if(email!=""){
            if(!mailFormat(email)){
                return false;
            }
        } 
        
        /* 检测用户名是否重复 */        
        if($("#typeSelect").val()=="2" || $("#typeSelect").val()=="3"){
        	var uName = $("#addUserNameN").val();
            if(!checkName(uName)){
            	return false;
            }
        }
        
    	/* 自定义字段复选框表单提交  */
    	var param=$("#formId").formValue();
     	var test={};   	
     	$(".checkFieldDiv input[type=checkbox]").each(function(){
     		test[this.name] || (test[this.name]=[]);
     		if($(this).prop('checked')){
     			test[this.name].push($(this).attr('value'));
     		}
     	});     	
     	for(var name in test){
     		param[name]=test[name];
     	}   	
    	var str=JSON.stringify(param); 
    	
    	/*  添加客服和管理员，校验用户姓名  */
    	var userName = $("#addUserNameN").val();
    	if(addType!="1"){
    		if(userName==""){
                notice.warning("请填写用户姓名!");
                return false;
            }
    	}
    	    	  	
            var telPhone = $("#addUserPhone").val();              
            /* 手机格式校验（1开头11位） */
            if(telPhone!=""){
                if(!phoneFormat(telPhone)){
                    return;
                }
            }
            
            /* 校验手机号是否已存在 */
            if(telPhone!=""){
                var bool=existPhone(telPhone);
                if(bool=="false"){
                    return;
                }
                else if(bool=="true"){
                    $.ajax({
                        url : "<%=request.getContextPath()%>/userManageMongo/addUser",
                        type : "post",
                        dataType : "json",
                        data : {
                            entId :   "${entId}",                           
                            userInfos: str,                            
                            send: send,                            
                            loginType: loginType,                                                       
                        },
                        success : function (data) {
                            if (data.success) {
                                notice.success("用户创建成功！");
                                $("#addUserModal").modal('hide');
                                var userId=data.rows;
                                var userName=$("#loginName").val();
                                var url= "<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+userId;
                                parent.openTab(url,"user",userName,true);
                            } else {
                                $("#addUserModal").modal('hide');
                                notice.danger("添加失败！"+data.msg);
                            }
                        }
                    });
                }
            }else{
                $.ajax({
                    url : "<%=request.getContextPath()%>/userManageMongo/addUser",
                    type : "post",
                    dataType : "json",
                    data : {
                        entId :   "${entId}",                           
                        userInfos: str,                            
                        send: send,                            
                        loginType: loginType,                                                       
                    },
                    success : function (data) {
                        if (data.success) {
                            notice.success("用户创建成功！");
                            $("#addUserModal").modal('hide');
                            var userId=data.rows;
                            var userName=$("#loginName").val();
                            var url= "<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+userId;
                            parent.openTab(url,"user",userName,true);
                        } else {
                            $("#addUserModal").modal('hide');
                            notice.danger("添加失败！"+data.msg);
                        }
                    }
                });
            }
    }
    /**进入批量导入界面 */
    function goImport(){
        $("#rightDiv").hide();
        $("#rightIframe").show().attr('src','<%=request.getContextPath()%>/userImport/index');
    }
    /* 查看用户详情 */
    function viewDetails(userId,userName){
        var url="<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+userId + '&t=' + new Date();
        parent.openTab(url,"user",userName,true);
    }
    /* 批量编辑用户 */
    function editUserSubmit(){
        var trArr = [];
        $("#grid td input[type=checkbox]:checked").each(function(){
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
            $("#group1Menu li:first-child").click();
        }else{
            notice.danger(data.msg);
        }
    }
    /* 取消按钮 */
    function cancel(){
        $("#grid input[type=checkbox]").each(function(){
            $(this).attr("checked",false);
        });
        $("#toolbar").removeClass("show");
    }
    /* 客服类型二级下拉框 */
    function secondLevel(){
        var parentId=document.getElementById("typeSelect").value;
        if(parentId==2){	
            $("#secondType").css("display","block");
            $("#roleLabel").css("display","block");

    		$("#star").css("display","inline-block");
    		$("#need").css("display","inline-block");
    		$("#addUserNameN").attr("placeholder","管理员或客服必填");     	
    	    
    		var id = "";
    		if("${userType1}"=="2" && "${roleId}"=="5"){
    			id = "4";
    		}  		
            $.post("<%=request.getContextPath()%>/usrManage/secondLevel?parentId="+parentId+"&id="+id, secondLevelCallBack, 'json');
        }else if(parentId==3){
            $("#secondType").css("display","none");
            $("#roleLabel").css("display","none");
            
    		$("#star").css("display","inline-block");
    		$("#need").css("display","inline-block");
    		$("#addUserNameN").attr("placeholder","管理员或客服必填");
        }else{
            $("#secondType").css("display","none");
            $("#roleLabel").css("display","none");
            
    		$("#star").css("display","none");
    		$("#need").css("display","none");
    		$("#addUserNameN").attr("placeholder","");
        }
    }
    /* 客服类型二级下拉框回调函数 */
    function secondLevelCallBack(data){
        if(data.success){
            $("#secondType").empty();
            var $secondType = $("#secondType");
            for(var j=0;j<data.rows.length;j++){
                var sub = data.rows[j];
                $secondType.append('<option value="'+sub.id+'">'+sub.name+'</option>');
            }
        }else{
            notice.danger(data.msg);
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

    /*  手机校验 （1开头11位）*/
    function phoneFormat(telPhone){
        //var patrn = /^1\d{10}$/;
        var patrn = /^1\d{10}$|^[9][1]\d{10}$|^[9][0][1]\d{10}$/;
        if (patrn.test(telPhone)){
            return true;
        }else{
            notice.warning("请输入正确的手机格式！");
            return false;
        }
    }
    
    function getUserStatus(userStatusStr){
        var userStatus="";
        if(userStatusStr=="正常"){
            userStatus="1";
        }else if(userStatusStr=="已停用"){
            userStatus="4";
        }else if(userStatusStr=="未核审"){
            userStatus="3";
        }else{
            userStatus="";
        }
        return userStatus;
    }
    /*  用户按状态分类显示 */
    $("#status li").click(function(){
        $("#allSelect").prop("checked",false);
        $("#toolbar").removeClass("show");
        $("#status li").removeClass("active");

        $(this).addClass("active");
        var url="";
        var roleId=null;
        var userStatus=getUserStatus($(this).text());
        $("#group1Menu li").each(function(){
            var style=$(this).attr("class");
            if(style=="active"){
                url=$(this).data("href");
                if(url.indexOf("role/list")>=0){
                	url="<%=request.getContextPath()%>/userManageMongo/queryUser/7";
                	roleId=$(this).data("roleId");
                }
            }
        });
        getTableData({page:1,rows:10,entId:'${entId}',roleId:roleId,userStatus:userStatus},url);
        $("#grid").show();
    });

    $("#dropdown1Select li :checkbox").click(function(e){
        var fieldParam = {};
        $('#dropdown1Select :checkbox[name=checkbox]:checked').each(function(i){
            var $this = $(this);
            fieldParam[$this.val()] = $this.parent().text();
            /*
            if(0==i){
                fieldNames = $(this).val();
                fieldParam += $(this).val()+':"'+$(this).parent().text()+'"';
            }else{
                fieldNames += (","+$(this).val());
                fieldParam+=","+$(this).val()+':"'+$(this).parent().text()+'"';
            }
            */
        });
        /*
        fieldParam+='}';
        var fieldobj = eval('(' + fieldParam + ')');
        */
        setUserTable(fieldParam);
        fieldNames = fieldParam;
        e.stopPropagation();
    });

    function setUserTable(fieldNames){
        var $tr = $('#userTableTr');
        $tr.empty().append('<th width="40"><input class="ember-view ember-checkbox all-checkbox" type="checkbox" id="allSelect"> </th>');
        for(var key in fieldNames){
            $tr.append('<th width="150">'+fieldNames[key]+'</th>');
            if(key.indexOf('field')==0){}
        }
        refresh(fieldNames);
    }

    function refresh(fieldNames){
        var userStatus=getUserStatus($('#status li.active').text());
        $("#group1Menu li").each(function(){
            var style=$(this).attr("class");
            if(style=="active"){
                url=$(this).data("href");
            }
        });
        getTableData({page:1,rows:10,entId:'${entId}',userStatus:userStatus},url);
        $("#grid").show();
    }

    /* 校验手机号码是否已存在 */
    function existPhone(telPhone){
        var result="false";
        $.ajax({
            url : "<%=request.getContextPath()%>/userManageMongo/existsPhone1?telPhone="+telPhone,
            type : "post",
            dataType : "json",
            async: false,
            data : {
                userInfos : "{'telPhone':'"+telPhone+"','entId':'${entId}'}",
            },
            success : function (data) {
                if (data.success) {
                    result="true";
                } else {
                    notice.warning(data.msg);
                }
            }
        });
        return result;
    }
    
    
    /* 验证登录方式 */
    function accountFormat(loginName){
        var patrn = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
        var patrn1 = /^1\d{10}$/;
        
        if (patrn.test(loginName)){
        	/* 邮箱 */
            loginType="0";
        }else if(patrn1.test(loginName)){
        	/* 电话 */
        	loginType="1";
        }else{
            /* 用户输入 */
        	loginType="8";
        }
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
    
    
    function callout(telphone){
    	if(telphone != ""){
	    	parent.callOut(telphone);
    	}else{
    		notice.warning("号码为空!");
    	}
    }
    
    /* 添加用户,根据权限动态加载用户类型 */
    function setTypeSelect(){
    	var selectHTML = "";
    	
        if("${isFounder}"=="1"){
        	selectHTML = '<select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">'+
    		'<option value="1">客户</option><option value="2">客服</option><option value="3">管理员</option></select>';      
    	}else if("${userType1}"=="3" && "${roleId}"=="3" && "${isFounder}"!="1"){
    		selectHTML = '<select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">'+
    		'<option value="1">客户</option><option value="2">客服</option></select>';       
    	}else if("${userType1}"=="2" && "${roleId}"=="4"){
    		selectHTML = '<select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">'+
    		'<option value="1">客户</option></select>';       
    	}else if("${userType1}"=="2" && "${roleId}"=="5"){
    		selectHTML = '<select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">'+
    		'<option value="1">客户</option><option value="2">客服</option></select>'; 
    	}    	
        $("#type").append(selectHTML + '<p class="help-block">管理员和客服人员是处理工单的的用户角色，普通用户是外部提交工单的用户角色</p>');   	
    }
    
    /* 校验用户名是否已存在 */
    function checkName(name){
        var result=false;
        $.ajax({
            url : "<%=request.getContextPath()%>/userManageMongo/checkUserName?userName="+name,
            type : "post",
            dataType : "json",
            async: false,
            data : {
                userInfos : "{'userName':'"+name+"','entId':'${entId}'}",
            },
            success : function (data) {
                if (data.success) {
                    result=true;
                } else {
                    notice.warning(data.msg);
                }
            }
        });
        return result;
    }
    
</script>
<script type="text/javascript">
function newUserTab(){
	var url = "<%=request.getContextPath()%>/userManageMongo/getUserNewPage";
	var title = "新增用户"
	parent.openTab(url,null,title,null);
}
</script>
</body>
</html>