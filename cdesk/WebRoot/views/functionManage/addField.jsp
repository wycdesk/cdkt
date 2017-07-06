<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/views/include/pageHeader.jsp" %>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/userField.css">
</head>

<body class="page">
 <div class="panel-frame ps-container">
  <div class="panel-wrap">  
   <form target="_self" method="post" id="fieldForm">
     <input value="STRING" name="type" id="type" type="hidden">   
     <input name="componentType" id="componentType" type="hidden"> 
     <input name="defaultValue" id="defaultValue" type="hidden">   
     <input name="candidateValue" id="candidateValue" type="hidden">  
             
     <ul class="breadcrumbs">
         <li>
         <a target="_self" href="<%=request.getContextPath()%>/userField/userfields">用户字段</a>
         </li>
         <li>添加 用户字段 : <span id="span1"></span></li>
     </ul>   
     
     <div class="kf5-section">  
        <div class="field t-col">
            <h4 class="ln">对客服</h4>
            <div class="t-col-content">
            <h5> 显示标题（客服）</h5>          
            <input name="name" id="UserField_agent_title" maxlength="255" type="text">                
            <p class="hint">在用户信息页上显示给客服的标题</p>       
            <h5> 描述（可选）</h5>
            <textarea name="remark" id="UserField_description"></textarea>                
            <p class="hint">该字段在用户信息页上的描述信息</p>
        </div>
        </div>
        
     </div>
      
        <!-- 添加下拉菜单或复选框时显示 -->
        <div class="field t-col" style="display:none" id="dropdownlist">
            <h4 class="ln">下拉菜单的选项</h4>
            <div class="t-col-content">  
              <ul class="select-area" id="select-area">
                 <li id="index1">
                     <input id="items_1" name="UserField[items][1][key]" value="" placeholder="请输入选项内容" type="text">
                     <a target="_self" href="javascript:void(0);" class="remove" onclick="delItem(1);"></a>
                 </li>
              </ul>
              <a id="a1" target="_self" href="javascript:void(0);" class="add-conditions" onclick="addItem();"></a>
            </div>
        </div>
         
       <!-- 添加正则匹配字段时显示 -->
        <div class="field t-col" style="display:none" id="customized">
            <h4 class="ln">正则匹配字段</h4>
            <div class="t-col-content">
               <h5>请在这里输入正则表达式的规则</h5>
               <input name="UserField[items]" id="Customized_items" type="text">                    
               <p class="hint">
                                                       例如qq号码：^[1-9]\d{4,9}$  
                  <a onclick="window.open(&quot;<%=request.getContextPath()%>/userField/regularExpression/&quot;,&quot;newwindow&quot;,&quot;height=500,width=550,top=0,left=0,toolbar=no,menubar=no,scrollbars=no, resizable=no,location=no, status=no&quot;) ;">
                                                       查看更多常用正则表达式范例</a>
               </p>
            </div>
        </div>   
        
        <!--操作栏   -->               
        <div class="field field-operation">
            <input type="button" id="submitbutton" class="btn-sm btn-green fr" onClick="addType()" value="提交" >       
        </div>               
     </div>
   </form>               
  </div>
</div>

  <script src="<%=request.getContextPath()%>/script/userField/all.min.js"></script>   
  <script type="text/javascript">
  var i=1;
  var checklist=[];
  
  $(function(){	  
	  if("${type}"=="text"){
	      $("#span1").html("文本框");
	      $("#componentType").val("1");
	  }else if("${type}"=="textarea"){
		  $("#span1").html("文本区域");	 
	      $("#componentType").val("2");
	  }else if("${type}"=="dropdownlist"){
		  $("#span1").html("下拉菜单");
		  $("#a1").html("添加下拉菜单选项");
		  $("#componentType").val("3");
		  $("#dropdownlist").show();
	  }else if("${type}"=="checkboxlist"){
		  $("#span1").html("复选框");
		  $("#a1").html("添加复选框选项");		 
		  $("#componentType").val("4");
		  $("#dropdownlist").show();
	  }else if("${type}"=="number_int"){
		  $("#span1").html("数字");
	      $("#componentType").val("6");
	  }else if("${type}"=="number_float"){
	      $("#span1").html("小数");	
	      $("#componentType").val("7");
	  }else if("${type}"=="customized"){
		  $("#span1").html("正则匹配字段");
		  $("#componentType").val("8");
		  $("#customized").show();
	  }else if("${type}"=="phoneNum"){
		  $("#span1").html("电话号码");
		  $("#componentType").val("9");
	  }
  });
  
  /* 添加复选框选项 */
  function addItem(){
	  var spanHtml="";
	  i=i+1;
	  spanHtml+='<li id="index'+i+'")><input id="items_'+i+'" name="" value="" placeholder="请输入选项内容" type="text">'+
	  '<a target="_self" href="javascript:void(0);" class="remove" onclick="delItem('+i+');" ></a></li>';
	  $("#select-area").append(spanHtml);
  }
  
  /* 移除复选框选项 */
  function delItem(data){
	  i=i-1;
	  $("#index"+data).remove();
  }
  
  /* 添加用户自定义字段 */
  function addType(){
	  /* 标题不能为空 */
	  if($("#UserField_agent_title").val()==""){
		  notice.warning("显示标题（客服） 不可为空白.");
		  return false;
	  }
	    	  
	  if("${type}"=="checkboxlist" || "${type}"=="dropdownlist"){
	      $("#dropdownlist input[type=text]").each(function() { 
	    	  if($(this).val()!=""){
				  checklist.push($(this).val());	
	    	  }	  	 
          });   
	      
	      var json = {};
	      for(var i=0;i<checklist.length;i++){
	    	  json[i+""]=checklist[i];
	      }
	      
	      $("#candidateValue").val(JSON.stringify(json));	
	  }	   	  
	  if("${type}"=="customized"){
		  checklist[0]=$("#Customized_items").val();
		  
	      var json = {};
	      for(var i=0;i<checklist.length;i++){
	    	  json[i+""]=checklist[i];
	      }
	      $("#candidateValue").val(JSON.stringify(json));		
	  }
	  
	  
	  /* 电话自定义字段 */
	  if("${type}"=="phoneNum"){
/* 		   匹配格式：11位手机号
		        3-4位区号，7-8位直播号码，1-4位分机号码 
		                         如：12345678901,1234-12345678-1234 */
		         		  
 		  checklist[0]="((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)";
 		  var json = {};
	      
	      json[0]=checklist[0];
	      $("#candidateValue").val(JSON.stringify(json));		
	  }
	  
	  
	  /* 候选项不能为空（若有） */
	  if(("${type}"=="checkboxlist" || "${type}"=="dropdownlist" || "${type}"=="customized") && $("#candidateValue").val()==""){
		  notice.warning("选项不可为空白");
		  return false;
	  }
	  $("#submitbutton").attr("disabled","disabled");
          $.ajax({
              url: "<%=request.getContextPath()%>/userField/addDefinedField",
              dataType: 'json',
              data: $("#fieldForm").serialize(),
              success: function (data) {
                  if (data.success) {
                	  /* 返回字段管理界面 */
                	  var title=data.rows;
                	  notice.success("字段" + title + "创建成功！");
                	  location.href="<%=request.getContextPath()%>/userField/userfields"; 
                  }else {
                      notice.warning("添加自定义字段失败！");
                  }
              }
          });
  }  
	$(".panel-frame").perfectScrollbar();
  
  </script>
 </body>
</html>