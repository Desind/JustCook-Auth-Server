package com.justcook.authserver.model.User;

import com.justcook.authserver.model.Allergens;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document
public class User {
    @Id
    private String id;
    private String username;
    @Indexed(unique = true)
    private String email;
    private String password;
    private LocalDateTime registrationDate;
    private UserType userType;
    private UserStatus status;
    private List<Allergens> allergies;

    public User(String username,
                String email,
                String password,
                LocalDateTime registrationDate,
                UserType userType,
                UserStatus status,
                List<Allergens> allergies) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.registrationDate = registrationDate;
        this.userType = userType;
        this.status = status;
        this.allergies = allergies;
    }
}
