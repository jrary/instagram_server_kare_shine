package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.src.like.model.PostLikesReq;
import com.example.demo.src.like.model.PostLikesRes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
@Slf4j
public class LikeService {

    private PostLikeDao postLikeDao;

    public LikeService(PostLikeDao postLikeDao) {
        this.postLikeDao = postLikeDao;
    }
    public PostLikesRes createLike(PostLikesReq postLikeReq) throws BaseException {
        try{
            PostLikesRes postLikeRes;
            if(postLikeDao.checkLikes(postLikeReq) != 1){
                postLikeRes = postLikeDao.createLike(postLikeReq);
            }
            else{
                postLikeRes = postLikeDao.updateLike(postLikeReq,1); // 팔로우는 1
            }
//            log.info(postLikeRes.toString());
            return postLikeRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
    

    public PostLikesRes cancelLike(PostLikesReq postLikeReq) throws BaseException {
        try{
            PostLikesRes postLikeRes = postLikeDao.updateLike(postLikeReq,0); // 언팔로우는 0
            return postLikeRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
