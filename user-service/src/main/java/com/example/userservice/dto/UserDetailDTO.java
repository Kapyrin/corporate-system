package com.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserDetailDTO {
    private Long id;
    private String name;
    private String email;
    private LocalDateTime createdAt;
}
