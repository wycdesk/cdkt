<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div class="modal fade" id="mergeOrderModal">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span aria-hidden="true">&times;</span></button>
                <h4 class="modal-title">合并工单</h4>
            </div>
            <div class="modal-body">
                <div class="container-fluid">
                    <div class="row" id="mergeTopNotice" style="display: none;">
                        <div class="col-lg-12 col-sm-12 merge-top-notice-row">
                            <div class="merge-top-notice">合并工单<span id="order1Id"></span>到<span id="order2Id"></span></div>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12 col-sm-12">
                            将工单:
                            <h4 class="order-title">#这是一个工单标题</h4>
                            <small>创建于 2016/01/01 14:00</small>
                        </div>
                    </div>
                    <div class="row" id="orderReply1" style="display:none;">
                        <div class="col-lg-12 col-sm-12">
                            这条工单将以以下回复关闭:
                            <textarea class="order-reply" id="order1Content"></textarea>
                            <input type="checkbox" id="workSelf" checked/>工单发起人可以看见此回复
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-lg-12 col-sm-12 divide-line"><i class="fa fa-link"></i></div>
                    </div>
                    <div class="row" id="searchOrderRow">
                        <div class="col-lg-12 col-sm-12">合并到：</div>
                        <div class="form-group">
                            <div class="col-lg-12 col-sm-12">
                                <input id="searchOrderIdInput" placeholder="请输入目标工单的ID" name="searchOrderId" class="form-control"/>
                            </div>
                            <div class="col-lg-12 col-sm-12" style="display:none;">
                                <div class="merge-order-search-msg" id="error-msg">
                                    没找到任何订单
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="row" style="display:none;" id="orderReply2">
                        <div class="col-lg-12 col-sm-12">
                            这条工单将以以下回复关闭:
                            <textarea class="order-reply" id="order2Content"></textarea>
                            <input type="checkbox" id="workTarget" checked/>工单发起人可以看见此回复
                        </div>
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button id="cancelMergeBtn" type="button" class="btn btn-default btn-raised" data-dismiss="modal">取消</button>
                <button id="lastStepBtn" type="button" class="btn btn-default btn-raised" style="display:none;">上一步</button>
                <button id="confirmMergeBtn" type="button" class="btn btn-primary btn-raised" style="display:none;">确认合并</button>
            </div>
        </div>
    </div>
</div>
<script>
    !function(){
        var order1workId = '${workId}';
        var order2;
        var order2workId = '';
		var one=0,two=0;
        
        function getWorderOrder(workId){
            var params = {workId:workId};
            var info = JSON.stringify(params);
            return $.ajax({
                url : "http://<%=request.getServerName()%>:"+parent.workBasePath+"/workorder/detail?sessionKey="+ $.cookie("sessionKey"),
                type : "post",
                dataType : 'jsonp',
                data:{info:info, entId:"${entId}"},
                success:function(d){
                	if(!d.success){
                		notice.warning(d.msg);
                		$("#searchOrderIdInput").val('');
                		return;
                	}
                	if(d.rows[0].status=="4"){
                		notice.warning("工单已关闭");
                		$("#searchOrderIdInput").val('');
                		return;
                	}
                	if(d.rows[0].creatorId!='${creatorId}'){
                		notice.warning("不能合并到发起人不同的工单");
                		$("#searchOrderIdInput").val('');
                		return;
                	}
                    console.log(d);
                    if(d.success && d.rows.length){
                        $("#error-msg").parent().hide();
                        onFindOrder(d.rows[0]);
                    }
                    else{
                        //TODO:未查询到工单
                        $("#error-msg").parent().show();
                    }
                }
            });
        }
        $(function(){
            $("#order1Id").text('#${workId}');
            $("#order1Content,#order2Content").summernote({
                lang:"zh-CN",
                toolbar:[]
            });
            $("#mergeOrderModal input[name='searchOrderId']").on("input",function(){
            	var f=time();
            	setTimeout(f,1000);
            });
            $("#lastStepBtn,#cancelMergeBtn").click(function(){
                $("#order2Id").text("");
                $("#error-msg").show();
                $("#searchOrderRow").show();
                $("#mergeTopNotice").hide();
                $("#orderReply1").hide();
                $("#orderReply2").hide();
                $("#lastStepBtn").hide();
                $("#confirmMergeBtn").hide();
                $("#searchOrderIdInput").val('');
            });
            $("#confirmMergeBtn").click(function(){
                mergeOrder();
            });
        });

        //搜索到工单后，切换合并窗口的UI
        function onFindOrder(order){
            $("#order2Id").text(order.workId);
            $("#error-msg").hide();
            $("#searchOrderRow").hide();
            $("#mergeTopNotice").show();
            $("#orderReply1").show();
            $("#orderReply2").show();
            $("#lastStepBtn").show();
            $("#confirmMergeBtn").show();
            $("#order1Content").code('此工单已被关闭，并已合并至 #'+order.workId+' “'+order.title+'”');
            order2 = order;
            order2workId = order.workId;
        }

        //合并工单
        function mergeOrder(){
            //content:内容,
            //status:工单状态（info1只可传入”4”，表示该工单状态为已关闭）
            //是否为座席操作（1：是，0：否）
            var workSelf="1";
        	var workTarget="1";
        	if(document.getElementById("workSelf").checked){
        		workSelf="0";
        	}
        	if(document.getElementById("workTarget").checked){
        		workTarget="0";
        	}
            //alert(document.getElementById("workSelf").checked);
            //alert(document.getElementById("workTarget").checked);
            //return;
            var info1 = {workId:""+order1workId,content:$("#order1Content").code(),isPublicReply:workSelf,status:'4',updatorId:'${userId}',updatorName:'${userName}',isAgent:1};
            var info2 = {workId:""+order2workId,content:$("#order2Content").code(),isPublicReply:workTarget,status:order2.state,updatorId:'${userId}',updatorName:'${userName}',isAgent:1};
            var params = {
                info1:encodeURI(JSON.stringify(info1)),
                info2:encodeURI(JSON.stringify(info2))
            };
            $.ajax({
                url : "http://<%=request.getServerName()%>:"+parent.workBasePath+"/workorder/workOrderJoin?sessionKey="+ $.cookie("sessionKey"),
                type : "post",
                dataType : 'jsonp',
                data : params,
                success : function(data) {
                    //console.log('合并工单，返回结果');console.dir(data);
                    notice.success("合并成功");
                    parent.closeThisTab();
                }
            });
        }
        function mergeOrderQuery(){
        	var v = $("#mergeOrderModal input[name='searchOrderId']").val();
            if(v=='${workId}'){
            	notice.warning("不能合并到工单自身");
            	$("#searchOrderIdInput").val('');
            	return;
            }
            if(v){
                getWorderOrder(+v);
            }
        }
        function time(){
        	var flag=0;
        	if(one==0){
        		two=0;
        		one=1;
        		flag=0;
        	}else{
        		one=0;
        		two=1;
        		flag=1
        	}
        	return function(){
        		if(flag==0){
        			if(one==1){
        				mergeOrderQuery();
        			}
        		}else{
        			if(two==1){
        				mergeOrderQuery();
        			}
        		}
        	};
        }
    }();
</script>