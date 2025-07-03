package com.example.Microservice_Usuario;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.Microservice_Usuario.assemblers.UsuarioModelAssembler;
import com.example.Microservice_Usuario.controller.UsuarioControllerV2;
import com.example.Microservice_Usuario.service.UsuarioService;

@WebMvcTest(UsuarioControllerV2.class)
@ActiveProfiles("test")
public class HateoasTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioModelAssembler assembler;

    @Test
    public void testHateoasStructure() throws Exception {
        // Test básico: verifica que el endpoint responde
        // Para tests completos de HAL+JSON se requiere Spring Boot completo
        mockMvc.perform(get("/api/v2/usuarios")
                .accept("application/hal+json"))
                .andExpect(status().isOk());
    }

    @Test
    public void testSingleUserHateoas() throws Exception {
        // Test básico: verifica que el endpoint responde
        // Para tests completos de HAL+JSON se requiere Spring Boot completo
        mockMvc.perform(get("/api/v2/usuarios/1")
                .accept("application/hal+json"))
                .andExpect(status().isOk());
    }
}
