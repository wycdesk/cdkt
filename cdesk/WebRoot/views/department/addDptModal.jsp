<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="addDptModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog" style="width:630px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">新增部门</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid" >
                    <div class="row">
                        <div class="col-sm-12">
                            <form id="addDptForm" class="form-horizontal" autocomplete="true">
                                <!--  部门名称  -->                          
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">部门名称：<span style="color:red">*</span>（必填）</label>
                                    <input type="text" class="form-control" name="name" id="addDptName" placeholder="请输入部门名称">
                                </div>
                                <!-- 上级部门 -->
                                <div class="form-group" style="margin-bottom: 27px;" id="parentDpt3">
                                    <label for="inputPassword3" class="control-label">上级部门:</label>
                                    <select class="form-control" id="parentId3" name="parentId">
                                    </select>
                                </div>
                                <!--  部门描述 -->
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">部门描述：</label>
                                    <textarea class="form-control" name="reamrk" id="reamrk3" placeholder="请输入部门描述"></textarea>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>        
                <button type="button" class="btn btn-raised btn-primary btn-sm" id="addDptSubmit" onclick="addDptSubmit()">确定</button>       
            </div>
        </div>
    </div>
</div>

<script> 
   /* 添加部门 */
   function addDptSubmit(){
	   if($("#addDptName").val()==""){
		   notice.warning("部门名称不能为空!");		   
           return false;
	   }
	   
	   var param = $("#addDptForm").formValue();	   
       $.ajax({
           url : "<%=request.getContextPath()%>/department/add",
           type : "post",
           dataType : "json",
           async: false,
           data : param,
           success : function (data) {
               if (data.success) {
            	   notice.alert("更新成功");
            	   $("#addDptModal").modal("hide");
            	   location.reload(); 
               } else {
                   notice.warning(data.msg);
               }
           }
       }); 
   }  
</script>
