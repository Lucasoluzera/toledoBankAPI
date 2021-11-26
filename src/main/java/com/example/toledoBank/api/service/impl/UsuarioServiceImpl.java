package com.example.toledoBank.api.service.impl;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Telefone;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.service.ContaService;
import com.example.toledoBank.api.service.PessoaService;
import com.example.toledoBank.api.service.TelefoneService;
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

    @Autowired
    private ContaService contaService;

    public UsuarioServiceImpl(UsuarioRepository usuarioRepository, PessoaService pessoaService, ContaService contaService) {
        this.usuarioRepository = usuarioRepository;
        this.pessoaService = pessoaService;
        this.contaService = contaService;
    }

    @Override
    public UsuarioDTO save(UsuarioDTO usuarioDTO) {

        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);

        if (usuarioDTO.getPessoaDTO() == null)
        {
            if(this.pessoaService.existePorCPF(usuarioDTO.getCpfCnpj()))
                usuario.setPessoa(this.pessoaService.buscarPorCPF(usuarioDTO.getCpfCnpj()));
            else
                throw new IllegalArgumentException("Não existe uma pessoa com esse CPF.");
        }
        else {
            if(!this.pessoaService.existePorCPF(usuarioDTO.getPessoaDTO().getCpfCnpj())){
                this.pessoaService.save(usuarioDTO.getPessoaDTO());
                usuario.setPessoa(this.pessoaService.buscarPorCPF(usuarioDTO.getCpfCnpj()));
            }else {
                usuario.setPessoa(this.pessoaService.buscarPorCPF(usuarioDTO.getCpfCnpj()));
            }
        }


        if (!usuarioDTO.getContaAdmin())
            usuarioDTO.setConta(this.contaService.salvar());

        usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
        usuario = this.usuarioRepository.save(usuario);
        usuario.setSenha(usuarioDTO.getSenha());

        usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        usuarioDTO.setCpfCnpj(usuario.getPessoa().getCpfCnpj());
        usuarioDTO.setPessoaDTO(modelMapper.map(usuario.getPessoa(), PessoaDTO.class));

        return usuarioDTO;
    }

}
