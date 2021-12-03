package com.example.toledoBank.api.service.impl;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.service.ContaService;
import com.example.toledoBank.api.service.PessoaService;
import com.example.toledoBank.api.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

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
                this.pessoaService.salvar(usuarioDTO.getPessoaDTO());
                usuario.setPessoa(this.pessoaService.buscarPorCPF(usuarioDTO.getCpfCnpj()));
            }else {
                usuario.setPessoa(this.pessoaService.buscarPorCPF(usuarioDTO.getCpfCnpj()));
            }
        }


        if (!usuarioDTO.getContaAdmin())
            usuario.setConta(this.contaService.salvar());

        usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
        usuario = this.usuarioRepository.save(usuario);
        usuario.setSenha(usuarioDTO.getSenha());

        return converterDTO(usuarioDTO, usuario);
    }


    @Override
    public UsuarioDTO alterar(UsuarioDTO usuarioDTO) {
        Usuario usuario = modelMapper.map(usuarioDTO, Usuario.class);

        if(usuarioRepository.findById(usuarioDTO.getId()).isPresent()){
            Usuario usuarioBanco =  usuarioRepository.findById(usuarioDTO.getId()).get();


            Pessoa pessoa = usuario.getPessoa();
            pessoa.setId(usuarioBanco.getPessoa().getId());

            PessoaDTO pessoaDTO = modelMapper.map(pessoa, PessoaDTO.class);

            if(pessoa.getTelefone() != null)
                pessoaDTO.setTelefoneDTO(pessoa.getTelefone().getNumero());

            this.pessoaService.alterar(pessoaDTO);

            usuario.setPessoa(pessoa);
            if(usuario.getSenha() != null)
                usuario.setSenha(new BCryptPasswordEncoder().encode(usuario.getSenha()));
            usuario = this.usuarioRepository.save(usuario);
            usuario.setSenha(usuarioDTO.getSenha());

            return converterDTO(usuarioDTO, usuario);
        }
        throw new RuntimeException("Id não encontrado");
    }

    private UsuarioDTO converterDTO(UsuarioDTO usuarioDTO, Usuario usuario) {

        usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
        usuarioDTO.setCpfCnpj(usuario.getPessoa().getCpfCnpj());
        usuarioDTO.setPessoaDTO(modelMapper.map(usuario.getPessoa(), PessoaDTO.class));

        return usuarioDTO;
    }

    @Override
    public void excluir(Long id) {
        Usuario usuario = usuarioRepository.findById(id).get();
        usuarioRepository.deleteById(id);
        if(usuario.getConta() != null)
            contaService.excluir(usuario.getConta().getId());
    }

    @Override
    public Usuario usuarioLogado() {
        return usuarioRepository.findByLogin(SecurityContextHolder.getContext().getAuthentication().getName());
    }

    @Override
    public Optional<Usuario> buscarUsuarioId(Long id) {
        return usuarioRepository.findById(id);
    }

    @Override
    public Usuario buscarUsuarioPorCpf(String cpf) {
        return this.usuarioRepository.findByLogin(cpf);
    }

    @Override
    public List<Usuario> listar() {
        return usuarioRepository.findAll();
    }

}
