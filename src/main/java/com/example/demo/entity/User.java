package com.example.demo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User  implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long nudoss; // Auto-generated user ID

    private String password;
    private String username;
    private String lastname;
    private String email ;
    private Integer phonenumber;
    private String matcle;
    private String firstname;
    private String userID;
    private String soccle;
    private boolean hasPhoto = false;
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String photo;

    private String dossierType;
    private String language = "FR";



    public void setSoccle(String soccle) {
        if (soccle == null || soccle.length() != 3) {
            throw new IllegalArgumentException("soccle must be exactly 3 characters long");
        }
        this.soccle = soccle;

    }
    public void setMatcle(String matcle) {
        if (matcle == null || matcle.length() != 8) {
            throw new IllegalArgumentException("matcle must be exactly 8 characters long");
        }
        this.matcle = matcle;
    }
    public void setUserId(String mat, String soc) {
        if (mat != null && soc != null) {
            this.userID = "s"+mat+"."+soc;
        }
    }

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private Set<Role> roles;

    @JsonIgnore
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getModel()))
                .collect(Collectors.toList());
    }


    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
