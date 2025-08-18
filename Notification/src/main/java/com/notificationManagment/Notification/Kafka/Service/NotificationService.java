package com.notificationManagment.Notification.Kafka.Service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@Service
public class NotificationService {

    private static final Logger LOGGER = LogManager.getLogger(NotificationService.class);

    @Value("${mail.username}")
    private String user;

    @Value("${mail.password}")
    private String password;

    @Value("${mail.smtp.auth}")
    private String smtpAuth;

    @Value("${mail.smtp.starttls.enable}")
    private String starttlsEnable;

    @Value("${mail.smtp.host}")
    private String smtpHost;

    @Value("${mail.smtp.port}")
    private String smtpPort;

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

        Properties properties = new Properties();
        properties.put("mail.smtp.auth", smtpAuth);
        properties.put("mail.smtp.starttls.enable", starttlsEnable);
        properties.put("mail.smtp.host", smtpHost);
        properties.put("mail.smtp.port", smtpPort);

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

            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(user));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(receiver));
            message.setSubject(eTopic);
            message.setText(eMessage);

            Transport.send(message);
            LOGGER.info("Сообщение успешно отправлено! Получатель: {}",receiver);
        } catch (MessagingException e) {
            LOGGER.error("Send email failed {}", e.getMessage());
        }

    }

}
