<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户详情</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <%@page import="org.apache.commons.lang.StringUtils" %>
    <%@page import="java.util.*" %>
    <%@page import="com.mongodb.DBObject" %>
    <%@page import="com.channelsoft.ems.user.po.DatEntUserPo" %>
    <%@page import="com.channelsoft.ems.communicate.constant.WorkSource" %>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/user-new.css">
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote.js" ></script>
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote-zh-CN.js" ></script>
</head>
<body>

<div id="left-part">
    <div class="left-content">
        <div class="left-content-panel">
            <div class="left-content-panel-header" style="font-weight: 700;font-size: 20px;">${user.userName}</div>
            <ul class="left-content-panel-body left-part-list" id="group1Menu">
                <li><a href="#userInfoId">基本信息</a></li>
                <li><a href="#goutong">联络历史</a></li>
                <li><a href="#workInfoId">工单</a></li>
            </ul>
        </div>
    </div>
</div>

<div id="right-part">
    <div class="right-content">
        <div class="container-fluid">
            <div class="row" id="userInfoId">
                <div class="col-lg-12 col-md-12">
                    <div class="top panel" id="top">
                        <div class="user-info fl" >
                            <a class="user-avatar" id="avatar">
                                <img src="<%=request.getContextPath()%>/${photoUrl}" alt="" title="修改头像">
                                <form><input type="file" name="user_photo" id="user_photo"></form>
                            </a>
                            <h4>
                                <input id="name" class="ember-view ember-text-field" type="text" onchange="update(this.id,'text')" data-name="userName">
                            </h4>
                            <div class="drop-btn-default fl">
                                <div id="status1" class="dropdown">
                                    <a id="dLabel" data-target="#" data-toggle="dropdown" role="button" aria-haspopup="true" aria-expanded="false">
                                        状态<span id="span1" class="status green">正常</span>
                                    </a>
                                    <ul class="dropdown-menu" aria-labelledby="dLabel">
                                        <li id="li1" ><a>状态<span id="span2" class="status red">停用</span></a></li>
                                        <li id="li2" ><a>状态<span id="span3" class="status blue">未核审</span></a></li>
                                    </ul>
                                </div>

                                <div id="status" class="dropdown" style="display:none">
                                    <a>状态<span id="spann" ></span></a>
                                </div>
                            </div>
                        </div>

                        <div class="user-else" style="width:calc(100% - 205px);">
                            <div><label>登录账号：</label><input id="loginName" type="text" size="30" onchange="update(this.id,'text')" data-name="loginName"></div>

                            <div><label>邮箱：</label>
                            <%-- <span> ${user.email} </span></div> --%>
                            <input id="email" type="text" size="30" onchange="update(this.id,'text')" data-name="email"></div>
                            
                            <div><label>手机：</label><input id="phone" placeholder="-" type="text" size="30" onchange="update(this.id,'text')" data-name="telPhone">
                                <p id="fa-telphone" class="hint" style="display:none">
                                    <a class="fa fa-phone" onclick="callout($('#phone').val())"></a>
                                </p>
                                <!-- <p class="hint">
                                    <input type="checkbox" id="bindPhone" data-name="phoneBinded"> 绑定 (可接收短信提醒)
                                </p> -->
                            </div>
                            <div><label>座机：</label><input id="fixedPhone" placeholder="-" type="text" size="30" onchange="update(this.id,'text')" data-name="fixedPhone">
                            	<p id="fa-fixedPhone" class="hint" style="display:none">
                                   <a class="fa fa-phone" onclick="callout($('#fixedPhone').val())"></a>
                               </p>
                            </div>

                            <!-- 分配客服组 -->
                            <div id="groupName" class="group-label" >
                            </div>
                            
                            <span id="nikAndSign" style="display:none">
                                <label>客服昵称：</label><input id="nickName" placeholder="-" type="text" size="30" onchange="update(this.id,'text')" data-name="nickName" >
                                 &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                                <label>客服签名：</label><input id="signature" placeholder="-" type="text" size="80" onchange="update(this.id,'text')" data-name="signature">
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
                                <label id="roleLabel">角色权限：</label>
                                <select id="secondType" onchange="updateSelect(this.id)" data-name="roleId"></select>
                            </div>
                            <div><label>用户说明：</label><input id="userDesc" placeholder="-" type="text" size="30" onchange="update(this.id,'text')" data-name="userDesc"> </div>
                            <div><label>详细信息：</label><input id="remark" placeholder="-" type="text" size="30" onchange="update(this.id,'text')" data-name="remark"></div>
                            <!-- 自定义字段     -->
                            <ul id="userFieldUl">
                                <c:forEach items="${activeFieldList}" var="item">
                                    <div>
                                        <label>${item.name }：</label>
                                        <c:choose>
                                            <c:when test="${item.componentType=='1'}">
                                                <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>
                                                <input id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'text')" data-name="${item.key }" value="${fieldKey}">
                                            </c:when>

                                            <c:when test="${item.componentType=='2'}">
                                                <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>
                                                <textarea id="${item.key }" onchange="update(this.id,'textarea')" data-name="${item.key }" >${fieldKey}</textarea>
                                            </c:when>

                                            <c:when test="${item.componentType=='3'}">
                                                　　				    <select id="${item.key }" onchange="updateSelect(this.id)" data-name="${item.key }">
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
                                                                <input type="checkbox" onclick="checkUserField('${item.key}','${checkItems}',this.checked)"><span>${checkItems}</span>
                                                            </c:forEach>
                                                        </li>
                                                    </ul>
                                                </div>
                                            </c:when>

                                            <c:when test="${item.componentType=='6'}">
                                                <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>
                                                <input id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'int')" data-name="${item.key }" data-field="${item.name }" value="${fieldKey}">
                                            </c:when>

                                            <c:when test="${item.componentType=='7'}">
                                                <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>
                                                <input id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'float')" data-name="${item.key }" data-field="${item.name }" value="${fieldKey}">
                                            </c:when>

                                            <c:when test="${item.componentType=='8'}">
                                                <c:set value="${user[item.key]}" var="fieldKey" scope="request"></c:set>
                                                <c:forEach items="${item.candidateValue}" var="reg">
                                                    <input id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'customized')" data-name="${item.key }" data-field="${item.name }" data-reg="${reg}" value="${fieldKey}">
                                                </c:forEach>
                                            </c:when>
                                        </c:choose>
                                    </div>
                                </c:forEach>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            <!-- 沟通小结 display:none; -->

            <div class="row">
                <div class="col-lg-12 col-md-12">
                    <div class="panel link-nodus">
                        <div class="panel-heading">沟通小结</div>
                        <div class="input-in panel-body">
                            <textarea id="commContent"></textarea>
                            <div class="buttom-list">
                                <!-- 禁止按钮把 btn-primary  改为  btn-default -->
                                <button type="button" onclick="submitContent()" class="btn btn-raised btn-primary btn-sm">提交</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="row" id="communicateInfoId">
                <div class="col-lg-12 col-md-12">
                    <div class="panel link-history" id="goutong">
                        <div class="panel-heading">联络历史</div>
                        <div class="history-list panel-body"></div>
                    </div>
                </div>
            </div>

            <div class="row" id="workInfoId">
                <div class="col-lg-12 col-md-12">
                    <div class="panel">
                        <!-- 用户工单列表页 -->
                        <%@include file="/views/userManage/userOrderList.jsp" %>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<div id="right-new">
    <div class="right-content">
        <div>
            <div class="right-content-panel-header" style="font-weight: 700;font-size: 20px;">常用操作</div>
            <ul class="right-part-list">
                <li>
                    <a id="createWf" href="javascript:void(0);" onclick="newwork('thiscomm');">创建工单</a>
                </li>
                <li>
                    <a id="pwdSet" data-toggle="modal" data-target="#setPwdModal">密码设置</a>
                </li>
                <li>
                    <a id="merge" data-toggle="modal" onclick="goMerge();" data-target="#userMerge">合并用户</a>
                </li>
                <li>
                    <a id="switch" onclick="switchUser()">切换为该用户角色</a>
                </li>
                <li>
                    <a id="del" class="red" onclick="deleteUser()">删除用户</a>
                </li>
            </ul>
        </div>
    </div>
</div>
<%@include file= "/views/userManage/userEditWf.jsp" %>

<!-- 分配客服组弹窗 -->
<div id="assignAgent" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">分配客服组</span>
            </div>
            <div class="modal-body" id="agentGroup">

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="assignSubmit()" id="assignSubmit">提交</button>
            </div>
        </div>
    </div>
</div>

<!-- 编辑用户角色/权限弹窗 -->
<div id="rolePermission" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close" onclick="roleCancel()">
                    <span aria-hidden="true">&times;</span>
                </button>
                <span class="modal-title">编辑用户角色/权限</span>
            </div>

            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-sm-10 col-sm-offset-1">
                            <form class="form-horizontal">
                                <div class="form-group">
                                    <label for="secondType1" class="control-label">角色权限</label>
                                    <select class="form-control" id="secondType1"></select>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>

            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal" onclick="roleCancel()">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="roleSubmit()" id="rolePermSubmit">提交</button>
            </div>
        </div>
    </div>
</div>

<!-- 设置密码弹窗 -->
<div id="setPwdModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">密码/安全设置</span>
            </div>
            <div class="modal-body">
                <form class="form-horizontal">
                    <ul>
                        <li>
                            <div>
                                <div class="form-group">
                                    <label class="control-label">新密码<span class="red">*</span></label>
                                    <input type="password" id="password"  class="form-control">
                                </div>
                                <div>
                                    <span id="password_strength" class="weak medium strong"></span>
                                    <p class="hint">当前系统密码设置强度：<span>中</span> - 密码由数字和英文字母混合组成即可</p>
                                </div>
                                <p class="hint">如不需要修改密码，请留空</p>
                            </div>
                        </li>
                    </ul>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="modifyPwd()">提交</button>
            </div>
        </div>
    </div>
</div>

<!-- 合并用户弹窗 -->
<div id="userMerge" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content" style="width: 465px;">
            <div class="modal-header">
                <button type="button" onclick="buttonClose()" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">合并用户</span>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" style="margin-left:-10px;overflow:auto;">
                    <ul class="merge-user" style="margin-left:-15px">
                        <li class="fl">
                            <img src="<%=request.getContextPath()%>/${photoUrl}">
                            <h4>${user.userName}<br/>&lt;${user.nickName}&gt;</h4>
                            <p>${user.email }</p>
                        </li>
                        <img class="link-img" src="<%=request.getContextPath()%>/static/images/lianjie.png">
                        <li class="fr">
                            <img  src="<%=request.getContextPath()%>/H+3.2/img/profile_small.jpg">
                            <h4>未选择</h4>
                            <p></p>
                        </li>
                    </ul>
                    <div class="merge-input" >
                        <input placeholder="请输入目标用户的昵称或者邮箱" id="user-search" class="form-control" type="text" style="margin-bottom:0px;" autocomplete="off">
                        <input type="text" id="userIdForMerge" style="display:none;"/>
                        <div id="forSelected" style="display:none;">
                        </div>
                    </div>
                    <div class="alert merge-alert" style="color:black;">
                        <span class="orange glyphicon glyphicon-exclamation-sign" aria-hidden="true"></span>
                        当两个账号合并之后，保留目标用户的邮箱地址，
                        <%=
                        StringUtils.isNotBlank((String)((DBObject)request.getAttribute("user")).get("userName"))?
                                ((DBObject)request.getAttribute("user")).get("userName"):
                                ((DBObject)request.getAttribute("user")).get("email")
                        %>的工单（
                        <span class="_ticket">-</span>）、文档（
                        <span class="_post">-</span>）将会移至目标用户
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" onclick="mergeCancel()" id="mergeCancel" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="userMerge()">确认合并</button>
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
                                    </li>
                                    <li>
                                        <div class="form-group">
                                            <span class="red">警告 </span>
                                        </div>
                                        <div class="form-group">
                                            <input type="text" class="form-control" id="inputYes" placeholder="确认删除请在此输入：yes">
                                            <p class="red" id="tishi1">您将要删除的用户是客服用户！</p>
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

<script src="<%=request.getContextPath()%>/H+3.2/js/plugins/footable/footable.all.min.js"></script>
<script type="text/javascript" src="<%=request.getContextPath()%>/script/lib/ajaxFileUpload/ajaxupload.3.5.js"></script>
<script>
    var groupId = [];
    var count2=0;
    var originalType="${user.userType}";
    $(document).ready(function() {  
    	//alert('${param}');
        if("${user.userType}"=="1"){
            $("#typeSelect").attr("disabled",true);
            $('#loginName').prop("readOnly",false);
        }else{
            $('#typeSelect option[value="1"]').hide();
            $('#loginName').prop("readOnly",true);
        }

        if("${userType1}"!="3"){
            if("${userId1}"!="${user.userId}"){
                if("${user.userType}"!="1"){
                    //notice.warning("没有权限");
                    $("#top input,textarea").prop("readOnly",true);
                    $("#top select").prop("disabled", true);
                    $("#top input[type=checkbox]").prop("disabled", true);

                    $("#pwdSet").css("display","none");
                    $("#merge").css("display","none");
                    $("#switch").css("display","none");
                    $("#del").css("display","none");
                    $("#status1").css("display","none");
                    $("#status").css("display","block");
                    $("#nikAndSign").css("display","inline-block");

                    $("#swichPhone").css("display","block");

                    if("${user.userStatus}"=="1"){
                        $("#spann").addClass("status green");
                        $("#spann").html("正常");
                    }else if("${user.userStatus}"=="4"){
                        $("#spann").addClass("status red");
                        $("#spann").html("停用");
                    }else if("${user.userStatus}"=="3"){
                        $("#spann").addClass("status blue");
                        $("#spann").html("未审核");
                    }
                }else{
                    $("#typeSelect").prop("disabled",true);
                    $("#switch").css("display","none");
                }
            }else{
                $("#typeSelect").prop("disabled",true);
                $("#secondType").prop("disabled",true);

                $("#merge").css("display","none");
                $("#switch").css("display","none");
                $("#del").css("display","none");
                $("#status1").css("display","none");
                $("#status").css("display","block");
                $("#nikAndSign").css("display","inline-block");

                $("#swichPhone").css("display","block");

                if("${user.userStatus}"=="1"){
                    $("#spann").addClass("status green");
                    $("#spann").html("正常");
                }else if("${user.userStatus}"=="4"){
                    $("#spann").addClass("status red");
                    $("#spann").html("停用");
                }else if("${user.userStatus}"=="3"){
                    $("#spann").addClass("status blue");
                    $("#spann").html("未审核");
                }
            }
        }else{
            if("${loginEmail}"=="${founderEmail}"){
                if("${loginEmail}"=="${user.email}"){
                    $("#del").css("display","none");
                    $("#switch").css("display","none");
                    $("#merge").css("display","none");
                    $("#nikAndSign").css("display","inline-block");

                    $("#swichPhone").css("display","block");
                }else{
                    if("${user.userType}"!="1"){
                        $("#merge").css("display","none");
                        $("#nikAndSign").css("display","inline-block");

                        $("#swichPhone").css("display","block");
                    }
                }
            }else{
                if("${userId1}"=="${user.userId}"){
                    $("#merge").css("display","none");
                    $("#switch").css("display","none");
                    $("#del").css("display","none");
                    $("#nikAndSign").css("display","inline-block");

                    $("#swichPhone").css("display","block");

                    $("#typeSelect").prop("disabled", true);
                    $("#status1").css("display","none");
                    $("#status").css("display","block");

                    if("${user.userStatus}"=="1"){
                        $("#spann").addClass("status green");
                        $("#spann").html("正常");
                    }else if("${user.userStatus}"=="4"){
                        $("#spann").addClass("status red");
                        $("#spann").html("停用");
                    }else if("${user.userStatus}"=="3"){
                        $("#spann").addClass("status blue");
                        $("#spann").html("未审核");
                    }
                }else{
                    if("${user.userType}"=="2"){
                        $("#merge").css("display","none");
                        $("#nikAndSign").css("display","inline-block");

                        $("#swichPhone").css("display","block");
                    }
                    if("${user.userType}"=="3"){
                        //notice.warning("没有权限");
                        $("#pwdSet").css("display","none");
                        $("#merge").css("display","none");
                        $("#switch").css("display","none");
                        $("#del").css("display","none");
                        $("#nikAndSign").css("display","inline-block");

                        $("#swichPhone").css("display","block");

                        $("#top input,textarea").prop("readOnly",true);
                        $("#top select").prop("disabled", true);
                        $("#top input[type=checkbox]").prop("disabled", true);

                        $("#status1").css("display","none");
                        $("#status").css("display","block");

                        if("${user.userStatus}"=="1"){
                            $("#spann").addClass("status green");
                            $("#spann").html("正常");
                        }else if("${user.userStatus}"=="4"){
                            $("#spann").addClass("status red");
                            $("#spann").html("停用");
                        }else if("${user.userStatus}"=="3"){
                            $("#spann").addClass("status blue");
                            $("#spann").html("未审核");
                        }
                    }
                }
            }
        }
        if('${loginTime}'==""){
            $("#loginTime").html("未登录");
        }

        $("#avatar").hover(function() {
                    $(this).addClass("hover");
                },
                function(){
                    $(this).removeClass("hover");
                });
        
        $("#loginName").val("${user.loginName}");
        $("#email").val("${user.email}");
        $("#phone").val("${user.telPhone}");
        $("#typeSelect").val("${user.userType}");
        $("#name").val("${user.userName}");
        $("#nickName").val("${user.nickName}");
        $("#fixedPhone").val("${user.fixedPhone}");
        $("#userLabel").val("${user.userLabel}");
        $("#remark").val("${user.remark}");
        $("#userDesc").val("${user.userDesc}");
        $("#langSelect").val("${user.lang}");
        $("#contactPhone").val("${user.contactPhone}");
        $("#signature").val("${user.signature}");

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

        var userStatus="${userStatus}";

        if($("#span2").html()==userStatus){
            $("#li1").click();
        }
        else if($("#span3").html()==userStatus){
            $("#li2").click();
        }else if($("#span1").html()==userStatus) {
            count2+=1;
        }

        /*加载绑定手机选项 */
        var check="${user.phoneBinded}";
        if(check=="1"){
            $("#bindPhone").attr("checked",true);
        }
        if(check=="0"){
            $("#bindPhone").attr("checked",false);
        }
        if("${user.userType}"!=1){
            var userId="${user.userId}";
            var loginName="${user.loginName}";
            $("#groupName").css("display","block");
            /* 查询所属客服组 */
            $.post("<%=request.getContextPath()%>/usrManage/belongGroup?userId="+userId+"&loginName="+loginName, belongGroupCallBack, 'json');
        }else{
            $("#groupName").css("display","none");
        }
        if("${user.userType}"==2){
            $("#roleSelect").css("display","inline-block");
            var parentId="${user.userType}";
            $.post("<%=request.getContextPath()%>/usrManage/secondLevel?parentId="+parentId, secondLevelCallBack, 'json');
        }
        if("${user.userType}"==3){
            $("#nikAndSign").css("display","inline-block");

            $("#swichPhone").css("display","block");
        }
        
        $("#commContent").prop("readonly",false);
        
        if($("#phone").val()){
        	$("#fa-telphone").css("display","inline-block");
        }
        if($("#fixedPhone").val()){
        	$("#fa-fixedPhone").css("display","inline-block");
        }
        
        
        
        
        //弹屏后的自动合并弹窗处理
        if('${user.userType}' == '1'&&('${merged}'!='yes')){
        	var mergeParam={};
            mergeParam.userId = '${user.userId}';
            mergeParam.telPhone = '${user.telPhone}';
            if('${user.email}'){
            	mergeParam.email = '${user.email}';
            }
            $.post("<%=request.getContextPath()%>/userManageMongo/queryOrdinaryByEP",mergeParam,queryMergeBack,'json');
            
        }
    });
    function queryMergeBack(data){
    	if(data.rows.length>0){
    		$("#merge").click();
    		ordinaryBack(data,true);
    	}
    }

    /* 所属客服组回调函数 */
    function belongGroupCallBack(data){
        var groupName=new Array();
        var spanHtml="";
        groupId.length=0;
        if(data.rows.length!=0){
            for(var i=0;i<data.rows.length;i++){
                groupName.push(data.rows[i].groupName+"&nbsp;");
                groupId.push(data.rows[i].groupId);
                spanHtml+="<span>"+data.rows[i].groupName+"</span>";
            }
        }
        if("${userType1}"=="3"){
            if("${founderEmail}"=="${loginEmail}"||("${founderEmail}"!="${loginEmail}" && $("#typeSelect").val()=="2")){
                spanHtml+='<a id="assign" data-toggle="modal" data-target="#assignAgent" onclick="groupPanel()">分配客服组</a>';
            }
        }
        $("#groupName").empty();
        $("#groupName").append(spanHtml);
    }

    /* 输入框onchange */
    function update(data,type){
        if(data=="phone"){
            var telPhone=$("#phone").val();
            /* 手机格式校验（1开头11位） */
            if(telPhone!=""){
                if(!phoneFormat(telPhone))
                    return;
            }
            if(telPhone=="" && '${user.userType}'!='1'){
                notice.warning("手机号不能为空！");
            }else{
                var userInfos = "{'telPhone':'"+telPhone+"','userId':'${user.userId}','entId':'${user.entId}'}";

                $.post("<%=request.getContextPath()%>/userManageMongo/existsPhone1?userInfos="+userInfos+"&telPhone="+telPhone, existsPhone1CallBack, 'json');
            }
        }
        if(data=="email"){
        	var email = $("#email").val();
        	var entId = "${user.entId}";
        	var userId = "${user.userId}";
        	if(email!=""){
                if(!mailFormat(email)){
                	return;
                } 
        	}   
            $.post("<%=request.getContextPath()%>/userManageMongo/existsEmail?entId="+entId+"&email="+email+"&userId="+userId, checkEmailCallBack, 'json');  
        
        }else{
            var num="true";
            if(type=="int" || type=="float" || type=="customized"){
                var name=$("#"+data).data("field");
                num=checkInput(data,name,type);
            }
            if(num=="true"){
                var key = $("#"+data).data("name");
                var value = $("#"+data).val();
                var userInfos = "{'"+key+"':'"+value+"','userId':'${user.userId}','entId':'${user.entId}'}";

                var param=new Object();
                param.userInfos=userInfos;
                $.post("<%=request.getContextPath()%>/userManageMongo/updateUser", param, updateCallBack, 'json');
            }
        }
    };

    /* 更新回调函数 */
    function updateCallBack(data){
        if(!data.success){
            notice.danger(data.msg);
        }
        if($("#phone").val()){
        	$("#fa-telphone").css("display","inline-block");
        }
        if($("#fixedPhone").val()){
        	$("#fa-fixedPhone").css("display","inline-block");
        }
    }

    /* 角色下拉框onchange */
    function upTypeSelect(){
        if($("#typeSelect").val()!="2"){
            originalType=$("#typeSelect").val();
        }
        var parentId=$("#typeSelect").val();
        var userId="${user.userId}";
        var loginName="${user.loginName}";
        var userType=$("#typeSelect").val();
        if(parentId!="1"){
            $("#groupName").css("display","block");
            $("#nikAndSign").css("display","inline-block");

            $("#swichPhone").css("display","block");
            $("#distributionWO").css("display","inline-block");
            /* 查询所属客服组 */
            $.post("<%=request.getContextPath()%>/usrManage/belongGroup?userId="+userId+"&loginName="+loginName, belongGroupCallBack, 'json');
        }else{
            $("#groupName").css("display","none");
            $("#nikAndSign").css("display","none");

            $("#swichPhone").css("display","none");
            $("#distributionWO").css("display","none");

            var agentId="${user.email}";
            /* 修改为普通用户后，从所属客服组中删除此客服 */
            $.post("<%=request.getContextPath()%>/group/deleteAgent?agentId="+agentId, deleteAgentCallBack, 'json');
        }
        if(parentId!=2){
            var roleId = userType;
            $("#roleSelect").css("display","none");
            if(parentId==3){
                $("#merge").css("display","none");
                $("#switch").css("display","none");
                $("#del").css("display","none");
                $("#pwdSet").css("display","none");
            }
            if(parentId==1){
                $("#merge").css("display","block");
                $("#switch").css("display","block");
                $("#del").css("display","block");
                $("#pwdSet").css("display","block");
                $("#createWf").css("display","block");
            }
            var userInfos = "{'userType':'"+userType+"','roleId':'"+roleId+"','userId':'${user.userId}','entId':'${user.entId}'}";
            $.post("<%=request.getContextPath()%>/userManageMongo/updateUser?userInfos="+userInfos, updateCallBack, 'json');
        }else{
            $("#roleSelect").css("display","inline-block");

            $("#merge").css("display","none");
            $("#switch").css("display","block");
            $("#del").css("display","block");
            $("#pwdSet").css("display","block");
            $("#createWf").css("display","block");
            $.post("<%=request.getContextPath()%>/usrManage/secondLevel?parentId="+parentId, secondLevel1CallBack, 'json');
        }
        if(parentId!=3 && "${loginEmail}"!="${founderEmail}"){
            $("#status1").css("display","inline-block");
            $("#status").css("display","none");

            $("#top input,textarea").prop("readOnly",false);
            $("#top select").prop("disabled", false);
            $("#top input[type=checkbox]").prop("disabled", false);
        }
        if(parentId==3 && "${loginEmail}"!="${founderEmail}"){
            $("#top input,textarea").prop("readOnly",true);
            $("#top select").prop("disabled", true);
            $("#top input[type=checkbox]").prop("disabled", true);
            $("#typeSelect").prop("disabled",false);

            $("#status1").css("display","none");
            $("#status").css("display","block");
            if("${user.userStatus}"=="1"){
                $("#spann").addClass("status green");
                $("#spann").html("正常");
            }else if("${user.userStatus}"=="4"){
                $("#spann").addClass("status red");
                $("#spann").html("停用");
            }else if("${user.userStatus}"=="3"){
                $("#spann").addClass("status blue");
                $("#spann").html("未审核");
            }
        }
        if(parentId==3 && "${loginEmail}"=="${founderEmail}"){
            $("#status1").css("display","block");
            $("#status").css("display","none");

            $("#merge").css("display","none");
            $("#switch").css("display","block");
            $("#del").css("display","block");
            $("#pwdSet").css("display","block");
            $("#createWf").css("display","block");
        }
    }

    /* 其他下拉框onchange */
    function updateSelect(data){
        var key = $("#"+data).data("name");
        var value = $("#"+data).val();
        var userInfos = "{'"+key+"':'"+value+"','userId':'${user.userId}','entId':'${user.entId}'}";

        var param=new Object();
        param.userInfos=userInfos;
        $.post("<%=request.getContextPath()%>/userManageMongo/updateUser", param, updateCallBack, 'json');
    }

    /* 二级下拉框和弹窗下拉框的回调函数 */
    function secondLevel1CallBack(data){
        if(data.success){
            $("#secondType").empty();
            $("#secondType1").empty();
            var $secondType = $("#secondType");
            for(var j=0;j<data.rows.length;j++){
                var sub = data.rows[j];
                $secondType.append('<option value="'+sub.id+'">'+sub.name+'</option>');
                if("${user.roleId}"==sub.id){
                    document.getElementById("secondType").value=sub.id;
                }
            }
            var $secondType1 = $("#secondType1");
            for(var j=0;j<data.rows.length;j++){
                var sub = data.rows[j];
                $secondType1.append('<option value="'+sub.id+'">'+sub.name+'</option>');
                if("${user.roleId}"==sub.id){
                    document.getElementById("secondType1").value=sub.id;
                }
            }
            $("#rolePermission").modal('show');
        }
    }

    /* 二级下拉框回调函数 */
    function secondLevelCallBack(data){
        if(data.success){
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

    /* 分配客服组 */
    function assignSubmit(){
        var newGroupId=[];
        var agentId="${user.email}";
        var creatorId="${creatorId}";
        var creatorName="${creatorName}";
        $("#agentGroup input[type=checkbox]:checked").each(function() {
            newGroupId.push($(this).attr("id"));
        });

        if(groupId=="" && newGroupId==""){
            $("#assignAgent").modal('hide');
        }else{
            $.post("<%=request.getContextPath()%>/usrManage/assignAgent?groupId="+groupId+"&newGroupId="+newGroupId+
                    "&agentId="+agentId+"&creatorId="+creatorId+"&creatorName="+creatorName, assignCallBack, 'json');
        }
    }

    /* 分配客服组回调函数 */
    function assignCallBack(data){
        if(data.success){
            notice.success("分配客服组成功!");
            $("#assignAgent").modal('hide');
            var userId="${user.userId}";
            var loginName="${user.loginName}";
            /* 查询所属客服组 */
            $.post("<%=request.getContextPath()%>/usrManage/belongGroup?userId="+userId+"&loginName="+loginName, belongGroupCallBack, 'json');
        }else{
            notice.danger(data.msg);
        }
    }

    /* 修改密码 */
    function modifyPwd(){
        var loginPwd=$("#password").val();
        var emailPwd=loginPwd;
        if(loginPwd!=""){
            var userInfos = "{'loginPwd':'"+loginPwd+"','emailPwd':'"+emailPwd+"','userId':'${user.userId}','entId':'${user.entId}'}";
            $.post("<%=request.getContextPath()%>/userManageMongo/setPwd?userInfos="+userInfos, modifyPwdCallBack, 'json');
        }else{
            $("#setPwdModal").modal('hide');
        }
    }

    /* 修改密码回调函数 */
    function modifyPwdCallBack(data){
        if(data.success){
            notice.success("密码安全更新成功！");
            $("#setPwdModal").modal('hide');
        }else{
            notice.warning(data.msg);
        }
    }

    /* 删除用户  提示窗*/
    function deleteUser(){
        if("${user.userType}"==1){
            $("#delUser").text("${user.userName}");
            $("#deleteUserModal").modal('show');
        }else{
            $("#delAgent").text("${user.userName}");
            $("#deleteAgentModal").modal('show');
        }
    }

    /* 删除普通用户 */
    function deleteUserSubmit(){
        var ids='${user.userId}';
        var entId='${user.entId}';
        $.post("<%=request.getContextPath()%>/userManageMongo/deleteUser?ids="+ids+"&entId="+entId, deleteCallBack, 'json');
    }

    /* 删除客服/管理员*/
    function deleteAgentSubmit(){
        var ids='${user.userId}';
        var entId='${user.entId}';
        if($("#inputYes").val()=="yes"){
            $.post("<%=request.getContextPath()%>/userManageMongo/deleteUser?ids="+ids+"&entId="+entId, deleteCallBack, 'json');
        }
    }

    /* 删除用户回调函数 */
    function deleteCallBack(data){
        if(data.success){
            notice.success("用户删除成功！");
            $("#deleteUserModal").modal('hide');
            $("#deleteAgentModal").modal('hide');
            parent.closeThisTab();
            location.href= "<%=request.getContextPath()%>/usrManage/list";
        }else{
            notice.danger(data.msg);
        }
    }

    /*  获取全部客服组 */
    function groupPanel(){
        $.post("<%=request.getContextPath()%>/group/allGroups",getGroupCallBack, 'json');
    }

    function getGroupCallBack(data){
        var agentGroupDiv = $("#agentGroup");
        $("#agentGroup").empty();
        var ul=document.createElement("ul");
        if(data.success){
            for(var i=0;i<data.rows.length;i++){
                var el='<label class="label_item"><input type="checkbox" name="'+data.rows[i].groupName+'" id="'+data.rows[i].groupId+'">'+data.rows[i].groupName+'</label>';
                for(var j=0;j<groupId.length;j++){
                    if(data.rows[i].groupId==groupId[j]){
                        el='<label class="label_item"><input type="checkbox" checked=true name="'+data.rows[i].groupName+'" id="'+data.rows[i].groupId+'">'+data.rows[i].groupName+'</label>';
                    }
                }
                agentGroupDiv.append(el);
            }
        }else if(!data.success){
            notice.danger(data.msg);
        }
    }

    /*  状态切换事件   */
    $(".dropdown-menu li").click(function(){
        var lid=$(this).attr("id");
        if(lid=="li1"){
            var text1=$("#span1").html();
            var text2=$("#span2").html();
            var text="";
            var scls1=$("#span1").attr("class");
            var scls2=$("#span2").attr("class");
            var scls="";

            text=text1;
            text1=text2;
            text2=text;
            scls=scls1;
            scls1=scls2;
            scls2=scls;

            $("#span1").html(text1);
            $("#span2").html(text2);

            $("#span1").removeClass();
            $("#span1").addClass(scls1);
            $("#span2").removeClass();
            $("#span2").addClass(scls2);

            count2+=1;
            changeStatus();
        }else if(lid=="li2"){
            var text1=$("#span1").html();
            var text3=$("#span3").html();
            var text="";
            var scls1=$("#span1").attr("class");
            var scls3=$("#span3").attr("class");
            var scls="";

            text=text1;
            text1=text3;
            text3=text;
            scls=scls1;
            scls1=scls3;
            scls3=scls;

            $("#span1").html(text1);
            $("#span3").html(text3);

            $("#span1").removeClass();
            $("#span1").addClass(scls1);
            $("#span3").removeClass();
            $("#span3").addClass(scls3);

            count2+=1;
            changeStatus();
        }
    });

    /* 更新用户状态 */
    function changeStatus(){
        var userStatus = $("#span1").html();
        if(userStatus=="正常")
            userStatus=1;
        if(userStatus=="停用")
            userStatus=4;
        if(userStatus=="未核审")
            userStatus=3;
        var userInfos = "{'userStatus':'"+userStatus+"','userId':'${user.userId}','entId':'${user.entId}'}";
        $.post("<%=request.getContextPath()%>/userManageMongo/updateUser?userInfos="+userInfos, changeStatusCallBack, 'json');
    }

    /* 更新用户状态回调函数 */
    function changeStatusCallBack(data){
        if(data.success){
            if(count2!=1){
                notice.success("状态更新成功!");
            }
        }else{
            notice.danger(data.msg);
        }
    }

    /*  绑定手机前检测手机是否为空或已绑定 */
    $("#bindPhone").click(function(){
        var telPhone=$("#phone").val();
        if(telPhone==""){
            notice.warning("绑定前请先填写手机号！");
            return false;
        }else{
            if(!phoneFormat(telPhone)){
                $("#bindPhone").attr("checked",false);
                return;
            }
        }
        var userInfos = "{'telPhone':'"+telPhone+"','userId':'${user.userId}','entId':'${user.entId}'}";

        $.post("<%=request.getContextPath()%>/userManageMongo/existsPhone?userInfos="+userInfos, existsPhoneCallBack, 'json');
    });

    /* 手机是否已绑定回调函数 */
    function existsPhoneCallBack(data){
        if(data.success){
            var phoneBinded="0";
            if($("#bindPhone").prop("checked")==true){
                phoneBinded="1";
            }
            var userInfos = "{'phoneBinded':'"+phoneBinded+"','userId':'${user.userId}','entId':'${user.entId}'}";
            $.post("<%=request.getContextPath()%>/userManageMongo/updateUser?userInfos="+userInfos, bindPhoneCallBack, 'json');
        }else{
            notice.warning(data.msg);
            $("#bindPhone").prop("checked",false);
        }
    }

    /* 号码是否已存在回调函数 */
    function existsPhone1CallBack(data){
        if(data.success){
            if($("#phone").val()==""){
                $("#bindPhone").attr("checked",false);
                var phoneBinded="0";
                var userInfos = "{'telPhone':'"+$("#phone").val()+"','phoneBinded':'"+phoneBinded+"','userId':'${user.userId}','entId':'${user.entId}'}";
            }else{
                var userInfos = "{'telPhone':'"+$("#phone").val()+"','userId':'${user.userId}','entId':'${user.entId}'}";
            }
            $.post("<%=request.getContextPath()%>/userManageMongo/updateUser?userInfos="+userInfos, updateCallBack, 'json');
        }else{
            notice.warning(data.msg);
        }
    }

    /* 绑定手机回调函数 */
    function bindPhoneCallBack(data){
        if(!data.success){
            notice.danger(data.msg);
        }
    }

    /*切换用户*/
    function switchUser(){
        var param = new Object();
        param.userId='${user.userId}';
        param.userType='${user.userType}';
        param.loginType='${loginType}';
        $.post("<%=request.getContextPath()%>/userMongo/queryUserById",param,switchBack,'json');
    }
    function switchBack(data){
        if(data.success){
            var user=data.rows;
            if(user.userStatus=="1"||user.userStatus=="3"){
                parent.switchUser(user.loginName,user.loginPwd);
            }else if(user.userStatus=="0"){
                notice.warning("账户未激活");
            }else if(user.userStatus=="4"){
                notice.warning("账户已被停用");
            }
        }else{
            notice.danger(data.msg);
        }
    }

    /* 头像上传 */
    var bg_upload = new AjaxUpload($("#user_photo"), {
        action: "<%=request.getContextPath()%>/userManageMongo/changePhoto",
        name: "image",
        onSubmit: function(file, ext){
            if (! (ext && /^(jpg|png|jpeg|gif)$/.test(ext))){
                notice.warning("仅支持JPG, PNG 或 GIF 格式文件上传");
                return false;
            }
        },
        data:{userId:"${user.userId}"},
        onComplete: function(file, response){
            if(response){
                var data = eval("("+response+")");
                if(data.success==true){

                }else{
                    notice.danger(data.msg);
                }
            }
        }
    });

    /* 编辑用户角色/权限弹窗 */
    function roleSubmit(){
        var secondRole=$("#secondType1").val();
        $("#secondType").val(secondRole);
        var userType=$("#typeSelect").val();
        var roleId=secondRole;

        var userInfos = "{'userType':'"+userType+"','roleId':'"+roleId+"','userId':'${user.userId}','entId':'${user.entId}'}";
        $.post("<%=request.getContextPath()%>/userManageMongo/updateUser?userInfos="+userInfos, updateCallBack, 'json');

        $("#rolePermission").modal('hide');
    }

    /* 取消角色权限分配 */
    function roleCancel(){
        $("#roleSelect").css("display","none");
        $("#typeSelect").val(originalType);

        if(originalType=="1"){
            $("#groupName").css("display","none");
            $("#nikAndSign").css("display","none");

            $("#swichPhone").css("display","none");

            $("#distributionWO").css("display","none");

            $("#merge").css("display","block");
            $("#switch").css("display","block");
            $("#del").css("display","block");
            $("#pwdSet").css("display","block");
            $("#createWf").css("display","block");
        }
        if(originalType=="3" && "${loginEmail}"!="${founderEmail}"){
            $("#assign").css("display","none");

            $("#merge").css("display","none");
            $("#switch").css("display","none");
            $("#del").css("display","none");
            $("#pwdSet").css("display","none");
            $("#createWf").css("display","block");

            $("#status1").css("display","none");
            $("#status").css("display","block");

            if("${user.userStatus}"=="1"){
                $("#spann").addClass("status green");
                $("#spann").html("正常");
            }else if("${user.userStatus}"=="4"){
                $("#spann").addClass("status red");
                $("#spann").html("停用");
            }else if("${user.userStatus}"=="3"){
                $("#spann").addClass("status blue");
                $("#spann").html("未审核");
            }
            $("#top input").prop("readOnly",true);
            $("#langSelect").prop("disabled", true);
            $("#bindPhone").prop("disabled", true);
        }
    }

    /* 创建工单 */
    function newwork(commparam) {
    	if(commparam=="thiscomm"){
    		commparam ='${commparam}';
    	}
    	commparam=encodeURIComponent(commparam);
        parent.newwork("","","",commparam);

    }
    /*用户合并*/
    function goMerge(){
        $(".merge-user .fr h4").html("未选择");
        $(".merge-user .fr p").html("");
        $(".merge-user .fr img").attr("src","<%=request.getContextPath()%>/H+3.2/img/profile_small.jpg");
        $(".merge-input div#forSelected").html("");
        $(".merge-input div#forSelected").hide();
        $(".merge-input input").val("");
    }
    function mergeCancel(){
        preValue="";
    }
    function buttonClose(){
        preValue="";
    }
    function userMerge(){
        if(!$(".merge-user .fr p").text()){
            notice.warning("尚未选择要合并的用户");
            return;
        }
        var param=new Object();
        param.userMergeId='${user.userId}';
        param.userTargetId=$(".merge-input input#userIdForMerge").val();
        $.post("<%=request.getContextPath()%>/userManageMongo/mergeUser",param,mergeCallBack,'json');
    }
    function mergeCallBack(data){
        if(data.success){
            notice.success(data.msg);
            preValue="";
            $("#mergeCancel").click();
            var userId = $(".merge-input input#userIdForMerge").val();
            location.href="<%=request.getContextPath()%>/communicate/goComm?merged=yes&userId="+userId+"&sessionId=${sessionId}&source=${source}";
            //parent.closeThisTab();
        }else{
            notice.danger(data.msg);
        }
    }
    function makeBackColor(){
        $(".merge-input div#forSelected div").each(function(){
            $(this).mouseover(function(){
                $(this).css("background-color","gray");
            });
            $(this).mouseout(function(){
                $(this).css("background-color","");
            });
        });
    }
    function makeClick(){
        $(".merge-input div#forSelected div").each(function(){
            $(this).click(function(){
                var text=$(this).text();
                var pre=text.indexOf("<");
                while(pre>=0){
                    if(text.substring(pre+1).indexOf("<")>=0){
                        pre=pre+1+text.substring(pre+1).indexOf("<");
                    }else{
                        break;
                    }
                }
                var loginName=text.substring(pre+1).split(">")[0];
                var userName=text.substring(0,pre);
                var imgUrl=$(this).find("span").eq(1).text();
                var userId=$(this).find("span").eq(2).text();
                imgUrl="<%=request.getContextPath()%>/"+imgUrl;
                $(".merge-input div#forSelected").hide();
                $(".merge-input input").val(loginName);
                $(".merge-input input#userIdForMerge").val(userId);
                $(".merge-user .fr h4").html(userName);
                $(".merge-user .fr p").html("&lt;"+loginName+"&gt;");
                $(".merge-user .fr img").attr("src",imgUrl);
            });
        });
    }
    var preValue="";
    $("#user-search").on("input",function(){
        var value = this.value.split(" ").join("");
        if(value != "") {
            if(value.length>2){
                var start=value.substring(0,3);
                if(preValue!=start){
                    preValue=start;
                    $(".merge-input div#forSelected").html("");
                    $(".merge-input div#forSelected").hide();
                    var param={"value":start,"loginName":'${user.loginName}',"all":value};
                    $.post("<%=request.getContextPath()%>/userManageMongo/queryOrdinary",param,ordinaryBack,'json');
                }else{
                    selectUser(value);
                }
            }else{
                $(".merge-input div#forSelected").hide();
            }
        }else{
            $(".merge-input div#forSelected").hide();
        }
    });
    function ordinaryBack(data,auto){
        if(data.success){
            var users=data.rows;
            if(users.length>0){
            	if(auto==true){
            		$("#user-search").prop("disabled","disabled");
            	}
                $(".merge-input div#forSelected").html("");
                for(var i=0;i<users.length;i++){
                	var userId=users[i].userId,
                    loginName=users[i].loginName,photoUrl=users[i].photoUrl,userName=users[i].userName;
            		addDivForSelected(userName,userId,loginName,photoUrl);
                }
                makeBackColor();
                makeClick();
                if(data.msg.length>3&&!auto){
                    selectUser(data.msg);
                }else{
                    $(".merge-input div#forSelected").show();
                }
            }
        }else{
            if(data.msg){
                notice.danger(data.msg);
            }else{
                notice.danger("返回数据有错");
            }
        }
    }
    function addDivForSelected(userName,userId,loginName,photoUrl){
        var html="<div><span>"+userName+"&lt;"+loginName+"&gt;"+"</span><span style='display:none;'>"+photoUrl+
                "</span><span style='display:none;'>"+userId+"</span></div>";
        $(".merge-input div#forSelected").append(html);
    }
    function selectUser(value){
        var exist=false;
        $(".merge-input div#forSelected div").each(function(){
            var text=$(this).find("span").eq(0).html();
            if(text.indexOf(value)>=0){
                exist=true;
            }
        });
        if(!exist){
            $(".merge-input div#forSelected").hide();
        }
        else{
            $(".merge-input div#forSelected div").each(function(){
                var text=$(this).text();
                if(text.indexOf(value)>=0){
                    $(this).show();
                }else{
                    $(this).hide();
                }
            });
            $(".merge-input div#forSelected").show();
        }
    }
    function showAllUser(){
        if(!$(".merge-input div#forSelected div").length){
            return;
        }
        $(".merge-input div#forSelected").show();
        $(".merge-input div#forSelected div").each(function(){
            $(this).show();
        });
    }

    /*  自定义字段复选框选项 */
    function checkUserField(k,v,c){
        var key = k;
        var value=v;
        /* var userInfos = "{'"+key+"':'"+value+"','userId':'${user.userId}','entId':'${user.entId}'}";   */
        var entId='${user.entId}';
        var userId='${user.userId}';
        var field=k;
        var checked=c;

        $.post("<%=request.getContextPath()%>/userManageMongo/updateCheckBox?key="+key+"&value="+value+"&entId="+entId+"&userId="+userId+"&field="+field+"&checked="+checked, updateCallBack, 'json');
    }

    /* 自定义字段类型校验 */
    function checkInput(data,name,type){
        var nubmer = $("#"+data).val();
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

        if (!re.test(nubmer) && !re1.test(nubmer) && nubmer!=""){
            notice.warning(name + desc);
            return "false";
        }else{
            return "true";
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

    function deleteAgentCallBack(data){
        if(!data.success){
            notice.danger(data.msg);
        }
    }

</script>

<script id="history-list-template" type="text/x-handlebars-template">
    <div class="form-group listContainer">
		<form id="messageNone" style="display:none;">
			<input name="sessionId" value="{{sessionId}}" />
			<input name="ccodEntId" value="{{ccodEntId}}"/>
			<input name="ccodAgentId" value="{{ccodAgentId}}"/>
		</form>
        <div class="list-bar">
            <div class="fa"></div>
            <div class="list-time" style="background:#fff;padding-right:10px;">{{createTime}} </div>
            <%--{{userName}}--%>
            <div class="list-line" style="width: calc(100% - 80px);position: absolute;top: 10px;display:none;"></div>
            <div class="neirong-list" style="width: calc(100% - 400px);"><span>{{content}}</span></div>
            <div class="append">
                <a>补录工单</a>
            </div>
            <div class="icon">
                <a class="fa fa-chevron-circle-down"></a>
            </div>

        </div>
        <div class="text-list" style="display:none;">
            <div class="textarea-list"><textarea>{{content}}</textarea></div>
            <%--消息--%>
            <div class="new-message" style="display:none;">
                <div class="chatting">
                    <div class="chakan-long" style="margin-right:10px;"><a>< 查看更多 ></a></div>
                    <div class="chatting-text">
                        暂无消息
                    </div>
                </div>
            </div>

            <%--电话--%>
            <div class="phone-message" style="display:none;">
                <ul>
                    <li>
                        <div>
                            <label>呼叫类型:</label>
                            <span>{{callType}}</span>
                        </div>
                        <div>
							{{#if sessionId}}
                            	<label>呼叫时间:</label>
							{{else}}
								<label>补录时间:</label>
							{{/if}}
							<span>{{createTime}}</span>
                        </div>
                        <div>
                            <label>是否接听:</label>
                            <span>{{isConnected}}</span>
                        </div>
                    </li>
                    <li>
                        <div>
                            <label>主叫:</label>
                            <span>{{strAni}}</span>
                        </div>
                        <div>
                            <label>接机时间:</label>
                            <span>{{startTime}}</span>
                        </div>
                        <div>
                            <label>处理坐席:</label>
                            <span>{{opName}}</span>
                        </div>
                    </li>
                    <li>
                        <div>
                            <label>被叫:</label>
                            <span>{{strDnis}}</span>
                        </div>
                        <div>
                            <label>通话时长:</label>
                            <span>{{commTime}}s</span>
                        </div>
                        <div>
                            <label>呼叫技能组:</label>
                            <span></span>
                        </div>
                    </li>
                </ul>
				{{#if sessionId}}
                <div>
                    <label>通话录音:</label>
                    <span class="audio-phone"><audio src="" controls="controls" id="recordUrl"></audio><a id="recordDown">下载录音</a></span>
                </div>
				{{/if}}
            </div>
        </div>

    </div>
</script>
<script type="text/javascript">
//根据用户ID查询联络历史
function getComms(){
    $.post("<%=request.getContextPath()%>/communicate/getCommsByUserId",{userId:"${user.userId}"},function(data){
        var html = [];
        var lsTemp = Handlebars.compile($("#history-list-template").html());
        if(!(data.success)){
            notice.alert(data.msg);
            return;
        }
        var rows=data.rows?data.rows:0;
        if(rows==0||rows.length<=0){
            $(".history-list").empty().append("<h5>还没有联络历史</h5>");
            return;
        }
        if(rows.length>0){
            for(var i=0;i<rows.length;i++){
                var li=$(lsTemp(rows[i]));
                if(!rows[i].content){
                	$(li.find("div.neirong-list")[0]).find("span").empty().append("此次联络暂无沟通小结");
                }
                switch(rows[i].source){
                    case "1":
                        $($(li.find("div.list-bar")[0]).find("div")[0]).addClass("fa-im");
                        var msgDiv=$(li.find("div.new-message")[0])
                        msgDiv.css("display","block");
                        initIMMessage(msgDiv,rows[i].message);
                        break;
                    case "3":
                        $($(li.find("div.list-bar")[0]).find("div")[0]).addClass("fa-envelope-o");
                        break;
                    case "5":case "6":
                    	$($(li.find("div.list-bar")[0]).find("div")[0]).addClass("fa-phone");
                    	li.find("div.phone-message").show();
                    	break;
                    case "7":
                        $($(li.find("div.list-bar")[0]).find("div")[0]).addClass("fa-weixin");
                        li.find("div.new-message").show();
                        break;
                    case "8":
                        $($(li.find("div.list-bar")[0]).find("div")[0]).addClass("fa-video-camera");
                        break;
                    default:
                        $($(li.find("div.list-bar")[0]).find("div")[0]).addClass("fa-clipboard");
                }
              	//上（下）拉图标点击事件初始化
              	li.find("div.icon").click(updownToggle);
                //li.find("div.icon").click(function(){});
                if(rows[i].sessionId=='${sessionId}'){
                	li.find("div.append").find("a").text("创建工单");
                }
              	//补录工单按钮点击事件初始化
                li.find("div.append").click((function(){
                	var param=rows[i].param;
                	return function(){
                		newwork(param);
                	}
                })());
              	//（下拉框）沟通小结框里的值改变事件初始化
                li.find("textarea").on("change",(function(){
                	var commId=rows[i].commId;
                	var thePnt=this;
                	return function(){
                		submitLocalContent(commId,$(thePnt).val());
                	};
                }).call(li.find("textarea")));
                html.push(li);
            }
        }
        $(".history-list").empty().append(html);
    },'json');
}

//上（下）拉图标点击事件处理函数
function updownToggle(){
	var $this = $(this);
	var $c = $this.closest('.listContainer');
    var tog=$c.find("div.text-list")[0];
    var neirong=$c.find("div.neirong-list")[0];
    
    var jqA=$($(this).find("a")[0]);
    if(jqA.hasClass("fa-chevron-circle-down")){
    	//内容展开处理
        jqA.removeClass("fa-chevron-circle-down");
        jqA.addClass("fa-chevron-circle-up");
        $(neirong).find("span").css("display","none");
        if($c.find("div.phone-message").css("display")=="block"){
        	if($c.find("audio#recordUrl").length>0){
        		//初始化录音播放控件和下载录音链接
        		initAudioAndDownload($c);
        	}
        }
    }else{
    	//内容上拉处理
        jqA.removeClass("fa-chevron-circle-up");
        jqA.addClass("fa-chevron-circle-down");
        $(neirong).find("span").css("display","inline");
    }
    
    $(tog).toggle();
}
//初始化录音播放控件和下载录音链接
function initAudioAndDownload($c){
	var $audio = $c.find("audio#recordUrl");
	$audio.css("display","");
	$audio.siblings("#recordDown").css("display","");
	if($audio.attr("src") == ""){
    	var form = $c.find("form").formValue();
    	if(form.sessionId&&form.ccodEntId&&form.ccodAgentId){
    		if($audio.siblings("font").length>0){
					$audio.siblings("font").remove();
			}
    		
    		//获取通话录音地址并赋值给audio标签
    		$.post("<%=request.getContextPath()%>/communicate/getRecordUrl",form,function(data){
   				if(data.success){
   					if(data.rows){
   						//判断是否为mp3格式
   						if(data.rows.indexOf(".mp3")>=0){
   							$audio.on("error",function(){
   								$audio.css("display","none");
   		       					if($audio.siblings("font").length<=0){
   		       						$audio.after("<font color='red'>录音资源已损坏</font>");
   		       					}
   		       					$audio.siblings("#recordDown").css("display","none");
   		       				});
   							$audio.attr("src",data.rows);
   							
   						}else{
   							$audio.css("display","none");
   							$audio.after("<font color='red'>录音格式不支持,格式需为mp3</font>");
   						}
   						
   						var dowloadUrl="<%=request.getContextPath()%>/communicate/dowonloadRecord?httpUrl="+encodeURI(data.rows);
	       				$c.find("a#recordDown").prop("href",dowloadUrl);
   					}else{
   						$audio.css("display","none");
   						if($audio.siblings("font").length<=0){
       						$audio.after("<font color='red'>录音资源不存在</font>");
	       				}
       					$audio.siblings("#recordDown").css("display","none");
   					}
       			}else{
       				notice.warning(data.msg);
       				$audio.after("<font color='red'>录音资源不存在</font>");
       				$audio.siblings("#recordDown").css("display","none");
       			}
    		},'json');
    	}
    }
}

getComms();

function callout(telphone){
	if(telphone != ""){
 	parent.callOut(telphone);
	}else{
		notice.warning("号码为空!");
	}
}
</script>
<script type="text/javascript">
    function submitContent(){
        var param={};
        param.sessionId="${sessionId}";
        param.content=$("#commContent").val();
        $.post("<%=request.getContextPath()%>/communicate/saveContent",param,saveBack,'json');
    }
    function submitLocalContent(commId,content){
    	 var param={};
         param.commId=commId;
         param.content=content;
        $.post("<%=request.getContextPath()%>/communicate/saveContent",param,saveBack,'json');
    }
    function saveBack(data){
        if(data.success){
        	$("#commContent").closest(".row").css("display","none");
            notice.success(data.msg);
            getComms();
        }else{
            notice.alert(data.msg);
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
    
    /* 检查邮箱回调函数 */
    function checkEmailCallBack(data){
    	if(data.success){
            var userInfos = "{'email':'"+$("#email").val()+"','userId':'${user.userId}','entId':'${user.entId}'}";
            $.post("<%=request.getContextPath()%>/userManageMongo/updateUser?userInfos="+userInfos, updateCallBack, 'json');
    	}else{
        	notice.danger(data.msg);
        }
    }
    
    /*展示IM沟通消息*/
    function initIMMessage(msgDiv,msgList){
    	//alert(msgList);
    	if(!msgList){
    		return;
    	}
    	var msgContent=$(msgDiv.find("div.chatting-text")[0]);
    	var jsonList=eval("("+msgList+")");
    	msgDiv.find("div.chatting").css("padding-left","0px");
    	msgDiv.find("div.chatting").css("padding-right","0px");
    	var isLeft=true;
    	msgContent.empty();
    	for(var id in jsonList){
    		msgContent.css("padding-left","10px");
    		msgContent.css("padding-right","10px");
    		msgContent.css("overflow","auto");
    		msgContent.css("max-height","100px")
    		if(isLeft){
    			msgContent.append("<div style='float:left;width:100%;'><div style='padding:5px;border-radius:6px;background:#00faaa;float:left;'><span style='float:left;'>"+jsonList[id].text+"</span></div></div>");
    			isLeft=false;
    		}else{
    			msgContent.append("<div style='float:left;width:100%;'><div style='padding:5px;border-radius:6px;background:yellow;float:right;'><span style='float:right;'>"+jsonList[id].text+"</span></div></div>");
    			isLeft=true;
    		}
    	}
    }
</script>

</body>

</html>