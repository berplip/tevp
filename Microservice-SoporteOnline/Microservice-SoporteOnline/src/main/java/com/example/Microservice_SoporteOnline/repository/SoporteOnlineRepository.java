package com.example.Microservice_SoporteOnline.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.Microservice_SoporteOnline.model.SoporteOnline;

@Repository
public interface SoporteOnlineRepository extends JpaRepository<SoporteOnline, Long> {
}