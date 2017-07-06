<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/views/include/pageHeader.jsp" %>
<%-- <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css"> --%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/userField.css">
</head>
<body class="page">
  <div class="panel-frame ps-container ps-active-y">
  <div class="panel-wrap">
        <ul class="breadcrumbs">
           <li>
           <a target="_self" href="<%=request.getContextPath()%>/userField/userfields">用户字段  </a>
           </li>
           <li>选择自定义字段种类</li>
        </ul>
     
       <div class="kf5-section">
          <div class="field t-col">
             <h4 class="ln">文本框</h4>
             <div class="t-col-content">
                <div class="sample-box">
                    <div class="left">                   
                       <input name="" value="文本框里的文字" readonly="readonly" placeholder="" type="text">
                       <p class="hint">文本框字段可以填写少量的文字</p>
                    </div> 
                    <a target="_self" onclick="selectType('text')" class="btn-sm">选择此字段</a>                                           
                </div>
              </div>                        
           </div>
       </div>
       
       <div class="kf5-section">
          <div class="field t-col">
             <h4 class="ln">文本区域</h4>
             <div class="t-col-content">
                <div class="sample-box">
                    <div class="left">                   
                       <textarea name="" readonly="readonly">文本区域里的文字</textarea>
                       <p class="hint">文本区域字段可以填写多行文字及说明</p>
                    </div> 
                    <a target="_self" onclick="selectType('textarea')" class="btn-sm">选择此字段</a>
                </div>
             </div>                        
           </div>
       </div>
         
         <div class="kf5-section">
             <div class="field t-col">
                <h4 class="ln">下拉菜单</h4>
                   <div class="t-col-content">
                      <div class="sample-box">
                         <div class="left">                   
                           <select name="">
                              <option value="">男</option>
                              <option value="">女</option>
                           </select>
                           <p class="hint">基本的下拉菜单字段，可以设置多个下拉选项</p>
                         </div>
                         <a target="_self" onclick="selectType('dropdownlist')" class="btn-sm">选择此字段</a>
                      </div>
                  </div>
            </div>
         </div>
         
         <div class="kf5-section">
             <div class="field t-col">
                <h4 class="ln">复选框</h4>
                   <div class="t-col-content">
                      <div class="sample-box">
                         <div class="left">                   
                           <label class="checkboxlist">
                               <input type="checkbox" value="">
                                                                                               产品设计
                           </label>
                           <label class="checkboxlist">
                               <input type="checkbox" value="">
                                                                                               产品交互
                           </label>
                           <label class="checkboxlist">
                               <input type="checkbox" value="">
                                                                                             产品技术
                           </label>
                           <label class="checkboxlist">
                               <input type="checkbox" value="">
                                                                                              产品战略
                           </label>
                           <p class="hint">复选框字段可以设置多个能同时选中的选项</p>
                         </div>
                         <a target="_self" onclick="selectType('checkboxlist')" class="btn-sm">选择此字段</a>
                      </div>
                  </div>
            </div>
         </div>
         
       <div class="kf5-section">
          <div class="field t-col">
             <h4 class="ln">数字</h4>
             <div class="t-col-content">
                <div class="sample-box">
                    <div class="left">                   
                       <input name="" value="123456" placeholder="" type="number">
                       <p class="hint">这里仅可以填写数字</p>
                    </div> 
                    <a target="_self" onclick="selectType('number_int')" class="btn-sm">选择此字段</a>
                </div>
              </div>                        
           </div>
       </div>
         
       <div class="kf5-section">
          <div class="field t-col">
             <h4 class="ln">小数</h4>
             <div class="t-col-content">
                <div class="sample-box">
                    <div class="left">                   
                       <input name="" value="34.54" placeholder="" type="number">
                       <p class="hint">这里仅可以填写小数</p>
                    </div> 
                    <a target="_self" onclick="selectType('number_float')" class="btn-sm">选择此字段</a>
                </div>
              </div>                        
           </div>
       </div>  
         
       <div class="kf5-section">
          <div class="field t-col">
             <h4 class="ln">正则匹配字段</h4>
             <div class="t-col-content">
                <div class="sample-box">
                    <div class="left">                   
                       <input name="" value="13908187366" readonly="readonly" placeholder="" type="text">
                       <p class="hint">这里可以设置固定的正则匹配规则来限定文本框填写的内容</p>
                    </div> 
                    <a target="_self" onclick="selectType('customized')" class="btn-sm">选择此字段</a>
                </div>
              </div>                        
           </div>
       </div>
       
        <div class="kf5-section">
          <div class="field t-col">
             <h4 class="ln">电话号码</h4>
             <div class="t-col-content">
                <div class="sample-box">
                    <div class="left">                   
                       <input name="" value="13908187399" placeholder="" type="number">
                       <p class="hint">这里可以设置电话号码，匹配11位手机号或3-4位区号，7-8位直播号码，1-4位分机号码 </p>
                    </div> 
                    <a target="_self" onclick="selectType('phoneNum')" class="btn-sm">选择此字段</a>
                </div>
              </div>                        
           </div>
       </div>  
         
    </div>           
  </div>
    <script src="<%=request.getContextPath()%>/script/userField/all.min.js"></script>   
    <script type="text/javascript">
    /* 选择字段类型 */
    function selectType(data){
    	 //alert(data);
    	 var type=data;
         location.href="<%=request.getContextPath()%>/userField/addField?type="+type;
    }
    
	$(".panel-frame").perfectScrollbar();
    </script>
  </body>
</html>