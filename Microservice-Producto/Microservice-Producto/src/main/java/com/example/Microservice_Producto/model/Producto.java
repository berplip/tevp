package com.example.Microservice_Producto.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "producto")
@Schema(description = "Modelo que representa un producto en el sistema")
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "ID único del producto", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @Column(unique = true, nullable = true, length = 20)
    @Schema(description = "Código único del producto para comunicación entre máquinas", example = "P1A2B3C", required = false, maxLength = 20)
    private String codigo;

    @Schema(description = "Nombre del producto", example = "iPhone 15 Pro", required = true, maxLength = 255)
    private String nombre;
    
    @Schema(description = "Descripción detallada del producto", example = "Smartphone Apple con chip A17 Pro", maxLength = 500)
    private String descripcion;
    
    @Schema(description = "Precio del producto en pesos chilenos", example = "1200000.0", required = true, minimum = "0")
    private Double precio;
    
    @Schema(description = "Cantidad disponible en stock", example = "10", required = true, minimum = "0")
    private Integer stock;

    // Getters y setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Integer getStock() { return stock; }
    public void setStock(Integer stock) { this.stock = stock; }
}
