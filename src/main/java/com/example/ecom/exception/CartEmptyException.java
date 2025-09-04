package com.example.ecom.exception;


public class CartEmptyException extends RuntimeException {
    public CartEmptyException(String message) {
        super(message);
    }
}