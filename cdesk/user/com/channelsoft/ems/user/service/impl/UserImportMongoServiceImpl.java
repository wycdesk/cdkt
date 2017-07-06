package com.channelsoft.ems.user.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.logger.ManageLogUtils;
import com.channelsoft.cri.util.CdeskEncrptDes;
import com.channelsoft.cri.util.EMailCheckUtil;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.api.client.CCODClient;
import com.channelsoft.ems.api.constants.AgentRoleType;
import com.channelsoft.ems.api.po.CCODRequestPo;
import com.channelsoft.ems.ent.po.DatEntInfoPo;
import com.channelsoft.ems.field.constants.UserFieldStatusEnum;
import com.channelsoft.ems.field.po.UserDefinedFiedPo;
import com.channelsoft.ems.field.service.IUserFieldService;
import com.channelsoft.ems.log.constants.BusinessTypeEnum;
import com.channelsoft.ems.log.constants.LogTypeEnum;
import com.channelsoft.ems.log.service.ILogMongoService;
import com.channelsoft.ems.redis.constant.CacheGroup;
import com.channelsoft.ems.redis.util.ParamUtils;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.dao.IUserMongoDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserImportMongoService;
import com.channelsoft.ems.user.service.IUserMongoService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.vo.DatEntUserVo;
import com.channelsoft.ems.user.vo.UserImportResultVo;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import jxl.Workbook;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;

public class UserImportMongoServiceImpl implements IUserImportMongoService {

	@Autowired
	IUserDao userDao;
	@Autowired
	IUserMongoService userMongoService;
	@Autowired
	IUserMongoDao userMongoDao;
	@Autowired
	IUserService userService;
	@Autowired
	IUserFieldService userFieldService;
	@Autowired
	ILogMongoService logMongoService;
	
	@Override
	public UserImportResultVo upload(HttpServletRequest request, String addUserFlag, String updateUserFlag) throws ServiceException {
		// TODO Auto-generated method stub
		UserImportResultVo resultVo = new UserImportResultVo();
		String path=request.getSession().getServletContext().getRealPath("/");
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		MultipartHttpServletRequest multipartHttpservletRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartHttpservletRequest.getFile("fileImport");
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase();
		
		if(!("csv".equals(ext))){
			throw new ServiceException("请选择CSV格式文件，或文件格式不正确！");
		}
		
		String datePath =DateConstants.DATE_FORMAT_NUM_SHORT().format(new Date());
		path=path+WebappConfigUtil.getParameter("userImportURL").replace("entId", user.getEntId())+datePath;
		
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		String fileName= user.getEntId()+"_"+DateConstants.DATE_FORMAT_NUM().format(new Date())+ new Random().nextInt(10)+"."+ext;
		String destPath = path+"/"+fileName;
		File destFile = new File(destPath);
		try {
			file.transferTo(destFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new ServiceException("上传文件失败");
		}
		//获取文件编码
		String code = "GBK";//文件编码方式默认为GBK
		try {
			code = getFileEncode(destPath);
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("获取文件编码失败");
		}
//		logger.info("用户批量导入，上传文件的编码方式为："+code);
		List<DatEntUserVo> list = null;
		try{
			list = parseTxt(destPath,user,code);
		}catch(ServiceException e){
			throw new ServiceException(e.getMessage());
		}
		
		resultVo.setTotalNum(list.size()+"");//总记录条数
		int successNum = 0;
		int updateNum = 0;
		int failNum = 0;
		List<String> failList = new ArrayList<String>();//失败记录列表
		if(list !=null || list.size()!= 0){
			for(DatEntUserVo vo : list){
				DatEntUserPo po = new DatEntUserPo();
				BeanUtils.copyProperties(vo, po);
				String s = register(request, po,updateUserFlag);
				if("success".equals(s)){//添加成功
					successNum++;
				}else if("update".equals(s)){//更新成功
					updateNum++;
				}else{//失败
					failNum++;
					s="导入错误：第 "+vo.getRowNum()+" 行："+s;
					failList.add(s);
				}
			}
		}
		
		resultVo.setImportSuccessNum(successNum+"");
		resultVo.setUpdateSuccessNum(updateNum+"");
		resultVo.setImportFailNum(failNum+"");
		resultVo.setFailList(failList);
		return resultVo;
	}

	@Override
	public int userImport(DatEntUserPo po) throws ServiceException {
		// TODO Auto-generated method stub
		try {
			po.setUserId(userDao.getUserId());
			String userName=po.getLoginName();
			if(StringUtils.isNotBlank(userName)&&userName.indexOf("@")>0){
				userName=userName.substring(0, userName.indexOf("@"));
				if(StringUtils.isBlank(po.getNickName()))po.setNickName(userName);
				if(StringUtils.isBlank(po.getUserName())) po.setUserName(po.getNickName());
			}
			return userMongoDao.add(po, po.getEntId());
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}

	/**
	 * 解析csv文件
	 * @param path
	 * @param user
	 * @param code 文件编码方式
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年11月20日下午5:41:16
	 */
	@SuppressWarnings("resource")
	private List<DatEntUserVo> parseTxt(String path, SsoUserVo user, String code) throws ServiceException {
		List<DatEntUserVo> list = new ArrayList<DatEntUserVo>();
		try{
			if (path.endsWith("csv")) {
				InputStreamReader read = new InputStreamReader(new FileInputStream(path), code);
				BufferedReader readBuf = new BufferedReader(read);
				String s = "";
				
				s = readBuf.readLine();//读第0行
				if(StringUtils.isBlank(s)){//第一行为空
					throw new ServiceException("文件错误");
				}
				//获取列名对应的列编号
				int usernameNum = -1;//邮箱对应列编号
				int nameNum = -1;//用户昵称对应列编号
				int roleNum = -1;//用户角色和用户类型对应列编号
				int phoneNum = -1;//用户电话对应列编号
				int detailsNum = -1;//用户详情对应列编号
				int signaNum = -1;//用户签名对应列编号
				int notesNum = -1;//用户备注对应列编号
				String[] ts0 = s.split(",");
				for(int si=0;si<ts0.length;si++){
					/*System.out.println(ts0[si]);
					System.out.println(ts0[si].substring(1));
					System.out.println(ts0[si].charAt(0));
					System.out.println((int)ts0[si].charAt(0));
					char a = (char)65279;*/
					//utf8格式的文件第一个字符ASCII码为65279
					if((int)ts0[si].charAt(0)==65279){
						ts0[si] = ts0[si].substring(1);
					}
					
					if("username".equals(ts0[si])){
						usernameNum = si;
						continue;
					}/*else if("username".equals(ts0[si]) && "UTF-8".equals(code)){
						usernameNum = si;
						continue;
					}*/else if("name".equals(ts0[si])){
						nameNum = si;
						continue;
					}else if("role".equals(ts0[si])){
						roleNum = si;
						continue;
					}else if("phone".equals(ts0[si])){
						phoneNum = si;
						continue;
					}else if("details".equals(ts0[si])){
						detailsNum = si;
						continue;
					}else if("signature".equals(ts0[si])){
						signaNum = si;
						continue;
					}else if("notes".equals(ts0[si])){
						notesNum = si;
						continue;
					}
				}
				
				if(usernameNum<0){//username列未填写，则不能导入
					throw new ServiceException("必须包括username列");
				}
				
				int i = 1;
				while ((s = readBuf.readLine()) != null) {
//					System.out.println(s);
					if(StringUtils.isBlank(s)){
						break;
					}
					String[] ts = s.split(",",-1);
					DatEntUserVo po = new DatEntUserVo();
					po.setRowNum(i);
					po.setEntId(user.getEntId());//企业编号
					po.setEntName(user.getEntName());//
					if(roleNum>=0&&roleNum<ts.length){//导入文件中包含role列
						if("agent".equals(ts[roleNum])){//客服人员
							po.setUserType(UserType.SERVICE.value);
							po.setRoleId("4");//客户人员默认角色为：低权限客服
//							po.setRoleId(UserType.SERVICE.value);
						}else if("admin".equals(ts[roleNum])){//管理员
							po.setUserType(UserType.ADMINISTRATOR.value);
							po.setRoleId(UserType.ADMINISTRATOR.value);
						}else{//普通用户
							po.setUserType(UserType.CUSTOMER.value);
							po.setRoleId(UserType.CUSTOMER.value);
						}
					}else{//导入文件中包含role列,用户类型和角色为默认值
						po.setUserType(UserType.CUSTOMER.value);
						po.setRoleId(UserType.CUSTOMER.value);
					}
					po.setLoginType(LoginType.MAILBOX.value);//登录账号类型；默认为邮箱
					po.setLoginName(ts[usernameNum]);//登录名为邮箱;解析文件时未验证邮箱格式
					if(nameNum>=0&&nameNum<ts.length){//导入文件中包含name列
						po.setNickName(ts[nameNum]);//用户昵称
					}
					po.setEmail(ts[usernameNum]);
					if(phoneNum>=0&&phoneNum<ts.length){//导入文件中包含phone列
						po.setTelPhone(ts[phoneNum]);
						po.setContactPhone(ts[phoneNum]);
					}
					po.setCreatorId(user.getLoginName());
					po.setCreatorName(user.getNickName());
					po.setUpdatorId(user.getLoginName());
					po.setUpdatorName(user.getNickName());
					po.setUserStatus(UserStatus.FORACTIVE.value);//
					if(detailsNum>=0&&detailsNum<ts.length){//导入文件中包含details列
						po.setUserDesc(ts[detailsNum]);//用户说明
					}
					if(signaNum>=0 && signaNum<ts.length){//导入文件中包含signature列
						po.setSignature(ts[signaNum]);
					}
					if(notesNum>=0 && notesNum<ts.length){
						po.setRemark(ts[notesNum]);
					}
					
					list.add(po);
					i++;
					continue;
				}
				
				readBuf.close();
				read.close();
				
			}
			return list;
		}catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("解析文件失败");
		}
		
	}
	
	/**
	 * 获取文件的编码方式
	 * @param filePath 文件路径
	 * @return
	 * @throws Exception
	 * @author wangjie
	 * @time 2015年11月21日上午11:24:58
	 */
	@SuppressWarnings("resource")
	private String getFileEncode(String filePath) throws Exception{
		BufferedInputStream bin = new BufferedInputStream(new FileInputStream(filePath));
		int p = (bin.read() << 8) + bin.read();
		String code = null;
		//其中的 0xefbb、0xfffe、0xfeff、0x5c75这些都是这个文件的前面两个字节的16进制数
		switch (p) {
		case 0xefbb:
			code = "UTF-8";
			break;
		case 0xfffe:
			code = "Unicode";
			break;
		case 0xfeff:
			code = "UTF-16BE";
			break;
		case 0x5c75:
			code = "ANSI|ASCII";
			break;
		default:
			code = "GBK";
		}
		return code;
	}
	
	/**
	 * 用户添加或更新
	 * 返回success添加成功,添加成功后发送邮件
	 * 返回update更新成功
	 * 失败返回错误信息
	 * @param po
	 * @param updateUserFlag
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2015年11月21日下午2:19:25
	 */
	private String register(HttpServletRequest request, DatEntUserPo po, String updateUserFlag) throws ServiceException {
		if("".equals(po.getEmail()) || po.getEmail() == null){
			return "邮箱为空";
		}
		if(!EMailCheckUtil.check(po.getEmail())){//邮箱格式错误
			return "邮箱："+po.getEmail()+"格式错误";
		}
		boolean e = userMongoService.existsEmails(po.getEntId(), po.getEmail());
		if(e&&"1".equals(updateUserFlag)){//邮箱已被注册，需要更新
			//执行更新
			if(!UserType.CUSTOMER.value.equals(po.getRoleId())){//不是普通用户电话号码不能为空
				if(StringUtils.isBlank(po.getTelPhone())){
					return po.getLoginName()+"电话为空";
				}
			}
			if(StringUtils.isNotBlank(po.getTelPhone())){//电话号码不为空时，验证电话号码是否重复
				if(userMongoService.existsPhone(po.getEntId(), po.getTelPhone())){
					return "电话："+po.getTelPhone()+"已被占用";
				}
			}
			String time = DateConstants.DATE_FORMAT().format(new Date());
			DBObject dbo = new BasicDBObject();
			dbo.put("updateTime", time);
			dbo.put("userType", po.getUserType());
			dbo.put("roleId", po.getRoleId());
			dbo.put("nickName", po.getNickName());
			dbo.put("telPhone", po.getTelPhone());
			dbo.put("updatorId", po.getUpdatorId());
			dbo.put("updatorName", po.getUpdatorName());
			dbo.put("userDesc", po.getUserDesc());
			dbo.put("remark", po.getRemark());
			dbo.put("signature", po.getSignature());
			userMongoDao.updateUserByEmail(dbo, po.getEntId(), po.getEmail());
			
			return "update";
		}
		if(e&&"0".equals(updateUserFlag)){//邮箱已被注册，不更新
			return "邮箱："+po.getEmail()+"已被注册";
		}
		if(e&&"".equals(updateUserFlag)){//邮箱已被注册，不更新
			return "邮箱："+po.getEmail()+"已被注册";
		}
		if(!e){//邮箱未注册
			//执行添加用户
			if(!UserType.CUSTOMER.value.equals(po.getRoleId())){//不是普通用户电话号码不能为空
				if(StringUtils.isBlank(po.getTelPhone())){
					return po.getLoginName()+"电话为空";
				}
			}
			if(po.getTelPhone()!=null){//电话号码不为空时，验证电话号码是否重复
				if(userMongoService.existsPhone(po.getEntId(), po.getTelPhone())){
					return "电话："+po.getTelPhone()+"已被占用";
				}
			}
			
			String activeCode=MD5Util.MD5(po.getEmail()+new Date().getTime());
			po.setActiveCode(activeCode);
//			userService.registerBase(po);
			this.userImport(po);
			try {
				userService.sendMail(request,po,activeCode,false);
			} catch (Exception e1) {
				e1.printStackTrace();
				return po.getEmail()+",邮件发送失败";
			}
			
			return "success";
		}
		return "导入失败";
	}

	@Override
	public UserImportResultVo uploadExcel(HttpServletRequest request, String addUserFlag, String updateUserFlag)
			throws ServiceException {
		// TODO Auto-generated method stub
		UserImportResultVo resultVo = new UserImportResultVo();
		String path=request.getSession().getServletContext().getRealPath("/");
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		MultipartHttpServletRequest multipartHttpservletRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartHttpservletRequest.getFile("fileImport");
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase();
		
		if(!("xls".equals(ext)||"xlsx".equals(ext))){
			throw new ServiceException("只能上传excel文件");
		}
		
		String datePath =DateConstants.DATE_FORMAT_NUM_SHORT().format(new Date());
		path=path+WebappConfigUtil.getParameter("userImportURL").replace("entId", user.getEntId())+datePath;
		
		File dir = new File(path);
		if(!dir.exists()){
			dir.mkdirs();
		}
		
		String fileName= user.getEntId()+"_"+DateConstants.DATE_FORMAT_NUM().format(new Date())+ new Random().nextInt(10)+"."+ext;
		String destPath = path+"/"+fileName;
		File destFile = new File(destPath);
		try {
			file.transferTo(destFile);
		} catch (IllegalStateException | IOException e) {
			e.printStackTrace();
			throw new ServiceException("上传文件失败");
		}
		Map<String, Integer> map = new HashMap<String, Integer>();
		try{
			map = parseExcelTitle(destPath,user.getEntId());
		}catch(ServiceException e){
			throw new ServiceException(e.getMessage());
		}
		if(map.isEmpty()){
			throw new ServiceException("模板文件错误,请从新下载模板文件");
		}
		/*if(!map.containsKey("loginName")){
			throw new ServiceException("模板文件错误,请从新下载模板文件");
		}*/
			
//		List<DBObject> list = new ArrayList<DBObject>();
		try{
//			list = parseExcel(destPath,map, request, user);
			resultVo = parseExcel(destPath,map, request, user);
		}catch(ServiceException e){
			throw new ServiceException(e.getMessage());
		}
		
		/*resultVo.setTotalNum(list.size()+"");//总记录条数
		int successNum = 0;
		int updateNum = 0;
		int failNum = 0;
		List<String> failList = new ArrayList<String>();//失败记录列表
		if(list !=null || list.size()!= 0){
			int i=1;
			for(DBObject dbo : list){
				String s = addUser(request, user, dbo, updateUserFlag);
				if("success".equals(s)){//添加成功
					successNum++;
				}else{//失败
					failNum++;
					s="导入错误：第 "+i+" 行："+s;
					failList.add(s);
				}
				i++;
			}
		}
		
		resultVo.setImportSuccessNum(successNum+"");
		resultVo.setUpdateSuccessNum(updateNum+"");
		resultVo.setImportFailNum(failNum+"");
		resultVo.setFailList(failList);*/
		return resultVo;
	}
	
	/**
	 * 解析excel文件标题行，确定每一列的数据
	 * @param path
	 * @param entId
	 * @return
	 * @throws ServiceException
	 * @author wangjie
	 * @time 2016年3月28日上午11:29:44
	 */
	private Map<String,Integer> parseExcelTitle(String path, String entId) throws ServiceException {
		Map<String, Integer> map = new HashMap<String, Integer>();
		try {
			File file = new File(path);
			if (path.endsWith("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(file.getPath());
				XSSFSheet sheet = workbook.getSheetAt(0);

				//循环工作表Sheet
				if (sheet == null) {
					workbook.close();
					throw new ServiceException("文件为空");
				}
				//读取第0行标题，确定每一列的位置
				Row hssfRow = sheet.getRow(0);
				if (hssfRow == null) {
					workbook.close();
					return map;
				}
				if(StringUtils.isBlank(getValue(hssfRow.getCell(0)))){
					workbook.close();
					return map;
				}
				int cellNum = hssfRow.getPhysicalNumberOfCells();//总列数
				map.put("physicalNumberOfCells", cellNum);
				//查询启用状态的自定义字段
				List<UserDefinedFiedPo> list=userFieldService.queryDefinedFiled(entId, UserFieldStatusEnum.NORMAL.value, null);
				boolean defined = false;
				if(list.size()>0){//用户有自定义字段
					defined = true;
				}
				for(int i=0;i<cellNum;i++){//循环列
					String s = getValue(hssfRow.getCell(i)).trim();
					if("用户登陆密码".equals(s)){
						map.put("loginPwd", i);
					}else if("用户类型".equals(s)){
						map.put("userType", i);
					}else if("用户姓名".equals(s)){
						map.put("userName", i);
					}else if("用户邮箱".equals(s)){
						map.put("email", i);
					}else if("用户手机号".equals(s)){
						map.put("telPhone", i);
					}else if("昵称".equals(s)){
						map.put("nickName", i);
					}else if("备注".equals(s)){
						map.put("remark", i);
					}else if(defined){//有自定义字段
						for(UserDefinedFiedPo po : list){
							if(po.getName().equals(s)){
								map.put(po.getKey(), i);
								break;
							}
						}
					}
				}
				workbook.close();
			}else{
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
				HSSFSheet sheet = workbook.getSheetAt(0);

				//循环工作表Sheet
				if (sheet == null) {
					workbook.close();
					throw new ServiceException("文件为空");
				}
				//读取第0行标题，确定每一列的位置
				Row hssfRow = sheet.getRow(0);
				if (hssfRow == null) {
					workbook.close();
					return map;
				}
				if(StringUtils.isBlank(getValue(hssfRow.getCell(0)))){
					workbook.close();
					return map;
				}
				int cellNum = hssfRow.getPhysicalNumberOfCells();//总列数
				map.put("physicalNumberOfCells", cellNum);
				//查询启用状态的自定义字段
				List<UserDefinedFiedPo> list=userFieldService.queryDefinedFiled(entId, UserFieldStatusEnum.NORMAL.value, null);
				boolean defined = false;
				if(list.size()>0){//用户有自定义字段
					defined = true;
				}
				for(int i=0;i<cellNum;i++){//循环列
					String s = getValue(hssfRow.getCell(i)).trim();
					if("用户登录账号".equals(s)){//系统默认字段
						map.put("loginName", i);
					}else if("用户登陆密码".equals(s)){
						map.put("loginPwd", i);
					}else if("用户类型".equals(s)){
						map.put("userType", i);
					}else if("用户姓名".equals(s)){
						map.put("userName", i);
					}else if("用户邮箱".equals(s)){
						map.put("email", i);
					}else if("用户手机号".equals(s)){
						map.put("telPhone", i);
					}else if("昵称".equals(s)){
						map.put("nickName", i);
					}else if("备注".equals(s)){
						map.put("remark", i);
					}else if(defined){//有自定义字段
						for(UserDefinedFiedPo po : list){
							if(po.getName().equals(s)){
								map.put(po.getKey(), i);
								continue;
							}
						}
					}
				}
				workbook.close();
			}
			return map;
		} catch (Exception e) {
			e.printStackTrace();
			throw new ServiceException("解析excel文件失败，异常:"+e.getMessage());
		}
	}
	
	private UserImportResultVo parseExcel(String path, Map<String, Integer> map, HttpServletRequest request, SsoUserVo user) throws ServiceException {
		List<DBObject> list = new ArrayList<DBObject>();
		UserImportResultVo resultVo = new UserImportResultVo();
		List<String> failList = new ArrayList<String>();//失败记录列表
		
		String resultPath=request.getSession().getServletContext().getRealPath("/");//返回文件路径
		String datePath =DateConstants.DATE_FORMAT_NUM_SHORT().format(new Date());
		resultPath=resultPath+WebappConfigUtil.getParameter("userImportURL").replace("entId", user.getEntId())+datePath+"/result/";
		
		
		try {
			File file = new File(path);
			if (path.endsWith("xlsx")) {
				XSSFWorkbook workbook = new XSSFWorkbook(file.getPath());
				XSSFSheet sheet = workbook.getSheetAt(0);

			// 循环工作表Sheet
				if (sheet == null) {
					workbook.close();
//					return list;
				}

				// 循环行Row
				for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row hssfRow = sheet.getRow(rowNum);
					if (hssfRow == null) {
						continue;
					}
					/*if(StringUtils.isBlank(getValue(hssfRow.getCell(0)))){
						continue;
					}*/
					DBObject userPo=new BasicDBObject();
					for (Entry<String, Integer> entry: map.entrySet()) {  
						int cellNum = entry.getValue();
						userPo.put(entry.getKey(), getValue(hssfRow.getCell(cellNum)).trim());
					}
					
					list.add(userPo);
				}
				workbook.close();
			}else{
				FileOutputStream fileOut = null;
				
				HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
				HSSFSheet sheet = workbook.getSheetAt(0);
				// 循环工作表Sheet
				if (sheet == null) {
					workbook.close();
//					return list;
				}

				int successNum = 0;
				int updateNum = 0;
				int failNum = 0;
				
				//excel文件总列数
				int physicalNumberOfCells = map.get("physicalNumberOfCells");
				map.remove("physicalNumberOfCells");
				
				//创建标题行
				Row hssfRow0 = sheet.getRow(0);
				hssfRow0.createCell(physicalNumberOfCells).setCellValue("导入结果");
				sheet.setColumnWidth(physicalNumberOfCells, 50*256);
				
				// 循环行Row
				for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
					Row hssfRow = sheet.getRow(rowNum);
					if (hssfRow == null) {
						continue;
					}
					/*if(StringUtils.isBlank(getValue(hssfRow.getCell(0)))){
						continue;
					}*/
					DBObject userPo=new BasicDBObject();
					for (Entry<String, Integer> entry: map.entrySet()) {  
						int cellNum = entry.getValue();
						userPo.put(entry.getKey(), getValue(hssfRow.getCell(cellNum)).trim());
					}
					list.add(userPo);
					
					String s = addUser(request, user, userPo, "");
					//修改excel文件,设置结果
					hssfRow.createCell(physicalNumberOfCells).setCellValue(s);
					
					if("success".equals(s)){//添加成功
						successNum++;
					}else{//失败
						failNum++;
						s="导入错误：第 "+rowNum+" 行："+s;
						failList.add(s);
					}
					
				}
				String resultFileName= user.getEntId()+"_result"+ new Random().nextInt(10)+".xls";
				
				File dir = new File(resultPath);
				if (!dir.exists()) {
					dir.mkdirs();
				}
				
				File destFile = new File(resultPath+resultFileName);
				if(!destFile.exists()){
					destFile.createNewFile();
				}
				
				fileOut = new FileOutputStream(resultPath+resultFileName);
				workbook.write(fileOut);
				fileOut.close();
				
				resultVo.setTotalNum(list.size()+"");//总记录条数
				
				resultVo.setImportSuccessNum(successNum+"");
				resultVo.setUpdateSuccessNum(updateNum+"");
				resultVo.setImportFailNum(failNum+"");
				resultVo.setFailList(failList);
				resultVo.setResultFilePath(resultPath+resultFileName);
				
				workbook.close();
			}
			return resultVo;
		} catch (Exception e) {
			System.err.println(e);
			e.printStackTrace();
			throw new ServiceException("解析excel文件失败，异常:"+e.getMessage());
		}
	}
	
	private String getValue(Cell hssfCell) {
		if(hssfCell==null){
			return "";
		}
		if (hssfCell.getCellType() == hssfCell.CELL_TYPE_BOOLEAN) {
			return String.valueOf(hssfCell.getBooleanCellValue());
		} else if (hssfCell.getCellType() == hssfCell.CELL_TYPE_NUMERIC) {
			return String.valueOf( (hssfCell.getNumericCellValue()));
		} else {
			return String.valueOf(hssfCell.getStringCellValue());
		}
	}

	@Override
	public String downloadExcel(HttpServletRequest request, String realPath) throws ServiceException {
		// TODO Auto-generated method stub
		try{
			SsoUserVo user = SsoSessionUtils.getUserInfo(request);
			//查询启用状态的自定义字段
			List<UserDefinedFiedPo> list=userFieldService.queryDefinedFiled(user.getEntId(), UserFieldStatusEnum.NORMAL.value, null);
			
			String entPath =user.getEntId();
			File dir = new File(realPath+user.getEntId());
			if (!dir.exists()) {
				dir.mkdirs();
			}
			
			String fileName= user.getEntId()+"_demo"+ new Random().nextInt(10)+".xls";
			String relativePath =  entPath+"/"+fileName;
			File destFile = new File(realPath+relativePath);
			if(!destFile.exists()){
				destFile.createNewFile();
			}
			
			//创建excel 设置字体
			WritableFont titleFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);   
			WritableFont normalFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.BLACK); 
			WritableFont redFont = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false, UnderlineStyle.NO_UNDERLINE, jxl.format.Colour.RED); 

			WritableFont titleHeadFont = new WritableFont(WritableFont.ARIAL, 15, WritableFont.BOLD, false, UnderlineStyle.NO_UNDERLINE,jxl.format.Colour.BLACK);   
			WritableCellFormat titleHead = new WritableCellFormat(titleHeadFont);
			//冗余代码  --> titleHead = new WritableCellFormat(titleFont);
			titleHead.setAlignment(jxl.format.Alignment.CENTRE); 
			
			WritableCellFormat title = new WritableCellFormat(titleFont);
			//冗余代码  --> title = new WritableCellFormat(titleFont);
			title.setAlignment(jxl.format.Alignment.CENTRE); 
			title.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.getStyle(1));

			WritableCellFormat normal = new WritableCellFormat(normalFont);
			//冗余代码  --> normal = new WritableCellFormat(normalFont);
			normal.setAlignment(jxl.format.Alignment.CENTRE); 
			normal.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.getStyle(1));
			normal.setWrap(true);//自动换行
			
			WritableCellFormat red = new WritableCellFormat(redFont);
			//冗余代码  --> red = new WritableCellFormat(redFont);
			red.setAlignment(jxl.format.Alignment.CENTRE); 
			red.setBorder(jxl.format.Border.ALL, jxl.format.BorderLineStyle.getStyle(1));
			red.setWrap(true);//自动换行
			
			//
			WritableWorkbook wb = Workbook.createWorkbook(destFile);
			WritableSheet sheet = wb.createSheet("sheet", 0); 
			sheet.setColumnView(0, 25); // 设置列的宽度
			sheet.setColumnView(1, 25); // 设置列的宽度
			sheet.setColumnView(2, 25); // 设置列的宽度
			sheet.setColumnView(3, 25); // 设置列的宽度
			sheet.setColumnView(4, 25); // 设置列的宽度
			sheet.setColumnView(5, 25); // 设置列的宽度
			sheet.setColumnView(6, 25); // 设置列的宽度
			//sheet.setColumnView(7, 25); // 设置列的宽度
			
			//sheet.addCell(new Label(0, 0, "用户登录账号", title));//loginName
			sheet.addCell(new Label(0, 0, "用户类型", title));//userType;客户;客服
			sheet.addCell(new Label(1, 0, "用户姓名", title));//userName
			sheet.addCell(new Label(2, 0, "用户邮箱", title));//email
			sheet.addCell(new Label(3, 0, "用户手机号", title));//telPhone
			sheet.addCell(new Label(4, 0, "用户登陆密码", title));//loginPwd
			sheet.addCell(new Label(5, 0, "昵称", title));//nickName
			sheet.addCell(new Label(6, 0, "备注", title));//remark
			
			//用户自定义字段
			int num = 7;//系统默认字段数量
			for(int i=0;i<list.size();i++){
				sheet.setColumnView(num+i, 25); // 设置列的宽度
				
				sheet.addCell(new Label(num+i, 0, list.get(i).getName(), title));//自定义字段
			}
			wb.write();
			wb.close();
			
			return relativePath;
			
		}catch (IOException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}catch (WriteException e) {
			e.printStackTrace();
			throw new ServiceException(e.getMessage());
		}
	}
	
	private String addUser(HttpServletRequest request,SsoUserVo user, DBObject dbo, String updateUserFlag) throws ServiceException {
		try{
			boolean hasEmail = false;
			boolean hasTelPhone = false;
			if(dbo.containsField("email") && StringUtils.isNotBlank(dbo.get("email")+"")){//邮箱是否存在
				if(!EMailCheckUtil.check((String)dbo.get("email"))){//邮箱格式错误
					return "邮箱："+dbo.get("email")+"格式错误";
				}else{
					//验证邮箱是否被注册
					if(userMongoService.existsEmails(user.getEntId(), (String)dbo.get("email"))){
						return "邮箱："+dbo.get("email")+"已被注册";
					}
				}
				hasEmail = true;
			}
			if(dbo.containsField("telPhone") && StringUtils.isNotBlank(dbo.get("telPhone")+"")){//电话号码是否存在
				//电话号码格式验证
				if(StringUtils.isNumeric((String)dbo.get("telPhone")) && dbo.get("telPhone").toString().startsWith("1")){
					if(userMongoService.existsPhone(user.getEntId(), (String)dbo.get("telPhone"))){
						return "电话："+(String)dbo.get("telPhone")+"已被占用";
					}
					hasTelPhone = true;
				}else{
					return "电话："+dbo.get("telPhone")+"格式错误";
				}
			}
			String loginType="";
			String userType="";
			if(!hasEmail && !hasTelPhone){//邮箱和电话都不存在
				return "电话和邮箱必须存在一个";
			}else if(hasEmail){//邮箱存在
				dbo.put("loginName", (String)dbo.get("email"));
				dbo.put("loginType", LoginType.MAILBOX.value);
				String activeCode = MD5Util.MD5((String)dbo.get("email") + new Date().getTime());
				dbo.put("activeCode", activeCode);
				loginType = LoginType.MAILBOX.value;
			}else if(!hasEmail && hasTelPhone){//邮箱不存在，电话号码存在
				dbo.put("loginName", (String)dbo.get("telPhone"));
				dbo.put("loginType", LoginType.TELEPHONE.value);
				loginType = LoginType.TELEPHONE.value;
			}
			//只能导入客户
			if(dbo.containsField("userType")){//用户类型存在
				/*if(UserType.SERVICE.desc.equals(dbo.get("userType"))){
					dbo.put("userType", UserType.SERVICE.value);
					dbo.put("roleId", RoleType.NORMAL.value);//设置角色为普通客服
					userType = UserType.SERVICE.value;
				}else{
					dbo.put("userType", UserType.CUSTOMER.value);
					userType = UserType.CUSTOMER.value;
				}*/
				dbo.put("userType", UserType.CUSTOMER.value);
				userType = UserType.CUSTOMER.value;
			}else{//用户类型不存在
				dbo.put("userType", UserType.CUSTOMER.value);
				userType = UserType.CUSTOMER.value;
			}
			if(dbo.containsField("userName")){//用户名判断
				dbo.put("nickName", dbo.get("userName"));
			}else{
				if (loginType.equals(LoginType.MAILBOX.value)) {
					dbo.put("userName", dbo.get("loginName").toString().split("@")[0]);
					dbo.put("nickName", dbo.get("loginName").toString().split("@")[0]);
				}else if(loginType.equals(LoginType.TELEPHONE.value)){	
					dbo.put("userName", dbo.get("loginName"));
					dbo.put("nickName", dbo.get("loginName"));
				}
			}
			if(dbo.containsField("loginPwd")){//密码判断
				//密码存在
				String loginPwd = CdeskEncrptDes.encryptST(dbo.get("loginPwd").toString().trim());
				dbo.put("loginPwd", loginPwd);
			}else{
				//密码不存在，用登录名作为密码
				String loginPwd = CdeskEncrptDes.encryptST(dbo.get("loginName").toString().trim());
				dbo.put("loginPwd", loginPwd);
			}
			Date date = new Date();
			String time = DateConstants.DATE_FORMAT().format(date);
			String userId = userDao.getUserId();
			dbo.put("userId", userId);
			dbo.put("entId", user.getEntId());
			dbo.put("entName", user.getEntName());
			dbo.put("userStatus", UserStatus.NORMAL.value);
			dbo.put("createTime", time);
			dbo.put("updateTime", time);
			dbo.put("updatorId", user.getUserId());
			dbo.put("updatorName", user.getUserName());
			dbo.put("creatorId", user.getUserId());
			dbo.put("creatorName", user.getUserName());
			dbo.put("ccodEntId", user.getCcodEntId());
			
			if(UserType.SERVICE.value.equals(userType)){//客服需要调用CCOD接口写入坐席信息
				DatEntInfoPo entPo=ParamUtils.getEntInfo(user.getEntId());
				CCODRequestPo ccodPo = new CCODRequestPo();
				ccodPo.setEnterpriseId(entPo.getCcodEntId());
				ccodPo.setAgentId(userId);
				ccodPo.setAgentName(dbo.get("userName")+"");
				ccodPo.setAgentPassword(CdeskEncrptDes.decryptST((String)dbo.get("loginPwd")));
				ccodPo.setAgentRole(AgentRoleType.NORMAL.value);
				CCODClient.addAgent(ccodPo);
			}
			
			int success = userMongoService.add(dbo, user.getEntId());
			//客服添加需要刷新缓存
			if(UserType.SERVICE.value.equals(userType)){
				ParamUtils.refreshCache(CacheGroup.ENT_USER, user.getEntId());
			}
			
			/* 不发送邮件 */
			/*if (dbo.containsField("email")) {					
				DatEntUserPo po=new DatEntUserPo();
				po.setEntId(user.getEntId());
				po.setEmail(dbo.get("email")+"");
				try {
					userService.sendMailMongo(request, po, dbo.get("activeCode")+"", false);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}*/
			if (success > 0) {
				ManageLogUtils.AddSucess(request, "批量导入用户-添加用户", dbo.get("loginName")+"","entId=" + user.getEntId() + ",userType=" + dbo.get("userType") + ",roleId=" + dbo.get("roleId") +",nickName=" + dbo.get("nickName")+ ",userName=" + dbo.get("userName"));
				logMongoService.add(request, LogTypeEnum.ADD,BusinessTypeEnum.USER, userId,BusinessTypeEnum.USER.desc + "(" + dbo.get("email")+""+ ")", dbo);
			} else {
				ManageLogUtils.AddFail(request,new ServiceException("批量导入用户-添加失败"), "批量导入用户-添加用户", dbo.get("loginName")+"","entId=" + user.getEntId() + ",userType=" + dbo.get("userType")+ ",roleId=" + dbo.get("roleId")+ ",nickName=" + dbo.get("nickName")+ ",userName=" + dbo.get("userName"));
			}
			
			
		}catch (ServiceException e) {
			ManageLogUtils.AddFail(request,new ServiceException("批量导入用户失败," + e.getMessage()),"批量导入用户",dbo.get("loginName")+"","entId=" + user.getEntId() + ",userType=" + dbo.get("userType")+ ",roleId=" + dbo.get("roleId") + ",nickName="+ dbo.get("nickName") + ",userName="+ dbo.get("userName"));
			e.printStackTrace();
			return "导入失败";
		}
		
		return "success";
	}
	
}
