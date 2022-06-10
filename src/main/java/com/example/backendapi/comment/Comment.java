package com.example.backendapi.comment;

import com.example.backendapi.post.Post;
import com.example.backendapi.user.AppUser;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    @ManyToOne
    @JoinColumn(
            nullable = false,
            name = "owner_id"
    )
    private AppUser owner;
    private LocalDateTime createdAt;
    private int likesNumber = 0;
    @ManyToOne
    @JoinColumn(
            name = "post_id",
            nullable = false
    )
    private Post post;

    public Comment(String content, AppUser owner, LocalDateTime createdAt, int likesNumber) {
        this.content = content;
        this.owner = owner;
        this.createdAt = createdAt;
        this.likesNumber = likesNumber;
    }

    public Comment(String content, AppUser owner, Post post) {
        this.content = content;
        this.owner = owner;
        this.post = post;
        this.createdAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public AppUser getOwner() {
        return owner;
    }

    public void setOwner(AppUser owner) {
        this.owner = owner;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public int getLikesNumber() {
        return likesNumber;
    }

    public void setLikesNumber(int likesNumber) {
        this.likesNumber = likesNumber;
    }

    public Post getPost() {
        return post;
    }

    public void setPost(Post post) {
        this.post = post;
    }
}
