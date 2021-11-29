package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.ErrorDTO;
import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.service.PessoaService;
import com.example.toledoBank.api.service.UsuarioService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @Autowired
    private UsuarioService usuarioService;

    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    private PessoaDTO criar(@RequestBody PessoaDTO pessoaDTO){

        return pessoaService.salvar(pessoaDTO);
    }

    @PutMapping("/{id}")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<?> atualizar(@PathVariable Long id , @RequestBody PessoaDTO pessoaDTO) throws JsonProcessingException {

        if(!this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getPessoa().getId(), id)){
            Map<String,String> map = new HashMap<>();
            map.put("error", "Acesso Negado");
            String json = new ObjectMapper().writeValueAsString(map);
            return ResponseEntity.badRequest().body(json);
        }


        return ResponseEntity.status(200).body(pessoaService.alterar(id, pessoaDTO));
    }
}
