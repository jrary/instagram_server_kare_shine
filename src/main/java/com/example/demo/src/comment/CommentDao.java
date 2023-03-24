package com.example.demo.src.comment;


import com.example.demo.config.BaseException;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetHomeCommentRes;
import com.example.demo.src.comment.model.PostCommentReq;
import com.example.demo.src.user.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.FAILED_TO_LOGIN;

@Repository
public class CommentDao {

    private JdbcTemplate jdbcTemplate;
    final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public void setDataSource(DataSource dataSource){
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public int postExists(int postId){
        String checkUserQuery = "select exists(select user_id from instagram_db.Post where post_status = 1 and post_id = ?) ";
        return this.jdbcTemplate.queryForObject(checkUserQuery,
                int.class,
                postId);
    }

    public int checkUserActive(Long userId){
        String checkUserQuery = "select exists(select user_id from instagram_db.User where user_active = 1 and user_id = ?) ";
        return this.jdbcTemplate.queryForObject(checkUserQuery,
                int.class,
                userId);
    }

    public List<GetCommentRes> commentList(int postId){
        String getCommentListQuery = "select c.comment_id, u.user_id, c.comment_parent_id, u.user_nickname, u.user_profile_img, c.comment_text,\n" +
                "       case\n" +
                "           when timestampdiff(second, c.comment_create_at, current_timestamp) < 60\n" +
                "               then concat(timestampdiff(second, c.comment_create_at, current_timestamp), '초 전')\n" +
                "           when timestampdiff(minute , c.comment_create_at, current_timestamp) < 60\n" +
                "               then concat(timestampdiff(minute, c.comment_create_at, current_timestamp), '분 전')\n" +
                "           when timestampdiff(hour , c.comment_create_at, current_timestamp) < 24\n" +
                "               then concat(timestampdiff(hour, c.comment_create_at, current_timestamp), '시간 전')\n" +
                "           when timestampdiff(day , c.comment_create_at, current_timestamp) < 365\n" +
                "               then concat(timestampdiff(day, c.comment_create_at, current_timestamp), '일 전')\n" +
                "           else concat(timestampdiff(year , c.comment_create_at, current_timestamp), '년 전')\n" +
                "           end as uploadTime\n" +
                "from instagram_db.Post p\n" +
                "    join instagram_db.Comment c on c.post_id = p.post_id\n" +
                "    join instagram_db.User u on c.user_id = u.user_id\n" +
                "where p.post_id = ?";

        return this.jdbcTemplate.query(getCommentListQuery,
                (rs, rowNum)-> new GetCommentRes(
                        rs.getInt("comment_id"),
                        rs.getInt("user_id"),
                        rs.getInt("comment_parent_id"),
                        rs.getString("user_nickname"),
                        rs.getString("user_profile_img"),
                        rs.getString("comment_text"),
                        rs.getString("uploadTime")
                ), postId);
    }

    public int commentNum(int postId){
        String getCommentNumQuery = "select count(comment_id) from instagram_db.Comment where post_id = ?";
        return this.jdbcTemplate.queryForObject(getCommentNumQuery,
                int.class,
                postId);
    }

    public List<GetHomeCommentRes> commentHome(int postId, Long userId){
        String getCommentHomeListQuery = "select c.comment_id, uui.user_id, c.comment_parent_id, uui.user_nickname, c.comment_text\n" +
                "from Post p\n" +
                "    join Comment c on p.post_id = c.post_id\n" +
                "    right join (select u.user_id, u.user_nickname from User u where u.user_id = ?\n" +
                "        UNION select u.user_id, u.user_nickname from\n" +
                "            (select f.followee_id from Follow f where f.follower_id = ? ) as fid\n" +
                "                join instagram_db.User u on fid.followee_id = u.user_id)\n" +
                "            as uui on uui.user_id = c.user_id\n" +
                "where p.post_id = ?";

        return this.jdbcTemplate.query(getCommentHomeListQuery,
                (rs, rowNum)-> new GetHomeCommentRes(
                        rs.getInt("comment_id"),
                        rs.getInt("user_id"),
                        rs.getInt("comment_parent_id"),
                        rs.getString("user_nickname"),
                        rs.getString("comment_text")
                ), userId, userId, postId);
    }

    public int postComment(PostCommentReq postCommentReq, Long userId, int postId){
        String postCommentQuery = "insert into Comment(user_id, post_id, comment_parent_id, comment_text) " +
                "VALUES(?,?,?,?)";
        Object[] postCommentParams = new Object[]{userId, postId, postCommentReq.getCommentParentId(),
                postCommentReq.getCommentContents()};
        return this.jdbcTemplate.update(postCommentQuery, postCommentParams);
    }
}
