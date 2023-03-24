package com.example.demo.src.store;

import com.example.demo.config.BaseException;
import com.example.demo.src.comment.CommentDao;
import com.example.demo.src.comment.CommentProvider;
import com.example.demo.src.store.model.GetStoreIdRes;
import com.example.demo.src.store.model.GetStoreRes;
import com.example.demo.src.store.model.StoreRes;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;

// Service Create, Update, Delete 의 로직 처리
@Service
public class StoreService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final StoreDao storeDao;
    private final StoreProvider storeProvider;
    private final JwtService jwtService;


    @Autowired
    public StoreService(StoreDao storeDao, StoreProvider storeProvider, JwtService jwtService) {
        this.storeDao = storeDao;
        this.storeProvider = storeProvider;
        this.jwtService = jwtService;
    }

    // 게시물을 저장하는 함수
    public StoreRes store (Long userId, int postId) throws BaseException{
        try{
            // 만약, 특정 유저가 특정 게시물을 저장했던 기록이 남아 있다면, checkStoreAvailable 은 1을 반환한다.
            // 특정 유저가 특정 게시물을 저장한 후 취소하면 그 데이터를 삭제하는 것이 아닌 store_status 를 0으로(저장 취소) 변경했다.
            int checkStoreAvailable = storeProvider.checkStoreAvailable(userId, postId);
            // 만약, 특정 유저가 특정 게시물을 저장했던 기록이 없다면
            if(checkStoreAvailable == 0){
                // 새로운 Post_store 데이터를 생성하고, 생성 성공 시 1을 리턴함.
                if(storeDao.newStore(userId, postId) == 1){
                    return new StoreRes("게시물을 저장하였습니다.", postId);
                } else{
                    throw new BaseException(STORE_FAILED);
                }
            }
            // 만약, 특정 유저가 특정 게시물을 저장했던 기록이 남아 있다면
            else if(checkStoreAvailable == 1){
                // 그 기록에 대한 id 값을 받아온다. (Post_store 테이블의 store_id 값)
                GetStoreIdRes getStoreIdRes = storeDao.getStoreId(userId, postId);
                // store_status 를 1(저장)로 변경하고, 변경 성공 시 1을 리턴함.
                if(storeDao.store(getStoreIdRes) == 1){
                    return new StoreRes("게시물을 저장하였습니다.", postId);
                } else{
                    throw new BaseException(STORE_FAILED);
                }
            }
            else{
                throw new BaseException(VALIDATION_SERVER_ERROR);
            }
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    // 게시물 저장 취소 함수
    public StoreRes cancelStore (Long userId, int postId) throws BaseException{
        try{
            // 특정 유저가 특정 게시물을 저장한 기록이 있는지 확인 (없다면, 저장 취소할 수 없음)
            int checkStoreAvailable = storeProvider.checkStoreAvailable(userId, postId);
            if(checkStoreAvailable == 1){
                // 그 기록에 대한 id 값을 받아온다. (Post_store 테이블의 store_id 값)
                GetStoreIdRes getStoreIdRes = storeDao.getStoreId(userId, postId);
                if(getStoreIdRes == null) {
                    throw new BaseException(DATABASE_ERROR);
                }
                // store_status 를 0(저장 취소)로 변경하고, 변경 성공 시 1을 리턴함
                if(storeDao.cancelStore(getStoreIdRes) == 1){
                    return new StoreRes("게시물 저장을 취소하였습니다.", postId);
                } else{
                    throw new BaseException(STORE_FAILED);
                }
            }
            else{
                throw new BaseException(VALIDATION_SERVER_ERROR);
            }
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
    // 저장한 게시물 목록 보기
    public List<GetStoreRes> storeList (Long userId) throws BaseException{
        try{
            List<GetStoreRes> commentNum = storeDao.storeList(userId);
            return commentNum;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
