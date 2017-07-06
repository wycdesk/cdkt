<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<div id="addUserModal" class="modal fade bs-example-modal-sm" data-backdrop="static">
    <div class="modal-dialog" style="width:630px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">添加新用户</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid" >
                    <div class="row">
                        <div class="col-sm-12">
                            <form id="formId" class="form-horizontal" autocomplete="true">
                                <!-- 角色权限 -->
                                <div class="form-group" style="margin-bottom: 27px;" id="type">
                                    <label for="inputPassword3" class="control-label">用户角色:<span style="color:red">*</span>（必选）</label>
<!--                                     <select class="form-control" name="userType" id="typeSelect" onchange="secondLevel()">
                                        <option value="1">客户</option>
                                        <option value="2">客服</option>
                                        <option value="3">管理员</option>
                                    </select>
                                    <p class="help-block">管理员和客服人员是处理工单的的用户角色，普通用户是外部提交工单的用户角色</p> -->
                                </div>
                                <div class="form-group">
                                    <label for="inputPassword3" id="roleLabel" class="control-label" style="float:left;display:none">角色权限:<span style="color:red">*</span>（必选）</label>
                                    <select class="form-control" id="secondType" style="display:none"></select>
                                </div>
                                <div style="display:none">
                                    <input type="text" class="form-control" name="roleId" id="roleId">
                                </div>

                                <!-- 账号密码(必填) -->
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">登录账号：<span style="color:red">*</span>（必填）</label>
                                    <input type="text" class="form-control" name="lgNm" id="lgNm" placeholder="请输入用户邮箱或手机号">
                                </div>
                                <div class="form-group">
                                    <input type="password" style="display:none;">
                                    <label for="inputPassword3" class="control-label">密码：<span style="color:red">*</span>（必填）</label>
                                    <input type="password" class="form-control" name="loginPwd" id="passWord"  placeholder="请设置用户初始密码" autocomplete="off">
                                </div>

                                <!-- 其他信息(可选) -->
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">邮箱:</label>
                                    <input type="email" class="form-control" name="email" id="inputPassword3" placeholder="">
                                    <input type="checkbox" id="receiveEmail" > (可接收邮件提醒)
                                </div>

                                <!-- 用户姓名(必填) -->
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">用户姓名：<span id="star" style="color:red;display:none">*</span><span id="need" style="display:none">（必填）</span></label>
                                    <input type="text" class="form-control" name="usrNm" id="addUsrNm" placeholder="客服或管理员必填">
                                </div>

                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">手机:</label>
                                    <input type="text" class="form-control" name="telPhone" id="addUserPhone" placeholder="客服或管理员必填">
                                </div>
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">座机:</label>
                                    <input type="text" class="form-control" name="fixedPhone" id="fixedPhone" placeholder="">
                                </div>
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">用户说明:</label>
                                    <input type="text" class="form-control" name="uDesc" id="uDesc">
                                </div>
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">详细信息:</label>
                                    <input type="text" class="form-control" name="remark" id="remark" placeholder="">
                                </div>
                                <!-- 自定义字段     -->
                                <div>
                                    <a id="openOrClose" onclick="openOrClose()" class="unfold">
                                        <span id="open">展开</span>
                                        <span id="close" style="display:none">收起</span>
                                    </a>
                                    <div id="userFieldUl" style="display: none;">
                                        <c:forEach items="${activeFieldList}" var="item">
                                            <div class="form-group">
                                                <label for="inputPassword3" class="control-label">${item.name }：</label>
                                                <c:choose>
                                                    <c:when test="${item.componentType=='1'}">
                                                        <input class="form-control" name="${item.key }" id="${item.key }" placeholder="-" type="text"
                                                               size="30" onchange="update(this.id,'text')"
                                                               data-name="${item.key }" value="${fieldKey}">
                                                    </c:when>

                                                    <c:when test="${item.componentType=='2'}">
														<textarea class="form-control" name="${item.key }" id="${item.key }"
                                                                  onchange="update(this.id,'textarea')"
                                                                  data-name="${item.key }">${fieldKey}</textarea>
                                                    </c:when>

                                                    <c:when test="${item.componentType=='3'}">
                                                        <select class="form-control" name="${item.key }" id="${item.key }" data-name="${item.key }">
                                                            <option value="">-</option>
                                                            <c:forEach items="${item.candidateValue}" var="selectItems">
                                                                <option value="${selectItems }">${selectItems }</option>
                                                            </c:forEach>
                                                        </select>
                                                    </c:when>

                                                    <c:when test="${item.componentType=='4'}">
                                                        <div class="checkFieldDiv">
                                                            <ul>
                                                                <li id="${item.key}">
                                                                    <c:forEach items="${item.candidateValue}" var="checkItems">
                                                                        <input  name="${item.key }" value="${checkItems}" type="checkbox" >
                                                                        <span>${checkItems}</span>
                                                                    </c:forEach>
                                                                </li>
                                                            </ul>
                                                        </div>
                                                    </c:when>

                                                    <c:when test="${item.componentType=='6'}">
                                                        <input class="form-control" name="${item.key }" id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'int')" data-name="${item.key }" data-field="${item.name }" value="${fieldKey}">
                                                    </c:when>

                                                    <c:when test="${item.componentType=='7'}">
                                                        <input class="form-control" name="${item.key }" id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'float')" data-name="${item.key }" data-field="${item.name }" value="${fieldKey}">
                                                    </c:when>

                                                    <c:when test="${item.componentType=='8'}">
                                                        <c:forEach items="${item.candidateValue}" var="reg">
                                                            <input class="form-control" name="${item.key }" id="${item.key }" placeholder="-" type="text" size="30" onchange="update(this.id,'customized')" data-name="${item.key }" data-field="${item.name }" data-reg="${reg}" value="${fieldKey}">
                                                        </c:forEach>
                                                    </c:when>
                                                </c:choose>
                                            </div>
                                        </c:forEach>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" id="addUserSubmit">提交</button>
            </div>
        </div>
    </div>
</div>
<script>
    !function(){
        $(function(){
            $('#addUserSubmit').click(addUserSubmit);
        });
        /**
         * 添加用户
         */
        function addUserSubmit(){
            var addType=$("#typeSelect").val();
            var $secondType = $("#secondType");
            if($secondType.val()==null){
                $("#roleId").val(addType);
            }else{
                $("#roleId").val($secondType.val());
            }

            /* 登录账号不能为空且不能全为空格 */
            var loginName = $("#lgNm").val();
            if(loginName.trim() == ""){
                notice.warning("登录账号不能为空！");
                return false;
            }

            /* 确认账号登录方式 */
            accountFormat(loginName);

            /* 密码必须大于等于6位且不能全为空格 */
            var password=$("#passWord").val();
            if(password.length<6){
                notice.warning("密码长度必须大于等于6位！");
                return false;
            }else if(password.trim().length==0){
                notice.warning("密码不能为空！");
                return false;
            }

            /* 邮箱格式校验 */
            var email = $("#inputPassword3").val();
            if(email!=""){
                if(!mailFormat(email)){
                    return false;
                }
            }

            /* 检测用户名是否重复 */        
            if($("#typeSelect").val()=="2" || $("#typeSelect").val()=="3"){
            	var uName = $("#addUsrNm").val();
                if(!checkName(uName)){
                	return false;
                }
            }
            
            /* 自定义字段复选框表单提交  */
            var param=$("#formId").formValue();
            param.userDesc = param.uDesc;
            param.loginName = param.lgNm;
            param.userName = param.usrNm;
            var test={};
            $(".checkFieldDiv input[type=checkbox]").each(function(){
                test[this.name] || (test[this.name]=[]);
                if($(this).prop('checked')){
                    test[this.name].push($(this).attr('value'));
                }
            });
            for(var name in test){
                param[name]=test[name];
            }
            var str=JSON.stringify(param);

            // 添加客服和管理员，校验用户姓名
            var userName = $("#addUserNameN").val();
            if(addType!="1"){
                if(userName==""){
                    notice.warning("请填写用户姓名!");
                    return false;
                }
            }

            var telPhone = $("#addUserPhone").val();
            /* 手机格式校验（1开头11位） */
            if(telPhone!=""){
                if(!phoneFormat(telPhone)){
                    return;
                }
            }

            /* 校验手机号是否已存在 */
            if(telPhone!=""){
                var bool=existPhone(telPhone);
                if(bool=="false"){
                    return;
                }
                else if(bool=="true"){
                    $.ajax({
                        url : "<%=request.getContextPath()%>/userManageMongo/addUser",
                        type : "post",
                        dataType : "json",
                        data : {
                            entId :   "${entId}",
                            userInfos: str,
                            send: send,
                            loginType: loginType,
                        },
                        success : function (data) {
                            if (data.success) {
                                notice.success("用户创建成功！");
                                $("#addUserModal").modal('hide');
                                var userId=data.rows;
                                var userName=$("#loginName").val();
                                var url= "<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+userId;
                                parent.openTab(url,"user",userName,true);
                            } else {
                                $("#addUserModal").modal('hide');
                                notice.danger("添加失败！"+data.msg);
                            }
                        }
                    });
                }
            }else{
                $.ajax({
                    url : "<%=request.getContextPath()%>/userManageMongo/addUser",
                    type : "post",
                    dataType : "json",
                    data : {
                        entId :   "${entId}",
                        userInfos: str,
                        send: send,
                        loginType: loginType,
                    },
                    success : function (data) {
                        if (data.success) {
                            notice.success("用户创建成功！");
                            $("#addUserModal").modal('hide');
                            var userId=data.rows;
                            var userName=$("#loginName").val();
                            var url= "<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+userId;
                            parent.openTab(url,"user",userName,true);
                        } else {
                            $("#addUserModal").modal('hide');
                            notice.danger("添加失败！"+data.msg);
                        }
                    }
                });
            }
        }
        /* 验证登录方式 */
        function accountFormat(loginName){
            var patrn = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
            var patrn1 = /^1\d{10}$/;

            if (patrn.test(loginName)){
                /* 邮箱 */
                loginType="0";
            }else if(patrn1.test(loginName)){
                /* 电话 */
                loginType="1";
            }else{
                /* 用户输入 */
                loginType="8";
            }
        }
        /*  邮箱格式校验 */
        function mailFormat(email){
            var patrn = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
            if (patrn.test(email)){
                return true;
            }else{
                notice.warning("请输入正确的邮箱格式！");
                return false;
            }
        }

        /*  手机校验 （1开头11位）*/
        function phoneFormat(telPhone){
            //var patrn = /^1\d{10}$/;
            var patrn = /^1\d{10}$|^[9][1]\d{10}$|^[9][0][1]\d{10}$/;
            if (patrn.test(telPhone)){
                return true;
            }else{
                notice.warning("请输入正确的手机格式！");
                return false;
            }
        }

        /* 校验手机号码是否已存在 */
        function existPhone(telPhone){
            var result="false";
            $.ajax({
                url : "<%=request.getContextPath()%>/userManageMongo/existsPhone1?telPhone="+telPhone,
                type : "post",
                dataType : "json",
                async: false,
                data : {
                    userInfos : "{'telPhone':'"+telPhone+"','entId':'${entId}'}",
                },
                success : function (data) {
                    if (data.success) {
                        result="true";
                    } else {
                        notice.warning(data.msg);
                    }
                }
            });
            return result;
        }
    }();
    
    /* 自定义字段输入内容展开或收起 */
    function openOrClose(){
        if($("#userFieldUl").css("display")=="none"){
            $("#userFieldUl").css("display","block");
            $("#open").css("display","none");
            $("#close").css("display","block");
        }
        else if($("#userFieldUl").css("display")=="block"){
            $("#userFieldUl").css("display","none");
            $("#open").css("display","block");
            $("#close").css("display","none");
        }
    }
    
    /* 校验用户名是否已存在 */
    function checkName(name){
        var result=false;
        $.ajax({
            url : "<%=request.getContextPath()%>/userManageMongo/checkUserName?userName="+name,
            type : "post",
            dataType : "json",
            async: false,
            data : {
                userInfos : "{'userName':'"+name+"','entId':'${entId}'}",
            },
            success : function (data) {
                if (data.success) {
                    result=true;
                } else {
                    notice.warning(data.msg);
                }
            }
        });
        return result;
    }
</script>