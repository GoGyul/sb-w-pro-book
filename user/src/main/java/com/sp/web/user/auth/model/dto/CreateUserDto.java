package com.sp.web.user.auth.model.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String userPassword;

    @NotBlank
    private String nickname;

    private String role;
    private String gender;   // "M", "F"
    private String birthDate; // "YYYY-MM-DD"

}
