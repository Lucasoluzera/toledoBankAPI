package com.example.toledoBank.api.repository;

import com.example.toledoBank.api.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Usuario findByLogin(String login);

    Boolean existsByLogin(String login);
}
