<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>工单中心</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/console.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/order.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/script/lib/summernote/summernote.css">
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote.js" ></script>
    <script src="<%=request.getContextPath()%>/script/lib/summernote/summernote-zh-CN.js" ></script>
    <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
</head>
<body>
<div id="left-part">
    <header class="part-header">
        <div class="sidebar">工单中心</div>
    </header>
    <div class="left-content">
        <div class="left-content-panel" id="allClass">
            <div class="left-content-panel-header">公共的分类<a>刷新</a></div>
            <ul class="left-content-panel-body left-part-list" id="classId"></ul>
           <!--  自定义工单模板 -->
            <ul class="left-content-panel-body left-part-list" id="tclassId"></ul>
        </div>
    </div>
</div>
<div id="right-part">
    <header class="part-header">
        <div class="sidebar-right" id="right-part-title">受理中</div>
        <div class="float-right">
         <!--    <a class="btn" href="#">导出CSV格式</a>
            <a class="btn btn-green" href="#">编辑查看分类</a> -->
        </div>
    </header>
    <div class="right-content">
        <div class="right-content-panel container">
            <div class="table-content">
                <div class="col-12 grid">
                    <table class="table footable" cellspacing="0" cellpadding="0" id="orderGrid" data-page-size="10">
                        <thead>
                        <tr class="order">
                            <th data-sort-ignore="true" class="footable-first-column"><input class="" type="checkbox"></th>
                             <c:forEach items="${fieldList}" var="item">
                  			      <th>${item.name}</th>
              				  </c:forEach>                              
                        </tr>
                        </thead>
                        <tbody></tbody>
                        <tfoot>
                        <tr>
                            <td colspan="9">
                                <div id="pagination"></div>
                            </td>
                        </tr>
                        </tfoot>
                    </table>
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

<%@include file="editWf.jsp" %>  
<script id="table-tr-template" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td> <input class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td>
        <td>{{workId}}</td>
        <td class="title"><a>{{title}}</a></td>
		<td><span class="tip-order-state">{{status}}</span></td>
        <td>{{customName}}</td>
        <td>{{createDate}}</td>
        <td>{{serviceGroupName}}</td>
        <td>{{customServiceName}}</td>
        <td>{{tempName}}</td>
    </tr>
</script>

<!-- 公共的分类 -->
<script id="condition-template" type="text/x-handlebars-template">
    {{#each titleList}}
    <li>
        <a href="#">{{this.title}}
            <span class="num">{{this.number}}</span>
        </a>
    </li>
    {{/each}}
</script>

<!-- 自定义工单模板 -->
<script id="template-condition-template" type="text/x-handlebars-template">
    {{#each tempTitleList}}
    <li data-type= '{{this.tempId}}' >
        <a href="#">{{this.title}}
            <span class="num">{{this.number}}</span>
        </a>
    </li>
    {{/each}}
</script>


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
                    <span class="col-xs-3">发起人</span><span class="col-xs-9">{{customName}}</span>
                </div>
            </div>
            {{#if event.[0]}}
            <div class="container-fluid">
                <div class="row">
                    <span class="col-xs-12">最新回复</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">内容</span><span class="col-xs-9">{{event.[0].[0]}}</span>
                </div>
                <div class="row">
                    <span class="col-xs-3">回复人</span><span class="col-xs-9">{{event.[0].[3]}}</span>
                </div>
            </div>
            {{/if}}
        </div>
    </div>
</script>

<script>
    var wfStatus = ["尚未受理","受理中","等待客户回复","已解决","已关闭"];
    

    $(function(){
	     getTableData({condition:'01',tempId:"",page:1,rows:10,refresh:1});

	     $("#classId").data("querytype","01");
	     $("#tclassId").data("querytype","");
	     
        $("#orderGrid th input[type=checkbox]").click(function(){
            $("#orderGrid td input[type=checkbox]").prop("checked",$(this).prop("checked"));
        });
        
        //添加删除按钮事件
        $("#deleteBtn").click(function(){
            var data=getSelected();
            var idStr=[];
            for(var i=0;i<data.length;i++){
                idStr.push(data[i].workId)
            }
            $("#toolbar").removeClass("show");
            //提交删除参数
             idStr = idStr.join(",");           
             if (confirm("确定要删除选中的工单！")){
            	goDelete(idStr);
            }             
        });
        
        //添加清空按钮事件
        $("#cancelBtn").click(function(){
            $("input[type=checkbox]").prop("checked",false);
            $("#toolbar").removeClass("show");
        });
        
        //刷新按钮事件
        $(".left-content-panel-header a").click(function(){
            getTableData({condition:'01',tempId:"",page:1,rows:10,refresh:1});
            $("#right-part-title").text("受理中");
        });
        
    });

    /**
     * 刷新顶部按钮
     */
    var refreshRightButton = (function(){
        var conditionTemp = Handlebars.compile($("#condition-template").html());       
        var tempConditionTemp = Handlebars.compile($("#template-condition-template").html());
        
        var $ui = $("#classId");       
        var $tui = $("#tclassId");
        
        return function(data){
            $li = $(conditionTemp(data));
            $tli = $(tempConditionTemp(data));
            
            $ui.empty().append($li);
            $tui.empty().append($tli);
            
            $("#classId li:first").addClass("active");
            
            /* 公共的分类 */
            var i=0;
            $("#classId li").each(function(){
            	var $this = $(this);
            	if(i<9){
            		$this.data("querytype","0"+(i+1));
            	}else{
            		$this.data("querytype",i+1);
            	}	
            	i=i+1;
            });
            
            /* 自定义工单模板分类*/
            $("#tclassId li").each(function(){
            	var $this = $(this);
            	$this.data("querytype",$(this).data("type"));
            });
                            
            
            /* 公共的分类点击事件 */
            $("#classId li").click(function(){          	
                var $this = $(this);  
                $("#allClass li.active").removeClass("active");             
                $this.addClass("active");
                
                getTableData({page:1,rows:10,condition:$this.data("querytype"),tempId:"",refresh:-1});
                
                $("#classId").data("querytype",$this.data("querytype"));
                $("#tclassId").data("querytype","");
                    
                var title=$(this).html().split("<")[1];                   
                title=title.split(">")[1];
                $("#right-part-title").text(title);
            });
            
           /* 自定义工单模板点击事件 */
            $("#tclassId li").click(function(){
                var $this = $(this);
                $("#allClass li.active").removeClass("active");               
                $this.addClass("active");

                getTableData({page:1,rows:10,condition:"",tempId:$this.data("querytype"),refresh:-1});
                
                $("#tclassId").data("querytype",$this.data("querytype"));
                $("#classId").data("querytype","");
                
                var title=$(this).html().split("<")[1];                   
                title=title.split(">")[1];
                $("#right-part-title").text(title);
            });
        }
    })();

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
                url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryWorkOrderCenter?sessionKey="+ $.cookie("sessionKey"),
                dataType:'jsonp',
                data:param,
                success:function(data){
                	console.dir(data);
                	if(!data.success){
                		notice.alert(data.msg);
                	}
                	if(param.refresh==1){
                        refreshRightButton(data.rows);	
                	}
                
                	$("input[type=checkbox]").prop("checked",false);
                   	$("#toolbar").removeClass("show");
                    if(data.rows) createTable(data.rows.dbObjects);
                    pager.update(param.page,param.rows,data.total);
                    $.extend(paramCache,param);
                }
            });
        }
    })();

    /**
     * 刷新表格
     */
    var createTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template").html());
        var tooltipTemp = Handlebars.compile($("#tooltip-template").html());
        var $table = $("#orderGrid");

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
                    $(".left-content-panel-header a").click();
                    
/*                  var cond=    $("#classId").data("querytype");
                    var tempId=    $("#tclassId").data("querytype");
                    getTableData({condition:cond,tempId:tempId,page:1,rows:10,refresh:1}); */
                }else{
                    notice.alert("工单数据删除失败！","alert-danger");
                }
            }
        });
    }

    //提交按钮事件
    function sumitContent(){
        $("#editOrderModal").modal("hide");
        $("#toolbar").removeClass("show");
        $("#cancelBtn").click();
    }
</script>
</body>
</html>