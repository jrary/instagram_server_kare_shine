package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLikesRes {
    private int likeId;
    private int likeStatus;

    @Override
    public String toString() {
        return "PostLikesRes{" +
                "likeId=" + likeId +
                ", likeStatus=" + likeStatus +
                '}';
    }
}
