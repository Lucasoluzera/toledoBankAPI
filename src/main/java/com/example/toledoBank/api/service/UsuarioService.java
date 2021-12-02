package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Usuario;

import java.util.Optional;

public interface UsuarioService {


    UsuarioDTO save(UsuarioDTO usuario);

    UsuarioDTO alterar(UsuarioDTO usuarioDTO);

    void excluir(Long id);

    Usuario usuarioLogado();

    Optional<Usuario> buscarUsuarioId(Long id);
}
