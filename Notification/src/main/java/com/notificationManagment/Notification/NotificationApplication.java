package com.notificationManagment.Notification;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import javax.mail.*;
import java.io.IOException;



@SpringBootApplication
public class NotificationApplication {

	public static void main(String[] args) throws IOException, MessagingException {
		SpringApplication.run(NotificationApplication.class, args);
	}

}