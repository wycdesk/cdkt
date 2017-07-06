/**
 * Created by panfei on 2016/3/18.
 */

(function(window, WebAgent) {
    var t = null;
    var agentStatuLast = "";
    var butelStateLast = "";
    var serviceFlag = false;
    var alertingFlag = false;
    var loginFlag = false;
    var errorCodeLast = false;
    var Butel = {};
    /**
     * ajax函数
     * @param url
     * @param callback
     */
    Butel.ajaxFunc = function(url,callback) {
        $.ajax({
            url: "http://localhost:8080/" + url,
            dataType: "jsonp",
            jsonpCallback: callback,
            global:false,
            success: function(data) {
                switch(callback) {
                    case "login":
                        butelLogin(data);
                        break;
                    case "queryState":
                        butelQueryState(data);
                        break;
                    case "logOut":
                        butelLogOut(data);
                        break;
                    default:
                        break;
                }
            },
            error: function(data){
                console.log(callback + "-error:" + (data.ret || "ButelState:" + data.ButelState + ", AgentStatus:" + data.AgentStatus));
            }
        })
    };

    /**
     * 登录成功后的回调
     * @param data
     */
    function butelLogin(data) {
        console.log("发送butel-login回调" + data.ret);
        t = setInterval(function() {Butel.ajaxFunc("getStatus", "queryState")}, 1000);
    }

    /**
     * 查询成功回调
     * @param d ata
     */
     function butelQueryState(data) {
        data = JSON.parse(data);
        var butelState = data.ButelState;
        var agentStatus = data.AgentStatus;
        var errorCode = parseInt(data.ErrorCode);
        var CadObj = data.Cad;
        $("#state").text("ButelState:" + butelState + ", AgentStatus:" + agentStatus);
        console.log("ButelState:" + butelState + ", AgentStatus:" + agentStatus);

        if(agentStatuLast != agentStatus) {
            agentStatuLast = agentStatus;
            console.log("agentStatuLast" + agentStatuLast);
            switch(agentStatuLast) {
                case "Audio":
                    return;
                case "Alerting":
                    console.log("state-Alerting");
                    alertingFlag = true;
                    WebAgent.butelEvent.msgFromAgentButelAlerting(CadObj);
                    console.log("WebAgent.butelEvent.msgFromAgentButelAlerting");
                    break;
                case "Servicing":
                    console.log("state-Servicing");
                    serviceFlag = true;
                    WebAgent.butelEvent.msgFromAgentButelConnected(CadObj);
                    console.log("WebAgent.butelEvent.msgFromAgentButelConnected");
                    break;
                case "UnService":
                    console.log("state-UnService");
                    if((butelState != "UnLogged") && alertingFlag) {
                        if(serviceFlag) {
                            WebAgent.butelEvent.msgFromAgentButelEnd(CadObj);
                            console.log( WebAgent.butelEvent.msgFromAgentButelEnd);
                        } else {
                            WebAgent.butelEvent.msgFromAgentButelAlertingAbort(CadObj);
                            console.log("WebAgent.butelEvent.msgFromAgentButelAlertingAbort");
                        }
                        serviceFlag = false;
                        alertingFlag = false;
                    }
                    break;
                default:
                    break;
            }
        }

        if((butelStateLast != butelState) || (errorCodeLast != errorCode)) {
            butelStateLast = butelState;
            errorCodeLast = errorCode;

            switch(butelStateLast) {
                case "Logged":
                    if(errorCode === 0) {
                        loginFlag = true;
                        WebAgent.butelEvent.msgFromAgentButelLogin(errorCode);
                        console.log("登录成功");
                        //登录成功
                    }

                    if(errorCode < 0) {
                        WebAgent.butelEvent.msgFromAgentButelLogoutFail(errorCode);
                        console.log("登出失败");
                        //登出失败
                    }
                    break;
                case "UnLogged":
                    if(loginFlag && (errorCode === 0)) {
                        WebAgent.butelEvent.msgFromAgentButelLogout(errorCode);
                        WebAgent.ChatLogout();
                        loginFlag = false;
                        console.log("登出成功");
                        //登出成功
                    }
                   if(errorCode < 0) {
                       WebAgent.butelEvent.msgFromAgentButelLoginFail(errorCode);
                       console.log("登录失败");
                        //登录失败
                   }
                    clearInterval(t);
                    break;
                default:
                    break;
            }
        }
     }

    /**
     * 登出成功回调
     * @param data
     */
    function butelLogOut(data) {
        console.log("发送butel-logout回调");
    }

    if(!window.Butel) {
        window.Butel = Butel;
    }

})(window, WebAgent);
