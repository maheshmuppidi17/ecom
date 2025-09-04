package com.example.ecom.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@Document(collection = "offers")
public class Offer {
    @Id
    private String id;

    @NotBlank(message = "Offer code is required")
    @Indexed(unique = true)
    private String code;

    @Min(value = 1, message = "Discount must be at least 1%")
    @Max(value = 90, message = "Discount cannot exceed 90%")
    private int discountPercentage;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public int getDiscountPercentage() {
		return discountPercentage;
	}

	public void setDiscountPercentage(int discountPercentage) {
		this.discountPercentage = discountPercentage;
	}
    
    
}
