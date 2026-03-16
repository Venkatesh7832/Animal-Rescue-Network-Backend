package com.animalrescue.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "volunteer_claims")
public class VolunteerClaim {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "rescue_post_id", nullable = false)
	private RescuePost rescuePost;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "volunteer_id", nullable = false)
	private User volunteer;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private ClaimStatus status;

	@Column(columnDefinition = "TEXT")
	private String notes;

	@Column(name = "claimed_at")
	private LocalDateTime claimedAt;

	@Column(name = "resolved_at")
	private LocalDateTime resolvedAt;

	public enum ClaimStatus {
		PENDING, ACCEPTED, COMPLETED, CANCELLED
	}

	@PrePersist
	protected void onCreate() {
		claimedAt = LocalDateTime.now();
		if (status == null)
			status = ClaimStatus.PENDING;
	}

	public VolunteerClaim() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public RescuePost getRescuePost() {
		return rescuePost;
	}

	public void setRescuePost(RescuePost rescuePost) {
		this.rescuePost = rescuePost;
	}

	public User getVolunteer() {
		return volunteer;
	}

	public void setVolunteer(User volunteer) {
		this.volunteer = volunteer;
	}

	public ClaimStatus getStatus() {
		return status;
	}

	public void setStatus(ClaimStatus status) {
		this.status = status;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public LocalDateTime getClaimedAt() {
		return claimedAt;
	}

	public void setClaimedAt(LocalDateTime claimedAt) {
		this.claimedAt = claimedAt;
	}

	public LocalDateTime getResolvedAt() {
		return resolvedAt;
	}

	public void setResolvedAt(LocalDateTime resolvedAt) {
		this.resolvedAt = resolvedAt;
	}
}
