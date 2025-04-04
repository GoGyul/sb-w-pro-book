package com.sp.web.user.auth.model.entity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserEntity {

    private String userId;
    private String userPassword;
    private String gender;
    private String birthDate;
    private String role;

}
