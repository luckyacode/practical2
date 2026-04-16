package com.praveen.practical2;

import lombok.extern.slf4j.Slf4j;
import org.apache.camel.CamelContext;
import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.activemq.ActiveMQComponent;
import org.apache.camel.impl.DefaultCamelContext;

@Slf4j
public class MyRoute {
    public static void main(String[] args) throws Exception {
        CamelContext context = new DefaultCamelContext();

        // Configure the ActiveMQ component
        ActiveMQComponent amq = new ActiveMQComponent();
        amq.setBrokerURL("tcp://localhost:61616");
        amq.setUsePooledConnection(false);
        context.addComponent("activemq", amq);

        context.addRoutes(new OrderRoutingLogic());

//        context.addRoutes(new RouteBuilder() {
//            @Override
//            public void configure() {
//                // RouteDefinition starts here
//                from("activemq:queue:input.orders")
//                        .log("Received order: ${body}")
//                        .to("activemq:queue:processed.orders");
//            }
//        });

        context.start();
        Thread.sleep(5000);
        context.stop();
    }
}


class OrderRoutingLogic extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // 1. Listen for incoming orders
        from("activemq:queue:input.orders")
                .routeId("OrderRoutingRoute")

                .log("Processing order: ${body}")

                // 2. Start the Choice Logic
                .choice()
                .when(header("Priority").isEqualTo("High"))
                .log("High Priority: Requesting immediate processing...")
                // InOut sends the message and waits for a reply on this specific step
                .to(ExchangePattern.InOut, "activemq:queue:orders.priority.high?requestTimeout=5000")

                .when(header("Type").isEqualTo("Digital"))
                .log("Digital Order: Requesting license key...")
                .to(ExchangePattern.InOut, "activemq:queue:orders.digital?requestTimeout=5000")

                .otherwise()
                .log("Standard Order: Sending to fulfillment...")
                .to(ExchangePattern.InOut, "activemq:queue:orders.standard?requestTimeout=5000")
                .end()

                // 3. The ${body} here is now the RESPONSE received from the queues above.
                .log("Response received: ${body}")

                // 4. Archive the final result
                .to("activemq:queue:orders.audit")
                .log("Order processing complete and archived.");
    }
}

 class OrderRoutingLogic2 extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // 1. Listen for incoming orders
        from("activemq:queue:input.orders")
                .routeId("OrderRoutingRoute") // Good practice for debugging

                // 2. Start the Choice Logic
                .choice()
                .when(header("Priority").isEqualTo("High"))
                .log("High Priority Order detected: ${body}")
                .to("activemq:queue:orders.priority.high")

                .when(header("Type").isEqualTo("Digital"))
                .log("Digital Product: ${body}")
                .to("activemq:queue:orders.digital")

                .otherwise()
                .log("Standard Order: ${body}")
                .to("activemq:queue:orders.standard")
                .end() // 3. This ends the Choice block

                // 4. Send ALL messages to a general audit queue regardless of the choice above
                .to("activemq:queue:orders.audit")
                .log("Order processing complete and archived.");
    }
}