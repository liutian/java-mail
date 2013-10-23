package com.visionet.mail;

import javax.mail.Message;
import javax.mail.Store;

public class MailListener {
	public void each(Message msg,MailBean mailBean){}
	
	public boolean connect(Store store){return true;}
}
