package com.example.tevp.model;

// MENSAJES INTERNOS

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mensajes")
public class Mensaje {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "remitente_id")
    private Usuario remitente;

    @ManyToOne
    @JoinColumn(name = "destinatario_id")
    private Usuario destinatario;

    private String contenido;
    private LocalDateTime fechaEnvio;
}