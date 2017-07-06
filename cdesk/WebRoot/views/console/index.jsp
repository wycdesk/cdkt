<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>控制面板首页</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/console.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote.js" ></script>
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote-zh-CN.js" ></script>
    <script src="<%=request.getContextPath()%>/script/lib/Highcharts/js/highcharts.js" type="text/javascript"></script>
</head>
<body>
<div id="right-part" style="left:0;">
    <div class="right-content">
        <div class="right-content-panel container-fluid">
            <div class="row">
                <header class="console-header">今日情况</header>
                <nav class="panel-number-btn">
                    <ul id="queryButtons">
                        <li data-querytype="1" class="ember-view ">
                            <a><span class="red" id="queryHandleWorkOrder">0</span><p>服务客户数</p></a></li>
                        <li data-querytype="2" class="ember-view">
                            <a><span class="blue" id="queryTelphones">0</span><p>电话数量</p></a></li>
                        <li data-querytype="3" class="ember-view">
                            <a><span class="green" id="queryWeixins">0</span><p>微信数量</p></a>
                        </li>
                        <li data-querytype="4" class="ember-view">
                            <a><span class="orange" id="queryIMs">0</span><p>IM数量</p></a>
                        </li>
                        <li data-querytype="5" class="ember-view">
                            <a><span class="yellow" id="queryVideos">0</span><p>视频数量</p></a>
                        </li>
                        <li data-querytype="6" class="ember-view">
                            <a><span class="purple" id="todayWKnum">0</span><p>工单数量</p></a>
                        </li>
                    </ul>
                </nav>
            </div>
            <div class="row">
                <header class="console-header">分布情况</header>
                <nav class="panel-number-btn">
                    <ul>
                        <li data-querytype="1">
                            <div class="panel panel-default">
                                <div class="panel-heading">工单解决情况<span class="hui" id="chart1-span"></span></div>
                                <div class="panel-body">
                                    <div id="chart-1" style="height:300px;"></div>
                                </div>
                            </div>
                        </li>
                        <li data-querytype="2">
                            <div class="panel panel-default">
                                <div class="panel-heading">请求渠道分布<span class="hui" id="chart2-span"></span></div>
                                <div class="panel-body">
                                    <!-- <div style="height:300px;">图表</div> -->
                                    <div id="chart-2" style="height:300px;"></div>
                                </div>
                            </div>
                        </li>
                        <li data-querytype="3">
                            <div class="panel panel-default">
                                <div class="panel-heading">服务客户<span class="hui" id="chart3-span"></span></div>
                                <div class="panel-body">
                                    <div id="chart-3" style="height:300px;"></div>
                                </div>
                            </div>
                        </li>
                    </ul>
                </nav>
            </div>
            <div class="row table-content">
                <!--                           最近联络相关表格                                                                      -->
                <div class="col-sm-5" style="padding-left:0;">
                    <header class="console-header">最近联络</header>
                    <div class="col-12 grid panel">
                        <table id="commGrid" class="table footable" cellspacing="0" cellpadding="0" data-page-size="10">
                            <thead>
                            <tr class="order">
                                <th style="width:21%;">姓名</th>
                                <th style="width:21%;">渠道</th>
                                <th style="width:21%;">客服</th>
                                <th>联络时间</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td>王勇</td>
                                <td>微信</td>
                                <td>2016-01-26 <span> 14:20:20</span></td>
                            </tr>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="8">
                                    <div class="pagination-ul" id="paginationComm"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>
                    </div>
                </div>
                <div class="col-sm-7" style="padding-right:0">
                    <header class="console-header">最新工单</header>
                    <div class="col-12 grid panel">
                        <table id="latelyOrderGrid" class="table footable" cellspacing="0" cellpadding="0" data-page-size="10">
                            <thead>
                            <tr class="order">
                                <th>标题</th>
                                <th>工单发起人</th>
                                <th>创建时间</th>
                                <th>受理客服组</th>
                                <th>工单受理客服</th>
                            </tr>
                            </thead>
                            <tbody>
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="5">
                                    <div class="pagination-ul" id="paginationLately"></div>
                                </td>
                            </tr>
                            </tfoot>
                        </table>

                    </div>
                </div>
            </div>
        </div>
    </div>
    <div id="toolbar" >
        <button id="deleteBtn" type="button" class="btn btn-raised left-btn btn-danger">删除</button>
        <button id="cancelBtn" type="button" class="btn btn-raised btn-default">清除选择</button>
        <button id="editBtn" type="button" class="btn btn-raised btn-primary" data-toggle="modal" data-target="#editOrderModal"></button>
    </div>
</div>

<%@include file= "editWf.jsp" %>

<script type="text/javascript">
    var wfStatus = ["尚未受理","受理中","等待客户回复","已解决","已关闭"];

    $(function(){
        var now = new Date();
        var beforeWeek = new Date(now.getTime() - 7*24*3600*1000);

        getTodayWKNum().then(function(data){
            console.dir(data);
        });

        //请求渠道后台发送请求获取数据
        /* getData(cri.formatDate(beforeWeek,"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd")).then(function(data){
         console.dir(data);
         }); */
        getWorkOrderData(cri.formatDate(beforeWeek,"yyyy-MM-dd"),cri.formatDate(now,"yyyy-MM-dd")).then(function(data){
            console.dir(data);
        });
        getUserData();

        getTableData({page:1,rows:4});

        getTodaySourceNum();
    });

    function getWorkOrderData(beginTime,endTime){
        return $.ajax({
            url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryWorkOrderIndex?sessionKey="+ $.cookie("sessionKey"),
            dataType:'jsonp',
            data:{},
            success:function(data){
                if(data.success){
                    //设置扇形图中的数据
                    if(data.rows.titleList.length>0){
                        $("#chart1-span").html("（共"+data.rows.allNum+"）");
                    }else{
                        $("#chart1-span").html("（共0）");
                    }
                    var chart1 = $('#chart-1').highcharts();

                    changeChart1(chart1,data.rows);
                }
                else{
                    notice.alert('请求数据失败','alert-danger');
                }
            }
        });
    }

    /**
     * 请求渠道的后台数据
     */
    function getData(beginTime,endTime){
        return $.ajax({
            url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/statisticSurveyInfo/queryStatisticSurvey?sessionKey="+ $.cookie("sessionKey"),
            dataType:'jsonp',
            data:{beginTime:beginTime,endTime:endTime},
            success:function(data){
                if(data.success){

                    //设置扇形图中的数据
                    if(data.rows.desk.source.length>0){
                        $("#chart2-span").html("（共"+data.rows.desk.source[0].total+"）");
                    }else{
                        $("#chart2-span").html("（共0）");
                    }
                    var chart2 = $('#chart-2').highcharts();

                    changeChart2(chart2,data.rows);
                }
                else{
                    notice.alert(data.msg);
                }
            }
        });
    }
    //服务客户
    function getUserData(){
        return $.ajax({
            url:"<%=request.getContextPath()%>/report/queryCustomer",
            dataType:'json',
            data:{},
            success:function(data){
                if(data.success){
                    //设置扇形图中的数据
                    $("#queryHandleWorkOrder").text(data.rows.customDayNum);
                    $("#chart3-span").html("（共"+data.rows.allTotal+"）");
                    var chart3 = $('#chart-3').highcharts();
                    changeChart3(chart3,data.rows);

                    $("#chart2-span").html("（共"+data.rows.sourceTotal+"）");
                    var chart2 = $('#chart-2').highcharts();
                    changeChart2(chart2,data.rows.sourceList);
                }
                else{
                    notice.alert(data.msg);
                }
            }
        });
    }

    //动态改变扇形图数据
    var changeChart3= function(chart,rows){
        var data = new Array();

        var s1={};
        var i=0;
        s1.name=rows.dayText;
        s1.y=rows.dayNum;
        s1.color='#3D91C4';
        s1.sliced=true;
        s1.selected=true;
        if(s1.y != 0){
	        data[i] =s1;
	        i++;
        }

        var s2={};
        s2.name=rows.text;
        s2.y=rows.total;
        s2.color='#94CEFE';
        if(s2.y != 0){
	        data[i]=s2;
        }
        
        chart.series[0].setData(data);
    };

    var changeChart2= function(chart,rows){
        var xData = rows;
        var data = new Array();
        var phones=0;
        var p={};
        p.name="电话";
        p.color='#F44336';
        var j=0;
        for(var i=0;i<xData.length;i++){
            /* data[i]= [xData[i].sourceStr,xData[i].number]; */
            var s={};

            /* s.name=xData[i].source; */
            /* s.y=xData[i].num; */
            if(xData[i].source=="0"){
                s.name="网页表单";
                s.y=xData[i].num;
                s.color='#5A5A5B';
                data[j] =s;
                j++;
            }else if(xData[i].source=="1"){
                s.name="IM";
                s.y=xData[i].num;
                s.color='#2FC2BB';
                s.sliced=true;
                s.selected=true;
                data[j] =s;
                j++;
            }else if(xData[i].source=="2"){
                s.name="API接口";
                s.y=xData[i].num;
                s.color='#268453';
                data[j] =s;
                j++;
            }else if(xData[i].source=="3"){
                s.name="邮件";
                s.y=xData[i].num;
                s.color='#CCCCCC';
                data[j] =s;
                j++;
            }else if(xData[i].source=="4"){
                s.name="手机端";
                s.y=xData[i].num;
                s.color='#2E6387';
                data[j] =s;
                j++;
            }else if(xData[i].source=="5" || xData[i].source=="6"){

                phones=phones+parseInt(xData[i].num);
            }else if(xData[i].source=="7"){
                s.name="微信";
                s.y=xData[i].num;
                s.color='#7BB73B';
                data[j] =s;
                j++;
            }else if(xData[i].source=="8"){
                s.name="视频";
                s.y=xData[i].num;
                s.color='#F9B800';
                data[j] =s;
                j++;
            }
            /* data[i] =s; */
        }
        p.y=phones;
        if(phones!=0){
            data[j]=p;
        }
        chart.series[0].setData(data);
    };

    //动态改变扇形图数据
    var changeChart1= function(chart,rows){
        var xData = rows.titleList;
        var data = new Array();
        /* if(xData.length>0){
         var s={};
         s.name=xData[0].title;
         if(s.name=="未分配客服"){
         s.name="尚未处理";
         s.color='#ffbe26';
         }
         s.y=xData[0].number;
         s.sliced=true;
         s.selected=true;
         data[0] =s;
         } */
        for(var i=0;i<xData.length;i++){
            /* data[i]= [xData[i].sourceStr,xData[i].number]; */
            var s={};
            s.name=xData[i].title;
            if(s.name=="未分配客服"){
                s.name="尚未处理";
                s.color='#fc7c79';
                s.sliced=true;
                s.selected=true;
            }else if(s.name=="等待回复"){
                s.color='#67c0ff';
            }else if(s.name=="已解决"){
                s.color='#5dca91';
            }else if(s.name=="受理中"){
                s.color='#ffbe26';
            }
            s.y=xData[i].number;
            data[i] =s;
        }
        chart.series[0].setData(data);
    };


    Highcharts.setOptions({
        credits: {
            enabled: false
        },
        title:{
            text:null
        }
    });

    //扇形图
    $('#chart-3').highcharts({                   //图表展示容器，与div的id保持一致
        chart: {
            plotBackgroundColor: null,
            plotBorderWidth: null,
            plotShadow: false
        },
        tooltip: {
            pointFormat: '<b>{point.y:.0f} 名</b>'
        },
        plotOptions: {
            pie: {
                size:'180px',
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b><br/> {point.y:.0f} 名',
                },
                showInLegend: false
            }
        },
        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [

            ]
        }]
    });

    //扇形图
    $('#chart-2').highcharts({                   //图表展示容器，与div的id保持一致
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
                size:'180px',
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b><br/> {point.percentage:.1f} %',
                },
                showInLegend: false
            }
        },
        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [

            ]
        }]
    });

    //扇形图
    $('#chart-1').highcharts({                   //图表展示容器，与div的id保持一致
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
                size:'180px',
                allowPointSelect: true,
                cursor: 'pointer',
                dataLabels: {
                    enabled: true,
                    format: '<b>{point.name}</b><br/> {point.y:.0f}',
                },
                showInLegend: false
            }
        },
        series: [{
            type: 'pie',
            name: 'Browser share',
            data: [

            ]
        }]
    });

    //获取今日工单数量
    function getTodayWKNum(){
        return $.ajax({
            url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryTodayWorkOrderNum?sessionKey="+ $.cookie("sessionKey"),
            dataType:'jsonp',
            data:{},
            success:function(data){
                resultDate=data;

                if(data.success){
                    //设置数据
                    //console.dir(resultDate.rows.todayWKNum[0]);
                    $("#todayWKnum").text(resultDate.rows.todayWKNum[0]);
                }
                else{
                    notice.alert('请求数据失败','alert-danger');
                }
            }
        });
    }


    function getTodaySourceNum(){

        $.post("<%=request.getContextPath()%>/communicate/getTodayWKDetailNum?sessionKey="+ $.cookie("sessionKey"),{},function(data){
            if(data.success){
                var a=data.rows;
                var phones=0;
                for(var i = 0;i < a.length; i++) {
                    source=a[i].source;
                    if(source=="5"||source=="6"){
                        phones=phones+parseInt(a[i].num);
                    }
                    else if(source=="7"){
                        $('#queryWeixins').text(a[i].num);
                    }
                    else if(source=="1"){
                        $('#queryIMs').text(a[i].num);
                    }
                    else if(source=="8"){
                        $('#queryVideos').text(a[i].num);
                    }
                }
                $("#queryTelphones").text(phones);

            }else{
                notice.alert(data.msg,'alert-danger');
            }
        });

    }

</script>
<script id="table-tr-template" type="text/x-handlebars-template">
    <tr id="commHistory">
        <td data-prop="userName"><a>{{userName}}</a></td>
        <td data-prop="source">{{source}}</td>
        <td data-prop="source">{{opName}}</td>
        <td data-prop="createTimeStr">{{createTime}}</td>
    </tr>
</script>

<!-- 最近工单模板 -->
<script id="table-tr-template-lately" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td class="title"><a>{{title}}</a></td>
        <td>{{creatorName}}</td>
        <td>{{createDate}}</td>
        <td>{{serviceGroupName}}</td>
        <td>{{customServiceName}}</td>
    </tr>
</script>

<!-- 工单悬浮框模板 -->
<script id="tooltip-template" type="text/x-handlebars-template">
    <div class="tip-content panel panel-default">
        <div class="tip-header panel-heading">
            <span class="col-12"># {{workId}}</span><span class="tip-order-state">{{statusStr}}</span>
        </div>
        <div class="tip-row panel-body">
            <div class="container-fluid">
                <div class="row">
                    <span class="col-xs-3">标题</span><span class="col-xs-9">{{title}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">时间</span><span class="col-xs-9">{{createDate}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">发起人</span><span class="col-xs-9">{{creatorName}}</span>
                </div>
            </div>
        </div>
    </div>
</script>

<script type="text/javascript">
    var createCommTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template").html());
        var $table = $("#commGrid");
        return function(tableData, type){
            var html = [];
            var checkCount = tableData.length;
            for(var i= 0,len=tableData.length; i<len;i++){
                var $tr="";
                $tr = $(trTemp(tableData[i]));
                $tr.data("data",tableData[i]);
                html.push($tr);
            }
            $table.find("tbody").empty().append(html);
            $table.find("tbody").find("tr").each(function(){
            	var userId=$(this).data('data').userId;
            	var userName=$(this).data('data').userName;
            	$(this).find("a").click((function(){
            		var uID=userId;
            		var uName=userName;
            		var url="<%=request.getContextPath()%>/userManageMongo/userDetails?userId="+uID;
            		return function(){
            			parent.openTab(url,"",uName,true);
            			//alert(uID);
            		}
            	})());
            });
        };
        
    })();
    /**
     * 向后台请求数据
     */
    var getCommTableData = (function(){
        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#paginationComm"),{
            page:1,
            pageSize:4,
            total:0,
            onPage:function(page,pageSize){
                var url="";
                url="<%=request.getContextPath()%>/communicate/getComms";
                getCommTableData({page:page,rows:pageSize,entId:'${entId}'},url);
            }
        });
        return function(param,url){
            $.post(url,param,function(data){
                createCommTable(data.rows,null);
                pager.update(param.page,param.rows,data.total);
            });
        }
    })();
    getCommTableData({page:1,rows:4,entId:'${entId}'},"<%=request.getContextPath()%>/communicate/getComms");


    /**
     * 向后台请求数据(最近工单)
     */
    var getTableData = (function(){
        var paramCache = {};

        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#paginationLately"),{
            page:1,
            pageSize:4,
            total:0,
            onPage:function(page,pageSize){
                $.extend(paramCache,{page:page,rows:pageSize});
                getTableData(paramCache);
            }
        });
        return function(param,url){
            $.ajax({
                url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryLatelyWorkOrder?sessionKey="+ $.cookie("sessionKey"),
                dataType:'jsonp',
                data:param,
                success:function(data){
                    console.dir(data);
                    if(data.rows){
                        createTable(data.rows.dbObjects);
                    }
                    pager.update(param.page,param.rows,data.total);
                    $.extend(paramCache,param);
                }
            });
        }
    })();

    /**
     * 刷新表格(最近工单)
     */
    var createTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template-lately").html());
        var tooltipTemp = Handlebars.compile($("#tooltip-template").html());
        var $table = $("#latelyOrderGrid");

        return function(tableData){
            var html = [];
            for(var i= 0,len=tableData.length; i<len;i++){
                if((tableData[i].status !=null) && (0<=tableData[i].status)&&(tableData[i].status<=5)){
                    tableData[i].statusStr = wfStatus[tableData[i].status];
                    tableData[i].status = tableData[i].statusStr;
                }else{
                    tableData[i].statusStr = "-";
                    tableData[i].status = "-";
                }
                var $tr = $(trTemp(tableData[i]));
                $tr.data("row",i);
                var $a = $tr.find("a");

                var $tip = $(tooltipTemp(tableData[i]));
                switch(tableData[i].status)
                {
                    case wfStatus[0]:
                        $tip.find(".tip-order-state").addClass("orange");
                        $tr.find(".tip-order-state").addClass("orange");
                        break;
                    case wfStatus[1]:
                        $tip.find(".tip-order-state").addClass("red");
                        $tr.find(".tip-order-state").addClass("red");
                        break;
                    case wfStatus[2]:
                        $tip.find(".tip-order-state").addClass("blue");
                        $tr.find(".tip-order-state").addClass("blue");
                        break;
                    case wfStatus[3]:
                        $tip.find(".tip-order-state").addClass("green");
                        $tr.find(".tip-order-state").addClass("green");
                        break;
                    case wfStatus[4]:
                        $tip.find(".tip-order-state").addClass("black");
                        $tr.find(".tip-order-state").addClass("black");
                        break;
                    default:
                        $tip.find(".tip-order-state").addClass("red");
                        $tr.find(".tip-order-state").addClass("red");
                }

                $a.tooltipster({
                    content: $tip,
                    theme: 'tooltipster-shadow'
                });
                $a.data("data",tableData[i]);

                $a.click(function(){
                    var data = $(this).data("data");
                    console.dir(data);
                    var url = "<%=request.getContextPath()%>/order/detail?workId="+data.workId;
                    var title = "#" + data.workId + "-" + data.title;
                    parent.openTab(url,"order",title,true);
                });
                html.push($tr);
            }
            $table.find("tbody").empty().append(html);
        };
    })();
    
</script>
</body>
</html>