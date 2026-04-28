package com.duhanvinay.projects.airBnbApp.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UserDto {
    private String email;
    private Long id;
    private String name;
    private String gender;
    private LocalDate dateOfBirth;

}
