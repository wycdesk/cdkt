<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<div id="editJobModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog" style="width:630px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">修改岗位信息</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid" >
                    <div class="row">
                        <div class="col-sm-12">
                            <form id="editJobForm" class="form-horizontal" autocomplete="true">
                                <input type="hidden" class="form-control" name="id" id="jobId">
                                <!--  岗位名称  -->
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">岗位名称：<span style="color:red">*</span>（必填）</label>
                                    <input type="text" class="form-control" name="name" id="jobName" placeholder="请输入岗位名称">
                                </div>
                                <!--  所属部门 -->
                                <div class="form-group" style="margin-bottom: 27px;" id="dpt1">
                                    <label for="inputPassword3" class="control-label">所属部门:</label>
                                    <select class="form-control" id="dptId1" name="dptId">
                                    </select>
                                </div>
                                <!-- 上级岗位 -->
                                <div class="form-group" style="margin-bottom: 27px;" id="parentDpt1">
                                    <label for="inputPassword3" class="control-label">上级岗位:</label>
                                    <select class="form-control" id="parentId1" name="parentId">
                                    </select>
                                </div>
                                <!--  岗位描述 -->
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">岗位描述：</label>
                                    <textarea class="form-control" name="reamrk" id="reamrk1" placeholder="请输入岗位描述"></textarea>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" id="editJobSubmit" onclick="editJobSubmit()">确定</button>
            </div>
        </div>
    </div>
</div>

<script>
    /*  编辑岗位 */
    function editJobSubmit(){
        if($("#jobName").val()==""){
            notice.warning("岗位名称不能为空!");
            return false;
        }

        var param = $("#editJobForm").formValue();
        $.ajax({
            url : "<%=request.getContextPath()%>/department/updateJob",
            type : "post",
            dataType : "json",
            async: false,
            data : param,
            success : function (data) {
                if (data.success) {
                    notice.alert("更新成功");
                    $("#editJobModal").modal("hide");
                    location.reload();
                } else {
                    notice.warning(data.msg);
                }
            }
        });
    }
</script>