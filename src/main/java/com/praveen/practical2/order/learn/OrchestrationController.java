package com.praveen.practical2.order.learn;


import com.praveen.practical2.order.OrchestrationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class OrchestrationController {
    @Autowired
    private OrchestrationService orchestrationService;

    @GetMapping("/orch/{loanId}")
    public String orchestrate(@PathVariable String loanId){
        return orchestrationService.processOrders(loanId);
    }

    @GetMapping("/orch2/{loanId}")
    public String orchestrate2(@PathVariable String loanId){
        return orchestrationService.sendApplication(loanId);
    }

}
