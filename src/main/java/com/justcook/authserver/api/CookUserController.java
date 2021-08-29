package com.justcook.authserver.api;

import com.justcook.authserver.model.User.CookUser;
import com.justcook.authserver.model.User.RoleToUserForm;
import com.justcook.authserver.service.interfaces.CookUserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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
    public ResponseEntity<List<CookUser>> getAllCookUsers(HttpServletRequest request){
        //PRZYKŁADOWE ODEBRANIE INFORMACJI O UŻYTKOWNIKU Z TOKENA
        String username = String.valueOf(request.getAttribute("username"));
        log.warn("Username from token: " + username);
        List<CookUser> cookUsers = cookUserService.getCookUsers();
        return ResponseEntity.status(200).body(cookUsers);
    }

    @PostMapping("/role/add")
    public ResponseEntity<?> giveCookUserRole(@RequestBody RoleToUserForm form){
        log.info(form.toString());
        cookUserService.giveUserRole(form.getEmail(), form.getUserRole());
        return ResponseEntity.status(200).build();
    }

}
