package com.example.demo.src.user.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class User {
    private int userId;
    private String userName;
    private String userNickname;
    private String userPhone;
    private String userPwd;
    private String userEmail;
    private int userPublic;
    private int userActive;
}
