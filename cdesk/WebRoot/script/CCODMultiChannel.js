/**
 * 多渠道状态控制
 * @author zhouzy@channelsoft.com
 * @date 16/03/16.
 * 依赖Q.js
 */
!function(Q){
    /**
     * CCOD 多渠道集成
     * 1.状态控制
     * 2.回调方法
     * @constructor
     * @author:zhouzy@channelsoft.com
     * @date:2016/03/16
     */
    var isWAStateLocking = 0;//软电话是否锁定（通话时） 1:不可以置闲置忙 0:可以置闲置忙
    var WAState = 'BUSY';

    var isChatStateLocking = 0;//WebChat 是否锁定(通话、视频) 1:不可以置闲置忙 0:可以置闲置忙
    var ChatState = 'BUSY';

    var CCODMultiChannel = function(){
        this._WA = null;
        this._phoneStateBusyPromise = null;
        this._phoneStateReadyPromise = null;
        this._chatStateBusyPromise = null;
        this._chatStateReadyPromise = null;
        this._setChatStateReadyProcess = null;
    };

    /**
     * @param WA:
     * @param locking:状态改变回调函数
     * @param unLocking:状态改变回调函数
     * @param awayReasonList:小休待选项
     */
    CCODMultiChannel.prototype.init = function(WA,locking,unLocking,awayReasonList){
        var self = this;
        this._WA = WA;
        this._locking   = locking;
        this._unLocking = unLocking;
        this._setAwayList(awayReasonList);

        //软电话挂事件钩子 data={type:'',ext:eventData}
        WA.registerEventHandler(function(data){
            self._phoneStateHook(data);
        });

        //IM挂事件钩子
        self._IMStateHook();
        //this._setChatBusy();
    };

    /**
     * 坐席置闲多渠道能力
     * 提供给业务层，表示坐席准备处理来自多渠道的消息
     * 手动置闲，2s无电话进入再将chat置闲
     * s:置闲成功回调
     * f:置闲异常回调
     */
    CCODMultiChannel.prototype.agentReady = function(s,f){
        var self = this;
        if(!isWAStateLocking){
            self._setChatStateReadyProcess = function(){
                var task = function(){
                    self._setChatReady();
                    self._setChatStateReadyProcess = null;
                };
                var _timer = null;
                return {
                    start:function(){
                        _timer || (_timer = window.setTimeout(task,2*1000));
                    },
                    pend:function(){
                        if(_timer){
                            window.clearTimeout(_timer);
                            _timer = null;
                        }
                    }
                };
            }();
            self._setPhoneReady().then(function(){
                self._setChatStateReadyProcess.start();
                s && s.call();
            },f);
        }
    };

    /**
     * 坐席置忙多渠道能力
     * 提供给业务层，表示坐席不准备处理多渠道消息
     * 当软电话或者chat锁定时，不能置闲置忙
     * s:置忙成功回调
     * f:置忙异常回调
     */
    CCODMultiChannel.prototype.agentBusy = function(s,f){
        var self = this;
        if(!isWAStateLocking){
            //置忙时,清除掉10s等待置闲WebChat的计时器
            //self._chatTimer && clearTimeout(self._chatTimer);
            self._setChatStateReadyProcess && self._setChatStateReadyProcess.pend();
            self._setPhoneBusy().then(function(){
                self._setChatBusy().then(function(){
                    s && s.call();
                });
            });
        }
    };

    /**
     * 软电话置忙
     * 当可以置忙时，返回置忙的Promise
     * @private
     */
    CCODMultiChannel.prototype._setPhoneBusy = function(){
        console.log('[CCODMultiChanel] 置忙软电话');
        var self = this;
        var re = this._WA.extend.setBusy();
        if(re.code != 0){
            //如果置忙失败(多是由于已经置忙)，该Promise不会被resolve(不会拿到置忙成功事件),所以需要在此手动resolve
            window.setTimeout(function(){
                self._phoneStateBusyPromise && self._phoneStateBusyPromise.resolve();
            },100);
        }
        this._phoneStateBusyPromise = Q.defer();
        return this._phoneStateBusyPromise.promise;
    };

    /**
     * 软电话置闲
     * @private
     */
    CCODMultiChannel.prototype._setPhoneReady = function(){
        console.log('[CCODMultiChanel] 置闲软电话');
        var self = this;
        var re = this._WA.extend.setReady();
        if(re.code != 0){
            //如果置闲失败(多是由于已经置闲)，该Promise不会被resolve(不会拿到置闲成功事件),所以需要在此手动resolve
            window.setTimeout(function(){
                self._phoneStateReadyPromise && self._phoneStateReadyPromise.resolve();
            },100)
        }
        this._phoneStateReadyPromise = Q.defer();
        return this._phoneStateReadyPromise.promise;
    };

    /**
     * 监听软电话底层事件钩子
     * @private
     */
    CCODMultiChannel.prototype._phoneStateHook = function(d){
        var self = this;
        var eventType = d.type;
        if(eventType === events.EVENT_AGENT_LOGIN){
            self._setAwayList(d.ext.awayStatusList);
        }
        //入呼叫，坐席振铃
        if(eventType === events.EVENT_INBOUND_ALERTING){
            //判断是否chat是否将要置闲，如果是，取消置闲
            //this._chatTimer && window.clearTimeout(this._chatTimer);
            self._setChatStateReadyProcess && self._setChatStateReadyProcess.pend();
            //来电，置忙chat
            this._setChatBusy();
        }

        if(eventType === events.EVENT_AGENT_NOTREADY){
            console.log('[CCODMultiChanel] 软电话置忙成功');
            if(this._phoneStateBusyPromise){
                this._phoneStateBusyPromise.resolve();
                this._phoneStateBusyPromise = null;
            }
            isWAStateLocking = 0;
            WAState = 'BUSY';
            self._isLocking();
        }
        else if(eventType === events.EVENT_AGENT_READY){
            console.log('[CCODMultiChanel] 软电话置闲成功');
            if(this._phoneStateReadyPromise){
                this._phoneStateReadyPromise.resolve();
                this._phoneStateReadyPromise = null;
            }
            isWAStateLocking = 0;
            WAState = 'READY';
            self._isLocking();
            //打完电话，软电话自动置闲的情况，检查是否有挂起的置闲WebChat进程，如果有，则执行
            self._setChatStateReadyProcess && self._setChatStateReadyProcess.start();
        }
        else if(
            //当软电话的状态为非置闲置忙时，锁定置闲置忙
            eventType === events.EVENT_INBOUND_ALERTING ||
            eventType === events.EVENT_INBOUND_CONNECTED ||
            eventType === events.EVENT_OUTBOUND_ALERTING_TP ||
            eventType === events.EVENT_OUTBOUND_CONNECTED_TP ||
            eventType === events.EVENT_OUTBOUND_ALERTING_OP ||
            eventType === events.EVENT_OUTBOUND_CONNECTED_OP
        ){
            isWAStateLocking = 1;
            self._isLocking();
        }
    };
    /**
     * IM置忙
     * @private
     */
    CCODMultiChannel.prototype._setChatBusy = function(){
        console.log('[CCODMultiChanel] 置忙IM');
        this._chatStateBusyPromise = Q.defer();
        this._WA.ChatSetBusy();
        return this._chatStateBusyPromise.promise;
    };

    /**
     * IM置闲
     * @private
     */
    CCODMultiChannel.prototype._setChatReady = function(){
        console.log('[CCODMultiChanel] 置闲IM');
        this._chatStateReadyPromise = Q.defer();
        this._WA.ChatSetReady();
        return this._chatStateReadyPromise.promise;
    };

    /**
     * 监听IM底层事件钩子
     * @private
     */
    CCODMultiChannel.prototype._IMStateHook = function(){
        var self = this;
        //置忙成功回调：
        this._WA && this._WA.ChatRegisterEvent('busyCallback',function(){
            console.log('[CCODMultiChanel] 置忙IM成功');
            self._chatStateBusyPromise && self._chatStateBusyPromise.resolve();
            ChatState = "BUSY";
            isChatStateLocking = 0;
            self._isLocking();
        });
        //置闲成功回调
        this._WA && this._WA.ChatRegisterEvent('readyCallback',function(){
            console.log('[CCODMultiChanel] 置闲IM成功');
            self._chatStateReadyPromise && self._chatStateReadyPromise.resolve();
            ChatState = "READY";
            isChatStateLocking = 0;
            self._isLocking();
        });

        this._WA && this._WA.ChatRegisterEvent('getButelState',function(data){
            console.log('[CCODMultiChanel] Butel事件:' + JSON.stringify(data));
            //坐席振铃
            if(data.type === 'msgFromAgentButelAlerting') {
                isChatStateLocking = 1;
                self._isLocking();
                //当Butel弹屏时，使软电话为小休
                self._setChatBusy();
                self._WA.extend.setAway(self._awayResonList);
                self._WA.ChatSetBusy();
            }
            //坐席接通
            if(data.type === 'msgFromAgentButelConnected') {
                isChatStateLocking = 1;
                self._isLocking();
                //self._WA.ChatSetBusy();
            }
            //坐席发送butel呼叫结束
            //坐席发送butel呼叫振铃取消，没有接听只振铃就取消了
            if(data.type === 'msgFromAgentButelEnd' || data.type === 'msgFromAgentButelAlertingAbort') {
                console.log('[CCODMultiChanel] Butel呼叫结束,Butel未接听');
                isChatStateLocking = 0;
                self._isLocking();
                self.agentReady();
                self._WA.ChatSetReady();
            }
        });
    };

    /**
     * 当多渠道置闲置忙不能使用时触发
     * @private
     */
    CCODMultiChannel.prototype._isLocking = function(){
        if(isWAStateLocking || isChatStateLocking){
            this._locking && this._locking();
        }
        else{
            if(this._unLocking){
                var s = WAState == "READY" || ChatState == "READY" ? "READY" : "BUSY";
                this._unLocking(s,{
                    waState:WAState,
                    imState:ChatState
                });
            }
        }
    };

    /**
     * 设置软电话为小休
     * @param awayStatusList
     * @private
     */
    CCODMultiChannel.prototype._setAwayList = function(awayStatusList){
        if(awayStatusList){
            this._awayResonList = awayStatusList.replace(/^.+?=(\d+).*$/,"$1");
            //this._awayResonList = '1';
            console.log('[CCODMultiChanel] 使用小休序号为:' + this._awayResonList);
        }
    };

    window.CCODMultiChannel = new CCODMultiChannel();

    /**
     * 事件集
     */
    var events = {
        EVENT_AGENT_LOGIN : "EVENT_AGENT_LOGIN",            // 登录成功
        EVENT_NETTY_LOGIN_SUCCESS : "EVENT_NETTY_LOGIN_SUCCESS", // Netty方式登录成功（用于已存在http客户端登录的前提下）
        EVENT_AGENT_LOGOUT : "EVENT_AGENT_LOGOUT",          // 登出成功
        EVENT_AGENT_LOGIN_FAIL : "EVENT_AGENT_LOGIN_FAIL",  // 登录失败
        EVENT_AGENT_READY : "EVENT_AGENT_READY",             // 置闲
        EVENT_AGENT_NOTREADY : "EVENT_AGENT_NOTREADY",       // 置忙

        EVENT_OUTBOUND_ALERTING_TP : "EVENT_OUTBOUND_ALERTING_TP", // 外呼坐席振铃
        EVENT_OUTBOUND_CONNECTED_TP : "EVENT_OUTBOUND_CONNECTED_TP", // 外呼坐席接通
        EVENT_OUTBOUND_ALERTING_OP : "EVENT_OUTBOUND_ALERTING_OP",  // 外呼客户振铃
        EVENT_OUTBOUND_CONNECTED_OP : "EVENT_OUTBOUND_CONNECTED_OP", // 外呼客户接通
        EVENT_TP_DISCONNECT : "EVENT_TP_DISCONNECT",                 // 坐席挂断
        EVENT_INBOUND_ALERTING : "EVENT_INBOUND_ALERTING",          // 呼入坐席振铃事件
        EVENT_INBOUND_CONNECTED : "EVENT_INBOUND_CONNECTED",           // 呼入坐席接通事件

        EVENT_AGENT_ACW : "EVENT_AGENT_ACW"                                 // 事后整理
    };
}(Q);
