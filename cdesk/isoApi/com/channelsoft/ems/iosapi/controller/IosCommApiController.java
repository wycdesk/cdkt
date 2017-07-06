package com.channelsoft.ems.iosapi.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.communicate.constant.WorkSource;
import com.channelsoft.ems.communicate.po.CommPo;
import com.channelsoft.ems.communicate.service.ICommService;
import com.channelsoft.ems.iosapi.util.IosSignKeyUtils;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/iosCommApi")
public class IosCommApiController {

	@Autowired
	ICommService commService;
		
	/*查询我的联络历史*/
	@ResponseBody
	@RequestMapping(value = "/queryCommHistory")
	public ResultPo queryCommHistory(HttpServletRequest request,int page, int rows){	
		String entId=request.getParameter("entId");
		String userId=request.getParameter("userId");		
		String sk=request.getParameter("sk");

		CommPo commPo=new CommPo();
		commPo.setUserId(userId);
			
		List<DBObject> comms=null;
		PageInfo pageInfo = new PageInfo((page - 1) * rows, rows);
		try {     		
    		if(IosSignKeyUtils.validate(sk)){		
    			comms=commService.getComms(entId,commPo,pageInfo);  			
    		}else{
    			return ResultPo.failed(new Exception("签名错误"));	
    		}    		
		}catch(Exception e){
 			e.printStackTrace();
			return ResultPo.failed(new Exception("异常,"+ e.getMessage()));
		}
		
		if(comms!=null){			
			/*补填空缺的必须字段*/
			String field[]={"content","source","opId","opName","startTime","endTime","commId","createTime","commTime","isConnected","remoteUrl","strAni","callType"};
			for(DBObject db:comms){
				DBObjectUtils.fillField(db, field);
			}
			
			SimpleDateFormat format=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
			for(int i=0;i<comms.size();i++){
				DBObject dbI=comms.get(i);
				dbI.put("source", WorkSource.getEnum((String)dbI.get("source")).desc);
				dbI.put("createTime", format.format((Date)dbI.get("createTime")));
			}
			
			DBObject obj=new BasicDBObject();
			obj.put("commList", comms);
			obj.put("entId", entId);
			obj.put("sk", sk);
						
			return ResultPo.success("查询沟通历史成功", pageInfo!=null?pageInfo.getTotalRecords():comms.size(), obj);	 
		}else{
			return ResultPo.success("查询沟通历史成功",0, comms);	 
		}		
	}
	
	/*根据commId查询联络历史详情*/
	@ResponseBody
	@RequestMapping(value = "/queryCommDetail")
	public ResultPo queryCommDetail(HttpServletRequest request,int page, int rows){
		String entId=request.getParameter("entId");
		String commId=request.getParameter("commId");		
		String sk=request.getParameter("sk");		
		
		PageInfo pageInfo = new PageInfo((page - 1) * rows, rows);
		try {     		
    		if(IosSignKeyUtils.validate(sk)){
    			CommPo commPo=new CommPo();
    			commPo.setCommId(commId); 
    			/*根据commId查询联络历史*/
    			List<DBObject> comms=commService.getComms(entId,commPo,pageInfo); 
    			
    			/*补填空缺的必须字段*/
    			String field[]={"content","source","opId","opName","startTime","endTime","commId","createTime","commTime","isConnected","remoteUrl","strAni","callType"};
    			for(DBObject db:comms){
    				DBObjectUtils.fillField(db, field);
    			}
    			
    			SimpleDateFormat format=new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
    			
    			DBObject dbI=comms.get(0);
    			dbI.put("source", WorkSource.getEnum((String)dbI.get("source")).desc);
    			dbI.put("createTime", format.format((Date)dbI.get("createTime")));
    	    
    			return ResultPo.success("查询联络历史详情成功", pageInfo!=null?pageInfo.getTotalRecords():comms.size(), comms);	  			
    		}else{
    			return ResultPo.failed(new Exception("签名错误"));	
    		}    		
		}catch(Exception e){
 			e.printStackTrace();
			return ResultPo.failed(new Exception("异常,"+ e.getMessage()));
		}		
	}	
}
