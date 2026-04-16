package com.praveen.practical2.order.learn;

import org.apache.camel.ExchangePattern;
import org.apache.camel.builder.PredicateBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class OrchestrationRoute extends RouteBuilder {

    // Defined Constants
    private static final int TOTAL_LEVEL_VAL = 2;
    private static final String ASSESS_ENDPOINT_VAL = "activemq:queue:provider.A,activemq:queue:provider.B";
    private static final String STR_VAL = null; // Setting to null so the .isNull() condition passes

    @Override
    public void configure() throws Exception {

        from("activemq:queue:requestEndPoint")
                .id("AppNode-Request/Response-Node")
                .setExchangePattern(ExchangePattern.InOnly)

                // Initialize the constants into the Exchange so Camel can see them
                .setProperty("totalLevel", constant(TOTAL_LEVEL_VAL))
                .setProperty("ASSESS_ENDPOINT", constant(ASSESS_ENDPOINT_VAL))
                .setProperty("STR", constant(STR_VAL))

                .setProperty("Completion_Timeout", simple("2333"))
                .convertBodyTo(String.class)
                .bean("responseDTO", "logMessageHeader(*, 'App-Request')")

                // Custom Logic Processors
                .process("preAggregationStrategy")
                .process("requestConverter")
                .process("riskRuleProcessor")
                .log("after riskRuleProcessorDone.....")
                .choice()
                // Check if totalLevel > 1
                .when(exchangeProperty("totalLevel").isGreaterThan(1))
                .setExchangePattern(ExchangePattern.InOut)
                .loop(exchangeProperty("totalLevel"))
                .choice()
                .when(PredicateBuilder.and(
                        exchangeProperty("ASSESS_ENDPOINT").isNotNull(),
                        exchangeProperty("STR").isNull()
                ))
                .setHeader("CORELATION_ID", simple("${exchangeProperty.corelationId}"))
                .setBody(exchangeProperty("LRSRequest"))
                .convertBodyTo(String.class)
                .recipientList(exchangeProperty("ASSESS_ENDPOINT").tokenize(","))
                .parallelProcessing()
                .streaming()
                .timeout(3000)
                .aggregationStrategy("msgAggregator")
                .end()
                .process("riskLevelProcessor")
                .process("riskRuleProcessor")
                .endChoice()
                .end() // End Loop
                .endChoice()
                .end() // End Choice

                .setExchangePattern(ExchangePattern.InOut)
                .process("responseProcessor")
                .bean("sbMessageLogging", "log(*, 'FinalResponse')")
                .to("activemq:queue:responseEndpoint")
                .wireTap("direct:performanceLog");

        from("direct:performanceLog")
                .log("Performance Trace: ${body}");
    }
}