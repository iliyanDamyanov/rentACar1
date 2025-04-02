package org.rentacar1.app.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterRequest {
    @Size(min = 6, message = "Username must be at least 6 symbols")
    private String userName;
    @Size(min = 6, message = "Password must be at least 6 symbols")
    private String password;
    @NotNull
    @NotBlank(message = "Email must not be blank")
    private String email;
}
