package com.example.demo2.model;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name = "tutorials")
public class Tutorial {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @NotBlank(message = "field may not be blank")
    @Size(min=3,max=20)
    @Column(name = "title")
    private String title;

    @NotBlank(message = "field may not be blank")
    @Column(name = "description")
    private String description;

    @Column(name = "published")
    private boolean published;

    @NotBlank(message = "field may not be blank")
    @Pattern(regexp = "^[A-Za-z0-9+_.-]+@(.+)$")
    @Column(name = "email")
    private String email;

    public Tutorial() {

    }

    public Tutorial(String title, String description, boolean published, String email) {
        this.title = title;
        this.description = description;
        this.published = published;
        this.email = email;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublished() {
        return published;
    }

    public void setPublished(boolean isPublished) {
        this.published = isPublished;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}