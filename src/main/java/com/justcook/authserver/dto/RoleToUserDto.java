package com.justcook.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleToUserDto {
    private String email;
    private String userRole;
}
