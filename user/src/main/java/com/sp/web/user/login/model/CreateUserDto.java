package com.sp.web.user.login.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;

@Getter
@Setter
public class CreateUserDto {

    @NotBlank
    private String userId;

    @NotBlank
    private String userPassword;

    private String gender;   // "M", "F"
    private String birthDate; // "YYYY-MM-DD"

}
