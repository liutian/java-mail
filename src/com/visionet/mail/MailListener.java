package com.visionet.mail;

import javax.mail.Message;
import javax.mail.Store;

public class MailListener {
	public boolean each(Message msg,MailBean mailBean){return true;}
	
	public boolean connect(Store store){return true;}
}
