package com.channelsoft.ems.api.constants;

public class EventNameConstants {

	public static final String EVENT_CALL_CONTROL_FAIL="EVENT_CALL_CONTROL_FAIL";
	public static final String EVENT_CALL_CONTROL_FAIL_DESC="外呼呼叫控制失败";
	
	public static final String EVENT_MAKECALL_FAIL="EVENT_MAKECALL_FAIL";
	public static final String EVENT_MAKECALL_FAIL_DESC="当前状态不允许外呼";
	
	public static final String EVENT_OP_DISCONNECT="EVENT_OP_DISCONNECT";
	public static final String EVENT_OP_DISCONNECT_DESC="客户挂断";
	
	public static final String EVENT_TP_DISCONNECT="EVENT_TP_DISCONNECT";
	public static final String EVENT_TP_DISCONNECT_DESC="坐席挂断";
	
	public static final String EVENT_OUTBOUND_ALERTING_OP="EVENT_OUTBOUND_ALERTING_OP";
	public static final String EVENT_OUTBOUND_ALERTING_OP_DESC="客户振铃";
	
	public static final String EVENT_OUTBOUND_CONNECTED_OP="EVENT_OUTBOUND_CONNECTED_OP";
	public static final String EVENT_OUTBOUND_CONNECTED_OP_DESC="外呼成功";

}
