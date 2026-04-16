package com.praveen.practical2.order;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

public class OrderProcess extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("activemq:queue:orders.incoming")
                .routeId("OrderFlowOrchestrator")
                .log("Order Received: ${body}")

                // Step 1: Send to Payment Queue and WAIT for response
                .log("Requesting Payment for Order...")
                .to(ExchangePattern.InOut, "activemq:queue:payment.request?requestTimeout=5000")

                // Step 2: The ${body} is now the response from the Payment Service
                .choice()
                .when(simple("${body} contains 'SUCCESS'"))
                .log("Payment Verified: ${body}")
                .setBody(simple("${body} - Transaction Processed Successfully"))
                .to("activemq:queue:transaction.success")
                .otherwise()
                .log("Payment Failed or Pending: ${body}")
                .to("activemq:queue:transaction.failed")
                .end();
    }
}
