package com.example.ecom.repo;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.example.ecom.model.Offer;

import java.util.Optional;

public interface OfferRepository extends MongoRepository<Offer, String> {
    Optional<Offer> findByCode(String code);
}
