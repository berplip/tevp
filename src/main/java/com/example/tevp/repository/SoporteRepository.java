package com.example.tevp.repository;

import com.example.tevp.model.SoporteOnline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SoporteRepository extends JpaRepository<SoporteOnline, Long> {

    List<SoporteOnline> findByEstado(String estado);

}
