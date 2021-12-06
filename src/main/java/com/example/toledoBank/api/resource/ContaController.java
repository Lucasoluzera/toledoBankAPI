package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.ContaOperacoesDTO;
import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.service.ContaService;
import com.example.toledoBank.api.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@RestController
@RequestMapping("/conta")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContaController {

    @Autowired
    ContaService contaService;

    @Autowired
    UsuarioService usuarioService;


    @PostMapping(value = "/sacar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> sacar(@RequestBody ContaOperacoesDTO contaOperacoesDTO){

        if (this.usuarioService.usuarioLogado() != null && !this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getId(), contaOperacoesDTO.getUsuario().getId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");


        return ResponseEntity.ok(contaService.sacar(contaOperacoesDTO));
    }

    @PostMapping(value = "/depositar")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> depositar(@RequestBody ContaOperacoesDTO contaOperacoesDTO){
        if (this.usuarioService.usuarioLogado() != null && !this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getId(), contaOperacoesDTO.getUsuario().getId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");


        return ResponseEntity.ok(contaService.depositar(contaOperacoesDTO));
    }

    @PostMapping(value = "/transferir")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<?> transferir(@RequestBody ContaOperacoesDTO contaOperacoesDTO){

        if (this.usuarioService.usuarioLogado() != null && !this.usuarioService.usuarioLogado().getContaAdmin() && !Objects.equals(this.usuarioService.usuarioLogado().getId(), contaOperacoesDTO.getUsuario().getId()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Acesso Negado");



        return ResponseEntity.ok(contaService.transferir(contaOperacoesDTO));
    }
}
