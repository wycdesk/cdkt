<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>部门管理</title>
    <%@include file="/views/include/pageHeader.jsp"%>
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/innerFrame.css">
    <link rel="stylesheet" type="text/css" href="<%=request.getContextPath()%>/static/css/dptmanage.css">
</head>
<body>
<div class="container-fluid">  
    <div class="row">    
        <div style="text-align:right;">
            <button type="button" class="btn btn-raised btn-primary btn-sm" data-toggle="modal" data-target="#addDptModal">新增部门</button>     
            <button type="button" class="btn btn-raised btn-primary btn-sm" data-toggle="modal" data-target="#addJobModal">新增岗位</button> 
        </div>
             
        <div class="col-sm-offset-1 col-sm-10">
            <div class="panel tree-grid-panel">
                <table id="treeGrid"></table>
            </div>
        </div>
    </div>
</div>
<%@include file = "/views/department/editDptModal.jsp"%>
<%@include file = "/views/department/editJobModal.jsp"%>
<%@include file = "/views/department/addDptModal.jsp"%>
<%@include file = "/views/department/addJobModal.jsp"%>
<script>
    var departmentManger = function(){
        var initTableView = function(){
            $('#treeGrid').treegrid({
                url:'<%=request.getContextPath()%>/department/query',
                rowNum:false,
                columns:[
                    {field:'text',width:200,title:'部门'},
                    {field:'button',width:450,title:' ',button:[
                        {text:'修改',iconCls:'',handler:function(data){
                            console.log('[cdesk] 需要修改的节点数据,req=' + JSON.stringify(data));
                            
                            if(data.dptId==null){
                            	editDpt(data);
                            }else{
                            	editJob(data);
                            }                              
                        }},
                        {text:'授权',iconCls:'',handler:function(data){
                            console.log('[cdesk] 需要授权的节点数据,req=' + JSON.stringify(data));
                        }},
                        {text:'权限继承',iconCls:'',handler:function(data){
                            console.log('[cdesk] 需要权限继承节点数据,req=' + JSON.stringify(data));
                        }},
                        {text:'删除',iconCls:'',handler:function(data){
                            console.log('[cdesk] 需要删除的节点数据,req=' + JSON.stringify(data));
                            
                            if(data.dptId==null){
                            	deleteDpt(data);
                            }else{
                            	deleteJob(data);
                            }    
                        }}
                    ]}
                ],
                pagination:false
            });
        };
        return {
            initTableView:initTableView
        };
    }();

    $(function(){
        departmentManger.initTableView();
    });
    
    /*  获取部门信息  */
    function editDpt(data){
    	var param = {};
    	param.id = data.id;
    	var url = "<%=request.getContextPath()%>/department/queryDptById";
    	$.post(url, param, editDptCallBack, "json");
    }
    
    /* 获取部门信息回调函数 */
    function editDptCallBack(data){
    	if(data.success){
    		$("#dptId").val(data.rows[0].id);
      		$("#name").val(data.rows[0].name);
    		$("#parentId").val(data.rows[0].parentId);
    		$("#reamrk").val(data.rows[0].reamrk);
    		
    		$("#editDptModal").modal("show");
    	}else{
    		notice.warning(data.msg);
    	}
    }
    
    /*  获取岗位信息  */
    function editJob(data){
    	var param = {};
    	param.id = data.id;
    	var url = "<%=request.getContextPath()%>/department/queryJobById";
    	$.post(url, param, editJobCallBack, "json");
    }
    
    /* 获取岗位信息回调函数 */
    function editJobCallBack(data){
    	if(data.success){
    		$("#jobId").val(data.rows[0].id);
      		$("#jobName").val(data.rows[0].name);
      		$("#dptId1").val(data.rows[0].dptId);
    		$("#parentId1").val(data.rows[0].parentId);
    		$("#reamrk1").val(data.rows[0].reamrk);
    		
    		$("#editJobModal").modal("show");
    	}else{
    		notice.warning(data.msg);
    	}
    }
    
    /* 删除部门 */
    function deleteDpt(data){
    	if(data.parentId=="0"){
    		notice.alert("顶级部门无法删除!");
    		return;
    	}
    	if(confirm("确认删除选中的部门？")){
           	var param = {};
        	param.id = data.id;
        	var url = "<%=request.getContextPath()%>/department/delete";
        	$.post(url, param, delDptCallBack, "json");
    	}
    }
    
    /* 删除部门回调函数 */
    function delDptCallBack(data){
    	if(data.success){
    		notice.alert("删除成功");
    		location.reload();
    	}else{
    		notice.warning(data.msg);
    	}
    }
    
    /* 删除岗位 */
    function deleteJob(data){
    	if(data.parentId=="0"){
    		notice.alert("顶级岗位无法删除!");
    		return;
    	}
    	if(confirm("确认删除选中的岗位？")){
           	var param = {};
        	param.id = data.id;
        	var url = "<%=request.getContextPath()%>/department/deleteJob";
        	$.post(url, param, delJobCallBack, "json");
    	}
    }
    
    /* 删除岗位回调函数 */
    function delJobCallBack(data){
    	if(data.success){
    		notice.alert("删除成功");
    		location.reload(); 
    	}else{
    		notice.warning(data.msg);
    	}
    }

    /*
     * 初始化树形下拉框
     */
    function initDptTreeSelect(){
        var desp = function(data,deep,nodeCallBack){
            var html = [];
            for(var i=0,l=data.length;i<l;i++){
                var node = data[i];
                html.push(nodeCallBack(node,deep));
                if(node.children){
                    html = html.concat(desp(node.children,deep+1,nodeCallBack));
                }
            }
            return html;
        };
        var createOption = function(node,deep){
            var text = node.text;
            while(deep--){
                text = "&nbsp&nbsp&nbsp&nbsp" + text;
            }
            return '<option value="'+node.id+'">'+text+'</option>';
        };

        $.post('<%=request.getContextPath()%>/department/queryAllDpt',{},function(data){
            console.log('[cdesk] 请求所有部门resp=' + JSON.stringify(data));
            if(data.success){
                var html,
                    rows = data.rows;
                html = desp(rows,0,createOption).join('');
                $('#dptId1,#dptId2,#parentId3,#parentId').html(html);
            }
        });
        $.post('<%=request.getContextPath()%>/department/queryAllJob',{},function(data){
            console.log('[cdesk] 请求所有岗位resp=' + JSON.stringify(data));
            if(data.success){
                var html,
                    rows = data.rows;
                html = desp(rows,0,createOption).join('');
                $('#parentId2,#parentId1').html(html);
            }
        });
    }

    initDptTreeSelect();
</script>
</body>
</html>