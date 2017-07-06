<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript">
    !function(){
        var $c = $("#IMWindowContainer .right-container");
        var self = window.imWindow = {};
        var selector = '.customerContainer';
        var rightContainerT = Handlebars.compile($("#right-container-template").html());
        var num = 0;
        var userId;

        /**
         * 当新客户访问
         * @param info
         */
        self.onNewCustomer = function(chatId, IMSource){
            queryAjax(chatId, +(new Date()), IMSource).then(function(data){
                var $newCustom = $(rightContainerT(data.rows));
                $newCustom.attr('userid',data.rows.userId);
                $newCustom.attr('imSource',IMSource);
                bindEvent2CustomHtml($newCustom);

                userId = data.rows.userId;
                var userName = data.rows.userName;
                console.log('[cdesk] 新客户访问,查询关联用户,userName:'+userName);
                getRelationUser($newCustom)({userName:userName},"<%=request.getContextPath()%>/IM/queryRelationUser");

                console.log('[cdesk] 新客户访问,查询联络历史信息,userId:'+userId);
                getCommTableData($newCustom)({page:1,rows:6,entId:'${entId}',userId:userId},"<%=request.getContextPath()%>/communicate/getComms");

                console.log('[cdesk] 新客户访问,查询工单信息,userId:'+userId);
                getTableData($newCustom)({status:"-1",customId:userId,page:1,rows:8});

                console.log('[cdesk] 新客户访问,初始化知识库');
                getKnowledgeTableData($newCustom)({page:1,rows:6,startTime:"",endTime:"",fastSearch:""},"<%=request.getContextPath()%>/help/document/queryDocument");

                $c.find(selector).hide();
                $c.append($newCustom);

                dataService.push(chatId,data.rows,$newCustom);
                dataService.pushUserId(chatId,data.rows.userId,data.rows.commId);
            });
        };

        /**
         * 切换客户
         * @param userId
         */
        self.switchCustomer = function(chatId){
            userId = dataService.getUserId(chatId);
            console.log('[cdesk] 切换客户,chatId:'+chatId+',userId:'+userId);
            $c.find(selector).hide();
            var o = dataService.get(chatId);
            if(o){
                o.view.show();
            }
            else{
                this.onNewCustomer(chatId,"");
            }
        };

        /**
         * 客户离开
         * @param chatId
         */
        self.onCustomerLeave = function(chatId,message){
            //保存联络历史
            userId = dataService.getUserId(chatId);
            var commId = dataService.getCommId(chatId);
            var messages = encodeURI(JSON.stringify(message));
            console.log('[cdesk] 客户离开,chatId:'+chatId+',userId:'+userId+',commId:'+commId+',message:'+messages);
            $.post("<%=request.getContextPath()%>/communicate/endIMComm?commId="+commId+"&message="+encodeURI(messages)+"&endTimeStr="+(+(new Date())), updateCallBack, 'json');
            // $(selector)
        };

        /**
         * 给HTML绑定监听事件
         */
        function bindEvent2CustomHtml($c){
            $c.find('input[type=text]').on('change',function(){
                var userId = $c.attr('userid');
                //var userId = $(this).closest('.customeContainer').data('userId');
                var name = $(this).attr('data-name');
                var val = $(this).val();
                //TODO:检查必填 AJAX提交
                if(name=="telPhone"){
                    /* 手机格式校验（1开头11位） */
                    if(val!=""){
                        if(!phoneFormat(val)){
                            console.log('[cdesk] 修改客户电话,手机号码有误,userInfos:'+userInfos);
                            return;
                        }
                    }
                    var userInfos = "{'telPhone':'"+val+"','userId':'"+userId+"'}";

                    $.post("<%=request.getContextPath()%>/userManageMongo/existsPhoneIM?userInfos="+userInfos+"&telPhone="+val).then(function(data){
                        existsPhoneIMCallBack(data,val,userId);
                    });
                }
                if(name=="email"){
                    if(val!=""){
                        if(!mailFormat(val)){
                            return;
                        }
                    }
                    $.post("<%=request.getContextPath()%>/userManageMongo/existsEmailIM?email="+val).then(function(data){
                        checkEmailCallBack(data,val,userId);
                    });
                }else{
                    var userInfos = "{'"+name+"':'"+val+"','userId':'"+userId+"'}";
                    console.log('[cdesk] 修改客户信息,userInfos:'+userInfos);console.dir(userInfos);
                    $.post("<%=request.getContextPath()%>/userManageMongo/updateUserIM?userInfos="+userInfos, updateCallBack, 'json');
                }
            });
            $c.find('[data-id="createWorkButten"]').on('click',function(){
                var curUserId = $c.attr('userid');
                var imSource = $c.attr('imSource');
                console.log('[cdesk] 创建工单,curUserId:'+curUserId+',imSource:'+imSource);
                var source = '1';
                if(imSource == 'weixin'){
                	source = '7';
                }else if(imSource == 'butel'){
                	source = '8';
                }else{
                	source = '1';
                }
                var param = {source:source,userId:curUserId};
                var paramstr = encodeURI(JSON.stringify(param));
                newwork("",curUserId,"",encodeURI(paramstr));
            });
        }

        var dataService = (function(){
            var MAX_CACHE_LENGTH = 10;
            var length = 0;
            var _userListCache = {
            };
            var _userIdCache = {};//userId和chatId
            return {
                push:function(userId,userData,$dom){
                    if(!_userListCache[userId]){
                        if(length < MAX_CACHE_LENGTH){
                            _userListCache[userId]={
                                data:userData,
                                view:$dom
                            };
                            length++;
                        }
                        else{
                            dataService.shift();
                            dataService.push(userId,userData,$dom);
                        }
                    }
                },
                get:function(userId){
                    return _userListCache[userId];
                },
                shift:function(){
                    for(var userId in _userListCache){
                        var o = _userListCache[userId];
                        o.view.remove();
                        delete _userListCache[userId];
                        length--;
                        return;
                    }
                },
                pushUserId:function(chatId,userId,commId){
                    if(!_userIdCache[chatId]){
                        _userIdCache[chatId]={
                            data:userId,
                            commId:commId
                        };
                    }
                },
                getUserId:function(chatId){
                    return _userIdCache[chatId].data;
                },
                getCommId:function(chatId){
                    return _userIdCache[chatId].commId;
                },
                shiftUserId:function(chatId){
                    return delete _userIdCache[chatId];
                }
            };
        })();


        /**
         * ajax请求客户信息
         */
        function queryAjax(webchatId, startTime, IMSource){
            return $.ajax({
                url:"<%=request.getContextPath()%>/IM/queryOrAddUser",
                dataType:'json',
                data:{webchatId:webchatId, startTime:startTime, IMSource:IMSource},
                success:function(data){
                    if(data.success){
                    }
                    else{
                        notice.alert(data.msg);
                    }
                }
            });
        }

        /**
         * 查询关联用户
         */
        var getRelationUser = function($c){
            var page = function(param,url){
                $.post(url,param,function(data){
                    var conditionTemp = Handlebars.compile($("#relation-template").html());
                    var $ui = $c.find("[data-id=relationUser]");
                    var list = data.rows;
                    for(var i= 0,len=list.length; i<len;i++){
                        var po = list[i];
                        $ui.append(conditionTemp(po));
                    }

                });
            };
            return page;
        };

        /**
         * 联络历史分页向后台请求数据
         */
        var getCommTableData = function($c){
            /**
             * 初始化分页
             */
            var pager = new cri.Pager($c.find("[data-id=paginationComm]"),{
                page:1,
                pageSize:6,
                total:0,
                onPage:function(page,pageSize){
                    var url="";
                    url="<%=request.getContextPath()%>/communicate/getComms";
                    page({page:page,rows:pageSize,entId:'${entId}',userId:userId},url);
                }
            });

            var page = function(param,url){
                $.post(url,param,function(data){
                    //createCommTable(data.rows,null);
                    var tableData = data.rows;
                    var trTemp = Handlebars.compile($("#table-tr-template").html());
                    var $table = $c.find("[data-id=commGrid]");
                    var html = [];
                    for(var i= 0,len=tableData.length; i<len;i++){
                        var $tr="";
                        $tr = $(trTemp(tableData[i]));
                        $tr.data("data",tableData[i]);
                        html.push($tr);
                    }
                    $table.find("tbody").empty().append(html);
                    pager.update(param.page,param.rows,data.total);
                });
            };
            return page;
        };


        /**
         * 向后台请求数据(最近工单)
         */
        var getTableData = function($c){
            var wfStatus = ["尚未受理","受理中","等待客户回复","已解决","已关闭"];
            var paramCache = {};
            var $pager = $c.find("[data-id=paginationLately]");
            var pager = new cri.Pager($pager,{
                page:1,
                pageSize:8,
                total:0,
                onPage:function(page,pageSize){
                    $.extend(paramCache,{page:page,rows:pageSize});
                    ajaxQuery(paramCache);
                }
            });
            $pager.data('pager',pager);
            var ajaxQuery = function(param){
                $.ajax({
                    url:"http://<%=request.getServerName()%>:"+parent.workBasePath+"/queryWorkOrderInfo/queryCreatorAllWorkOrder?sessionKey="+ $.cookie("sessionKey"),
                    dataType:'jsonp',
                    data:param,
                    success:function(data){
                        console.dir(data);
                        //createTable(data.rows.list);
                        var tableData = data.rows.list;
                        var trTemp = Handlebars.compile($("#table-tr-template-lately").html());
                        var tooltipTemp = Handlebars.compile($("#tooltip-template").html());
                        var $table = $c.find("[data-id=latelyOrderGrid]");
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
                        pager.update(param.page,param.rows,data.total);
                        $.extend(paramCache,param);
                    }
                });
            };
            return ajaxQuery;
        };

        /**
         * 知识库
         */
        var getKnowledgeTableData = function($c){
            /**
             * 初始化分页
             */
            var pager = new cri.Pager($c.find("[data-id=paginationKnowledge]"),{
                page:1,
                pageSize:6,
                total:0,
                onPage:function(page,pageSize){
                    var url="<%=request.getContextPath()%>/help/document/queryDocument";
                    page({page:page,rows:pageSize,startTime:"",endTime:"",fastSearch:""},url);
                }
            });
            var page = function(param,url){
                $.post(url,param,function(data){
                    console.log('[cdesk] 知识库,data.rows:'+data.rows);console.dir(data);
                    var tableData = data.rows;
                    var trTemp = Handlebars.compile($("#table-tr-template-knowledge").html());
                    var $table = $c.find('[data-id=knowledgeTable]');
                    var html = [];
                    for(var i= 0,len=tableData.length; i<len;i++){
                        var $tr = $(trTemp(tableData[i]));
                        $tr.data("data",tableData[i]);

                        $tr.find("a").click((function(docId,title){
                            return function(){
                                var url="<%=request.getContextPath()%>/help/document/viewDetails?docId="+docId;
                                parent.openTab(url,"order",title);
                            };
                        })(tableData[i].docId,tableData[i].title));

                        html.push($tr);
                    }
                    $table.find("tbody").empty().append(html);
                    pager.update(param.page,param.rows,data.total);
                });
            };
            return page;
        };

        /*  手机校验 （1开头11位）*/
        function phoneFormat(telPhone){
            var patrn = /^1\d{10}$/;
            if (patrn.test(telPhone)){
                return true;
            }else{
                notice.warning("请输入正确的手机格式！");
                return false;
            }
        }
        /* 号码是否已存在回调函数 */
        function existsPhoneIMCallBack(data,telPhone,userId){
            if(data.success){
                var userInfos = "{'telPhone':'"+telPhone+"','userId':'"+userId+"'}";
                $.post("<%=request.getContextPath()%>/userManageMongo/updateUserIM?userInfos="+userInfos, updateCallBack, 'json');
            }else{
                notice.warning(data.msg);
            }
        }
        /* 更新回调函数 */
        function updateCallBack(data){
            if(!data.success){
                notice.warning(data.msg);
            }
        }
        /*  邮箱格式校验 */
        function mailFormat(email){
            var patrn = /^(\w)+(\.\w+)*@(\w)+((\.\w+)+)$/;
            if (patrn.test(email)){
                return true;
            }else{
                notice.warning("请输入正确的邮箱格式！");
                return false;
            }
        }
        /* 检查邮箱回调函数 */
        function checkEmailCallBack(data,email,userId){
            if(data.success){
                var userInfos = "{'email':'"+email+"','userId':'"+userId+"'}";
                $.post("<%=request.getContextPath()%>/userManageMongo/updateUserIM?userInfos="+userInfos, updateCallBack, 'json');
            }else{
                notice.danger(data.msg);
            }
        }
    }();
</script>