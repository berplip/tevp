package com.example.tevp.model;
//Mensajes internos

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mensajes")
public class msgint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "remitente_id")
    private Usuario remitente;

    @ManyToMany
    @JoinColumn(name = "destinario_id")
    private Usuario destinatario;

    private String contenido;
    private LocalDateTime fechaEnvio;

}
