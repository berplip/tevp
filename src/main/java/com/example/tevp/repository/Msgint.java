package com.example.tevp.repository;

import com.example.tevp.model.msgint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface Msgint extends JpaRepository<Msgint, Long> {

    List<msgint> findByRemitenteId(Long remitenteId);

}
