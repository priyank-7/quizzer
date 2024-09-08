package com.quizzer.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.Set;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "users")
public class User {

    // $2a$10$qtsTcGYdrq/452kTaX.gH.5fNF0mGzvtYzsP2xxuq8KOLDF9rQe5G
    // asd@gmail.com
    @Id
    @Column(name = "user_id")
    private String user_id;
    @Column(unique = true)
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    private String email;
    @NotBlank(message = "Password cannot be blank")
    private String password;
    @Getter
    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "role_id")
    )
    @JsonManagedReference(value = "users")
    private Set<Role> roles;

    //private String provider;

    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }
    public void setRoles(Role role) {
        this.roles.add(role);
    }
}
