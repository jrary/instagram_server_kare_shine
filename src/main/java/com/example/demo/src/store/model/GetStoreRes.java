package com.example.demo.src.store.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetStoreRes {
    private int storeId;
    private int postId;
    private String postImg;

    public GetStoreRes(int store_id, int post_id, String post_img_url) {
        this.storeId = store_id;
        this.postId = post_id;
        this.postImg = post_img_url;
    }
}
