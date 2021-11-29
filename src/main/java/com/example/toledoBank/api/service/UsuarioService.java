package com.example.toledoBank.api.service;

import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Usuario;

public interface UsuarioService {


    UsuarioDTO save(UsuarioDTO usuario);

    UsuarioDTO alterar(Long id, UsuarioDTO usuarioDTO);

    Boolean excluir(Long id);

    Usuario usuarioLogado();
}
