package com.justcook.authserver.api;

import com.justcook.authserver.dto.CookUserDto;
import com.justcook.authserver.dto.RecipeDto;
import com.justcook.authserver.dto.RoleToUserDto;
import com.justcook.authserver.model.User.UserRole;
import com.justcook.authserver.service.interfaces.CookUserService;
import com.justcook.authserver.service.interfaces.RecipeService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/admin")
@AllArgsConstructor
@Slf4j
public class AdminController{

    public final CookUserService cookUserService;
    public final RecipeService recipeService;

    @GetMapping("/users")
    public ResponseEntity<List<CookUserDto>> getAllCookUsers(@RequestParam String username, @RequestParam List<UserRole> role){
        List<CookUserDto> cookUsers = cookUserService.findByUsernameContainsAndRole(username, role);
        return ResponseEntity.status(200).body(cookUsers);
    }

    @PatchMapping("/user-role")
    public ResponseEntity<?> giveCookUserRole(@RequestBody RoleToUserDto form){
        cookUserService.giveUserRole(form.getEmail(), form.getUserRole());
        return ResponseEntity.status(200).build();
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> deleteUser(@RequestParam String id){
        cookUserService.deleteUser(id);
        return ResponseEntity.status(200).build();
    }

    @GetMapping("/recipes")
    public ResponseEntity<List<RecipeDto>> getAllRecipes(@RequestParam String name, @RequestParam String email){
        List<RecipeDto> recipes = recipeService.getAdminRecipes(name, email);
        return ResponseEntity.status(200).body(recipes);
    }

    @DeleteMapping("/recipe/{id}")
    public ResponseEntity<?> deleteRecipe(@PathVariable String id){
        recipeService.deleteRecipe(id);
        return ResponseEntity.status(200).build();
    }
}
