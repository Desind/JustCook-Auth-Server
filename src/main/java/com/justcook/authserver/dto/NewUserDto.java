package com.justcook.authserver.dto;

import lombok.Data;

@Data
public class NewUserDto {
    String username;
    String email;
    String password;
}
