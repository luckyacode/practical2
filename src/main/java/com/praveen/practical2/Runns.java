package com.praveen.practical2;

import org.apache.camel.CamelContext;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;

public class Runns {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        // 1. Tell Camel how to find your ActiveMQ 6.2.4 broker
        ActiveMQComponent amq = ActiveMQComponent.activeMQComponent("tcp://localhost:61616");
        context.addComponent("activemq", amq);

        // 2. Add your RouteDefinition
        context.addRoutes(new MySimpleRoute());

        // 3. Start the engine
        context.start();

        // Keep the app running for a bit to process messages
        Thread.sleep(10000);
        context.stop();
    }
}
