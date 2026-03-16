package com.animalrescue.service;

import com.animalrescue.dto.RescueRequestDTO;
import com.animalrescue.dto.RescueResponseDTO;
import com.animalrescue.entity.RescuePost;
import com.animalrescue.entity.User;
import com.animalrescue.repository.RescueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RescueService {

    @Autowired
    private RescueRepository rescueRepository;

    @Transactional
    public RescueResponseDTO createPost(RescueRequestDTO dto, User user) {
        RescuePost post = new RescuePost();
        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setLocation(dto.getLocation());
        post.setAnimalType(dto.getAnimalType());
        post.setAnimalCondition(dto.getAnimalCondition());
        post.setImageUrl(dto.getImageUrl());
        post.setContactNumber(dto.getContactNumber());
        post.setPostedBy(user);

        return RescueResponseDTO.fromEntity(rescueRepository.save(post));
    }

    public Page<RescueResponseDTO> getAllPosts(Pageable pageable) {
        return rescueRepository.findAll(pageable).map(RescueResponseDTO::fromEntity);
    }

    public RescueResponseDTO getPostById(Long id) {
        RescuePost post = rescueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rescue post not found with id: " + id));
        return RescueResponseDTO.fromEntity(post);
    }

    public Page<RescueResponseDTO> getPostsByStatus(String status, Pageable pageable) {
        RescuePost.Status rescueStatus = RescuePost.Status.valueOf(status.toUpperCase());
        return rescueRepository.findByStatus(rescueStatus, pageable).map(RescueResponseDTO::fromEntity);
    }

    public Page<RescueResponseDTO> searchPosts(String status, String animalType, String location, Pageable pageable) {
        RescuePost.Status rescueStatus = null;
        if (status != null && !status.isBlank()) {
            rescueStatus = RescuePost.Status.valueOf(status.toUpperCase());
        }
        return rescueRepository.findWithFilters(rescueStatus, animalType, location, pageable)
                .map(RescueResponseDTO::fromEntity);
    }

    public List<RescueResponseDTO> getMyPosts(User user) {
        return rescueRepository.findByPostedBy(user)
                .stream()
                .map(RescueResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional
    public RescueResponseDTO updatePost(Long id, RescueRequestDTO dto, User user) {
        RescuePost post = rescueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rescue post not found with id: " + id));

        if (!post.getPostedBy().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to update this post");
        }

        post.setTitle(dto.getTitle());
        post.setDescription(dto.getDescription());
        post.setLocation(dto.getLocation());
        post.setAnimalType(dto.getAnimalType());
        post.setAnimalCondition(dto.getAnimalCondition());
        post.setImageUrl(dto.getImageUrl());
        post.setContactNumber(dto.getContactNumber());

        return RescueResponseDTO.fromEntity(rescueRepository.save(post));
    }

    @Transactional
    public RescueResponseDTO updateStatus(Long id, String status, User user) {
        RescuePost post = rescueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rescue post not found with id: " + id));

        if (!post.getPostedBy().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to update this post");
        }

        post.setStatus(RescuePost.Status.valueOf(status.toUpperCase()));
        return RescueResponseDTO.fromEntity(rescueRepository.save(post));
    }

    @Transactional
    public void deletePost(Long id, User user) {
        RescuePost post = rescueRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rescue post not found with id: " + id));

        if (!post.getPostedBy().getId().equals(user.getId()) && user.getRole() != User.Role.ADMIN) {
            throw new RuntimeException("Not authorized to delete this post");
        }

        rescueRepository.delete(post);
    }
}
