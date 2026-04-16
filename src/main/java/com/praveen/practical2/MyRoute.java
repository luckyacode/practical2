package com.praveen.practical2;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class MyRoute {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        // Configure the ActiveMQ component
        ActiveMQComponent amq = new ActiveMQComponent();
        amq.setBrokerURL("tcp://localhost:61616");
        amq.setUsePooledConnection(false);
        context.addComponent("activemq", amq);

        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() {
                // RouteDefinition starts here
                from("activemq:queue:input.orders")
                        .log("Received order: ${body}")
                        .to("activemq:queue:processed.orders");
            }
        });

        context.start();
        Thread.sleep(5000);
        context.stop();
    }
}