package com.channelsoft.ems.api.client;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONObject;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.HttpPostUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.CchatResponsePo;
import com.channelsoft.ems.user.po.DatEntUserPo;
/**
 * Cchat接口工具
 * @author wangyong
 *
 */
public class CchatClient {

	static String addAgentUrl = WebappConfigUtil.getParameter("Cchat_ROOT")+"/agent/add.do";
	static String skey = WebappConfigUtil.getParameter("Cchat_SKEY");
	/**
	 * 通知Cchat系统添加客服
	 * @param entId 企业编号
	 * @param agentId 用户ID
	 * @param loginName用户登陆帐号
	 * @param password用户登陆密码
	 * @return
	 */
	public static CchatResponsePo addAgent(String entId,String agentId,String loginName,String password){
		int code=3;
		CchatResponsePo po=new CchatResponsePo("3","添加失败",null);
		String data="";
		try {
			Map<String, String> param = new HashMap<String, String>();
			param.put("entId", entId);
			param.put("agentId", agentId);
			param.put("loginName", loginName);
			//密码约定跟工号一致
			param.put("password", agentId);
//			param.put("password", password);
			String signature=DigestUtils.md5Hex(entId + "_" + skey);
			param.put("signature", signature);

			SystemLogUtils.Debug(String.format("========准备调用Cchat添加坐席接口：=======,entId=%s,agentId=%s,password=%s,signature=%s",entId, agentId,password,signature));
			data = HttpPostUtils.httpPost(addAgentUrl, param);
			SystemLogUtils.Debug(String.format("========调用Cchat添加坐席接口：=======,entId=%s,agentId=%s,password=%s,signature=%s",entId, agentId,password,signature)+",返回:"+data);
			JSONObject json=new JSONObject(data);
			code=json.getInt("code");
			if(code==2){
				SystemLogUtils.Debug(String.format("========调用Cchat添加坐席接口：=======,entId=%s,agentId=%s,password=%s,signature=%s",entId, agentId,password,signature)+",该客服已存在");
			}
			else if(code!=0){
				SystemLogUtils.Debug(String.format("========调用Cchat添加坐席接口：=======,entId=%s,agentId=%s,password=%s,signature=%s",entId, agentId,password,signature)+",添加客服失败,code="+code+",msg="+json.getString("msg"));
				throw new ServiceException("调用Chat添加客服失败");
			}
			po.setCode(code+"");
			po.setMsg(json.getString("msg"));
			po.setObj(json.get("obj"));
			SystemLogUtils.Debug(String.format("========调用Cchat添加坐席接口：=======,entId=%s,agentId=%s,password=%s,signature=%s",entId, agentId,password,signature)+",添加成功");
		}
		catch(Exception e){
			e.printStackTrace();
			throw new ServiceException("调用Chat添加客服失败");
		}
		return po;
	}
	
	/**
	 * 修改客服
	 * @param entId
	 * @param agentId
	 * @param user
	 * @return
	 */
	public static CchatResponsePo updateAgent(String entId,String agentId,DatEntUserPo user){
		int code=3;
		CchatResponsePo po=new CchatResponsePo("3","修改失败",null);
	
		return po;
	}
	/**
	 * 删除客服
	 * @param entId
	 * @param agentId
	 * @return
	 */
	public static CchatResponsePo deleteAgent(String entId,String agentId){
		int code=3;
		CchatResponsePo po=new CchatResponsePo("3","删除失败",null);
	
		return po;
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
