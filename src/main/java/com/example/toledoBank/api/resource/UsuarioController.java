package com.example.toledoBank.api.resource;


import com.example.toledoBank.api.dto.ErrorDTO;
import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/usuario")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<?> criar(@RequestBody @Valid UsuarioDTO usuarioDTO) {

        if (usuarioService.buscarUsuarioPorCpf(usuarioDTO.getCpfCnpj()) != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login já existe.");

        if (this.usuarioService.usuarioLogado() != null && !this.usuarioService.usuarioLogado().getContaAdmin() && usuarioDTO.getContaAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");

        return ResponseEntity.ok(usuarioService.save(usuarioDTO));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<?> excluir(@PathVariable Long id) throws JsonProcessingException {

        if (this.usuarioService.usuarioLogado() != null && !this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getId(), id))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");

        if (usuarioService.buscarUsuarioId(id).isPresent()) {
            usuarioService.excluir(id);
            return ResponseEntity.status(200).body("sucesso");
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao existe");
        }

    }

    @PutMapping("/{id}")
    private ResponseEntity<?> alterar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) throws JsonProcessingException {

        if (usuarioService.usuarioLogado() != null && !this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getId(), id))
            return ResponseEntity.badRequest().body("Usuario sem permissao");

        if (usuarioService.buscarUsuarioPorCpf(usuarioDTO.getCpfCnpj()) != null)
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Login já existe.");


        if (usuarioService.buscarUsuarioId(id).isPresent()) {
            usuarioDTO.setId(id);
            return ResponseEntity.ok().body(usuarioService.alterar(usuarioDTO));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao existe");
        }
    }

    @GetMapping
    private ResponseEntity<?> listar() {

        if(usuarioService.usuarioLogado() == null && !usuarioService.usuarioLogado().getContaAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sem permissão.");

        List<Usuario> usuarioList = new ArrayList<>();

        usuarioService.listar().forEach(
                usuario ->{
                    usuario.setSenha(null);
                    usuarioList.add(usuario);
                }
        );

        return ResponseEntity.ok(usuarioList);
    }

    @GetMapping("/id/{id}")
    private ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (usuarioService.buscarUsuarioId(id).isPresent()) {
            Usuario usuario = usuarioService.buscarUsuarioId(id).get();
            UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
            usuarioDTO.setPessoaDTO(modelMapper.map(usuario.getPessoa(), PessoaDTO.class));
            usuarioDTO.setSenha(null);
            return ResponseEntity.ok(usuarioDTO);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário com esse ID encontrado.");
    }

    @GetMapping("/cpf/{cpf}")
    private ResponseEntity<?> buscarPorCPF(@PathVariable String cpf) {
        if (usuarioService.buscarUsuarioPorCpf(cpf) != null) {
            Usuario usuario = usuarioService.buscarUsuarioPorCpf(cpf);
            UsuarioDTO usuarioDTO = modelMapper.map(usuario, UsuarioDTO.class);
            usuarioDTO.setPessoaDTO(modelMapper.map(usuario.getPessoa(), PessoaDTO.class));
            usuarioDTO.setSenha(null);
            return ResponseEntity.ok(usuarioDTO);
        } else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário com esse CPF encontrado.");
    }
}
