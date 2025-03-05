package com.informa.tareas.test;

import com.informa.tareas.model.dto.TareaResponse;
import com.informa.tareas.model.dto.User;
import com.informa.tareas.model.dto.util.Role;
import com.informa.tareas.model.entities.Usuario;
import com.informa.tareas.service.TareaService;
import com.informa.tareas.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.Assertions;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Map;

@SpringBootTest
class TestApplicationTests {


    UsuarioService usuarioService;
    TareaService tareaService;

    @Autowired
    public TestApplicationTests(UsuarioService usuarioService, TareaService tareaService) {
        this.usuarioService = usuarioService;
        this.tareaService = tareaService;
    }
/*
    @Test
    void testUsuarioController() {
        Mono<User> uno = usuarioService.findByUsername("admin");

        StepVerifier.create(uno)
                .assertNext(user -> {
                    // Assuming user.getRoles() returns a List or Collection, and the first role should be something like "admin"
                    Assertions.assertEquals(Role.ROLE_ADMIN, user.getRoles().getFirst());
                })
                .verifyComplete();
    }

    @Test
    void testTareaController() {
        Flux<TareaResponse> uno = tareaService.listarPorIdUsuario(Long.parseLong("26"));
        Flux<TareaResponse> dos = tareaService.listarPorIdUsuario(Long.parseLong("26"));
        Flux.just(StepVerifier.create(uno)
                .equals(dos));
    }

*/
}