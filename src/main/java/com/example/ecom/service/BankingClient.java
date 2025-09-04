package com.example.ecom.service;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.example.ecom.dto.FundTransferDTO;

@FeignClient(name = "banking-service", url = "http://localhost:9090/api/transactions")
public interface BankingClient {

    @PostMapping("/transact")
    String transact(@RequestBody FundTransferDTO transferDTO);
}
