package com.example.backendapi.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CommentDto {

    private Long postId;
    private Long id;
    private String content;
    private Long ownerId;
    private String ownerName;
    private LocalDateTime createdAt;
    private int likesNumber;

}
