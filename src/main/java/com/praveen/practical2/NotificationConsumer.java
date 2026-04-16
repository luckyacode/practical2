package com.praveen.practical2;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificationConsumer {
    @JmsListener(destination = "loan-queue")
    public void receiveMessage(String message) {
        System.out.println("Received from ActiveMQ: " + message);
        // Logic to send email or SMS here
    }
}