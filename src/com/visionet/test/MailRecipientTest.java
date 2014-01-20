package com.visionet.test;

import java.util.List;
import java.util.Properties;
import javax.mail.Flags;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Store;
import com.visionet.mail.MailListener;
import com.visionet.mail.MailService;
import com.visionet.mail.MailBean;
import com.visionet.mail.MailRecipient;

public class MailRecipientTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Properties prop = new Properties();
//		prop.put(MailService.PORT, "143");//默认为143端口
//		prop.put(MailService.PROTOCOL, "imap");//默认为imap协议
		prop.put(MailService.HOST, "imap.163.com");
		prop.put(MailService.USERNAME, "1989ti.anyi@163.com");
		prop.put(MailService.USERPWD, "w13783208079m");
		MailRecipient recipient = new MailRecipient(prop);
		recipient.setDebug(true);
		recipient.setDownloadFile(true);
		recipient.setResourceDirPath("e:\\emaildir");
		
		//Flags flags = new Flags(Flags.Flag.RECENT);//未读
		//SearchTerm searchTerm = new FlagTerm(flags,true);
		//或者
		//SearchTerm searchTerm = new MessageIDTerm("<tencent_75378A790103137823D04F24@qq.com>");
		//recipient.setSearchTerm(searchTerm);
		
		recipient.setListener(new MailListener() {
			
			public boolean each(Message msg, MailBean mailBean) {
				try {
					msg.setFlag(Flags.Flag.SEEN, true);
//					msg.setFlag(Flags.Flag.DELETED, true);
				} catch (MessagingException e) {
					e.printStackTrace();
				}
				
				return true;
			}
			
			public boolean connect(Store store){
				return true;
			}
		});
		
		try {
			List<MailBean> mailBeanList = recipient.recipient();
			System.out.println("mailCount:" + mailBeanList.size());
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

}
