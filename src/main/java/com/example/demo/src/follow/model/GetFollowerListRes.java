package com.example.demo.src.follow.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class GetFollowerListRes {

    private int userId;
    private List<Integer> followerId = new ArrayList<>();;

    public GetFollowerListRes (List<Follow> follows) {
        this.userId = follows.get(0).getFolloweeId();
        for (Follow follow : follows) {
            this.followerId.add(follow.getFollowerId());
        }
    }

}



