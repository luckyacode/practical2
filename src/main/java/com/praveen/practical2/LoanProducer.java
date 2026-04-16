package com.praveen.practical2;

import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoanProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public String sendApplication(String loanId) {
        System.out.println("Sending loan: " + loanId);
        jmsTemplate.convertAndSend("loan-queue", "New Loan Application: " + loanId);
        return "Success send loan";
    }

    public String sendApplication2(String loanId) {
        System.out.println("input.orders sending.... " + loanId);
        jmsTemplate.convertAndSend("input.orders", "New input order Application: " + loanId, message -> {
            // Setting a custom String property for your Camel Choice logic
            message.setStringProperty("Priority", "High");
            // You can also set standard JMS headers if needed
            message.setJMSType("HighPriorityOrder");
            return message;
        });
        return "Success sent";
    }


    public String test(String loanId) {
        try {
            CamelContext context = new DefaultCamelContext();
            ActiveMQComponent amq = new ActiveMQComponent();
            amq.setBrokerURL("tcp://localhost:61616");
            amq.setUsePooledConnection(false);
            context.addComponent("activemq", amq);
            context.addRoutes(new OrderRoutingLogic());
            context.start();
            Thread.sleep(5000);
            context.stop();
        }catch(Exception e){
            System.out.println("Exception ");
        }
        return "Success Test";
    }
}