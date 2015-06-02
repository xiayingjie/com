package com.xyj.common.tool.mail.imp;


import com.xyj.common.tool.mail.ISendMail;
import com.xyj.common.tool.mail.bean.MailBean;
import com.xyj.common.tool.mail.ho.MailHO;

/**
 * @className:SendMail.java
 * @classDescription:实现发送邮件
 * @author:xiayingjie
 * @createTime:Mar 31, 2008
 */
public class SendMail implements ISendMail {

	MailHO mailHO = MailHO.getMailHOInstance();

    /**
	 * 发送文本邮件 注：一般使用163之类的邮件服务器都需要认证，另外就是有些邮件服务器禁止smtp协议
	 * 
	 * @param mail
	 *            是否需要认证
	 * @return boolean
	 */
	public boolean sendMailByText(MailBean mail) {
		return mailHO.sendMail(mail);
	}

	public static void main(String[] args) {
		MailBean mail = new MailBean();
		// String[] attaches = { "F:\\aaaaaaaaaaaaaa.txt",
		// "F:\\java 基础Jdo是什么.txt" };
		// mail.setAttaches(attaches);
		// mail.setCopyStr(copyStr);
		mail.setMailContent("hello world");
		mail.setMailHost("smtp.163.com");
		mail.setPassword("xiayingjie");
		// mail.setSecretStr(secretStr);
		mail.setSendMail("jiege82000@163.com");
		mail.setSubjectName("附件发送测试");
		mail.setUserName("jiege82000@163.com");
		String[] inceptStr = { "jiege82000@163.com", "xiaoxin5230@163.com" };
		mail.setInceptStr(inceptStr);

		new SendMail().sendMailByText(mail);

	}
}
