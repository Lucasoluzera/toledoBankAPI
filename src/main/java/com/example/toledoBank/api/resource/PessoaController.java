package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.ErrorDTO;
import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Pessoa;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.service.PessoaService;
import com.example.toledoBank.api.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/pessoa")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private ResponseEntity<?> criar(@RequestBody PessoaDTO pessoaDTO){
        if (pessoaService.existePorCPF(pessoaDTO.getCpfCnpj()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pessoa já existe com esse CPF.");


        return ResponseEntity.ok(pessoaService.salvar(pessoaDTO));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<?> atualizar(@PathVariable Long id , @RequestBody PessoaDTO pessoaDTO) throws JsonProcessingException {

        if(usuarioService.usuarioLogado() != null && !this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getPessoa().getId(), id))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");


        if (pessoaService.existePorCPF(pessoaDTO.getCpfCnpj()))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Pessoa já existe com esse CPF.");



        if(this.pessoaService.buscarPessoaId(id).isPresent()){
            pessoaDTO.setId(id);
            return ResponseEntity.ok().body(pessoaService.alterar(pessoaDTO));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa inexistente");
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<?> excluir(@PathVariable Long id) throws JsonProcessingException {

        if(usuarioService.usuarioLogado() != null && !this.usuarioService.usuarioLogado().getContaAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");

        if(pessoaService.buscarPessoaId(id).isPresent()){
            pessoaService.excluir(id);
            return  ResponseEntity.status(200).body("Sucesso");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Pessoa nao existe");
        }
    }


    @GetMapping
    private ResponseEntity<?> listar() {
        if(usuarioService.usuarioLogado() != null && !usuarioService.usuarioLogado().getContaAdmin())
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Sem permissão.");

        return ResponseEntity.ok(pessoaService.listar());
    }

    @GetMapping("/id/{id}")
    private ResponseEntity<?> buscarPorId(@PathVariable Long id) {
        if (pessoaService.buscarPessoaId(id).isPresent()){
            Pessoa pessoa = pessoaService.buscarPessoaId(id).get();
            PessoaDTO pessoaDTO = modelMapper.map(pessoa, PessoaDTO.class);
            pessoaDTO.setTelefoneDTO(pessoa.getTelefone().getNumero());
            return ResponseEntity.ok(pessoaDTO);
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário com esse ID encontrado.");
    }

    @GetMapping("/cpf/{cpf}")
    private ResponseEntity<?> buscarPorCPF(@PathVariable String cpf) {
        if (pessoaService.buscarPorCPF(cpf) != null){
            Pessoa pessoa = pessoaService.buscarPorCPF(cpf);
            PessoaDTO pessoaDTO = modelMapper.map(pessoa, PessoaDTO.class);
            pessoaDTO.setTelefoneDTO(pessoa.getTelefone().getNumero());
            return ResponseEntity.ok(pessoaDTO);
        }
        else
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Nenhum usuário com esse CPF encontrado.");
    }
}
