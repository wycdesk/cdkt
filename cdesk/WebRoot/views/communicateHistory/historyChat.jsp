<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>带系统信息的联络历史-电话</title>
<%@include file="/views/include/pageHeader.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/historPhone-new.css">
</head>
<body>
     <div id="left-history"> 
        <div class="container-fluid">
        <form action="" id="historyForm">
            <div class="row">
                <h4 class="In">
                    <label>业务类型：</label>
                </h4>
                <div class="right-In">
                    <select name="businessType">
                        <option value="0">咨询组</option>
                        <option value="1">咨询组1</option>
                        <option value="2">咨询组2</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <h4  class="In">
                    <label>关联工单：</label>
                </h4>
                <div class="right-In">
                    <input name="workOrder"  type="text" value="关联工单">
                </div>
            </div>
            <!-- 自定义字段 -->
            <c:forEach items="${historyFieldList}" var="item">
	      		<div class="row">
	      			<h4 class="In">
	      				<label>${item.name }：</label>
	      			</h4>
	      			<div class="right-In">
	      				<c:choose>
	                         <c:when test="${item.componentType=='1'}">
                                 <input name="${item.key }" id="${item.key }" placeholder="" type="text"
                                        size="30" onchange="update(this.id,'text')"
                                        data-name="${item.key }" value="">
                             </c:when>

                             <c:when test="${item.componentType=='2'}">
									<textarea name="${item.key }" id="${item.key }"
                                       onchange="update(this.id,'textarea')"
                                       data-name="${item.key }">${fieldKey}</textarea>
                             </c:when>

                             <c:when test="${item.componentType=='3'}">
                                 <select name="${item.key }" id="${item.key }" data-name="">
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
                                 <input name="${item.key }" id="${item.key }" placeholder="" type="text" size="30" onchange="update(this.id,'int')" data-name="${item.key }" data-field="${item.name }" value="">
                             </c:when>

                             <c:when test="${item.componentType=='7'}">
                                 <input name="${item.key }" id="${item.key }" placeholder="" type="text" size="30" onchange="update(this.id,'float')" data-name="${item.key }" data-field="${item.name }" value="">
                             </c:when>

                             <c:when test="${item.componentType=='8'}">
                                 <c:forEach items="${item.candidateValue}" var="reg">
                                     <input name="${item.key }" id="${item.key }" placeholder="" type="text" size="30" onchange="update(this.id,'customized')" data-name="${item.key }" data-field="${item.name }" data-reg="${reg}" value="">
                                 </c:forEach>
                             </c:when>
                         </c:choose>
	      			</div>
	      		</div>
	      </c:forEach>
	      </form>
		</div>
        <div class="form-group modal-footer history-footer">
            <button type="button" class="btn btn-raised btn-primary btn-sm"  href="" onclick="submitChat()">提交</button>
            <button type="button" class="btn btn-raised btn-default btn-sm"  href="">取消</button>
        </div>
    </div>
    <div id="right-history"> 
        <header class="history-header">
            <div class="sidebar">通话记录</div>
        </header>
        <div class="right-content">
            <ul class="history-list">
                <li>
                    <label>客户姓名：</label>
                    <span>${historyInfo.userName}</span>
                </li>
                <li>
                    <label>联络方式：</label>
                    <span>${historyInfo.source}</span>
                </li>
                <li>
                    <label>坐席：</label>
                    <span>${historyInfo.opName}</span>
                </li>
                <li>
                    <label>开始时间：</label>
                    <span>${historyInfo.startTime }</span>
                </li>
                <li>
                    <label>状态：</label>
                    <span>${historyInfo.isConnected }</span>
                </li>
                <li>
                    <label>等待时长：</label>
                    <span>${historyInfo.commTime }秒</span>
                </li>
                <li>
                    <label>通话记录：</label>
                    <span><a>下载</a></span>
                    <div class="new-message" style="display:none;">
                		<div class="chatting">
                    	<div class="chakan-long" style="margin-right:10px;"><a>< 查看更多 ></a></div>
                    	<div class="chatting-text">
                       		暂无消息
                    	</div>
                	</div>
            		</div>
                </li>
                <li>
                    <label>随路数据：</label>
                    <span>文本</span>
                </li>
            </ul>
        </div>
    </div>

<script id="comm-IM-temp-left" type="text/x-handlebars-template">
<div style="float:left">
	<div class="leftCN-comm"></div>
	<div class="leftBox-comm">
		<span>{{text}}</span>
	</div>
</div>
<br/><br/>
</script>
<script id="comm-IM-temp-right" type="text/x-handlebars-template">
<div style="float:right">
	<div class="rightCN-comm"></div>
	<div class="rightBox-comm">
		<span>{{text}}</span>
	</div>
</div>
<br/><br/>
</script>
<script>

$(function(){
//	debugger;
//	submitChat();
	var li = $("#right-history");
	var msgDiv=$(li.find("div.new-message")[0]);
	msgDiv.css("display","block");
	var msgList = ${historyInfo.message};
	initIMMessage(msgDiv,msgList);
});
/*展示IM沟通消息*/ 
function initIMMessage(msgDiv,msgList){
	if(!msgList){
		return;
	}
	var msgContent=$(msgDiv.find("div.chatting-text")[0]);
	var jsonList=eval("("+msgList+")");
	msgDiv.find("div.chatting").css("padding-left","0px");
	msgDiv.find("div.chatting").css("padding-right","0px");
	msgContent.empty();
	for(var id in jsonList){
		msgContent.css("padding-left","10px");
		msgContent.css("padding-right","10px");
		msgContent.css("overflow","auto");
		msgContent.css("max-height","200px");
		if(jsonList[id].direction=="recv"){
			var leftTemp = Handlebars.compile($("#comm-IM-temp-left").html());
			//msgContent.append("<div style='margin-top:10px;float:left;width:100%;'><div style='padding:5px;border-radius:6px;background:#00faaa;float:left;'><span style='float:left;'>"+jsonList[id].text+"</span></div></div>");
			msgContent.append(leftTemp(jsonList[id]));
		}else{
			var rightTemp = Handlebars.compile($("#comm-IM-temp-right").html());
			//msgContent.append("<div style='float:left;width:100%;'><div style='padding:5px;border-radius:6px;background:yellow;float:right;'><span style='float:right;'>"+jsonList[id].text+"</span></div></div>");
			msgContent.append(rightTemp(jsonList[id]));
		}
	}
}

/*提交保存*/
function submitChat(){
//	alert("click submit");
	
	var param=$("#historyForm").formValue();
	var str=JSON.stringify(param);
	
	$.ajax({
		url:"<%=request.getContextPath()%>/history/modifyChatOrPhone",
		type:"post",
		dataType : "json",
		data:{
			entId:"${entId}",
			Info:str,
		},
		success:function(data){
			if(data.success){
				notice.success("添加成功！");
				
			}else{
				notice.danger("添加失败！"+data.msg);
			}
		}
	});
}
</script>
</body>
</html>