package com.justcook.authserver.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.ZonedDateTime;
import java.util.List;

@Data
@Document
public class User {
    @Id
    private String id;
    private String username;
    private String email;
    private String password;
    private ZonedDateTime registrationDate;
    private UserType userType;
    private List<Allergies> allergies;
}
