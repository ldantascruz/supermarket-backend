package br.lcsoftware.supermarket.services;


import br.lcsoftware.supermarket.dtos.AuthenticationDto;
import br.lcsoftware.supermarket.dtos.LoginResponseRecordDto;
import br.lcsoftware.supermarket.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtils jwtUtils;

    public LoginResponseRecordDto login(AuthenticationDto authDto){
        // Cria mecanismo de credencial para o Spring
        UsernamePasswordAuthenticationToken userAuth = new UsernamePasswordAuthenticationToken(authDto.email(), authDto.password());

        // Autentica usuário
        Authentication authentication = authenticationManager.authenticate(userAuth);

        // Recupera informações do usuário autenticado
        UserDetailsImpl userAuthentication = (UserDetailsImpl)authentication.getPrincipal();

        // Gera token JWT
        String token = jwtUtils.generateTokenFromUserDetailsImpl(userAuthentication);

        // Cria resposta com informações do usuário e token
        return new LoginResponseRecordDto(
                userAuthentication.getIdUser(),
                userAuthentication.getUsername(),
                userAuthentication.getName(),
                token
        );
    }
}
