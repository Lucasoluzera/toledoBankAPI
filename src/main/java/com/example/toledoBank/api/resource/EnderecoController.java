package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.model.Endereco;
import com.example.toledoBank.api.service.EnderecoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.nio.file.Path;

@RestController
@RequestMapping("/endereco")
public class EnderecoController {

    @Autowired
    private EnderecoService enderecoService;

    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    private Endereco criar(@RequestBody Endereco endereco){
        return enderecoService.save(endereco);
    }

    @PutMapping("/{id}")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    private Endereco alterar(@RequestBody Endereco endereco, @PathVariable Long id){
        return enderecoService.getById(id).map(
                enderecoBD -> {
                    enderecoBD.setRua(endereco.getRua());
                    enderecoBD.setNumero(endereco.getNumero());
                    enderecoBD.setCep(endereco.getCep());
                    enderecoBD.setBairro(endereco.getBairro());
                    enderecoBD.setCidade(endereco.getCidade());
                    return enderecoBD;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

    }
}
