package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.SHA256.encrypt;

// Service Create, Update, Delete 의 로직 처리
@Service
public class UserService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final UserDao userDao;
    private final UserProvider userProvider;
    private final JwtService jwtService;


    @Autowired
    public UserService(UserDao userDao, UserProvider userProvider, JwtService jwtService) {
        this.userDao = userDao;
        this.userProvider = userProvider;
        this.jwtService = jwtService;

    }

    PostJoinRes userJoin (PostJoinReq postJoinReq, String phone) throws BaseException{
        String encrypt_pwd = encrypt(postJoinReq.getUserPwd());
        if(userDao.postUser(postJoinReq, phone, encrypt_pwd) == 1){
            int newUserId = userDao.getUserId(postJoinReq.getUserEmail()).getUserId();
            String jwt = jwtService.createJwt(newUserId);
            return new PostJoinRes(newUserId, jwt);
        } else {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetLoginRes login(GetLoginReq getLoginReq, LoginStatus loginStatus) throws BaseException {
        // 데베에 저장된 암호 받아오기
        User user = userDao.getUser(getLoginReq, loginStatus);
        // 지금 입력받은 암호 암호화하기
        String encrypt_pwd = encrypt(getLoginReq.getUserPwd());
        // 일치하는지 확인 - 암호 같으면 jwt 생성 후 반환
        if (encrypt_pwd.equals(user.getUserPwd())) {
            int userId = user.getUserId();
            String jwt = jwtService.createJwt(userId);
            return new GetLoginRes(userId, jwt);
        }
        else{
            throw new BaseException(FAILED_TO_LOGIN);
        }
    }

    public void modifyUser(Long userId, PatchUserReq patchUserReq) throws BaseException {
        try{
            int resultName = userDao.modifyUser(userId, patchUserReq);
            if(resultName == 0){
                throw new BaseException(MODIFY_FAIL_USERNAME);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void modifyUserImg(Long userId, PatchUserImgReq patchUserImgReq) throws BaseException {
        try{
            int resultName = userDao.modifyUserImg(userId, patchUserImgReq);
            if(resultName == 0){
                throw new BaseException(USER_EMPTY_IMG);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetHomeUserInfoRes homeUser(Long userId) throws BaseException {
        try{
            GetHomeUserInfoRes getHomeUserInfoRes = userDao.homeUser(userId);
            return getHomeUserInfoRes;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public GetMypageUserInfoRes mypageUser(Long userId) throws BaseException {
        try{
            GetMypageUserInfoRes getMypageUserInfoRes = userDao.mypageUser(userId);
            return getMypageUserInfoRes;
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void userInactive(Long userId) throws BaseException {
        try{
            int resultName = userDao.userInactive(userId);
            if(resultName == 0){
                throw new BaseException(INVALID_USER);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public void userActive(Long userId) throws BaseException {
        try{
            int resultName = userDao.userActive(userId);
            if(resultName == 0){
                throw new BaseException(INVALID_USER);
            }
        } catch(Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
