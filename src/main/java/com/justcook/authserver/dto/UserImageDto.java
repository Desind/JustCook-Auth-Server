package com.justcook.authserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NonNull;

@Data
@AllArgsConstructor
@NonNull
public class UserImageDto {
    String image;
}
