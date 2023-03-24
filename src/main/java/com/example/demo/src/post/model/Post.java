package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Post {
    private int postId;
    private int userId;
    private String postText;
    private int postStatus;
    private String postCreateAt; // 몇일전 이렇게 올 수 있음.
    private int postImgId;
    private String PostImgUrl;

    @Override
    public String toString() {
        return "Post{" +
                "postId=" + postId +
                ", userId=" + userId +
                ", postText='" + postText + '\'' +
                ", postStatus=" + postStatus +
                ", postCreateAt='" + postCreateAt + '\'' +
                ", postImgId=" + postImgId +
                ", PostImgUrl='" + PostImgUrl + '\'' +
                '}';
    }
}
