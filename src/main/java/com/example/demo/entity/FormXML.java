    package com.example.demo.entity;

    import jakarta.persistence.*;
    import lombok.*;

    @Entity
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public class FormXML {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Column(unique = true)
        private String name; // previously "label" – now represents OBJECT Name

        @Lob
        @Column(columnDefinition = "TEXT")
        private String xmlContent;
    }
