package com.example.demo.src.like.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;



@Getter
@Setter
@AllArgsConstructor
public class GetPostLikeListRes {

    private int postId;
    private List<Integer> userIdList = new ArrayList<>();;

    public GetPostLikeListRes (List<Like> likes) {
        this.postId = likes.get(0).getPostId();
        System.out.println(likes);
        for (Like like : likes) {
            this.userIdList.add(like.getUserId());
        }
    }

    @Override
    public String toString() {
        return "GetPostLikeListRes{" +
                "postId=" + postId +
                ", userIdList=" + userIdList +
                '}';
    }
}



