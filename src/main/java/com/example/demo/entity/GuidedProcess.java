package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
public class GuidedProcess {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @OneToMany(mappedBy = "guidedProcess", cascade = CascadeType.ALL)
    @OrderBy("orderIndex ASC")
    @JsonManagedReference
    private List<Step> steps = new ArrayList<>();

    // Getters & Setters
}
