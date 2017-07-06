<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>新增联络历史</title>
<%@include file="/views/include/pageHeader.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/historPhone-new.css">
</head>
<body>
	<div id="left-history">
        <div class="container-fluid">
        <form action="" id="historyForm">
            <div class="row">
                <h4 class="In">
                    <label>客户姓名：</label>
                </h4>
                <div class="right-In">
                    <input name="userName" id="userName"  type="text" value="">
                </div>
            </div>
            <div class="row">
                <h4 class="In">
                    <label>联络方式：</label>
                </h4>
                <div class="right-In">
                    <select name="source" id="source">
                        <option value="0">QQ</option>
                        <option value="1">电话</option>
                        <option value="2">webchat</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <h4  class="In">
                    <label>关联工单：</label>
                </h4>
                <div class="right-In">
                    <input name="workOrder" id="workOrder"  type="text" value="关联工单">
                </div>
            </div>
            <div class="row">
                <h4 class="In">
                    <label>业务类型：</label>
                </h4>
                <div class="right-In">
                    <select name="businessType" id="businessType">
                        <option value="0">咨询组</option>
                        <option value="1">咨询组1</option>
                        <option value="2">咨询组2</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <h4 class="In">
                    <label>来源：</label>
                </h4>
                <div class="right-In">
                    <select name="origin" id="origin">
                        <option value="0">来源</option>
                        <option value="1">来源1</option>
                        <option value="2">来源2</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <h4 class="In">
                    <label>联络类型：</label>
                </h4>
                <div class="right-In">
                    <select name="commType" id="commType">
                        <option value="0">联络类型</option>
                        <option value="1">联络类型1</option>
                        <option value="2">联络类型2</option>
                    </select>
                </div>
            </div>
            <div class="row">
                <h4  class="In">
                    <label>描述：</label>
                </h4>
                <div class="right-In">
                    <input name="description" id="description"  type="text" value="">
                </div>
            </div>
            <div class="row">
                <h4  class="In">
                    <label>处理结果：</label>
                </h4>
                <div class="right-In">
                    <input name="handleResult" id="handleResult"  type="text" value="">
                </div>
            </div>
            <div class="row">
                <h4  class="In">
                    <label>标签：</label>
                </h4>
                <div class="right-In">
                    <input name="tag" id="tag"  type="text" value="">
                </div>
            </div>
            <div class="row">
                <h4  class="In">
                    <label>小结：</label>
                </h4>
                <div class="right-In">
                    <textarea name ="sunmmary" id="summary" rows="3" ></textarea>
                </div>
            </div>
      <!-- 自定义字段  -->    
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
            <button type="button" class="btn btn-raised btn-primary btn-sm"  href="" onclick="handSubmit()">提交</button>
            <button type="button" class="btn btn-raised btn-default btn-sm"  href="" onclick="chat()">取消</button>
        </div>
    </div>
</body>
<script type="text/javascript">
function handSubmit(){
	
	var username = $("#userName").val();
	if(username == ""){
           notice.warning("客户姓名不能为空！");
           return false;
       }else if(username.trim().length==0){
           notice.warning("客户姓名不能全为空格！");
           return false;
       }
	
	var workorder = $("#workOrder").val();
	if(workorder == ""){
        notice.warning("关联工单不能为空！");
        return false;
    }else if(workorder.trim().length==0){
        notice.warning("关联工单不能全为空格！");
        return false;
    }
	
	var param=$("#historyForm").formValue();
	var str=JSON.stringify(param);
	
	$.ajax({
		url:"<%=request.getContextPath()%>/history/handAdd",
		type:"post",
		dataType : "json",
		data:{
			entId:"${entId}",
			historyInfo:str,
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

function chat(){
	<%-- $.ajax({
		url:"<%=request.getContextPath()%>/history/chat",
		type:"post",
		dataType : "json",
		data:{
			commId:'307'
		},
		success:function(data){
			if(data.success){
				notice.success("添加成功！");
				
			}else{
				notice.danger("添加失败！"+data.msg);
			}
		}
	}); --%>
	parent.openTab("<%=request.getContextPath()%>/history/chat?commId=307","","修改","");
}
</script>
</html>