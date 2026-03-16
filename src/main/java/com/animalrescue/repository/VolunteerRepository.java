package com.animalrescue.repository;

import com.animalrescue.entity.RescuePost;
import com.animalrescue.entity.User;
import com.animalrescue.entity.VolunteerClaim;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VolunteerRepository extends JpaRepository<VolunteerClaim, Long> {

	List<VolunteerClaim> findByVolunteer(User volunteer);

	List<VolunteerClaim> findByRescuePost(RescuePost rescuePost);

	List<VolunteerClaim> findByRescuePostAndStatus(RescuePost rescuePost, VolunteerClaim.ClaimStatus status);

	Optional<VolunteerClaim> findByRescuePostAndVolunteer(RescuePost rescuePost, User volunteer);

	boolean existsByRescuePostAndVolunteer(RescuePost rescuePost, User volunteer);

	List<VolunteerClaim> findByVolunteerAndStatus(User volunteer, VolunteerClaim.ClaimStatus status);
}
