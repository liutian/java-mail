package com.visionet.test;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.MessagingException;

import com.visionet.mail.MailService;
import com.visionet.mail.MailBean;
import com.visionet.mail.MailSender;

public class MailSenderTest {
	public static void main(String[] args) throws MessagingException {
		MailBean mailBean = new MailBean();
		
		Map<String,File> attachments = new HashMap<String,File>();
		attachments.put("a文件.xlsx", new File("E:/mailtest/1.xlsx"));
		attachments.put("b文件.txt", new File("E:/mailtest/2.txt"));
		attachments.put("c文件.zip", new File("E:/mailtest/3.zip"));
		attachments.put("d文件.rar", new File("E:/mailtest/4.rar"));
		attachments.put("e文件.pptx", new File("E:/mailtest/5.pptx"));
		attachments.put("f文件.docx", new File("E:/mailtest/6.docx"));
		attachments.put("g文件.7z", new File("E:/mailtest/7.7z"));
//		attachments.put("h文件.pdf", new File("e:/mailtest/9.pdf"));
		mailBean.setAttachments(attachments);
		
		List<String> toAddresses = new ArrayList<String>();
		toAddresses.add("1191577401@qq.com");
		mailBean.setToAddresses(toAddresses );
		
		List<String> bccAddresses = new ArrayList<String>();
		bccAddresses.add("1989ti.anyi@163.com");
		mailBean.setBccAddresses(bccAddresses );
		
		List<String> ccAddresses = new ArrayList<String>();
		ccAddresses.add("tianyi_2514@126.com");
		mailBean.setCcAddresses(ccAddresses );
		
		List<String> replyToAddresses = new ArrayList<String>();
		replyToAddresses.add("liussdne@gmail.com");
		replyToAddresses.add("liussssssdne@gmail.com");
		mailBean.setReplyToAddresses(replyToAddresses );
		
		Map<String,String> headers = new HashMap<String,String>();
		headers.put("X-ss", "我的");
		headers.put("X-ssf", "ss");
		mailBean.setHeaders(headers);
		
		Map<String, File> innerResources = new HashMap<String,File>();
		innerResources.put("abbdsd", new File("E:/mailtest/8.jpg"));
		mailBean.setInnerResources(innerResources);

		mailBean.setContent("<p>hello world!<p>内嵌资源<img src='cid:abbdsd'>aa");
		
//		mailBean.setSender("11@visionet.com.cn");//有些邮件服务器对发件人不是本域的邮件不予发送并返回550错误
		mailBean.setSender("1989ti.anyi@163.com");
		mailBean.setSenderDate(new Date());
		mailBean.setSubject("邮件email");
		
		Properties prop = new Properties();
		prop.setProperty(MailService.USERNAME, "1989ti.anyi@163.com");
		prop.setProperty(MailService.USERPWD, "w13783208079m");
		prop.setProperty(MailService.HOST, "smtp.163.com");
//		prop.setProperty(MailService.PROTOCOL, "smtp");
//		prop.setProperty(MailService.PORT, "25");
		MailSender sender = new MailSender(prop);
		mailBean.setReceipt(true);
		
		sender.sendAsyn(mailBean,sender.new SendCallBack(){
			@Override
			public void callBack() {
				System.out.println("异步发送邮件完毕!");
			}
		});
		sender.setDebug(true);
		
//		sender.send(mailBean);
//		System.out.println("发送完毕!");
	}
}
