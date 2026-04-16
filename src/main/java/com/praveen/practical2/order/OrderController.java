package com.praveen.practical2.order;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController

public class OrderController {
    @Autowired
    private OrderService orderService;

    @GetMapping("/order/{loanId}")
    public String produce(@PathVariable String loanId){
        return orderService.processOrders(loanId);
    }

}
