package com.example.Microservice_Pedido.model;

public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String email; // Cambia 'correo' por 'email'

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
}
