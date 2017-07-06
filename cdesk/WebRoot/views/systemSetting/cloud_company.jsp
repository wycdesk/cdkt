<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/views/include/pageHeader.jsp"%>
<script src="<%=request.getContextPath()%>/script/lib/PCASClass.js" charset="gb2312"></script>
<link rel="stylesheet" type="text/css"href="<%=request.getContextPath()%>/static/css/systemSetting/all.css">
<style type="text/css">
.field-operation [type="button"]{height: 34px;line-height: 34px; background: #fff; font-size: 14px; border-radius: 3px; padding: 0 12px; display: inline-block; border: 1px solid #e9eaed; color: #374051; background: #21d376; color: #fff; border-color: #21d376; float:right; cursor:pointer;}
</style>
<title>企业信息</title>
</head>
<body class="page">
    <div class="panel-frame">
        <div class="panel-wrap">
            <h2>企业信息</h2>
            <!-- <nav class="nav-tabs">
                <ul>

                    <li class="active"><a href="<%=request.getContextPath()%>/cloud/company">企业信息</a></li>

                    <li><a href="<%=request.getContextPath()%>/cloud/account">增值服务</a></li>
                    <li><a href="<%=request.getContextPath()%>/cloud/financial">财务信息</a></li>
                    <li><a href="<%=request.getContextPath()%>/cloud/invoice">发票管理</a></li>
                    <li><a href="<%=request.getContextPath()%>/cloud/refer">推广计划</a></li>

                </ul>
            </nav> -->
            <form id="yw0" action="#" method="post">
                <div style="display:none"> </div>
                <div class="alert alert-warning">请如实填写以下信息，它将作为我们向您提供服务的重要依据，我们务必会对您的资料保密。</div>
                <input name="entId" type="hidden" value="${entVo.entId}" />
                <div class="kf5-section">
                    <div class="field t-col">
                        <h4 class="ln"><label for="Company_name">公司名称</label></h4>
                        <div class="t-col-content">
                            <input name="entName" id="Company_name" type="text" value="${entVo.entName}" />
                        </div>
                    </div>
                    <div class="field t-col">
                        <h4 class="ln"><label for="Company_industry">行业</label></h4>
                        <div class="t-col-content">
                            <select name="industry" id="Company_industry">
                                <option value="0">--请选择行业--</option>
                                <option value="1">教育</option>
                                <option value="2">娱乐/艺术</option>
                                <option value="3">金融/保险</option>
                                <option value="4">医疗/保健</option>
                                <option value="5">制造业</option>
                                <option value="6">电讯/媒体</option>
                                <option value="7">广告/市场</option>
                                <option value="8">小型企业支持</option>
                                <option value="9">咨询/顾问</option>
                                <option value="10">房地产</option>
                                <option value="11">零售业</option>
                                <option value="12">互联网/IT</option>
                                <option value="13">旅游业</option>
                                <option value="14">公共事业</option>
                                <option value="15">非盈利组织</option>
                                <option value="16">其他</option>
                            </select>
                        </div>
                    </div>
                    <div class="field t-col">
                        <h4 class="ln"><label for="Company_scale">规模</label></h4>
                        <div class="t-col-content">
                            <select name="scale" id="Company_scale">
                                <option value="1">5人以下</option>
                                <option value="2">5-19人</option>
                                <option value="3">20-99人</option>
                                <option value="4">100-499人</option>
                                <option value="5">500人以上</option>
                            </select>
                        </div>
                    </div>
                    <div class="field t-col">
                        <h4 class="ln"><label for="Company_admin_name">联系人</label></h4>
                        <div class="t-col-content">
                            <input name="contactUser" id="Company_admin_name" type="text" value="${entVo.contactUser}" />
                            <p class="hint">请填写真实的联系人姓名，以便我们必要时能及时联系你</p>
                        </div>
                    </div>
                    <div class="field t-col">
                        <h4 class="ln"><label for="Company_province">省</label>/<label for="Company_city">市</label>/<label for="Company_area">区</label></h4>
                        <div class="t-col-content">
                            <select name="province" id="Company_province"></select>
                            <select name="city" id="Company_city"></select>
                            <select name="area" id="Company_area"></select>
                        </div>
                    </div>
                    <div class="field t-col">
                        <h4 class="ln"><label for="Company_address">地址</label></h4>
                        <div class="t-col-content">
                            <input name="address" id="Company_address" type="text" value="${entVo.address}" />
                            <p class="hint">请精确至街道，方便我们邮寄发票或礼品</p>
                        </div>
                    </div>
                    <div class="field t-col">
                        <h4 class="ln"><label for="Company_mailcode">邮政编码</label></h4>
                        <div class="t-col-content">
                            <input class="text small" name="zipCode" id="Company_mailcode" type="text" value="${entVo.zipCode}" />
                        </div>
                    </div>
                    <div class="field field-operation">
                        <input class="btn btn-green" type="button" name="yt0" value="提交" onclick="onSubmit();" />
                    </div>
                </div>
            </form>
        </div>
    </div>
<script type="text/javascript">

$(function(){
	$("#Company_industry").val('${entVo.industry}');
	$("#Company_scale").val('${entVo.scale}');
	new PCAS("province","city","area","${entVo.province}","${entVo.city}","${entVo.area}");
});

function onSubmit() {
	$.post("<%=request.getContextPath()%>/ent/update", $("#yw0").serialize(), updateCallBack, 'json');
}

function updateCallBack(data) {
	if (data.success) {
		notice.alert(data.msg);
		window.location.href = '<%=request.getContextPath()%>/cloud/company';
	} else {
		notice.alert(data.msg,"alert-danger");
	}
}
</script>
</body>
</html>