package com.pack.v2.models;

import jakarta.persistence.*;
import java.util.Date;
@Entity
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(columnDefinition = "TEXT")
    private String title;
    @Column(columnDefinition = "MEDIUMTEXT")
    private String text;
    public String imageName;
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @ManyToOne
    @JoinColumn(name="user_id", nullable=false)
    private User user;


    // Геттеры и сеттеры:


    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Long getId() {return id;}
    public void setId(Long id) {this.id = id;}
    public String getTitle() {return title;}
    public void setTitle(String title) {this.title = title;}
    public String getText() {return text;}
    public void setText(String text) {this.text = text;}
    public String getImageName() {return imageName;}
    public void setImageName(String imageName) {this.imageName = imageName;}
    public Date getCreatedDate() {return createdDate;}
    public void setCreatedDate(Date createdDate) {this.createdDate = createdDate;}

    //  Конструкторы:
    public Post() {}
}
