<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<%@include file="/views/include/pageHeader.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/systemSetting/all.css">
<title>品牌设置</title>
</head>
<body class="">
	<div class="panel-frame">
		<div class="panel-wrap">
			<h2>品牌设置</h2>
			<nav class="navbar navbar-default">
				<!-- Nav tabs -->
				<ul class="nav navbar-nav" role="tablist">
					<li role="presentation" class="active">
						<a href="#home" aria-controls="home" role="tab" data-toggle="tab">基本设置</a>
					</li>
					<!-- 
					<li role="presentation">
						<a href="#profile" aria-controls="profile" role="tab" data-toggle="tab">域名绑定</a>
					</li>
					 -->
				</ul>
			</nav>
			<!-- Tab panes -->
			<div class="container tab-content">
                <!-- 基本设置 -->
				<div role="tabpanel" class="tab-pane active" id="home">
					<form class="form-horizontal">
						<div class="form-group">
							<label for="Setting_site_title" class="control-label">云客服平台名称</label>
                            <input class="form-control"  name="entName" id="Setting_site_title" type="text" maxlength="99" value="天翔科技">
                            <p class="help-block">您的云客服平台名称，将显示在标题，头部和邮件中，替换标签{{account.name}}将调用此名称。</p>
						</div>
						<div class="form-group">
							<label class="control-label" for="Setting_site_sub_title">云客服平台描述</label>
								<input class="form-control"  name="entDesc" id="Setting_site_sub_title"
									   type="text" maxlength="99" value="公共测试平台" />
								<p class="help-block">请填写简单的描述，将显示在标题的后面作为云客服平台的描述。</p>
						</div>
						<div class="form-group">
							<label class=" control-label" for="Setting_meta_keyword">SEO关键词</label>
								<input class="form-control" name="seo" id="Setting_meta_keyword" type="text" maxlength="255" value="天翔" />
								<p class="help-block">您的云客服平台的全局关键词，每个关键词请用半角逗号分隔，将放置于前台每页的头部META标签里</p>
						</div>
						<div class="form-group is-empty is-fileinput">
							<label for="Setting_logo" class=" control-label">云客服平台LOGO</label>
                            <input type="text" readonly="" class="form-control" placeholder="请选择文件...">
                            <input type="file" multiple="" name="Setting_logo" id="Setting_logo">
                            <p class="help-block" style="display:inline-block;">建议使用PNG或GIF格式的透明图片，默认LOGO图片高度为70像素</p>
						</div>
						<div class="form-group is-fileinput">
							<label for="Setting_favicon" class=" control-label">Favicon</label>
                            <input type="text" readonly="" class="form-control" placeholder="请选择文件...">
                            <input type="file" name="Setting_favicon" id="Setting_favicon">
                            <p class="help-block" style="display:inline-block;">显示在浏览器地址栏左侧的个性图标，请上传ICO格式的图标，建议大小为16像素*16像素</p>
						</div>

						<div class="form-group modal-footer">
							<button type="button" class="btn btn-raised btn-primary btn-sm" onclick="submit();" href="javascript:void(0);">提交</button>
						</div>
					</form>
				</div>
                <!-- 域名绑定 -->
				<div role="tabpanel" class="tab-pane" id="profile">
					<div class="section-pt">域名绑定</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="<%=request.getContextPath()%>/script/lib/ajaxFileUpload/ajaxupload.3.5.js"></script>
	<script type="text/javascript">
		function selectFile(){
			$("input[name=logo]").click();
		}
		var bg_upload = new AjaxUpload($("#Setting_logo"), {
            action: "<%=request.getContextPath()%>/brandSetting/changeimage",
            name: "image",
            onSubmit: function(file, ext){
                 if (! (ext && /^(jpg|png|jpeg|gif)$/.test(ext))){
                	 notice.alert("仅支持JPG, PNG 或 GIF 格式文件上传");
                    return false;
                }
                $("#logo_image_status").html("上传中...");
            },
            data:{type:"logo"},
            onComplete: function(file, response){
                if(response){
                    var data = eval("("+response+")");
                    if(data.success==true){
                        $("#logo_image_status").html(data.msg);
                        $("#logo_image").html("<img height=\"90\" src=\""+"<%=request.getContextPath()%>/"+data.rows+"\"/>");
                    }else{
                    	notice.alert(data.msg);
                    }
                }
            }
        });
		
		var bg_upload = new AjaxUpload($("#Setting_favicon"), {
            action: "<%=request.getContextPath()%>/brandSetting/changeimage",
            name: "image",
            onSubmit: function(file, ext){
            	if (! (ext && /^(ico)$/.test(ext))){
            		notice.alert("仅支持ICO 格式文件上传");
                    return false;
                }
                $("#favicon_image_status").html("上传中...");
            },
            data:{type:"favicon"},
            onComplete: function(file, response){
                if(response){
                    var data = eval("("+response+")");
                    if(data.success==true){
                        $("#favicon_image_status").html(data.msg);
                        $("#favicon_image").html("<img height=\"90\" src=\""+"<%=request.getContextPath()%>/"+data.rows+"\"/>");
                    }else{
                    	notice.alert(data.msg);
                    }
                }
            }
        });

		/*]]>*/
		
		$(function(){
			$("#Setting_site_title").val("${entName}");
			$("#Setting_site_sub_title").val("${entDesc}");
			$("#Setting_meta_keyword").val("${seo}");
			var logo_image_str = "";
			if("${logoUrl}" !=""){
				logo_image_str += "<img height=\"90\" src=\""+"<%=request.getContextPath()%>/"+"${logoUrl}"+"\"/>";
				logo_image_str += "<p class=\"hint\"><a href=\"javascript:void(0);\" onclick=\"del_img('logo');\"> 删除图片</a></p>";
			}
			$("#logo_image").html(logo_image_str);
			
			var favicon_image_str="";
			if("${faviconUrl}" !=""){
				favicon_image_str += "<img height=\"90\" src=\""+"<%=request.getContextPath()%>/"+"${faviconUrl}"+"\"/>";
				favicon_image_str += "<p class=\"hint\"><a href=\"javascript:void(0);\" onclick=\"del_img('favicon');\"> 删除图片</a></p>";
			}
			$("#favicon_image").html(favicon_image_str);
		});
		
		function del_img(type){
	        if(confirm("确定删除图片？")){
	            $.post("<%=request.getContextPath()%>/brandSetting/delimage/",{type:type},function(data){
	                if(data.success==true){
	                    window.location.reload();
	                }else{
	                    /* showNotice("图片不存在",false); */
	                    notice.alert("图片不存在");
	                }
	            });
	        }
	    }
		
		function submit(){
			var entName = $("#Setting_site_title").val();
			var entDesc = $("#Setting_site_sub_title").val();
			var seo = $("#Setting_meta_keyword").val();
			var param = {entName:entName,entDesc:entDesc,seo:seo};
			$.ajax({
					url:"<%=request.getContextPath()%>/brandSetting/brandSetting",
					type:"post",
					data:param,
					dataType:'json',
					success:function(data){
						submitCallback(data);
					}
				});
		}
		function submitCallback(data){
			if(data.success==true){
                window.location.reload();
            }else{
            	notice.alert(data.msg);
            }
		}
	</script>
</body>
</html>