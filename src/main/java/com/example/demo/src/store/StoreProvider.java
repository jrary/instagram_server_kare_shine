package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.comment.CommentDao;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;

//Provider : Read의 비즈니스 로직 처리
@Service
public class StoreProvider {

    private final StoreDao storeDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public StoreProvider(StoreDao storeDao, JwtService jwtService) {
        this.storeDao = storeDao;
        this.jwtService = jwtService;
    }

    // 해당 게시물이 유효한지 확인 (존재하는지, 활성화 상태인지)
    public int checkPostActive(int postId) throws BaseException{
        try {
            return storeDao.postExists(postId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 해당 유저가 유효한지 확인 (존재하는지, 활성화 상태인지)
    public int checkUserActive(Long userId) throws BaseException{
        try {
            return storeDao.checkUserActive(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 유저의 게시물 저장 상태가 유효한지 확인
    public int checkStoreAvailable(Long userId, int postId) throws BaseException{
        try {
            return storeDao.checkStoreAvailable(userId, postId);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
