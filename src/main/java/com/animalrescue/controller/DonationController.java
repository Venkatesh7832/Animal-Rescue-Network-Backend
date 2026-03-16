package com.animalrescue.controller;

import com.animalrescue.dto.DonationDTO;
import com.animalrescue.entity.User;
import com.animalrescue.service.AuthService;
import com.animalrescue.service.DonationService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/donations")
@CrossOrigin(origins = "*")
public class DonationController {

    @Autowired
    private DonationService donationService;

    @Autowired
    private AuthService authService;

    @PostMapping
    public ResponseEntity<?> makeDonation(@Valid @RequestBody DonationDTO dto,
                                           @RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);
            DonationDTO result = donationService.makeDonation(dto, user);
            return ResponseEntity.ok(result);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-donations")
    public ResponseEntity<?> getMyDonations(@RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);
            List<DonationDTO> donations = donationService.getMyDonations(user);
            return ResponseEntity.ok(donations);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-total")
    public ResponseEntity<?> getMyTotal(@RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);
            BigDecimal total = donationService.getMyTotalDonations(user);
            return ResponseEntity.ok(Map.of("totalDonated", total));
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/post/{rescuePostId}")
    public ResponseEntity<?> getDonationsForPost(@PathVariable Long rescuePostId) {
        try {
            List<DonationDTO> donations = donationService.getDonationsForPost(rescuePostId);
            return ResponseEntity.ok(donations);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/post/{rescuePostId}/total")
    public ResponseEntity<?> getTotalForPost(@PathVariable Long rescuePostId) {
        try {
            BigDecimal total = donationService.getTotalDonationsForPost(rescuePostId);
            return ResponseEntity.ok(Map.of("totalDonated", total));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/all")
    public ResponseEntity<?> getAllDonations(@RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);
            if (user.getRole() != com.animalrescue.entity.User.Role.ADMIN) {
                return ResponseEntity.status(403).body(Map.of("error", "Access denied"));
            }
            return ResponseEntity.ok(donationService.getAllDonations());
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    private User getUserFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return authService.getUserFromToken(token);
    }
}
