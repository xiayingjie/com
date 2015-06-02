package com.xyj.common.tool.mail;


import com.xyj.common.tool.mail.bean.MailBean;

/**
 * @className:ISendMail.java
 * @classDescription:对邮件的发送
 * @author:xiayingjie
 * @createTime:Mar 31, 2008
 */
public interface ISendMail {
	/**
	 * 发送文本邮件 注：一般使用163之类的邮件服务器都需要认证，另外就是有些邮件服务器禁止smtp协议
	 * 
	 * @param mail
	 *            是否需要认证
	 * @return boolean
	 */
	public boolean sendMailByText(MailBean mail);
}
