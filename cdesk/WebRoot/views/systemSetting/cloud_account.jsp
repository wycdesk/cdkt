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
                <li class="active"><a href="<%=request.getContextPath()%>/cloud/account">增值服务</a></li>
                <li><a href="<%=request.getContextPath()%>/cloud/company">公司信息</a></li>
                <li><a href="<%=request.getContextPath()%>/cloud/financial">财务信息</a></li>
                <li><a href="<%=request.getContextPath()%>/cloud/invoice">发票管理</a></li>
                <li><a href="<%=request.getContextPath()%>/cloud/refer">推广计划</a></li>
			</ul>
			</nav>
			<div class="account-box">
				<div class="item list1">
					<a class="fr" href="javascript:;" onclick="useCoupon();">活动优惠码</a>
					<h3>账户余额 (元)</h3>
					<div class="number">20291.20</div>
					<a href="javascript:;" class="lh30" onclick="showOtherPay();">其他充值方式</a>
					<a href="javascript:void(0);" class="btn-sm btn-green fr"
						onclick="showPay();">支付宝充值</a>
				</div>
				<div class="item list2">
					<h3>目前已购业务：</h3>
					<ul>
						<li>当前服务模式：<span>高级PLUS服务模式</span><a href="javascript:;"
							onclick="showUpgrade();"> 升级 </a></li>
						<li>坐席个数：当前10个/最大10个</li>
						<li>服务到期时间： 2017年10月24日 <a href="javascript:;"
							onclick="showFee();">续费</a>
						</li>
					</ul>
					<a href="javascript:;" class="btn-sm btn-green fr"
						onclick="showFee();">续费</a> <a href="javascript:;"
						class="btn-sm btn-green fr" onclick="showUpgrade();">升级服务</a>
				</div>
				<div class="item list3">
					<h3>更多附属服务：</h3>
					<ul>
						<li>电话语音服务<span class="status red">热</span><a
							href="javascript:;" onclick="showVoice();">购买</a></li>
						<li>短信服务<a href="javascript:;" onclick="showSms();">购买</a></li>

					</ul>
				</div>
			</div>
			<header class="kf5-list-header">
			<h3>财务账单</h3>
			</header>
			<table class="table-ui1">
				<thead>
					<tr>
						<th>时间</th>
						<th>入账</th>
						<th>扣费</th>
						<th>余额</th>
						<th>说明备注</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>2015年11月11日 15:26</td>
						<td>￥50.00元</td>
						<td></td>
						<td>￥20291.20元</td>
						<td>活动优惠赠送</td>
					</tr>
					<tr>
						<td>2015年11月09日 15:33</td>
						<td></td>
						<td>￥200.00元</td>
						<td>￥20241.20元</td>
						<td>购买短信：200元</td>
					</tr>
					<tr>
						<td>2015年11月04日 16:06</td>
						<td>￥20000.00元</td>
						<td></td>
						<td>￥20441.20元</td>
						<td>其他</td>
					</tr>
					<tr>
						<td>2015年10月26日 18:37</td>
						<td></td>
						<td>￥12000.00元</td>
						<td>￥441.20元</td>
						<td>由[高级服务模式]升级为[高级PLUS服务模式]</td>
					</tr>
					<tr>
						<td>2015年10月26日 18:36</td>
						<td>￥4000.00元</td>
						<td></td>
						<td>￥12441.20元</td>
						<td>赠送金额</td>
					</tr>
					<tr>
						<td>2015年10月19日 15:27</td>
						<td></td>
						<td>￥21600.00元</td>
						<td>￥8441.20元</td>
						<td>高级服务模式续费：24个月</td>
					</tr>
				</tbody>
			</table>
			<a href="<%=request.getContextPath()%>/cloud/financial" class="btn-sm">查看全部账单</a>

			<div class="light-box-mask kf5_account" style="display: none;">
				<div class="light-box light-box-pay dn">
					<header> <a class="close" href="javascript:void(0);"
						onclick="closeBox();"><i class="icon-cross"></i></a>
					<h3>在线充值</h3>
					</header>
					<div class="light-box-body">
						<div id="pay_operate">
							<div class="field t-col">
								<h4>充值账号</h4>
								<div class="t-col-content">
									<p>天翔企业公司</p>
								</div>
							</div>
							<div class="field t-col">
								<h4>充值金额</h4>
								<div class="t-col-content">
									<input id="total_fee" type="text" name="" placeholder="" />元
								</div>
							</div>
							<div class="field t-col">
								<h4>充值描述</h4>
								<div class="t-col-content">
									<textarea class="mb15" id="body" name=""></textarea>
									<p class="hint">充值的描述，可以留空</p>
								</div>
							</div>
							<div class="clearfix">
								<a href="javascript:;" id="pay_btn" class="btn btn-green fr">支付充值</a>
							</div>
						</div>
						<div id="pay_display" style="display: none;">
							<div class="field t-col">
								<h4>订单编号</h4>
								<div class="t-col-content">
									<p>KF5_1447810460289</p>
								</div>
							</div>
							<div class="field t-col">
								<h4>充值账号</h4>
								<div class="t-col-content">
									<p>天翔企业公司</p>
								</div>
							</div>
							<div class="field t-col">
								<h4>充值金额</h4>
								<div class="t-col-content">
									<span class="amount lh30" id="confirm_amount">￥</span>
								</div>
							</div>
							<div class="clearfix">
								<a class="btn btn-green fr" id="paySubmit" role="submit"
									target="_blank"
									href="/system/alipay?trade_no=KF5_1447810460289">确认充值</a>
							</div>
						</div>
						<div id="paying_confirm" style="display: none;">
							已进入充值支付流程 <a class="btn-sm" href="javascript:;"
								onclick="window.location.reload();" role="button">充值完成</a>
						</div>
					</div>
				</div>
				<div class="light-box light-box-otherpay dn" id="other_payment"
					style="max-width: none;">
					<header> <a class="close" href="javascript:void(0);"
						onclick="closeBox();"><i class="icon-cross"></i></a>
					<h3>其他充值方式</h3>
					</header>
					<div class="light-box-body">
						<header>
						<h4>
							公司银行账户<span>(注：企业付费推荐向对公账号直接转账，处理效率更高，入账更快。)</span>
						</h4>
						</header>
						<table class="table-ui1" style="width: 750px">
							<thead>
								<tr>
									<th>银行种类</th>
									<th>账号名称</th>
									<th>账号</th>
									<th>开户支行</th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>中国建设银行</td>
									<td>成都逸创信息技术有限公司</td>
									<td>5100 1446 4390 5150 3392</td>
									<td>建设银行成都武侯支行</td>
								</tr>
							</tbody>
						</table>
					</div>
				</div>
				<div class="light-box light-box-fee dn" id="renewal">
					<header> <a class="close" href="javascript:void(0);"
						onclick="closeBox();"><i class="icon-cross"></i></a>
					<h3>请选择续费时长</h3>
					</header>
					<div class="light-box-body">
						<div class="plans">
							<div class="variation">
								续费时长： <select id="months">
									<option value="12" selected="selected">1 年</option>
									<option value="24">2 年</option>
									<option value="36">3 年</option>
								</select>
							</div>
						</div>
						<div class="calculate">
							<div class="cal_title">费用计算</div>
							<table>
								<tr>
									<td>客服坐席个数 :</td>
									<td class="cal_digit"><span id="plan_extra_seat">10</span>
										* ￥<span id="plan_per_seat_price">140</span> / 月</td>
								</tr>
								<tr>
									<td>计算公式【客服座席个数 * 单价 * 月】 :</td>
									<td class="cal_digit"><span id="plan_formula"></span></td>
								</tr>
								<tr>
									<td class="total">续费费用合计 :</td>
									<td class="total cal_digit">￥<span id="plan_total"
										class="red b"></span></td>
								</tr>
							</table>
						</div>
						<div class="operation-main">
							<input type="button" id="btn_renewals" class="btn btn-green fr"
								value="续费套餐服务" role="submit">
						</div>
					</div>
				</div>
				<div class="light-box light-box-upgrade dn" id="upgrade">
					<header> <a class="close" href="javascript:void(0);"
						onclick="closeBox();"><i class="icon-cross"></i></a>
					<h3>升级服务模式</h3>
					</header>
					<div class="light-box-body">
						<div class="plans">
							<div class="plan_tabs">
								<div class="p_item">
									<dl>
										<div class="choose">
											<a class="plan_select" id="plan_12" data-id="12">选择</a>
										</div>
										<div class="plan_detail rx-f14">高级PLUS服务模式 ￥1680 / 客服坐席
											/ 年</div>
									</dl>
								</div>
							</div>
							<div style="margin-bottom: 20px">
								<a class="btn-sm" target="_blank"
									href="http://www.kf5.com/product/pricing" role="button">查看服务模式对比</a>
							</div>
							<div class="variation" id="extra_seat_variation"
								style="display: none">
								增加座席个数： <select name="extra_seat" id="extra_seat">
									<option>0</option>
									<option>1</option>
									<option>2</option>
									<option>3</option>
									<option>4</option>
									<option>5</option>
									<option>6</option>
									<option>7</option>
									<option>8</option>
									<option>9</option>
									<option>10</option>
									<option>11</option>
									<option>12</option>
									<option>13</option>
									<option>14</option>
									<option>15</option>
									<option>16</option>
									<option>17</option>
									<option>18</option>
									<option>19</option>
									<option>20</option>
									<option>21</option>
									<option>22</option>
									<option>23</option>
									<option>24</option>
									<option>25</option>
									<option>26</option>
									<option>27</option>
									<option>28</option>
									<option>29</option>
									<option>30</option>
									<option>31</option>
									<option>32</option>
									<option>33</option>
									<option>34</option>
									<option>35</option>
									<option>36</option>
									<option>37</option>
									<option>38</option>
									<option>39</option>
									<option>40</option>
									<option>41</option>
									<option>42</option>
									<option>43</option>
									<option>44</option>
									<option>45</option>
									<option>46</option>
									<option>47</option>
									<option>48</option>
									<option>49</option>
									<option>50</option>
									<option>51</option>
									<option>52</option>
									<option>53</option>
									<option>54</option>
									<option>55</option>
									<option>56</option>
									<option>57</option>
									<option>58</option>
									<option>59</option>
									<option>60</option>
									<option>61</option>
									<option>62</option>
									<option>63</option>
									<option>64</option>
									<option>65</option>
									<option>66</option>
									<option>67</option>
									<option>68</option>
									<option>69</option>
									<option>70</option>
									<option>71</option>
									<option>72</option>
									<option>73</option>
									<option>74</option>
									<option>75</option>
									<option>76</option>
									<option>77</option>
									<option>78</option>
									<option>79</option>
									<option>80</option>
									<option>81</option>
									<option>82</option>
									<option>83</option>
									<option>84</option>
									<option>85</option>
									<option>86</option>
									<option>87</option>
									<option>88</option>
									<option>89</option>
									<option>90</option>
									<option>91</option>
									<option>92</option>
									<option>93</option>
									<option>94</option>
									<option>95</option>
									<option>96</option>
									<option>97</option>
									<option>98</option>
									<option>99</option>
									<option>100</option>
									<option>200</option>
									<option>300</option>
									<option>500</option>
								</select>
							</div>
						</div>
						<div class="calculate" style="display: none">
							<div class="cal_title">费用计算</div>
							<div id="change_plan_info"></div>
						</div>
						<div class="operation-main">
							<input type="button" class="btn btn-green fr"
								id="btn_change_plan" value="升级" style="display: none"
								role="submit">
						</div>
					</div>
				</div>
				<div class="light-box light-box-voice dn" id="buyvoice">
					<header> <a class="close" href="javascript:void(0);"
						onclick="closeBox();"><i class="icon-cross"></i></a>
					<h3>语音电话服务</h3>
					</header>
					<div class="light-box-body">
						<div class="alert alert-warning">
							请选择要购买的话费额度，它将从您的账户余额中扣除作为您电话语音服务的通话费用。<br>购买后请在 <span
								class="b">支持渠道 > 电话语音 > 计费信息</span> 里查看。
						</div>
						<div class="plan_tabs" style="margin-bottom: 0px;">
							<div class="p_item">
								<dl>
									<div class="choose">
										<a class="plan_select voice_plan_300"
											onclick="voice_plan_select(300);">选择</a>
									</div>
									<div class="plan_detail rx-f14">
										<span class="plan_text">￥ 300</span>适合测试或微型团队的电话语音服务
									</div>
								</dl>
								<dl>
									<div class="choose">
										<a class="plan_select voice_plan_600"
											onclick="voice_plan_select(600);">选择</a>
									</div>
									<div class="plan_detail rx-f14">
										<span class="plan_text">￥ 600</span>适合中小型企业的电话语音支持服务
									</div>
								</dl>
								<dl>
									<div class="choose">
										<a class="plan_select voice_plan_1200"
											onclick="voice_plan_select(1200);">选择</a>
									</div>
									<div class="plan_detail rx-f14">
										<span class="plan_text">￥ 1200</span>适合中型企业的电话语音、呼叫中心服务
									</div>
								</dl>
								<dl>
									<div class="choose">
										<a class="plan_select voice_plan_6000"
											onclick="voice_plan_select(6000);">选择</a>
									</div>
									<div class="plan_detail rx-f14">
										<span class="plan_text">￥ 6000</span>适合大型企业的或大型服务规模的呼叫中心服务
									</div>
								</dl>
								<div class="operation-main">
									<input class="btn btn-green fr" onclick="buy_voice();"
										type="button" value="提交购买" role="submit">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="light-box light-box-sms dn" id="buysms">
					<header> <a class="close" href="javascript:void(0);"
						onclick="closeBox();"><i class="icon-cross"></i></a>
					<h3>短信服务</h3>
					</header>
					<div class="light-box-body">
						<div class="alert alert-warning">
							请选择要购买的短信条数，购买后费用将从您的账户余额中扣除。<br>购买后请在 <span class="b">支持渠道
								> 短信提醒设置 > 短信统计</span> 里查看。
						</div>
						<div class="plan_tabs" style="margin-bottom: 0px;">
							<div class="p_item">
								<dl>
									<div class="choose">
										<a class="plan_select sms_plan_200"
											onclick="sms_plan_select(200);">选择</a>
									</div>
									<div class="plan_detail rx-f14">
										<span class="plan_text">￥200</span> 2000条
									</div>
								</dl>
								<dl>
									<div class="choose">
										<a class="plan_select sms_plan_500"
											onclick="sms_plan_select(500);">选择</a>
									</div>
									<div class="plan_detail rx-f14">
										<span class="plan_text">￥500</span> 6000条
									</div>
								</dl>
								<dl>
									<div class="choose">
										<a class="plan_select sms_plan_1000"
											onclick="sms_plan_select(1000);">选择</a>
									</div>
									<div class="plan_detail rx-f14">
										<span class="plan_text">￥1000</span> 14000条
									</div>
								</dl>
								<dl>
									<div class="choose">
										<a class="plan_select sms_plan_5000"
											onclick="sms_plan_select(5000);">选择</a>
									</div>
									<div class="plan_detail rx-f14">
										<span class="plan_text">￥5000</span> 75000条
									</div>
								</dl>
								<div class="operation-main">
									<input class="btn btn-green fr" onclick="buy_sms();"
										type="button" value="提交购买" role="submit">
								</div>
							</div>
						</div>
					</div>
				</div>
				<div class="light-box light-box-coupon dn">
					<header> <a class="close" href="javascript:void(0);"
						onclick="closeBox();"><i class="icon-cross"></i></a>
					<h3>活动优惠码兑换金额</h3>
					</header>
					<div class="light-box-body">
						<div id="pay_operate">
							<div class="field t-col">
								<h4>优惠码</h4>
								<div class="t-col-content">
									<input id="coupon_key" type="text" name="" placeholder="" />
									<p class="hint">请输入优惠码，提交后会立即兑换金额充入您的账户</p>
								</div>
							</div>
							<div class="clearfix">
								<a href="javascript:;" id="coupon_submit"
									class="btn btn-green fr">提交</a>
							</div>
						</div>
					</div>
				</div>

			</div>
		</div>
	</div>
	<script type="text/javascript" src="/site/page_stat/"></script>
	<script type="text/javascript">
/*<![CDATA[*/

	function getTotalFee(){
		return $("#total_fee").val();
	}

	$("#pay_btn").click(function(){
		var reg	= new RegExp(/^\d*\.?\d{0,2}$/);
		var fee = getTotalFee();
		if (!reg.test(fee) || fee.length==0 || parseInt(fee)<=0){
	        $("#total_fee").val("请输入付款金额").css({"color":"#c00"});
	        $("#pay_display").hide();
			$("#pay_operate").show();
		}else{
			//save payment
			var values = {};
			values.out_trade_no = "KF5_1447810460289";
			values.total_fee = parseInt(getTotalFee());
			values.body =  $("#body").val();
			//console.log(values);return false;
			$.post("/system/payment",values,function(data){
				if(data=="success"){
					$("#title_show").html("确认账户充值");
					$("#pay_display").show();
					$("#pay_operate").hide();
					$("#confirm_amount").html("￥"+fee);
				}else{
					KF5.alert("系统繁忙，请稍后再试！");
				}
			});
		}
	});

	$("#total_fee").focus(function(){
		if($(this).val()!="") $(this).val("").css("color","#555555");
	});

	$("#paySubmit").click(function(){
		$("#pay_display").hide();
		$("#paying_confirm").show();
	});


    $("#months").change(function(){
        var months = $(this).val();
        var plan_extra_seat = $("#plan_extra_seat").text();
        var plan_per_seat_price = $("#plan_per_seat_price").text();
        var total = 0;
        var total = plan_extra_seat * plan_per_seat_price * months;
        if(total>0) $("#btn_renewals").show();
        else $("#btn_renewals").hide();
        $("#plan_formula").text(plan_extra_seat+" * "+" ￥"+plan_per_seat_price+" * "+months);
        $("#plan_total").text(total);
    });
    $("#months").change();
    $("#btn_renewals").click(function(){
        $(this).attr("disabled","disabled");
        var total = $("#plan_total").text();
        var text = "您需要支付￥{total}元,确定续费吗？";
        if(confirm(text.replace(/{total}/g,total))){
            $.post("/system/ajax/",{method: "renewals",months:$("#months").val(),total:$("#plan_total").text()},function(data){
                if(data=="success"){
                    KF5.alert("续费成功！");
                    location.reload();
                }else{
                    KF5.alert("续费失败！"+data);
                    $(this).removeAttr("disabled");
                }
            });
        }else
            $(this).removeAttr("disabled");
    });


    $("#upgrade .plan_select").click(function(){
        $("#upgrade .plan_select").removeClass("selected");
        $(this).addClass("selected");
        change_plan($(this).attr("data-id"));
    });
    $("#extra_seat").change(function(){
        if($("#upgrade .p_item .selected").length > 0){
            change_plan($("#upgrade .p_item .selected").attr("data-id"));
        }
    });
    $("#btn_change_plan").click(function(){
        $(this).attr("disabled","disabled");
        var text = "升级套餐需要支付￥{total}元,确定升级吗？";
        var total = $("#change_plan_total").text();
        if(confirm(text.replace(/{total}/g,total))){
            $.post("/system/ajax/",{method:"change_plan",upgrade:true,plan_id:plan_id,extra_seat:$("#extra_seat").val(),radom:Math.random()},function(data){
                if(data == "success"){
                    KF5.alert("升级成功！");
                    parent.location.reload();
                }else{
                    KF5.alert(data);
                    $(this).removeAttr("disabled");
                }
            });
        }else
            $(this).removeAttr("disabled");
    });
    var plan_id;
    function change_plan(id){
        plan_id = id;
        $.post("/system/ajax/",{method:"change_plan",plan_id:id,extra_seat:$("#extra_seat").val(),radom:Math.random()},function(data){
            $(".calculate,#extra_seat_variation").show();
            $("#change_plan_info").html(data);
            if($("#change_plan_total").length > 0 && $("#change_plan_total").text()!="0"){
                $("#btn_change_plan").show();
            }else{
                $("#btn_change_plan").hide();
            }
        });
    }


    var voice_plan_id;
    voice_plan_select(300);
    function voice_plan_select(id)
    {
        voice_plan_id = id;
        $("#buyvoice .plan_select").removeClass("selected");
        $("#buyvoice .voice_plan_"+id).addClass("selected");
    }
    function buy_voice()
    {
        if(confirm("购买语音套餐将会花费 ￥"+voice_plan_id+"，确认支付？"))
        {
            $.post("/system/ajax/",{method:"buy_voice",price:voice_plan_id},
                function(data){
                    if(data=="success"){
                        closeBox();
                        KF5.alert("购买成功！");
                    }else{
                        KF5.alert(data);
                    }
                }
            );
        }
    }


    var sms_plan_id;
    sms_plan_select(200);
    function sms_plan_select(id)
    {
        sms_plan_id = id;
        $("#buysms .plan_select").removeClass("selected");
        $("#buysms .sms_plan_"+id).addClass("selected");
    }
    function buy_sms()
    {
        if(confirm("购买短信套餐将会支付 ￥"+sms_plan_id+"，确认支付？"))
        {
            $.post("/system/ajax/",{method:"buy_sms",price:sms_plan_id},
                function(data){
                    if(data=="success"){
                        closeBox();
                        KF5.alert("购买成功！");
                    }else{
                        KF5.alert(data);
                    }
                }
            );
        }
    }



    //提交URL
    var couponSubmitUrl="/system/coupon/";

    /**
     * 返回结果：
     * 成功：{"err":0}
     * 失败：{"err":1, "msg":"...."}
     */

    //提交操作
    $("#coupon_submit").click(function(){
        var couponKey=$("#coupon_key").val();
        var submitIng=false;
        if(couponKey){
            if(!submitIng){
                submitIng=true;
                $.post(couponSubmitUrl,{key:couponKey},function(res){
                    if(res.err==0){
                        //请求成功，刷新页面
                        window.location.reload();
                    }else if(res.err==1){
                        //请求失败，显示错误信息
                        KF5.alert(res.msg);
                    }
                    submitIng=false;
                },"json");
				
            }
        }else{
            KF5.alert("请填写优惠码");
        }
    });



    function showPay(){
        $(".light-box-mask").show();
        $("#pay_display,#paying_confirm").hide();
        $("#pay_operate").show();
        $(".light-box-pay").removeClass("dn");
    }
    function showOtherPay(){
        $(".light-box-mask").show();
        $(".light-box-otherpay").removeClass("dn");
    }
    function showUpgrade(){
        $(".light-box-mask").show();
        $(".light-box-upgrade").removeClass("dn");
    }
    function showFee(){
        $(".light-box-mask").show();
        $(".light-box-fee").removeClass("dn");
    }
    function showVoice(){
        $(".light-box-mask").show();
        $(".light-box-voice").removeClass("dn");
    }
    function showSms(){
        $(".light-box-mask").show();
        $(".light-box-sms").removeClass("dn");
    }
    function closeBox(){
        $(".light-box").addClass("dn");
        $(".light-box-mask").hide();
    }
    function cancelBox(){
        closeBox();
    }
    function useCoupon(){
        $(".light-box-mask").show();
        $(".light-box-coupon").removeClass("dn");
    }


            $(".panel-frame").perfectScrollbar();
            $(".light-box-body").perfectScrollbar();
            //触发器，自动化小提示
            tip();
        
/*]]>*/
</script>
</body>
</html>