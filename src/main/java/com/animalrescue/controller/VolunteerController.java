package com.animalrescue.controller;

import com.animalrescue.entity.User;
import com.animalrescue.entity.VolunteerClaim;
import com.animalrescue.service.AuthService;
import com.animalrescue.service.VolunteerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/volunteers")
@CrossOrigin(origins = "*")
public class VolunteerController {

    @Autowired
    private VolunteerService volunteerService;

    @Autowired
    private AuthService authService;

    @PostMapping("/claim/{rescuePostId}")
    public ResponseEntity<?> claimRescue(@PathVariable Long rescuePostId,
                                          @RequestBody(required = false) Map<String, String> body,
                                          @RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);
            String notes = body != null ? body.get("notes") : null;
            VolunteerClaim claim = volunteerService.claimRescue(rescuePostId, user, notes);
            return ResponseEntity.ok(claim);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/my-claims")
    public ResponseEntity<?> getMyClaims(@RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);
            List<VolunteerClaim> claims = volunteerService.getMyClaims(user);
            return ResponseEntity.ok(claims);
        } catch (RuntimeException e) {
            return ResponseEntity.status(401).body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/post/{rescuePostId}")
    public ResponseEntity<?> getClaimsForPost(@PathVariable Long rescuePostId,
                                               @RequestHeader("Authorization") String token) {
        try {
            getUserFromToken(token); // Validate auth
            List<VolunteerClaim> claims = volunteerService.getClaimsForPost(rescuePostId);
            return ResponseEntity.ok(claims);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PatchMapping("/claim/{claimId}/status")
    public ResponseEntity<?> updateClaimStatus(@PathVariable Long claimId,
                                                @RequestBody Map<String, String> body,
                                                @RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);
            String status = body.get("status");
            VolunteerClaim updated = volunteerService.updateClaimStatus(claimId, status, user);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/claim/{claimId}")
    public ResponseEntity<?> cancelClaim(@PathVariable Long claimId,
                                          @RequestHeader("Authorization") String token) {
        try {
            User user = getUserFromToken(token);
            volunteerService.cancelClaim(claimId, user);
            return ResponseEntity.ok(Map.of("message", "Claim cancelled successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    private User getUserFromToken(String token) {
        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);
        }
        return authService.getUserFromToken(token);
    }
}
