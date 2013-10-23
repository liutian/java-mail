package com.visionet.mail;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import javax.mail.Folder;
import javax.mail.Header;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMessage.RecipientType;
import javax.mail.internet.MimeUtility;
import javax.mail.search.SearchTerm;

import org.apache.log4j.Logger;

public class MailRecipient extends MailService{
	private static Logger logger = Logger.getLogger(MailRecipient.class);
	protected int port = 143;
	protected String protocol = "imap";
	private boolean downloadFile;
	private String folderName;
	private String resourceDirPath;
	private MailListener listener;
	private SearchTerm searchTerm;
	
	public MailRecipient(Properties prop){
		super(prop);
	}
	
	public List<MailBean> recipient() throws MessagingException{
		Session session = getSession();
		Store store = session.getStore(protocol);
		store.connect(host,port,userName,userPwd); 
		
		boolean recipientBool = true;
		if(listener != null){
			recipientBool = listener.connect(store);
		}
		
		List<MailBean> mailBeanList = new ArrayList<MailBean>();
		
		if(recipientBool){
			mailBeanList = _recipient(store);
		} 
		
		store.close();
		
		return mailBeanList;
	}

	private List<MailBean> _recipient(Store store) throws MessagingException {
		Folder folder = store.getFolder(folderName == null ? "INBOX" : folderName);
		List<MailBean> mailBeanList = new ArrayList<MailBean>();
		
		if(folder != null){
			folder.open(Folder.READ_WRITE);
			Message[] msgArr = null;
			
			if(searchTerm != null){
				msgArr = folder.search(searchTerm);
			}else{
				msgArr = folder.getMessages();
			}
			
			for (int i = 0; i < msgArr.length; i++) {
				MailBean mailBean = _convert((MimeMessage)msgArr[i]);
				
				if(isDebug){
					debug(mailBean, logger);
				}
				
				if(listener != null){
					listener.each(msgArr[i], mailBean);
				}
				
				mailBeanList.add(mailBean);
			}
			
			folder.close(true);
		}
		
		return mailBeanList;
	}

	private MailBean _convert(MimeMessage msg) throws MessagingException {
		MailBean mailBean = new MailBean();
		mailBean.setMessageID(msg.getMessageID());
		
		String fromAddress = null;
		
		if(null != msg.getFrom() && msg.getFrom().length > 0){
    		InternetAddress[] addresses = (InternetAddress[])msg.getFrom();
    		fromAddress  = addresses[0].getAddress();
    	}else if(null != msg.getSender()){
			InternetAddress address = (InternetAddress)msg.getSender();
    		fromAddress = address.getAddress();
    	}else{
    		fromAddress = "unnamed";
    	}
		
		mailBean.setSender(fromAddress);
		mailBean.setSubject(msg.getSubject());
		mailBean.setSenderDate(msg.getSentDate());
		
		InternetAddress[] tos = (InternetAddress[])msg.getRecipients(RecipientType.TO);
		mailBean.setToAddresses(new ArrayList<String>());
		for (int i = 0; i < tos.length; i++) {
			mailBean.getToAddresses().add(tos[i].getAddress());
		}
		
		InternetAddress[] ccs = (InternetAddress[])msg.getRecipients(RecipientType.TO);
		mailBean.setCcAddresses(new ArrayList<String>());
		for (int i = 0; i < ccs.length; i++) {
			mailBean.getCcAddresses().add(ccs[i].getAddress());
		}
		
		InternetAddress[] bccs = (InternetAddress[])msg.getRecipients(RecipientType.TO);
		mailBean.setBccAddresses(new ArrayList<String>());
		for (int i = 0; i < bccs.length; i++) {
			mailBean.getBccAddresses().add(bccs[i].getAddress());
		}
		
		InternetAddress[] replays = (InternetAddress[])msg.getReplyTo();
		mailBean.setReplyToAddresses(new ArrayList<String>());
		for (int i = 0; i < replays.length; i++) {
			mailBean.getReplyToAddresses().add(replays[i].getAddress());
		}
		
		if(msg.getHeader("Disposition-Notification-TO") != null
				&& msg.getHeader("Disposition-Notification-TO").length > 0){
			mailBean.setReceipt(true);
		}
		
		@SuppressWarnings("unchecked")
		Enumeration<Header> headers = (Enumeration<Header>)msg.getAllHeaders();
		mailBean.setHeaders(new HashMap<String,String>());
		while (headers.hasMoreElements()) {
			Header header = headers.nextElement();
			mailBean.getHeaders().put(header.getName(), header.getValue());
		}
		
		mailBean.setAttachments(new HashMap<String,File>());
		mailBean.setInnerResources(new HashMap<String,File>());
		
		try {
			_convertMessageBody((Part)msg,mailBean);
		} catch (IOException e) {
			logger.error("获取邮件内容详情错误!", e);
		}
		
		return mailBean;
	}
	
	private void _convertMessageBody(Part part,MailBean mailBean) throws MessagingException, IOException {
		if(part.isMimeType("text/plain")){
			mailBean.setContentText(part.getContent().toString());
    	}else if(part.isMimeType("text/html")){
    		mailBean.setContentHtml(part.getContent().toString());
    	}else if(part.isMimeType("multipart/*")){
    		Multipart multipart=(Multipart)part.getContent();
    		for (int i = 0; i < multipart.getCount(); i++) {
    			_convertMessageBody((Part)multipart.getBodyPart(i),mailBean);
			}
    	}else if(part.isMimeType("message/rfc822")){
    		_convertMessageBody((Part)part.getContent(),mailBean);
    	}else if(part instanceof MimeBodyPart){
    		String disposition = part.getDisposition();
    		String contentId = ((MimeBodyPart) part).getContentID();
    		
    		if(contentId != null){
    			contentId = contentId.replaceAll("^<|>$", "");
    		}
    		
    		if((disposition != null && (Part.INLINE.equalsIgnoreCase(disposition) 
    				|| Part.ATTACHMENT.equalsIgnoreCase(disposition)))
    				|| contentId != null){
    			String fileName = part.getFileName();
    			if(null != fileName){
    				fileName = MimeUtility.decodeText(fileName);
    			}
    			
    			File file = null;
    			
    			if(Part.INLINE.equalsIgnoreCase(disposition) || contentId != null){
    				file = new File(resourceDirPath + File.separatorChar + contentId);
    				mailBean.getInnerResources().put(contentId, file);
    			}else if(Part.ATTACHMENT.equalsIgnoreCase(disposition)){
    				file = new File(resourceDirPath + File.separatorChar + fileName);
    				mailBean.getAttachments().put(fileName, file);
    			}
    			
    			if(downloadFile){
    				new File(resourceDirPath).mkdirs();
    				((MimeBodyPart)part).saveFile(file);
    			}
    		}
    	}
	}

	@Override
	protected Session getSession() {
		Properties props = new Properties();
    	Session session = Session.getInstance(props);
		return session;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	public boolean isDownloadFile() {
		return downloadFile;
	}

	public void setDownloadFile(boolean downloadFile) {
		this.downloadFile = downloadFile;
	}

	public String getResourceDirPath() {
		return resourceDirPath;
	}

	public void setResourceDirPath(String resourceDirPath) {
		this.resourceDirPath = resourceDirPath;
	}
	public MailListener getListener() {
		return listener;
	}

	public void setListener(MailListener listener) {
		this.listener = listener;
	}

	public SearchTerm getSearchTerm() {
		return searchTerm;
	}

	public void setSearchTerm(SearchTerm searchTerm) {
		this.searchTerm = searchTerm;
	}
	
}
