<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/views/include/pageHeader.jsp"%>
<link rel="stylesheet" type="text/css"href="<%=request.getContextPath()%>/static/css/systemSetting/all.css">
<title>管理日志</title>
</head>
<body class="page">
    <div class="panel-frame">
                <div class="panel-wrap">
            <h2>管理日志</h2>
<nav class="nav-tabs">
    <ul>
        <li id="all"><a onClick="goLog('all')" >全部</a></li>
        <li id="login"><a onClick="goLog('login')"  >登录</a></li>
        <li id="edit"><a onClick="goLog('edit')"  >编辑</a></li>
        <li id="delete"><a  onClick="goLog('delete')" >删除</a></li>
    </ul>
</nav>

   <div class="table-ui1" id="rightDiv">
            <div class="row table-content">
                <div class="col-12 grid">
                    <table class="table" cellspacing="0" cellpadding="0" id="grid">
                        <thead>
                              <tr>
           						  <th>操作用户</th>
           						  <th>动作</th>
           						  <th>操作对象</th>
          						  <th>操作时间</th>
           						  <th>操作IP</th>
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
            </div>
        </div>


<script id="table-tr-template" type="text/x-handlebars-template">
    <tr>
  	 <td>{{userName}}({{loginName}})</td>
     <td>{{operationStr}}</td>
 	 <td>{{operateObject}}</td>
	 <td>{{optTime}}</td>
     <td>{{loginIp}}</td>
    </tr>
</script>
<script type="text/javascript">
  $(function(){
	  $('li[id='+'${type}]').addClass("active");
	
	  getTableData({page:1,rows:10,type:'${type}'},"<%=request.getContextPath()%>/manageLog/query");
  });
  
  
  /**
   * 刷新表格
   */
  var createTable = (function(){
      var trTemp = Handlebars.compile($("#table-tr-template").html());
      var $table = $("#grid");
      
      return function(tableData, type){
          var html = [];
          for(var i= 0,len=tableData.length; i<len;i++){
              var $tr = $(trTemp(tableData[i]));
              $tr.data("data",tableData[i]);
              html.push($tr);
          }
          $table.find("tbody").empty().append(html);
      };
  })();
  
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
              getTableData({page:page,rows:pageSize,type:'${type}'},"<%=request.getContextPath()%>/manageLog/query");
          }
      });
      return function(param,url){
          url = url || "";
          $.ajax({
              url:url,
              dataType:'json',
              data:param,
              success:function(data){
                  createTable(data.rows);
                  pager.update(param.page,param.rows,data.total);
              }
          });
      };
  })();
  
  var sessionKey=$.cookie('sessionKey');
  
  function goLog(type){
	  location.href="<%=request.getContextPath()%>/manageLog/index?type="+type+"&sessionKey="+sessionKey;
  }
</script>


</body>
</html>