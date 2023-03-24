package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@AllArgsConstructor

public class GetFolloweeListRes {
//    private int followId;
    private int userId;
    private List<Integer> followeeId = new ArrayList<>();
//    private int followStatus;
//    private String followCreateAr;


    public GetFolloweeListRes (List<Follow> follows){
//        List<Integer> list = new ArrayList<>();
        this.userId = follows.get(0).getFollowerId();
        for(Follow follow : follows){
            this.followeeId.add(follow.getFolloweeId());
        }
//        return new GetFolloweeListRes(follows.get(0).getFollowerId(),list);
    }

}



