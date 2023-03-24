package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GetHomeUserInfoRes {
    private int userId;
    private String userName;
    private String userNickname;
    private String profileImgUrl;
}
