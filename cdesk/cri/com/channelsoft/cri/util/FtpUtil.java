package com.channelsoft.cri.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPListParseEngine;
import org.apache.commons.net.ftp.FTPReply;

import com.channelsoft.cri.common.BaseObject;
import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.BaseException;
/**
 * FTP对象工具类，需要实例化，并手动连接、关闭。
 * <dl>
 * <dt>smartbms</dt>
 * <dd>Description：建议使用封装的静态类FtpUtils</dd>
 * <dd>Copyright：Copyright (C) 2013</dd>
 * <dd>Company：青牛(北京)技术有限公司</dd>
 * <dd>CreateDate：2013-1-9 下午09:28:30</dd>
 * </dl>
 * @author 魏铭
 */
public class FtpUtil extends BaseObject{

	/**
	 * FTP 登录用户名
	 */
    private  String userName;
    /**
     * FTP 登录密码
     */
    private  String password;
    /**
     * FTP 服务器地址IP地址
     */
    private  String ip;
    /**
     * FTP 端口
     */
    private int port = 21;
    /**
     * FTP 客户端代理 
     */
    private FTPClient ftpClient = null;
    /**
     * FTP 文件目录
     */
    private String remoteFilePath;
    /**
     * 下载文件名称
     */
    private String ftpFileName;
    private String rootDir;
    /**
     * ftpclient打开的所有文件
     */
    private FTPFile[] fileList;
	
    public String getFtpFileName() {
		return ftpFileName;
	}

    public String getRemoteFilePath() {
    	return remoteFilePath;
    }
    
    public FtpUtil(String userName, String password, String ip, int port) {
		this.userName = userName;
		this.password = password;
		this.ip = ip;
		this.port = port;
	}
    
    public FtpUtil(String ftpUrl) throws BaseException {
		try {
			ftpUrl = ftpUrl.replace("\\","/").replace(File.separator, "/");
			@SuppressWarnings("unused")
			String ipports = setAuth(ftpUrl);//设置用户名密码
			String path = setIpNPort(ftpUrl);//设置ip和port
			setPathNFile(path);//设置文件路径
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(BaseErrCode.ERR_FTP);
		}
	}
    
    public void connectServer() throws BaseException{
        if (ftpClient == null) {
	        int reply;
	        try {
                ftpClient = new FTPClient();
                ftpClient.setControlEncoding("GBK"); //文件名乱码,默认ISO8859-1，不支持中文
                ftpClient.setDefaultPort(port);
                ftpClient.connect(ip);
                ftpClient.login(userName, password);
                super.logSuccess("FTP", "连接FTP", ip);
                reply = ftpClient.getReplyCode();
                ftpClient.setDataTimeout(20000);
                rootDir =  ftpClient.printWorkingDirectory();
                super.logSuccess("FTP", "切换主目录", rootDir);
                
                if (!FTPReply.isPositiveCompletion(reply)) {
                	ftpClient.disconnect();
                	BaseException exception = new BaseException(BaseErrCode.ERR_FTP.code, "FTP连接失败");
                	super.logFail("FTP", "连接FTP", ip, exception);
                	throw exception;
                } else {
                    ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
                }
	        } catch (SocketException e) {
	            e.printStackTrace();
	            BaseException exception =  new BaseException(BaseErrCode.ERR_FTP.code, "登录ftp服务器 " + ip + " 失败,连接超时！");
	            super.logFail("FTP", "登录服务器", ip, exception);
	            throw exception;
	        } catch (Exception e) {
	        	e.printStackTrace();
		        BaseException exception =  new BaseException(BaseErrCode.ERR_FTP.code, "登录ftp服务器 " + ip + " 失败,FTP服务器无法打开！");
		        super.logFail("FTP", "登录服务器", ip, exception);
		        throw exception;
	        }
        }
    }
    
    public void listRemoteAllFiles(String path) throws BaseException{
        try {
        	FTPListParseEngine f = ftpClient.initiateListParsing(path);
	        if (f.hasNext()) {
	        	FTPFile[] files = f.getFiles();
	        	for(FTPFile file:files){
	        		if(file.isDirectory() && !file.getName().equals(".")&& !file.getName().equals("..")){
	            		//logDebug(path + "/" + file.getName());
	            		listRemoteAllFiles(path+ "/" +file.getName());
	            	}else if(!file.getName().equals(".")&& !file.getName().equals("..")){
	            		//logDebug(path+ "/" + file.getName());
	            	}
	        	}
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new BaseException(BaseErrCode.ERR_FTP);
        }
	}
    
    public void closeConnect() {
    	try {
    		if (ftpClient != null) {
    			ftpClient.logout();
    		}
    	} catch (Exception e) {
    	}
    	try {
    		if (ftpClient != null) {
    			ftpClient.disconnect();
    		}
    	} catch (Exception e) {
    	}
	}
    /**
     * 下载文件
     * @param remotePath
     * @param fileName
     * @param localPath
     * @return
     * @CreateDate 2013-1-9 下午03:01:29
     *
     * @author 魏铭
     */
    public String downFile(String remotePath, String fileName, String localPath) throws BaseException {
    	gotoRootDir();
    	remotePath = "./" + remotePath;
    	logDebug("remotePath:" + remotePath + " localPath:"+localPath);
    	
    	try {
    		boolean changeFlag = ftpClient.changeWorkingDirectory(remotePath);//转移到FTP服务器目录 
    		if(changeFlag){
    			if(fileList == null || fileList.length < 1){
    				fileList = ftpClient.listFiles();
    			}
        		boolean getFile = false;
        		for(FTPFile ff: fileList){ 
        			if(ff.getName().equals(fileName)){ 
        				File saveFile = new File(localPath);
        				if(!saveFile.exists()){
        					saveFile.mkdirs();
        				}
        				File localFile = new File(localPath + "/" + ff.getName()); 
        				FileOutputStream fos = new FileOutputStream(localFile);  
        				ftpClient.retrieveFile(ff.getName(), fos); 
        				fos.close(); 
        				getFile = true;
        				break;
        			}
        		}
        		if(!getFile){
        			throw new BaseException(BaseErrCode.ERR_FTP.code, "远程文件不存在");
        		}
        		return localPath+ "/" + fileName;
    		}else{
    			throw new BaseException(BaseErrCode.ERR_FTP.code, "远程目录不存在");
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new BaseException(BaseErrCode.ERR_FTP);
    	}
    }
    /**
     * 获取FTP目录下的文件名，需要在连接以后才能调用
     * @param ftpPath
     * @return
     * @throws BaseException
     * @CreateDate 2013-1-24 下午06:56:07
     *
     * @author 魏铭
     */
    public List<String> listFile(String ftpPath) throws BaseException {
    	List<String> fileNames = new ArrayList<String>();
    	try {
    		gotoRootDir();
        	String remotePath = "./" + ftpPath;
        	logDebug("remotePath:" + remotePath);
        	boolean changeFlag = ftpClient.changeWorkingDirectory(remotePath);//转移到FTP服务器目录 
        	logDebug("changeFlag:" + changeFlag);
        	//ftpClient.enterLocalPassiveMode();
        	if(changeFlag){
        		FTPFile[] files = this.ftpClient.listFiles();
            	for(FTPFile file:files){
            		if(file.isDirectory() && !file.getName().equals(".")&& !file.getName().equals("..")){
                		logDebug(ftpPath + "/" + file.getName());
                	}else if(!file.getName().equals(".")&& !file.getName().equals("..")){
                		logDebug(ftpPath+ "/" + file.getName());
                		fileNames.add(file.getName());
                	}
            	}
        	}else{
    			throw new BaseException(BaseErrCode.ERR_FTP.code, "远程目录不存在");
    		}
        	
        } catch (Exception e) {
        	e.printStackTrace();
        	throw new BaseException(BaseErrCode.ERR_FTP);
        }
        return fileNames;
    }
    
    public String downFile(String savefileName, String localPath) throws BaseException {
    	gotoRootDir();
    	String remotePath = "./" + this.remoteFilePath;
    	logDebug("remotePath:" + remotePath + " localPath:"+localPath);
    	
    	try {
    		boolean changeFlag = ftpClient.changeWorkingDirectory(remotePath);//转移到FTP服务器目录 
    		ftpClient.enterLocalPassiveMode();
    		if(changeFlag){
    			if(fileList == null || fileList.length < 1){
    				fileList = ftpClient.listFiles();
    			}
        		boolean getFile = false;
        		for(FTPFile ff: fileList){ 
        			if(ff.getName().equals(this.getFtpFileName())){ 
        				File saveFile = new File(localPath);
        				if(!saveFile.exists()){
        					saveFile.mkdirs();
        				}
        				File localFile = new File(localPath + "/" + savefileName); 
        				FileOutputStream fos = new FileOutputStream(localFile);  
        				ftpClient.retrieveFile(ff.getName(), fos); 
        				fos.close(); 
        				getFile = true;
        				break;
        			}
        		}
        		if(!getFile){
        			throw new BaseException(BaseErrCode.ERR_FTP.code, "远程文件不存在");
        		}
        		return localPath+ "/" + savefileName;
    		}else{
    			throw new BaseException(BaseErrCode.ERR_FTP.code, "远程目录不存在");
    		}
    		
    	} catch (Exception e) {
    		e.printStackTrace();
    		throw new BaseException(BaseErrCode.ERR_FTP);
    	}
    }
    
    /**
     * 递归创建FTP文件夹
     * @param ftpDir
     * @CreateDate 2013-1-9 下午03:01:16
     *
     * @author 陈煜文
     */
    private void makeFtpDir(String ftpDir) {
    	gotoRootDir();
    	String[] paths = ftpDir.split("/");
    	for(int i = 1; i < paths.length; i++) {
    		try {
				if(!ftpClient.changeWorkingDirectory(paths[i])) {
					ftpClient.makeDirectory(paths[i]);
					ftpClient.changeWorkingDirectory(paths[i]);
				}
			} catch (Exception e) {
				e.printStackTrace();
				throw new BaseException(BaseErrCode.ERR_FTP.code, "创建远程目录失败");
			}
    	}
    }

    /**
     * 直接上传当地文件
     * @param localFilePath
     * @param fileName
     * @param ftpfile
     * @CreateDate 2013-1-9 下午03:01:05
     *
     * @author 陈煜文
     */
    public void uploadFile(String localFilePath, String fileName, String ftpfile) {
    	try {
			if(!ftpClient.changeWorkingDirectory(ftpfile)) {
				makeFtpDir(ftpfile);
			}
			File localFile = new File(localFilePath); 
			FileInputStream fis = new FileInputStream(localFile);
			ftpClient.enterLocalPassiveMode();
			ftpClient.storeFile(fileName, fis);
			fis.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }

    /**
     * 上传文件夹或文件 ,包括所有子文件夹和文件, 返回根目录地址
     * @param localPath
     * @param remoteDir
     * @return
     * @CreateDate 2013-1-9 下午03:00:57
     *
     * @author 陈煜文
     */
    public String uploadDir(String localPath,String remoteDir) throws BaseException{
    	gotoRootDir();
    	File localFilePath = new File(localPath);
    	logDebug("remoteDir:"+remoteDir + " work dir:"+localFilePath + " localpath:"+localPath);
    	try{
    		if(localFilePath.exists()) {
    			if(localFilePath.isDirectory()) {
    				//如果文件夹存在
        			File[] dirFileList = localFilePath.listFiles();
        			if(dirFileList != null && dirFileList.length > 0) {
        				for(File ff : dirFileList) {
        					if(ff.isDirectory()) {
        						String dirName = ff.getName();
        						//在FTP服务器上创建文件夹
        						String localDir = localPath + "/" + dirName;
        						String newRemoteDir = remoteDir + "/" + dirName;
        						if(!ftpClient.changeWorkingDirectory(newRemoteDir)){
        							makeFtpDir(newRemoteDir);
        						}
        						uploadDir(localDir, newRemoteDir);
        					} else {
        						//一般文件
        						uploadFile(localPath + "/" + ff.getName(), ff.getName(), remoteDir);
        					}
        				}
        			}
    			} else {
    				//如果是文件的话则直接上传文件
        			uploadFile(localPath, localFilePath.getName(), remoteDir);
    			}
				return remoteDir + "/" + localFilePath.getName();
    		} else {
    			throw new BaseException(BaseErrCode.ERR_FTP.code, "远程目录不存在");
    		}
    	}catch(Exception e){
    		e.printStackTrace();
    		throw new BaseException(BaseErrCode.ERR_FTP);
    	}
    }
    /**
     * 下载文件夹 ,包括所有子文件夹和文件, 返回根目录地址
     * @param savePath
     * @return
     * @CreateDate 2013-1-9 下午08:42:25
     *
     * @author 魏铭
     */
    public String downloadDir(String savePath)
    {
    	String remoteDir = this.getRemoteFilePath() + this.getFtpFileName();
    	savePath = savePath + File.separator + this.getFtpFileName();
    	return downloadDir(savePath, remoteDir);
    }

    private String downloadDir(String localPath,String remoteDir) throws BaseException{
    	gotoRootDir();
    	String workDir = "./"+ remoteDir;
    	logDebug("remoteDir:"+remoteDir + " work dir:"+workDir + " localpath:"+localPath);
    	try{
    		boolean changeFlag = ftpClient.changeWorkingDirectory(workDir);
    		if(changeFlag){//如果切换成功
    			FTPFile[] dirFileList = ftpClient.listFiles();
    			if(dirFileList != null && dirFileList.length > 0){
	        		for(FTPFile ff:dirFileList){
	        			logDebug("Down file name:"+ff.getName());
	        			if(ff.isDirectory()){//如果是文件夹
	        				String dirName = ff.getName();
	        				//在本地创建文件夹
	        				String localDir = localPath + "/" + dirName;
	        				String newRemotDir = remoteDir + "/" + dirName;
	        				File localFile = new File(localDir);
	        				if(!localFile.exists()){
	        					localFile.mkdirs();
	        				}
	        				this.downloadDir(localDir, newRemotDir);
	        			}else{//一般文件
	        				File saveDir = new File(localPath);
	        				if(!saveDir.exists()){
	        					saveDir.mkdirs();
	        				}
	        				File localFile = new File(localPath + "/" + ff.getName()); 
	        				FileOutputStream fos = new FileOutputStream(localFile);  
	        				ftpClient.retrieveFile(ff.getName(), fos); 
	        				fos.close(); 
	        			}
	        		}
	    		}
        		return localPath;
    		}else{
    			throw new BaseException(BaseErrCode.ERR_FTP.code, "远程目录不存在");
    		}
    	}catch(Exception e){
    		throw new BaseException(BaseErrCode.ERR_FTP);
    	}
    }
    
    private String setIpNPort(String fileUrl){
    	Pattern pattern = Pattern.compile("(\\d{1,3}\\.){3}\\d{1,3}(:\\d{1,5}){0,1}");
		Matcher matcher = pattern.matcher(fileUrl);
		String ipNPort = "";
		if(matcher.find()){
			ipNPort = matcher.group(0);
			if(StringUtils.contains(ipNPort, ":")){
				ip = ipNPort.split(":")[0];
				port = Integer.valueOf(ipNPort.split(":")[1]);
			}else{
				ip = ipNPort;
				port = 21;
			}
		}
		if(StringUtils.equals(ipNPort, "")){
			return "";
		}else{
			String[] filePath = fileUrl.split(ipNPort);
			StringBuffer sbf = new StringBuffer();
			sbf.append(filePath[1]);
			return sbf.toString();
		}
    }
    private void setPathNFile(String path){
    	String fileSeprator = "/";
		String[] files = path.split(fileSeprator, -1);
    	int len = files.length;
    	ftpFileName = files[len - 1];
    	StringBuffer sbf = new StringBuffer();
    	
    	for(int j = 1; j < len - 1; j++){
    		sbf.append("/");
    		sbf.append(files[j]);
    	}
        remoteFilePath =  sbf.toString();
    }

    private String setAuth(String fileUrl){
//    	Pattern pattern = Pattern.compile("//[\\w]+:[\\w]+@");
//		Matcher matcher = pattern.matcher(fileUrl);
//		String ipNPort = "";
//		if(matcher.find()){
//			ipNPort = matcher.group();
//		}
//		int len = ipNPort.length();
//		ipNPort = ipNPort.substring(2, len - 1);
    	String str1 = fileUrl.substring(6);
		String[] split1 = str1.split("@");
		
		String[] acc = split1[0].split(":");
		userName = acc[0];
		password = acc[1];
		return split1[1];
    }
    
    private void gotoRootDir(){
		try {
			boolean changeRoot = ftpClient.changeWorkingDirectory(rootDir);
			logDebug("change to root:"+changeRoot);
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(BaseErrCode.ERR_FTP.code, "远程目录不存在");
		}
    }
    
    public static void main(String[] args)
    {
//    	FtpUtil ftp = new FtpUtil("ftp://bms:bms-nmlt-unicom-2012@1.25.202.39:21/ftp/bms");
//    	ftp.connectServer();
//    	ftp.downFile("新建文本文档.txt", "d:/ftptest/t1");
//    	ftp.closeConnect();
    	
//    	String remoteFile = "ftp://ftprecord:ftprecord@192.168.1.108:21/";
//		String localFilePath = "D:\\IPTV\\CpInfo\\response";
//		String ftpfile = "CpInfo/response/";
//		FtpUtil ftp = new FtpUtil(remoteFile);
//		ftp.connectServer();
//		ftp.uploadDir(localFilePath, ftpfile);
//		ftp.closeConnect();
		
//    	String localFilePath = "D:\\IPTV\\CpInfo\\response";
//		String desDir = "D:\\IPTV\\CpInfo\\backup";
//		File dir = new File(localFilePath);
//		File des = new File(desDir);
//		boolean success = dir.renameTo(new File(des, dir.getName()));  
//		System.out.println("success is "+success);
    	
    	String src = "D:\\IPTV\\CpInfo\\response";
    	String dest = "D:\\IPTV\\CpInfo\\backup";
    	File srcFolder = new File(src);
    	File destFolder = new File(dest);
    	File newFile = new File(destFolder.getAbsoluteFile() + "\\" + srcFolder.getName());
    	boolean result = srcFolder.renameTo(newFile);
    	System.out.println("result is "+result);
    }
}
