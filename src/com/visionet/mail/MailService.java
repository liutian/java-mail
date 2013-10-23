package com.visionet.mail;

import java.io.File;
import java.util.Properties;
import javax.mail.Session;
import org.apache.log4j.Logger;

public abstract class MailService {
	protected int port;
	protected String protocol;
	protected String host;
	protected String userName;
	protected String userPwd;
	protected boolean isDebug;
	
	protected long oldTimestamp;
	protected long newTimestamp;
	
	public static final String PORT = "port";
	public static final String PROTOCOL = "protocol";
	public static final String HOST = "host";
	public static final String USERNAME = "userName";
	public static final String USERPWD = "userPwd";
	
	protected void debug(MailBean mailBean,Logger logger){
		String emailUUID = "%mail:" + System.currentTimeMillis() +"%";
		
		logger.debug(emailUUID + "Sender: " + mailBean.getSender());
		logger.debug(emailUUID + "To: " + mailBean.getToAddresses());
		logger.debug(emailUUID + "CC: " + mailBean.getCcAddresses());
		logger.debug(emailUUID + "BCC: " + mailBean.getBccAddresses());
		logger.debug(emailUUID + "Subject: " + mailBean.getSubject());
		logger.debug(emailUUID + "Body: " + mailBean.getContent());
		logger.debug(emailUUID + "Body-html: " + mailBean.getContentHtml());
		logger.debug(emailUUID + "Body-text: " + mailBean.getContentText());
		logger.debug(emailUUID + "Reply to: " + mailBean.getReplyToAddresses());
		logger.debug(emailUUID + "Message ID: " + mailBean.getMessageID());
		logger.debug(emailUUID + "In Reply To: " + mailBean.getInReplyTo());

		if (mailBean.getAttachments() != null) {
			for (String key : mailBean.getAttachments().keySet()) {
				File attachment = mailBean.getAttachments().get(key);

				if (attachment != null) {
					String path = attachment.getAbsolutePath();

					logger.debug(emailUUID + "Attachment #" + key + ": " + path);
				}
			}
		}
		
		if (mailBean.getInnerResources() != null) {
			for (String key : mailBean.getInnerResources().keySet()) {
				File attachment = mailBean.getInnerResources().get(key);

				if (attachment != null) {
					String path = attachment.getAbsolutePath();

					logger.debug(emailUUID + "InnerResources #" + key + ": " + path);
				}
			}
		}	
	}
	
	protected abstract Session getSession();
	
	protected MailService(Properties prop){
		this.protocol = prop.getProperty(PROTOCOL, this.protocol);
		this.port = Integer.parseInt(prop.getProperty(PORT, String.valueOf(this.port)));
		this.host = prop.getProperty(HOST);
		this.userName = prop.getProperty(USERNAME);
		this.userPwd = prop.getProperty(USERPWD);
	}
	
	public boolean isDebug() {
		return isDebug;
	}

	public void setDebug(boolean isDebug) {
		this.isDebug = isDebug;
	}
}
