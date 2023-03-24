package com.example.demo.src.user;

import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import com.example.demo.utils.SHA256;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

import static com.example.demo.config.BaseResponseStatus.*;

//Provider : Read의 비즈니스 로직 처리
@Service
public class UserProvider {

    private final UserDao userDao;
    private final JwtService jwtService;


    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public UserProvider(UserDao userDao, JwtService jwtService) {
        this.userDao = userDao;
        this.jwtService = jwtService;
    }

    public int checkUserExistByEmail(String email) throws BaseException {
        try{
            return userDao.checkUserExistByEmail(email);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserExistByPhone(String phone) throws BaseException {
        try{
            return userDao.checkUserExistByPhone(phone);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserExistByNickname(String nickname) throws BaseException {
        try{
            return userDao.checkUserExistByNickname(nickname);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int getUserIdByEmail(String email) throws BaseException{
        try{
            return userDao.getUserId(email).getUserId();
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int checkUserActive(Long userId) throws BaseException{
        try {
            return userDao.checkUserActive(userId);
        } catch (Exception exception){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public boolean phoneRule(String nickname) throws BaseException{
        boolean result = Pattern.matches("[0-9-]*$", nickname);
        return result;
    }
    public boolean nicknameRule(String nickname) throws BaseException{
        boolean result = Pattern.matches("[a-zA-Z0-9_.]*$", nickname);
        return result;
    }

    public User getUserInfoById(Long userId) throws BaseException{
        try {
            return userDao.getUserInfoById(userId);
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
