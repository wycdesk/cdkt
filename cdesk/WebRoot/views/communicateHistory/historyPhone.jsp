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
	                              <input name="${item.key }" id="${item.key }" type="text" value="">
	                          </c:when>
	                          <c:when test="${item.componentType=='2'}">
									<textarea class="" name="${item.key }" id="${item.key }" rows="3"></textarea>
	                          </c:when>
	                          <c:when test="${item.componentType=='3'}">
	                              <select name="${item.key }" id="${item.key }">
	                                  <option value="">-</option>
	                                  <c:forEach items="${item.candidateValue}" var="selectItems">  
	                                      <option value="${selectItems }">${selectItems }</option>
	                                  </c:forEach>
	                              </select>
	                          </c:when>
                         </c:choose>
	      			</div>
	      		</div>
	      </c:forEach>
	      </form>
        </div>
        <div class="form-group modal-footer history-footer">
            <button type="button" class="btn btn-raised btn-primary btn-sm"  href="" onclick= "submitChat()">提交</button>
            <button type="button" class="btn btn-raised btn-default btn-sm"  href="">取消</button>
        </div>
    </div>
    <div id="right-history">
        <header class="history-header">
            <div class="sidebar">通话记录</div>
        </header>
        <div class="right-content">
            <ul class="history-list">
              <c:forEach items="${historyInfoList}" var="item">
                <li>
                    <label>客户姓名：</label>
                    <span>${item.userName}</span>
                </li>
                <li>
                    <label>联络方式：</label>
                    <span>${item.source}</span>
                </li>
                <li>
                    <label>主叫：</label>
                    <span>${item.strAni}</span>
                </li>
                <li>
                    <label>被叫：</label>
                    <span>${item.strDnis}</span>
                </li>
                <li>
                    <label>坐席：</label>
                    <span>${item.opName}</span>
                </li>
                <li>
                    <label>开始时间：</label>
                    <span>${item.startTime }</span>
                </li>
                <li>
                    <label>状态：</label>
                    <span>${item.isConnected }</span>
                </li>
                <li>
                    <label>等待时长：</label>
                    <span>${item.commTime }秒</span>
                </li>
                <li>
                    <label>通话录音：</label>
                    <span><audio src="" controls="controls"></audio></span>
                    <span><a>下载</a></span>
                </li>
                <li>
                    <label>随路数据：</label>
                    <span>文本</span>
                </li>
              </c:forEach>
            </ul>
        </div>
    </div>
</body>
<script type="text/javascript">
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
</html>