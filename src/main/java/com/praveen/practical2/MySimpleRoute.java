package com.praveen.practical2;

import org.apache.camel.builder.RouteBuilder;

public class MySimpleRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        // This entire block is a RouteDefinition
        from("activemq:queue:SourceQueue")
                .setBody(simple("Hello ${body}, welcome to ActiveMQ!"))
                .log("Processing message: ${body}")
                .to("activemq:queue:TargetQueue");

    }
}