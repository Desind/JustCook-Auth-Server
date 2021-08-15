package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.UserType;

import java.util.List;
import java.util.Optional;

public interface CookUserService {
    CookUser saveUser(CookUser cookUser);
    void setUserType(String email, UserType userType);
    Optional<CookUser> getCookUserById(String id);
    CookUser getCookUserByEmail(String email);
    CookUser getCookUserByUsername(String username);
    List<CookUser> getCookUsers();
}
