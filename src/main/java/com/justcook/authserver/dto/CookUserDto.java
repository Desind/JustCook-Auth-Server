package com.justcook.authserver.dto;

import com.justcook.authserver.model.User.UserRole;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CookUserDto {
    private String id;
    private String username;
    private String email;
    private LocalDateTime registrationDate;
    private Integer recipesCreated;
    private List<UserRole> userRoles;
}
