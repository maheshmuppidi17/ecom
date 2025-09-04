package com.example.ecom.service;

import java.util.List;

import com.example.ecom.dto.OfferRequest;
import com.example.ecom.dto.OfferResponse;
import com.example.ecom.model.Offer;

public interface OfferService {
    OfferResponse createOffer(OfferRequest offerRequest);
    List<Offer> getAllOffers();
    Offer getOfferByCode(String code);
}
