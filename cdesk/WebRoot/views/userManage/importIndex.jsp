<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>用户导入</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/ajaxFileUpload/ajaxfileupload.css">
    <script src="<%=request.getContextPath()%>/script/lib/ajaxFileUpload/ajaxfileupload.js" ></script>
</head>
<body>
    <div class="panel-frame ps-container">
        <div class="panel-wrap">
            <div class="tab-content">
                <!-- 添加用户 -->
                <div role="tabpanel" class="tab-pane active" id="home">
                    <div class="section-pt">
                        <div class="field t-col">
                            <h4>
                                <label>添加 用户</label>
                            </h4>
                            <div class="t-col-content">
                                <label>
                                    <input name="create_user" id="create_user" value="1" type="checkbox" checked="checked" disabled="disabled">
                                    创建导入文件中的用户
                                </label>
                                <p class="hint">使用导入的文件里的数据在云客服平台创建新用户</p>
                            </div>
                        </div>
                        <!-- <div class="field t-col">
                            <h4>
                                <label>更新 用户</label>
                            </h4>
                            <div class="t-col-content">
                                <label>
                                    <input name="update_user" id="update_user" value="1" type="checkbox" checked="checked" >
                                    更新已经存在的用户
                                </label>
                                <p class="hint">使用导入的文件里的数据更新已经存在的用户信息</p>
                            </div>
                        </div> -->
                        <div class="form-group">
                            <label for="fileImport" class="control-label">选择文件导入</label>
                            <div>
                                <input type="text" readonly="" class="form-control" placeholder="请选择文件上传...">
                                <input type="file" id="fileImport" name="fileImport"  multiple="">
                            </div>
                            <p class="hint">
                                请在右边侧栏链接中了解导入文件格式规范。
                            </p>
                        </div>
                        <div class="field t-col" style="display:none" id="callBack">
                            <h4>
                                <label>导入结果</label>
                            </h4>
                            <h4>
                                <a href="#" onclick="downloadResult()">下载结果文件</a>
                            </h4>
                            <div class="t-col-content">
                                <ol>
                                <li>共计<span class="hint" id="totalNum" style="color: red"></span>条记录，用时<span class="hint" id="time" style="color: red"></span>秒</li>
                                <li>成功导入<span class="hint" id="importSuccessNum" style="color: red"></span>条记录</li>
                                <li>更新<span class="hint" id="updateSuccessNum" style="color: red"></span>条记录</li>
                                <li>导入失败<span class="hint" id="importFailNum" style="color: red"></span> 条记录</li>
                                <li>导入失败记录列表：</li>
                                </ol>
                                <ol id="failList">

                                </ol>
                            </div>
                        </div>
                        <div class="field field-operation">
                           <!--  <input type="submit" name="yt0" onClick="download()" value="下载模板"> -->
                            <input type="submit" name="yt0" class="btn btn-raised btn-primary btn-sm" onClick="importSubmit()" value="导入">
                        </div>


                    </div>
                </div>
            </div>
        </div>
        <div class="help_block">
            <button class="help_tab" type="button" data-toggle="collapse" data-target="#collapseExample" aria-expanded="false" aria-controls="collapseExample">
                帮助中心与说明
            </button>
            <div class="collapse" id="collapseExample">
                <div class="well">
                    导入的文件必须是CSV格式，而且被存储的字符集不能为Unicode类型，支持的字符集包括GBK,UTF-8,ANSI。
                    <br>
                    <a href="<%=request.getContextPath()%>/userImport/help" target="_blank">了解更多</a>
                </div>
            </div>
        </div>
    </div>
    
<script>
	
function importSubmit(){
 	var param=new Object();
 	param.addUserFlag = $("#create_user").val();
 	param.updateUserFlag = $("#update_user").val();
 	
 	
 	$.ajaxFileUpload({
		url : "<%=request.getContextPath()%>/userImport/upload",// url  
		secureuri : false,
		fileElementId : 'fileImport',// 上传控件的id  
		dataType : 'json',
		data : param, // 其它请求参数  
		success : function(data){
			importSubmitCallBack(data);
		},
		handleError : function(data, status, e) {
			/* notice.alert("导入失败"); */
			notice.warning("导入失败");
		}
	});
 }
 var resultFilePath;
 function importSubmitCallBack(data){
 	if(data.success == true){
 		resultFilePath = data.rows.resultFilePath;
		document.getElementById("callBack").style.display="";
		$("#totalNum").html(data.rows.totalNum);
		$("#time").html(data.rows.time);
		$("#importSuccessNum").html(data.rows.importSuccessNum);
		$("#updateSuccessNum").html(data.rows.updateSuccessNum);
		$("#importFailNum").html(data.rows.importFailNum);
		var items = data.rows.failList;
		var html = "";
		for(var i=0;i<items.length;i++){
			html += "<li>"+items[i]+"</li>"
		}
		$("#failList").html(html);
 	}else{
 		notice.warning(data.msg);
 	}
 }
 
 function download(){
		window.open('<%=request.getContextPath()%>/userImport/download');
	}
 function downloadResult(){
	 window.open('<%=request.getContextPath()%>/userImport/downResultExcel?resultFilePath='+encodeURI(resultFilePath));
 }
 
</script>
</body>
</html>