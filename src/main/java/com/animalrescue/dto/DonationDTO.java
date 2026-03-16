package com.animalrescue.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class DonationDTO {

	private Long id;

	private Long rescuePostId;

	@NotNull(message = "Amount is required")
	@DecimalMin(value = "1.0", message = "Minimum donation is 1.0")
	private BigDecimal amount;

	private String message;
	private boolean anonymous;
	private String paymentStatus;
	private String transactionId;
	private String donorUsername;
	private LocalDateTime donatedAt;

	public DonationDTO() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getRescuePostId() {
		return rescuePostId;
	}

	public void setRescuePostId(Long rescuePostId) {
		this.rescuePostId = rescuePostId;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isAnonymous() {
		return anonymous;
	}

	public void setAnonymous(boolean anonymous) {
		this.anonymous = anonymous;
	}

	public String getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(String paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public String getDonorUsername() {
		return donorUsername;
	}

	public void setDonorUsername(String donorUsername) {
		this.donorUsername = donorUsername;
	}

	public LocalDateTime getDonatedAt() {
		return donatedAt;
	}

	public void setDonatedAt(LocalDateTime donatedAt) {
		this.donatedAt = donatedAt;
	}
}
