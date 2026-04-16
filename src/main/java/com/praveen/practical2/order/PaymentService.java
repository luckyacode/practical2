package com.praveen.practical2.order;

import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Service;

@Service
public class PaymentService {

    @JmsListener(destination = "payment.request")
    @SendTo("queue:payment.replies.fallback") // Spring returns the result to the JMSReplyTo header set by Camel
    public String processPayment(String orderDetails) {
        System.out.println("Processing payment for: " + orderDetails);

        // Logic to verify payment...
        boolean isSuccess = true;

        if (isSuccess) {
            return "PAYMENT_SUCCESS_REF_999";
        } else {
            return "PAYMENT_REJECTED_INSUFFICIENT_FUNDS";
        }
    }
}