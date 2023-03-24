package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Like {
    private int likeId;
    private int postId;
    private int userId;
    private int likeStatus;
    private String likeCreateAt;
}
