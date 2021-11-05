package com.justcook.authserver.api;

import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.RoleToUserForm;
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

@Controller
@RequestMapping("/api/user")
@AllArgsConstructor
@Slf4j
public class CookUserController {
    public final CookUserService cookUserService;

    @PostMapping("/new")
    public ResponseEntity<CookUser> saveNewCookUser(@RequestBody CookUser cookUser){
        cookUserService.saveUser(cookUser);
        return ResponseEntity.status(201).build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<CookUser>> getAllCookUsers(){
        List<CookUser> cookUsers = cookUserService.getCookUsers();
        return ResponseEntity.status(200).body(cookUsers);
    }

    @PostMapping("/role/add")
    public ResponseEntity<?> giveCookUserRole(@RequestBody RoleToUserForm form){
        log.info(form.toString());
        cookUserService.giveUserRole(form.getEmail(), form.getUserRole());
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/refreshtoken")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        cookUserService.tokenRefresh(request, response);
    }

    @PutMapping("/likerecipe/{id}")
    public ResponseEntity<?> likeRecipe(HttpServletRequest request, @PathVariable String id){
        cookUserService.likeRecipe(String.valueOf(request.getAttribute("username")),id);
        return ResponseEntity.status(200).build();
    }
    @DeleteMapping("/dislikerecipe/{id}")
    public ResponseEntity<?> dislikeRecipe(HttpServletRequest request, @PathVariable String id){
        if(cookUserService.dislikeRecipe(String.valueOf(request.getAttribute("username")),id)){
            return ResponseEntity.status(HttpStatus.OK).build();
        }
        return ResponseEntity.status(404).build();
    }

    //TODO: ODCZYTYWANIE DANYCH UŻYTKOWNIKA
}
