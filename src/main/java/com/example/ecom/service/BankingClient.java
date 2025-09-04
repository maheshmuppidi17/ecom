//package com.example.ecom.service;
//
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//
//import com.example.ecom.dto.BankingDebitRequest;
//import com.example.ecom.dto.BankingVerifyRequest;
//
//@FeignClient(name = "banking")
//public interface BankingClient {
//
//	  @PostMapping("/api/bank/verify")
//	    boolean verify(@RequestBody BankingVerifyRequest request);
//
//	    @PostMapping("/api/bank/debit")
//	    boolean debit(@RequestBody BankingDebitRequest request);
//}

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
