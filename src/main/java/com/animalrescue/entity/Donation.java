package com.animalrescue.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "donations")
public class Donation {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "donor_id")
	private User donor;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rescue_post_id")
	private RescuePost rescuePost;

	@Column(nullable = false, precision = 10, scale = 2)
	private BigDecimal amount;

	@Column
	private String message;

	@Column(name = "is_anonymous")
	private boolean anonymous;

	@Enumerated(EnumType.STRING)
	@Column(name = "payment_status", nullable = false)
	private PaymentStatus paymentStatus;

	@Column(name = "transaction_id")
	private String transactionId;

	@Column(name = "donated_at")
	private LocalDateTime donatedAt;

	public enum PaymentStatus {
		PENDING, SUCCESS, FAILED
	}

	@PrePersist
	protected void onCreate() {
		donatedAt = LocalDateTime.now();
		if (paymentStatus == null)
			paymentStatus = PaymentStatus.PENDING;
	}

	public Donation() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getDonor() {
		return donor;
	}

	public void setDonor(User donor) {
		this.donor = donor;
	}

	public RescuePost getRescuePost() {
		return rescuePost;
	}

	public void setRescuePost(RescuePost rescuePost) {
		this.rescuePost = rescuePost;
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

	public PaymentStatus getPaymentStatus() {
		return paymentStatus;
	}

	public void setPaymentStatus(PaymentStatus paymentStatus) {
		this.paymentStatus = paymentStatus;
	}

	public String getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(String transactionId) {
		this.transactionId = transactionId;
	}

	public LocalDateTime getDonatedAt() {
		return donatedAt;
	}

	public void setDonatedAt(LocalDateTime donatedAt) {
		this.donatedAt = donatedAt;
	}
}
