package com.example.demo.src.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class StoreRes {
    private String status;
    private int postId;

    public StoreRes(String status, int post_id) {
        this.status = status;
        this.postId = post_id;
    }
}
