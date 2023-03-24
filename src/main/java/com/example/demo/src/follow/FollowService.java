package com.example.demo.src.follow;


import com.example.demo.config.BaseException;
import com.example.demo.src.follow.FollowDao;
import com.example.demo.src.follow.FollowProvider;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.follow.model.PostFollowRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.POST_IMG_EMPTY;

@Service
public class FollowService {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    private final FollowDao followDao;
    private final FollowProvider followProvider;

    public FollowService(FollowDao followDao, FollowProvider followProvider) {
        this.followDao = followDao;
        this.followProvider = followProvider;
    }

    public PostFollowRes createFollow(PostFollowReq postFollowReq) throws BaseException {

        try{
            PostFollowRes postFollowRes;
            if(followDao.checkFollows(postFollowReq) != 1){
                postFollowRes = followDao.createFollow(postFollowReq);
            }
            else{
                postFollowRes = followDao.updateFollow(postFollowReq,1); // 팔로우는 1
            }
            return postFollowRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostFollowRes cancelFollow(PostFollowReq postFollowReq) throws BaseException {
        try{
            PostFollowRes postFollowRes = followDao.updateFollow(postFollowReq,0); // 언팔로우는 0
            return postFollowRes;
        } catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
