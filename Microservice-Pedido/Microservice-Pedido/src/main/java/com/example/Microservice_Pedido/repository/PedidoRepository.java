package com.example.Microservice_Pedido.repository;

import com.example.Microservice_Pedido.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Método para buscar pedidos por el ID del usuario (lol)
    List<Pedido> findByUsuarioId(Long usuarioId);
}