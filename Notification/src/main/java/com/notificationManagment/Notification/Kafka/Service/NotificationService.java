package com.notificationManagment.Notification.Kafka.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class NotificationService {

    private static final Logger LOGGER = LogManager.getLogger(NotificationService.class);

    @KafkaListener(topics = "EmailMessage", groupId = "EmailNotificationGroup", containerFactory = "kafkaListenerContainerFactory")
    public void listenEmailKafka(String email) throws Exception {

        try{
            sendEmail(email);
        }

        catch (Exception e){
            LOGGER.error("Kafka listener failed {}", e.getMessage());
        }

    }

    private void sendEmail(String Emessage){
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

            String eTopic = Emessage.split("\\|")[0].trim();
            String eMessageTemp = Emessage.split("\\|")[1].trim();
            String eMessage = eMessageTemp.split(" receiver")[0].trim();
            String receiver =  Emessage.split("receiver ")[1].trim();

            // Создание сообщения
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(eTopic);
            message.setText(eMessage);

            // Отправка сообщения
            Transport.send(message);
            System.out.println("Сообщение успешно отправлено!");
        } catch (MessagingException e) {
            e.printStackTrace();
        }

    }

}
