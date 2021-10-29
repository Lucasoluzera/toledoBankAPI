package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.PessoaDTO;
import com.example.toledoBank.api.service.PessoaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pessoa")
public class PessoaController {

    @Autowired
    private PessoaService pessoaService;

    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.CREATED)
    private PessoaDTO criar(@RequestBody PessoaDTO pessoaDTO){

        return pessoaService.save(pessoaDTO);
    }
}
