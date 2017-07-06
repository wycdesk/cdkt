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

<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/user-new-ylb.css">

<style type="text/css">
#setChargeModal .modal-content{
	width: 900px;
	left: -150px;
}
.modal-footer{
	text-align: center;
}
#userFieldRows td{
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
			            		<span style="float:left;" >登录账号</span>
			            		<span style="color:red;font-size: 22px;float:left;display:none;">*</span>
			            		：
		            		</label>
		            		
		            	</div>
		                <input id="loginName" name="loginName"   type="text" autocomplete="off" />
		            </div>
		            <div>
		            	<label>
		            		<span style="float:left;" >登录密码</span>
		            		<span style="color:red;font-size: 22px;float:left;display:none;" id = "passwordNeed">*</span>
		            		：
		            	</label>
		                <input id="loginPwd" name="loginPwd"  type="password"  />
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
	        	<input type = "button" style = "opacity: 0.5;" value = "创建工单" disabled="disabled"/>
	        	<input type = "button" style = "opacity: 0.5;" value = "创建联络历史" disabled="disabled"/>
        		<input type = "button" style = "opacity: 0.5;" value = "合并用户" disabled="disabled"/>
        		<input type = "button" style = "opacity: 0.5;" value = "删除用户" disabled="disabled"/>
        	</div>
        
            <div class="row" id="workAndCommunication">
                <div class="col-lg-12 col-md-12">
                    <div class="panel" style = "box-shadow:0 0 0 0;">
                    	<div class="bottom" id="bottom"  style = "background: white;border:0;">
						    <ul class="tab-title1">
						        <li class="active" id="workOrders">
						            <a id="workOrder">工单(0) </a>
						        </li>
						        <li id="distributionWO">
						            <a id="distributionWOs">联络历史(0) </a>
						        </li>
						    </ul>
						</div>
						<div class="panel-body">
							<span id = "wordShow">暂无工单</span>
							<div style = "width: 400px;height:300px;padding-left:50px;padding-top:100px;">
								<span>打电话，是先呼通坐席，再呼通客户；</span> <br>
								<span>自动外拨是通过平台，先呼通客户，再呼通坐席；</span>
							</div>
						</div>
                    </div>
                </div>
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
                            <td colspan="8"><div id="pagination"></div></td>
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

<script id="table-tr-template" type="text/x-handlebars-template">
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
$(document).ready(function(){
	$("#workAndCommunication .tab-title1 li").click(function(){
		$(this).siblings().removeClass("active");
		$(this).addClass("active");
		if($(this).attr("id") == "workOrders"){
			$("#wordShow").text("暂无工单");
		}else{
			$("#wordShow").text("暂无联络历史");
		}
	});
	$("#loginName").on("change",function(){
		if($(this).val()){
			if($("#passwordNeed").is(":hidden")){
				$("#passwordNeed").show();
			}
			$("#loginPwd").removeAttr("disabled");
		}else{
			if($("#passwordNeed").is(":visible")){
				$("#passwordNeed").hide();
			}
			$("#loginPwd").val("");
			$("#loginPwd").attr("disabled","disabled");
		}
	});
});
function saveUser(){
	var param = {};
	var userInfo = $("#userInformation").formValue();
	var loginName = userInfo.loginName;
	if(userInfo.loginName&&!userInfo.loginPwd){
		notice.warning("登录名存在的时候密码不能为空");
		return;
	}
	if(!userInfo.userName){
		notice.warning("用户名不能为空");
		return;
	}
	if(!userInfo.loginName&&userInfo.loginPwd){
		delete userInfo.loginPwd;
	}
	if(loginName){
		switch(loginName){
			case userInfo.email:
				param.loginType = "0";
				break;
			case userInfo.telPhone:
				param.loginType = "1";
				break;
			case userInfo.weixin:
				param.loginType = "2";
				break;
			case userInfo.weibo:
				param.loginType = "4";
				break;
			default:
				param.loginType = "8";
		}
	}
	
	param.entId = '${entId}';
	for(var prop in userInfo){
		if(!userInfo[prop]){
			delete userInfo[prop];
		}
	}
	userInfo.userType = "1";
	userInfo.roleId = "1";
	param.send = "false";
	if(userInfo.chargeman){
		userInfo.chargeId = $("#chargeman").data("chargeId");
	}
	var userInfos = JSON.stringify(userInfo);
	userInfos = encodeURI(userInfos);
	param.userInfos = userInfos;
	$.post("<%=request.getContextPath()%>/userManageMongo/addUser",param,saveCallBack,"json");
}

function saveCallBack(data){
	if(data.success){
		notice.success(data.msg);
		var userId = data.rows;
		location.href = "<%=request.getContextPath()%>/userManageMongo/userDetailsYlb?userId="+userId;
	}else{
		notice.warning(data.msg);
	}
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
    var trTemp = Handlebars.compile($("#table-tr-template").html());
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