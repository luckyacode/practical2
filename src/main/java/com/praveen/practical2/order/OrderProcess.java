package com.praveen.practical2.order;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.RouteBuilder;

public class OrderProcess extends RouteBuilder {


    @Override
    public void configure() throws Exception {
        from("activemq:queue:orders.incoming")
                .routeId("OrderFlowOrchestrator")

                // 1. STORE: Save the original order body into a property
                .setProperty("originalOrder", body())
                .log("Order Received: ${exchangeProperty.originalOrder}")

                // 2. FRAUD CHECK
                .log("Requesting Fraud Detection...")
                .to(ExchangePattern.InOut, "activemq:queue:fraud.request?requestTimeout=5000")

                // 3. MANIPULATE: Store fraud result and rebuild the body for Payment
                .setProperty("fraudStatus", body())
                .log("Fraud Status: ${exchangeProperty.fraudStatus}")

                // Reconstruct the body to send to Payment (Original Order + Fraud Result)
                .setBody(simple("${exchangeProperty.originalOrder} | FraudCheck: ${exchangeProperty.fraudStatus}"))

                // 4. PAYMENT
                .log("Requesting Payment with body: ${body}")
                .to(ExchangePattern.InOut, "activemq:queue:payment.request?requestTimeout=5000")

                // 5. FINAL SEND
                .choice()
                .when(simple("${body} contains 'SUCCESS'"))
                .log("Payment Verified: ${body}")
                // Combine everything for the final success message
                .setBody(simple("Order: ${exchangeProperty.originalOrder} | Status: ${body}"))
                .to("activemq:queue:transaction.success")
                .otherwise()
                .log("Payment Failed: ${body}")
                .to("activemq:queue:transaction.failed")
                .end();
    }
}
