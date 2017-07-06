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
</head>
<body>
<div id="left-part">
    <header class="part-header">
        <div class="sidebar">首页</div>
    </header>
    <div class="left-content">
       <!-- 
        <div class="left-content-top left-content-panel">
            <h2 class="left-content-panel-header">官方信息</h2>
            <ul class="left-content-top notice">
                <li>平台通知1</li>
                <li>公司通告1</li>
            </ul>
        </div>
         -->
        <div class="left-content-bottom left-content-panel">
            <h2 class="left-content-panel-header">工单状态</h2>
            <div class="timeline" id="workActivity">
            </div>
        </div>
    </div>
</div>
<div id="right-part">
    <header class="part-header">
        <ul class="nav nav-pills">
            <li><button class="btn btn-primary btn-outline btn-sm">工单处理概况</button></li>
        </ul>
    </header>
    <div class="right-content">
        <div class="right-content-panel container">
            <div class="row">
                <nav class="panel-number-btn">
                    <ul id="queryButtons">
                        <li data-querytype="1" class="ember-view active">
                            <a><span class="red" id="queryHandleWorkOrder">-</span><p>我处理中的工单</p></a></li>
                        <li data-querytype="2" class="ember-view">
                            <a><span class="blue" id="queryMyGroupHandleWorkOrder">-</span><p>我客服组处理中的工单</p></a></li>
                        <li data-querytype="3" class="ember-view">
                            <a><span class="green" id="queryServiceGroupUnAssignWorkOrder">-</span><p>我客服组里未分配的工单</p></a>
                        </li>
                          <!-- 
                        <li data-querytype="4" class="ember-view">
                            <a><span class="orange" id="queryGoodEvaluateWorkOrder">-</span><p>有满意度评价的工单</p></a>
                        </li> -->
                       
                        <li data-querytype="5" class="ember-view">
                            <a><span class="purple" id="queryDocument">-</span><p>知识库文档</p></a>
                        </li>
                        
                    </ul>
                </nav>
            </div>
            <div class="row table-content">
                <div class="col-12 grid">
                    <table class="table footable" cellspacing="0" cellpadding="0" id="orderGrid" data-page-size="10">
                        <thead>
                        <tr class="order">
                            <th data-sort-ignore="true" class="footable-first-column"><input class="" type="checkbox"></th>
                            <th>编号</th>
                            <th>标题</th>
                            <th>状态</th>
                            <th>工单发起人</th>
                            <th>创建日期</th>
                            <th>受理客服组</th>
                            <th>工单受理客服</th>
                        </tr>
                        </thead>
                        <tbody></tbody>
                        <tfoot>
                        <tr>
                            <td colspan="8">
                                <div id="pagination"></div>
                            </td>
                        </tr>
                        </tfoot>
                    </table>        
                                       
                    <!--  知识库文档 -->
                    <table class="table footable" cellspacing="0" cellpadding="0" id="knowledgeTable" data-page-size="10" style="display:none">
                        <thead>
                        <tr class="order">
                            <th>编号</th>
                            <th>标题</th>
                            <th>作者</th>
                            <!-- <th>分类</th> -->
                            <th>发布</th>
                            <th>最后更新</th>
<!--                             <th>查看</th>
                            <th>评论</th> -->
                        </tr>
                        </thead>
                        <tbody></tbody>
                        <tfoot>
                        <tr>
                            <td colspan="8">
                                <div id="pagination1" align="right"></div>
                            </td>
                        </tr>
                        </tfoot>
                    </table>
                    
                   <!-- 没有内容-->
                   <div class="body" id="noContent" style="display:none">
                                                          没有内容 
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

<script id="workCenter-template" type="text/x-handlebars-template">
    <div class="timeline-item">
        <div class="row">
            <div class="col-xs-3 date">
                <i class="fa fa-briefcase"></i>
                <br>
                <small class="text-navy">{{timeSpace}}</small>
            </div>
            <div class="col-xs-7 content no-top-border">
                <p class="m-b-xs"><strong><a href="#">{{updatorName}}</a> 回复了<a href="#"> # {{workId}} {{title}}</a></strong></p>
                <p>{{{content}}}</p>
            </div>
        </div>
    </div>
</script>
<script id="table-tr-template" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td> <input class="ember-view ember-checkbox table-checkbox" type="checkbox" > </td>
        <td>{{workId}}</td>
        <td class="title"><a>{{title}}</a></td>
		<td><span class="tip-order-state">{{status}}</span></td>
        <td>{{creatorName}}</td>
        <td>{{createDate}}</td>
        <td>{{serviceGroupName}}</td>
        <td>{{customServiceName}}</td>
    </tr>
</script>
<script id="tooltip-template" type="text/x-handlebars-template">
    <div class="tip-content panel panel-default md-whiteframe-2dp">
        <div class="tip-header panel-heading">
            <span class="col-12"># {{workId}}</span><span class="tip-order-state">{{statusStr}}</span>
        </div>
        <div class="tip-row panel-body">
            <div class="container-fluid">
                <div class="row">
                    <span class="col-xs-3">标题 :</span><span class="col-xs-9">{{title}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">时间 :</span><span class="col-xs-9">{{createDate}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">发起人 :</span><span class="col-xs-9">{{creatorName}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">问题描述 :</span><span class="col-xs-9">{{{issue}}}</span>
                </div>
            </div>
            {{#if newevent}}
            <div class="container-fluid">
                <div class="row">
                    <span class="col-xs-12">最新回复</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">内容 :</span><span class="col-xs-9">{{{newevent.content}}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">时间 :</span><span class="col-xs-9">{{newevent.contentUpdateDate}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">回复人 :</span><span class="col-xs-9">{{newevent.updatorName}}</span>
                </div>
            </div>
            {{/if}}
        </div>
    </div>
</script>

<!-- 文档模板 -->
<script id="document-template" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td>{{docId}}</td>
        <td class="title"><a onclick="viewDetails('{{docId}}','{{title}}')">{{title}}</a></td>
        <td>{{author}}</td>
        <td>{{createTime}}</td>
        <td>{{updateTime}}</td>          
    </tr>
</script>

<script>
    var wfStatus = ["尚未受理","受理中","等待回复","已解决","已关闭"];
    $(function(){
        $("#recentStatistic").click(function(){
            window.location.href="<%=request.getContextPath()%>/reportforms/index";
        });
        getTableData({queryType:'1',page:1,rows:10});
        getDocNum();
        
        $("#queryButtons").data("querytype",$("#queryButtons li.active").data("querytype"));
        $("#queryButtons li").click(function(){       	
            var $this = $(this);
            $this.siblings().removeClass("active");
            $this.addClass("active");
            if($("#queryButtons").data("querytype")!=$this.data("querytype") && $(this).data("querytype")!="5"){
            	$("#knowledgeTable").hide();
            	$("#orderGrid").show();
            	$("#noContent").hide(); 
            	
                getTableData({page:1,rows:10,queryType:$this.data("querytype")});
                $("#queryButtons").data("querytype",$this.data("querytype"));
            }
            if($("#queryButtons").data("querytype")!=$this.data("querytype") && $(this).data("querytype")=="5"){
            	$("#knowledgeTable").show();
            	$("#orderGrid").hide();
            	$("#noContent").hide(); 
            	
                getDocTableData({page:1,rows:10});
                $("#queryButtons").data("querytype",$this.data("querytype"));
            }           
        }); 
              
        $("#orderGrid th input[type=checkbox]").click(function(){
            $("#orderGrid td input[type=checkbox]").prop("checked",$(this).prop("checked"));
        });

        //添加删除按钮事件
        $("#deleteBtn").click(function(){
            var data=getSelected();
            var idStr=[];
            for(var i=0;i<data.length;i++){
                idStr.push(data[i].workId);
            }
            $("#toolbar").removeClass("show");
            //提交删除参数
            idStr = idStr.join(",");
            goDelete(idStr);
        });

        //添加清空按钮事件
        $("#cancelBtn").click(function(){
            $("input[type=checkbox]").prop("checked",false);
            $("#toolbar").removeClass("show");
        });
        setWorkOrderActivity();
               
    });

    /**
     * 刷新顶部按钮
     */
    var refreshTopButton = function(rows){
        if(rows.queryHandleWorkOrder!=-1){
            $("#queryHandleWorkOrder").text(rows.queryHandleWorkOrder);
        }
        if(rows.queryMyGroupHandleWorkOrder!=-1){
            $("#queryMyGroupHandleWorkOrder").text(rows.queryMyGroupHandleWorkOrder);
        }
        if(rows.queryServiceGroupUnAssignWorkOrder!=-1){
            $("#queryServiceGroupUnAssignWorkOrder").text(rows.queryServiceGroupUnAssignWorkOrder);
        }
        if(rows.queryGoodEvaluateWorkOrder!=-1){
            $("#queryGoodEvaluateWorkOrder").text(rows.queryGoodEvaluateWorkOrder);
        } 

    };

    /**
     * 向后台请求数据
     */
    var getTableData = (function(){
        var paramCache = {};

        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#pagination"),{
            page:1,
            pageSize:10,
            total:0,
            onPage:function(page,pageSize){
                $.extend(paramCache,{page:page,rows:pageSize});
                getTableData(paramCache);
            }
        });
        return function(param,url){
            $.ajax({
                url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryHandleWorkOrder?sessionKey="+ $.cookie("sessionKey"),
                dataType:'jsonp',
                data:param,
                success:function(data){
                    $("input[type=checkbox]").prop("checked",false);
                    $("#toolbar").removeClass("show");
                    console.dir(data);
                    if(data.success){
                        refreshTopButton(data.rows);
                        createTable(data.rows.list);
                        pager.update(param.page,param.rows,data.total);
                        $.extend(paramCache,param);
                    }
                    else{
                        notice.alert('请求数据失败','alert-danger');
                    }
                }
            });
        }
    })();

            
    /**
     * 向后台请求数据(文档)
     */
    var getDocTableData = (function(){
        var paramCache = {};
        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#pagination1"),{
            page:1,
            pageSize:10,
            total:0,
            onPage:function(page,pageSize){
                $.extend(paramCache,{page:page,rows:pageSize});
                getDocTableData(paramCache);
            }
        });
        return function(param,url){
            $.ajax({
                url:"<%=request.getContextPath()%>/help/document/queryDocBelongMe",
                dataType:'json',
                data:param,
                success:function(data){
                    $("input[type=checkbox]").prop("checked",false);
                    $("#toolbar").removeClass("show");
                    console.dir(data);
                    if(data.success){
                    	if(data.rows.length=="0"){
                   		   $("#knowledgeTable").hide();
                   		   $("#noContent").show();
                         }else{
                   		   $("#knowledgeTable").show();
                  		   $("#noContent").hide(); 
                  		   
                           createDocTable(data.rows);
                           pager.update(param.page,param.rows,data.total);
                           $.extend(paramCache,param);
                         }
                    }else{
                        notice.alert('请求数据失败','alert-danger');
                    }
                }
            });
        }
    })();
    
    
    /**
     * 刷新表格(文档)
     */
     var createDocTable = (function(){
         var trTemp = Handlebars.compile($("#document-template").html());
         var $table = $("#knowledgeTable");
         
         return function(tableData, type){
             var html = [];

             for(var i= 0,len=tableData.length; i<len;i++){            	
                 var $tr = $(trTemp(tableData[i]));             
                 $tr.data("data",tableData[i]);
                 html.push($tr);     
             }
           
             $table.find("tbody").empty().append(html);             
         };
     })();
    
    
    /**
     * 刷新表格
     */
    var createTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template").html());
        var tooltipTemp = Handlebars.compile($("#tooltip-template").html());
        var $table = $("#orderGrid");
        //记录状态为关闭状态时的计数器
        var count = 0;
        return function(tableData){
            var html = [];
            for(var i= 0,len=tableData.length; i<len;i++){
                var $tr = $(trTemp(tableData[i]));
                $tr.data("row",i);
                var $a = $tr.find("a");
                //将状态转为字符串
                if((tableData[i].status !=null) && (0<=tableData[i].status)&&(tableData[i].status<=5)){
                    tableData[i].statusStr = wfStatus[tableData[i].status];
                    tableData[i].status = wfStatus[tableData[i].status];
                }else{
                    tableData[i].statusStr = "-";
                    tableData[i].status="-";
                }
                
                var $tr = $(trTemp(tableData[i]));
                $tr.data("row",i);
                var $a = $tr.find("a");
                //处理最新回复
                if(tableData[i].event != null && tableData[i].event.length >1){
                    for(var num = tableData[i].event.length -1; num>0; num--){
                        if(tableData[i].event[num].content){
                        	if(tableData[i].event[num].content!=null && tableData[i].event[num].content!=""){
                                tableData[i].newevent = tableData[i].event[num];
                                tableData[i].newevent.contentUpdateDate =   cri.formatDate(new Date(tableData[i].newevent.contentUpdateDate),"yyyy年MM月dd日 HH:mm");
                                break;
                        	}
                        }
                    }
                }
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
                        $tr.find('input').remove();
                        count++;
                        break;
                    default:
                        $tip.find(".tip-order-state").addClass("red");
                }
                if(count == tableData[i]){
                    $("thead input[type=checkbox]").remove();
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
                    parent.openTab(url,"order",title);
                });
                html.push($tr);
            }
            $table.find("tbody").empty().append(html);
            $table.find("input[type=checkbox]").change(function(){
                var data =  getSelected();

                //比较所选的checkbox的个数与所请求的数据的个数是否相同，如果相同则选中表格头的checkbox，若不相同则不选择表格头中的checkbox；
                if(data.length == tableData.length){
                    $("thead input[type=checkbox]").prop("checked",true);
                }else if(data.length < tableData.length){
                    $("thead input[type=checkbox]").prop("checked",false);
                }

                if($table.find('td input[type=checkbox]:checked').length){
                    $("#editBtn").text("编辑 "+data.length+" 张工单");
                    $("#toolbar").addClass("show");
                }
                else{
                    $("#toolbar").removeClass("show");
                }
            });
            window.getSelected=function(){
                var data=[];
                $("#orderGrid td input[type=checkbox]").each(function(){
                    if($(this).prop("checked")){
                        var index=parseInt($(this).closest("tr").data("row"));
                        data.push(tableData[index]);
                    }
                });
                return data;
            };
        };
    })();

    function goDelete(idStr){
        var param={};
        param.deleteId=idStr;
        $.ajax({
            url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/deleteWorkOrder?sessionKey="+ $.cookie("sessionKey"),
            dataType:'jsonp',
            data:param,
            success:function(data){
                if(data.success){
                    notice.alert("工单数据删除成功！","alert-success");
                    var getCurrentData = $("#queryButtons li.active").data("querytype");
                    getTableData({queryType:getCurrentData,page:1,rows:10});
                }else{
                    notice.alert("工单数据删除失败！","alert-danger");
                }
            }
        });
    }

    function setWorkOrderActivity(){
        $.ajax({
            url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryWorkOrderActivity?sessionKey="+ $.cookie("sessionKey"),
            dataType:'jsonp',
            success:function(data){
                if(data.success){
                    console.dir(data);
                    createWorkActivity(data.rows);
                }else{
                    notice.alert("工单数据删除失败！","alert-danger");
                }
            }
        });
    }

    /**
     *创建工单状态
     */
    var createWorkActivity = (function(){
        var eventTemp = Handlebars.compile($("#workCenter-template").html());
        var $eventList = $("#workActivity");

        return function(eventList){
            var eventHtml = [];
            for(var i= 0,len=eventList.length; i<len;i++){
                var e = eventList[i];
                e.updatorName = e.event[e.event.length-1].updatorName;
                eventHtml.push($(eventTemp(e)));
            }
                $eventList.empty().html(eventHtml);
            
        };
    })();
    
    /* 我发布的文档数量显示在控制台 */
    function getDocNum(){
    	var param={};
    	param.page=1;
    	param.rows=10;
    	$.post("<%=request.getContextPath()%>/help/document/queryDocBelongMe",param,numCallBack,"json");
    }
    
    /* 文档数量回调函数 */
    function numCallBack(data){
    	if(data.success){
    		$("#queryDocument").text(data.total);
    	}else{
    		notice.alert(data.msg,"alert-danger");
    	}
    }
    
    /* 查看文档详情 */
     function viewDetails(docId,title){
        var url="<%=request.getContextPath()%>/help/document/viewDetails?docId="+docId;
        parent.openTab(url,"order",title);
    } 
</script>
</body>
</html>