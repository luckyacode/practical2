package com.praveen.practical2.order.learn;

import org.springframework.stereotype.Component;

@Component("responseDTO")
class ResponseDTO {
    public void logMessageHeader(Object body, String label) {
        System.out.println("[" + label + "] Header Logged: " + body);
    }
}

@Component("sbMessageLogging")
class SBMessageLogging {
    public void log(Object body, String context) {
        System.out.println("Logging Context [" + context + "]: " + body);
    }
}