package com.justcook.authserver.dto;

import lombok.Data;

@Data
public class RoleToUserDto {
    private String email;
    private String userRole;
}
