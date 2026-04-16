package com.praveen.practical2.order.learn;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ProviderBService {

    @JmsListener(destination = "provider.B")
    @SendTo("queue:provider.B.replies.fallback") // Spring returns the result to the JMSReplyTo header set by Camel
    public String processProviderB(String orderDetails) {
        System.out.println("Processing provider.B  for: " + orderDetails);

        // Logic to verify payment...
        boolean isSuccess = (new Random()).nextBoolean();

        if (isSuccess) {
            System.out.println("provider.B Processed Success ....");
            return "provider.B SUCCESS";
        } else {
            System.out.println("provider.B,  failed");
            return "provider.B FAILED";
        }
    }
}