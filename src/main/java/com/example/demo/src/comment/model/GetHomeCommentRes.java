package com.example.demo.src.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GetHomeCommentRes {
    private int commentId;
    private int userId;
    private int commentParentId;
    private String userNickname;
    private String commentContents;

    public GetHomeCommentRes(int comment_id, int user_id, int comment_parent_id,
                             String user_nickname, String comment_text) {
        this.commentId = comment_id;
        this.userId = user_id;
        this.commentParentId = comment_parent_id;
        this.userNickname = user_nickname;
        this.commentContents = comment_text;
    }
}
