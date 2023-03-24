package com.example.demo.src.post;

import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.post.model.PostPostsReq;
import com.example.demo.src.post.model.PostPostsRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.NOTNORMALUSER_ERROR;

@RestController
@RequestMapping("/app/posts")
public class PostController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final PostProvider postProvider;
    @Autowired
    private final PostService postService;

    @Autowired
    private final JwtService jwtService;




    public PostController(PostProvider postProvider, PostService postService, JwtService jwtService){
        this.postProvider = postProvider;
        this.postService = postService;
        this.jwtService = jwtService;
    }

    /**
     *  조회 API
     * [GET] /posts
     * 전체 게시글 최신순서로 정렬해서 가저오기 API : 탐색 게시물 목록
     * @return BaseResponse<List<GetPostRes>>
     */
    //Query String
    @ResponseBody
    @GetMapping("") // (GET) 127.0.0.1:9000/app/posts
    public BaseResponse<List<GetPostRes>> getPosts() {
        try{
            // Get Posts
            List<GetPostRes> getPostRes = postProvider.getPosts();
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *  조회 API
     * [GET] /posts/users/:userId
     * 유저 아이디 게시글 보기 API : 해당 유저 게시물 목록
     * @return BaseResponse<List<GetPostRes>>
     */
    @ResponseBody
    @GetMapping("users/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<List<GetPostRes>> getPostsUserId(@PathVariable("userId") int userId) throws BaseException {
        // Get Posts

        try{
            Long userIdByJwt = jwtService.getUserId();
//            System.out.println(userIdByJwt);
//            System.out.println(userId);
            if(userIdByJwt != userId){
                return new BaseResponse<>(NOTNORMALUSER_ERROR);
            }
            List<GetPostRes> getPostRes = postProvider.getPostsUserId(userId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    @ResponseBody
    @GetMapping("status/{postId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<PostPostsRes> getDeletePostId(@PathVariable("postId") int postId) {
        // Get Posts
        try{
            PostPostsRes getPostRes = postProvider.getDeletePostId(postId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *  조회 API
     * [GET] /posts/home/:userId
     * 홈화면 게시글 목록 조회하기 API : 내 홈화면 게시글 목록 (follow 연동X)
     * @return BaseResponse<List<GetPostRes>>
     */
    @ResponseBody
    @GetMapping("home/{userId}") // (GET) 127.0.0.1:9000/app/users/:userId
    public BaseResponse<List<GetPostRes>> getMyPosts(@PathVariable("userId") int userId) {
        // Get Posts
        try{
            List<GetPostRes> getPostRes = postProvider.getHomePosts(userId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }
    /**
     *  조회 API
     * [GET] /posts/:postid
     * 해당 포스트 정보 가져오기 API : 원하는 게시글의 탐색
     * @return BaseResponse<List<GetPostRes>>
     */
    @ResponseBody
    @GetMapping("/{postId}")
    public BaseResponse<List<GetPostRes>> getPostPostId(@PathVariable("postId") int postId) {
        // Get Posts
        try{
            List<GetPostRes> getPostRes = postProvider.getPostPostId(postId);
            return new BaseResponse<>(getPostRes);
        } catch(BaseException exception){
            return new BaseResponse<>((exception.getStatus()));
        }
    }

    /**
     *  생성 API
     * [Post] /posts
     * 게시글 생성 API : 게시글 생성하기
     * @return BaseResponse<PostPostsRes>
     */

    @ResponseBody
    @PostMapping("")
    public BaseResponse<PostPostsRes> createPost(@RequestBody PostPostsReq postPostsReq){
        try {
            PostPostsRes postPostRes = postService.createPost(postPostsReq);
            return new BaseResponse<>(postPostRes);
        } catch (BaseException exception) {
            return new BaseResponse<>((exception.getStatus()));
        }
    }





}

