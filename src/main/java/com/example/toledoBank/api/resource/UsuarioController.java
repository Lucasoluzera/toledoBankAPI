package com.example.toledoBank.api.resource;


import com.example.toledoBank.api.dto.ErrorDTO;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;


    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    private UsuarioDTO criar(@RequestBody @Valid UsuarioDTO usuarioDTO){

        return usuarioService.save(usuarioDTO);
    }

    @DeleteMapping("/{id}")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    private ResponseEntity<?> excluir(@PathVariable Long id) throws JsonProcessingException {

        if(!this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getId(), id)){
            return ResponseEntity.badRequest().body("Acesso Negado");
        }

        if(usuarioService.buscarUsuarioId(id).isPresent()){
            usuarioService.excluir(id);
            return  ResponseEntity.status(200).body("sucesso");
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao existe");
        }

    }

    @PutMapping("/{id}")
    @CrossOrigin
    private ResponseEntity<?> alterar(@PathVariable Long id, @RequestBody UsuarioDTO usuarioDTO) throws JsonProcessingException {

        if (!this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getId(), id)) {
            return ResponseEntity.badRequest().body("Usuario sem permissao");
        }

        if(usuarioService.buscarUsuarioId(id).isPresent()){
            usuarioDTO.setId(id);
            return ResponseEntity.ok().body(usuarioService.alterar(usuarioDTO));
        }else{
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario nao existe");
        }
    }
}
