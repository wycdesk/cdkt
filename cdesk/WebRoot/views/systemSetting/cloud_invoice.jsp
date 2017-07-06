<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<%@include file="/views/include/pageHeader.jsp"%>
<link rel="stylesheet" type="text/css"href="<%=request.getContextPath()%>/static/css/systemSetting/all.css">
<title>云客服帐号</title>
</head>
<body class="page">
    <div class="panel-frame">
                <div class="panel-wrap">
            <h2>云客服账号</h2>
<nav class="nav-tabs">
    <ul>
        <li><a href="<%=request.getContextPath()%>/cloud/account">增值服务</a></li>
        <li><a href="<%=request.getContextPath()%>/cloud/company">公司信息</a></li>
        <li><a href="<%=request.getContextPath()%>/cloud/financial">财务信息</a></li>
        <li class="active"><a href="<%=request.getContextPath()%>/cloud/invoice">发票管理</a></li>
        <li><a href="<%=request.getContextPath()%>/cloud/refer">推广计划</a></li>
    </ul>
</nav>
<header class="kf5-list-header">
    <div class="operation-secondary">
        <span class="menu-text">可开票额度 <span id="invoice_balance">502.00</span>元</span>
        <span class="menu-options">
            <input id="invoice_value_expect" class="ember-view ember-text-field text" type="text" placeholder="请填写开票金额">
        </span>
        <span class="menu-btn">
            <a class="btn-sm green" role="submit" onclick="add_invoice();">开具发票</a>
        </span>
    </div>
</header>
<table class="table-ui1">
    <thead>
        <tr>
            <th>发票金额</th>
            <th>发票抬头</th>
            <th style="max-width:30%">寄送地址</th>
            <th>申请时间</th>
            <th>状态</th>
            <th>快递/单号</th>
            <th>操作</th>
        </tr>
    </thead>
    <tbody>
                                    <tr>
                    <td>300.00</td>
                    <td>sofofosdfiouoi</td>
                    <td>sdfsdfdsfosdouif</td>
                    <td>2015年07月08日 11:37</td>
                                            <td>等待处理</td>
                                        <td></td>
                    <td>
                                                    <a onclick="invoice_operation_edit(40);">编辑</a>
                                            </td>
                </tr>
                            <tr>
                    <td>100.00</td>
                    <td>测试数据</td>
                    <td>测试数据</td>
                    <td>2015年06月15日 11:42</td>
                                            <td>受理中</td>
                                        <td>12343333</td>
                    <td>
                                            </td>
                </tr>
                        </tbody>
</table>
<div class="pager">
    </div>

<div id="invoice_poplayer" class="light-box-mask" style="display:none;">
    <div class="light-box">
        <header>
            <a class="close" href="javascript:void(0);" onclick="$('.light-box-mask').hide();"><i class="icon-cross"></i></a>
            <h3>发票开具信息</h3>
        </header>
        <div class="light-box-body" style="height:465px;">
            <div class="field t-col" style="display:none;">
                <h4>票据ID</h4>
                <div class="t-col-content">
                    <input id="invoice_id" type="text" class="text" value="" />
                </div>
            </div>
            <div class="field t-col">
                <h4>开票金额</h4>
                <div class="t-col-content">
                    <p><span id="dis_invoice_value"></span> 元</p>
                </div>
            </div>
            <div class="field t-col" style="display:none;">
                <h4>开票金额</h4>
                <div class="t-col-content">
                    <input id="invoice_value" type="text" class="text" value="" />
                </div>
            </div>
            <div class="field t-col">
                <h4>发票抬头</h4>
                <div class="t-col-content">
                    <input id="invoice_title" type="text" class="text" value="" />
                    <p class="hint">请填写发票抬头全称，如果发票已经寄出，则无法更改发票抬头</p>
                </div>
            </div>
            <div class="field t-col">
                <h4>寄送地址</h4>
                <div class="t-col-content">
                    <input id="invoice_address" type="text" class="text" value="" />
                </div>
            </div>
            <div class="field t-col">
                <h4>邮编</h4>
                <div class="t-col-content">
                    <input id="invoice_postal_code" type="text" class="text small" value="" />
                </div>
            </div>
            <div class="field t-col">
                <h4>联系人</h4>
                <div class="t-col-content">
                    <input id="invoice_name" type="text" class="text" value="" />
                </div>
            </div>
            <div class="field t-col">
                <h4>联系号码</h4>
                <div class="t-col-content">
                    <input id="invoice_phone" type="text" class="text" value="" />
                </div>
            </div>
            <div class="field t-col">
                <h4>纳税人识别号</h4>
                <div class="t-col-content">
                    <input id="invoice_tax_identity" type="text" class="text" value="" />
                    <p class="hint">税务系统分配的纳税人识别号码，仅供开票用</p>
                </div>
            </div>
            <div class="field t-col">
                <h4>注册地址</h4>
                <div class="t-col-content">
                    <input id="invoice_tax_address" type="text" class="text" value="" />
                    <p class="hint">税务注册时登记的公司地址，仅供开票用</p>
                </div>
            </div>
            <div class="field t-col">
                <h4>注册电话</h4>
                <div class="t-col-content">
                    <input id="invoice_tax_contact" type="text" class="text" value="" />
                    <p class="hint">税务注册时登记的电话号码，仅供开票用</p>
                </div>
            </div>
            <div class="field t-col">
                <h4>开户行</h4>
                <div class="t-col-content">
                    <input id="invoice_tax_bank" type="text" class="text" value="" />
                    <p class="hint">开户银行名称，仅供开票用</p>
                </div>
            </div>
            <div class="field t-col">
                <h4>账号</h4>
                <div class="t-col-content">
                    <input id="invoice_tax_bank_account" type="text" class="text" value="" />
                    <p class="hint">开户银行账号，仅供开票用</p>
                </div>
            </div>
        </div>
        <div class="light-box-footer clearfix">
            <input class="btn btn-green fr" id="edit_invoice_submit_btn" type="submit" value="提交">
        </div>
    </div>
</div>

        </div>
    </div>
    <script type="text/javascript" src="/site/page_stat/"></script>
<script type="text/javascript">
/*<![CDATA[*/


	function add_invoice(){
		var quota = parseInt($("#invoice_balance").text());
		var commit = parseInt($("#invoice_value_expect").val());
		if(isNaN(quota) || quota<300){
			KF5.alert("您的平台暂时无法使用开票服务");
			return false;
		}
		if(!commit){
			KF5.alert("请输入开票金额");
			return false;
		}
		if(commit < 300){
			KF5.alert("最低开票金额为 300元");
			return false;
		}
		if(commit > parseInt($("#invoice_balance").html())){
			KF5.alert("超过可开发票金额");
			return false;
		}
		clearInvoiceData($("#invoice_poplayer input[type!=submit]"));
		$("#invoice_value_area").hide();
		$("#dis_invoice_value_area").show();
		$("#invoice_value").attr("value", commit);
		$("#dis_invoice_value").text($("#invoice_value").val());
		$("#invoice_poplayer").show();
	}

	function invoice_operation_edit(id){
		var method = "get_invoice";
		$.post("/system/ajax/"+"id/"+id, {method:method}, function(data){
			if(data.message == "success"){
				setInvoiceData($("#invoice_poplayer input[type!=submit]"), data.datas);
				$("#invoice_poplayer").show();
			}
			else KF5.alert(data.error);
		},"json");
	}

	$("#edit_invoice_submit_btn").click(function(){
		var datas = null;
		datas = getInvoiceData($("#invoice_poplayer input[type!=submit]"));
		if(datas){
			var method = "set_invoice";
			$.post("/system/ajax/", {method:method,invoice:datas}, function(data){
				if(data.message == "success") window.location.reload();
				else KF5.alert(data.error);
			},"json");
		}
		return false;
	});

	function getInvoiceData(elements){
		var obj = {};
		$.each(elements, function(i,ele){
			var indexname = ele.id.substr(8);
	  		obj[indexname] = $(this).val();
		});
		return JSON.stringify(obj);
	}

	function setInvoiceData(elements, data){
		$.each(elements, function(i,ele){
			var indexname = ele.id.substr(8);
			$(this).attr("value", data[indexname]);
		});
		$("#dis_invoice_value").text(data["value"]);
		$("#invoice_value_area").show();
		$("#dis_invoice_value_area").hide();
	}

	function clearInvoiceData(elements){
		$.each(elements, function(){
			$(this).attr("value", "");
		});
	}



            $(".panel-frame").perfectScrollbar();
            $(".light-box-body").perfectScrollbar();
            //触发器，自动化小提示
            tip();
        
/*]]>*/
</script>
</body>
</html>