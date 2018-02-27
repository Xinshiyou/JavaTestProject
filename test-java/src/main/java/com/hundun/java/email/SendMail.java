package com.hundun.java.email;

import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMail {

	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";

	/**
	 * @DESC send email
	 * @param smtpHost
	 * @param from
	 * @param fromUserPassword
	 * @param to
	 * @param subject
	 * @param messageText
	 * @param messageType
	 * @throws MessagingException
	 */
	public static void sendMessage(String smtpHost, String from, String fromUserPassword, String to, String subject,
			String messageText, String messageType) throws MessagingException {

		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.starttls.enable", "true");// 使用 STARTTLS安全连接
		props.put("mail.smtp.port", "465"); // google使用465或587端口
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.auth", "true"); // 使用验证
		Session mailSession = Session.getInstance(props, new EmailAuthenticator(from, fromUserPassword));

		// 第二步：编写消息
		System.out.println("编写消息from——to:" + from + "——" + to);

		InternetAddress fromAddress = new InternetAddress(from);
		InternetAddress toAddress = new InternetAddress(to);
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(fromAddress);
		message.addRecipient(RecipientType.TO, toAddress);

		message.setSentDate(Calendar.getInstance().getTime());
		message.setSubject(subject);
		message.setContent(messageText, messageType);

		// 第三步：发送消息
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(smtpHost, from, fromUserPassword);
		Transport.send(message, message.getRecipients(RecipientType.TO));
		System.out.println("message yes");
	}

}
