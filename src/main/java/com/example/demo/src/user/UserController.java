package com.example.demo.src.user;

import com.example.demo.config.BaseResponseStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponse;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;


import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.ValidationRegex.isRegexEmail;

@RestController
@RequestMapping("/user")
public class UserController {
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private final UserProvider userProvider;
    @Autowired
    private final UserService userService;
    @Autowired
    private final JwtService jwtService;

    public UserController(UserProvider userProvider, UserService userService, JwtService jwtService){
        this.userProvider = userProvider;
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 1.1 회원가입 API
     * [POST] /user/join
     */
    @ResponseBody
    @PostMapping("/join")
    public BaseResponse<PostJoinRes> join(@RequestBody PostJoinReq postJoinReq) throws BaseException {
        // Request null값 확인하기
        if(postJoinReq.getUserName()==null){
            throw new BaseException(USER_EMPTY_NAME);
        }
        if(postJoinReq.getUserNickname()==null){
            throw new BaseException(USER_EMPTY_NICKNAME);
        }
        if(postJoinReq.getUserPhone()==null){
            throw new BaseException(USER_EMPTY_PHONE);
        }
        if(postJoinReq.getUserPwd()==null){
            throw new BaseException(USER_EMPTY_PASSWORD);
        }
        if(postJoinReq.getUserEmail()==null){
            throw new BaseException(USER_EMPTY_EMAIL);
        }
        // 닉네임이 유효한지 확인 - 문자, 숫자, _, . 만 가능.
//        if(postJoinReq.getUserNickname().matches(" ^[a-zA-Z0-9_.]*$")){
//            throw new BaseException(USER_EMPTY_NICKNAME);
//        }
        if(!userProvider.nicknameRule(postJoinReq.getUserNickname())){
            throw new BaseException(USER_INVALID_NICKNAME);
        }
        // 휴대폰 번호 사이 "-" 제거
        String phone = postJoinReq.getUserPhone().replaceAll("[^0-9]", "");
//        if(phone.length() != 10 && phone.length() != 11){
//            throw new BaseException(USER_INVALID_PHONE);
//        }
        if((phone.length() != 10 && phone.length() != 11) || !userProvider.phoneRule(postJoinReq.getUserPhone())){
            logger.warn(postJoinReq.getUserPhone());
            throw new BaseException(USER_INVALID_PHONE);
        }
        // 이메일이 유효한지 확인
        if(!isRegexEmail(postJoinReq.getUserEmail())){
            throw new BaseException(USER_INVALID_EMAIL);
        }
        // 이미 존재하는 email인지 확인
        if(userProvider.checkUserExistByEmail(postJoinReq.getUserEmail()) == 1){
            throw new BaseException(USER_EXISTS_EMAIL);
        }
        // 이미 존재하는 phone인지 확인
        if(userProvider.checkUserExistByPhone(phone) == 1){
            throw new BaseException(USER_EXISTS_PHONE);
        }
        // 이미 존재하는 nickname인지 확인
        if(userProvider.checkUserExistByNickname(postJoinReq.getUserNickname()) == 1){
            throw new BaseException(USER_EXISTS_NICKNAME);
        }

        PostJoinRes getLoginRes = userService.userJoin(postJoinReq, phone);
        return new BaseResponse<>(getLoginRes);
    }
    /**
     * 1.2 유저 로그인 API
     * [GET] /user/login
     */
    @ResponseBody
    @PostMapping("/login")
    public BaseResponse<GetLoginRes> login(@RequestBody GetLoginReq getLoginReq) throws BaseException {
        LoginStatus loginStatus;
        // Request null 값 확인하기
        if(getLoginReq.getUserInfo()==null){
            throw new BaseException(USER_EMPTY_NAME);
        }
        if(getLoginReq.getUserPwd()==null){
            throw new BaseException(USER_EMPTY_PASSWORD);
        }
        // 로그인 시, 이메일/휴대폰 번호/아이디(닉네임) 중 어떤 것을 사용했는 지 판별하기
        if(isRegexEmail(getLoginReq.getUserInfo())){
            loginStatus = LoginStatus.EMAIL;
        }
        else if(userProvider.phoneRule(getLoginReq.getUserInfo())){
            loginStatus = LoginStatus.PHONE;
        }
        else if(userProvider.nicknameRule(getLoginReq.getUserInfo())){
            loginStatus = LoginStatus.NICKNAME;
        }
        else{
            throw new BaseException(INVALID_USER);
        }
        GetLoginRes getLoginRes = userService.login(getLoginReq, loginStatus);
        return new BaseResponse<>(getLoginRes);
    }
    /**
     * 1.3 유저 정보 수정 API
     * [PATCH] /user/mod
     */
    @ResponseBody
    @PatchMapping("/mod")
    public BaseResponse<String> modifyUserProfile(@RequestBody PatchUserReq patchUserReq) throws BaseException {
        Long userIdByJwt = jwtService.getUserId();
        // Request의 Name 글자수 예외처리
        if(patchUserReq.getUserName() == null || (patchUserReq.getUserName().length() == 0 || patchUserReq.getUserName().length() > 45)){
            throw new BaseException(USER_EMPTY_NICKNAME);
        }
        // 휴대폰 번호 사이 "-" 제거
        String phone = patchUserReq.getUserPhone().replaceAll("[^0-9]", "");
        // 유효한 휴대폰 번호인지 확인
        if((phone.length() != 10 && phone.length() != 11) || !userProvider.phoneRule(phone)){
            throw new BaseException(USER_INVALID_PHONE);
        }
        // 이메일이 유효한지 확인
        if(!isRegexEmail(patchUserReq.getUserEmail())){
            throw new BaseException(USER_INVALID_EMAIL);
        }
        // 이미 존재하는 email인지 확인
        if(userProvider.checkUserExistByEmail(patchUserReq.getUserEmail()) == 1
                && !Objects.equals(patchUserReq.getUserEmail(), userProvider.getUserInfoById(userIdByJwt).getUserEmail())){
            throw new BaseException(USER_EXISTS_EMAIL);
        }
        // 이미 존재하는 phone인지 확인
        if(userProvider.checkUserExistByPhone(phone) == 1
                && !Objects.equals(phone, userProvider.getUserInfoById(userIdByJwt).getUserPhone())){
            throw new BaseException(USER_EXISTS_PHONE);
        }
        if(!Objects.equals(patchUserReq.getUserGender(), "Male") && !Objects.equals(patchUserReq.getUserGender(), "Female")){
            throw new BaseException(USER_INVALID_GENDER);
        }
        if (userProvider.checkUserActive(userIdByJwt) == 0){
            throw new BaseException(INVALID_USER);
        }
        userService.modifyUser(userIdByJwt, patchUserReq);
        String result = "사용자 정보를 수정하였습니다.";
        return new BaseResponse<>(result);
    }
    /**
     * 1.4 유저 프로필 사진 수정 API
     * [PATCH] /user/img
     */
    @ResponseBody
    @PatchMapping("/img")
    public BaseResponse<String> modifyUserProfileImg(@RequestBody PatchUserImgReq patchUserImgReq) throws BaseException {
        Long userIdByJwt = jwtService.getUserId();
        // Request null 값 확인하기
        if(patchUserImgReq.getProfileImgUrl() == null || patchUserImgReq.getProfileImgUrl().length() == 0){
            throw new BaseException(USER_EMPTY_IMG);
        }
        if (userProvider.checkUserActive(userIdByJwt) == 0){
            throw new BaseException(INVALID_USER);
        }
        userService.modifyUserImg(userIdByJwt, patchUserImgReq);
        String result = "프로필 이미지를 수정하였습니다.";
        return new BaseResponse<>(result);
    }
    /**
     * 1.5 로그인한 유저 정보 API (홈 화면)
     * [GET] /user/home
     */
    @ResponseBody
    @GetMapping("/home")
    public BaseResponse<GetHomeUserInfoRes> homeUser() throws BaseException {
        Long userIdByJwt = jwtService.getUserId();
        // 유효한 유저인지 확인
        if (userProvider.checkUserActive(userIdByJwt) == 0){
            throw new BaseException(INVALID_USER);
        }
        GetHomeUserInfoRes getHomeUserInfoRes = userService.homeUser(userIdByJwt);
        return new BaseResponse<>(getHomeUserInfoRes);
    }
    /**
     * 1.6 마이페이지 회원 정보 API
     * [GET] /user/mypage
     */
    @ResponseBody
    @GetMapping("/mypage")
    public BaseResponse<GetMypageUserInfoRes> mypageUser() throws BaseException {
        Long userIdByJwt = jwtService.getUserId();
        // 유효한 유저인지 확인
        if (userProvider.checkUserActive(userIdByJwt) == 0){
            throw new BaseException(INVALID_USER);
        }
        GetMypageUserInfoRes getMypageUserInfoRes = userService.mypageUser(userIdByJwt);
        return new BaseResponse<>(getMypageUserInfoRes);
    }
    /**
     * 1.7 비활성화 전환 API
     * [PATCH] /user/inactive
     */
    @ResponseBody
    @PatchMapping("/inactive")
    public BaseResponse<String> inactiveUser() throws BaseException {
        Long userIdByJwt = jwtService.getUserId();
        // 유효한 유저인지 확인
        if (userProvider.checkUserActive(userIdByJwt) == 0){
            throw new BaseException(USER_NOW_UNAVAILABLE);
        }
        userService.userInactive(userIdByJwt);
        String result = "비활성화가 완료되었습니다.";
        return new BaseResponse<>(result);
    }

    /**
     * 1.8 비활성화 해제 API
     * [PATCH] /user/active
     */
    @ResponseBody
    @PatchMapping("/active")
    public BaseResponse<String> activeUser() throws BaseException {
        Long userIdByJwt = jwtService.getUserId();
        if (userProvider.checkUserActive(userIdByJwt) == 1){
            throw new BaseException(USER_NOW_AVAILABLE);
        }
        userService.userActive(userIdByJwt);
        String result = "비활성화를 해제하였습니다.";
        return new BaseResponse<>(result);
    }
}
