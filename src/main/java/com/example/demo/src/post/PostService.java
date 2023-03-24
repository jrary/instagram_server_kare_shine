package com.example.demo.src.post;


import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.PostPostsReq;
import com.example.demo.src.post.model.PostPostsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.POST_IMG_EMPTY;

@Service
public class PostService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final PostDao postDao;
    private final PostProvider postProvider;

    public PostService(PostDao postDao, PostProvider postProvider) {
        this.postDao = postDao;
        this.postProvider = postProvider;
    }


    public PostPostsRes createPost(PostPostsReq postPostsReq) throws BaseException {
        if(postPostsReq.getPostImg() == null){
            throw new BaseException(POST_IMG_EMPTY);
        }
        try{
            PostPostsRes postPostsRes = postDao.createPost(postPostsReq);

            return postPostsRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
