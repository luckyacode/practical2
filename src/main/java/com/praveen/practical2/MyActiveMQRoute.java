package com.praveen.practical2;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;

public class MyActiveMQRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // This starts a RouteDefinition
        RouteDefinition route = from("activemq:queue:Orders.Incoming");

        route.choice()
                .when(header("type").isEqualTo("retail"))
                .to("activemq:queue:Orders.Retail")
                .otherwise()
                .to("activemq:queue:Orders.Bulk")
                .end();
    }

    public void addCustomRoute(CamelContext context) throws Exception {
        RouteDefinition rd = new RouteDefinition();

        rd.from("timer:tick?period=5000")
                .setBody().constant("Hello from ActiveMQ!")
                .to("activemq:topic:LogTopic");

        // Manually add the definition to the context
//        context.addRouteDefinition(rd);
    }
}