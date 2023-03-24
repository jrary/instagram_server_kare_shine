package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostJoinReq {
    private String userName;
    private String userNickname;
    private String userPhone;
    private String userPwd;
    private String userEmail;
    private String userProfileImg;
}
