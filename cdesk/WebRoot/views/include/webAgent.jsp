<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script src="${WebAgent_ROOT}/WebAgent.js"></script>
<script src="<%=request.getContextPath()%>/script/butel.js"></script>
<script src="<%=request.getContextPath()%>/script/CCODMultiChannel.js"></script>
<script type="text/javascript">
    var customInfo = null;
    //thisCall 每通电话，需要填鸭式写入各个阶段的数据例如接听时间，通话时长等，最后入库
    var thisCall = {};
    var workOrderId = null;
    $(window).on('unload',function(){
        logoutCCODMultiChannel();
    });
    $(function(){
        var $im = $('#IMWindowContainer');
        var isIMOpened = false;
        $("#IMBtn").click(function(){
        	isIMOpened || $im.detach();
            $im.show();
            isIMOpened = true;
            tabManager.open({
                content:$im,
                type:'im',
                closeAble:true,
                title:'IM通话',
                onClose:function(){
                    if(this.$content){
                        $im = this.$content.find('#IMWindowContainer').detach();
                        isIMOpened = false;
                    }
                }
            });
        });
        $("#setReadyToggle .toggle").on("click",function(){
            $("#currentStatus").text('切换状态...');
            if($(this).prev().prop("checked")){
                CCODMultiChannel.agentBusy(function(){
                    console.log('[CDesk] 多渠道能力置忙成功');
                    $("#currentStatus").text('已经置忙');
                },function(){
                    console.log('[CDesk] 多渠道能力置忙失败');
                });
            }
            else{
                CCODMultiChannel.agentReady(function(){
                    console.log('[CDesk] 多渠道能力置闲成功');
                    $("#currentStatus").text('已经置闲');
                },function(){
                    console.log('[CDesk] 多渠道能力置闲失败');
                });
            }
        });
        $("#phoneBtn").on('click',function(){
            WebAgent.WaChat.toggle();
        });
    });

    WebAgent.init({
        useLocal:false,
        loadBootstrap:false,
        callback: function() {
            WebAgent.WaInit({
                ui:false,
                callback: function(waAutoLoginResult,waAutoLoginData) {
                    var awayStatus = waAutoLoginData && waAutoLoginData.ext && waAutoLoginData.ext.awayStatus;
                    var appkey, numbe, uid;
                    var cchatJs = "${WebAgent_ROOT}/WA/theme/cchat.js";
                    WebAgent.attachModule(cchatJs, function() {
                        WebAgent.ChatInit({
                            selectorName:'#im-container',
                            callback: function(){
                                CCODMultiChannel.init(WebAgent,
                                    function(){console.log('[CDesk] 当前置忙置闲状态不能使用');$("#setReadyToggle").addClass('disabled');},
                                    function(s,moreStates){
                                        console.log('[CDesk] 当前置闲置忙状态能使用,状态为:'+s);
                                        $("#setReadyToggle.disabled").removeClass('disabled');
                                        if(s == 'BUSY'){
                                            $("#currentStatus").text('已经置忙');
                                            $("#phoneBtn").removeClass('disabled');
                                        }else{
                                            $("#currentStatus").text('已经置闲');
                                            if(moreStates.waState === "READY"){
                                                $("#phoneBtn").addClass('disabled');
                                                WebAgent.WaChat.hide();
                                            }
                                        }
                                    },
                                    awayStatus
                                );
                                //如果软电话为自动登录，显示地去置忙(待商榷怎么搞，如果用户只是刷新了浏览器，用户需要保持跟之前的状态一致)
                                if(waAutoLoginResult){
                                    console.log('[CDesk] WA自动登录成功,数据为:' + JSON.stringify(waAutoLoginData));
                                    loginCCODMultiChannel($.extend({
                                        entId:"${user.ccodEntId}",
                                        agentId:"${user.userId}",
                                        agentPassword:"${pwd}",
                                        agentNumber:'${user.telPhone}',
                                        isForce:true
                                    },waAutoLoginData.ext),true);
                                }
                                else{
                                    loginCCODMultiChannel({
                                        entId:"${user.ccodEntId}",
                                        agentId:"${user.userId}",
                                        agentPassword:"${pwd}",
                                        agentNumber:'${user.telPhone}',
                                        isForce:true
                                    },false);
                                }
                                WebAgent.ChatToggle();
                                WebChatEventHook();
                            }
                        });
                        WAEventHook();
                    });
                }
            });
        }
    });

    /**
     * 登陆多渠道统一入口
     * p:登陆信息
     * isWAAutoLogin:软电话是否已经自动登陆
     */
    var loginCCODMultiChannel = function(p,isWAAutoLogin){
        //登陆软电话
        function WALogin(p){
            var q = Q.defer();
            console.log('[CDesk] 软电话登陆信息:'+JSON.stringify(p));
            WebAgent.registerEventHandler(function(data){
                if(data.type === 'EVENT_AGENT_LOGIN'){
                    console.log('[CDesk] 软电话登陆成功,返回数据:'+JSON.stringify(data));
                    p.appkey = data.ext.nubeAppKey;
                    p.numbe  = data.ext.nubeNum && data.ext.nubeNum.replace("btl:", "") || "";
                    p.uid    = data.ext.nubeUUID;
                    q.resolve(p);
                }
                if(data.type === 'EVENT_AGENT_LOGIN_FAIL'){
                    q.reject(new Error(JSON.stringify({
                        channel:'WA',
                        msg:data.msg
                    })));
                }
            });
            WebAgent.registerResultHandler(function(data){
                if(data.type === 'login' || data.type === 'forceLogin'){
                    if(data.code !== "000"){
                        q.reject(new Error(JSON.stringify({
                            channel:'WA',
                            msg:data.msg || data.ext.errorMessage || ''
                        })));
                    }
                }
            });

            if(WebAgent){
                var r = WebAgent.extend.login(p);
                console.log('[CDesk] 软电话登录立即返回结果:' + JSON.stringify(r));
            }
            return q.promise;
        }

        //登陆WebChat
        function WebChatLogin(p){
            var q = Q.defer();
            console.log('[CDesk] WebChat登陆信息:' + JSON.stringify(p));
            WebAgent && WebAgent.ChatRegisterEvent("loginFailCallback", function(data) {
                if(data && data.type === 'loginFail'){
                    q.reject(new Error(JSON.stringify({
                        channel:'IM',
                        msg:''
                    })));
                }
                else{
                    q.resolve(p);
                }
            });
            WebAgent && WebAgent.ChatRegisterEvent("loginCallback", function(data){
                q.resolve(p);
            });
            WebAgent && WebAgent.ChatLogin({
                agentId: p.agentId,
                entId: p.entId,
                password:'1'
            });

            return q.promise;
        }

        //登陆Butel
        function ButelLogin(p){
            var q = Q.defer();
            console.log('[CDesk] Butel登陆信息:' + JSON.stringify(p));
            WebAgent.ChatRegisterEvent('getButelState',function(data){
                if(data === 'msgFromAgentButelLogin') {
                    q.resolve(p);
                }
                if(data === 'msgFromAgentButelLoginFail'){
                    q.reject(new Error(JSON.stringify({
                        channel:'Butel',
                        msg:'errorCode' + data.errorCode
                    })));
                }
            });
            //Butel && Butel.ajaxFunc("Login?appkey=" + p.appkey + "&numbe=" + p.numbe + "&uid=" + p.uid + "&localname=test", "login");
            Butel && Butel.ajaxFunc("Login?appkey=" + p.appkey + "&numbe=" + p.numbe + "&uid=" + p.uid + "&localname=test", "login");
            return q.promise;
        }

        function onLoginError(e){
            var channel = e.channel;
            var msg = e.msg;
            notice.danger('登陆多渠道('+channel+')失败:' + msg);
            if(channel === 'WA'){}
            else if(channel === 'IM'){
                WebAgent && WebAgent.extend.logout();
            }
            else if(channel === 'Butel'){
                WebAgent && WebAgent.extend.logout();
            }
        }

        if(!isWAAutoLogin){
            WALogin(p)
                .then(function(data){
                    return WebChatLogin(data);
                })
                .then(function(data){
                    return ButelLogin(data);
                })
                .then(function(){
                    notice.success('登陆多渠道成功');
                })
                .catch(function(e){
                    onLoginError(JSON.parse(e.message));
                });
        }
        else{
            WebChatLogin(p).then(function(data){
                return ButelLogin(data);
            })
            .then(function(){
                console.log('[CDesk] 登陆结束');
                notice.success('登陆多渠道成功');
            })
            .catch(function(e){
                onLoginError(JSON.parse(e.message));
            });
        }
    };

    //登出 CCODMultiChannel
    function logoutCCODMultiChannel(){
        notice.info('正在登出多渠道！');
        window.setTimeout(function(){
            window.location.href = "<%=request.getContextPath()%>/logout";
        },1*1000);
        WebAgent && WebAgent.extend.logout();
        Butel && Butel.ajaxFunc("Logout", "logOut");
        WebAgent.ChatLogout();
    }

    /**
     * 软电话挂事件
     */
    function WAEventHook(){
        var isInBound = false;
        WebAgent.registerEventHandler(function(data){
            //外呼客户振铃事件 呼入坐席振铃事件
            if(data.type == "EVENT_OUTBOUND_ALERTING_OP" || data.type == "EVENT_INBOUND_ALERTING"){
                thisCall.source="";
                if(data.type == "EVENT_OUTBOUND_ALERTING_OP"){
                    thisCall.source = "6";
                } else if(data.type == "EVENT_INBOUND_ALERTING"){
                    thisCall.source = "5";
                }
                var telphone=data.ext.strDnis;
                var userParam={userAccount:telphone,userAccountType:'1',telphone:telphone,entId:'${user.entId}',signature:'${signKey}',updatorAccount:'${user.loginName}'};
                $.post('<%=request.getContextPath()%>/userApi/updateOrAddUser',userParam).then(function(userData){
                    var ud = (new Function('return ' + userData)());
                    thisCall.startTime=new Date().getTime();
                    if(ud.success){
                        createComm(new Date().getTime(),data,ud.rows.userId, data.ext.strDnis, thisCall.source);
                    }else{
                        console.dir("添加用户出错:"+ud.msg);
                    }
                });
            }
            //呼入坐席接通 外呼客户接通
            if(data.type == "EVENT_INBOUND_CONNECTED" || data.type == "EVENT_OUTBOUND_CONNECTED_OP"){
                thisCall.startTime = new Date().getTime();//开始时间
                if(data.type === "EVENT_INBOUND_CONNECTED"){
                    isInBound = true;
                }
                else{
                    isInBound = false;
                }
            }

            //EVENT_TP_DISCONNECT:外呼挂断（客户挂断或者坐席挂断）
            if(data.type == "EVENT_TP_DISCONNECT" || data.type == "EVENT_OP_DISCONNECT"){
                console.dir("挂机信息:"+JSON.stringify(data));
                thisCall.endTime=new Date().getTime();
                var callSeconds=parseInt((thisCall.endTime-thisCall.startTime)/1000);
                var min=0;
                var showTimes="";
                if(callSeconds>=60){
                    min=parseInt(callSeconds/60);
                    showTimes=min+"分"+(callSeconds-60*min)+"秒";
                }
                else{
                    showTimes=callSeconds+"秒";
                }
                var content1 = '<ul><li>去电详情</li><li>呼出电话至:'+data.ext.strDnis+'</li>'+
                        '<li>呼出号码:' + data.ext.strAni + '</li>' +
                        '<li>通话时间:' + cri.formatDate(new Date(thisCall.startTime),'yyyy年MM月dd日 hh:mm') + '</li>' +
                        '<li>通话时长:'+showTimes+'</li>'+
                        '<li>呼出人:${user.loginName}</li></ul>';
                var content = '<audio src="<%=request.getContextPath()%>/download/HeIsPirate.mp3" controls/>'+content1;
                updateComm(thisCall.startTime, thisCall.endTime, callSeconds, data,thisCall.source);

                if(isInBound){
                    CCODMultiChannel.agentReady(function(){
                        console.log('[CDesk] 多渠道能力置闲成功');
                        $("#currentStatus").text('已经置闲');
                    },function(){
                        console.log('[CDesk] 多渠道能力置闲失败');
                    });
                }
                else{
                    CCODMultiChannel.agentBusy(function(){
                        console.log('[CDesk] 多渠道能力置忙成功');
                        $("#currentStatus").text('已经置忙');
                    },function(){
                        console.log('[CDesk] 多渠道能力置忙失败');
                    });
                }

            }
        });
    }

    /**
     * WebChat 事件回调
     */
    function WebChatEventHook(){
        //新用户进来注册函数
        WebAgent.ChatRegisterEvent('newCustomInter',function(data){
            console.log('[WebChat] 新用户进入 信息:' + JSON.stringify(data));//{userId:'如果cDesk没有传入给IM，则IM生成userId,如果传入，则回传给cDesk',skillGroupId:'',skillGroupName:''}
            imWindow && imWindow.onNewCustomer(data.userId, data.userSource);
        });

        //切换客户注册函数
        WebAgent.ChatRegisterEvent('selectUser',function(data){
            console.log('[WebChat] 切换客户 信息:' + JSON.stringify(data));//data.userId:（如果cDesk没有传入给IM，则IM生成userId,如果传入，则回传给cDesk）
            imWindow && imWindow.switchCustomer(data.userId);
        });

        //客户下线注册函数
        WebAgent.ChatRegisterEvent('sessionQuit',function(data){
            console.log('[WebChat] 客户下线 信息:' + JSON.stringify(data));//data.userId;data.messages:聊天记录(数组)
            imWindow && imWindow.onCustomerLeave(data.userId,data.messages);
        });

        WebAgent.ChatRegisterEvent('getButelState',function(data){
            console.log('[WebChat] Butel事件:' + JSON.stringify(data));
            //坐席振铃
            if(data === 'msgFromAgentButelAlerting') {
                console.log('[WebChat] TODO:视频振铃');
                $("#content-main iframe").hide();
                $("#IMContainer").show();
            }

            //坐席接通
            if(data === 'msgFromAgentButelConnected') {
                console.log('[WebChat] TODO:坐席接通');
            }
            //坐席发送butel呼叫结束
            //坐席发送butel呼叫振铃取消，没有接听只振铃就取消了
            if(data === 'msgFromAgentButelEnd' || data === 'msgFromAgentButelAlertingAbort') {
                console.log('[WebChat] TODO:取消');
            }

            if(data === 'msgFromAgentButelLogout') {
                console.log('[WebChat] Butel登出');
                console.log('[WebChat] 登出IM');
                WebAgent && WebAgent.ChatLogout();
                location.href = "<%=request.getContextPath()%>/logout";
            }
        });

        WebAgent.ChatRegisterEvent('unReadMsgNum',function(data){
            console.log('未读消息数' + data.unReadMsgNum);
            var n = +data.unReadMsgNum;
            n = n < 100 ? n:99;
            var $n = $("#noticeNumber").text(n);
            if(n <= 0){
                $n.hide();
            }
            else{
                $n.show();
            }
        });
    }

    /**
     * 查询所属客服组
     * @param userId
     * @param entId
     */
    function queryServiceGroup(userId,entId){
        return $.post("<%=request.getContextPath()%>/userApi/queryAgentGroup",{userId:userId,entId:entId});
    }

    /**
     * 软电话创建联络历史
     **/
    function createComm(startTime,paramData,userId,calloutPhone, source){
        var title='呼出至 '+calloutPhone+' 的语音电话';
        var param=encodeURI(JSON.stringify(paramData));
        var url="<%=request.getContextPath()%>/communicate/goComm?startTimeStr="+startTime+"&source="+source+"&userId="+userId+"&param="+param;
        $("#IMContainer").hide();
        openTab(url,"",title,true);
    }

    /**
     * 联络历史更新
     * 挂机调用
     **/
    function updateComm(startTime, endTime, showTimes, paramData, source){
        $.ajax({
            url:"<%=request.getContextPath()%>/communicate/endComm",
            dataType:'json',
            data:{param:encodeURI(JSON.stringify(paramData)),startTimeStr:startTime,endTimeStr:endTime,commTime:showTimes,source:source},
            success:function(data){
                if(data.success){
                }else{
                    notice.alert(data.msg);
                }
            }
        });
    }

    /*
     * 扩展外呼
     */
    function callOut(telphone){
        var result = WebAgent.extend.makeCall({
            outCallNumber:telphone
        });
        if(result.code==0){
            WA.cchat.toggle();
        }else{
            notice.alert(result.msg);
        }
    }
</script>

