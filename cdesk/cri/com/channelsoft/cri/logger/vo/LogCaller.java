package com.channelsoft.cri.logger.vo;

import java.io.Serializable;

/**
 * 获取定义本对象的函数的调用者。
 * <dl>
 * <dt>portal</dt>
 * <dd>Description:</dd>
 * <dd>Copyright: Copyright (C) 2013</dd>
 * <dd>Company: 青牛(北京)技术有限公司 成都研究所</dd>
 * <dd>CreateDate: 2013-4-27</dd>
 * </dl>
 * @author 魏铭
 */
public class LogCaller  implements Serializable{
	private static final long serialVersionUID = -3188990205172995018L;
	private String className;
	private String methodName;
	private String fullClassName;
	
	public LogCaller()
	{
		StackTraceElement element = new Exception().getStackTrace()[2];
		this.fullClassName = element.getClassName();
		this.methodName = element.getMethodName();
		String[] path = this.fullClassName.split("\\.");
		className = path[path.length-1];
	}
	public LogCaller(int i)
	{
		StackTraceElement element = new Exception().getStackTrace()[i + 2];
		this.fullClassName = element.getClassName();
		this.methodName = element.getMethodName();
		String[] path = this.fullClassName.split("\\.");
		className = path[path.length-1];
	}
	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getFullClassName() {
		return fullClassName;
	}

	public void setFullClassName(String fullClassName) {
		this.fullClassName = fullClassName;
	}
	
	public String getHeader() {
		return className + "." + methodName + "() - ";
	}
}
