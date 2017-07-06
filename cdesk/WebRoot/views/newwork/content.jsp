<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="publish">
	<div class="publish-title">
		<label id="title"></label>
		<p>
			<input id="wfTitle" maxlength="120" type="text">
		</p>
	</div>
	<div class="publish-content">
		<label id="content"></label>
		<div class="md-editor" id="editor"></div>
		
		<div>
            <p align="center"><font color="blue">上传文件类型可为图片或txt格式等，大小不能超过
            <font color="red">2m</font>!</font></p>
        </div>
		
        <div  class="uploader">
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
<script>
	$(function(){	
		//富文本框编辑
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
		
		if("${sessionId}"!=""){
			if("${source}"=="5"||"${source}"=="6"){
				$("#wfTitle").val("${title}");			
				$("#editor").code("${content}");
			}
		}

		
		
		//附件上传处理
		Dropzone.options.myDropzone = {
			//指定上传图片的路径
			url: "<%=request.getContextPath()%>/attachments/upload",
			//添加上传取消和删除预览图片的链接，默认不添加
			addRemoveLinks: true,

			//关闭自动上传功能，默认会true会自动上传
			autoProcessQueue: true,
             
			//添加区域提示信息
			dictDefaultMessage: "点击此处进行上传",
			
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

	//获取上传附件的信息
	function getAttachments(){
		var attachement=[];
		$("#fileGroup .file").each(function(){
			var data = $(this).data("fileInfo");
			attachement.push(JSON.stringify(data));
		});
		return "["+attachement.join(",")+"]";
	}
</script>