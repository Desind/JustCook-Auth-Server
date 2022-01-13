package com.justcook.authserver.api;

import com.justcook.authserver.dto.NewUserDto;
import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.dto.RoleToUserDto;
import com.justcook.authserver.service.interfaces.CookUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.*;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@Controller
@RequestMapping("/api")
@AllArgsConstructor
@Slf4j
public class CookUserController {
    public final CookUserService cookUserService;


    //201 poprawne utworzenie użytkownika
    //403 email albo username zajęte
    @PostMapping("/user")
    public ResponseEntity<CookUser> saveNewCookUser(@RequestBody NewUserDto newUserDto){
        if (cookUserService.saveUser(newUserDto) != null){
            return ResponseEntity.status(201).build();
        }
        return ResponseEntity.status(403).build();
    }

    @GetMapping("/users")
    public ResponseEntity<List<CookUser>> getAllCookUsers(){
        List<CookUser> cookUsers = cookUserService.getCookUsers();
        return ResponseEntity.status(200).body(cookUsers);
    }

    @PostMapping("/user-role")
    public ResponseEntity<?> giveCookUserRole(@RequestBody RoleToUserDto form){
        cookUserService.giveUserRole(form.getEmail(), form.getUserRole());
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/refresh-token")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        cookUserService.tokenRefresh(request, response);
    }

    @PutMapping("/like-recipe/{id}")
    public ResponseEntity<?> likeRecipe(HttpServletRequest request, @PathVariable String id){
        cookUserService.likeRecipe(String.valueOf(request.getAttribute("username")),id);
        return ResponseEntity.status(200).build();
    }
    @DeleteMapping("/dislike-recipe/{id}")
    public ResponseEntity<?> dislikeRecipe(HttpServletRequest request, @PathVariable String id){
        if(cookUserService.dislikeRecipe(String.valueOf(request.getAttribute("username")),id)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(404).build();
    }

    @GetMapping("/permission-check")
    public ResponseEntity<?> permissionCheck(){
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/admin-permission-check")
    public ResponseEntity<?> adminPermissionCheck(){
        return ResponseEntity.status(200).build();
    }
}
