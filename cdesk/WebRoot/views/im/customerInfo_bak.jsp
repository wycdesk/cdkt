<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
    var userInfo = {};
    var userId="";
    /*
     * 缓存信息
     */
    function cacheInfo(userId,data,type){
        var user = {};
        if(type == "1"){//用户信息
            if(userInfo[userId]){
                userInfo[userId].info = data;
            }else{
                userInfo[userId] = {};
                userInfo[userId].info = data;
            }
        }else if(type == "2"){//工单
            if(userInfo[userId]){
                userInfo[userId].work = data;
            }else{
                userInfo[userId] = {};
                userInfo[userId].work = data;
            }
        }else if(type=="3"){//联络历史
            if(userInfo[userId]){
                userInfo[userId].comm = data;
            }else{
                userInfo[userId] = {};
                userInfo[userId].comm = data;
            }
        }
        //userInfo[userId] = user;
        //notice.alert(userInfo[userId].info.rows.userName);
    }
    /**
     * 删除缓存信息
     */
    function deleteCache(userId){
        delete  userInfo[userId];
    }

    //从缓存获取客户基本信息
    function getInfo(userId){
        if(userInfo[userId]){
            if(userInfo[userId].info){
                return userInfo[userId].info;
            }
        }
        return "";
    }
    //从缓存获取工单信息
    function getWork(userId){
        if(userInfo[userId]){
            if(userInfo[userId].work){
                return userInfo[userId].work;
            }
        }
        return "";
    }
    //从缓存获取联络历史
    function getComm(userId){
        if(userInfo[userId]){
            if(userInfo[userId].comm){
                return userInfo[userId].comm;
            }
        }
        return "";
    }


    function customer(customerId){
        userId = customerId;
        queryCustomerInfo(userId,"aa123","5");
        getCommTableData({page:1,rows:6,entId:'${entId}',userId:userId},"<%=request.getContextPath()%>/communicate/getComms");
        getTableData({status:"-1",creatorId:userId,page:1,rows:8});
    }

    /*
     * 用户信息
     */
    function queryCustomerInfo(customerId, webchatId, userAccountType){
        if(getInfo(customerId)){
            var data = getInfo(customerId);
            queryCustomerInfoCallBack(data.rows);
        }else{
            $.ajax({
                url:"<%=request.getContextPath()%>/IM/queryOrAddUser",
                dataType:'json',
                data:{customerId:customerId,webchatId:webchatId,userAccountType:userAccountType},
                success:function(data){
                    if(data.success){
                        cacheInfo(userId,data,"1");
                        queryCustomerInfoCallBack(data.rows);
                    }
                    else{
                        notice.alert(data.msg);
                    }
                }
            });
        }

    }
    function queryCustomerInfoCallBack(data){
        $("#userName").val(data.userName);
        $("#telPhone").val(data.telPhone);
        $("#fixedPhone").val(data.fixedPhone);
        $("#email").val(data.email);
        $("#userLabel").val(data.userLabel);
        $("#remark").val(data.remark);
    }
    /*
     * 联络历史
     */
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
            pageSize:6,
            total:0,
            onPage:function(page,pageSize){
                var url="";
                url="<%=request.getContextPath()%>/communicate/getComms";
                getCommTableData({page:page,rows:pageSize,entId:'${entId}',userId:userId},url);
            }
        });
        return function(param,url){
            if(param.page>1){
                $.post(url,param,function(data){
                    createCommTable(data.rows,null);
                    pager.update(param.page,param.rows,data.total);
                });
            }else{
                if(getComm(param.userId)){
                    var data = getComm(param.userId);
                    createCommTable(data.rows,null);
                    pager.update(param.page,param.rows,data.total);
                }else{
                    $.post(url,param,function(data){
                        createCommTable(data.rows,null);
                        pager.update(param.page,param.rows,data.total);
                        cacheInfo(userId, data, "3");
                    });
                }
            }
        }
    })();


    var wfStatus = ["尚未受理","受理中","等待客户回复","已解决","已关闭"];
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
            pageSize:8,
            total:0,
            onPage:function(page,pageSize){
                $.extend(paramCache,{page:page,rows:pageSize});
                getTableData(paramCache);
            }
        });
        return function(param,url){
            if(param.page>1){
                $.ajax({
                    url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryCreatorAllWorkOrder?sessionKey="+ $.cookie("sessionKey"),
                    dataType:'jsonp',
                    data:param,
                    success:function(data){
                        console.dir(data);
                        if(data.rows){
                            createTable(data.rows.list);
                        }
                        pager.update(param.page,param.rows,data.total);
                        $.extend(paramCache,param);
                    }
                });
            }else{
                if(getWork(userId)){
                    var data = getWork(userId);
                    if(data.rows){
                        createTable(data.rows.list);
                    }
                    pager.update(param.page,param.rows,data.total);
                    $.extend(paramCache,param);
                }else{
                    $.ajax({
                        url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryCreatorAllWorkOrder?sessionKey="+ $.cookie("sessionKey"),
                        dataType:'jsonp',
                        data:param,
                        success:function(data){
                            console.dir(data);
                            cacheInfo(userId, data, "2");
                            if(data.rows){
                                createTable(data.rows.list);
                            }
                            pager.update(param.page,param.rows,data.total);
                            $.extend(paramCache,param);
                        }
                    });
                }
            }

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
                    parent.openTab(url,"order",title);
                });
                html.push($tr);
            }
            $table.find("tbody").empty().append(html);
        };
    })();



    /**
     * 知识库
     */
    var getKnowledgeTableData = (function(){
        /**
         * 初始化分页
         */
        var pager = new cri.Pager($("#paginationKnowledge"),{
            page:1,
            pageSize:6,
            total:0,
            onPage:function(page,pageSize){
                var url="<%=request.getContextPath()%>/help/document/queryDocument";
                var fastSearch="";
                getKnowledgeTableData({page:page,rows:pageSize,startTime:"",endTime:"",fastSearch:""},url);
            }
        });
        return function(param,url){
            $.post(url,param,function(data){
                createKnowledgeTable(data.rows);
                pager.update(param.page,param.rows,data.total);
            });
        }
    })();

    /**
     * 刷新文档表格
     */
    var createKnowledgeTable = (function(){
        var trTemp = Handlebars.compile($("#table-tr-template-knowledge").html());
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
    /* 查看文档详情 */
    function viewDetails(docId,title){
        var url="<%=request.getContextPath()%>/help/document/viewDetails?docId="+docId;
        parent.openTab(url,"order",title);
    }
    $(function(){
        var url="<%=request.getContextPath()%>/help/document/queryDocument";
        getKnowledgeTableData({page:1,rows:6,startTime:"",endTime:"",fastSearch:""},url);
    });

    !function(){
        var self = window.imWindow = {};
        /**
         * 当新客户访问
         * @param info
         */
        self.onNewCustomer = function(chatId){
            //TODO:1.AJAX查询客户，放到页面缓存，刷新右边区域

        };

        /**
         * 切换客户
         * @param userId
         */
        self.switchCustomer = function(chatId){

        };

        /**
         * 客户离开
         * @param chatId
         */
        self.onCustomerLeave = function(chatId){};

        /**
         * 查询到的客户缓存，长度最长为10
         * @type {Array}
         */
        var customerListCache = [];
    }();
</script>