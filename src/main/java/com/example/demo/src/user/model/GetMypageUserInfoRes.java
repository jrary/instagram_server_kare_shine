package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetMypageUserInfoRes {
    private int userId;
    private String userName;
    private String userNickname;
    private String profileImgUrl;
    private int postNum;
    private int followerNum;
    private int followingNum;
    private String userText;
    private String userWebsite;
    private int userPublic;
}
