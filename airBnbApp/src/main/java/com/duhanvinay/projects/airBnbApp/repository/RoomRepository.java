package com.duhanvinay.projects.airBnbApp.repository;

import com.duhanvinay.projects.airBnbApp.entity.Room;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoomRepository extends JpaRepository<Room, Long> {
}
