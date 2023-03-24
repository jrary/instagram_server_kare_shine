package com.example.demo.src.like;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.like.model.GetPostLikeCountRes;
import com.example.demo.src.like.model.GetPostLikeListRes;
import com.example.demo.src.like.model.PostLikesReq;
import com.example.demo.src.like.model.PostLikesRes;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/app/likes")
@Slf4j
public class LikeController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final LikeProvider likeProvider;
    @Autowired
    private final LikeService likeService;


    public LikeController(LikeProvider likeProvider, LikeService likeService) {
        this.likeProvider = likeProvider;
        this.likeService = likeService;
    }

    /**
     * 조회 API
     * [GET] /likers/{postId}
     * 해당 포스트의 좋아요한 유저 보기 API :
     *
     * @return BaseResponse<List < GetLikeRes>>
     */


    @ResponseBody
    @GetMapping("/likers/{postId}")
    public BaseResponse<GetPostLikeListRes> getLikers(@PathVariable("postId") int postId) {
        // Get Likes
        try {
            GetPostLikeListRes getPostRes = likeProvider.getPostLikeList(postId);
            System.out.println("------------");
            System.out.printf("postId: %d \n",postId);
            System.out.println(getPostRes.toString());
            System.out.println(getPostRes);
            System.out.println("------------");
            return new BaseResponse<>(getPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 조회 API
     * [GET] /likers/{postId}/counts
     * 해당 포스트의 좋아요한 유저 갯수 보기 API :
     *
     * @return BaseResponse<List < GetLikeRes>>
     */


    @ResponseBody
    @GetMapping("/counts/{postId}")
    public BaseResponse<GetPostLikeCountRes> getLikersCount(@PathVariable("postId") int postId) {
        // Get Likes
        try {
            GetPostLikeCountRes getPostLikeCountRes = likeProvider.getPostlikeCount(postId);
            return new BaseResponse<>(getPostLikeCountRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }


    /**
     * 생성 API
     * [Post] /
     * 좋아요 API :
     *
     * @return BaseResponse<PostPostLikesRes>
     */
    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostLikesRes> createLike(@RequestBody PostLikesReq postLikeReq){
        try {
            PostLikesRes postLikeRes = likeService.createLike(postLikeReq);
//           log.info(postLikeRes.toString());
            System.out.println("postLikeRes.toString(): "+postLikeRes.toString());
            return new BaseResponse<>(postLikeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     * 생성 API
     * [Post] /unlikes
     * 좋아요취소하기 API :
     *
     * @return BaseResponse<PostPostLikesRes>
     */
    @ResponseBody
    @PatchMapping("/unlikes")
    public BaseResponse<PostLikesRes> updateUnLike(@RequestBody PostLikesReq postlikesReq){
        try {
            PostLikesRes postLikeRes = likeService.cancelLike(postlikesReq);
            return new BaseResponse<>(postLikeRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }





}
