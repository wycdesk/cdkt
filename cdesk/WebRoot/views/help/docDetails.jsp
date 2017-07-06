<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="/views/include/pageHeader.jsp"%>
<title>文档详情</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/help.css">
</head>
<body>
<header class="part-header">
    <span>文档详情</span>
</header>
<div class="right-content">
    <div class="right-content-panel container">
        <div class="table-content">
            <div class="col-12 grid">
                <div class="body">
                    <div class="main">
                        <div class="publish-title">                          
                           <p><label id="title" /><strong>${doc.title}</strong></p>                               
                           <strong>${doc.author}</strong> • 发表于：${doc.createTime} • 更新于：${doc.updateTime}<br> 
                        </div>                                                     
                        <div class="publish-content">                            
                        <p><span id="editor" /></p>
                        </div>                                                   
                    </div>                   
                </div>  
                
                <!--   附件下载 -->
                <div class="publish-title" id="attachment" style="display:none">               
                    <c:forEach items="${attachList}" var="item">
                         <label>附件：${item.originalName} (${item.size}K) <a href="<%=request.getContextPath()%>/attachments/download?newFileName=${item.fileNew}">&nbsp;&nbsp;下载</a></label>                
                    </c:forEach>                
                </div>                                     
            </div>           
        </div>
    </div>
</div>

<script type="text/javascript">
$(document).ready(function() {  
	$("#editor").html('${doc.content}');	
	var list='${attachList}';
	
	if(list!=null){
		$("#attachment").show();
	}else{
		$("#attachment").hide();
	}	
});

</script>
</body>
</html>