package com.example.Microservice_Descuento.repository;

import com.example.Microservice_Descuento.model.Descuento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DescuentoRepository extends JpaRepository<Descuento, Long> {
}
