package com.example.tevp.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Producto {

    private int id;
    private String isbn;
    private String titulo;
    private int fechaPublicacion;
    private String autor;

}
