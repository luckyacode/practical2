package com.praveen.practical2.order.learn;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component("waitForAllListenersProcessor")
public class WaitForAllListenersProcessor implements Processor {

    @Autowired
    private ActiveMqMonitor activeMqMonitor;

    @Override
    public void process(Exchange exchange) throws Exception {
        // Define the list of queues that MUST be active before we proceed
        String[] requiredQueues = {"provider.A", "provider.B", "fraud.request"};
        boolean allReady = false;

        while (!allReady) {
            allReady = true; // Assume true until proven otherwise

            for (String queue : requiredQueues) {
                if (!activeMqMonitor.isQueueActivelyListening(queue)) {
                    System.out.println("WAITING: " + queue + " has no listeners. Tomcat still booting...");
                    allReady = false;
                    break;
                }
            }

            if (!allReady) {
                // Wait for 5 seconds before checking again
                Thread.sleep(5000);
            }
        }

        System.out.println("SUCCESS: All  components are active. Proceeding with orchestration.");
        // Set the endpoints now that we know they are safe
//        exchange.setProperty("ASSESS_ENDPOINT", "activemq:queue:provider.A,activemq:queue:provider.B,activemq:queue:fraud.request");
    }
}