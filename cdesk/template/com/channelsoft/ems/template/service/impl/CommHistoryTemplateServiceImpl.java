package com.channelsoft.ems.template.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;

import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.vo.PageInfo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.ent.service.IDatEntService;
import com.channelsoft.ems.field.constants.ComponentTypeEnum;
import com.channelsoft.ems.field.dao.IFieldDao;
import com.channelsoft.ems.field.dao.IFieldMongoDao;
import com.channelsoft.ems.field.po.BaseFieldPo;
import com.channelsoft.ems.template.constants.TemplateTypeEnum;
import com.channelsoft.ems.template.dao.ICommHistoryTemplateDao;
import com.channelsoft.ems.template.dao.ITempDao;

import com.channelsoft.ems.template.po.TemplatePo;
import com.channelsoft.ems.template.service.ICommHistoryTemplateService;

/**
 * 
 * @Description: 联络历史模版Service层实现类
 * @author chenglitao
 * @date 2016年5月26日 上午10:35:53
 *
 */
public class CommHistoryTemplateServiceImpl implements
		ICommHistoryTemplateService {

	@Autowired
	ICommHistoryTemplateDao commHistoryTemplateDao;

	@Autowired
	ITempDao tempDao;

	@Autowired
	IFieldDao fieldDao;

	@Autowired
	IFieldMongoDao fieldMongoDao;
	@Autowired
	IDatEntService entService;

	/* 查询联络历史模板 */
	@Override
	public List<TemplatePo> queryCommHistoryTemplates(String entId,
			String status, PageInfo page) throws ServiceException {
		TemplatePo po = new TemplatePo();
		po.setStatus(status);
		po.setTempType(TemplateTypeEnum.COMMHISTORY.value);
		return this.query(entId, po, page);
	}

	/* 按条件查询模板 */
	@Override
	public List<TemplatePo> query(String entId, TemplatePo po, PageInfo page)
			throws ServiceException {
		try {
			return commHistoryTemplateDao.query(entId, po, page);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException("查询出错");
		}
	}

	/* 添加联络历史模板 */
	@Override
	public int addCommHistoryTemp(TemplatePo po, String entId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			synchronized (TemplateServiceImpl.class) {
				if (StringUtils.isBlank(this.getTempId(entId))) {
					throw new ServiceException("模板编号为空");
				}

				po.setTempId(this.getTempId(entId));
				int num = commHistoryTemplateDao.addTemp(po, entId);

				if (num == 0) {
					/* 添加工单默认字段 */
					List<BaseFieldPo> list = new ArrayList<BaseFieldPo>();
					/* 标题 */
					BaseFieldPo workPo = new BaseFieldPo();
					workPo.setTempId(po.getTempId());
					workPo.setKey("title");
					workPo.setName("标题");
					workPo.setComponentType(ComponentTypeEnum.TEXT.value);
					workPo.setSortNum(this.getKey(entId).substring(5));
					list.add(workPo);

					/* 问题描述 */
					BaseFieldPo workPo1 = new BaseFieldPo();
					workPo1.setTempId(po.getTempId());
					workPo1.setKey("content");
					workPo1.setName("问题描述");
					workPo1.setComponentType(ComponentTypeEnum.TEXT_AREA.value);
					workPo1.setSortNum(this.getKey(entId).substring(5));
					list.add(workPo1);

					/* 受理客服组 */
					BaseFieldPo workPo2 = new BaseFieldPo();
					workPo2.setTempId(po.getTempId());
					workPo2.setKey("serviceGroupName");
					workPo2.setName("受理客服组");
					workPo2.setComponentType(ComponentTypeEnum.SELECT.value);
					workPo2.setSortNum(this.getKey(entId).substring(5));
					list.add(workPo2);

					/* 受理客服 */
					BaseFieldPo workPo3 = new BaseFieldPo();
					workPo3.setTempId(po.getTempId());
					workPo3.setKey("customServiceName");
					workPo3.setName("受理客服");
					workPo3.setComponentType(ComponentTypeEnum.SELECT.value);
					workPo3.setSortNum(this.getKey(entId).substring(5));
					list.add(workPo3);

					/* 状态 */
					BaseFieldPo workPo4 = new BaseFieldPo();
					workPo4.setTempId(po.getTempId());
					workPo4.setKey("status");
					workPo4.setName("工单状态");
					workPo4.setComponentType(ComponentTypeEnum.SELECT.value);
					workPo4.setSortNum(this.getKey(entId).substring(5));
					list.add(workPo4);

					/* 类型 */
					BaseFieldPo workPo5 = new BaseFieldPo();
					workPo5.setTempId(po.getTempId());
					workPo5.setKey("type");
					workPo5.setName("类型");
					workPo5.setComponentType(ComponentTypeEnum.SELECT.value);
					workPo5.setCandidateValue(new String[] { "问题", "事务", "故障",
							"任务" });
					workPo5.setSortNum(this.getKey(entId).substring(5));
					list.add(workPo5);

					/* 优先级 */
					BaseFieldPo workPo6 = new BaseFieldPo();
					workPo6.setTempId(po.getTempId());
					workPo6.setKey("priority");
					workPo6.setName("优先级");
					workPo6.setComponentType(ComponentTypeEnum.SELECT.value);
					workPo6.setCandidateValue(new String[] { "低", "中", "高",
							"紧急" });
					workPo6.setSortNum(this.getKey(entId).substring(5));
					list.add(workPo6);

					commHistoryTemplateDao.addDefaultField(list, entId);
				}
				return num;
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("添加工单自定义字段出错");
		}
	}

	/* 获取联络历史模板Id */
	@Override
	public String getTempId(String entId) throws ServiceException {
		String num = tempDao.getWorkTemplateId();
		return num;
	}

	/* 获取字段Id */
	@Override
	public String getKey(String entId) throws ServiceException {
		String num = fieldDao.getKey();
		if (Integer.valueOf(num) < 10) {
			num = "0" + num;
		}
		return "field" + num;
	}

	/* 删除模板（逻辑删除） */
	@Override
	public int deleteTemplate(String entId, String key) throws ServiceException {
		try {
			return commHistoryTemplateDao.deleteTemplate(entId, key);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/* 模板启停用 */
	@Override
	public int changeMode(String entId, String type, String key)
			throws ServiceException {
		try {
			return commHistoryTemplateDao.changeMode(entId, type, key);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/* 模板排序 */
	@Override
	public int sortTemplate(String entId, String ids) throws ServiceException {
		String[] sortKey = ids.trim().split(",");
		try {
			int[] sortNums = commHistoryTemplateDao.getSortNums(entId);
			return commHistoryTemplateDao
					.sortTemplate(entId, sortKey, sortNums);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/* 按模板Id删除字段（逻辑删除） */
	@Override
	public int deleteFieldByTempId(String entId, String key)
			throws ServiceException {
		try {
			return commHistoryTemplateDao.deleteFieldByTempId(entId, key);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/* 按模板Id查询字段 */
	@Override
	public List<BaseFieldPo> queryFieldsByTempId(String entId, String tempId,
			PageInfo page) throws ServiceException {
		BaseFieldPo po = new BaseFieldPo();
		po.setTempId(tempId);

		return this.queryFields(entId, po, page);
	}

	/* 按条件查询字段 */
	@Override
	public List<BaseFieldPo> queryFields(String entId, BaseFieldPo po,
			PageInfo page) throws ServiceException {
		try {
			return commHistoryTemplateDao.queryFields(entId, po, page);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException("查询出错");
		}
	}

	/* 添加自定义字段 */
	@Override
	public int addDefinedField(BaseFieldPo po, String entId)
			throws ServiceException {
		// TODO Auto-generated method stub
		try {
			synchronized (TemplateServiceImpl.class) {
				po.setKey(this.getKey(entId));
				return fieldMongoDao.add(po, entId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("添加自定义字段出错");
		}
	}

	/* 编辑模板信息（标题） */
	@Override
	public int goEdit(String entId, TemplatePo po) throws ServiceException {
		try {
			return commHistoryTemplateDao.goEdit(entId, po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}

	/* 企业开户调用接口，根据企业编号生成该企业的默认模板（包括模板数据和字段数据） */
	@Override
	public int addDefaultCommHistoryTemp(String entId) throws ServiceException {
		try {
			synchronized (TemplateServiceImpl.class) {
				if (StringUtils.isBlank(this.getTempId(entId))) {
					throw new ServiceException("模板编号为空");
				}

				TemplatePo po1 = new TemplatePo();
				po1.setTempId(this.getTempId(entId));
				po1.setTempName("手动新增联络历史");
				/* 添加联络历史默认模板 */
				addDefaultTemplate(entId, po1);

				TemplatePo po2 = new TemplatePo();
				po2.setTempId(this.getTempId(entId));
				po2.setTempName("带系统信息联络历史-电话");
				addDefaultTemplate(entId, po2);

				TemplatePo po3 = new TemplatePo();
				po3.setTempId(this.getTempId(entId));
				po3.setTempName("带系统信息联络历史-chat");
				return addDefaultTemplate(entId, po3);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("添加工单自定义字段出错");
		}
	}

	public int addDefaultTemplate(String entId, TemplatePo po)
			throws ServiceException {
		int num = commHistoryTemplateDao.addDefaultTemp(po, entId);
		try {

			if (num == 0) {
				/* 添加联络历史默认字段 */
				List<BaseFieldPo> list = new ArrayList<BaseFieldPo>();
				/* 客户姓名 */
				BaseFieldPo workPo = new BaseFieldPo();
				workPo.setTempId(po.getTempId());
				workPo.setKey("custName");
				workPo.setName("客户姓名");
				workPo.setComponentType(ComponentTypeEnum.TEXT.value);
				workPo.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo);

				/* 关联工单 */
				BaseFieldPo workPo1 = new BaseFieldPo();
				workPo1.setTempId(po.getTempId());
				workPo1.setKey("workOrder");
				workPo1.setName("关联工单");
				workPo1.setComponentType(ComponentTypeEnum.TEXT.value);
				workPo1.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo1);

				/* 业务类型 */
				BaseFieldPo workPo2 = new BaseFieldPo();
				workPo2.setTempId(po.getTempId());
				workPo2.setKey("businesstype");
				workPo2.setName("业务类型");
				workPo2.setComponentType(ComponentTypeEnum.SELECT.value);
				workPo2.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo2);

				/* 联络方式 */
				BaseFieldPo workPo3 = new BaseFieldPo();
				workPo3.setTempId(po.getTempId());
				workPo3.setKey("contactWay");
				workPo3.setName("联络方式");
				workPo3.setComponentType(ComponentTypeEnum.SELECT.value);
				workPo3.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo3);

				/* 来源 */
				BaseFieldPo workPo4 = new BaseFieldPo();
				workPo4.setTempId(po.getTempId());
				workPo4.setKey("source");
				workPo4.setName("来源");
				workPo4.setComponentType(ComponentTypeEnum.SELECT.value);
				workPo4.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo4);

				/* 类型 */
				BaseFieldPo workPo5 = new BaseFieldPo();
				workPo5.setTempId(po.getTempId());
				workPo5.setKey("type");
				workPo5.setName("类型");
				workPo5.setComponentType(ComponentTypeEnum.SELECT.value);
				workPo5.setCandidateValue(new String[] { "问题", "事务", "故障", "任务" });
				workPo5.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo5);

				/* 联络类型 */
				BaseFieldPo workPo6 = new BaseFieldPo();
				workPo6.setTempId(po.getTempId());
				workPo6.setKey("contactType");
				workPo6.setName("联络类型");
				workPo6.setComponentType(ComponentTypeEnum.SELECT.value);
				workPo6.setCandidateValue(new String[] { "低", "中", "高", "紧急" });
				workPo6.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo6);
				
				/* 描述 */
				BaseFieldPo workPo7 = new BaseFieldPo();
				workPo7.setTempId(po.getTempId());
				workPo7.setKey("desc");
				workPo7.setName("描述");
				workPo7.setComponentType(ComponentTypeEnum.TEXT.value);
				workPo7.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo7);
				
				
				
				/* 处理结果 */
				BaseFieldPo workPo8 = new BaseFieldPo();
				workPo8.setTempId(po.getTempId());
				workPo8.setKey("result");
				workPo8.setName("处理结果");
				workPo8.setComponentType(ComponentTypeEnum.TEXT.value);
				workPo8.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo8);
				
				
				/* 标签 */
				BaseFieldPo workPo9 = new BaseFieldPo();
				workPo9.setTempId(po.getTempId());
				workPo9.setKey("label");
				workPo9.setName("标签");
				workPo9.setComponentType(ComponentTypeEnum.TEXT.value);
				workPo9.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo9);
				
				/* 小结 */
				BaseFieldPo workPo10 = new BaseFieldPo();
				workPo10.setTempId(po.getTempId());
				workPo10.setKey("summary");
				workPo10.setName("小结");
				workPo10.setComponentType(ComponentTypeEnum.TEXT_AREA.value);
				workPo10.setSortNum(this.getKey(entId).substring(5));
				list.add(workPo10);

				commHistoryTemplateDao.addDefaultField(list, entId);

			}

			return num;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("添加联络历史自定义字段出错");
		}
	}

	@Override
	public Map<String, Map<String, List<TemplatePo>>> queryAll()
			throws ServiceException {
		// TODO Auto-generated method stub
		List<DatEntInfoPo> entList = entService.queryAll();
		Map<String, Map<String, List<TemplatePo>>> map = new HashMap<String, Map<String, List<TemplatePo>>>();
		TemplatePo po = new TemplatePo();
		for (DatEntInfoPo ent : entList) {
			String entId = ent.getEntId();
			List<TemplatePo> list = this.query(entId, po, null);
			Map<String, List<TemplatePo>> typeMap = new HashMap<String, List<TemplatePo>>();
			for (TemplatePo t : list) {
				List<TemplatePo> typeList = typeMap.get(t.getTempType());
				if (typeList == null || typeList.size() == 0) {
					typeList = new ArrayList<TemplatePo>();
				}
				typeList.add(t);
				typeMap.put(t.getTempType(), typeList);
			}
			map.put(entId, typeMap);
		}
		return map;
	}
}
