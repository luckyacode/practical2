package com.praveen.practical2.order;

import com.praveen.practical2.order.learn.OrchestrationRoute;
import lombok.SneakyThrows;
import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;


@Service
public class OrchestrationService {

        @Autowired
        private JmsTemplate jmsTemplate;

        public String sendApplication(String msg) {
            System.out.println("Sending requestEndpointMsg: " + msg);
            jmsTemplate.convertAndSend("requestEndPoint",   msg);
            return "Success Process Now";
        }


    @SneakyThrows
        public String processOrders(String loanId) {
            try {
                CamelContext context = new DefaultCamelContext();
                ActiveMQComponent amq = new ActiveMQComponent();
                amq.setBrokerURL("tcp://localhost:61616");
                amq.setUsePooledConnection(false);
                amq.setUseMessageIDAsCorrelationID(true);
                context.addComponent("activemq", amq);
                context.addRoutes(new OrchestrationRoute());
                context.start();
                Thread.sleep(5000);
                context.stop();
            }catch (Exception e){
                System.out.println("Exception "+e.getMessage());
            }
            return "Success Orchestration Complete";
        }
    }