<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<div id = "divForHistory" style = "display: none">
    <div class="history-list panel-body">
        <div class="sub-switch-body" id="sorts">
            <div class="body">
                <div id="history-list" >
                
                </div>
            </div>
        </div>
    </div>
</div>

<script id="history-list-template" type="text/x-handlebars-template">
    <div class="form-group listContainer">
		<form class="messageNone" style="display:none;">
			<input name="sessionId" value="{{sessionId}}" />
			<input name="ccodEntId" value="{{ccodEntId}}"/>
			<input name="ccodAgentId" value="{{ccodAgentId}}"/>
		</form>
        <div class="list-bar">
            <div class="fa"></div>
            <div class="list-time" style="background:#fff;padding-right:10px;">{{createTime}} </div>
            <%--{{userName}}--%>
            <div class="list-line" style="width: calc(100% - 80px);position: absolute;top: 10px;display:none;"></div>
            <div class="neirong-list" style="width: calc(100% - 400px);"><span>{{content}}</span></div>
            <div class="append">
                <a>补录工单</a>
            </div>
            <div class="icon">
                <a class="fa fa-chevron-circle-down"></a>
            </div>

        </div>
        <div class="text-list" style="display:none;">
            <div class="textarea-list"><textarea>{{content}}</textarea></div>
            <%--消息--%>
            <div class="new-message" style="display:none;">
                <div class="chatting">
                    <div class="chakan-long" style="margin-right:10px;"><a>< 查看更多 ></a></div>
                    <div class="chatting-text">
                        暂无消息
                    </div>
                </div>
            </div>

            <%--电话--%>
            <div class="phone-message" style="display:none;">
                <ul>
                    <li>
                        <div>
                            <label>呼叫类型:</label>
                            <span>{{callType}}</span>
                        </div>
                        <div>
							{{#if sessionId}}
                            	<label>呼叫时间:</label>
							{{else}}
								<label>补录时间:</label>
							{{/if}}
							<span>{{createTime}}</span>
                        </div>
                        <div>
                            <label>是否接听:</label>
                            <span>{{isConnected}}</span>
                        </div>
                    </li>
                    <li>
                        <div>
                            <label>主叫:</label>
                            <span>{{strAni}}</span>
                        </div>
                        <div>
                            <label>接机时间:</label>
                            <span>{{startTime}}</span>
                        </div>
                        <div>
                            <label>处理坐席:</label>
                            <span>{{opName}}</span>
                        </div>
                    </li>
                    <li>
                        <div>
                            <label>被叫:</label>
                            <span>{{strDnis}}</span>
                        </div>
                        <div>
                            <label>通话时长:</label>
                            <span>{{commTime}}s</span>
                        </div>
                        <div>
                            <label>呼叫技能组:</label>
                            <span></span>
                        </div>
                    </li>
                </ul>
				{{#if sessionId}}
                <div>
                    <label>通话录音:</label>
                    <span class="audio-phone"><audio src="" controls="controls" id="recordUrl"></audio><a id="recordDown">下载录音</a></span>
                </div>
				{{/if}}
            </div>
        </div>

    </div>
</script>

<script type="text/javascript">
//根据用户ID查询联络历史
function getComms(){
    $.post("<%=request.getContextPath()%>/communicate/getCommsByUserId",{userId:"${user.userId}"},function(data){
        var html = [];
        var lsTemp = Handlebars.compile($("#history-list-template").html());
        if(!(data.success)){
            notice.alert(data.msg);
            return;
        }
        var rows=data.rows?data.rows:0;
        $("#communication").text("联络历史("+rows.length+")")
        if(rows.length>0){
            for(var i=0;i<rows.length;i++){
                var li=$(lsTemp(rows[i]));
                if(!rows[i].content){
                    $(li.find("div.neirong-list")[0]).find("span").empty().append("此次联络暂无沟通小结");
                }
                var iconDiv = li.find("div.list-bar > div:first");
                switch(rows[i].source){
                    case "1":
                    	iconDiv.addClass("fa-im");
                        var msgDiv=$(li.find("div.new-message")[0])
                        msgDiv.css("display","block");
                        initIMMessage(msgDiv,rows[i].message);
                        break;
                    case "3":
                    	iconDiv.addClass("fa-envelope-o");
                        break;
                    case "5":case "6":
                    	iconDiv.addClass("fa-phone");
                    	li.find("div.phone-message").show();
                    	break;
                    case "7":
                    	iconDiv.addClass("fa-weixin");
                        li.find("div.new-message").show();
                        break;
                    case "8":
                    	iconDiv.addClass("fa-video-camera");
                        break;
                    default:
                    	iconDiv.addClass("fa-clipboard");
                }
                //上（下）拉图标点击事件初始化
                li.find("div.icon").click(updownToggle);
                /* li.find("div.icon").click(function(){
                	
                }); */
                //补录工单按钮点击事件初始化
                li.find("div.append").click((function(){
                	var param=rows[i].param;
                	return function(){
                		newworkParam(param);
                	}
                })());
                //（下拉框）沟通小结框里的值改变事件初始化
                li.find("textarea").on("change",(function(commId){
                	var commId=rows[i].commId;
                	return function(){
                		submitContent(commId,$(this).val());
                	};
                })(rows[i].commId));
                html.push(li);
            }
        }
        $("#history-list").empty().append(html);
    },'json');
}

//上（下）拉图标点击事件处理函数
function updownToggle(){
	var $this = $(this);
	var $c = $this.closest('.listContainer');
    var tog=$c.find("div.text-list")[0];
    var neirong=$c.find("div.neirong-list")[0];
    
    
    var jqA=$($(this).find("a")[0]);
    if(jqA.hasClass("fa-chevron-circle-down")){
    	//内容展开处理
        jqA.removeClass("fa-chevron-circle-down");
        jqA.addClass("fa-chevron-circle-up");
        $(neirong).find("span").css("display","none");
        if($c.find("div.phone-message").css("display")=="block"){
        	if($c.find("audio#recordUrl").length>0){
        		//初始化录音播放控件和下载录音链接
        		initAudioAndDownload($c);
        	}
        }
    }else{
    	//内容上拉处理
        jqA.removeClass("fa-chevron-circle-up");
        jqA.addClass("fa-chevron-circle-down");
        $(neirong).find("span").css("display","inline");
    }
    
    $(tog).toggle();
}

//初始化录音播放控件和下载录音链接
function initAudioAndDownload($c){
	var $audio = $c.find("audio#recordUrl");
	$audio.css("display","");
	$audio.siblings("#recordDown").css("display","");
	if($audio.attr("src") == ""){
    	var form = $c.find("form").formValue();
    	if(form.sessionId&&form.ccodEntId&&form.ccodAgentId){
    		if($audio.siblings("font").length>0){
					$audio.siblings("font").remove();
			}
    		
    		//获取通话录音地址并赋值给audio标签
    		$.post("<%=request.getContextPath()%>/communicate/getRecordUrl",form,function(data){
   				if(data.success){
   					if(data.rows){
   						//判断是否为mp3格式
   						if(data.rows.indexOf(".mp3")>=0){
   							$audio.on("error",function(){
   								$audio.css("display","none");
   		       					if($audio.siblings("font").length<=0){
   		       						$audio.after("<font color='red'>录音资源已损坏</font>");
   		       					}
   		       					$audio.siblings("#recordDown").css("display","none");
   		       				});
   							$audio.attr("src",data.rows);
   							
   						}else{
   							$audio.css("display","none");
   							$audio.after("<font color='red'>录音格式不支持,格式需为mp3</font>");
   						}
   						
   						var dowloadUrl="<%=request.getContextPath()%>/communicate/dowonloadRecord?httpUrl="+encodeURI(data.rows);
	       				$c.find("a#recordDown").prop("href",dowloadUrl);
   					}else{
   						$audio.css("display","none");
   						if($audio.siblings("font").length<=0){
       						$audio.after("<font color='red'>录音资源不存在</font>");
	       				}
       					$audio.siblings("#recordDown").css("display","none");
   					}
       			}else{
       				notice.warning(data.msg);
       				$audio.after("<font color='red'>"+data.rows+"</font>");
       				$audio.siblings("#recordDown").css("display","none");
       			}
    		},'json');
    	}
    }
}
$(function(){
	getComms();
})

function newworkParam(param){
	param=encodeURI(param);
	parent.newwork("","","",param);
}
</script>

<script id="comm-IM-temp-left" type="text/x-handlebars-template">
<div style="float:left">
	<div class="leftCN-comm"></div>
	<div class="leftBox-comm">
		<span>{{text}}</span>
	</div>
</div>
<br/><br/>
</script>
<script id="comm-IM-temp-right" type="text/x-handlebars-template">
<div style="float:right">
	<div class="rightCN-comm"></div>
	<div class="rightBox-comm">
		<span>{{text}}</span>
	</div>
</div>
<br/><br/>

</script>

<script type="text/javascript">
	/*展示IM沟通消息*/
	function initIMMessage(msgDiv,msgList){
		if(!msgList){
			return;
		}
		var msgContent=$(msgDiv.find("div.chatting-text")[0]);
		var jsonList=eval("("+msgList+")");
		msgDiv.find("div.chatting").css("padding-left","0px");
		msgDiv.find("div.chatting").css("padding-right","0px");
		msgContent.empty();
		for(var id in jsonList){
			msgContent.css("padding-left","10px");
			msgContent.css("padding-right","10px");
			msgContent.css("overflow","auto");
			msgContent.css("max-height","200px");
			if(jsonList[id].direction=="recv"){
				var leftTemp = Handlebars.compile($("#comm-IM-temp-left").html());
				//msgContent.append("<div style='margin-top:10px;float:left;width:100%;'><div style='padding:5px;border-radius:6px;background:#00faaa;float:left;'><span style='float:left;'>"+jsonList[id].text+"</span></div></div>");
				msgContent.append(leftTemp(jsonList[id]));
			}else{
				var rightTemp = Handlebars.compile($("#comm-IM-temp-right").html());
				//msgContent.append("<div style='float:left;width:100%;'><div style='padding:5px;border-radius:6px;background:yellow;float:right;'><span style='float:right;'>"+jsonList[id].text+"</span></div></div>");
				msgContent.append(rightTemp(jsonList[id]));
			}
		}
	}
	
	function submitContent(commId,content){
        var param={};
        param.commId=commId;
        param.content=content;
        $.post("<%=request.getContextPath()%>/communicate/saveContent",param,saveBack,'json');
    }
    function saveBack(data){
        if(data.success){
            notice.success(data.msg);
            getComms();
        }else{
            notice.alert(data.msg);
        }
    }
</script>
