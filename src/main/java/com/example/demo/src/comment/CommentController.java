package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetHomeCommentRes;
import com.example.demo.src.comment.model.PostCommentReq;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.UserService;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/comment")
public class CommentController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final CommentProvider commentProvider;
    @Autowired
    private final CommentService commentService;
    @Autowired
    private final JwtService jwtService;

    public CommentController(CommentProvider commentProvider, CommentService commentService, JwtService jwtService){
        this.commentProvider = commentProvider;
        this.commentService = commentService;
        this.jwtService = jwtService;
    }

    /**
     * 3.1 댓글 목록 조회 API
     * [GET] /comment
     */
    @ResponseBody
    @GetMapping("/{postId}")
    public BaseResponse<List<GetCommentRes>> commentList(@PathVariable("postId") int postId) throws BaseException {
        // 해당 게시물이 유효한지 확인
        if (commentProvider.checkPostActive(postId) == 0){
            throw new BaseException(POST_UNAVAILABLE);
        }
        List<GetCommentRes> commentResList = commentService.commentList(postId);
        return new BaseResponse<>(commentResList);
    }
    /**
     * 3.2 댓글 개수 API
     * [GET]
     */
    @ResponseBody
    @GetMapping("/num/{postId}")
    public BaseResponse<Integer> commentNum(@PathVariable("postId") int postId) throws BaseException {
        // 해당 게시물이 유효한지 확인
        if (commentProvider.checkPostActive(postId) == 0){
            throw new BaseException(POST_UNAVAILABLE);
        }
        int commentNum = commentService.commentNum(postId);
        return new BaseResponse<>(commentNum);
    }
    /**
     * 3.3 나와 게시글 작성자 댓글 API (홈 화면)
     * [GET]
     */
    @ResponseBody
    @GetMapping("/home/{postId}")
    public BaseResponse<List<GetHomeCommentRes>> commentHome(@PathVariable("postId") int postId) throws BaseException {
        Long userId = jwtService.getUserId();
        // 해당 유저가 유효한지 확인
        if (commentProvider.checkUserActive(userId) == 0){
            throw new BaseException(INVALID_USER);
        }
        // 해당 게시물이 유효한지 확인
        if (commentProvider.checkPostActive(postId) == 0){
            throw new BaseException(POST_UNAVAILABLE);
        }
        List<GetHomeCommentRes> homeCommentList = commentService.commentHome(postId, userId);
        return new BaseResponse<>(homeCommentList);
    }
    /**
     * 3.4 댓글 작성 API
     * [POST]
     */
    @ResponseBody
    @PostMapping("/posts/{postId}")
    public BaseResponse<String> commentPost(@PathVariable("postId") int postId, @RequestBody PostCommentReq postCommentReq) throws BaseException {
        Long userId = jwtService.getUserId();
        // Request null 값 확인하기
        if (commentProvider.checkUserActive(userId) == 0){
            throw new BaseException(INVALID_USER);
        }
        if (commentProvider.checkPostActive(postId) == 0){
            throw new BaseException(POST_UNAVAILABLE);
        }
        if(postCommentReq.getCommentContents().length() == 0){
            throw new BaseException(EMPTY_CONTENTS);
        }
        // 댓글 내용은 2000자를 넘길 수 없음
        if(postCommentReq.getCommentContents().length() > 2000){
            throw new BaseException(COMMENT_LONG_LIMIT);
        }
        String result = commentService.postComment(postCommentReq, userId, postId);
        return new BaseResponse<>(result);
    }
}
