package com.justcook.authserver.service.interfaces;

import com.justcook.authserver.dto.NewUserDto;
import com.justcook.authserver.model.User.CookUser;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface CookUserService {
    CookUser saveUser(NewUserDto cookUser);
    void giveUserRole(String email, String userRole);
    Optional<CookUser> getCookUserById(String id);
    CookUser getCookUserByEmail(String email);
    CookUser getCookUserByUsername(String username);
    List<CookUser> getCookUsers();
    void tokenRefresh(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void likeRecipe(String email, String id);
    boolean dislikeRecipe(String email, String id);
    String getUsernameFromId(String id);
    List<String> getUserFavouriteRecipes(String id);
}
