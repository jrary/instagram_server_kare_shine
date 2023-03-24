package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
public class GetPostLikeCountRes {

    private int postId;
    private int likeCount;

    @Override
    public String toString() {
        return "GetPostLikeCountRes{" +
                "postId=" + postId +
                ", likeCount=" + likeCount +
                '}';
    }
}



