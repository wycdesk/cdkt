package com.channelsoft.ems.api.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.tuckey.web.filters.urlrewrite.utils.StringUtils;

import com.channelsoft.cri.logger.SystemLogUtils;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.po.CfgEntWxSimplePo;
import com.channelsoft.ems.api.po.ResultPo;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.ent.po.CfgEntWxPo;
import com.channelsoft.ems.ent.service.ICfgEntWxService;
import com.channelsoft.ems.ent.service.IDatEntService;

@Controller
@RequestMapping("/wxApi")
public class EntWxApiController {
	
	@Autowired
	ICfgEntWxService entWxService;
	@Autowired
	IDatEntService entService;

    @RequestMapping(value="query")
    @ResponseBody
    public ResultPo query(String entId, String signature, HttpServletRequest request) {
    	SystemLogUtils.Debug(String.format("查询企业微信号: entId=%s, signature=%s", entId, signature));
    	// 验证参数不为空
    	if (StringUtils.isBlank(entId)) {
    		return ResultPo.failed(new Exception("企业Id为空"));
    	}
    	if (StringUtils.isBlank(signature)) {
    		return ResultPo.failed(new Exception("签名为空"));
    	}
    	// 验证签名
    	String skey = WebappConfigUtil.getParameter("ENT_WX_SKEY");
    	String signComp = DigestUtils.md5Hex(entId + "_" + skey);
    	if (!signComp.equals(signature)) {
    		return ResultPo.failed(new Exception("签名错误"));
    	}
//    	boolean isExist=entService.existThisEntId(entId);
    	boolean isExist=ParamUtils.getEntInfo(entId)==null?false:true;
    	if (!isExist) {
    		return ResultPo.failed(new Exception("企业不存在"));
    	}
    	
		try {
			List<CfgEntWxPo> list = entWxService.queryByEntId(entId);
			List<CfgEntWxSimplePo> simpleList = new ArrayList<CfgEntWxSimplePo>();
			for (CfgEntWxPo po : list) {
				CfgEntWxSimplePo simplePo = new CfgEntWxSimplePo();
				BeanUtils.copyProperties(po, simplePo);
				simpleList.add(simplePo);
			}
			return ResultPo.success("查询成功", simpleList.size(), simpleList);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ResultPo.failed(new Exception("系统异常"));
    }
}
