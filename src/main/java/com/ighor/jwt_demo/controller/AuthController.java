package com.ighor.jwt_demo.controller;

import com.ighor.jwt_demo.config.SecurityConfig;
import com.ighor.jwt_demo.config.TokenConfig;
import com.ighor.jwt_demo.dto.request.LoginRequest;
import com.ighor.jwt_demo.dto.request.RegisterUserRequest;
import com.ighor.jwt_demo.dto.response.LoginResponse;
import com.ighor.jwt_demo.dto.response.RegisterUserResponse;
import com.ighor.jwt_demo.entity.User;
import com.ighor.jwt_demo.repository.UserRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final TokenConfig tokenConfig;

    public AuthController(UserRepository userRepository ,PasswordEncoder passwordEncoder, AuthenticationManager authenticationManager, TokenConfig tokenConfig){
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.tokenConfig = tokenConfig;
    }

    @PostMapping("/login")
    //Recebe e valida o JSON do login
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest loginRequest){
        //Cria o token de autenticação interno do Spring
        //Spring Security só aceita login no formato UsernamePasswordAuthenticationToken
        UsernamePasswordAuthenticationToken userAndPass = new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password());
        //Autentica no Spring Security:
        //Busca o usuário no banco (UserDetailsService)
        //Compara a senha com BCrypt (PasswordEncoder)
        //se estiver certo: retorna um objeto Authentication
        //se estiver errado: lança BadCredentialsException
        Authentication authentication = authenticationManager.authenticate(userAndPass);

        //Recupera o usuário autenticado do banco de dados
        User user = (User) authentication.getPrincipal();
        //Gera o JWT para o usuário logado
        String token = tokenConfig.generateToken(user);
        //Retorna o token no body
        //retorna uma resposta HTTP 200 (OK) contendo um objeto JSON com o token dentro
        return ResponseEntity.ok(new LoginResponse(token));
    }

    @PostMapping("/register")
    //Recebe uma requisição HTTP POST com um JSON no corpo (@RequestBody).
    //O JSON é convertido para o objeto RegisterUserRequest.
    public ResponseEntity<RegisterUserResponse> register(@Valid @RequestBody RegisterUserRequest userRequest){
        //Cria um novo usuário
        User newUser = new User();
        //Copia os dados da requisição para o usuário
        //passwordEncoder.encode() esta criptografando a senha
        newUser.setPassword(passwordEncoder.encode(userRequest.password()));
        newUser.setEmail(userRequest.email());
        newUser.setName(userRequest.name());

        //Salva no banco de dados
        userRepository.save(newUser);


        //Retorna uma resposta HTTP 201 (Created)
        //Responde 201 CREATED, dizendo que o usuário foi criado.
        return ResponseEntity.status(HttpStatus.CREATED).body(new RegisterUserResponse(newUser.getName(), newUser.getEmail()));
    }
}