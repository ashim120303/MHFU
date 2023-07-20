package com.pack.v2.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 6, max = 254)
    @Email
    @NotEmpty
    @Column(nullable = false, unique = true)
    private String email;

    @NotEmpty
    @Size(min = 3, max = 15)
    @Column(nullable = false, unique = true)
    private String username;

    @NotEmpty
    @Size(min = 8, max = 128)
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[0-9]).*$", message = "В пароле должны присутствовать английские буквы и цифры")
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Post> posts;

    // Геттеры и сеттеры
    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}
    public String getUsername() {return username;}
    public void setUsername(String username) {this.username = username;}
    public String getPassword() {return password;}
    public void setPassword(String password) {this.password = password;}
    public List<Post> getPosts() {return posts;}
    public void setPosts(List<Post> posts) {this.posts = posts;}

    // Конструкторы
    public User() {}
    public User(String username, String email) {
        this.username = username;
        this.email = email;
    }
}