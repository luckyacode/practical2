package com.praveen.practical2;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class MQController {
    @Autowired
    private  LoanProducer loanProducer;
    @GetMapping("/produce/{loanId}")
    public String produce(@PathVariable String loanId){
        return loanProducer.sendApplication(loanId);
    }
}
