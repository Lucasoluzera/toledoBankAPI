package com.example.toledoBank.api.seguranca;

import com.example.toledoBank.api.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Service
@Component
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String cpf) throws UsernameNotFoundException {

        if (usuarioRepository.existsByLogin(cpf)) {

            return usuarioRepository.findByLogin(cpf);
        }

        throw new UsernameNotFoundException("CPF: " + cpf + " não encontrado.");
    }



}
