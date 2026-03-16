package com.animalrescue.repository;

import com.animalrescue.entity.RescuePost;
import com.animalrescue.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RescueRepository extends JpaRepository<RescuePost, Long> {

    Page<RescuePost> findByStatus(RescuePost.Status status, Pageable pageable);

    Page<RescuePost> findByPostedBy(User user, Pageable pageable);

    @Query("SELECT r FROM RescuePost r WHERE " +
           "(:status IS NULL OR r.status = :status) AND " +
           "(:animalType IS NULL OR LOWER(r.animalType) LIKE LOWER(CONCAT('%', :animalType, '%'))) AND " +
           "(:location IS NULL OR LOWER(r.location) LIKE LOWER(CONCAT('%', :location, '%')))")
    Page<RescuePost> findWithFilters(@Param("status") RescuePost.Status status,
                                     @Param("animalType") String animalType,
                                     @Param("location") String location,
                                     Pageable pageable);

    List<RescuePost> findByPostedBy(User user);
}
