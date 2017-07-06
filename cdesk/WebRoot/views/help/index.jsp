<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>帮助中心</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/help.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/console.css">
</head>
<body class=" pace-done">

<div id="left-part">
    <header class="part-header">
        <div class="sidebar">帮助中心</div>
    </header>
    <div class="left-content">
        <div class="left-content-panel">
            <div class="left-content-panel-header">文档知识库</div>
            <ul class="left-content-panel-body left-part-list">
                <li class="active" id="manage">
                    <a>所有知识库文档</a>
                </li>
                <li id="publish">
                    <a>发布文档</a>
                </li>
                
<!--                 <li>
                    <a href="#">文档草稿</a>
                </li>
                <li>
                    <a href="#">知识库分区分类</a>
                </li> -->
                
            </ul>
        </div>

<!--         <div class="left-content-panel">
            <div class="left-content-panel-header">讨论社区</div>
            <ul class="left-content-panel-body left-part-list">
                <li>
                    <a href="#">社区话题分类</a>
                </li>
            </ul>
        </div> -->

<!--         <div class="left-content-panel">
            <div class="left-content-panel-header">帮助中心设置</div>
            <ul class="left-content-panel-body left-part-list">
                <li>
                    <a href="#">帮助中心设置</a>
                </li>
                <li>
                    <a href="#">帮助中心模板设计</a>
                </li>
            </ul>
        </div> -->
        
    </div>

</div>
<div id="right-part">
    <div class="tiaozhuan">
        <header class="part-header">
            <span>知识库文档</span>
            <div class="dropdown" style="padding-right:300px;">
                <a href="#" onclick="goPublish()">+发布新文档</a>
            </div>
        </header>
        <div class="right-content">
            <div class="right-content-panel container">
                <div class="table-content">
                    <div class="col-12 grid" style="padding: 0;">
                        <div>
                            <header class="title">
                                <div class="drop-btn-default dropdown">
                                    <button id="knowledge-time" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                                                                 所有时间
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" aria-labelledby="knowledge-time" id="timeSelect">
                                        <li><a id="allTime" >所有时间</a></li>
                                        <li><a id="lastWeek" >最近一周</a></li>
                                        <li><a id="lastMonth" >最近一月</a></li>
                                        <li><a id="lastThreeMonthes" >最近一季</a></li>
                                    </ul>
                                </div>
                                
                                                                
<!--                                 <div class="drop-btn-default dropdown">
                                    <button id="knowledge-classification" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                                                                                所有分类
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" aria-labelledby="knowledge-classification" id="classSelect">
                                        <li><a>所有分类</a></li>
                                    </ul>
                                </div> -->
                                
                                                                
<!--                                 <div class="drop-btn-default dropdown">
                                    <button id="knowledge-else" type="button" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                                        &nbsp;所 有
                                        <span class="caret"></span>
                                    </button>
                                    <ul class="dropdown-menu" aria-labelledby="knowledge-else" id="elseSelect">
                                        <li><a>所有</a></li>
                                        <li><a>推荐显示</a></li>
                                        <li><a>高亮显示</a></li>
                                        <li><a>分类置顶</a></li>
                                        <li><a>回复关闭</a></li>
                                        <li><a>控制台</a></li>
                                    </ul>
                                </div> -->
                                
                                
                                <button type="button" id="selectBtn">筛选</button>
                                <form class="search-sm fr" method="post" action="javascript:fastSearch()">
                                    <label>
                                        <i class="fa fa-search"></i>
                                        <input type="search" placeholder="快捷搜索" id="fastSearch" >                                        
                                    </label>
                                </form>
                            </header>
                            
                            <div class="body" id="knowledgeTable">
                                <table class="table">
                                    <thead>
                                    <tr>
                                        <th><input type="checkbox" id="allSelect"/></th>
                                        <th>编号</th>
                                        <th>标题</th>
                                        <th>作者</th>
                                        <!-- <th>分类</th> -->
                                        <th>创建时间</th>
                                        <th>最后更新</th>
<!--                                         <th class="tc">查看</th>
                                        <th class="tc">评论</th>
                                        <th class="tc">投票</th> -->
                                        <th></th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    
<!--                                     <tr>
                                        <td><input type="checkbox" /></td>
                                        <td class="title">
                                            <a>来自 android sdk 的工单反馈</a>
                                            <p>
                                                <span class="status red0">置顶</span>
                                                <span class="status orange0">高亮</span>
                                                <span class="status green0">推荐</span>
                                                <span class="status gray0">回复关闭</span>
                                                <span class="status blue0">控制台</span>
                                            </p>
                                        </td>
                                        <td>demo</td>
                                        <td><a>产品测试文档</a></td>
                                        <td>2016年01月29日 10:40</td>
                                        <td>3天前</td>
                                        <td class="tc"><span class="red">8</span></td>
                                        <td class="tc"><span class="red">0</span></td>
                                        <td class="tc"><span class="red">0</span></td>
                                        <td><a class="btn-sm">编辑</a></td>
                                    </tr> -->
                                    
                                    </tbody>
                                    <tfoot>
                                    <tr>
                                        <td colspan="7">
                                            <div id="pagination"></div>
                                        </td>
                                    </tr>
                                    </tfoot>
                                </table>
                            </div>
                            
                            <!-- 没有内容-->
                            <div class="body" id="noContent" style="display:none">
                                                                                      没有内容 
                            </div>  
                            
                        </div>
                    </div>
                </div>
            </div>
        </div>
        
        <div id="toolbar" style="width:calc(100% - 300px);">
            <button id="deleteBtn" type="button" class="btn btn-raised left-btn btn-danger">删除</button>
            <button id="cancelBtn" type="button" class="btn btn-raised btn-default btn-dark" onclick="cancel()">取消</button>
            <!-- <button id="editBtn" type="button" class="btn btn-raised btn-primary" data-toggle="modal" data-target="#editDocModal">编辑文档</button> -->
        </div>
        
    </div>
  
    <iframe style="width:100%;height:100%;border:0;display:none;" name="iframe" id="iframe"></iframe>
   
</div>


<!-- 编辑文档 -->
<div id="editDocModal" class="modal fade bs-example-modal-sm" data-backdrop="static" >
    <div class="modal-dialog" style="width:450px;">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <span class="modal-title">批量编辑文档</span>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row">
                        <div class="col-sm-10 col-sm-offset-1">
                            <form class="form-horizontal">
                            
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">选择分类</label>
                                    <select class="form-control" id="classEdit">
                                        <option value="">-不改变</option>
                                    </select>
                                </div>
                                                               
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">设为草稿 </label>
                                    <select class="form-control" id="draftEdit">
                                        <option value="">-不改变</option>
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label for="inputEmail3" class="control-label">添加新标签 </label>
                                    <input type="text"  class="form-control" id="newLabel" placeholder="请输入新标签" >
                                </div>
                                
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">置顶显示</label>
                                    <select class="form-control" id="topEdit">
                                        <option value="">-不改变</option>
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">高亮显示</label>
                                    <select class="form-control" id="hightlightEdit">
                                        <option value="">-不改变</option>
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">推荐显示</label>
                                    <select class="form-control" id="recommendEdit">
                                        <option value="">-不改变</option>
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">关闭回复</label>
                                    <select class="form-control" id="closeReplyEdit">
                                        <option value="">-不改变</option>
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                                </div>
                                
                                <div class="form-group">
                                    <label for="inputPassword3" class="control-label">控制台首页 </label>
                                    <select class="form-control" id="consoleEdit">
                                        <option value="">-不改变</option>
                                        <option value="1">是</option>
                                        <option value="0">否</option>
                                    </select>
                                </div>
                                
                            </form>
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-raised btn-default btn-sm" data-dismiss="modal">取消</button>
                <button type="button" class="btn btn-raised btn-primary btn-sm" onclick="editDocSubmit()" id="editDocSubmit">提交</button>
            </div>
        </div>
    </div>
</div>

<!-- 文档模板(管理员) -->
<script id="table-tr-template" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td><input class="ember-view ember-checkbox table-checkbox" type="checkbox"></td>
        <td>{{docId}}</td>
        <td class="title"><a onclick="viewDetails('{{docId}}','{{title}}')">{{title}}</a></td>
        <td>{{author}}</td>
        <td>{{createTime}}</td>
        <td>{{updateTime}}</td>                 
        <td><button type="button" class="btn-sm" onclick="editDocument('{{docId}}')">编辑</button></td>   
    </tr>
</script>

<!-- 文档模板(客服) -->
<script id="table-tr-template-agent" type="text/x-handlebars-template">
    <tr class="ember-view">
        <td><input class="ember-view ember-checkbox table-checkbox" type="checkbox" DISABLED></td>
        <td>{{docId}}</td>
        <td class="title"><a onclick="viewDetails('{{docId}}','{{title}}')">{{title}}</a></td>
        <td>{{author}}</td>
        <td>{{createTime}}</td>
        <td>{{updateTime}}</td>       
    </tr>
</script>


<script src="<%=request.getContextPath()%>/H+3.2/js/plugins/footable/footable.all.min.js"></script>
<script>
   var now = new Date();
   var beforeWeek = new Date(now.getTime() - 7*24*3600*1000);
   var beforeMonth = new Date(now.getTime() - 30*24*3600*1000);
   var beforeThreeMonth = new Date(now.getTime() - 3*30*24*3600*1000);
   var startTime="";
   var endTime="";
   var userType="";
   
    $(document).ready(function() { 	
        $('#orderGrid').footable();
        getUserType();
        
        $("#selectBtn").click();
        //点击左列表处理
        $(".left-part-list li").click(function(){
            $(".left-part-list li.active").removeClass("active");
            $(this).addClass("active");
            
            $("#knowledgeTable input[type=checkbox]").each(function(){
                $(this).attr("checked",false);
            });
            $("#toolbar").removeClass("show");
            
            var iframeObj = $(window.frames["iframe"].document); 
            
            if($(this).attr("id")=="publish"){
            	/* 当发布的文档标题和内容为空时才刷新发布文档的页面 */
            	if((iframeObj.find("#title").val()=="" || iframeObj.find("#title").val()==undefined) && (iframeObj.find(".note-editable").text()=="" || iframeObj.find(".note-editable").text()==undefined)){
                    $("iframe").attr("src","<%=request.getContextPath()%>/knowledge/goPublish");
                    $(".tiaozhuan").css("display","none");
                    $("iframe").css("display","");
            	}          	
            }
            if($(this).attr("id")=="manage"){           	
            	/* 当发布的文档标题和内容为空时才跳转到文档管理界面       */    	
             	if((iframeObj.find("#title").val()=="" || iframeObj.find("#title").val()==undefined) && (iframeObj.find(".note-editable").text()=="" || iframeObj.find(".note-editable").text()==undefined)){              		
                    $(".tiaozhuan").css("display","block");
                    $("iframe").css("display","none");                     
                    $("#selectBtn").click();                   
            	}
             	/* 当发布文档标题或内容不为空，离开发布界面时提示是否保存此文档 */
             	else{ 
            		$("#publish").addClass("active");
            		$(this).removeClass("active");
            		
            		if(confirm("是否要保存此文档？")){
            			$("#iframe")[0].contentWindow.goPublish(); 
            		}else{
            	    	iframeObj.find("#title").val("");
            	    	iframeObj.find(".note-editable").text("");
                		$("#publish").removeClass("active");
                		$(this).addClass("active");
            	    	          	    	
                        $(".tiaozhuan").css("display","block");
                        $("iframe").css("display","none");                     
                        $("#selectBtn").click();
                        
                        notice.success("已放弃此文档");
            		}
            	}            	
            }
        });

    });
    
    
    /**
     * 向后台请求数据
     */
    var getTableData = (function(){       
        /**
         * 初始化分页
         */
         var pager = new cri.Pager($("#pagination"),{
            page:1,
            pageSize:10,
            total:0,
            onPage:function(page,pageSize){
            	var url="<%=request.getContextPath()%>/help/document/queryDocument"; 
            	var fastSearch=$("#fastSearch").val();        	             
                getTableData({page:page,rows:pageSize,startTime:startTime,endTime:endTime,fastSearch:fastSearch},url);
            }
        });
        return function(param,url){
            $.post(url,param,function(data){
                if(data.rows.length=="0"){
         		   $("#knowledgeTable").hide();
         		   $("#noContent").show();
                }else{          	
         		   $("#knowledgeTable").show();
         		   $("#noContent").hide();       		   
         		   createTable(data.rows);
         		   
                   pager.update(param.page,param.rows,data.total);
                }
            });
        }
    })(); 
    
    
    /**
     * 刷新文档表格
     */
    var createTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template").html());
        var trTempAgent = Handlebars.compile($("#table-tr-template-agent").html());        
        var $table = $("#knowledgeTable");
        
        return function(tableData, type){
            var html = [];           
            /* 管理员 */
            if(userType=="3"){
            	$("#allSelect").prop("disabled",false);
            	
                for(var i= 0,len=tableData.length; i<len;i++){            	
                    var $tr = $(trTemp(tableData[i]));             
                    $tr.data("data",tableData[i]);
                    html.push($tr);     
                }              
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
                    	$("#editBtn").text("编辑 "+length+" 篇文档");
                        $("#toolbar").addClass("show");
                    }
                    else{
                        $("#toolbar").removeClass("show");
                    }
                });
            }
            /* 客服 */
            else{
            	$("#allSelect").prop("disabled",true);
            	
                for(var i= 0,len=tableData.length; i<len;i++){            	
                    var $tr = $(trTempAgent(tableData[i]));             
                    $tr.data("data",tableData[i]);
                    html.push($tr);     
                }
              
                $table.find("tbody").empty().append(html);
            }
            
        };
    })();
    
        
    /*  时间筛选 */
    $("#timeSelect a").click(function(){
    	$("#knowledge-time").html($(this).html()+'<span class="caret"></span>');
    	
   		if($(this).attr("id")=="lastWeek"){   			   	
   		    startTime=cri.formatDate(beforeWeek,"yyyy-MM-dd");
			endTime=cri.formatDate(now,"yyyy-MM-dd");
   		}
   		if($(this).attr("id")=="lastMonth"){
   			startTime=cri.formatDate(beforeMonth,"yyyy-MM-dd");
   			endTime=cri.formatDate(now,"yyyy-MM-dd");
   		}
   		if($(this).attr("id")=="lastThreeMonthes"){
   			startTime=cri.formatDate(beforeThreeMonth,"yyyy-MM-dd");
   			endTime=cri.formatDate(now,"yyyy-MM-dd");
   		}
   		if($(this).attr("id")=="allTime"){
   			startTime="";
   			endTime="";
   		}
    });
    
    /* 其他筛选 */
    $("#elseSelect a").click(function(){
    	$("#knowledge-else").html($(this).html()+'<span class="caret"></span>');
    });
    
    /* 分类筛选 */
    $("#classSelect a").click(function(){
    	$("#knowledge-classification").html($(this).html()+'<span class="caret"></span>');
    });
    
    /* 筛选按钮 */
    $("#selectBtn").click(function(){
    	var url="<%=request.getContextPath()%>/help/document/queryDocument"; 
    	var fastSearch=$("#fastSearch").val(); 
    	
    	$("#allSelect").prop("checked",false);
      	$("#toolbar").removeClass("show"); 
    	
    	getTableData({page:1,rows:10,startTime:startTime,endTime:endTime,fastSearch:fastSearch},url);
    });
        
    /* 取消按钮 */
    function cancel(){
        $("#knowledgeTable input[type=checkbox]").each(function(){
            $(this).attr("checked",false);
        });
        $("#toolbar").removeClass("show");
    }
        
    /* 批量编辑文档 */
    function editDocSubmit(){
    	$("#editDocModal").modal("hide");
    	cancel();
    }
    
    /* 发布新文档按钮 */
    function goPublish(){
    	$("#publish").click();
    }
    
    /* 编辑单个文档 */
    function editDocument(id){
        $("iframe").attr("src","<%=request.getContextPath()%>/help/document/queryDocById?docId="+id);
        $(".tiaozhuan").css("display","none");
        $("iframe").css("display","");
        
    }
    
    /* 文档区快捷搜索 */ 
    function fastSearch(){
    	$("#selectBtn").click();
    }
    

    /**
     * 删除文档按钮
     */
     $("#deleteBtn").click(function deleteRow(){      
            var idStr = [];           
            $("#knowledgeTable td input[type=checkbox]:checked").each(function(){
                var $tr = $(this).closest("tr");
                idStr.push($tr.data("data").docId);
            });   
            idStr = idStr.join(",");
            
            goDelete(idStr);
    });
    
     /* 删除文档方法 */
     function goDelete(idStr){
         var param={};
         param.ids=idStr;
         
         if(confirm("确认删除这些文档吗？")){
         	 $.ajax({
                  url:"<%=request.getContextPath()%>/help/document/deleteDocument",
                  dataType:'json',
                  data:param,
                  success:function(data){
                  	if(data.success){
                      	notice.success("文档删除成功！");
                      	$("#toolbar").removeClass("show");              
                      	$("#selectBtn").click();
                      }else{
                      	notice.danger("文档删除失败！");
                      }
                  }
              });
         }  
     }
     function publichSuc(){
    	 var iframeObj = $(window.frames["iframe"].document); 
    	 iframeObj.find("#title").val("");
    	 iframeObj.find(".note-editable").text("");
    	 
    	 $(".left-part-list li#manage").click();
     }
     
     /* 查看文档详情 */
     function viewDetails(docId,title){
         var url="<%=request.getContextPath()%>/help/document/viewDetails?docId="+docId;
         parent.openTab(url,"order",title);
     }
     
     /* 获取登录用户类型 */
     function getUserType(){
         $.post("<%=request.getContextPath()%>/help/document/getUserType",null,userTypeCallBack,"json");
     }
     /* 用户类型回调函数 */
     function userTypeCallBack(data){
    	 if(data.success){
    		 userType=data.rows;
    	 }else{
    		 notice.danger(data.msg);
    	 }
     }

</script>

</body>
</html>