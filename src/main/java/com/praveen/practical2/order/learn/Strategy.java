package com.praveen.practical2.order.learn;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.stereotype.Component;

@Component("preAggregationStrategy")
class PreAggProcessor implements Processor {
    public void process(Exchange e) { e.setProperty("totalLevel", 2); e.setProperty("corelationId", "123"); }
}

@Component("requestConverter")
class ReqConvProcessor implements Processor {
    public void process(Exchange e) { e.setProperty("LRSRequest", "SIMULATED_LRS_REQ"); }
}

@Component("riskRuleProcessor")
class RiskRuleProcessor implements Processor {
    public void process(Exchange e) { e.setProperty("ASSESS_ENDPOINT", "activemq:queue:fraud.request"); }
}

@Component("riskLevelProcessor")
class RiskLevelProcessor implements Processor {
    public void process(Exchange e) { /* Logic for risk level */ }
}

@Component("responseProcessor")
class RespProcessor implements Processor {
    public void process(Exchange e) { e.getIn().setBody("Final Calculated Result"); }
}