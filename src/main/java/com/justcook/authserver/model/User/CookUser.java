package com.justcook.authserver.model.User;

import com.justcook.authserver.model.Allergens;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
@NoArgsConstructor
public class CookUser {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private List<UserRole> userRoles;
    private UserStatus status;
    private String image;
    private List<String> favouriteRecipes;
    private List<Allergens> allergies;
}
