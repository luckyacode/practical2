package com.praveen.practical2.order;


import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class OrderService {

    @Autowired
    private JmsTemplate jmsTemplate;

    @SneakyThrows
    public String processOrders(String loanId) {
        try {
            CamelContext context = new DefaultCamelContext();
            ActiveMQComponent amq = new ActiveMQComponent();
            amq.setBrokerURL("tcp://localhost:61616");
            amq.setUsePooledConnection(false);
            context.addComponent("activemq", amq);
            context.addRoutes(new OrderProcess());
            context.start();
            Thread.sleep(5000);
            context.stop();
        }catch (Exception e){
            System.out.println("Exception "+e.getMessage());
        }
        return "Success Order Complete";
    }
}