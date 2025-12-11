package com.ighor.jwt_demo.dto.request;

import jakarta.validation.constraints.NotEmpty;

public record RegisterUserRequest(
        @NotEmpty(message = "Nome é obrigatorio")  String name,
        @NotEmpty(message = "Email é obrigatorio") String email,
        @NotEmpty(message = "Senha é obrigatoria") String password
) {

}
