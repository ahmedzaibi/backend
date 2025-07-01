package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer orderIndex;

    @ManyToOne
    private FormXML form;

    @ManyToOne
    @JsonBackReference
    private GuidedProcess guidedProcess;

    @Column
    private String objectName;
}

