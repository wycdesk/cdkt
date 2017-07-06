<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>数据统计</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/dataStatistics.css">
  <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/dropzone/dropzone.css"/>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/usermanage.css">
</head>
<body>
<header class="header">
    数据统计
</header>
<div class="container-fluid main">
    <div class="row">
 
        <div class="col-sm-12">
            <div class="navbar navbar-default">
               <ul class="nav navbar-nav" id="menu">
                <li class="active"><a href="#overview" data-toggle="tab">概览</a></li>
                <li><a href="#workStatistic" data-toggle="tab">工单统计</a></li>
                <li><a href="#performance" data-toggle="tab">业绩排行榜</a></li>
                <!-- 
                <li><a href="#">帮助文档知识库</a></li>
                <li><a href="#">帮助中心搜索</a></li>
                <li><a href="#">讨论社区</a></li>
                <li ><a href="#">满意度统计</a></li>
                 -->
               </ul>
            </div>
        </div>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <div class="search-bar">
                                            统计时间段:
                <div class="btn-group time" role="group" aria-label="...">
                    <button    type="button" class="btn btn-sm btn-outline active btn-primary">近一周</button>
                    <button     type="button" class="btn btn-sm btn-outline">近一个月</button>
                    <button     type="button" class="btn btn-sm btn-outline">近三个月</button>
                    <!-- <button     type="button" class="btn btn-sm btn-outline">自定义</button> -->
                </div>
            </div>
        </div>
    </div>
    
    
  <div id="myTabContent" class="tab-content">
  	<!--   概览      -->
  	<div class="tab-pane fade in active" id="overview">
     	<div class="panel-heading">概览</div>
     	<div class="panel-body">
     		<nav class="panel-number-btn">
   				<ul id="queryButtonsOverview">
                	<li data-querytype="00" class="ember-view active">
	                    <a><span class="red" id="customerServed">0</span><p>服务客户数</p></a></li>
	                <li data-querytype="5" class="ember-view">
	                    <a><span class="blue" id="telcount">0</span><p>电话数量</p></a></li>
	                <li data-querytype="7" class="ember-view">
	                    <a><span class="green" id="wechatcount">0</span><p>微信数量</p></a>
	                </li>
	                <li data-querytype="1" class="ember-view">
	                    <a><span class="orange" id="IMcount">0</span><p>IM数量</p></a>
	                </li>
	                <li data-querytype="8" class="ember-view">
	                    <a><span class="purple" id="videocount">0</span><p>视频数量</p></a>
	                </li>
	                <li data-querytype="11" class="ember-view">
	                    <a><span class="purple" id="workcount">0</span><p>工单数量</p></a>
	                </li>
                </ul>
            </nav>
            <div id="chart-0"></div>
      	</div>
        <div id="charts"  class="row">
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
     		<nav class="panel-number-btn">
   				<ul id="queryButtons">
                	<li data-querytype="1" class="ember-view active">
	                    <a><span class="red" id="created">-</span><p>新工单数</p></a></li>
	                <li data-querytype="2" class="ember-view">
	                    <a><span class="blue" id="solved">-</span><p>已解决的工单总数</p></a></li>
	                <li data-querytype="3" class="ember-view">
	                    <a><span class="green" id="average_respond_time">-</span><p>响应时间（小时）平均</p></a>
	                </li>
	                <li data-querytype="4" class="ember-view">
	                    <a><span class="orange" id="average_solve_time">-</span><p>解决时间（小时）平均</p></a>
	                </li>
	                <!-- <li data-querytype="5" class="ember-view">
	                    <a><span class="purple" id="average_satisfy">-</span><p>满意度（%）平均</p></a>
	                </li> -->
                </ul>
            </nav>
            <div>
            <div id="chart-1" style="width:$('nav').css('width')px;"></div>
            </div>
      	</div>
        <!-- <div id="charts"  class="row">
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
		                <div class="panel-heading">工单首次响应时间（小时）</div>
		                <div class="panel-body">
		                    <div id="chart-3"></div>
		                </div>
		            </div>
		        </div>
		        <div class="col-sm-4">
		            <div class="panel panel-default">
		                <div class="panel-heading">工单解决时间（小时）</div>
		                <div class="panel-body">
		                    <div id="chart-4"></div>
		                </div>
		            </div>
		        </div> 
        </div> --> 
     </div>
     
    <!--   业绩排行榜 -->
   <div class="tab-pane fade" id="performance">
    <div class="row">
        <div class="col-sm-12">
            <div class="panel panel-default">
                <div class="panel-heading">
                                                  业绩排行榜 
                   <div class="btn-group fr">
                       <a type="button"  data-querytype="1"  class="btn btn-sm active">按客服查看</a>
                       <a type="button" data-querytype="2"  class="btn btn-sm btn-br">按客服组查看</a>
                   </div>
                </div>
                <div class="panel-body">
                    <!-- <nav class="panel-number-btn">
                        <ul id="queryButtonsPer">
                            <li data-querytype="1" class="ember-view active">
                                <a><span class="red" id="solvedPer">-</span><p>已解决工单总数</p></a></li>
                            <li data-querytype="2" class="ember-view">
                                <a><span class="blue" id="average_respond_timePer">-</span><p>首次响应时间(小时)平均</p></a></li>
                            <li data-querytype="3" class="ember-view">
                                <a><span class="green" id="average_solve_timePer">-</span><p>解决时间(小时)平均</p></a>
                            </li>
                            <li data-querytype="4" class="ember-view">
                                <a><span class="orange" id="average_satisfyPer">-</span><p>满意度(%)平均</p></a>
                            </li>
                            <li data-querytype="5" class="ember-view">
                                <a><span class="purple" id="average_update_timePer">-</span><p>客服更新次数总数</p></a>
                            </li>
                        
                        </ul>
                    </nav> -->
			             <div class="row table-content">
			                <div class="col-12 grid">
			                    <table class="table" cellspacing="0" cellpadding="0"  id="serviceTable"  >
			                        <thead>
			                        <tr class="order">			                         
			                            <th>排名</th>
			                            <th>客服职员</th>
			                            <th>客服组</th>
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
			                    <!-- <table class="table" cellspacing="0" cellpadding="0" id="groupTable" style="display:none">
				                        <thead>
				                        <tr class="order">
				                            <th>排名</th>
				                            <th>客服组</th>
				                            <th>参与客服</th>
				                            <th>解决工单</th>
				                        </tr>
				                        </thead>
				                        <tbody></tbody>
				                        <tfoot>
				                        <tr>
				                            <td colspan="6"><div id="paginationGroup"></div></td>
				                        </tr>
				                        </tfoot>
                                  </table> --> 
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

<script id="table-tr-service-template" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td>{{order}}</td>
        <td style="display:none">{{userId}}</td>      
        <td class="user">
            <a class="avatar"><img src="<%=request.getContextPath()%>/{{photoUrl}}" onclick="viewDetails('{{userId}}','{{userName}}','{{loginTime}}')"></a>
            <div class="user-right">
                <a class="name" href="#" onclick="viewDetails('{{userId}}','{{userName}}')">{{userName}}</a>
                <span class="privilege">{{userType}}</span>
            </div>
        </td>  
        <td>{{#each groups}} 
					<span class="privilege">{{name}}</span>
 			{{/each}}
        </td>
        <td>{{num}}</td>
          
    </tr>
</script>
 
<script id="table-tr-group-template" type="text/x-handlebars-template">
 <tr class="ember-view">
        <td>{{order}}</td>
 	    <td>{{group_name}}</td>
        <td class="user">{{#each agents}} 

				 <span class="privilege"> <a class="avatar"><img src="<%=request.getContextPath()%>/{{photoUrl}}" onclick="viewDetails('{{id}}','{{name}}','{{loginTime}}')"></a></span>
    		      <td style="display:none">{{id}}</td>   
 				  <td style="display:none">{{name}}</td>   
				 
 			{{/each}}
        </td>
        <td>{{num}}</td>
          
    </tr>
</script>

<!-- 业绩排行榜模板 -->
<script id="table-tr-performance-template" type="text/x-handlebars-template">
 <tr class="ember-view">
        <td>{{order}}</td>
 	    <td>
			{{#if customServiceName}}
				{{customServiceName}}
			{{else}}
				<span>-</span>
			{{/if}} 
		</td>
		<td>
			{{#if serviceGroupName}}
				{{serviceGroupName}}
			{{else}}
				<span>-</span>
			{{/if}} 
		</td>
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
        var now = new Date();
        var beforeWeek = new Date(now.getTime() - 7*24*3600*1000);
        var beforeMonth = new Date(now.getTime() - 30*24*3600*1000);
        var beforeThreeMonth = new Date(now.getTime() - 3*30*24*3600*1000);
        
    
        
        //导航栏的单击事件
        $('a[data-toggle="tab"]').on('shown.bs.tab', function (e) {
            // 获取已激活的标签页的名称
            activeTab = $(e.target).text();
            if("业绩排行榜"==activeTab){
            	$(".btn-group.fr").data("querytype","1");
            	$(".btn-group.fr a").siblings().removeClass("active");
            	$(".btn-group.fr a").first().addClass("active");
            	var params={
            			"beginTime":cri.formatDate($(".btn-group.time").data("querytype"),"yyyy-MM-dd"),
            			"endTime":cri.formatDate(now,"yyyy-MM-dd"),
            			"type":"customServiceId",  /* 这里的参数可以是service或者group*/
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
            	 }else if($this.data("querytype")==2){
            		 serviceOrGroup="serviceGroupId";
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
           
            	 var   chart1 = $('#chart-1').highcharts();	
            	 var queryType= $this.data("querytype");
            
            	 var temp="";
            	 if(queryType==1){
            		 temp=resultDate.rows.created;
            	 }else if(queryType==2){
            		 temp=resultDate.rows.solved;
            	 }else if(queryType==3){
            		 temp=resultDate.rows.average_respond_time;
            	 }else if(queryType==4){
            		 temp=resultDate.rows.average_solve_time;
            	 }else if(queryType==5){
            		 temp=resultDate.rows.average_satisfy;
            	 }
            	 
            	 changeChart1(chart1,temp);
            	 $("#queryButtons").data("querytype",$this.data("querytype"));
            }
           
        }); 
        
        //业绩排行的菜单项，已解决工单总数等（已经去掉）
        /* $("#queryButtonsPer").data("querytype",$("#queryButtonsPer li.active").data("querytype"));
        $("#queryButtonsPer li").click(function(){
        	
   
            var $this = $(this);
            $this.siblings().removeClass("active");
            $this.addClass("active");
            if($("#queryButtonsPer").data("querytype")!=$this.data("querytype")){
            	 var queryType= $this.data("querytype");
                 
            	 var serviceOrGroup="";
            	 var category="";
            	 
            	 if(queryType==1){
            		 category="solved";           	 
                     $("#serviceTable .order th:last").html("解决工单");
                	 $("#groupTable .order th:last").html("解决工单");
            	 }else if(queryType==2){
            		 category="average_respond_time";
            		 $("#serviceTable .order th:last").html("首次响应时间");
                 	 $("#groupTable .order th:last").html("首次响应时间");
                  
            	 }else if(queryType==3){
            		 category="average_solve_time";
            		 $("#serviceTable .order th:last").html("解决时间");
                 	 $("#groupTable .order th:last").html("解决时间");
                 	 
            	 }else if(queryType==4){
            		  category="average_satisfy";
            		  $("#serviceTable .order th:last").html("满意度(%)");
                 	  $("#groupTable .order th:last").html("满意度(%)");
                 	 
            	 }else if(queryType==5){
            		 category="average_update_time";
            		  $("#serviceTable .order th:last").html("客服更新次数");
                 	  $("#groupTable .order th:last").html("客服更新次数");
                 	 
            	 }
        
            	 if($(".btn-group.fr").data("querytype")==1){
            		 serviceOrGroup="service";
            	 }else if($(".btn-group.fr").data("querytype")==2){
            		 serviceOrGroup="group";
            	 }
           		var params={
               			"beginTime":cri.formatDate( $(".btn-group.time").data("querytype"),"yyyy-MM-dd"),
               			"endTime":cri.formatDate(now,"yyyy-MM-dd"),
               			"serviceOrGroup":serviceOrGroup,   //这里的参数可以是service或者group
               			"category":category
               	}
           		
           		getPermanceData(params);
               $("#queryButtonsPer").data("querytype",$this.data("querytype"));
            }
           
        }); */ 
        
        
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
        
        $(".btn-group.time").data("querytype",$(".btn-group.time button.active").data("querytype"));
        $(".btn-group.time button").click(function(){
            var $this = $(this);
            $this.siblings().removeClass("active");
            $this.addClass("active");
            if($(".btn-group.time").data("querytype")!=$this.data("querytype")){            

                       if("业绩排行榜"==activeTab){
                    	   
                    		 var serviceOrGroup="";
                        	 /* var category="";
                           
                        	 var queryType= $("#queryButtonsPer").data("querytype");
                        	 if(queryType==1){
                        		 category="solved";                      	 
                        	 }else if(queryType==2){
                        		 category="average_respond_time";
                        	 }else if(queryType==3){
                        		 category="average_solve_time";
                        	 }else if(queryType==4){
                        		 category="average_satisfy";
                        	 }else if(queryType==5){
                        		 category="average_update_time";
                        	 } */
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
                    	   getPermanceData(params).then(function(data){
             	                 console.dir(data);
             	             });
                       }else if("工单统计"==activeTab){
           	           	 getData(cri.formatDate($this.data("querytype"),"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd")).then(function(data){
           	                 console.dir(data);
           	             });
                       }else if("概览"==activeTab){
                    	   getOViewData(cri.formatDate($this.data("querytype"),"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd"));
                         }

            	 $(".btn-group.time").data("querytype",$this.data("querytype"));
            }
           
        });  
        
        //统计概况后台发送请求获取数据
        /* getData(cri.formatDate(beforeWeek,"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd")).then(function(data){
            console.dir(data);
        }); */
        getOViewData(cri.formatDate($(".btn-group.time").data("querytype"),"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd"));
    });

    /* 查看用户详情 */
    function viewDetails(userId,userName,loginTime){;
<%--         var url="<%=request.getContextPath()%>/usrManage/userDetails?userId="+userId+"&loginTime="+loginTime;
        parent.openTab(url,"user",userName); --%>
        
        var url="<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+userId+"&loginTime="+loginTime;
        parent.openTab(url,"user",userName);
    }
    
    /**
     * 业绩排行榜的刷新表格
     */
    var createTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-performance-template").html());
        //var trGroupTemp = Handlebars.compile($("#table-tr-group-template").html());
      //  var tooltipTemp = Handlebars.compile($("#tooltip-template").html());
        return function(tableData){
        	/* var serOrGroup= $(".btn-group.fr").data("querytype");
            if(serOrGroup==1){  */   
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
                
             /* }else if(serOrGroup==2){
            	 
           	    $("#serviceTable").hide();
       	        $("#groupTable").show(); 
                var $groupTable = $("#groupTable");
                var html = [];
                for(var i= 0,len=tableData.length; i<len;i++){
                    var $tr = $(trGroupTemp(tableData[i]));
                
               /*      var $a = $tr.find("a");
                    
                    var $tip = $(tooltipTemp(tableData[i]));
                    $a.tooltipster({
                        content: $tip,
                        theme: 'tooltipster-shadow'
                    });
                    
                    html.push($tr);
                }
                $groupTable.find("tbody").empty().append(html);
             } */
        };   
        
    })();
    
    
    /*获取业绩排行榜的后台数据  */
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
     * 获取概况统计的后台数据
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
		               var   chart1 = $('#chart-1').highcharts();	
		                //设置扇形图中的数据
		               //var   chart2 = $('#chart-2').highcharts();	
		               //设置柱状图中的数据
		               //var   chart3 = $('#chart-3').highcharts();
		               //设置条柱状图中的数据
		               //var   chart4 = $('#chart-4').highcharts();	
		                
		               changeChart1(chart1,data.rows.created);
		               //changeChart2(chart2,data.rows);
		              // changeChart3(chart3,data.rows);
		               //changeChart4(chart4,data.rows);
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
		            }
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
		            }
		            else{
		                notice.alert('请求数据失败','alert-danger');
		            }
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
		            }
        	}
        });
    }
    

    /**
     * 刷新业绩排行榜顶部按钮
     */
    var refreshRightButtonPer = function(rows){
    	if(rows.desk.solved!=-1){
    		$("#solvedPer").text(rows.desk.solved);
    	}
        if(rows.desk.solved!=-1){
           $("#average_respond_timePer").text(rows.desk.average_respond_time);
        }
        if(rows.desk.average_respond_time!=-1){
        	 $("#average_solve_timePer").text(rows.desk.average_solve_time);
        }
       if(rows.desk.average_solve_time!=-1){
    	   $("#average_satisfyPer").text(rows.desk.average_satisfy);
       }
       if(rows.desk.average_satisfy!=-1){
    	   $("#average_update_timePer").text(rows.desk.average_update_time);
       }
      
    };
    
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
    //动态改变折线图
    var changeChart1= function(chart,datas){

    	
      //得到x轴和y轴的数据
	  var xData = datas.keys;
      var yData =datas.values;
 
      chart.xAxis[0].setCategories(null);
      chart.series[0].setData(null);
      chart.xAxis[0].setCategories(xData);
      chart.series[0].setData(yData);
      chart.redraw();
    }
    
    //动态改变扇形图数据
    var changeChart2= function(rows){
    	var sourceStrList=["网页表单","IM","API接口","邮件","手机端","电话","test","微信","视频"];
  	    var xData = rows;
        var data = new Array();
        for(var i=0;i<xData.length;i++){
        	
        	data[i]= [sourceStrList[parseInt(xData[i].source)],parseInt(xData[i].num)];
        }    
        chart2.series[0].setData(data);
      }
      
    //动态改变柱状图数据
    var changeChart3= function(rows){
    	var xData = rows.titleList;
        var data = new Array();
        for(var i=0;i<xData.length;i++){
        	data[i]= [xData[i].title,xData[i].number];
        }    
        chart3.series[0].setData(data);
    }
     //动态改变条状图数据
    var changeChart4= function(rows){
        var data = new Array();
       	data[0]= [rows.newText,rows.newNum];
       	data[1]= [rows.servText,rows.servNum];
        chart4.series[0].setData(data);
      }
      
    
    
 

    Highcharts.setOptions({
        credits: {
            enabled: false
        },
        title:{
            text:null
        }
    });
    
    
    //折线图
     $('#chart-1').css("width",$('nav').css('width'));
     var chart=$('#chart-1').highcharts({
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
            min: -5
        },
        tooltip: {
            formatter: function() {
                return  this.x+'<br/><b>'+ this.series.name +'</b>' +': '+ this.y;
                        
            }
        },

        series: [{
        	name:"工单数量",
           
        }]
    });

    
    //扇形图
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
    var colors = Highcharts.getOptions().colors;

    function setChart(name, categories, data, color) {
        chart.xAxis[0].setCategories(categories, false);
        chart.series[0].remove(false);
        chart.addSeries({
            name: name,
            data: data,
            color: color || 'white'
        }, false);
        chart.redraw();
    }

    //柱状图
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
    }).highcharts(); // return chart

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