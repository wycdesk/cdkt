package com.channelsoft.ems.department.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.AjaxResultPo;
import com.channelsoft.ems.department.constans.DepartmentStatusEnum;
import com.channelsoft.ems.department.po.DepartmentPo;
import com.channelsoft.ems.department.po.JobPo;
import com.channelsoft.ems.department.service.IDepartmentService;
import com.channelsoft.ems.department.vo.DepartmentVo;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;


@Controller
@RequestMapping("/department")
public class DepartmentController {

	@Autowired
	IDepartmentService departmentService; 
	
	@Autowired
	IUserMongoService userService;
	
	/*部门管理首页*/
	@RequestMapping(value = "/index")
	public String departmentList(HttpServletRequest request, HttpServletResponse response, Model model){
	    return "department/index";
	 }
	
	/*查询部门和岗位共同组成树状链表*/
	@RequestMapping(value = "/query")
	@ResponseBody
	public AjaxResultPo query(HttpServletRequest request) {
		AjaxResultPo res = new AjaxResultPo(true,"查询成功");
		try {
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();
			
			List<DepartmentVo> list = departmentService.query(enterpriseid);			
			res.setRows(list);
			res.setTotal(list.size());
			return res;
		} catch (ServiceException e) {
			return new AjaxResultPo(false,e.getMessage());
		}
	}
		
	/*修改部门*/
	@ResponseBody
	@RequestMapping("/update")
	public AjaxResultPo update(DepartmentPo po, HttpServletRequest request) {
		try {
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();				
			/*判断部门名称是否存在*/
			DepartmentPo dptPo = new DepartmentPo();
			dptPo.setEntId(enterpriseid);
			dptPo.setStatus(DepartmentStatusEnum.NORMAL.value);
			dptPo.setName(po.getName());
			List<DepartmentVo> list = departmentService.query(dptPo);
			
			if(list.size()>0){
				if(!po.getId().equals(list.get(0).getId())){
					return AjaxResultPo.failed(new Exception("部门名称已存在!"));
				}else{
					/*赋值level*/
					DepartmentPo dptPo1 = new DepartmentPo();
					dptPo1.setEntId(enterpriseid);
					if(StringUtils.isNotBlank(po.getParentId())){
						dptPo1.setId(po.getParentId());
					}				
					List<DepartmentVo> list1 = departmentService.query(dptPo1);				
					String level = "";
					if(list1.size()==1){
						level = Integer.toString(Integer.parseInt(list1.get(0).getLevel())+1);
					}	
					po.setLevel(level);
					
					po.setEntId(enterpriseid);
					departmentService.update(po);	
					return AjaxResultPo.successDefault();
				}				
			}else{
				po.setEntId(enterpriseid);
				departmentService.update(po);	
				return AjaxResultPo.successDefault();
			}
		} catch (ServiceException e) {			
			e.printStackTrace();			
			return new AjaxResultPo(false, e.getMessage());
		}
	}
	
	/*删除部门（逻辑删除）*/
	@ResponseBody
	@RequestMapping("/delete")
	public AjaxResultPo delete(String id,HttpServletRequest request) {
		String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();
		try {
			/*部门是否包含有用户*/
			BasicDBObject dbo = new BasicDBObject();
			dbo.put("entId", enterpriseid);
			dbo.put("dptId", id);			
			List<DBObject> list = userService.queryUserList(dbo, null);
			
			if(list.size()>0){				
				return AjaxResultPo.failed(new Exception("此部门有用户存在，无法删除!"));
			}else{			
				departmentService.delete(enterpriseid, id);
				return AjaxResultPo.successDefault();
			}
		} catch (ServiceException e) {			
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		} 
	}
	
	/*添加部门*/
	@ResponseBody
	@RequestMapping("/add")
	public AjaxResultPo add(DepartmentPo po, HttpServletRequest request) {
		try {
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();						
			
			/*判断部门名称是否存在*/
			DepartmentPo dptPo = new DepartmentPo();
			dptPo.setEntId(enterpriseid);
			dptPo.setStatus(DepartmentStatusEnum.NORMAL.value);
			dptPo.setName(po.getName());
			List<DepartmentVo> list = departmentService.query(dptPo);
			
			if(list.size()>0){					
				return AjaxResultPo.failed(new Exception("部门名称已存在!"));
			}else{				
				/*生成部门编号*/
				String id = "";
				if(StringUtils.isNotBlank(po.getParentId())){
					id = departmentService.getDepartmentId(enterpriseid, po.getParentId());
				}
											
				/*赋值level*/
				DepartmentPo dptPo1 = new DepartmentPo();
				dptPo1.setEntId(enterpriseid);
				if(StringUtils.isNotBlank(po.getParentId())){
					dptPo1.setId(po.getParentId());
				}				
				List<DepartmentVo> list1 = departmentService.query(dptPo1);				
				String level = "";
				if(list1.size()==1){
					level = Integer.toString(Integer.parseInt(list1.get(0).getLevel())+1);
				}				
				
				po.setId(id);
				po.setEntId(enterpriseid);
				po.setLevel(level);
				departmentService.add(po);
				
				return AjaxResultPo.successDefault();
			}			
		} catch (ServiceException e) {
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		} 
	}
	
	/*删除岗位（逻辑删除）*/
	@ResponseBody
	@RequestMapping("/deleteJob")
	public AjaxResultPo deleteJob(String id,HttpServletRequest request) {
		String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();
		try {		
			/*岗位是否包含有用户*/
			BasicDBObject dbo = new BasicDBObject();
			dbo.put("entId", enterpriseid);
			dbo.put("jobId", id);			
			List<DBObject> list = userService.queryUserList(dbo, null);
			
			if(list.size()>0){				
				return AjaxResultPo.failed(new Exception("此岗位有用户存在，无法删除!"));
			}else{			
				departmentService.deleteJobById(enterpriseid, id);
				return AjaxResultPo.successDefault();
			}
		} catch (ServiceException e) {			
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		} 
	}
	
	/*修改岗位*/
	@ResponseBody
	@RequestMapping("/updateJob")
	public AjaxResultPo updateJob(JobPo po, HttpServletRequest request) {
		try {		
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();						
			/*赋值level*/
			JobPo job = new JobPo();
			job.setEntId(enterpriseid);
			job.setId(po.getParentId());
			List<DepartmentVo> list = departmentService.queryJobById(job);
			
			po.setEntId(enterpriseid);
			if(list.size()==1){
				po.setLevel(Integer.toString((Integer.parseInt(list.get(0).getLevel())+1)));
			}			
			departmentService.updateJob(po);	
			return AjaxResultPo.successDefault();			
		} catch (ServiceException e) {					
			e.printStackTrace();			
			return new AjaxResultPo(false, e.getMessage());
		}
	}
	
	/*添加岗位*/
	@ResponseBody
	@RequestMapping("/addJob")
	public AjaxResultPo addJob(JobPo po, HttpServletRequest request) {
		try {
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();						
			
			/*生成岗位编号*/
			String id = "";
			if(StringUtils.isNotBlank(po.getParentId())){
				id = departmentService.getJobId(enterpriseid, po.getParentId());
			}
		    		
			/*赋值level*/
		    JobPo po1 = new JobPo();
		    po1.setEntId(enterpriseid);
		    if(StringUtils.isNotBlank(po.getParentId())){
		    	po1.setId(po.getParentId());
		    }		    
			List<DepartmentVo> list = departmentService.queryJobById(po1);
			String level = "";
			if(list.size()==1){
				level = Integer.toString(Integer.parseInt(list.get(0).getLevel())+1);
			}
			
			po.setId(id);
			po.setEntId(enterpriseid);
			po.setLevel(level);			
			departmentService.addJob(po);
				
			return AjaxResultPo.successDefault();		
		} catch (ServiceException e) {
			e.printStackTrace();
			return new AjaxResultPo(false, e.getMessage());
		} 
	}
	
	/*按部门Id查询部门*/
	@RequestMapping(value = "/queryDptById")
	@ResponseBody
	public AjaxResultPo queryDptById(String id, HttpServletRequest request) {
		AjaxResultPo res = new AjaxResultPo(true,"查询成功");
		try {
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();

			DepartmentPo po = new DepartmentPo();
			po.setEntId(enterpriseid);
			po.setId(id);
			List<DepartmentVo> list = departmentService.query(po);			
			res.setRows(list);
			res.setTotal(list.size());
			return res;
		} catch (ServiceException e) {
			return new AjaxResultPo(false,e.getMessage());
		}
	}
	
	/*按岗位Id查询岗位*/
	@RequestMapping(value = "/queryJobById")
	@ResponseBody
	public AjaxResultPo queryJobById(String id, HttpServletRequest request) {
		AjaxResultPo res = new AjaxResultPo(true,"查询成功");
		try {
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();

			JobPo po = new JobPo();
			po.setEntId(enterpriseid);
			po.setId(id);
			List<DepartmentVo> list = departmentService.queryJobById(po);			
			res.setRows(list);
			res.setTotal(list.size());
			return res;
		} catch (ServiceException e) {
			return new AjaxResultPo(false,e.getMessage());
		}
	}
		
	/*查询所有部门并组成树状链表*/
	@RequestMapping(value = "/queryAllDpt")
	@ResponseBody
	public AjaxResultPo queryAllDpt(HttpServletRequest request) {
		AjaxResultPo res = new AjaxResultPo(true,"查询成功");
		try {
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();

			DepartmentPo po = new DepartmentPo();
			po.setEntId(enterpriseid);
			List<DepartmentVo> list = departmentService.queryAllDpt(enterpriseid);		
			res.setRows(list);
			res.setTotal(list.size());
			return res;
		} catch (ServiceException e) {
			return new AjaxResultPo(false,e.getMessage());
		}
	}
	
	/*查询所有岗位并组成树状链表*/
	@RequestMapping(value = "/queryAllJob")
	@ResponseBody
	public AjaxResultPo queryAllJob(HttpServletRequest request) {
		AjaxResultPo res = new AjaxResultPo(true,"查询成功");
		try {
			String enterpriseid = SsoSessionUtils.getEntInfo(request).getEntId();

			JobPo po = new JobPo();
			po.setEntId(enterpriseid);
			List<DepartmentVo> list = departmentService.queryAllJob(enterpriseid);	
			res.setRows(list);
			res.setTotal(list.size());
			return res;
		} catch (ServiceException e) {
			return new AjaxResultPo(false,e.getMessage());
		}
	}	
}
