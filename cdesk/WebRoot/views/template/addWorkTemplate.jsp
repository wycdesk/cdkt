<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/views/include/pageHeader.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/userField.css">

<link href="<%=request.getContextPath()%>/script/lib/font-awesome/css/font-awesome.css" rel="stylesheet">
<link href="<%=request.getContextPath()%>/static/css/common.css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/script/lib/func.js" ></script>
<script src="<%=request.getContextPath()%>/H+3.2/js/jquery-2.1.1.min.js"></script>
<script src="<%=request.getContextPath()%>/script/userField/all.min.js" type="text/javascript"></script>
</head>

<body class="page">
 <div class="panel-frame ps-container">
  <div class="panel-wrap">  
   <form target="_self" method="post" id="fieldForm">
             
     <ul class="breadcrumbs">
         <li>
         <a target="_self" href="<%=request.getContextPath()%>/workTemplate/workTemplates">工单自定义分类</a>
         </li>
         <li>添加 工单自定义分类 </li>
     </ul>   
     
     <div class="kf5-section">  
        <div class="field t-col">
            <h4 class="ln">对客服</h4>
            <div class="t-col-content">
               <h5> 显示标题（客服）</h5>          
               <input name="tempName" id="UserField_agent_title" maxlength="255" type="text">                
               <p class="hint">显示给客服的标题</p>          
            </div>
        </div>    
     </div>  
        
     <!--操作栏   -->               
     <div class="field field-operation">
        <a onclick="cancel()" class="btn-sm btn-red ml0" href="javascript:void(0);" target="_self" >取消</a>
        <input type="button" id="submitBtn" class="btn-sm btn-green fr" onClick="addTemplate()" value="提交">       
     </div>   
     
   </form>               
  </div>
</div>

  <script src="<%=request.getContextPath()%>/script/userField/all.min.js"></script>   
  <script type="text/javascript">

  /* 添加工单模板 */
  function addTemplate(){
	  /* 标题不能为空 */
	  if($("#UserField_agent_title").val()==""){
		  notice.warning("显示标题（客服） 不可为空白.");
		  return false;
	  }
      
	  /* 避免连续点击重复提交 */
	  $("#submitBtn").attr("disabled","disabled");
          $.ajax({
              url: "<%=request.getContextPath()%>/workTemplate/addWorkTemp",
              dataType: 'json',
              data: $("#fieldForm").serialize(),
              success: function (data) {
                  if (data.success) {
                	  /* 返回字段管理界面 */
                	  var title=data.rows;
                	  notice.success("工单自定义字段：" + title + "&nbsp;" + " 创建成功！");
                	  location.href="<%=request.getContextPath()%>/workTemplate/workTemplates"; 
                  }else {
                      notice.warning("添加工单自定义字段失败！");
                  }
              }
          });
  }  
	$(".panel-frame").perfectScrollbar();
  
	/* 返回模板列表页 */
	function cancel(){
		location.href="<%=request.getContextPath()%>/workTemplate/workTemplates"; 
	}
	
  </script>
 </body>
</html>