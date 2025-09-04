package com.example.ecom.dto;

public class BankingDebitRequest {
    private String accountNumber;
    private double amount;
    private String purchaseRef;
    private String toAccount;
    
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public double getAmount() {
		return amount;
	}
	public void setAmount(double amount) {
		this.amount = amount;
	}
	public String getPurchaseRef() {
		return purchaseRef;
	}
	public void setPurchaseRef(String purchaseRef) {
		this.purchaseRef = purchaseRef;
	}
	public String getToAccount() {
		return toAccount;
	}
	public void setToAccount(String toAccount) {
		this.toAccount = toAccount;
	}
	
}