package com.visionet.mail;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Map;

public class MailBean implements Cloneable{
	//邮件标示符
	private String messageID;
	//邮件主题
	private String subject;
	//邮件内容
	private String content;
	private String contentText;
	private String contentHtml;
	//邮件发送人
	private String sender;
	//邮件发送日期
	private Date senderDate;
	//收件人
	private List<String> toAddresses;
	//抄送人
	private List<String> ccAddresses;
	//暗送人
	private List<String> bccAddresses;
	//邮件回复人
	private List<String> replyToAddresses;
	//邮件附件
	private Map<String,File> attachments;
	//内嵌资源
	private Map<String,File> innerResources;
	private Map<String,String> headers;
	//被本邮件回复的邮件的messageID
	private String inReplyTo;
	//是否回执邮件
	private boolean receipt;
	
	public String getMessageID() {
		return messageID;
	}
	public void setMessageID(String messageID) {
		this.messageID = messageID;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Date getSenderDate() {
		return senderDate;
	}
	public void setSenderDate(Date senderDate) {
		this.senderDate = senderDate;
	}
	public String getInReplyTo() {
		return inReplyTo;
	}
	public void setInReplyTo(String inReplyTo) {
		this.inReplyTo = inReplyTo;
	}
	public Map<String, String> getHeaders() {
		return headers;
	}
	public void setHeaders(Map<String, String> headers) {
		this.headers = headers;
	}
	public List<String> getToAddresses() {
		return toAddresses;
	}
	public void setToAddresses(List<String> toAddresses) {
		this.toAddresses = toAddresses;
	}
	public List<String> getCcAddresses() {
		return ccAddresses;
	}
	public void setCcAddresses(List<String> ccAddresses) {
		this.ccAddresses = ccAddresses;
	}
	public List<String> getBccAddresses() {
		return bccAddresses;
	}
	public void setBccAddresses(List<String> bccAddresses) {
		this.bccAddresses = bccAddresses;
	}
	public List<String> getReplyToAddresses() {
		return replyToAddresses;
	}
	public void setReplyToAddresses(List<String> replyToAddresses) {
		this.replyToAddresses = replyToAddresses;
	}
	public Map<String, File> getAttachments() {
		return attachments;
	}
	public void setAttachments(Map<String, File> attachments) {
		this.attachments = attachments;
	}
	public Map<String, File> getInnerResources() {
		return innerResources;
	}
	public void setInnerResources(Map<String, File> innerResources) {
		this.innerResources = innerResources;
	}
	public String getContentText() {
		return contentText;
	}
	public void setContentText(String contentText) {
		this.contentText = contentText;
	}
	public String getContentHtml() {
		return contentHtml;
	}
	public void setContentHtml(String contentHtml) {
		this.contentHtml = contentHtml;
	}
	public boolean isReceipt() {
		return receipt;
	}
	public void setReceipt(boolean receipt) {
		this.receipt = receipt;
	}
	@Override
	protected Object clone() throws CloneNotSupportedException {
		return super.clone();
	}
}
