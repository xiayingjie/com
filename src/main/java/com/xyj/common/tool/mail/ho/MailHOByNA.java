package com.xyj.common.tool.mail.ho;

//import java.io.InputStream;
//import java.io.OutputStream;
//import java.net.InetAddress;
//import java.net.Socket;
//import java.util.Hashtable;
//
//import javax.naming.NamingEnumeration;
//import javax.naming.directory.Attributes;
//import javax.naming.directory.DirContext;
//import javax.naming.directory.InitialDirContext;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.naming.*;
import javax.naming.directory.*;

/**
 * @className:MailHOByNA.java
 * @classDescription:邮件处理对象，不需要认证，直接通过smtp发送 NoAuthentication
 * @author:xiayingjie
 * @createTime:Apr 1, 2008
 */
public class MailHOByNA {

	String dns = null;

	/**
	 * 查找目标邮件的邮件服务器
	 * 
	 * @param inceptMail
	 * @return
	 */
	public void getDNS(String inceptMail) throws Exception {

		// 获取邮箱后缀
		String domain = inceptMail.substring(inceptMail.indexOf("@") + 1,
				inceptMail.length());
		Hashtable env = new Hashtable();
		env.put("java.naming.factory.initial",
				"com.sun.jndi.dns.DnsContextFactory");
		// env.put("java.naming.provider.url", dns);
		DirContext ctx = new InitialDirContext(env);
		Attributes attr = ctx.getAttributes(domain, new String[] { "mx" });
		NamingEnumeration servers = attr.getAll();
		int i = 99999;
		// 列出所有邮件服务器：
		while (servers.hasMore()) {
			dns = servers.next().toString();
		}

	}

	public void sendMailBySmtp() throws Exception {
		String END_FLAG = "\r\n";
		String mx = "mx.mail.163.split.netease.com";
		InetAddress addr = InetAddress.getByName(mx);
		Socket socket = new Socket(addr, 25);

		InputStream in = socket.getInputStream();
		OutputStream out = socket.getOutputStream();

		// 连接成功后服务器会响应：
		response(in);

		// 首先发送HELO命令：
		send("HELO www.google.com" + END_FLAG, out);
		response(in);

		// 然后发送发件人地址：
		send("MAIL FROM: xiaoxin5230@163.com" + END_FLAG, out);
		response(in);

		// 设置收件人地址：
		send("RCPT TO: jiege82000@163.com" + END_FLAG, out);
		response(in);

		// 开始发送邮件正文：
		send("DATA" + END_FLAG, out);
		response(in);

		send("From: xiaoxin5230@163.com" + END_FLAG, out);
		send("To:jiege82000@163.com" + END_FLAG, out);
		send("Subject: yiwaiba" + END_FLAG, out);
		send("Content-Type: text/plain;" + END_FLAG, out);
		send(END_FLAG + END_FLAG, out);

		// 发送邮件正文，如果用中文，需要BASE64编码：
		send("text message body!" + END_FLAG, out);
		// 每行以\r\n结束，不可过长，可拆成多行。

		// 以"\r\n.\r\n"作为结束标志：
		send(END_FLAG + "." + END_FLAG, out);
		response(in);

		// 结束并确认发送：
		send("QUIT" + END_FLAG, out);
		response(in);
		in.close();
		out.close();
		socket.close();

	}

	public void response(InputStream in) throws Exception {
		byte[] buffer = new byte[1024];
		int n = in.read(buffer);
		String s = new String(buffer, 0, n);
		// 服务器会返回：### Text
		// 具体含义见RFC821

	}

	public void send(String s, OutputStream out) throws Exception {
		byte[] buffer = s.getBytes();
		out.write(buffer);
		// 不要忘了flush()，否则可能在缓冲区：
		out.flush();
	}

	public static void main(String[] args) {
		MailHOByNA mailHO = new MailHOByNA();
		try {
			mailHO.getDNS("jiege82000@163.com");
			mailHO.sendMailBySmtp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
