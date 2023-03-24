package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.src.follow.model.GetFolloweeListRes;
import com.example.demo.src.follow.model.GetFollowerListRes;
import com.example.demo.src.follow.model.Follow;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

@Service
public class FollowProvider {

    private FollowDao followDao;

    public FollowProvider(FollowDao followDao) {
        this.followDao = followDao;
    }

    public GetFollowerListRes getFollowerList(int userId) throws BaseException {
        try{
            List<Follow> followers = followDao.getFollowers(userId);
            GetFollowerListRes followerlist = new GetFollowerListRes(followers);
            return followerlist;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetFolloweeListRes getFolloweeList(int userId) throws BaseException {
        try{
            List<Follow> followees = followDao.getFollowees(userId);
            GetFolloweeListRes getFolloweeListRes = new GetFolloweeListRes(followees);
            return getFolloweeListRes;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

}
