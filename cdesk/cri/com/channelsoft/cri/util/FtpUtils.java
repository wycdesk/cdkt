package com.channelsoft.cri.util;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.log4j.Logger;

import com.channelsoft.cri.constant.BaseErrCode;
import com.channelsoft.cri.exception.BaseException;


/**
 * FTP静态工具类
 * <dl>
 * <dt>smartbms</dt>
 * <dd>Description：FtpUtil的静态封装，直接调用</dd>
 * <dd>Copyright：Copyright (C) 2013</dd>
 * <dd>Company：青牛(北京)技术有限公司</dd>
 * <dd>CreateDate：2013-1-9 下午09:28:07</dd>
 * </dl>
 * @author 魏铭
 */
public class FtpUtils {
	protected static Logger logger = Logger.getLogger(FtpUtils.class);
	/**
	 * 生成本地文件名。
	 * @param oldFileName
	 * @param isChangeName
	 * @return
	 * @CreateDate 2013-1-9 下午04:40:29
	 *
	 * @author 魏铭
	 */
	private static String createFileName(String oldFileName, boolean isChangeName)
	{
        String saveFileName = oldFileName;
        if (isChangeName)
        {
        	String suffix  = oldFileName.substring(oldFileName.lastIndexOf("."));
        	saveFileName = System.currentTimeMillis() + suffix.toLowerCase();
        }
        return saveFileName;
	}
	/**
	 * 生成时间路径
	 * @param savePath
	 * @param isRelative
	 * @return
	 * @CreateDate 2013-1-9 下午09:29:41
	 *
	 * @author 魏铭
	 */
	private static String createRelativePath(String savePath, boolean isRelative)
	{
		String targetDir = savePath;
		String returnLocalPath = "/";
		if (isRelative)
		{
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMM");
			String relativePath = sdf.format(new Date());
			targetDir = savePath + "/" + relativePath;
			returnLocalPath += relativePath;
		}
		
		File dirPath = new File(targetDir);
        if(!dirPath.exists()){
        	dirPath.mkdirs();
        }
        return returnLocalPath;
	}
	
	/**
	 * 下载文件或目录
	 * 注意：下载目录是将FTP路径下的最后一个目录保存到本地路径下，即将ftp://ip:port/path/dir 保存为savePath/dir，会自动生成dir目录
	 * @param ftpUrl
	 * @param savePath
	 * @param isChangeName 是否需要替换文件名
	 * @param isRelative 是否存放到时间目录
	 * @param isDir 是否是目录
	 * @return
	 * @throws BaseException
	 * @CreateDate 2013-1-9 下午09:19:20
	 *
	 * @author 魏铭
	 */
	public static String download(String ftpUrl, String savePath, boolean isChangeName, boolean isRelative, boolean isDir) throws BaseException
	{
		if (isDir)
		{
			return downloadDir(ftpUrl, savePath);
		}
		else
		{
			return downloadFile(ftpUrl, savePath, isChangeName, isRelative);
		}
	}
	/**
	 * 下载文件
	 * @param ftpUrl
	 * @param savePath
	 * @param isChangeName 是否需要替换文件名
	 * @param isRelative 是否存放到时间目录
	 * @return
	 * @throws BaseException
	 * @CreateDate 2013-1-9 下午09:19:56
	 *
	 * @author 魏铭
	 */
	public static String downloadFile(String ftpUrl, String savePath, boolean isChangeName, boolean isRelative) throws BaseException
	{
		FtpUtil ftp = new FtpUtil(ftpUrl);
		try {
			String relativePath = FtpUtils.createRelativePath(savePath, isRelative);
			String localFileName = FtpUtils.createFileName(ftp.getFtpFileName(), isChangeName);
			ftp.connectServer();
			ftp.downFile(localFileName, savePath + relativePath);
			return relativePath + "/" + localFileName;
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(BaseErrCode.ERR_FTP);
		} finally{
			ftp.closeConnect();
		}
	}
	/**
	 * 下载目录
	 * 注意：下载目录是将路径下的最后一个目录保存到本地路径下，即将ftp://ip:port/path/dir 保存为savePath/dir，会自动生成dir目录
	 * @param ftpUrl
	 * @param savePath
	 * @return
	 * @throws BaseException
	 * @CreateDate 2013-1-9 下午09:20:09
	 *
	 * @author 魏铭
	 */
	public static String downloadDir(String ftpUrl, String savePath) throws BaseException{
		FtpUtil ftp = new FtpUtil(ftpUrl);
		try {
			ftp.connectServer();
			return ftp.downloadDir(savePath);
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(BaseErrCode.ERR_FTP);
		}finally{
			ftp.closeConnect();
		}
    }
	/**
	 * 上传文件或目录
	 * 注意：上传目录是将路径下的所有文件传到FTP路径下，即将savePath/dir/ 上传到ftp://ip:port/path， 不会生成dir目录。
	 * @param localPath
	 * @param ftpUrl
	 * @return
	 * @CreateDate 2013-1-9 下午09:20:33
	 *
	 * @author 魏铭
	 */
	public static String upload(String localPath, String ftpUrl)
	{
		FtpUtil ftp = new FtpUtil(ftpUrl);
    	try {
			ftp.connectServer();
			ftp.uploadDir(localPath, "/" + ftp.getRemoteFilePath() + "/" + ftp.getFtpFileName());
			ftp.closeConnect();
			String fileName = localPath.replace("\\","/").replace(File.separator, "/").substring(localPath.lastIndexOf("/") + 1);
			return ftpUrl + "/" + fileName;
		} catch (BaseException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new BaseException(BaseErrCode.ERR_FTP);
		}finally{
			ftp.closeConnect();
		}
	}
	
	
	public static void main(String[] args)
	{
		downloadDir("ftp://epsftp:epsftp@192.168.1.206/dir11", "D:\\ftptest");
	}
}
