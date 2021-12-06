package com.example.toledoBank.api.resource;

import com.example.toledoBank.api.dto.UsuarioDTO;
import com.example.toledoBank.api.model.Usuario;
import com.example.toledoBank.api.repository.UsuarioRepository;
import com.example.toledoBank.api.seguranca.JwtTokenUtil;
import com.example.toledoBank.api.seguranca.JwtUserDetailsService;
import com.example.toledoBank.api.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class AuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    public JwtTokenUtil jwtTokenUtil;

    @Autowired
    private JwtUserDetailsService userDetailsService;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private ModelMapper modelMapper;




    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<Usuario> createAuthenticationToken(@RequestBody Usuario usuario) throws Exception {

        String senhaFront = usuario.getSenha();

        authenticate(usuario.getLogin(), senhaFront);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(usuario.getLogin());
        usuario = (Usuario) userDetails;
        usuario.setToken(jwtTokenUtil.generateToken(userDetails));

        usuario.setSenha(senhaFront);


        return ResponseEntity.ok(usuario);
    }


    private void authenticate(String cpf, String senha) throws Exception {

        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(cpf, senha));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
