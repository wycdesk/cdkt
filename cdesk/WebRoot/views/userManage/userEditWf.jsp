<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/console.css">
<div id="editOrderModal" class="modal fade bs-example-modal-sm">
    <div class="modal-dialog" style="width:750px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">更新选中工单</span>
            </div>
            <div class="modal-body">
                <div class="sidebar">
                    <div class="scrollbar-box ps-container">
                        <form>
                            <fieldset>
                                <div class="form-group">
                                    <label for="serviceGroupId" class="control-label">受理客服组</label>
                                    <div>
                                        <select id="serviceGroupId"  class="form-control" data-field="serviceGroup">
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="customServiceId" class="control-label">受理客服</label>
                                    <div>
                                        <select id="customServiceId" class="form-control" data-field="customService">
                                            <option value = "nochange">-不改变-</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">工单状态</label>
                                    <div>
                                        <select class="form-control" data-field="status">
                                            <option value = "nochange">-不改变-</option>
                                            <option value="1">受理中</option>
                                            <option value="2">等待回复</option>
                                            <option value="3">已解决</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">工单类型</label>
                                    <div>
                                        <select data-field="type" class="form-control">
                                            <option value = "nochange">-不改变-</option>
                                            <option value="">-</option>
                                            <option value="1">问题</option>
                                            <option value="2">事务</option>
                                            <option value="3">故障</option>
                                            <option value="4">任务</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="control-label">优先级</label>
                                    <div>
                                        <select data-field="priority" class="form-control">
                                            <option value = "nochange">-不改变-</option>
                                            <option value="">-</option>
                                            <option value="1">低</option>
                                            <option value="2">中</option>
                                            <option value="3">高</option>
                                            <option value="4">紧急</option>
                                        </select>
                                    </div>
                                </div>
                            </fieldset>
                            <fieldset></fieldset>
                        </form>
                    </div>
                </div>
                <div class="main">
                    <div class="radio-box">
                        <label><input class="" type="radio" name="replay-choice" checked value="0">公开回复</label>
                        <label><input class="" type="radio" name="replay-choice" value="1">私密回复<span>（仅客服可见）</span></label>
                    </div>
                    <div class="md-editor" id="editor"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
                <button onclick="sumitContent()"  type="button" class="btn btn-raised btn-primary btn-sm">提交</button>
            </div>
        </div>
    </div>
</div>
<script>
//初始化表单信息
$(function(){
	//获取客服组信息
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
	getAgentGroupInfo();
});

function getAgentGroupInfo(){
    var $serviceGroupName = $("#serviceGroupId");
    var param = {
        userId:'${userId}',
        entId:'${entId}'
    };
    $.post("<%=request.getContextPath()%>/userApi/queryGroupAgent",param,function(data){
        if(data.result == "0"){
            console.log('客服组信息:');
            console.dir(data);
            var data = data.rows;
            $("#serviceGroupId").empty();
            $("#serviceGroupId").append("<option value = 'nochange'>-不改变-</option>");
            $("#serviceGroupId").append('<option value="">-</option>');
            for(var i=0;i<data.length;i++){
                $("#serviceGroupId").append('<option value="' + data[i].groupId + '">' + data[i].groupName + '</option>');
            }
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
        };
        if(groupId==""){
        	$("#customServiceId").empty();
        	return;
        	
        }
        if(groupId=="nochange"){
        	$("#customServiceId").empty();
        	$("#customServiceId").append("<option value = 'nochange'>-不改变-</option>");
        	return;
        	
        }
        $.post("<%=request.getContextPath()%>/userApi/queryGroupAgent",param, function(data){
            if(data.result == "0"){
                var data = data.rows[0].agentList;
                $("#customServiceId").empty();
                $("#customServiceId").append("<option value = 'nochange'>-不改变-</option>");
                $("#customServiceId").append('<option value="">-</option>');
                for (var i = 0; i < data.length; i ++) {
                    $("#customServiceId").append('<option value="' + data[i].userId + '">' + data[i].userName + '</option>');
                }
            }
        });
    }
}

//提交按钮事件
function sumitContent(){
	var submitInfo={};
	$("#editOrderModal").modal("hide");
	//获取所选择数据的工单id
     var idStr = [];
     idStr = getSelected();
     //提交删除参数
     idStr = idStr.join(",");
      $("fieldset select").each(function(){
    	 if($(this).val()!="nochange"){
    		 if($(this).data("field") == "customService" || $(this).data("field")=="serviceGroup"){
    			 submitInfo[$(this).data("field")+"Id"]=$(this).val();
        		 submitInfo[$(this).data("field")+"Name"]=$(this).find("option:selected").text();
    		 }else{
    			 submitInfo[$(this).data("field")]=$(this).val();
    		 }
    	 }
     });
     //提交所选择的content
     submitInfo["content"]=$.trim($("#editor").code());
     submitInfo["updatorId"]="${creatorId}";
     submitInfo["updatorName"]="${creatorName}";
     ajaxrequest(idStr,submitInfo);
     $("#cancelBtn").click();             	                 		 
}

function ajaxrequest(idStr,submitInfo){
	$.ajax({
        url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/updateWorkOrder?sessionKey="+ $.cookie("sessionKey"),
        dataType:'jsonp',
        data:{
        	"idStr":idStr,
        	"info":encodeURI(JSON.stringify(submitInfo))
        },
        success:function(data){
        	if(data.success){
        		$("#editor").code("");
        		getAgentGroupInfo();
            	notice.success("工单数据更新成功！");
            	if($("#workOrders").prop("class")=="active"){
            	  		$("#workOrders").click(); 
            	  		getAssignTotal();
            	}
            	if($("#distributionWO").prop("class")=="active"){
            	  		$("#distributionWO").click();
            	  		getAssignTotal();
            	}       
            }else{
            	notice.danger("工单数据更新失败！");
            }
        }
    });
}
</script>