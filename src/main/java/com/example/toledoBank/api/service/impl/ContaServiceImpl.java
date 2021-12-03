package com.example.toledoBank.api.service.impl;

import com.example.toledoBank.api.dto.ContaOperacoesDTO;
import com.example.toledoBank.api.model.Conta;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.ContaRepository;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.service.ContaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ContaServiceImpl implements ContaService {

    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    public ContaServiceImpl(ContaRepository contaRepository, UsuarioRepository usuarioRepository) {
        this.contaRepository = contaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    public Conta salvar() {
        Conta contaBanco = contaRepository.findFirstByOrderByIdDesc();

        if(contaBanco == null)
            contaBanco = Conta.builder().numero(0).build();

        Conta conta = Conta.builder()
                .saldo(BigDecimal.ZERO)
                .agencia(1)
                .numero(contaBanco.getNumero() + 1)
                .build();

        return this.contaRepository.save(conta);
    }

    @Override
    public ContaOperacoesDTO sacar(ContaOperacoesDTO contaOperacoesDTO) {
        Conta conta = contaRepository.findByAgenciaAndNumero(contaOperacoesDTO.getUsuario().getConta().getAgencia(), contaOperacoesDTO.getUsuario().getConta().getNumero());
        conta.setSaldo(conta.getSaldo().subtract(BigDecimal.valueOf(Long.parseLong(contaOperacoesDTO.getSaldo()))));
        contaOperacoesDTO.getUsuario().setConta(this.contaRepository.save(conta));
        return contaOperacoesDTO;
    }

    @Override
    public ContaOperacoesDTO depositar(ContaOperacoesDTO contaOperacoesDTO) {
        Conta conta = contaRepository.findByAgenciaAndNumero(contaOperacoesDTO.getUsuario().getConta().getAgencia(), contaOperacoesDTO.getUsuario().getConta().getNumero());
        conta.setSaldo(conta.getSaldo().add(BigDecimal.valueOf(Long.parseLong(contaOperacoesDTO.getSaldo()))));
        contaOperacoesDTO.getUsuario().setConta(this.contaRepository.save(conta));
        return contaOperacoesDTO;
    }

    @Override
    public ContaOperacoesDTO transferir(ContaOperacoesDTO contaOperacoesDTO) {
        Conta conta = contaRepository.findByAgenciaAndNumero(contaOperacoesDTO.getUsuario().getConta().getAgencia(), contaOperacoesDTO.getUsuario().getConta().getNumero());
        conta.setSaldo(conta.getSaldo().subtract(BigDecimal.valueOf(Long.parseLong(contaOperacoesDTO.getSaldo()))));
        contaOperacoesDTO.getUsuario().setConta(this.contaRepository.save(conta));

        Conta contaSecundaria = usuarioRepository.findByLogin(contaOperacoesDTO.getCpfContaSecundaria()).getConta();
        contaSecundaria.setSaldo(conta.getSaldo().add(BigDecimal.valueOf(Long.parseLong(contaOperacoesDTO.getSaldo()))));
        this.contaRepository.save(contaSecundaria);
        return contaOperacoesDTO;
    }

    @Override
    public void excluir(Long id) {
        contaRepository.deleteById(id);
    }
}
