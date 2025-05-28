package com.example.tevp.repository;

import com.example.tevp.model.Resena;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Long> {

    List<Resena> findByProducto_Id(Long productoId);

}
