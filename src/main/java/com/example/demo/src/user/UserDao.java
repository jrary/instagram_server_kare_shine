package com.example.demo.src.user;


import com.example.demo.config.BaseException;
import com.example.demo.config.BaseResponseStatus;
import com.example.demo.src.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import static com.example.demo.config.BaseResponseStatus.FAILED_TO_LOGIN;

@Repository
public class UserDao {

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User getUser(GetLoginReq getLoginReq, LoginStatus loginStatus) throws BaseException {
        try{
            String getUserQuery;
            String getUserParams = getLoginReq.getUserInfo();
            if(loginStatus == LoginStatus.EMAIL){
                getUserQuery = "select user_id, user_name, user_nickname, user_phone, user_pwd, user_email, user_public, user_active " +
                        "from User where user_email = ?";
                logger.warn("email");
            }
            else if(loginStatus == LoginStatus.PHONE){
                getUserParams = getLoginReq.getUserInfo().replaceAll("[^0-9]", "");
                getUserQuery = "select user_id, user_name, user_nickname, user_phone, user_pwd, user_email, user_public, user_active " +
                        "from User where user_phone = ?";
                logger.warn("phone");
            }
            else {
                getUserQuery = "select user_id, user_name, user_nickname, user_phone, user_pwd, user_email, user_public, user_active " +
                        "from User where user_nickname = ?";
                logger.warn("nickname");
            }

            return this.jdbcTemplate.queryForObject(getUserQuery,
                    (rs, rowNum)-> new User(
                            rs.getInt("user_id"),
                            rs.getString("user_name"),
                            rs.getString("user_nickname"),
                            rs.getString("user_phone"),
                            rs.getString("user_pwd"),
                            rs.getString("user_email"),
                            rs.getInt("user_public"),
                            rs.getInt("user_active")
                    ), getUserParams);
        }catch (Exception e){
            throw new BaseException(FAILED_TO_LOGIN);
        }

    }

    public int checkUserExistByEmail(String email){
        String checkEmailQuery = "select exists(select user_email from User where user_email = ?)";
        String checkEmailParams = email;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int checkUserExistByPhone(String phone){
        String checkEmailQuery = "select exists(select user_email from User where user_phone = ?)";
        String checkEmailParams = phone;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public int checkUserExistByNickname(String nickname){
        String checkEmailQuery = "select exists(select user_email from User where user_nickname = ?)";
        String checkEmailParams = nickname;
        return this.jdbcTemplate.queryForObject(checkEmailQuery,
                int.class,
                checkEmailParams);
    }

    public UserIdRes getUserId(String email){
        String getUserQuery = "select user_id from User where user_email = ?";
        String getUserParams = email;

        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum)-> new UserIdRes(
                        rs.getInt("user_id")
                ), getUserParams);
    }

    public int checkUserActive(Long userId){
        String checkUserQuery = "select exists(select user_id from instagram_db.User where user_active = 1 and user_id = ?) ";
        return this.jdbcTemplate.queryForObject(checkUserQuery,
                int.class,
                userId);
    }

    public int postUser(PostJoinReq postJoinReq, String phone, String pwd){
        String postJoinQuery = "insert into User(user_name, user_nickname, user_phone, user_pwd, " +
                "user_email, user_profile_img) " +
                "VALUES(?,?,?,?,?,?)";
        Object[] postJoinParams = new Object[]{postJoinReq.getUserName(), postJoinReq.getUserNickname(),
                phone, pwd,
                postJoinReq.getUserEmail(), postJoinReq.getUserProfileImg()};
        return this.jdbcTemplate.update(postJoinQuery, postJoinParams);
    }

    public int modifyUser(Long userId, PatchUserReq patchUserReq){
        String modifyUserNameQuery = "update User set user_name = ?, user_website = ?, user_text = ?, " +
                "user_phone = ?, user_email = ?, user_gender = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserReq.getUserName(), patchUserReq.getUserWebsite(),
                patchUserReq.getUserText(), patchUserReq.getUserPhone(),
                patchUserReq.getUserEmail(), patchUserReq.getUserGender(),
                userId};
        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public int modifyUserImg(Long userId, PatchUserImgReq patchUserImgReq){
        String modifyUserNameQuery = "update User set user_profile_img = ? where user_id = ? ";
        Object[] modifyUserNameParams = new Object[]{patchUserImgReq.getProfileImgUrl(), userId};
        return this.jdbcTemplate.update(modifyUserNameQuery,modifyUserNameParams);
    }

    public GetHomeUserInfoRes homeUser(Long userId){
        String getHomeUserQuery = "select user_id, user_name, user_nickname, user_profile_img from User where user_id = ?";
        return this.jdbcTemplate.queryForObject(getHomeUserQuery,
                (rs, rowNum)-> new GetHomeUserInfoRes(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_nickname"),
                        rs.getString("user_profile_img")
                ), userId);
    }

    public GetMypageUserInfoRes mypageUser(Long userId){
        String getMypageUserQuery = "select u.user_id, u.user_name, u.user_nickname, u.user_profile_img,\n" +
                "       (select count(case when u.user_id = p.user_id then 1 end)) as post_num,\n" +
                "       (select count(case when u.user_id = f.followee_id then 1 end) from Follow f) as follower_num,\n" +
                "       (select count(case when u.user_id = f.follower_id then 1 end) from Follow f) as following_num,\n" +
                "       u.user_text, u.user_website, u.user_public\n" +
                "    from User u\n" +
                "    left join Post p on u.user_id = p.user_id\n" +
                "where u.user_id = ?";
        return this.jdbcTemplate.queryForObject(getMypageUserQuery,
                (rs, rowNum)-> new GetMypageUserInfoRes(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_nickname"),
                        rs.getString("user_profile_img"),
                        rs.getInt("post_num"),
                        rs.getInt("follower_num"),
                        rs.getInt("following_num"),
                        rs.getString("user_text"),
                        rs.getString("user_website"),
                        rs.getInt("user_public")
                ), userId);
    }

    public int userInactive(Long userId){
        String userInactiveQuery = "update User set user_active = 0 where user_id = ? ";
        Object[] userInactiveParams = new Object[]{userId};
        return this.jdbcTemplate.update(userInactiveQuery,userInactiveParams);
    }

    public int userActive(Long userId){
        String userInactiveQuery = "update User set user_active = 1 where user_id = ? ";
        Object[] userInactiveParams = new Object[]{userId};
        return this.jdbcTemplate.update(userInactiveQuery,userInactiveParams);
    }

    public User getUserInfoById(Long userId){
        String getUserQuery = "select user_id, user_name, user_nickname, user_phone, user_pwd, user_email, user_public, user_active " +
                "from User where user_id = ?";
        return this.jdbcTemplate.queryForObject(getUserQuery,
                (rs, rowNum)-> new User(
                        rs.getInt("user_id"),
                        rs.getString("user_name"),
                        rs.getString("user_nickname"),
                        rs.getString("user_phone"),
                        rs.getString("user_pwd"),
                        rs.getString("user_email"),
                        rs.getInt("user_public"),
                        rs.getInt("user_active")
                ), userId);
    }
}
