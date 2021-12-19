package com.justcook.authserver.dto;

import com.justcook.authserver.model.User.UserRole;
import lombok.Data;

@Data
public class RoleToUserDto {
    private String email;
    private UserRole userRole;
}
