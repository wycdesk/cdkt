package com.channelsoft.ems.user.service.impl;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.constant.DateConstants;
import com.channelsoft.cri.exception.ServiceException;
import com.channelsoft.cri.util.EMailCheckUtil;
import com.channelsoft.cri.util.WebappConfigUtil;
import com.channelsoft.ems.register.util.MD5Util;
import com.channelsoft.ems.sso.util.SsoSessionUtils;
import com.channelsoft.ems.sso.vo.SsoUserVo;
import com.channelsoft.ems.user.constant.LoginType;
import com.channelsoft.ems.user.constant.UserStatus;
import com.channelsoft.ems.user.constant.UserType;
import com.channelsoft.ems.user.dao.IUserDao;
import com.channelsoft.ems.user.dao.IUserImportDao;
import com.channelsoft.ems.user.dao.IUsrManageDao;
import com.channelsoft.ems.user.po.DatEntUserPo;
import com.channelsoft.ems.user.service.IUserImportService;
import com.channelsoft.ems.user.service.IUserService;
import com.channelsoft.ems.user.vo.DatEntUserVo;
import com.channelsoft.ems.user.vo.UserImportResultVo;

public class UserImportServiceImpl implements IUserImportService {

	private Logger logger = Logger.getLogger(UserImportServiceImpl.class);
			
	@Autowired
	IUserService userService;
	@Autowired
	IUserDao userDao;
	@Autowired
	IUsrManageDao usrManageDao;
	@Autowired
	IUserImportDao userImportDao;
	
	/*@Override
	public UserImportResultVo upload(HttpServletRequest request, String addUserFlag, String updateUserFlag) throws ServiceException {
		// TODO Auto-generated method stub
		UserImportResultVo resultVo = new UserImportResultVo();
		String path=request.getSession().getServletContext().getRealPath("/");
//		String entId = DomainUtils.getEntId(request);
		SsoUserVo user = SsoSessionUtils.getUserInfo(request);
		MultipartHttpServletRequest multipartHttpservletRequest = (MultipartHttpServletRequest) request;
		MultipartFile file = multipartHttpservletRequest.getFile("fileImport");
		String origName = file.getOriginalFilename();
		String ext = FilenameUtils.getExtension(origName).toLowerCase();
		
		if(!("csv".equals(ext))){
			throw new ServiceException("只能上传csv文件");
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
		logger.info("用户批量导入，上传文件的编码方式为："+code);
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
	}*/
	
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
	/*private String register(HttpServletRequest request, DatEntUserPo po, String updateUserFlag) throws ServiceException {
		if("".equals(po.getEmail()) || po.getEmail() == null){
			return "邮箱为空";
		}
		if(!EMailCheckUtil.check(po.getEmail())){//邮箱格式错误
			return "邮箱："+po.getEmail()+"格式错误";
		}
		boolean e = userService.existsEmails(po.getEntId(), po.getEmail());
		if(e&&"1".equals(updateUserFlag)){//游戏已被注册，需要更新
			//执行更新
			if(po.getTelPhone()!=null){//电话号码不为空时，验证电话号码是否重复
				if(userDao.existsPhone(po.getEntId(), po.getTelPhone())){
					return "电话："+po.getTelPhone()+"已被占用";
				}
			}
			usrManageDao.importUserUpdate(po);
			
			return "update";
		}
		if(e&&"0".equals(updateUserFlag)){//游戏已被注册，不更新
			return "邮箱："+po.getEmail()+"已被注册";
		}
		if(e&&"".equals(updateUserFlag)){//游戏已被注册，不更新
			return "邮箱："+po.getEmail()+"已被注册";
		}
		if(!e){//邮箱未注册
			//执行添加用户
			if(po.getTelPhone()!=null){//电话号码不为空时，验证电话号码是否重复
				if(userDao.existsPhone(po.getEntId(), po.getTelPhone())){
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
	}*/

	/*@Override
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
			return userImportDao.userImport(po);
		} catch (DataAccessException e) {
			e.printStackTrace();
			throw new ServiceException(BaseErrCode.DB_ERROR, e.getMessage());
		}
	}*/
}
