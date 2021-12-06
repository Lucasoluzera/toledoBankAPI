package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.ContaOperacoesDTO;
import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/conta")
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class ContaController {

    @Autowired
    ContaService contaService;


    @PostMapping(value = "/sacar")
    @ResponseStatus(HttpStatus.OK)
    public ContaOperacoesDTO sacar(@RequestBody ContaOperacoesDTO contaOperacoesDTO){
        return contaService.sacar(contaOperacoesDTO);
    }

    @PostMapping(value = "/depositar")
    @ResponseStatus(HttpStatus.OK)
    public ContaOperacoesDTO depositar(@RequestBody ContaOperacoesDTO contaOperacoesDTO){
        return contaService.depositar(contaOperacoesDTO);
    }

    @PostMapping(value = "/transferir")
    @ResponseStatus(HttpStatus.OK)
    public ContaOperacoesDTO transferir(@RequestBody ContaOperacoesDTO contaOperacoesDTO){
        return contaService.transferir(contaOperacoesDTO);
    }
}
