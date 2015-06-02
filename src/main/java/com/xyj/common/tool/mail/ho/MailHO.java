package com.xyj.common.tool.mail.ho;

import com.xyj.common.tool.mail.bean.MailBean;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.Properties;



/**
 * @className:MailHO.java
 * @classDescription:邮件发送操作类，采用单例模式
 * @author:xiayingjie
 * @createTime:Apr 1, 2008
 */
public class MailHO {

	private Properties pro = System.getProperties();// 属性

	private MimeMessage mimeMessage = null; // MIME邮件对象

	private Session session = null; // 邮件会话对象

	private MimeBodyPart bodyPart = null;

	private Multipart multipart = null; // Multipart对象,邮件内容,标题,附件等内容均添加到其中后再生成MimeMessage对象

	private static MailHO mailHO = new MailHO();

	/**
	 * 构造函数私有，单例模式
	 * 
	 */
	private MailHO() {
	}

	/**
	 * 获取发送会话
	 * 
	 * @param mail
	 *            邮件对象
	 *            false为匿名，true为认证
	 * @return javax.mail.Session对象
	 */
	public Session getSession(final MailBean mail) {
		// 发送邮件的一些协议，邮件地址，端口
		pro.put("mail.transport.protocol", "smtp");
		pro.put("mail.smtp.host", mail.getMailHost());
		pro.put("mail.smtp.port", "25");

		// 匿名获取会话
		if (!mail.isNeed()) {
			// 设置认证为false
			pro.put("mail.smtp.auth", "false");
			session = Session.getDefaultInstance(pro, null);
		}
		// 认证获取会话
		else {
			// 设置认证为true
			pro.put("mail.smtp.auth", "true");
			try {
				session = Session.getDefaultInstance(pro, new Authenticator() {
					public PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(mail.getUserName(),
								mail.getPassword());
					}
				});
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return session;

	}

	/**
	 * 获取MimeMessage对象
	 * 
	 * @param session
	 * @return
	 */
	public MimeMessage getMimeMessage(Session session) {
		try {
			mimeMessage = new MimeMessage(session); // 创建MIME邮件对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mimeMessage;
	}

	/**
	 * 获取MultiPart对象
	 * 
	 * @return javax.mail.Multipart对象
	 */
	public Multipart getMultPart() {
		try {
			multipart = new MimeMultipart();// 创建Multpart对象
		} catch (Exception e) {
			e.printStackTrace();
		}
		return multipart;
	}

	/**
	 * 设置邮件主题
	 * 
	 * @param subjectName
	 * @throws javax.mail.MessagingException
	 */
	public void setSubjectName(String subjectName) throws MessagingException {

		mimeMessage.setSubject(subjectName);

	}

	/**
	 * 设置发件人地址
	 * 
	 * @param mailFromAddress
	 * @throws javax.mail.MessagingException
	 */
	public void setSendMail(String mailFromAddress) throws MessagingException {

		mimeMessage.setFrom(new InternetAddress(mailFromAddress));

	}

	/**
	 * 设置收件人地址，收件人类型为to,cc,bcc(大小写不限)
	 * 
	 * @param inceptStr
	 *            收件人地址
	 * @param mailType
	 *            收件类型
	 * @throws javax.mail.MessagingException
	 */
	public void setInceptMail(String inceptStr[], String mailType)
			throws MessagingException {

		// 收件人InternetAddress
		InternetAddress mailToAddress = null;

		// 为MimeMultipart对象设置收件人（to,cc,bbc）
		for (int i = 0; i < inceptStr.length; i++) {

			try {
				mailToAddress = new InternetAddress(inceptStr[i]);
				// 假如传入的值不是to,cc,bcc,那么默认为to
				if (mailType.equalsIgnoreCase("to")) {
					mimeMessage.addRecipient(Message.RecipientType.TO,
							mailToAddress);

				} else if (mailType.equalsIgnoreCase("cc")) {
					mimeMessage.addRecipient(Message.RecipientType.CC,
							mailToAddress);
				} else if (mailType.equalsIgnoreCase("bcc")) {
					mimeMessage.addRecipient(Message.RecipientType.BCC,
							mailToAddress);
				} else {
					mimeMessage.addRecipient(Message.RecipientType.TO,
							mailToAddress);
				}
			} catch (Exception exx) {
				exx.printStackTrace();
			}

		}

	}

	/**
	 * 设置邮件发送日期
	 * 
	 * @param mailsendDate
	 *            发送邮件时间
	 * @throws javax.mail.MessagingException
	 */
	public void setSendDate(Date mailsendDate) throws MessagingException {
		mimeMessage.setSentDate(mailsendDate);
	}

	/**
	 * 设置邮件发送文本
	 * 
	 * @param mailContent
	 */
	public void setMailContent(String mailContent) {
		bodyPart = new MimeBodyPart();
		try {
			bodyPart.setText(mailContent);
			multipart.addBodyPart(bodyPart);
		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * 设置邮件发送附件
	 * 
	 * @param attaches
	 * @return
	 * @throws javax.mail.MessagingException
	 */
	public void setAccessories(String[] attaches) throws MessagingException {

		FileDataSource file = null;
		// 遍历附件,将附件添加到MimeMultipart对象
		for (int i = 0; i < attaches.length; i++) {
			bodyPart = new MimeBodyPart();
			DataSource source = new FileDataSource(attaches[i].trim());
			file = new FileDataSource(attaches[i]);

			try {
				bodyPart.setDataHandler(new DataHandler(source));
				// 获取文件名
				int index = 0;
				String accessoriesName = null;
				if (attaches[i].lastIndexOf("//") > 0) {
					index = attaches[i].lastIndexOf("//");
					accessoriesName = attaches[i].substring(index + 2);
				} else if (attaches[i].lastIndexOf("\\") > 0) {
					index = attaches[i].lastIndexOf("\\");
					accessoriesName = attaches[i].substring(index + 1);
				} else {
					accessoriesName = "attache" + i;
				}
				// 一般的邮件服务器都是unix默认编码是iso8859-1，否则在服务器上面会出现乱码
				try {
					accessoriesName = new String(accessoriesName.getBytes(),
							"iso8859-1");
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				bodyPart.setFileName(accessoriesName);
				multipart.addBodyPart(bodyPart);
			} catch (MessagingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * 设置邮件内容
	 * 
	 * @param multipart
	 */
	public void setMailContent(Multipart multipart) {
		try {
			mimeMessage.setContent(multipart);
		} catch (MessagingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 发送邮件
	 * 
	 * @param mail
	 *            邮件对象
	 * @return
	 */
	public boolean sendMail(MailBean mail) {
		boolean flag = false;
		try {
			// 获取邮件session
			this.getSession(mail);
			// 获取MimeMessage对象
			this.getMimeMessage(session);
			// 获取MultiPart对象
			this.getMultPart();

			// 设置发件人
			this.setSendMail(mail.getSendMail());
			// 设置主题
			this.setSubjectName(mail.getSubjectName());
			// 设置收件人

			this.setInceptMail(mail.getInceptStr(), "to");

			// 设置抄送人
			if (mail.getCopyStr() != null) {
				this.setInceptMail(mail.getInceptStr(), "cc");
			}

			// 设置密送人
			if (mail.getSecretStr() != null) {
				this.setInceptMail(mail.getInceptStr(), "bcc");
			}

			// 设置邮件内容
			this.setMailContent(mail.getMailContent());

			// 设置附件
			if (mail.getAttaches() != null) {
				this.setAccessories(mail.getAttaches());
			}
			// 将mutipart放入邮件当中
			this.setMailContent(multipart);
			// 发送邮件
			Transport.send(mimeMessage);
			flag = true;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return flag;
	}

	/**
	 * 获取mailHO对象 (饿汉模式)缺点：每次访问都必须构建一个对象.优点：安全
	 * 
	 * @return MailHO
	 */
	public static MailHO getMailHOInstance() {
		return mailHO;
	}

}

// ---------------懒汉模式
// /**
// * 获取mailHO对象 (懒汉模式)缺点：同步导致其不安全.优点:不必每次请求都创建一个对象
// * @return MailHO
// */
// private MailHO mailHO=null;
// public synchronized static MailHO getLazyMailHOInstance(){
// if(mailHO==null){
// return new MailHO();
// }
// return mailHO;
//
//
// }
//

// 登记模式，缺点:
// 1.由于子类必须允许父类以构造子调用产生实例,因此，它的构造方法必须是公开的
// 2.由于父类的实例必须存在才可能有子类的实例，这在有些情况下是一个浪费
// static private HashMap register = new HashMap();
// static
// {
// MailHO mailHO = new MailHO();
// register.put(mailHO.getClass().getName(), mailHO);
// }
//
// /**
// * 保护的默认构造子
// */
// protected mailHO()
// {
// }
//
// /**
// * 静态工厂方法，返还此类惟一的实例
// */
// static public MailHO getInstance(String name)
// {
// if (name == null)
// {
// name = "com.common.mail.ho.MailHO";
// }
// if (register.get(name) == null)
// {
// try
// {
// register.put(name, Class.forName(name).newInstance());
// }
// catch (Exception e)
// {
// System.out.println("Error happened.");
// }
// }
// return (MailHO) (register.get(name));
// }
// public class RegSingletonChild extends MailHO
// {
// public RegSingletonChild()
// {
// }
//
// /**
// * 静态工厂方法
// */
// static public RegSingletonChild getInstance()
// {
// return (RegSingletonChild)
// RegSingleton.getInstance("com.javapatterns.singleton.demos.RegSingletonChild");
// }
//
// }
