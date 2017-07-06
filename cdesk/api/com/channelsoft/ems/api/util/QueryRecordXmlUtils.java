package com.channelsoft.ems.api.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;


public class QueryRecordXmlUtils {

	private static Logger logger=Logger.getLogger(QueryRecordXmlUtils.class);
	
	public static List<String> parse(String xml)
    {
		List<String> list = new ArrayList<String>();
        Document doc = null;
        try {
            doc = DocumentHelper.parseText(xml); // 将字符串转为XML
            Element rootElt = doc.getRootElement(); // 获取根节点Response
            Iterator iters = rootElt.elementIterator("recordFile"); // 获取根节点下的子节点record
            
            //查询成功
            while(iters.hasNext()){
            	 Element recordEle = (Element) iters.next();
                 String recordName = recordEle.elementTextTrim("recordName"); 
                 list.add(recordName);
            }
            
        } catch (DocumentException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return list;
    }
	
	public static void main(String[] args){
		String xml = "<recordFiles>"+
				"<recordFile>"+
				"<recordName>"+
				"http://10.130.41.232:9081/ROOT/0000003300/201003/057128929523/20100302140654380_18951352069.wav"+
				"</recordName>"+
				"</recordFile>"+
				"<recordFile>"+
				"<recordName>"+
				"http://10.130.41.232:9081/ROOT/0000003300/201003/057128929523/20100302140654380_18951352069.wav"+
				"</recordName>"+
				"</recordFile>"+
				"</recordFiles>";

				
				 
		List<String> list = QueryRecordXmlUtils.parse(xml);
		System.out.println(list.get(0));
		System.out.println(list.get(1));
	}
}
