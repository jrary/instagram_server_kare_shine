package com.example.demo.src.comment.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PostCommentReq {
    private int commentParentId;
    private String commentContents;
}
