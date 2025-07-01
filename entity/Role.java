package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dossierID;

    private String name;
    private String label;

    @Enumerated(EnumType.STRING)
    private RoleCategory category;

    private String parameter;

    @Enumerated(EnumType.STRING)
    private RoleModel model;

    private String modelLabel;
    private String localization;
    private boolean delegable = false;
    private boolean delegated = false;

    @Enumerated(EnumType.STRING)
    private RoleStruct rolStruct;

    private Long activities;
    private void setParemeter(User user) {
        if (user.getMatcle() != null && user.getMatcle().length()>= 4) {
            String first4 = user.getMatcle().substring(0, 4);
            String last3 = user.getMatcle().length() >= 7 ? user.getMatcle().substring(user.getMatcle().length() - 3) : user.getMatcle().substring(user.getMatcle().length() - 1);
            this.parameter = "000000000" + first4 + "-" + last3;
        } else {
            this.parameter = null;
        }
    }
    public void setModel(RoleModel model) {
        this.model = model;
        updateName();
    }
    private void updateName() {
        if (model != null && parameter != null) {
            this.name = model.toString() + "(" + parameter + ")";
        } else {
            this.name = null;
        }
    }
    @ManyToOne
    @JoinColumn(name = "user_id", nullable = true)
    @JsonIgnore
    private User user;

}
