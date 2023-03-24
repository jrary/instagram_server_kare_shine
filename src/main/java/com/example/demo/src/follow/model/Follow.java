package com.example.demo.src.follow.model;

import com.example.demo.src.post.model.GetPostRes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class Follow {
    private int followId;
    private int followerId;
    private int followeeId;
    private int followStatus;
    private String followCreateAr;

    @Override
    public String toString() {
        return "Follow{" +
                "followId=" + followId +
                ", followerId=" + followerId +
                ", followeeId=" + followeeId +
                ", followStatus=" + followStatus +
                ", followCreateAr='" + followCreateAr + '\'' +
                '}';
    }
}
