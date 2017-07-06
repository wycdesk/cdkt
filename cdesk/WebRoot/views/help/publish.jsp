<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<%@include file="/views/include/pageHeader.jsp"%>
<title>发布文档</title>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/help.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
</head>
<body>
<header class="part-header">
    <span>发布文档</span>
    <!-- <div class="dropdown">
        <a href="">+发布新文档</a>
    </div> -->
</header>
<div class="right-content">
    <div class="right-content-panel container">
        <div class="table-content">
            <div class="col-12 grid">
                <div class="body">
                    <div class="publish">
                        <div class="main">
                            <div class="publish-title">
                                <label class="required">标题<span class="red">*</span></label>
                                <p><input type="text" id="title" /></p>
                            </div>
                            <div class="publish-content">
                                <label class="required">内容<span class="red">*</span></label>
                                <!-- <div>
                                    <textarea style="width:100%;min-height:280px; border:1px solid #d4d4d4;border-radius: 4px;"></textarea>
                                </div> -->
                                <div class="md-editor" id="editor"></div>
                            </div>
                            <!-- <div>
                                <div class="uploader-box-small">
                                    <div class="uploader-drop-btn">
                                        <span>点击上传附件</span>
                                        <input type="file" />
                                    </div>
                                </div>
                            </div> -->
                            <div>
                            	<p align="center"><font color="blue">上传文件类型可为图片或txt格式等，大小不能超过
                            	<font color="red">2m</font>!</font></p>
                            </div>
                            <div>
                                <div class="uploader">
                                    <div id="fileGroup"></div>
                                    <form action="/"
                                          class="dropzone"
                                          enctype="multipart/form-data"
                                          id="my-dropzone"
                                          method="post">
                                    </form>
                                </div>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
            <div id="toolbar" class="show">
                <!-- <button type="button" class="btn btn-raised btn-default">保存草稿</button> -->
                <button type="button" class="btn btn-raised btn-success" onclick="goPublish();">保存并发布</button>
            </div>
        </div>
    </div>
</div>

<script src="<%=request.getContextPath()%>/script/lib/summernote/summernote.js" ></script>
<script src="<%=request.getContextPath()%>/script/lib/summernote/summernote-zh-CN.js" ></script>
<script type="text/javascript">
$(document).ready(function() {
	$("#editor").summernote({
	    lang:"zh-CN",
	    height: 200,
	    toolbar:[
	        ['style', ['style','bold', 'italic', 'underline', 'clear']],
	        ['font', ['strikethrough', 'superscript', 'subscript','hr']],
	        ['fontsize', ['fontsize']],
	        ['color', ['color']],
	        ['para', ['ul', 'ol', 'paragraph','table']],
	        ['height', ['height','redo','undo']],
	    ]});
		
	$("#title").val('${doc.title}');
	$("#editor").code('${doc.content}');
	
	//附件上传处理
    Dropzone.options.myDropzone = {
        //指定上传图片的路径
        url: "<%=request.getContextPath()%>/attachments/upload",
        //添加上传取消和删除预览图片的链接，默认不添加
        addRemoveLinks: true,
        //关闭自动上传功能，默认会true会自动上传
        autoProcessQueue: true,
        //添加区域提示信息
        dictDefaultMessage: "点击此处进行上传<br/>或将文件拖至此处",
        dictRemoveFile:"删除文件",
        sending: function(file, xhr, formData) {
            formData.append("userId", "${userId}");
            formData.append("userName","${userName}");
        },
        init: function () {
            myDropzone = this; // closure
            var fileDesc;
            //当上传完成后的事件，接受的数据为JSON格式
            this.on("complete", function (data) {
                if (this.getUploadingFiles().length === 0 && this.getQueuedFiles().length === 0) {
                    var res = eval('(' + data.xhr.responseText + ')');
                    if (res.success) {
                        var $fileDetail = $("<input type='text' class='file' style='display:none'>");
                        $fileDetail.attr("id",fileDesc);
                        $fileDetail.val(res.rows[0].fileNew).data("fileInfo",res.rows[0]);
                        $("#fileGroup").append($fileDetail);
                        notice.alert(res.msg,"alert-success");
                    }
                    else {
                        notice.alert(res.msg,'alert-danger');
                    }
                }
            });
            this.on("addedfile",function(file){
                fileDesc = file.lastModified;
            });
            //删除图片的事件，当上传的图片为空时，使上传按钮不可用状态
            this.on("removedfile", function (file) {
                if(file != null){
                    $.post("<%=request.getContextPath()%>/attachments/delete",{"newFileName":$("#"+file.lastModified).val()},function(data){
                        if(data.success){
                            $("#"+file.lastModified).remove();
                            notice.alert(data.msg,"alert-success");
                        }else{
                            notice.alert(data.msg,'alert-danger');
                        }
                    });
                }
            });
        }
    };
});

function goPublish(){
	if($("#title").val()==""||$("#title").val()==null){
		notice.warning("标题不能为空");
		return;
	}
	if($(".note-editable").text()==""||$(".note-editable").text()==null){
		notice.warning("内容不能为空");
		return;
	}
	var inputs=$("#fileGroup>input");
	var lt=inputs.length;
	var attachment="";
	if(lt>0){
		attachment=$(inputs.get(0)).data("fileInfo").attachmentId;
		if(lt>1){
			for(var i=1;i<inputs.length;i++){
				attachment=attachment+","+$(inputs.get(i)).data("fileInfo").attachmentId;
			}
		}
	}
	var param={};
	param["attachment"]=attachment;
	param["content"]=$("#editor").code();
	param["title"]=$("#title").val();
	
	var docId="${docId}";
	/* 是否为编辑 */
 	if('${isEdit}'=="true"){
		$.post("<%=request.getContextPath()%>/help/partition/goEdit?docId="+docId,param,editBack,"json");
	}else{
		$.post("<%=request.getContextPath()%>/help/partition/goPublish",param,publishBack,"json");
	}
	
}
function publishBack(data){
	if(data.success){
		notice.success(data.msg);
		parent.publichSuc();
	}else{
		notice.warning(data.msg);
	}
}

/* 编辑文档回调 */
function editBack(data){
	if(data.success){
		notice.success(data.msg);
		parent.publichSuc();
	}else{
		notice.warning(data.msg);
	}
}

</script>
</body>
</html>