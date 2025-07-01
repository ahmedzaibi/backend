package com.example.demo.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Champ {
    @Id
    long idValue ;
    String champLabel ;
    String champType ;
    String champValue ;
}
