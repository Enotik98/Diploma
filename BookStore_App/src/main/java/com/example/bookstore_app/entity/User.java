package com.example.bookstore_app.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@org.hibernate.annotations.TypeDef(name = "user_role", typeClass = SQLEnumType.class)

@Table(name = "users", schema = "public")
@Data
@Entity(name = "users")
@Getter
@Setter

public class User {
    @Id
    @Column(name = "id", updatable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Column(name = "p_salt")
    private String p_salt;

    @Column(name = "first_name")
    private String first_name;
    @Column(name = "last_name")
    private String last_name;
    @Column(name = "phone")
    private String phone;
    @Column(name = "blocked")
    private Boolean isBlocked;
    @javax.persistence.Enumerated(javax.persistence.EnumType.STRING)
    @Type(type = "user_role")
    @Column(name = "role")
    private Role role;
    @Column(name = "created_at")
    private Date created_at;
    @OneToMany(mappedBy = "user")
    private List<Sale> sales;
    @PrePersist
    public void setDefaultValues(){
        if (role == null) {
            role = Role.CLIENT;
        }
        if (isBlocked == null){
            isBlocked = false;
        }
    }

}
