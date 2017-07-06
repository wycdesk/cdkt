<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>系统设置</title>
       <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/console.css">
</head>
<body>
<div id="left-part">
    <header class="part-header">系统设置</header>
    <div class="left-content">
        <c:if test="${hasGroup1}">
            <div class="left-content-panel">
              
                <ul class="left-content-panel-body left-part-list" id="group1Menu">
                    <c:forEach items="${permissionList}" var="item">
                        <c:if test="${item.groupId == '1'}">
                            <li id="li${item.id}" data-href="${item.url}" data-isajax="${item.isAjax}"><a>${item.name}</a></li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

          <c:if test="${hasGroup2}">
            <div class="left-content-panel">
                <div class="left-content-panel-header">支持渠道</div>
                <ul class="left-content-panel-body left-part-list" id="group2Menu">
                    <c:forEach items="${permissionList}" var="item">
                        <c:if test="${item.groupId == '2'}">
                            <li id="li${item.id}" data-href="${item.url}" data-isajax="${item.isAjax}"><a>${item.name}</a></li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </c:if>

        <c:if test="${hasGroup3}">
            <div class="left-content-panel">
                <div class="left-content-panel-header">账号</div>
                <ul class="left-content-panel-body left-part-list" id="group3Menu">
                    <c:forEach items="${permissionList}" var="item">
                        <c:if test="${item.groupId == '3'}">
                            <li id="li${item.id}" data-href="${item.url}" data-isajax="${item.isAjax}"><a>${item.name}</a></li>
                        </c:if>
                    </c:forEach>
                </ul>
            </div>
        </c:if>
</div>
<div id="right-part">
<iframe name="iframe" id="rightIframe" style="display:none" width="100%" height="100%" src="" frameborder="0" data-id="index_v1.html" seamless></iframe>
</div>
<script src="<%=request.getContextPath()%>/H+3.2/js/plugins/footable/footable.all.min.js"></script>
<script>
    $(document).ready(function() {
    	
        $("#left-part .left-content-panel li").click(function(){
        	lid=$(this).attr("id");
        	
            $("#left-part .left-content-panel li").removeClass("active");
            $(this).addClass("active");
            $("#right-part .part-header").text($(this).find("a").text());
      
            	if ($(this).data("isajax") == '1') {
		            $("#rightDiv").show();
		            $("#rightIframe").hide();
		            $("#gridGroup").hide();
		            $("#paginationTR").show();
		            $("#paginationForgTR").hide();
		            $("#toolbar").removeClass("show");
                    getTableData({page:1,rows:10,entId:'${entId}'},$(this).data("href"));
                    $("#grid").show();
                }
                else {
                    $("#rightDiv").hide();
		            $("#rightIframe").show();
                    $("#rightIframe").attr('src',$(this).data("href"));
                }
          
        });
        $("#left-part .left-content-panel:first-child li:first-child").click();
    });
</script>
</body>
</html>