package com.example.backendapi.post;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PostDto {

    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private int likesNumber;
    private Long ownerId;
    private String ownerName;
    private long commentsNumber;
}
