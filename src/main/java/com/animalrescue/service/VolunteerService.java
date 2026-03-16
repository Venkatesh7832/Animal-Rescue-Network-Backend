package com.animalrescue.service;

import com.animalrescue.entity.RescuePost;
import com.animalrescue.entity.User;
import com.animalrescue.entity.VolunteerClaim;
import com.animalrescue.repository.RescueRepository;
import com.animalrescue.repository.VolunteerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    @Autowired
    private RescueRepository rescueRepository;

    @Transactional
    public VolunteerClaim claimRescue(Long rescuePostId, User volunteer, String notes) {
        RescuePost post = rescueRepository.findById(rescuePostId)
                .orElseThrow(() -> new RuntimeException("Rescue post not found with id: " + rescuePostId));

        if (post.getStatus() == RescuePost.Status.RESCUED || post.getStatus() == RescuePost.Status.CLOSED) {
            throw new RuntimeException("This rescue post is no longer active");
        }

        if (volunteerRepository.existsByRescuePostAndVolunteer(post, volunteer)) {
            throw new RuntimeException("You have already claimed this rescue");
        }

        VolunteerClaim claim = new VolunteerClaim();
        claim.setRescuePost(post);
        claim.setVolunteer(volunteer);
        claim.setNotes(notes);
        claim.setStatus(VolunteerClaim.ClaimStatus.PENDING);

        post.setStatus(RescuePost.Status.IN_PROGRESS);
        rescueRepository.save(post);

        return volunteerRepository.save(claim);
    }

    public List<VolunteerClaim> getMyClaims(User volunteer) {
        return volunteerRepository.findByVolunteer(volunteer);
    }

    public List<VolunteerClaim> getClaimsForPost(Long rescuePostId) {
        RescuePost post = rescueRepository.findById(rescuePostId)
                .orElseThrow(() -> new RuntimeException("Rescue post not found"));
        return volunteerRepository.findByRescuePost(post);
    }

    @Transactional
    public VolunteerClaim updateClaimStatus(Long claimId, String status, User user) {
        VolunteerClaim claim = volunteerRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Volunteer claim not found"));

        boolean isVolunteer = claim.getVolunteer().getId().equals(user.getId());
        boolean isOwner = claim.getRescuePost().getPostedBy().getId().equals(user.getId());
        boolean isAdmin = user.getRole() == User.Role.ADMIN;

        if (!isVolunteer && !isOwner && !isAdmin) {
            throw new RuntimeException("Not authorized to update this claim");
        }

        VolunteerClaim.ClaimStatus newStatus = VolunteerClaim.ClaimStatus.valueOf(status.toUpperCase());
        claim.setStatus(newStatus);

        if (newStatus == VolunteerClaim.ClaimStatus.COMPLETED) {
            claim.setResolvedAt(LocalDateTime.now());
            RescuePost post = claim.getRescuePost();
            post.setStatus(RescuePost.Status.RESCUED);
            rescueRepository.save(post);
        } else if (newStatus == VolunteerClaim.ClaimStatus.CANCELLED) {
            claim.setResolvedAt(LocalDateTime.now());
        }

        return volunteerRepository.save(claim);
    }

    @Transactional
    public void cancelClaim(Long claimId, User volunteer) {
        VolunteerClaim claim = volunteerRepository.findById(claimId)
                .orElseThrow(() -> new RuntimeException("Volunteer claim not found"));

        if (!claim.getVolunteer().getId().equals(volunteer.getId())) {
            throw new RuntimeException("Not authorized to cancel this claim");
        }

        claim.setStatus(VolunteerClaim.ClaimStatus.CANCELLED);
        claim.setResolvedAt(LocalDateTime.now());
        volunteerRepository.save(claim);

        List<VolunteerClaim> activeClaims = volunteerRepository
                .findByRescuePostAndStatus(claim.getRescuePost(), VolunteerClaim.ClaimStatus.PENDING);
        if (activeClaims.isEmpty()) {
            RescuePost post = claim.getRescuePost();
            post.setStatus(RescuePost.Status.OPEN);
            rescueRepository.save(post);
        }
    }
}
