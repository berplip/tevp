package com.example.Microservice_Usuario;

import java.util.List;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.service.UsuarioService;

import net.datafaker.Faker;

@Profile({"dev", "test"})
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioService usuarioService;

    @Override
    public void run(String... args) throws Exception {
        try {
            System.out.println("🚀 Iniciando DataLoader en perfil TEST/DEV...");
            
            // Primero actualizar usuarios existentes sin código
            actualizarUsuariosExistentes();
            
            // Luego crear nuevos usuarios si no hay suficientes
            crearNuevosUsuarios();
            
            System.out.println("✅ DataLoader ejecutado completamente");
        } catch (Exception e) {
            System.err.println("❌ Error en DataLoader: " + e.getMessage());
            e.printStackTrace();
            // No relanzar la excepción para que no falle el contexto de Spring
        }
    }
    
    private void actualizarUsuariosExistentes() {
        try {
            System.out.println("🔄 Actualizando usuarios existentes sin código...");
            
            List<Usuario> usuariosSinCodigo = usuarioService.obtenerTodos().stream()
                .filter(u -> u.getCodigo() == null || u.getCodigo().trim().isEmpty())
                .collect(java.util.stream.Collectors.toList());
            
            Faker faker = new Faker();
            
            for (int i = 0; i < usuariosSinCodigo.size(); i++) {
                Usuario usuario = usuariosSinCodigo.get(i);
                String codigo = generarCodigoUnico(faker, i);
                usuario.setCodigo(codigo);
                usuarioService.actualizar(usuario.getId(), usuario);
            }
            
            System.out.println("✅ Actualizados " + usuariosSinCodigo.size() + " usuarios existentes");
        } catch (Exception e) {
            System.err.println("❌ Error actualizando usuarios existentes: " + e.getMessage());
        }
    }
    
    private void crearNuevosUsuarios() {
        try {
            long totalUsuarios = usuarioService.contarTodos();
            int usuariosACrear = 50 - (int) totalUsuarios;
            
            if (usuariosACrear <= 0) {
                System.out.println("✅ Ya hay suficientes usuarios (" + totalUsuarios + "), no se crean nuevos");
                return;
            }
            
            System.out.println("📝 Creando " + usuariosACrear + " nuevos usuarios...");
            
            Faker faker = new Faker();
            Random random = new Random();

            // Generar diferentes tipos de usuarios
            String[] dominios = {"gmail.com", "hotmail.com", "yahoo.com", "outlook.com", "empresa.com", "universidad.edu"};
            String[] ciudades = {"Bogotá", "Medellín", "Cali", "Barranquilla", "Cartagena", "Bucaramanga", "Pereira", "Santa Marta"};
        
        for (int i = 0; i < usuariosACrear; i++) {
            Usuario usuario = new Usuario();
            
            // Generar código único
            String codigo = generarCodigoUnico(faker, (int) totalUsuarios + i);
            usuario.setCodigo(codigo);
            
            // Generar datos básicos
            String nombre = faker.name().fullName();
            usuario.setNombre(nombre);
            
            // Generar correo válido basado en el nombre
            String nombreUsuario = generarNombreUsuarioValido(nombre, faker);
            String dominio = dominios[random.nextInt(dominios.length)];
            usuario.setCorreo(nombreUsuario + "@" + dominio);
            
            // Generar contraseña segura
            usuario.setContrasena(generarContrasenaSegura(faker));
            
            // Generar teléfono colombiano
            usuario.setTelefono(generarTelefonoColombiano(faker));
            
            // Generar dirección
            String ciudad = ciudades[random.nextInt(ciudades.length)];
            usuario.setDireccion(faker.address().streetAddress() + ", " + ciudad);
            
            // Asignar estado aleatorio (90% activos, 8% inactivos, 2% bloqueados)
            double estadoRandom = random.nextDouble();
            if (estadoRandom < 0.90) {
                usuario.setEstado(Usuario.EstadoUsuario.ACTIVO);
            } else if (estadoRandom < 0.98) {
                usuario.setEstado(Usuario.EstadoUsuario.INACTIVO);
            } else {
                usuario.setEstado(Usuario.EstadoUsuario.BLOQUEADO);
            }
            
            usuarioService.crear(usuario);
        }
        
        System.out.println("✅ Creados " + usuariosACrear + " nuevos usuarios");
        } catch (Exception e) {
            System.err.println("❌ Error creando nuevos usuarios: " + e.getMessage());
        }
    }
    
    private String generarNombreUsuarioValido(String nombreCompleto, Faker faker) {
        // Limpiar el nombre y generar un nombre de usuario válido para email
        String nombreLimpio = nombreCompleto.toLowerCase()
            .replace(" ", ".")
            .replaceAll("[^a-z0-9.]", "") // Solo letras, números y puntos
            .replaceAll("\\.+", ".") // Reemplazar múltiples puntos consecutivos con uno solo
            .replaceAll("^\\.|\\.$", ""); // Remover puntos al inicio y final
        
        // Si el nombre queda vacío después de la limpieza, generar uno alternativo
        if (nombreLimpio.isEmpty() || nombreLimpio.length() < 3) {
            nombreLimpio = "usuario" + faker.number().numberBetween(1000, 9999);
        }
        
        // Agregar un número al final para evitar duplicados
        return nombreLimpio + faker.number().numberBetween(1, 999);
    }
    
    private String generarContrasenaSegura(Faker faker) {
        // Generar contraseña que combine letras, números y símbolos
        String letras = faker.lorem().characters(4, 8);
        String numeros = faker.number().digits(2);
        String simbolos = faker.options().option("!", "@", "#", "$", "%");
        
        return letras + numeros + simbolos;
    }
    
    private String generarTelefonoColombiano(Faker faker) {
        // Generar números de teléfono colombianos
        String[] prefijosCelular = {"300", "301", "302", "303", "304", "305", "310", "311", "312", "313", "314", "315", "316", "317", "318", "319", "320", "321", "322", "323", "324", "325"};
        String prefijo = prefijosCelular[faker.random().nextInt(prefijosCelular.length)];
        String numero = faker.number().digits(7);
        
        return prefijo + numero;
    }
    
    private String generarCodigoUnico(Faker faker, int indice) {
        // Generar código único similar al formato de carreras (ej: U0000001A, U0000002B, etc.)
        String prefijo = "U";
        String numero = String.format("%07d", indice + 1); // 7 dígitos con ceros a la izquierda
        String sufijo = faker.options().option("A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z");
        
        return prefijo + numero + sufijo;
    }
}
