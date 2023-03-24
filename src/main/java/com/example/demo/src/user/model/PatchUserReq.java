package com.example.demo.src.user.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PatchUserReq {
    private String userName;
    private String userWebsite;
    private String userText;
    private String userEmail;
    private String userPhone;
    private String userGender;
}
