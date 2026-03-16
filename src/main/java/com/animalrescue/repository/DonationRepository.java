package com.animalrescue.repository;

import com.animalrescue.entity.Donation;
import com.animalrescue.entity.RescuePost;
import com.animalrescue.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface DonationRepository extends JpaRepository<Donation, Long> {

    List<Donation> findByDonor(User donor);

    List<Donation> findByRescuePost(RescuePost rescuePost);

    List<Donation> findByPaymentStatus(Donation.PaymentStatus status);

    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.rescuePost = :rescuePost AND d.paymentStatus = 'SUCCESS'")
    BigDecimal sumSuccessfulDonationsByPost(@Param("rescuePost") RescuePost rescuePost);

    @Query("SELECT SUM(d.amount) FROM Donation d WHERE d.donor = :donor AND d.paymentStatus = 'SUCCESS'")
    BigDecimal sumSuccessfulDonationsByDonor(@Param("donor") User donor);
}
