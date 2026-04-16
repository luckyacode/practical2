package com.praveen.practical2.order.learn;

import jakarta.jms.Message;
import lombok.SneakyThrows;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class ProviderAService {

    @SneakyThrows
    @JmsListener(destination = "provider.A")
    @SendTo("queue:provider.A.replies.fallback") // Spring returns the result to the JMSReplyTo header set by Camel
    public String processProviderA(String orderDetails, Message message) {
        System.out.println("Processing provider.A  for: " + orderDetails);
        System.out.println("process message corelationId "+message.getJMSCorrelationID());
        // Logic to verify payment...
        boolean isSuccess = (new Random()).nextBoolean();

        if (isSuccess) {
            System.out.println("provider.A Processed Success ....");
            return "provider.A SUCCESS";
        } else {
            System.out.println("provider.A,  failed");
            return "provider.A FAILED";
        }
    }
}