package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.comment.CommentProvider;
import com.example.demo.src.comment.CommentService;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetHomeCommentRes;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.store.model.StoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.INVALID_USER;
import static com.example.demo.config.BaseResponseStatus.POST_UNAVAILABLE;

@RestController
@RequestMapping("/store")
public class StoreController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final StoreProvider storeProvider;
    @Autowired
    private final StoreService storeService;
    @Autowired
    private final JwtService jwtService;

    public StoreController(StoreProvider storeProvider, StoreService storeService, JwtService jwtService){
        this.storeProvider = storeProvider;
        this.storeService = storeService;
        this.jwtService = jwtService;
    }

    /**
     * 6.1 게시물 저장 API
     * [POST] /store/:postId
     */
    @ResponseBody
    @PostMapping("/{postId}")
    public BaseResponse<StoreRes> storePost(@PathVariable("postId") int postId) throws BaseException {
        Long userId = jwtService.getUserId();
        // 해당 유저가 유효한지 확인
        if (storeProvider.checkUserActive(userId) == 0){
            throw new BaseException(INVALID_USER);
        }
        //해당 게시물이 유효한지 확인
        if (storeProvider.checkPostActive(postId) == 0){
            throw new BaseException(POST_UNAVAILABLE);
        }
        StoreRes storeRes = storeService.store(userId, postId);
        return new BaseResponse<>(storeRes);
    }
    /**
     * 6.2 게시물 저장 취소 API
     * [PATCH] /store/:postId
     */
    @ResponseBody
    @PatchMapping("/{postId}")
    public BaseResponse<StoreRes> cancelStore(@PathVariable("postId") int postId) throws BaseException {
        Long userId = jwtService.getUserId();
        // 해당 유저가 유효한지 확인
        if (storeProvider.checkUserActive(userId) == 0){
            throw new BaseException(INVALID_USER);
        }
        // 해당 게시물이 유효한지 확인
        if (storeProvider.checkPostActive(postId) == 0){
            throw new BaseException(POST_UNAVAILABLE);
        }
        StoreRes cancelStoreRes = storeService.cancelStore(userId, postId);
        return new BaseResponse<>(cancelStoreRes);
    }
    /**
     * 6.3 게시물 저장 목록 API
     * [GET] /store
     */
    @ResponseBody
    @GetMapping("")
    public BaseResponse<List<GetStoreRes>> storeList() throws BaseException {
        Long userId = jwtService.getUserId();
        // 해당 유저가 유효한지 확인
        if (storeProvider.checkUserActive(userId) == 0){
            throw new BaseException(INVALID_USER);
        }
        List<GetStoreRes> storeList = storeService.storeList(userId);
        return new BaseResponse<>(storeList);
    }
}
