package com.example.demo.dtos;


import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String message;

    }
