package com.animalrescue.service;

import com.animalrescue.dto.DonationDTO;
import com.animalrescue.entity.Donation;
import com.animalrescue.entity.RescuePost;
import com.animalrescue.entity.User;
import com.animalrescue.repository.DonationRepository;
import com.animalrescue.repository.RescueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class DonationService {

    @Autowired
    private DonationRepository donationRepository;

    @Autowired
    private RescueRepository rescueRepository;

    @Transactional
    public DonationDTO makeDonation(DonationDTO dto, User donor) {
        Donation donation = new Donation();
        donation.setDonor(donor);
        donation.setAmount(dto.getAmount());
        donation.setMessage(dto.getMessage());
        donation.setAnonymous(dto.isAnonymous());
        donation.setTransactionId(UUID.randomUUID().toString());
        donation.setPaymentStatus(Donation.PaymentStatus.SUCCESS); // Simulated

        if (dto.getRescuePostId() != null) {
            RescuePost post = rescueRepository.findById(dto.getRescuePostId())
                    .orElseThrow(() -> new RuntimeException("Rescue post not found"));
            donation.setRescuePost(post);
        }

        Donation saved = donationRepository.save(donation);
        return toDTO(saved);
    }

    public List<DonationDTO> getMyDonations(User donor) {
        return donationRepository.findByDonor(donor)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public List<DonationDTO> getDonationsForPost(Long rescuePostId) {
        RescuePost post = rescueRepository.findById(rescuePostId)
                .orElseThrow(() -> new RuntimeException("Rescue post not found"));
        return donationRepository.findByRescuePost(post)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    public BigDecimal getTotalDonationsForPost(Long rescuePostId) {
        RescuePost post = rescueRepository.findById(rescuePostId)
                .orElseThrow(() -> new RuntimeException("Rescue post not found"));
        BigDecimal total = donationRepository.sumSuccessfulDonationsByPost(post);
        return total != null ? total : BigDecimal.ZERO;
    }

    public BigDecimal getMyTotalDonations(User donor) {
        BigDecimal total = donationRepository.sumSuccessfulDonationsByDonor(donor);
        return total != null ? total : BigDecimal.ZERO;
    }

    public List<DonationDTO> getAllDonations() {
        return donationRepository.findAll()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private DonationDTO toDTO(Donation donation) {
        DonationDTO dto = new DonationDTO();
        dto.setId(donation.getId());
        dto.setAmount(donation.getAmount());
        dto.setMessage(donation.getMessage());
        dto.setAnonymous(donation.isAnonymous());
        dto.setPaymentStatus(donation.getPaymentStatus().name());
        dto.setTransactionId(donation.getTransactionId());
        dto.setDonatedAt(donation.getDonatedAt());
        if (donation.getRescuePost() != null) {
            dto.setRescuePostId(donation.getRescuePost().getId());
        }
        if (donation.getDonor() != null && !donation.isAnonymous()) {
            dto.setDonorUsername(donation.getDonor().getUsername());
        }
        return dto;
    }
}
