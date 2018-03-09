package com.hundun.java.email;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Properties;

import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;

/**
 * @DESC send email
 * @author xinshiyou
 */
public class SendMail {

	private static final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
	private static final String FORM_NAME = "临时邮件通知中心";

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
	 * @throws UnsupportedEncodingException
	 */
	public static void sendMessage(String smtpHost, String from, String fromUserPassword, String to, String subject,
			String messageText, String messageType) throws MessagingException, UnsupportedEncodingException {

		// 第一步： 配置参数
		Properties props = new Properties();
		props.put("mail.smtp.host", smtpHost);
		props.put("mail.smtp.starttls.enable", "true");// 使用 STARTTLS安全连接
		props.put("mail.smtp.port", "587"); // google使用465或587端口
		// props.put("mail.smtp.socketFactory.port", "587");
		// props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
		props.put("mail.smtp.socketFactory.fallback", "false");
		props.put("mail.smtp.auth", "true"); // 使用验证
		Session mailSession = Session.getInstance(props, new EmailAuthenticator(from, fromUserPassword));
		mailSession.setDebug(true);

		// 第二步：编写消息
		InternetAddress fromAddress = new InternetAddress(MimeUtility.encodeText(FORM_NAME) + "<" + from + ">");
		MimeMessage message = new MimeMessage(mailSession);
		message.setFrom(fromAddress);

		String[] tos = to.split(",");
		InternetAddress[] toAdds = new InternetAddress[tos.length];
		InternetAddress toAddress = null;
		for (int i = 0; i < tos.length; i++) {
			toAddress = new InternetAddress(tos[i]);
			toAdds[i] = toAddress;
		}
		message.addRecipients(RecipientType.TO, toAdds);
		message.setSentDate(Calendar.getInstance().getTime());
		message.setSubject(subject);
		message.setContent(messageText, messageType);

		// 第三步：发送消息
		Transport transport = mailSession.getTransport("smtp");
		transport.connect(smtpHost, from, fromUserPassword);
		Transport.send(message, message.getRecipients(RecipientType.TO));
		transport.close();
		System.out.println("message yes");
	}

}
