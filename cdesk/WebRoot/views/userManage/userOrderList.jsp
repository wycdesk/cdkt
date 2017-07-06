<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="bottom" id="bottom">
    <ul class="tab-title1">
        <li class="active" id="workOrders">
            <a id="workOrder">工单 </a>
        </li>
        <li id="distributionWO">
            <a id="distributionWOs">分配工单 </a>
        </li>
    </ul>
</div>
<div>
    <div class="panel-body">
        <div class="sub-switch-body" id="sorts">
            <div class="body">
                <div class="sub-switch-menu" id="oStatus">
                    <ul>
                        <li id="allStatus" class="active"><a id="allStatuss">全部</a></li>
                    </ul>
                    <ul>
                        <li id="unAccept" style="display:none"><a id="unAccepts">尚未受理</a></li>
                    </ul>
                    <ul>
                        <li id="accept" style="display:none"><a id="accepts">受理中</a></li>
                    </ul>
                    <ul>
                        <li id="waitReply" style="display:none"><a id="waitReplys">等待回复</a></li>
                    </ul>
                    <ul>
                        <li id="resolve" style="display:none"><a id="resolves">已解决</a></li>
                    </ul>
                    <ul>
                        <li id="close" style="display:none"><a id="closes">已关闭</a></li>
                    </ul>
                </div>

                <div class="sub-switch-menu" id="oStatus1" style="display:none">
                    <ul>
                        <li id="allStatus1" class="active"><a id="allStatuss1">全部</a></li>
                    </ul>
                    <ul>
                        <li id="unAccept1" style="display:none"><a id="unAccepts1">尚未受理</a></li>
                    </ul>
                    <ul>
                        <li id="accept1" style="display:none"><a id="accepts1">受理中</a></li>
                    </ul>
                    <ul>
                        <li id="waitReply1" style="display:none"><a id="waitReplys1">等待回复</a></li>
                    </ul>
                    <ul>
                        <li id="resolve1" style="display:none"><a id="resolves1">已解决</a></li>
                    </ul>
                    <ul>
                        <li id="close1" style="display:none"><a id="closes1">已关闭</a></li>
                    </ul>
                </div>

                <div class="sub-switch-menu" id="noOrderGrid" >
                    <!--  没有工单 -->
                </div>

                <table class="table footable" cellspacing="0" cellpadding="0" id="orderGrid" data-page-size="10">
                    <thead>
                    <tr class="order">
                        <th data-sort-ignore="true" class="footable-first-column"><input type="checkbox" id="allSelect"></th>
                        <th>编号</th>
                        <th>标题</th>
                        <th>工单发起人</th>
                        <th>创建日期</th>
                        <th>受理客服组</th>
                        <th>工单受理客服</th>
                        <th>状态</th>
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
            </div>
        </div>
    </div>
</div>
<div id="toolbar"  style="left:15%;right:15%;">
    <button id="deleteBtn" type="button" class="btn btn-raised left-btn btn-danger">删除</button>
    <button id="cancelBtn" type="button" class="btn btn-raised btn-default">清除选择</button>
    <button id="editBtn" type="button" class="btn btn-raised btn-primary" data-toggle="modal" data-target="#editOrderModal"></button>
</div>

<script id="table-tr-template" type="text/x-handlebars-template">
     
    <tr class="ember-view" data-workId='{{workId}}'>
        <td> <input class="ember-view ember-checkbox table-checkbox" type="checkbox"> </td>
        <td>{{workId}}</td>
        <td class="title"><a>{{title}}</a></td>
        <td>{{customName}}</td>
        <td>{{createDate}}</td>
        <td>{{serviceGroupName}}</td>
        <td>{{customServiceName}}</td>
        
        <td><span class="{{#equal status '0'}}status orange{{/equal}}{{#equal status '1'}}status red{{/equal}}{{#equal status '2'}}status blue{{/equal}}{{#equal status '3'}}status green{{/equal}}{{#equal status '4'}}black{{/equal}}">{{statusStr}}</span></td>
    </tr>
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
    var orderStatus="-1";
    var orderStatusStr="";
    var wfStatus = ["尚未受理","受理中","等待回复","已解决","已关闭"];
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
            if($("#workOrders").attr("class")=="active"){
            	getTableData(paramCache);
            }
            if($("#distributionWO").attr("class")=="active"){
                getTableData1(paramCache);
            }
        }
    }); 
    
    $(document).ready(function() {
    	if("${user.userType}"=="1"){
    		$("#distributionWO").css("display","none");
    	}
    	getTableData({customId:'${user.userId}', userType: '${user.userType}',status:orderStatus,page:1,rows:10}); 
    	
    	getAssignTotal();
    	
    	$("#orderGrid th input[type=checkbox]").click(function(){
            $("#orderGrid td input[type=checkbox]").prop("checked",$(this).prop("checked"));         
        });
    
        //添加删除按钮事件
        $("#deleteBtn").click(function(){
            var idStr = [];
            idStr = getSelected();
            //提交删除参数
            idStr = idStr.join(",");
            goDelete(idStr);
        }); 
        
        //添加清空按钮事件
        $("#cancelBtn").click(function(){
            $("input[type=checkbox]").prop("checked",false);
            $("#toolbar").removeClass("show");
        });  
    });
    
    /* 获取被分配工单的总数 */
    function getAssignTotal(){
        $.ajax({
        	url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryAssignToMe?sessionKey="+ $.cookie("sessionKey"),
            dataType:'jsonp',
            data:{customServiceId:'${user.userId}',status:orderStatus,page:1,rows:10},
            success:function(data){
                 if(data.success){
                	 $("#distributionWOs").text("分配工单（"+data.total+"）");
                 }
            }
        });
    }
    
    /**
     * 向后台请求数据-工单
     */
    var getTableData = (function(){

        return function(param,url){	  
            $.ajax({
                url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryCreatorAllWorkOrder?sessionKey="+ $.cookie("sessionKey"),
                dataType:'jsonp',
                data:param,
                success:function(data){
                     console.dir(data);
                     if(!data.success){
                    	 notice.alert(data.msg);
                     }
                     if($("#allStatus").attr("class")=="active"){
                    	 createTable(data.rows.list,data.total,data.rows);
                     }
                     if($("#unAccept").prop("class")=="active"||$("#accept").prop("class")=="active"||$("#waitReply").prop("class")=="active"||$("#resolve").prop("class")=="active"||$("#close").prop("class")=="active"){
                    	 createTable1(data.rows.list);
                     }
                     pager.update(param.page,param.rows,data.total);
                     $.extend(paramCache,param);
                }
            });
        }
    })(); 
    
    /**
     * 向后台请求数据-分配工单
     */
    var getTableData1 = (function(){
        
        return function(param,url){      	
            $.ajax({
                url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryAssignToMe?sessionKey="+ $.cookie("sessionKey"),
                dataType:'jsonp',
                data:param,
                success:function(data){
                     console.dir(data);
                     
                     if($("#allStatus1").attr("class")=="active"){
                    	 createDistrTable(data.rows.list,data.total,data.rows);
                     }
                     if($("#unAccept1").prop("class")=="active"||$("#accept1").prop("class")=="active"||$("#waitReply1").prop("class")=="active"||$("#resolve1").prop("class")=="active"||$("#close1").prop("class")=="active"){
                    	 createTable1(data.rows.list);
                     }
                     pager.update(param.page,param.rows,data.total);
                     $.extend(paramCache,param);
                }
            });
        }
    })();
    
    /* 刷新表格-分配工单 */
    var createDistrTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template").html());
        var tooltipTemp = Handlebars.compile($("#tooltip-template").html());     
        var $table = $("#orderGrid");

        return function(tableData,data,rows){
            var html = [];
            
            var noAccept=[];
            var accept=[];
            var waitingForCustom=[];
            var resolve=[];
            var close=[];
            
            if(data=="0"){     	
            	$("#orderGrid").hide();
            	$("#noOrderGrid").show();
            }else{	
            	$("#orderGrid").show();
            	$("#noOrderGrid").hide();
            }
            for(var i= 0,len=tableData.length; i<len;i++){ 
               if((tableData[i].status !=null) && (0<=tableData[i].status)&&(tableData[i].status<=5)){
               	 tableData[i].statusStr = wfStatus[tableData[i].status];
               }else{
               	 tableData[i].statusStr = "-";
               } 

               if(tableData[i].statusStr=="尚未受理"){
            	   noAccept.push(tableData[i]);
               }
               if(tableData[i].statusStr=="受理中"){
            	   accept.push(tableData[i]);
               } 
               if(tableData[i].statusStr=="等待回复"){
            	   waitingForCustom.push(tableData[i]);
               }
               if(tableData[i].statusStr=="已解决"){
            	   resolve.push(tableData[i]);
               }
               if(tableData[i].statusStr=="已关闭"){
            	   close.push(tableData[i]);
               } 
            }
          
            /* 总的分类标签和列表中分类的行头 */
            if(rows.noaccept){
            	$("#unAccepts1").text("尚未受理（"+rows.noaccept+"）");
            	$("#unAccept1").css("display","block");
            }else
            	$("#unAccept1").css("display","none");
            if(noAccept.length){
            	html.push('<tr><th colspan="8">尚未受理</th><tr>');
            }
            createTr(noAccept,trTemp,tooltipTemp,html);

            if(rows.accept){
            	$("#accepts1").text("受理中（"+rows.accept+"）");            	
            	$("#accept1").css("display","block");
            }else
            	$("#accept1").css("display","none");
            if(accept.length){
            	html.push('<tr><th colspan="8">受理中</th><tr>');
            }
            createTr(accept,trTemp,tooltipTemp,html);
            
            if(rows.waitingforcutom){
            	$("#waitReplys1").text("等待回复（"+rows.waitingforcutom+"）");           	
            	$("#waitReply1").css("display","block");
            }else
            	$("#waitReply1").css("display","none");
            if(waitingForCustom.length){
            	html.push('<tr><th colspan="8">等待回复</th><tr>');
            }
            createTr(waitingForCustom,trTemp,tooltipTemp,html);
            
            if(rows.resolve){
            	$("#resolves1").text("已解决（"+rows.resolve+"）");            	
            	$("#resolve1").css("display","block");
            }else
            	$("#resolve1").css("display","none");
            if(resolve.length){
            	html.push('<tr><th colspan="8">已解决</th><tr>');
            }
            createTr(resolve,trTemp,tooltipTemp,html);
            
            if(rows.close){
            	$("#closes1").text("已关闭（"+rows.close+"）");            	
            	$("#close1").css("display","block");
            }else
            	$("#close1").css("display","none");
            if(close.length){
            	html.push('<tr><th colspan="8">已关闭</th><tr>');
            }
            createTr(close,trTemp,tooltipTemp,html);
            
            $table.find("tbody").empty().append(html);
            
            $table.find("input[type=checkbox]").change(function(){
            	var $t = $(this).parent();
            	if($t.is("th")){
                    $table.find("td input[type=checkbox]").prop("checked",$(this).prop("checked"));
                }
            	var length = $table.find('td input[type=checkbox]:checked').length;           	
                if(!$t.is("th")){
                	if(tableData.length==length){
                		$("#allSelect").prop("checked",true);
                	}
                	else{
                		$("#allSelect").prop("checked",false);
                	}
                  }           	
                if($table.find('td input[type=checkbox]:checked').length){
                	$("#editBtn").text("编辑 "+length+" 张工单");
                    $("#toolbar").addClass("show");
                }
                else{
                    $("#toolbar").removeClass("show");
                }              
            });
        };
    })(); 
    
    /**
     * 刷新表格-工单
     */
    var createTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template").html());
        var tooltipTemp = Handlebars.compile($("#tooltip-template").html());     
        var $table = $("#orderGrid");

        return function(tableData,data,rows){
            var html = [];
            
            var noAccept=[];
            var accept=[];
            var waitingForCustom=[];
            var resolve=[];
            var close=[];
 
            $("#workOrder").text("工单（"+data+"）");
            
            if(data=="0"){
            	$("#orderGrid").hide();
            	$("#noOrderGrid").show();
            }else{
            	$("#orderGrid").show();
            	$("#noOrderGrid").hide();
            }
            for(var i= 0,len=tableData.length; i<len;i++){ 
               if((tableData[i].status !=null) && (0<=tableData[i].status)&&(tableData[i].status<=5)){
               	 tableData[i].statusStr = wfStatus[tableData[i].status];
               }else{
               	 tableData[i].statusStr = "-";
               } 

               if(tableData[i].statusStr=="尚未受理"){
            	   noAccept.push(tableData[i]);
               }
               if(tableData[i].statusStr=="受理中"){
            	   accept.push(tableData[i]);
               } 
               if(tableData[i].statusStr=="等待回复"){
            	   waitingForCustom.push(tableData[i]);
               }
               if(tableData[i].statusStr=="已解决"){
            	   resolve.push(tableData[i]);
               }
               if(tableData[i].statusStr=="已关闭"){
            	   close.push(tableData[i]);
               }     	 
            }
            
            /* 总的分类标签和列表中分类的行头 */
            if(rows.noaccept){
            	$("#unAccepts").text("尚未受理（"+rows.noaccept+"）");
            	$("#unAccept").css("display","block");
            }else
            	$("#unAccept").css("display","none");
            if(noAccept.length){
            	html.push('<tr><th colspan="8">尚未受理</th><tr>');
            }
            createTr(noAccept,trTemp,tooltipTemp,html);

            if(rows.accept){
            	$("#accepts").text("受理中（"+rows.accept+"）");            	
            	$("#accept").css("display","block");
            }else
            	$("#accept").css("display","none");
            if(accept.length){
            	html.push('<tr><th colspan="8">受理中</th><tr>');
            }
            createTr(accept,trTemp,tooltipTemp,html);
            
            if(rows.waitingforcutom){
            	$("#waitReplys").text("等待回复（"+rows.waitingforcutom+"）");           	
            	$("#waitReply").css("display","block");
            }else
            	$("#waitReply").css("display","none");
            if(waitingForCustom.length){
            	html.push('<tr><th colspan="8">等待回复</th><tr>');
            }
            createTr(waitingForCustom,trTemp,tooltipTemp,html);
            
            if(rows.resolve){
            	$("#resolves").text("已解决（"+rows.resolve+"）");            	
            	$("#resolve").css("display","block");
            }else
            	$("#resolve").css("display","none");
            if(resolve.length){
            	html.push('<tr><th colspan="8">已解决</th><tr>');
            }
            createTr(resolve,trTemp,tooltipTemp,html);
            
            if(rows.close){
            	$("#closes").text("已关闭（"+rows.close+"）");            	
            	$("#close").css("display","block");
            }else
            	$("#close").css("display","none");
            if(close.length){
            	html.push('<tr><th colspan="8">已关闭</th><tr>');
            }
            createTr(close,trTemp,tooltipTemp,html);
                    
            $table.find("tbody").empty().append(html);
            
            $table.find("input[type=checkbox]").change(function(){	
            	var $t = $(this).parent();
            	if($t.is("th")){
                    $table.find("td input[type=checkbox]").prop("checked",$(this).prop("checked"));
                }
            	var length = $table.find('td input[type=checkbox]:checked').length;
                if(!$t.is("th")){
                	if(tableData.length==length){
                		$("#allSelect").prop("checked",true);
                	}
                	else{
                		$("#allSelect").prop("checked",false);
                	}
                  }           	
                if($table.find('td input[type=checkbox]:checked').length){
                	$("#editBtn").text("编辑 "+length+" 张工单");
                    $("#toolbar").addClass("show");
                }
                else{
                    $("#toolbar").removeClass("show");
                }
            }); 
        };
    })();
    
    var getSelected = function(){
        var data=[];
        $("#orderGrid td input[type=checkbox]").each(function(){
            if($(this).prop("checked")){
                var workId = $(this).closest("tr").attr("data-workId");
                data.push(workId);
            }
        });
        return data;
    };
    
    /* 点击工单分类显示 */
     var createTable1 = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template").html());
        var tooltipTemp = Handlebars.compile($("#tooltip-template").html());     
        var $table = $("#orderGrid");

        return function(tableData){
            var html = [];
            var status=[];
            for(var i= 0,len=tableData.length; i<len;i++){    	
               if((tableData[i].status !=null) && (0<=tableData[i].status)&&(tableData[i].status<=5)){
            	   if(tableData[i].status==orderStatus){
                	   status.push(tableData[i]);   
                   }           	   
               } 
            }
            for(var i=0;i<status.length;i++){
            	status[i].statusStr = wfStatus[status[i].status];
            }
            /* 去掉行头括号里的文字 */
            var reg=/\（.*?）/g;
            orderStatusStr=orderStatusStr.replace(reg,"");
            
            html.push('<tr><th colspan="8">'+orderStatusStr+'</th><tr>');
            createTr(status,trTemp,tooltipTemp,html);
            
            $table.find("tbody").empty().append(html);
            
            $table.find("input[type=checkbox]").change(function(){           	
            	var $t = $(this).parent();
            	if($t.is("th")){
                    $table.find("td input[type=checkbox]").prop("checked",$(this).prop("checked"));
                }

            	var length = $table.find('td input[type=checkbox]:checked').length;
            	
                if(!$t.is("th")){
                	if(tableData.length==length){
                		$("#allSelect").prop("checked",true);
                	}
                	else{
                		$("#allSelect").prop("checked",false);
                	}
                  }
            	
                if($table.find('td input[type=checkbox]:checked').length){
                	$("#editBtn").text("编辑 "+length+" 张工单");
                    $("#toolbar").addClass("show");
                }
                else{
                    $("#toolbar").removeClass("show");
                } 
            }); 
        };
    })();       
    
    /* 根据不同的受理状态归类产生行数据 */
    function createTr(trArray,trTemp,tooltipTemp,html){
        if(trArray.length!=0){
     	   for(var i=0;i<trArray.length;i++){
     		   var $tr = $(trTemp(trArray[i]));
                $tr.data("row",i);
                var $a = $tr.find("a");
                var $tip = $(tooltipTemp(trArray[i]));
                     
    switch(trArray[i].status)
        {            
        case "0":
            $tip.find(".tip-order-state").addClass("orange");
            break;
        case "1":
            $tip.find(".tip-order-state").addClass("red");
            break;
        case "2":
        	$tip.find(".tip-order-state").addClass("blue");
            break;
        case "3":
        	$tip.find(".tip-order-state").addClass("green");
            break;
        case "4":
        	$tip.find(".tip-order-state").addClass("black");
            break;
        default:
        	$tip.find(".tip-order-state").addClass("red");
        } 
         $a.tooltipster({
             content: $tip,
             theme: 'tooltipster-shadow'
         });
         $a.data("data",trArray[i]);

         $a.click(function(){
             var data = $(this).data("data");
             console.dir(data);
             var url = "<%=request.getContextPath()%>/order/detail?workId="+data.workId;
             var title = "#" + data.workId + "-" + data.title;
             parent.openTab(url,"order",title,true);
         });
         if(!$a.text()){
        	 $a.parent().css("cursor","pointer")
             $a.parent().click(function(){
            	 $(this).find("a").click();
             });
         }
        
        
         html.push($tr);
        }
      }
    }

     function goDelete(idStr){
        var param={};
        param.deleteId=idStr;
        
        if(confirm("确定要删除选中的工单？")){
        	 $.ajax({
                 url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/deleteWorkOrder?sessionKey="+ $.cookie("sessionKey"),
                 dataType:'jsonp',
                 data:param,
                 success:function(data){
                 	if(data.success){
                     	notice.success("工单数据删除成功！");
                     	$("#toolbar").removeClass("show");              
                     	 if($("#workOrders").prop("class")=="active"){
                     		$("#workOrders").click(); 
                     		getAssignTotal();
                     	 }
                     	 if($("#distributionWO").prop("class")=="active"){
                     		$("#distributionWO").click();
                     		getAssignTotal();
                     	 }                     	                 	
                     }else{
                     	notice.danger("工单数据删除失败！");
                     }
                 }
             });
        }  
    }
    
    //提交按钮事件
    function sumitContent(){
    	$("#editOrderModal").modal("hide");
    	$("#toolbar").removeClass("show");
    	 $("#cancelBtn").click();
    }
    
    /* 工单受理状态分类li */
    $("#oStatus li").click(function(){
    	$("#allSelect").prop("checked",false);
    	$("#toolbar").removeClass("show");
    	
    	$("#oStatus li").removeClass("active");
    	$(this).addClass("active");
    	orderStatusStr=$(this).text();
    	orderStatus=getOrderStatus(orderStatusStr);
  	
    	getTableData({customId:'${user.userId}', userType: '${user.userType}',status:orderStatus,page:1,rows:10}); 
    	$("#grid").show();
    });
    
    /* 分配工单受理状态分类li */
    $("#oStatus1 li").click(function(){
    	$("#allSelect").prop("checked",false);
    	$("#toolbar").removeClass("show");
    	
    	$("#oStatus1 li").removeClass("active");
    	$(this).addClass("active");
    	orderStatusStr=$(this).text();
    	orderStatus=getOrderStatus(orderStatusStr);
  	
    	getTableData1({customServiceId:'${user.userId}',status:orderStatus,page:1,rows:10}); 
    	$("#grid").show();
    });
    
    /* 工单状态的转换 */
    function getOrderStatus(orderStatusStr){
    	var orderStatus="";
    	if(orderStatusStr.substr(0,4)=="尚未受理"){   
    		orderStatus="0";
    	}else if(orderStatusStr.substr(0,3)=="受理中"){
    		orderStatus="1";
    	}else if(orderStatusStr.substr(0,4)=="等待回复"){
    		orderStatus="2";
    	}else if(orderStatusStr.substr(0,3)=="已解决"){
    		orderStatus="3";
    	}else if(orderStatusStr.substr(0,3)=="已关闭"){
    		orderStatus="4";
    	}else if(orderStatusStr=="全部"){
    		orderStatus="-1";
    	}
    	return orderStatus;
    }
    
    /* 分配工单 */
    $("#distributionWO").click(function(){
    	
    	$("#bottom li").removeClass("active");
    	$(this).addClass("active");
    	$("#oStatus").css("display","none");
    	$("#oStatus1").css("display","block");
    	
    	$("#allStatus1").click();
    });
    
    /* 工单 */
    $("#workOrders").click(function(){
    	
       	$("#bottom li").removeClass("active");
    	$(this).addClass("active");
    	$("#oStatus").css("display","block");
    	$("#oStatus1").css("display","none");
    	
    	$("#allStatus").click();
    });
    
 </script>
