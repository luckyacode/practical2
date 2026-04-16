package com.praveen.practical2.order;


import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class FraudDetectService {

    @JmsListener(destination = "fraud.request")
    @SendTo("queue:fraud.replies.fallback") // Spring returns the result to the JMSReplyTo header set by Camel
    public String processFraudDetection(String orderDetails) {
        System.out.println("Processing fraud detection for: " + orderDetails);

        // Logic to verify payment...
        boolean isSuccess = (new Random()).nextBoolean();

        if (isSuccess) {
            System.out.println("Congrat's, Geniune Order....");
            return "Geniune Order";
        } else {
            System.out.println("Fraud Alert , order failed");
            return "Fraud Order";
        }
    }
}