package com.praveen.practical2.order.learn;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("checkReadinessProcessor")
public class CheckReadinessProcessor implements Processor {
    @Autowired
    private ActiveMqMonitor activeMqMonitor;

    @Override
    public void process(Exchange exchange) throws Exception {
        String[] requiredQueues = {"provider.A", "provider.B", "responseEndpoint","fraud.request"};

        for (String queue : requiredQueues) {
            if (!activeMqMonitor.isQueueActivelyListening(queue)) {
                // Log here so you see it immediately in the console
                System.out.println(">>> Readiness FAILED for queue : " + queue + " has no active listeners.");

                throw new ListenerNotReadyException("Tomcat component " + queue + " is not ready.");
            }
        }
        System.out.println(">>> CHECK PASSED: All components are listening. Starting Orchestration...");
    }
}