package com.channelsoft.ems.iosapi.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.DBObjectUtils;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.constants.AgentRoleType;
import com.channelsoft.ems.api.po.CCODRequestPo;
import com.channelsoft.ems.api.po.ResultPo;

import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.group.po.AgentPo;
import com.channelsoft.ems.group.po.GroupPo;
import com.channelsoft.ems.group.service.IGroupService;
import com.channelsoft.ems.iosapi.util.IosSignKeyUtils;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.user.constant.RoleType;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.util.PhotoUrlUtil;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

@Controller
@RequestMapping("/iosUserApi")
public class IosUserApiController {

	@Autowired
	IDatEntService entService;
	
	@Autowired
	IUserMongoService userMongoService;
	
	@Autowired
	IUserFieldService userFieldService;
	
	@Autowired
	IGroupService groupService;
	
	/*分类查询用户列表*/
	@ResponseBody
	@RequestMapping(value = "/queryUser")
	public ResultPo queryUser(HttpServletRequest request,int page, int rows){   	
		String entId=request.getParameter("entId");
		String userType=request.getParameter("userType");		
		String sk=request.getParameter("sk");
			
		PageInfo pageInfo = new PageInfo((page - 1) * rows, rows);
		try {     		
    		if(IosSignKeyUtils.validate(sk)){
    			DBObject dbo=new BasicDBObject();  
    			
    			if(StringUtils.isBlank(userType)){   				
        	    	/*全部用户*/     	    	  	
        	    	dbo.put("entId", entId);
    			}else{
    				/*按用户类型查询用户列表*/   	
        	    	dbo.put("entId", entId);
        	    	dbo.put("userType", userType);     	    	
    			}	   
    			
    			List<DBObject> list=userMongoService.queryUserList(dbo, pageInfo);   			
    			
    			/*补填空缺的共有字段*/
    			String field[]={"nickName","email","telPhone","userName"};
    			for(DBObject db:list){
    				DBObjectUtils.fillField(db, field);
    			}	    		 			
    			DBObject obj = new BasicDBObject();
    			obj.put("list", list);
    			obj.put("entId", entId);
    			obj.put("sk", sk);
    			
    	    	return ResultPo.success("查询成功", pageInfo.getTotalRecords(), obj);	 	    	
    		}else{
    			return ResultPo.failed(new Exception("签名错误"));	
    		}
    	}
    	catch (Exception e) {
 			e.printStackTrace();
			return ResultPo.failed(new Exception("异常,"+ e.getMessage()));
 		}
	}
		
       /*用户详情页*/
	   @ResponseBody
	   @RequestMapping(value = "/userDetails")
	   	public ResultPo userDetails(HttpServletRequest request, Model model,HttpServletResponse response) {		   		    
		    String entId=request.getParameter("entId");
		    String userId= request.getParameter("userId");		    
			String sk=request.getParameter("sk");		
		    
			try {     		
	    		if(IosSignKeyUtils.validate(sk)){
	    			DBObject queryObject=new BasicDBObject();
	    			queryObject.put("entId", entId);
	    			queryObject.put("userId", userId);
	    			List<DBObject>  list=userMongoService.queryUserDetail(queryObject, null);
	    		   	   
	    		    //获取用户头像
	    		    PhotoUrlUtil.getPhotoUrl(request, model, entId, list.get(0).get("photoUrl")+"");		
	    		    	    		    
	    		    /*启用的用户自定义字段*/
	    			List<UserDefinedFiedPo> activeFieldList=userFieldService.queryDefinedFiled(entId, "1", null);				
	    		    	    			
	    			/*补填空缺的共有字段*/
	    			String field[]={"nickName","email","telPhone","userName","creatorId","creatorName","updatorId","updatorName","userDesc","userLabel","fixedPhone","remark","signature","contactPhone"};
	    			for(DBObject db:list){
	    				DBObjectUtils.fillField(db, field);
	    			}	
	    			
	    			/*填补空缺的自定义字段*/
	    			String field1[]=new String[activeFieldList.size()];	    			
	    			for(int i=0;i<activeFieldList.size();i++){
	    				field1[i]=activeFieldList.get(i).getKey();
	    			}	    				    		      			
	    			for(DBObject db:list){
	    				DBObjectUtils.fillField(db, field1);
	    			}	    		    
	    			DBObject obj=new BasicDBObject(); 	    			
	    			obj.put("activeFieldList", activeFieldList);
	    			obj.put("userList", list.get(0));
	    			
	    		    return ResultPo.success("查询成功", 1, obj);	
	    		}else{
	    			return ResultPo.failed(new Exception("签名错误"));	
	    		}
			}catch(Exception e){
	 			e.printStackTrace();
				return ResultPo.failed(new Exception("异常,"+ e.getMessage()));
			}
	   	}
	   	   
	    /*客服组客服信息(返回客服组map,以及客服组和包含客服的map)*/
		@ResponseBody
		@RequestMapping(value = "/serviceGroup")
		public ResultPo serviceGroup(HttpServletRequest request, Model model,HttpServletResponse response) {		   
			String entId=request.getParameter("entId");			
			String sk=request.getParameter("sk");				
		
			try {			
				if(IosSignKeyUtils.validate(sk)){	
					List<GroupPo> list = ParamUtils.getEntGroupList(entId);
										
					Map<String,Object> map1=new HashMap<String,Object>();									
					Map<String,Object> map=new HashMap<String,Object>();
					
					/*根据客服组Id查询客服*/
					for(int i=0;i<list.size();i++){
						List<AgentPo> agentlist = groupService.queryGroupAgentForMongo(entId, list.get(i).getGroupId());							
						List<DBObject> dbList=new ArrayList<DBObject>();
						
						for(AgentPo po:agentlist){
							DBObject obj=DBObjectUtils.getDBObject(po);							
							dbList.add(obj);
						}
												
						map.put(list.get(i).getGroupId(), dbList);					
						map1.put(list.get(i).getGroupId(), list.get(i).getGroupName());
					}			    
					DBObject obj=new BasicDBObject();
					obj.put("groupMap", map1);
					obj.put("agentGroupMap", map);
									
					return ResultPo.success("查询成功", 1, obj);
				}else{
					return ResultPo.failed(new Exception("签名错误"));	
				}			
			} catch (ServiceException e) {
				e.printStackTrace();
				return ResultPo.failed(new Exception("异常,"+ e.getMessage()));
			}
		}
				
		/*修改用户信息*/
		@ResponseBody
		@RequestMapping(value = "/updateUser")
		public ResultPo updateUser(String userInfos, HttpServletRequest request) throws Exception {
			SystemLogUtils.Debug("updateUser,userInfos="+userInfos);
		
			String sk=request.getParameter("sk");			
			try{
				if(IosSignKeyUtils.validate(sk)){				
					/**
					 * 通知CCOD修改坐席
					 */
					JSONObject json = new JSONObject(userInfos);
					DatEntUserPo userpo=ParamUtils.getAgentById(json.getString("userId"), json.getString("entId"));

					if(userpo == null){
						SystemLogUtils.Debug("========updateUser,未查询到缓存用户,不通知CCOD修改坐席==========");
						 userpo=userMongoService.queryUserById(json.getString("entId"), json.getString("userId"));
					}
					
					String userType = userpo.getUserType();
					if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){						
						CCODRequestPo ccodPo = new CCODRequestPo();
						ccodPo.setEnterpriseId(userpo.getCcodEntId());
						ccodPo.setAgentId(json.getString("userId"));
						if(json.has("userName")){
							ccodPo.setAgentName(json.getString("userName"));
						}else{
							ccodPo.setAgentName(userpo.getUserName());
						}
						ccodPo.setAgentPassword(CdeskEncrptDes.decryptST(userpo.getLoginPwd()));
						String roleId = "4";
						if(json.has("roleId")){
							roleId = json.getString("roleId");
						}
						if(RoleType.MONITOR.value.equals(roleId)){
							ccodPo.setAgentRole(AgentRoleType.MANAGER.value);
						}else{
							ccodPo.setAgentRole(AgentRoleType.NORMAL.value);
						}
						CCODClient.updateAgent(ccodPo);
					}

					String updatorId = SsoSessionUtils.getUserInfo(request).getUserId();
					String updatorName = SsoSessionUtils.getUserInfo(request).getUserName();
					userMongoService.updateUser(userInfos,updatorId,updatorName);
					//客服或者管理员修改需要刷新缓存
					if(UserType.SERVICE.value.equals(userType)||UserType.ADMINISTRATOR.value.equals(userType)){
						ParamUtils.refreshCache(CacheGroup.ENT_USER, userpo.getEntId());
					}
				    return ResultPo.success("修改成功", 1, null);
				}else{
					return ResultPo.failed(new Exception("签名错误"));
				}
			}catch(ServiceException e){
				e.printStackTrace();
				return ResultPo.failed(new Exception("异常," + e.getMessage()));
			}
		}	
}
