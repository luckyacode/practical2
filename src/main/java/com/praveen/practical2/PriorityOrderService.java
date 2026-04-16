//package com.praveen.practical2;
//
//import org.springframework.jms.annotation.JmsListener;
//import org.springframework.messaging.handler.annotation.SendTo;
//import org.springframework.stereotype.Service;
//
//@Service
//public class PriorityOrderService {
//
//    @JmsListener(destination = "orders.priority.high")
//    @SendTo // This tells Spring to send the return value back to the JMSReplyTo header
//    public String handleHighPriorityOrder(String orderBody) {
//        System.out.println("Processing High Priority: " + orderBody);
//
//        // This String is what the original RouteDefinition will receive as the new ${body}
//        return "Acknowledged: High Priority processing complete for " + orderBody;
//    }
//}