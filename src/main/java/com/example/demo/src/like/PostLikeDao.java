package com.example.demo.src.like;

import com.example.demo.src.like.model.GetPostLikeCountRes;
import com.example.demo.src.like.model.Like;
import com.example.demo.src.like.model.PostLikesReq;
import com.example.demo.src.like.model.PostLikesRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;


@Repository
public class PostLikeDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Like> getPostLikeList(int postId) {
        String getPostLikeListQuery = "select * from Post_like where post_id = ? and like_status = 1";
        return this.jdbcTemplate.query(getPostLikeListQuery,
                (rs,rowNum) -> new Like(
                        rs.getInt("like_id"),
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getInt("like_status"),
                        rs.getString("like_create_at"))
                ,postId
        );

    }


    public int checkLikes(PostLikesReq postLikeReq) {
        String checkLikeQuery = "select exists(select * from Post_like where post_id = ? and user_id = ?)";
        Object[] checkLikeParams = new Object[]{postLikeReq.getPostId(),postLikeReq.getUserId()};
        return this.jdbcTemplate.queryForObject(checkLikeQuery,
                int.class,
                checkLikeParams);
    }

    public PostLikesRes createLike(PostLikesReq postLikeReq) {
        String createLikeQuery = "insert into Post_like (post_id,user_id) VALUES (?,?)";
        Object[] createLikeParams = new Object[]{postLikeReq.getPostId(),postLikeReq.getUserId()};
        this.jdbcTemplate.update(createLikeQuery, createLikeParams);
        String lastInserIdQuery = "select last_insert_id()";
        int newLikeId = this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
        return new PostLikesRes(newLikeId,1);
    }

    public Like getLikeId(int postId, int userId){
        String findLikeIdQuery = "Select * from Post_like where post_id = ? and user_id = ?";
        Object[] findLikeIdParams = new Object[]{postId,userId};
        return this.jdbcTemplate.queryForObject(findLikeIdQuery,
                (rs,rowNum) -> new Like(
                        rs.getInt("like_id"),
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getInt("like_status"),
                        rs.getString("like_create_at"))
                ,findLikeIdParams
        );
    }

    public PostLikesRes updateLike(PostLikesReq postLikeReq, int like_status) {
        String updateLikeQuery = "UPDATE Post_like SET like_status = ? WHERE post_id = ? and user_id = ?";
        Object[] updateLikeParams = new Object[]{like_status,postLikeReq.getPostId(),postLikeReq.getUserId()};
        this.jdbcTemplate.update(updateLikeQuery, updateLikeParams);

        int likeId = getLikeId(postLikeReq.getPostId(),postLikeReq.getUserId()).getLikeId();
        return new PostLikesRes(likeId,like_status);
    }

    public GetPostLikeCountRes getPostLikeCount(int postId) {
        String getPostLikeCount = "select post_id, COUNT(user_id) as likeCount from Post_like where post_id = ? group by post_id;";
        return this.jdbcTemplate.queryForObject(getPostLikeCount,
                (rs,rowNum) -> new GetPostLikeCountRes(
                        rs.getInt("post_id"),
                        rs.getInt("likeCount")),
                postId);
    }
}
