package com.example.demo.src.post.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class GetPostRes {
    private int postId;
    private int userId;
    private String postText;
    private int postStatus;
    private String postCreateAt; // 몇일전 이렇게 올 수 있음.
    private List<Integer> postImgId;
    private List<String> postImgUrl;

    public GetPostRes() {
    }

    public GetPostRes(Post post) {
        this.postId = post.getPostId();
        this.userId = post.getUserId();
        this.postText = post.getPostText();
        this.postStatus = post.getPostStatus();
        this.postCreateAt = post.getPostCreateAt();
        this.postImgId = new ArrayList<>();
//        this.postImgId.add(post.getPostImgId());
        this.postImgUrl = new ArrayList<>();
//        this.postImgUrl.add(post.getPostImgUrl());
    }

    public List<GetPostRes>getPostsResList(List<Post> posts){
//        List<Integer> postImgId = new ArrayList<>();
//        List<String> postImgUrl = new ArrayList<>();
        Post firstPost = posts.get(0);
        int checkPostId = firstPost.getPostId();
        GetPostRes getPostRes = new GetPostRes(firstPost);
        List<GetPostRes> getPostReses = new ArrayList<>();
        for(Post post : posts){
            if(checkPostId == post.getPostId()){
                getPostRes.postImgId.add(post.getPostImgId());
                getPostRes.postImgUrl.add(post.getPostImgUrl());
            }else {
                getPostReses.add(getPostRes);
//                System.out.println(getPostReses);
                getPostRes = new GetPostRes(post);
                checkPostId = post.getPostId();
                getPostRes.postImgId.add(post.getPostImgId());
                getPostRes.postImgUrl.add(post.getPostImgUrl());

            }
        }
        getPostReses.add(getPostRes);
        return getPostReses;
    }



}
