package com.xyj.common.tool.mail.bean;

/**
 * @className:MailDTO.java
 * @classDescription:邮件对象
 * @author:xiayingjie
 * @createTime:Mar 31, 2008
 */
public class MailBean {
	private String userName;/*--发件人姓名*/

	private String password;/*--发件人密码*/

	private String sendMail; /*--发件人*/

	private String[] inceptStr;/*--收件人*/

	private String[] copyStr;/*--抄送人*/

	private String[] secretStr;/*--秘送人*/

	private String subjectName;/*--主题名*/

	private String[] attaches; /*--附件*/

	private String mailContent;/*--邮件内容*/

	private String mailHost; /*--邮件服务器*/

	private boolean need; /*--是否需要认证*/

	public boolean isNeed() {
		return need;
	}

	public void setNeed(boolean need) {
		this.need = need;
	}

	public String[] getAttaches() {
		return attaches;
	}

	public void setAttaches(String[] attaches) {
		this.attaches = attaches;
	}

	public String[] getCopyStr() {
		return copyStr;
	}

	public void setCopyStr(String[] copyStr) {
		this.copyStr = copyStr;
	}

	public String[] getInceptStr() {
		return inceptStr;
	}

	public void setInceptStr(String[] inceptStr) {
		this.inceptStr = inceptStr;
	}

	public String getMailContent() {
		return mailContent;
	}

	public void setMailContent(String mailContent) {
		this.mailContent = mailContent;
	}

	public String getMailHost() {
		return mailHost;
	}

	public void setMailHost(String mailHost) {
		this.mailHost = mailHost;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String[] getSecretStr() {
		return secretStr;
	}

	public void setSecretStr(String[] secretStr) {
		this.secretStr = secretStr;
	}

	public String getSendMail() {
		return sendMail;
	}

	public void setSendMail(String sendMail) {
		this.sendMail = sendMail;
	}

	public String getSubjectName() {
		return subjectName;
	}

	public void setSubjectName(String subjectName) {
		this.subjectName = subjectName;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

}
