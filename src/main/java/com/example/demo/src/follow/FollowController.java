package com.example.demo.src.follow;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.follow.model.GetFolloweeListRes;
import com.example.demo.src.follow.model.GetFollowerListRes;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.follow.model.PostFollowRes;
import com.example.demo.src.post.model.PostPostsReq;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/app/follows")
public class FollowController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final FollowProvider followProvider;
    @Autowired
    private final FollowService followService;


    public FollowController(FollowProvider followProvider, FollowService followService) {
        this.followProvider = followProvider;
        this.followService = followService;
    }

    /**
     * 생성 API
     * [Post] /follows
     * 팔로우하기 API :
     *
     * @return BaseResponse<PostFollowRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostFollowRes> createFollow(@RequestBody PostFollowReq postFollowReq){
        try {
            PostFollowRes postFollowRes = followService.createFollow(postFollowReq);
            return new BaseResponse<>(postFollowRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 생성 API
     * [Post] /follows
     * 언팔로우하기 API :
     *
     * @return BaseResponse<PostFollowRes>
     */
    @ResponseBody
    @PatchMapping("/unfollows")
    public BaseResponse<PostFollowRes> updateUnFollow(@RequestBody PostFollowReq postFollowReq){
        try {
            PostFollowRes postFollowRes = followService.cancelFollow(postFollowReq);
            return new BaseResponse<>(postFollowRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 조회 API
     * [GET] /follows/users/:userId
     * 유저 아이디 게시글 보기 API : 해당 유저 게시물 목록
     *
     * @return BaseResponse<List < GetFollowRes>>
     */


    @ResponseBody
    @GetMapping("/followers/{myUserId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetFollowerListRes> getFollowers(@PathVariable("myUserId") int userId) {
        // Get Follows
        try {
            GetFollowerListRes getFollowerRes = followProvider.getFollowerList(userId);
            return new BaseResponse<>(getFollowerRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("/followees/{myUserId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<GetFolloweeListRes> getFollowees(@PathVariable("myUserId") int userId) {
        // Get Follows
        try {
            GetFolloweeListRes getFolloweeRes = followProvider.getFolloweeList(userId);
            return new BaseResponse<>(getFolloweeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }
}
