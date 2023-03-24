package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostLikesReq {
    private int postId;
    private int userId;

    @Override
    public String toString() {
        return "PostLikesReq{" +
                "postId=" + postId +
                ", userId=" + userId +
                '}';
    }
}
