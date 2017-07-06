<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新增用户</title>

<%@include file="/views/include/pageHeader.jsp"%>
<%@page import="org.apache.commons.lang.StringUtils" %>
<%@page import="java.util.*" %>
<%@page import="com.mongodb.DBObject" %>
<%@page import="com.channelsoft.ems.user.po.DatEntUserPo" %>
<%@page import="com.channelsoft.ems.communicate.constant.WorkSource" %>

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/user-new-ylb.css">

<style type="text/css">
#setChargeModal .modal-content{
	width: 900px;
	left: -150px;
}
#setChargeModal .modal-footer{
	text-align: center;
}
#setChargeModal #userFieldRows td{
	//overflow: auto;
}
</style>

</head>
<body>
<div id="left-part" style="height: 100%;width: 300px;padding-left: 1%;">
    <div class="left-content-ylb">
        <div class="left-content-panel">
            <div class="left-content-panel-header-ylb">
            	<span><b>客户信息</b></span>
            	<span class="forClick" onClick = "saveUser();"><a>保存</a></span>
            	<span class="forClick" onClick = "parent.closeThisTab();"><a>取消</a></span>
            </div>
            <hr style = "border-bottom: 1px dashed;border-top:0;"/>
            <form action="" id ="userInformation" style = "height:100%;">
            	<div id = "information">
	            	<div>
		                <label>
		                	<span style="float:left;" >姓名</span>
		            		<span style="color:red;font-size: 22px;float:left;">*</span>
		            		：
		                </label>
		                <input id="userName" name="userName"  type="text" />
		            </div>
		            <div>
		                <label>负责人：</label>
		                <input id="chargeman" name="chargeman" onfocus="getDataTable(this);"  type="text" data-toggle="modal" data-target="#setChargeModal" />
		                <span onclick = "$('#chargeman').val('');" style = "display:inline-block;font-size:20px;cursor:pointer;">&times;</span>
		            </div>
		            <div>
		                <label>客户组织：</label>
		                <input id="organization" name="organization"  type="text" />
		            </div>
		            <div>
		                <label>归属地：</label>
		                <input id="address" name="address"  type="text" />
		            </div>
		            <div>
		            	<label>
		            		<span style="float:left;" >客户分类</span>
		            		<span style="color:red;font-size: 22px;float:left;">*</span>
		            		：
		            	</label>
		                <select id = "level" name = "level">
		                	<option value = "2">高</option>
		                	<option value = "1">中</option>
		                	<option value = "0">低</option>
		                </select>
		                <!-- <input id="userName" name="userName"  type="text" /> -->
		            </div>
		            <div>
		                <label>电话：</label>
		                <div style = "width:90%;">
		                	<input id="telPhone" name="telPhone"  type="text"  style = "width:85%;" />
		                	<div style = "font-size:26px;float:right;" class="fa fa-phone"></div>
		                </div>
		            </div>
		            <div>
		                <label>微信：</label>
		                <div style = "width:90%;">
		                	<input id="weixin" name="weixin"  type="text"  style = "width:85%;" />
		                	<div style = "font-size:26px;float:right;" class="fa fa-wechat"></div>
		                </div>
		                <!-- <input id="weixin" name="weixin"  type="text" />
		                <div style = "font-size:26px;float:right;" class="fa fa-wechat"></div> -->
		            </div>
		            <div>
		                <label>邮箱：</label>
		                 <div style = "width:90%;">
		                	<input id="email" name="email"  type="text"  style = "width:85%;" />
		                	<div style = "font-size:26px;float:right;" class="fa fa-envelope"></div>
		                </div>
		                <!-- <input id="email" name="email"  type="text" /> -->
		            </div>
		            <div>
		                <label>微博：</label>
		                <input id="weibo" name="weibo"  type="text" />
		            </div>
		            <div>
		                <label>标签：</label>
		                <input id="userLabel" name="userLabel"  type="text" />
		            </div>
		            <div>
		                <label>常用IP：</label>
		                <input id="ip" name="ip"  type="text" />
		            </div>
		            <div>
		            	<div>
		            		<label>
			            		<span style="float:left;" >登录账号</span>：
		            		</label>
		            		
		            	</div>
		                <input id="loginName" name="loginName"   type="text" autocomplete="off" />
		            </div>
	            </div>
            </form>
            
        </div>
    </div>
</div>
<div id="right-part" style="width: -webkit-calc(100% - 300px);background: white;">
    <div class="right-content" style = "padding:5px 0 0 0;">
        <div class="container-fluid">
        	<div style = "float:right;margin-bottom: 10px;">
	        	<input type = "button" onClick = "newwork();" value = "创建工单" />
	        	<input type = "button" value = "创建联络历史" />
        		<input type = "button" data-toggle="modal" onclick="goMerge();" data-target="#userMerge" value = "合并用户" />
        		<input id="pwdSet" style = "opacity: 0.5;" data-toggle="modal" data-target="#setPwdModal" type = "button" value = "密码设置" disabled="disabled" />
        		<input id="del" class="red" onclick="deleteUser()" type = "button" value = "删除用户" />
        	</div>
        
            <div class="row" id="workAndCommunication">
                <div class="col-lg-12 col-md-12">
                    <div class="panel" style = "box-shadow:0 0 0 0;">
                    	<div class="bottom" id="bottom"  style = "background: white;border:0;">
						    <ul class="tab-title1">
						        <li class="active" id="workOrders">
						            <a id="workOrder">工单(0) </a>
						        </li>
						        <li id="communications">
						            <a id="communication">联络历史(0) </a>
						        </li>
						    </ul>
						</div>
						<%@include file = "/views/userManage/userOrderListYlb.jsp" %>
						<%@include file = "/views/userManage/commListYlb.jsp" %>
                    </div>
                </div>
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
                            <h4>${user.userName}</h4>
                            <p>&lt;${user.loginName }&gt;</p>
                        </li>
                        <img class="link-img" src="<%=request.getContextPath()%>/static/images/lianjie.png">
                        <li class="fr">
                            <img  src="<%=request.getContextPath()%>/H+3.2/img/profile_small.jpg">
                            <h4>未选择</h4>
                            <p></p>
                        </li>
                    </ul>
                    <div class="merge-input" >
                        <input placeholder="请输入目标用户的姓名或者登录名" id="user-search" class="form-control" type="text" style="margin-bottom:0px;" autocomplete="off">
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

<!-- 负责人选择弹窗 -->
<div id="setChargeModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">选择客户负责人</span>
            </div>
            <div class="modal-body">
            	<div>
            		<span>部门：</span>
            		<select>
            			<option>全部</option>
            			<option>应用开发部</option>
            			<option>财政部</option>
            		</select>
            		<input type = "text" placeholder = "请输入用户名" />
            		<input type = "button" value = "搜索"/>
            	</div>
            	<div class="sub-switch-menu" id="noUserGrid">
                    <!-- 没有用户 -->
                </div>
                <table class="table" cellspacing="0" cellpadding="0" id="grid" style="table-layout:fixed;">
                    <thead>
                    <tr class="order" id="userTableTr">
                    	<th width="20"></th>
                        <th width="50">姓名</th>
                        <th width="50">部门</th>
                        <th width="50">岗位</th>
                        <th width="50">技能组</th>
                        <th width="60">电话</th>
                        <th width="60">邮箱</th>
                        <th width="80">创建时间</th>
                    </tr>
                    </thead>
                    <tbody></tbody>
                    <tfoot>
                        <tr id="paginationTR">
                            <td colspan="8"><div id="paginationCharge"></div></td>
                        </tr>
                    </tfoot>
                </table>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="chooseMan()">确定</button>
            </div>
        </div>
    </div>
</div>

<script id="table-tr-template-charge" type="text/x-handlebars-template">
    <tr id="userFieldRows">
        <td><input type="radio" name = "choosed" onchange = "rdChange($(this).closest('#userFieldRows'));"></td>
        <td style="display:none">{{userId}}</td>
        <td style="display:none">{{userLabel}}</td>
        <td data-prop="userName" class="user">
            <span>{{userName}}</span>
        </td>
		<td>部门</td>
		<td>岗位</td>
		<td>技能组</td>
		<td data-prop="telPhone">{{telPhone}}</td>
        <td data-prop="email">{{email}}</td>
        <td data-prop="createTime">{{createTime}}</td>
    </tr>
</script>

<script type="text/javascript">
var userJson;
$(document).ready(function(){
	userJson = JSON.parse('${user}')
	$("#userInformation").formValue(userJson);
	
	changePasswdSet();
	
	$("#workAndCommunication .tab-title1 li").click(function(){
		$(this).siblings().removeClass("active");
		$(this).addClass("active");
		if($(this).attr("id") == "workOrders"){
			$("#divForWork").show();
			$("#divForHistory").hide();
		}else{
			$("#divForWork").hide();
			$("#divForHistory").show();
		}
	});
	
});
function changePasswdSet(){
	if($("#loginName").val()){
		$("#pwdSet").removeAttr("disabled");
		$("#pwdSet").css("opacity",1);
	}else{
		$("#pwdSet").attr("disabled","disabled");
		$("#pwdSet").css("opacity",0.5);
	}
}
function saveUser(){
	var param = {};
	var userInfo = $("#userInformation").formValue();
	var loginName = userInfo.loginName;
	param.entId = '${entId}';
	if(!userInfo.userName){
		notice.warning("用户名不能为空");
		return;
	}
	for(var prop in userInfo){
		if(!userInfo[prop] && !userJson[prop]){
			delete userInfo[prop];
		}
	}
	for(var prop in userInfo){
		if(userInfo[prop] == userJson[prop]){
			delete userInfo[prop];
		}
	}
	if(Object.getOwnPropertyNames(userInfo).length == 0){
		notice.success("没有改变");
		return ;
	}
	if(userInfo.chargeman){
		userInfo.chargeId = $("#chargeman").data("chargeId");
	}
	if(userInfo.chargeman == ""){
		userInfo.chargeId = "";
	}
	userInfo.userId = '${user.userId}';
	userInfo.entId = '${entId}';
	param.userInfos = JSON.stringify(userInfo);
	$.post("<%=request.getContextPath()%>/userManageMongo/updateUser",param,updateCallBack,"json");
}

function updateCallBack(data){
	if(data.success){
		notice.success(data.msg);
		changePasswdSet();
	}else{
		notice.warning(data.msg);
	}
}

/* 创建工单 */
function newwork() {
    var curEmail="${user.email}";
    var curUserId="${user.userId}";
    var curUserName="${user.userName}";
    parent.newwork(curEmail,curUserId,curUserName,"");
}
</script>

<script type="text/javascript">
/*用户合并*/
function goMerge(){
    $(".merge-user .fr h4").html("未选择");
    $(".merge-user .fr p").html("");
    $(".merge-user .fr img").attr("src","<%=request.getContextPath()%>/H+3.2/img/profile_small.jpg");
    $(".merge-input div#forSelected").html("");
    $(".merge-input div#forSelected").hide();
    $(".merge-input input").val("");
    $(".merge-input input#userIdForMerge").val("");
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
        parent.closeThisTab();
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
                var param={"value":start,"loginName":'${user.loginName}',"all":value,"userId":'${user.userId}'};
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
function ordinaryBack(data){
    if(data.success){
        var users=data.rows;
        if(users.length>0){
            $(".merge-input div#forSelected").html("");
            for(var i=0;i<users.length;i++){
                var userId=users[i].userId,
                        loginName=users[i].loginName,photoUrl=users[i].photoUrl,userName=users[i].userName;
                addDivForSelected(userName,userId,loginName,photoUrl);
            }
            makeBackColor();
            makeClick();
            if(data.msg.length>3){
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
</script>
<script type="text/javascript">
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
</script>

<script type="text/javascript">
/**
 * 向后台请求数据
 */
var getTableData = (function(){
    /**
     * 初始化分页
     */
    var pager = new cri.Pager($("#paginationCharge"),{
        page:1,
        pageSize:10,
        total:0,
        onPage:function(page,pageSize){
            var userStatus="";
            var url="<%=request.getContextPath()%>/userManageMongo/queryChargeman";
            var roleId=null;
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

//负责人选择表格建立

var fieldNames={userName:"用户名",email:"邮箱",telPhone:"电话",createTime:"创建时间",loginTime:"最后登陆"};
var defaultFieldNames=fieldNames;
var createTable = (function(){
    var trTemp = Handlebars.compile($("#table-tr-template-charge").html());
    /* var trTempAgent = Handlebars.compile($("#table-tr-template-agent").html());
    var trTempFounder = Handlebars.compile($("#table-tr-template-founder").html()); */
    var $table = $("#grid");

    return function(tableData, type){
        var html = [];
        var checkCount = tableData.length;

        for(var i= 0,len=tableData.length; i<len;i++){

            var $tr=$(trTemp(tableData[i]));
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
    };
})();

function getDataTable(that){
	$(that).blur();
	//$(this).blur();
	getTableData({page:1,rows:10,entId:'${entId}'}, "<%=request.getContextPath()%>/userManageMongo/queryChargeman");
}
var choosedTr = null;
function rdChange(tr){
	choosedTr = tr;
}
function chooseMan(){
	var name = "",userId = "";
	if(choosedTr == null){
		notice.warning("请选择负责人");
		return ;
	}
	var dataH = $(choosedTr).data("data");
	name = dataH.userName;
	userId = dataH.userId;
	$("#chargeman").val(name);
	$("#chargeman").data("chargeId",userId);
	//notice.alert(name+":::"+userId);
	$("#setChargeModal").modal('hide');
}
</script>
</body>
</html>