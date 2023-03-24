package com.example.demo.src.post.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PostPostsReq {
    private int userId;
    private String postText;
    private String[] postImg;
}

//{"userId":2,
// "postText":안녕하세요,
// "postImg": {"image.jpg","image2.png"
//
// }
// }
