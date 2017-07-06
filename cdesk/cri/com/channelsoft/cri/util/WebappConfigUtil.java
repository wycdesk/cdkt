package com.channelsoft.cri.util;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Node;
import org.dom4j.io.SAXReader;

import com.channelsoft.cri.common.BaseObject;
import com.channelsoft.cri.exception.BaseException;

public class WebappConfigUtil extends BaseObject{

	private final static String CONFIG_FILE_NAME = "/webapp-config.xml";//配置文件的路径字符串
	private Document configDocument;
	private static WebappConfigUtil instance = new WebappConfigUtil();//得到配置文件的document对象
	private WebappConfigUtil() {
		SAXReader reader = new SAXReader();//创建一个读取xml文件的对象
		try {
			configDocument = reader.read(WebappConfigUtil.class
					.getResourceAsStream(CONFIG_FILE_NAME));//括号内：查找具有给定名称的资源 括号外：得到document对象

		} catch (DocumentException exp) {
			super.logFail("", "读取配置文件", CONFIG_FILE_NAME, new BaseException(exp.getMessage()));
			configDocument = DocumentHelper.createDocument();
			configDocument.addElement("webapp-configs");
		}
	}
	public static Boolean getBoolean(String name) {
		String value = getParameter(name);
		return Boolean.valueOf(value);
	}

	public static Integer getInteger(String name, Integer defValue) {
		String value = getParameter(name);
		if (StringUtils.isEmpty(value)) {
			return defValue;
		}
		try {
			return Integer.valueOf(value);
		} catch (NumberFormatException exp) {
			instance.logFail("", "读取配置参数", CONFIG_FILE_NAME, new BaseException("不是数字"));
			return defValue;
		}
	}

	public static String getParameter(String name) {
		//构建读取wabapp-config.xml的字符串（大标签套小标签）
		String xpath = "/webapp-configs/param[@name='" + name + "']/@value";
//		System.out.println("instance="+instance);
//		System.out.println("instance.configDocument"+instance.configDocument);
		Node node = instance.configDocument.selectSingleNode(xpath);//找到节点
		if (node == null) {
			instance.logFail("", "读取配置参数", name, new BaseException("参数不存在"));
			return "";
		}
		return node.getText();//返回节点的文本
	}
	
	public static List<String> getParameterList(String name) {
		String xpath = "/webapp-configs/param[@name='" + name + "']/list/value";
		List<?> nodes = instance.configDocument.selectNodes(xpath);
		List<String> values = new ArrayList<String>();
		for (Iterator<?> iter = nodes.iterator(); iter.hasNext();) {
			Node node = (Node) iter.next();
			values.add(node.getText());
		}
		return values;
	}
}

