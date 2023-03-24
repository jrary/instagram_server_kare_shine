package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.GetPostLikeCountRes;
import com.example.demo.src.like.model.GetPostLikeListRes;
import com.example.demo.src.like.model.Like;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class LikeProvider {

    private PostLikeDao postLikeDao;

    public LikeProvider(PostLikeDao postLikeDao) {
        this.postLikeDao = postLikeDao;
    }

    public GetPostLikeListRes getPostLikeList(int postId) throws BaseException {
        try{
            List<Like> postLikeList = postLikeDao.getPostLikeList(postId);
            GetPostLikeListRes newList = new GetPostLikeListRes(postLikeList);
            return newList;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetPostLikeCountRes getPostlikeCount(int postId) throws BaseException {
        try{
            GetPostLikeCountRes getLikeCount = postLikeDao.getPostLikeCount(postId);
            return getLikeCount;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
