package com.example.Microservice_Usuario;

import com.example.Microservice_Usuario.model.Usuario;
import com.example.Microservice_Usuario.repository.UsuarioRepository;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Profile("dev")
@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public void run(String... args) throws Exception {
        Faker faker = new Faker();

        // Genera y guarda 20 usuarios falsos
        for (int i = 0; i < 20; i++) {
            Usuario usuario = new Usuario();
            usuario.setNombre(faker.name().fullName());
            usuario.setCorreo(faker.internet().emailAddress());
            usuario.setContrasena(faker.lorem().word() + faker.number().digits(4));
            usuarioRepository.save(usuario);
        }
        System.out.println("--- Base de datos de Usuarios inicializada con 20 datos de Faker. ---");
    }
}
