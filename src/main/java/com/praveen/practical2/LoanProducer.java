package com.praveen.practical2;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class LoanProducer {
    @Autowired
    private JmsTemplate jmsTemplate;

    public String sendApplication(String loanId) {
        System.out.println("Sending loan: " + loanId);
        jmsTemplate.convertAndSend("loan-queue", "New Loan Application: " + loanId);
        return "Success send loan";
    }
}