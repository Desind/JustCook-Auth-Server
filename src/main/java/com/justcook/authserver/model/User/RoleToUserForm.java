package com.justcook.authserver.model.User;

import lombok.Data;

@Data
public class RoleToUserForm{
    private String email;
    private String userRole;
}
