<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!-- 批量导入用户 -->
<div id="importUserModal" class="modal fade bs-example-modal-sm">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">导入新用户</h4>
            </div>
            <div class="modal-body">
                <form class="form-horizontal dropzone" action="/file-upload">
                    <div class="form-group">
                        <label for="inputEmail" class="col-sm-3 control-label">用户昵称:</label>
                        <div class="col-sm-7">
                            <input type="text" class="form-control" id="inputEmail" placeholder="请输入用户昵称">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-sm-3 control-label">邮箱:</label>
                        <div class="col-sm-7">
                            <input type="email" class="form-control" id="inputPassword" placeholder="请输入邮箱地址">
                            <p class="hint">邮箱将作为用户的登录账号，创建后不可更改</p>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="inputPassword" class="col-sm-3 control-label">选择文件:</label>
                        <div class="col-sm-7">
                            <input type="file" class="form-control" id="fileImport" name="fileImport">
                            <p class="hint">注意： 导入新用户成功后，系统会立即发送欢迎邮件给导入的用户。</p>
                        </div>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="importSubmit()">提交</button>
            </div>
        </div>
    </div>

</div>

