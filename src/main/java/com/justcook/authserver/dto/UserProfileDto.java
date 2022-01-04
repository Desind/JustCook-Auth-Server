package com.justcook.authserver.dto;

import com.justcook.authserver.model.Allergens;
import com.justcook.authserver.model.User.UserRole;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserProfileDto {
    private String id;
    private String username;
    private String email;
    private LocalDateTime registrationDate;
    private List<UserRole> userRoles;
    private String image;
    private List<String> favouriteRecipes;
    private List<Allergens> allergies;
}
