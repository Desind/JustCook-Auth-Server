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
    //TODO: PROFILOWE, LISTA ULUBIONYCH PRZEPISÓW (ID)
    private List<Allergens> allergies;

    public CookUser(String username,
                    String email,
                    String password,
                    LocalDateTime registrationDate,
                    List<UserRole> userRole,
                    UserStatus status,
                    List<Allergens> allergies) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.userRoles = userRole;
        this.status = status;
        this.allergies = allergies;
    }
}
