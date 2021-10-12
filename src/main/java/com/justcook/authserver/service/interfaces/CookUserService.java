package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.UserRole;

import java.util.List;
import java.util.Optional;

public interface CookUserService {
    CookUser saveUser(CookUser cookUser);
    void giveUserRole(String email, String userRole);
    Optional<CookUser> getCookUserById(String id);
    CookUser getCookUserByEmail(String email);
    CookUser getCookUserByUsername(String username);
    List<CookUser> getCookUsers();
}
