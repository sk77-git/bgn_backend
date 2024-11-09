package com.skthakur.bgn_backend.model;

import com.skthakur.bgn_backend.enums.ResourceType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "posts")
public class Post {
    private final LocalDateTime createdAt = LocalDateTime.now();
    private LocalDateTime updatedAt;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String title;
    private String description;
    private String author;
    private String authorInfo;
    private String fileName;
    private String filePath;
    @Enumerated(EnumType.STRING)
    private ResourceType resourceType;
    private int semester;

    public Post() {
    }

    public Post(LocalDateTime updatedDateTime, String title, String description, String author, String authorInfo, String fileName, String filePath, ResourceType resourceType, int semester) {
        this.updatedAt = updatedDateTime;
        this.title = title;
        this.description = description;
        this.author = author;
        this.authorInfo = authorInfo;
        this.fileName = fileName;
        this.filePath = filePath;
        this.resourceType = resourceType;
        this.semester = semester;
    }
    
}
