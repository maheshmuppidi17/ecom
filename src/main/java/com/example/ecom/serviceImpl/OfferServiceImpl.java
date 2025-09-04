package com.example.ecom.serviceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.example.ecom.dto.OfferRequest;
import com.example.ecom.dto.OfferResponse;
import com.example.ecom.exception.ResourceNotFoundException;
import com.example.ecom.model.Offer;
import com.example.ecom.repo.OfferRepository;
import com.example.ecom.service.OfferService;

import java.util.List;

@Service
public class OfferServiceImpl implements OfferService {
	
	private static final Logger logger =
    		LoggerFactory.getLogger(OfferServiceImpl.class);

    @Autowired private OfferRepository offerRepository;

    @Override
    public OfferResponse createOffer(OfferRequest offerRequest) {
    	logger.warn("offer code cannot be null");
        if (offerRequest == null) throw new IllegalArgumentException("Offer cannot be null");
        if (!StringUtils.hasText(offerRequest.getCode())) {
            throw new IllegalArgumentException("Offer code is required");
        }
        if (offerRequest.getDiscountPercentage() < 1 || offerRequest.getDiscountPercentage() > 90) {
            throw new IllegalArgumentException("Discount must be between 1 and 90");
        }
        try {
			Offer offer = new Offer();
			BeanUtils.copyProperties(offerRequest, offer);

			Offer savedOffer = offerRepository.save(offer);
			OfferResponse offerResponse = new OfferResponse();
			BeanUtils.copyProperties(savedOffer, offerResponse);
        	
            return offerResponse;
        } catch (DuplicateKeyException ex) {
            throw new IllegalArgumentException("Offer code already exists");
        }
    }

    @Override
    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    @Override
    public Offer getOfferByCode(String code) {
    	logger.error("offer code not found");
        return offerRepository.findByCode(code)
                .orElseThrow(() -> new ResourceNotFoundException("Offer not found: " + code));
    }
}
