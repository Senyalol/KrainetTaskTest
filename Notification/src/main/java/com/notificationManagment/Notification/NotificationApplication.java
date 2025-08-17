package com.notificationManagment.Notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
//import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


@SpringBootApplication
public class NotificationApplication {

	public static void main(String[] args) throws IOException, MessagingException {
		SpringApplication.run(NotificationApplication.class, args); // Запуск приложения

//		FileInputStream fileInputStream = new FileInputStream("src/config.properties");
//		Properties properties = new Properties();
//		properties.setProperty("smtp.host", "smtp.gmail.com");
//
//		String user = properties.getProperty("mail.user");
//		String password = properties.getProperty("mail.password");
//		String host = properties.getProperty("mail.host");
//
//		Properties prop = new Properties();
//		prop.put("mail.store.protocol","imaps");
//
//		Store store = Session.getInstance(prop).getStore();
//		store.connect(host, user, password);
//
//		Folder inbox = store.getFolder("INBOX");
//		inbox.open(Folder.READ_ONLY);
//
//		System.out.println("Колличество писем на почте " + inbox.getMessageCount());


		String user = "MrSimonCar@yandex.ru"; // Ваш логин
		String password = "vnotnpfiepbfqiev";

		Properties properties = new Properties();
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", "smtp.yandex.ru");
		properties.put("mail.smtp.port", "587");

		Session session = Session.getInstance(properties, new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(user, password);
			}
		});

		try {
			// Создание сообщения
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(user));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress("naumenko.sema@internet.ru"));
			message.setSubject("Salam mama!");
			message.setText("Салам алекум , Ега");

			// Отправка сообщения
			Transport.send(message);
			System.out.println("Сообщение успешно отправлено!");
		} catch (MessagingException e) {
			e.printStackTrace();
		}

		// Настройка и отправка письма
//		String to = "MrSimonCar@ya.ru";
//		String from = "MrSimonCar@ya.ru";
//		String host = "smtp.yandex.com";
//
//		Properties properties = System.getProperties();
//		properties.setProperty("mail.smtp.host", host);
//		properties.setProperty("mail.smtp.port", "587");
//		properties.setProperty("mail.smtp.auth", "true");
//		properties.setProperty("mail.smtp.starttls.enable", "true");
//
//		Session session = Session.getDefaultInstance(properties, new Authenticator() {
//			protected PasswordAuthentication getPasswordAuthentication() {
//				return new PasswordAuthentication("MrSimonCar@yandex.ru", "MisterSimonBOG");
//			}
//		});
//
//		try {
//			MimeMessage message = new MimeMessage(session);
//			message.setFrom(new InternetAddress(from));
//			message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
//			message.setSubject("Важное сообщение!");
//			message.setText("Salam mama!");
//
//			System.setProperty("https.protocols", "TLSv1.2");
//			Transport.send(message);
//			System.out.println("Sent message successfully....");
//
//		} catch (AddressException e) {
//			throw new RuntimeException(e);
//		} catch (MessagingException e) {
//			throw new RuntimeException(e);
//		}
	}
}