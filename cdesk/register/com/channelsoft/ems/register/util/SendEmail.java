package com.channelsoft.ems.register.util;

import java.util.Date;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import com.channelsoft.cri.util.BeanFactoryUtil;

/**
 * 
 * <dl>
 * <dt>SendEmail</dt>
 * <dd>Description:发送邮件的工具类</dd>
 * <dd>Copyright: Copyright (C) 2006</dd>
 * <dd>Company: 青牛（北京）技术有限公司</dd>
 * <dd>CreateDate: 2014-7-4</dd>
 * </dl>
 * 
 * @author cuihc
 */
public class SendEmail {
	
	public static final String HOST = "smtp.163.com";  
    public static final String PROTOCOL = "smtp";     
    public static final int PORT = 25;  
    public static final String FROM = "cdesk_qnsoft@163.com";//发件人的email  
    public static final String PWD = "vxrpgwztvuvsfsdw";//发件人密码  
    
    private static final Logger logger = Logger.getLogger(SendEmail.class);
    private static Session session = null;
    
    private static ThreadPoolUtil threadPool = null;
    
    private static ThreadPoolUtil getHtreadPool() throws Exception {
    	if (threadPool == null) {
    		threadPool = (ThreadPoolUtil)BeanFactoryUtil.getBean("threadPool");
    	}
    	return threadPool;
    }
    
      
    /** 
     * 获取Session 
     * @return 
     */  
    private static synchronized Session getSession() {  
    	if (session == null) {
    		Properties props = System.getProperties();  
            props.put("mail.smtp.host", HOST);//设置服务器地址  
            props.put("mail.transport.protocol" , PROTOCOL);//设置协议  
            props.put("mail.smtp.port", PORT);//设置端口  
            props.put("mail.smtp.auth" , "true");  
            props.put("mail.smtp.starttls.enable", "true");        //好像是解决javax.mail.MessagingException: [EOF]，没有验证
              
            Authenticator authenticator = new Authenticator() {  
      
                @Override  
                protected PasswordAuthentication getPasswordAuthentication() {  
                    return new PasswordAuthentication(FROM, PWD);  
                }  
                  
            };  
            session = Session.getDefaultInstance(props , authenticator);
    	}
        return session;  
    }  
      
    public static void send(final String subject, final String toEmail , final String content)throws Exception {  
    	getHtreadPool().execute(new Runnable() {
			@Override
			public void run() {
				 Session session = getSession(); 
				        
				        session.setDebug(true);  
				        
			        	logger.info("发送邮件[" + toEmail + "]--------------" + content);
			            // Instantiate a message  
			            Message msg = new MimeMessage(session);  
			   
			            //Set message attributes  
			            try {
							msg.setFrom(new InternetAddress(FROM));  
							InternetAddress[] address = {new InternetAddress(toEmail)};  
							msg.setRecipients(Message.RecipientType.TO, address);  
							msg.setSubject(subject);  
							msg.setSentDate(new Date());  
							msg.setContent(content , "text/html;charset=utf-8"); 
							
   
							//Send the message
						    Transport.send(msg);
						} catch (AddressException e) {
							logger.error(e.getMessage(),e);
							logger.info("发送邮件[" + toEmail + "]--------------失败,内容为：" + content);
							return;
						} catch (MessagingException e) {
							logger.error(e.getMessage(),e);
							logger.info("发送邮件[" + toEmail + "]--------------失败,内容为：" + content);
							return;
						}  
			            logger.info("发送邮件[" + toEmail + "]--------------成功,内容为：" + content);
			}
    	});
    }

}
