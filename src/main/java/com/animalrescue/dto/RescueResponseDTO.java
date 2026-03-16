package com.animalrescue.dto;

import com.animalrescue.entity.RescuePost;
import java.time.LocalDateTime;

public class RescueResponseDTO {

	private Long id;
	private String title;
	private String description;
	private String location;
	private String animalType;
	private String animalCondition;
	private String imageUrl;
	private String status;
	private String contactNumber;
	private String postedByUsername;
	private Long postedById;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public RescueResponseDTO() {
	}

	public static RescueResponseDTO fromEntity(RescuePost post) {
		RescueResponseDTO dto = new RescueResponseDTO();
		dto.setId(post.getId());
		dto.setTitle(post.getTitle());
		dto.setDescription(post.getDescription());
		dto.setLocation(post.getLocation());
		dto.setAnimalType(post.getAnimalType());
		dto.setAnimalCondition(post.getAnimalCondition());
		dto.setImageUrl(post.getImageUrl());
		dto.setStatus(post.getStatus().name());
		dto.setContactNumber(post.getContactNumber());
		dto.setCreatedAt(post.getCreatedAt());
		dto.setUpdatedAt(post.getUpdatedAt());
		if (post.getPostedBy() != null) {
			dto.setPostedByUsername(post.getPostedBy().getUsername());
			dto.setPostedById(post.getPostedBy().getId());
		}
		return dto;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getAnimalType() {
		return animalType;
	}

	public void setAnimalType(String animalType) {
		this.animalType = animalType;
	}

	public String getAnimalCondition() {
		return animalCondition;
	}

	public void setAnimalCondition(String animalCondition) {
		this.animalCondition = animalCondition;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getPostedByUsername() {
		return postedByUsername;
	}

	public void setPostedByUsername(String postedByUsername) {
		this.postedByUsername = postedByUsername;
	}

	public Long getPostedById() {
		return postedById;
	}

	public void setPostedById(Long postedById) {
		this.postedById = postedById;
	}

	public LocalDateTime getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDateTime createdAt) {
		this.createdAt = createdAt;
	}

	public LocalDateTime getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDateTime updatedAt) {
		this.updatedAt = updatedAt;
	}
}
