package com.duhanvinay.projects.airBnbApp.repository;

import com.duhanvinay.projects.airBnbApp.entity.Hotel;
import com.duhanvinay.projects.airBnbApp.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
    List<Hotel> findByOwner(User owner);
}
