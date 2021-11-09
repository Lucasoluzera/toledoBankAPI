package com.example.toledoBank.api.service.impl;

import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.service.PessoaService;
import com.example.toledoBank.api.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UsuarioServiceImpl implements UsuarioService {


    @Autowired
    private final ModelMapper modelMapper = new ModelMapper();


    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PessoaService pessoaService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PessoaService pessoaService) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaService = pessoaService;
    }

    @Override
    public UsuarioDTO save(UsuarioDTO usuarioDTO) {

        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);

        if (pessoaService.existePorCPF(usuarioDTO.getCpfCnpj()))
            usuario.setPessoa(this.pessoaService.buscarPorCPF(usuarioDTO.getCpfCnpj()));
        else
            throw new RuntimeException("Não existe uma pessoa com esse CPF");

        usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));

        usuario = this.usuarioRepository.save(usuario);
        usuario.setSenha(usuarioDTO.getSenha());

        usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        usuarioDTO.setCpfCnpj(usuario.getPessoa().getCpfCnpj());

        return usuarioDTO;
    }

}
