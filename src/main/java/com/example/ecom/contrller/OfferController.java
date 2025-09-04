package com.example.ecom.contrller;

import com.example.ecom.dto.AppResponseDto;
import com.example.ecom.dto.OfferRequest;
import com.example.ecom.dto.OfferResponse;
import com.example.ecom.model.Offer;
import com.example.ecom.service.OfferService;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/offers")
public class OfferController {
	
	private static final Logger logger =
    		LoggerFactory.getLogger(OfferController.class);

    @Autowired private OfferService offerService;

    @PostMapping("/create")
    public ResponseEntity<AppResponseDto> createOffer(@Valid @RequestBody OfferRequest offerRequest) {
    	logger.warn("create the offer code with discount precentage");
    	OfferResponse offerResponse=offerService.createOffer(offerRequest);
    	AppResponseDto appResponseDto=new AppResponseDto(2001,"Offer code created successfully",offerResponse);
        return new ResponseEntity<AppResponseDto>(appResponseDto, HttpStatus.OK);
    }

    @GetMapping("/{code}")
    public ResponseEntity<AppResponseDto> getByCode(@PathVariable String code) {
    	logger.error("Invalid offer code provided");
    	Offer offerCode=offerService.getOfferByCode(code);
    	AppResponseDto appResponseDto=new AppResponseDto(2001, "Offer Code", offerCode);
        return new ResponseEntity<AppResponseDto>(appResponseDto, HttpStatus.OK);
    }
    
    @GetMapping("/getAll")
    public ResponseEntity<List<Offer>> getAllOffers() {
        return ResponseEntity.ok(offerService.getAllOffers());
    }
}
