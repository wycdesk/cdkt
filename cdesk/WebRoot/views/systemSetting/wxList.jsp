<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/systemSetting/all.css">
<%@include file="/views/include/pageHeader.jsp"%>
<title>微信客服</title>
</head>
<body class="page">
    <div class="panel-frame">
                <div class="panel-wrap">
            <h2>微信客服</h2>

<button type="button" class="btn btn-primary btn-sm" onclick="gotoAddWx()">添加</button>

   <div class="table-ui1" id="rightDiv">
            <div class="row table-content">
                <div class="col-12 grid">
                    <table class="table" cellspacing="0" cellpadding="0" id="grid">
                        <thead>
                              <tr>
           						  <th>微信号</th>
           						  <th>原始号</th>
           						  <th>APPID</th>
           						  <th>备注</th>
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

<!-- 添加微信号 -->
<div id="addWxModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">添加微信号</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="addWxForm">
                    <div class="form-group">
                        <label for="inputOriginalId3" class="col-sm-3 control-label">原始号:</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="inputOriginalId3" name="originalId" placeholder="请输入原始号">
                            <p class="hint">原始号是微信的唯一标识，创建后不可更改</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputWxAccount3" class="col-sm-3 control-label">微信号:</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="inputWxAccount3" name="wxAccount" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputAppId3" class="col-sm-3 control-label">APP_ID:</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="inputAppId3" name="appId" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputAppSecret3" class="col-sm-3 control-label">APP_SECRET:</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="inputAppSecret3" name="appSecret" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputRemark3" class="col-sm-3 control-label">备注:</label>
                        <div class="col-sm-7">
                        	<textarea class="form-control" rows="3" id="inputRemark3" name="remark" placeholder="备注"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary btn-sm" onclick="addWxSubmit()" id="addWxSubmit">提交</button>
            </div>
        </div>
    </div>
</div>

<!-- 编辑微信号 -->
<div id="editWxModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">编辑微信号</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal" id="editWxForm">
                	<input type="hidden" id="inputOriginalId5" name="originalId">
                    <div class="form-group">
                        <label for="inputWxAccount5" class="col-sm-3 control-label">微信号:</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="inputWxAccount5" name="wxAccount" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputAppId5" class="col-sm-3 control-label">APP_ID:</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="inputAppId5" name="appId" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputAppSecret5" class="col-sm-3 control-label">APP_SECRET:</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="inputAppSecret5" name="appSecret" placeholder="">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputRemark5" class="col-sm-3 control-label">备注:</label>
                        <div class="col-sm-7">
                        	<textarea class="form-control" rows="3" id="inputRemark5" name="remark" placeholder="备注"></textarea>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-primary btn-sm" onclick="editWxSubmit()" id="editWxSubmit">提交</button>
            </div>
        </div>
    </div>
</div>
<script id="table-tr-template" type="text/x-handlebars-template">
    <tr>
  	 <td>{{wxAccount}}</td>
     <td>{{originalId}}</td>
     <td>{{appId}}</td>
 	 <td>{{remark}}</td>
	 <td><a href="javascript:void(0);" onclick="gotoEditWx('{{originalId}}','{{wxAccount}}','{{appId}}','{{appSecret}}','{{remark}}')" >编辑</a> <a href="javascript:void(0);" onclick="deleteWx('{{originalId}}')" >删除</a></td>
    </tr>
</script>
<script type="text/javascript">
  $(function(){
	  getTableData({page:1,rows:10},"<%=request.getContextPath()%>/entWx/query");
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
              getTableData({page:page,rows:pageSize},"<%=request.getContextPath()%>/entWx/query");
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
  
function gotoAddWx() {
	$("#addWxModal").modal('show');
}
  
function addWxSubmit() {
	if($("#inputOriginalId3").val()==""){
        notice.alert("请填写原始号！","alert-danger");
        return false;
    }
    if($("#inputAppId3").val()==""){
        notice.alert("请填写APP_ID！","alert-danger");
        return false;
    }
    if($("#inputAppSecret3").val()==""){
        notice.alert("请填写APP_SECRET！","alert-danger");
        return false;
    }
    
    $.post("<%=request.getContextPath()%>/entWx/add", $("#addWxForm").serialize(), addCallBack, 'json');
}

function addCallBack(data) {
	if (data.success) {
		notice.alert(data.msg);
		$("#addWxModal").modal('hide');
		window.location.href = '<%=request.getContextPath()%>/entWx/list';
	} else {
		notice.alert(data.msg,"alert-danger");
	}
}

function deleteWx(originalId) {
	if (confirm("确定要删除吗？删除后不可恢复！")) {
		$.post("<%=request.getContextPath()%>/entWx/delete", {originalId: originalId}, deleteCallBack, 'json');
	}
}

function deleteCallBack(data) {
	if (data.success) {
		notice.alert(data.msg);
		window.location.href = '<%=request.getContextPath()%>/entWx/list';
	} else {
		notice.alert(data.msg,"alert-danger");
	}
}

function gotoEditWx(originalId,wxAccount,appId,appSecret,remark) {
	$("#inputOriginalId5").val(originalId);
	$("#inputWxAccount5").val(wxAccount);
	$("#inputAppId5").val(appId);
	$("#inputAppSecret5").val(appSecret);
	$("#inputRemark5").val(remark);
	
	$("#editWxModal").modal('show');
}

function editWxSubmit() {
    if($("#inputAppId5").val()==""){
        notice.alert("请填写APP_ID！","alert-danger");
        return false;
    }
    if($("#inputAppSecret5").val()==""){
        notice.alert("请填写APP_SECRET！","alert-danger");
        return false;
    }
    $.post("<%=request.getContextPath()%>/entWx/update", $("#editWxForm").serialize(), editCallBack, 'json');
}

function editCallBack(data) {
	if (data.success) {
		notice.alert(data.msg);
		$("#editWxModal").modal('hide');
		window.location.href = '<%=request.getContextPath()%>/entWx/list';
	} else {
		notice.alert(data.msg,"alert-danger");
	}
}
</script>


</body>
</html>