<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>数据统计</title>
<%@include file="/views/include/pageHeader.jsp"%>
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/dataStatistics.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/dropzone/dropzone.css" />
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
<link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
</head>
<body>
<header class="header"> 数据统计 </header>
<div class="container-fluid main">

	<!-- 数据统计类型选择列表 -->
	<div class="row">
		<div class="col-sm-12">
			<div class="navbar navbar-default">
				<ul class="nav navbar-nav" id="menu">
					<li class="active"><a href="#overview" data-toggle="tab">概览</a></li>
					<li><a href="#workStatistic" data-toggle="tab">工单统计</a></li>
					<li><a href="#performance" data-toggle="tab">坐席统计</a></li>
				</ul>
			</div>
		</div>
	</div>
	<!-- 统计时间选择列表-->
	<div class="row">
		<div class="col-sm-12">
			<div class="search-bar">
				统计时间段:
				<div class="btn-group time" role="group" aria-label="...">
					<!-- <button type="button"
						class="btn btn-sm btn-outline active btn-primary">近一周</button> -->
					<button type="button"
						class="btn btn-sm btn-outline active">近一周</button>
					<button type="button" class="btn btn-sm btn-outline">近一个月</button>
					<button type="button" class="btn btn-sm btn-outline">近三个月</button>
					<!-- <button     type="button" class="btn btn-sm btn-outline">自定义</button> -->
				</div>
			</div>
		</div>
	</div>
	
	<!-- 统计内容：概览、工单统计、坐席统计 -->
	<div id="myTabContent" class="tab-content">
		<!--   概览      -->
		<div class="tab-pane fade in active" id="overview">
			<div class="panel-heading">概览</div>
			<div class="panel-body">
				<!-- 概览列表 -->
				<nav class="panel-number-btn">
					<ul id="queryButtonsOverview">
						<li data-querytype="00" class="ember-view active"><a><span
								class="red" id="customerServed">0</span>
							<p>服务客户数</p></a></li>
						<li data-querytype="5" class="ember-view"><a><span
								class="blue" id="telcount">0</span>
							<p>电话数量</p></a></li>
						<li data-querytype="7" class="ember-view"><a><span
								class="green" id="wechatcount">0</span>
							<p>微信数量</p></a></li>
						<li data-querytype="1" class="ember-view"><a><span
								class="orange" id="IMcount">0</span>
							<p>IM数量</p></a></li>
						<li data-querytype="8" class="ember-view"><a><span
								class="purple" id="videocount">0</span>
							<p>视频数量</p></a></li>
						<li data-querytype="11" class="ember-view"><a><span
								class="purple" id="workcount">0</span>
							<p>工单数量</p></a></li>
					</ul>
				</nav>
				<div id="chart-0"></div>
			</div>
			<!-- 概览的三个统计扇形图 -->
			<div id="charts" class="row">
				<div class="col-sm-4">
					<div class="panel panel-default">
						<div class="panel-heading">支持渠道</div>
						<div class="panel-body">
							<div id="chart-2"></div>
						</div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="panel panel-default">
						<div class="panel-heading">工单状态</div>
						<div class="panel-body">
							<div id="chart-3"></div>
						</div>
					</div>
				</div>
				<div class="col-sm-4">
					<div class="panel panel-default">
						<div class="panel-heading">新建客户/服务客户数比例</div>
						<div class="panel-body">
							<div id="chart-4"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 工单统计 -->
		<div class="tab-pane fade in" id="workStatistic">
			<div class="panel-heading">工单统计</div>
			<div class="panel-body">
				<!-- 工单统计列表 -->
				<nav class="panel-number-btn">
					<ul id="queryButtons">
						<li data-querytype="1" class="ember-view active"><a><span
								class="red" id="created">-</span>
							<p>新工单数</p></a></li>
						<li data-querytype="2" class="ember-view"><a><span
								class="blue" id="solved">-</span>
							<p>已解决的工单总数</p></a></li>
						<li data-querytype="3" class="ember-view"><a><span
								class="green" id="average_respond_time">-</span>
							<p>响应时间（小时）平均</p></a></li>
						<li data-querytype="4" class="ember-view"><a><span
								class="orange" id="average_solve_time">-</span>
							<p>解决时间（小时）平均</p></a></li>
					</ul>
				</nav>
				<!-- 工单统计折线图 -->
				<div>
					<div id="chart-1"></div>
				</div>
			</div>
		</div>

		<!--   坐席统计 -->
		<div class="tab-pane fade" id="performance">
			<div class="row">
				<div class="col-sm-12">
					<div class="panel panel-default">
						<div class="panel-heading">坐席统计
							<div class="btn-group fr">
								<a type="button" data-querytype="1" class="btn btn-sm active">按客服查看</a>
								<a type="button" data-querytype="2" class="btn btn-sm btn-br">按客服组查看</a>
							</div>
						</div>
						<!-- 坐席统计表格 -->
						<div class="panel-body">
							<div class="row table-content">
								<div class="col-12 grid">
									<table class="table" cellspacing="0" cellpadding="0"
										id="serviceTable">
										<thead>
											<tr class="order">
												<th>排名</th>
												<th id="serviceType">客服职员</th>
												<!-- <th>客服组</th> -->
												<th>解决工单</th>
												<th>未解决数</th>
												<th>服务量</th>
												<th>电话数</th>
												<th>Webchat数</th>
												<th>微信数</th>
											</tr>
										</thead>
										<tbody></tbody>
									</table>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</div>
<script src="<%=request.getContextPath()%>/script/lib/Highcharts/js/highcharts.js" type="text/javascript"></script>

<!-- 坐席统计模板 -->
<script id="table-tr-performance-template" type="text/x-handlebars-template">
 <tr class="ember-view">
        <td>{{order}}</td>
 	    
		{{#if customServiceName}}
			<td>
				{{customServiceName}}
			</td>
		{{/if}} 
		{{#if serviceGroupName}}
			<td>
				{{serviceGroupName}}
			</td>
		{{/if}} 
		<td>
			{{#if solved}}
				{{solved}}
			{{else}}
				<span>0</span>
			{{/if}} 
		</td>
		<td>
			{{#if unsolved}}
				{{unsolved}}
			{{else}}
				<span>0</span>
			{{/if}} 
		</td>
		<td>
			{{#if servNum}}
				{{servNum}}
			{{else}}
				<span>0</span>
			{{/if}} 
		</td>
		<td>
			{{#if source5}}
				{{source5}}
			{{else}}
				<span>0</span>
			{{/if}} 
		</td>
		<td>
			{{#if source1}}
				{{source1}}
			{{else}}
				<span>0</span>
			{{/if}} 
		</td>
		<td>
			{{#if source7}}
				{{source7}}
			{{else}}
				<span>0</span>
			{{/if}} 
		</td>
    </tr>
</script>
<script type="text/javascript">
	//定义全局变量，保存后台返回的数据
var  resultDate;
	//保存当前活动的导航栏
var activeTab="概览";

$(function(){
	//时间变量初始化
	var now = new Date();
	var beforeWeek = new Date(now.getTime() - 7*24*3600*1000);
	var beforeMonth = new Date(now.getTime() - 30*24*3600*1000);
	var beforeThreeMonth = new Date(now.getTime() - 3*30*24*3600*1000);
       
	//导航栏的单击事件
	$('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
	    // 获取已激活的标签页的名称
	    activeTab = $(e.target).text();
	    
	    if("坐席统计"==activeTab){
	    	$(".btn-group.fr").data("querytype","1");
	    	$(".btn-group.fr a").siblings().removeClass("active");
	    	$(".btn-group.fr a").first().addClass("active");
	    	$("#serviceType").text("客服职员");
	    	var params={
	   			"beginTime":cri.formatDate($(".btn-group.time").data("querytype"),"yyyy-MM-dd"),
	   			"endTime":cri.formatDate(now,"yyyy-MM-dd"),
	   			"type":"customServiceId",  /* 这里的参数可以是serviceGroupId或者customServiceId*/
	    	}
	    	getPermanceData(params);
	    	
	    }else if("工单统计"==activeTab){
	   		getData(cri.formatDate($(".btn-group.time").data("querytype"),"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd")).then(function(data){
	          console.dir(data);
	          });
	    	 
	    }else if("概览"==activeTab){
	    	getOViewData(cri.formatDate($(".btn-group.time").data("querytype"),"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd"));
	    }
	});
	
	//选择时间 添加自定义data-querytype类型
	var i=0;
	$(".btn-group.time button").each(function(){
		var $this = $(this);
		if(i==0){
			$this.data("querytype",beforeWeek);	
		}else if(i==1){
			$this.data("querytype",beforeMonth);	 
		}else if(i==2){
			$this.data("querytype",beforeThreeMonth);	 
		} 
		i=i+1;
	});
	//点击时间栏事件初始化
	$(".btn-group.time").data("querytype",$(".btn-group.time button.active").data("querytype"));
	$(".btn-group.time button").click(function(){
	    var $this = $(this);
	    $this.siblings().removeClass("active");
	    $this.addClass("active");
	    if($(".btn-group.time").data("querytype")!=$this.data("querytype")){
	    	if("坐席统计"==activeTab){
	    		var serviceOrGroup="";
	    		var tem= $(".btn-group.fr").data("querytype");
	    		if(tem==1){
	    			serviceOrGroup="customServiceId";
	    		}else if(tem==2){
	    			serviceOrGroup="serviceGroupId";
	    		}
	    		var params={
	    				"beginTime":cri.formatDate($this.data("querytype"),"yyyy-MM-dd"),
		     			"endTime":cri.formatDate(now,"yyyy-MM-dd"),
		     			"type":serviceOrGroup  /* 这里的参数可以是customServiceId或者serviceGroupId*/
		     	}
	    		getPermanceData(params).then( function(data){ console.dir(data);	});
	       	   
	   	   }else if("工单统计"==activeTab){
	   		   getData(cri.formatDate($this.data("querytype"),"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd"))
	   		   		.then(function(data){
	   		   			console.dir(data);
	   		   		});
	   		$($("#queryButtons li")[0]).click();
   		   }else if("概览"==activeTab){
   			   getOViewData(cri.formatDate($this.data("querytype"),"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd"));
	       }
	    	$(".btn-group.time").data("querytype",$this.data("querytype"));
	    }
   	}); 
	
	//按客服查询和按客服组查询按钮
	$(".btn-group.fr").data("querytype",$(".btn-group.fr a.active").data("querytype"));
	$(".btn-group.fr a").click(function(){
	    var $this = $(this);
	    $this.siblings().removeClass("active");
	    $this.addClass("active");    
	
	    if($(".btn-group.fr").data("querytype")!=$this.data("querytype")){
	    	 var serviceOrGroup="";
	    	 var category="";
	    	 if($this.data("querytype")==1){
	    		 serviceOrGroup="customServiceId";
	    		 $("#serviceType").text("客服职员");
	    	 }else if($this.data("querytype")==2){
	    		 serviceOrGroup="serviceGroupId";
	    		 $("#serviceType").text("客服组");
	    	 }
	    	var params={
	       			"beginTime":cri.formatDate($(".btn-group.time").data("querytype"),"yyyy-MM-dd"),
	       			"endTime":cri.formatDate(now,"yyyy-MM-dd"),
	       			"type":serviceOrGroup /* 这里的参数可以是service或者group*/
	       	}
	   		getPermanceData(params);
	    	$(".btn-group.fr").data("querytype",$this.data("querytype"));
	    }; 
	});
       
	//概况统计的菜单项，新建工单总数等(暂不用管)
	$("#queryButtons").data("querytype",$("#queryButtons li.active").data("querytype"));
	$("#queryButtons li").click(function(){
	    var $this = $(this);
	    $this.siblings().removeClass("active");
	    $this.addClass("active");
	    if($("#queryButtons").data("querytype")!=$this.data("querytype")){
	    	 //var chart1 = $('#chart-1').highcharts();	
	    	 var queryType= $this.data("querytype");
	    
	    	 var temp="";
	    	 if(queryType==1){
	    		 temp=resultDate.rows.created;
	    		 chart1.yAxis[0].setTitle({text:"工单数量"});
	    	 }else if(queryType==2){
	    		 temp=resultDate.rows.solved;
	    		 chart1.yAxis[0].setTitle({text:"工单数量"});
	    	 }else if(queryType==3){
	    		 temp=resultDate.rows.average_respond_time;
	    		 chart1.yAxis[0].setTitle({text:"时间（h）"});
	    	 }else if(queryType==4){
	    		 temp=resultDate.rows.average_solve_time;
	    		 chart1.yAxis[0].setTitle({text:"时间（h）"});
	    	 }else if(queryType==5){
	    		 chart1.yAxis[0].setTitle({text:"时间（h）"});
	    		 temp=resultDate.rows.average_satisfy;
	    	 }
	    	 changeChart1(temp);
	    	 $("#queryButtons").data("querytype",$this.data("querytype"));
	    }
	   
	}); 
       
	 
	
      getOViewData(cri.formatDate($(".btn-group.time").data("querytype"),"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd"));
  	});

	/**
	 * 坐席统计的刷新表格
	 */
	var createTable = (function(){
	    var trTemp = Handlebars.compile($("#table-tr-performance-template").html());
	    return function(tableData){
	        var html = [];
	       	$("#groupTable").hide();
	        $("#serviceTable").show(); 
	   	    var $servieTable = $("#serviceTable");
	   	    var i=1;
	        for(var dbMap in tableData){
	        	tableData[dbMap].value.order=i;
	            var $tr = $(trTemp(tableData[dbMap].value));
	            html.push($tr);
	            i++;
	        }
	        $servieTable.find("tbody").empty().append(html);
	    };   
	    
	})();
   
   
	/*获取坐席统计的后台数据  */
	function getPermanceData(param){
	    return $.ajax({
	        url:"<%=request.getContextPath()%>/report/queryPerformanceSort",
	        dataType:'json',
	        data:param,
	        success:function(datas){
	 				if(datas.success){
	 					 //console.dir(datas);
	 				     //refreshRightButtonPer(datas.rows);
	                     createTable(datas.rows);                 
	          }
	          else{
	              notice.alert('请求数据失败','alert-danger');
	          }
	    }
	    });
}
   
/**
 * 获取工单统计的后台数据
 */
function getData(beginTime,endTime){
    return $.ajax({
        url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/statisticSurveyInfo/queryStatisticSurvey?sessionKey="+ $.cookie("sessionKey"),
        dataType:'jsonp',
        data:{beginTime:beginTime,endTime:endTime},
        success:function(data){
              	resultDate=data;
          if(data.success){
              refreshTopButton(data.rows);
              //设置折线图中的数据
             changeChart1(data.rows.created);
          }else{
              notice.alert('请求数据失败','alert-danger');
          }
    	}
    });
}
/**
 * 获取概览统计的后台数据
 */
function getOViewData(beginTime,endTime){
	//获取各渠道的服务数量
	$.ajax({
		url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryWorkOrderIndex?sessionKey="+ $.cookie("sessionKey"),
        dataType:'jsonp',
        data:{beginTime:beginTime,endTime:endTime},
        success:function(data){
 				if(data.success){
 					changeChart3(data.rows);                
          }else{
              notice.alert('请求数据失败','alert-danger');
              changeChart3(null);
          }
    	},
    	error:function(data){
    		changeChart3(null);
    	}
    });
	$.ajax({
        url:"<%=request.getContextPath()%>/communicate/getRecentServiceNum",
        dataType:'json',
        data:{beginTime:beginTime,endTime:endTime},
        success:function(data){
			if(data.success){
				changeChart2(data.rows);
				refreshOViewTopButton(data.rows);                
         	} else{
             	notice.alert('请求数据失败','alert-danger');
             	changeChart2(null);
         	}
    	},
    	error:function(data){
    		changeChart2(null);
    	}
    });
	$.ajax({
        url:"<%=request.getContextPath()%>/report/queryServedCustomer",
        dataType:'json',
        data:{beginTime:beginTime,endTime:endTime},
        success:function(data){
 				if(data.success){
 					$("#queryButtonsOverview").find("li[data-querytype='00']")
 						.find("span").text(data.rows.servNum);
 					$("#queryButtonsOverview").find("li[data-querytype='11']")
				.find("span").text(data.rows.workNum);
			changeChart4(data.rows);
          }
          else{
              notice.alert('请求数据失败','alert-danger');
              changeChart4(null);
          }
    	},
    	error:function(data){
    		changeChart4(null);
    	}
    });
	$.ajax({
        url:"<%=request.getContextPath()%>/report/getServiceStatistic",
        dataType:'json',
        data:{beginTime:beginTime,endTime:endTime},
        success:function(data){
        	if(data.success){
        		changeChart0(data.rows);
        	}else{
              notice.alert('请求数据失败','alert-danger');
          }
    	}
    });
}
   
/**
 * 刷新工单统计顶部按钮
 */
var refreshTopButton = function(rows){
	if(rows.desk.created!=-1){
		$("#created").text(rows.desk.created);
	}
    if(rows.desk.solved!=-1){
       $("#solved").text(rows.desk.solved);
    }
    if(rows.desk.average_respond_time!=-1){
    	 $("#average_respond_time").text(rows.desk.average_respond_time);
    }
   if(rows.desk.average_solve_time!=-1){
	   $("#average_solve_time").text(rows.desk.average_solve_time);
   }
   if(rows.desk.average_satisfy!=-1){
	   $("#average_satisfy").text(rows.desk.average_satisfy);
   }
    
};
/**
 * 刷新概览顶部按钮
 */
var refreshOViewTopButton = function(rows){
	if(rows&&rows.length>0){
		for(var i=0;i<rows.length;i++){
			var s=rows[i].source;
			var num=rows[i].num;
			if(s&&num>-1){
				var fd="li[data-querytype='"+s+"']"
				$("#queryButtonsOverview").find(fd).find("span").text(num);
			}
		}
	}
    
};
</script>

<!--有关图形的初始化以及处理  -->
<script type="text/javascript">
Highcharts.setOptions({
    credits: {
        enabled: false
    },
    title:{
        text:null
    }
});

//动态改变服务统计折线图
var changeChart0= function(datas){
  //得到x轴和y轴的数据
  var xData = datas.keys;
  var yData =datas.values;
  chart0.xAxis[0].setCategories(null);
  chart0.series[0].setData(null);
  chart0.xAxis[0].setCategories(xData);
  chart0.series[0].setData(yData);
  chart0.redraw();
}

//动态改变工单统计折线图
var changeChart1= function(datas){
  //得到x轴和y轴的数据
  var xData = datas.keys;
  var yData =datas.values;
  chart1.xAxis[0].setCategories(null);
  chart1.series[0].setData(null);
  chart1.xAxis[0].setCategories(xData);
  chart1.series[0].setData(yData);
  chart1.redraw();
}

//动态改变支持渠道扇形图数据
var changeChart2= function(rows){
	var sourceStrList=["网页表单","IM","API接口","邮件","手机端","电话","test","微信","视频"];
	if(rows==""||rows==undefined||rows==null){
	    var data = new Array();
    	data[0]= ["无数据",1];
	    chart2.series[0].setData(data);
	}else{
		//alert("have");
		var xData = rows;
	    var data = new Array();
	    for(var i=0;i<xData.length;i++){
	    	var index=parseInt(xData[i].source);
	    	data[i]= [sourceStrList[index],parseInt(xData[i].num)];
	    }
	    chart2.series[0].setData(data);
	}
  }
  
//动态改变工单状态扇形图数据
var changeChart3= function(rows){
	if(rows==""||rows==undefined||rows==null){
	    var data = new Array();
    	data[0]= ["无数据",1];
	    chart3.series[0].setData(data);
	}else{
		
		var xData = rows.titleList;
	    var data = new Array();
	    for(var i=0;i<xData.length;i++){
	    	data[i]= [xData[i].title,xData[i].number];
	    }    
	    chart3.series[0].setData(data);
	}
}
 //动态改变新建客户、服务客户数比例扇形图数据
var changeChart4= function(rows){
	if(rows==""||rows==undefined||rows==null){
	    var data = new Array();
    	data[0]= ["无数据",1];
	    chart4.series[0].setData(data);
	}else{
		var data = new Array();
	   	data[0]= [rows.newText,rows.newNum];
	   	data[1]= [rows.servText,rows.servNum];
	    chart4.series[0].setData(data);
	}
}
//概览统计折线图
var chart0=$('#chart-0').highcharts({
    chart: {
        type: 'spline'
    },
    xAxis: {
        type: 'category',
        tickmarkPlacement:"on"  //设置刻度的对齐方式
     
    },
    yAxis: {
        title: {
            text: '服务量'
        },
        min: 0
    },
    tooltip: {
        formatter: function() {
            return  this.x+'<br/><b>'+ this.series.name +'</b>' +': '+ this.y;
                    
        }
    },
    series: [{
    	name:"服务量",
       
    }]
}).highcharts(); 

 
//工单统计折线图
$('#chart-1').css("width",$('nav').css('width'));
var chart1=$('#chart-1').highcharts({
    chart: {
        type: 'spline'
    },
    xAxis: {
        type: 'category',
        tickmarkPlacement:"on"  //设置刻度的对齐方式
     
    },
    yAxis: {
        title: {
            text: '工单数量'
        },
        min: 0
    },
    tooltip: {
        formatter: function() {
            return  this.x+'<br/><b>'+ this.series.name +'</b>' +': '+ this.y;
                    
        }
    },
    series: [{
    	name:"工单数量",
       
    }]
}).highcharts();

//支持渠道扇形图
var chart2=$('#chart-2').highcharts({                   //图表展示容器，与div的id保持一致
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false
    },
    tooltip: {
        pointFormat: '<b>{point.percentage:.1f}%</b>'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: false
            },
            showInLegend: true               
        }
    },
    series: [{
        type: 'pie',
        name: 'Browser share',
        data: [
      
        ]
    }]
}).highcharts();

/* var colors = Highcharts.getOptions().colors;

function setChart(name, categories, data, color) {
    chart.xAxis[0].setCategories(categories, false);
    chart.series[0].remove(false);
    chart.addSeries({
        name: name,
        data: data,
        color: color || 'white'
    }, false);
    chart.redraw();
} */

//工单状态扇形图
var chart3 = $('#chart-3').highcharts({                   //图表展示容器，与div的id保持一致
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false
    },
    tooltip: {
        pointFormat: '<b>{point.y:.0f}</b>'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: false
            },
            showInLegend: true               
        }
    },
    series: [{
        type: 'pie',
        name: 'Browser share',
        data: [
      
        ]
    }]
}).highcharts();

//新建客户、服务客户数比例扇形图
var chart4=$('#chart-4').highcharts({                   //图表展示容器，与div的id保持一致
    chart: {
        plotBackgroundColor: null,
        plotBorderWidth: null,
        plotShadow: false
    },
    tooltip: {
        pointFormat: '<b>{point.y:.0f}</b>'
    },
    plotOptions: {
        pie: {
            allowPointSelect: true,
            cursor: 'pointer',
            dataLabels: {
                enabled: false
            },
            showInLegend: true               
        }
    },
    series: [{
        type: 'pie',
        name: 'Browser share',
        data: [
      
        ]
    }]
}).highcharts();
</script>
</body>
</html>