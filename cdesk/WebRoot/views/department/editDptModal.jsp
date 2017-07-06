<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editDptModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog" style="width:630px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">修改部门信息</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid" >
                    <div class="row">
                        <div class="col-sm-12">
                            <form id="editDptForm" class="form-horizontal" autocomplete="true">
                                <input type="hidden" class="form-control" name="id" id="dptId">
                                <!--  部门名称  -->                          
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">部门名称：<span style="color:red">*</span>（必填）</label>
                                    <input type="text" class="form-control" name="name" id="name" placeholder="请输入部门名称">
                                </div>
                                <!-- 上级部门 -->
                                <div class="form-group" style="margin-bottom: 27px;" id="parentDpt">
                                    <label for="inputPassword3" class="control-label">上级部门:</label>
                                    <select class="form-control" id="parentId" name="parentId">
                                    </select>
                                </div>
                                <!--  部门描述 -->
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">部门描述：</label>
                                    <textarea class="form-control" name="reamrk" id="reamrk" placeholder="请输入部门描述"></textarea>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">                
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>     
                <button type="button" class="btn btn-raised btn-primary btn-sm" id="editDptSubmit" onclick="editDptSubmit()">确定</button>          
            </div>
        </div>
    </div>
</div>

<script>   
   /*  编辑部门 */
   function editDptSubmit(){  
	   if($("#name").val()==""){
		   notice.warning("部门名称不能为空!");		   
           return false;
	   }
	   
	   var param = $("#editDptForm").formValue();	   
       $.ajax({
           url : "<%=request.getContextPath()%>/department/update",
           type : "post",
           dataType : "json",
           async: false,
           data : param,
           success : function (data) {
               if (data.success) {
            	   notice.alert("更新成功");
            	   $("#editDptModal").modal("hide");
            	   location.reload(); 
               } else {
                   notice.warning(data.msg);
               }
           }
       });
   }   
</script>