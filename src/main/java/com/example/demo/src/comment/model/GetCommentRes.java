package com.example.demo.src.comment.model;

import com.example.demo.src.post.model.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class GetCommentRes {
    private int commentId;
    private int userId;
    private int commentParentId;
    private String userNickname;
    private String userProfileImg;
    private String commentContents;
    private String uploadTime;

    public GetCommentRes(int comment_id, int user_id, int comment_parent_id,
                         String user_nickname, String user_profile_img,
                         String comment_text, String uploadTime) {
        this.commentId = comment_id;
        this.userId = user_id;
        this.commentParentId = comment_parent_id;
        this.userNickname = user_nickname;
        this.userProfileImg = user_profile_img;
        this.commentContents = comment_text;
        this.uploadTime = uploadTime;
    }
}
