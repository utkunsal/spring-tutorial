package com.example.repositories;

import com.example.entities.Hotel;
import com.example.entities.Review;
import com.example.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByHotel(Hotel hotel);
    List<Review> findAllByUser(User user);
}
