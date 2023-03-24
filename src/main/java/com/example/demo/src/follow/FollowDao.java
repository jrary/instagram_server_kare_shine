package com.example.demo.src.follow;


import com.example.demo.src.follow.model.Follow;
import com.example.demo.src.follow.model.GetFollowerListRes;
import com.example.demo.src.follow.model.PostFollowReq;
import com.example.demo.src.follow.model.PostFollowRes;
import com.example.demo.src.post.model.Post;
import com.example.demo.src.post.model.PostPostsReq;
import com.example.demo.src.post.model.PostPostsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class FollowDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Follow> getFollowers(int userId) { // userId를 팔로우한 사람들
        String getFollowersQuery = "select * from Follow where followee_id = ? and follow_status = 1";
        return this.jdbcTemplate.query(getFollowersQuery,
                (rs,rowNum) -> new Follow(
                        rs.getInt("follow_id"),
                        rs.getInt("follower_id"),
                        rs.getInt("followee_id"),
                        rs.getInt("follow_status"),
                        rs.getString("follow_create_at"))
                ,userId
        );
    }

    public List<Follow> getFollowees(int userId) { // userId를 팔로우한 사람들
        String getFollowersQuery = "select * from Follow where follower_id = ? and follow_status = 1";
        return this.jdbcTemplate.query(getFollowersQuery,
                (rs,rowNum) -> new Follow(
                        rs.getInt("follow_id"),
                        rs.getInt("follower_id"),
                        rs.getInt("followee_id"),
                        rs.getInt("follow_status"),
                        rs.getString("follow_create_at"))
                ,userId
        );
    }
    public int checkFollows(PostFollowReq postFollowReq) {
        String checkFollowQuery = "select exists(select * from Follow where follower_id = ? and followee_id = ?)";
        Object[] checkFollowParams = new Object[]{postFollowReq.getFollowerId(),postFollowReq.getFolloweeId()};
        return this.jdbcTemplate.queryForObject(checkFollowQuery,
                int.class,
                checkFollowParams);
    }

    public PostFollowRes createFollow(PostFollowReq postFollowReq) {
        String createFollowQuery = "insert into Follow (follower_id,followee_id) VALUES (?,?)";
        Object[] createFollowParams = new Object[]{postFollowReq.getFollowerId(),postFollowReq.getFolloweeId()};
        this.jdbcTemplate.update(createFollowQuery, createFollowParams);
        String lastInserIdQuery = "select last_insert_id()";
        int newFollowId = this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
        return new PostFollowRes(newFollowId,1);
    }

    public Follow getFollowId(int followerId,int followeeId){
        String findFollowIdQuery = "Select * from Follow where follower_id = ? and followee_id = ?";
        Object[] findFollowIdParams = new Object[]{followerId,followeeId};
        return this.jdbcTemplate.queryForObject(findFollowIdQuery,
                (rs,rowNum) -> new Follow(
                        rs.getInt("follow_id"),
                        rs.getInt("follower_id"),
                        rs.getInt("followee_id"),
                        rs.getInt("follow_status"),
                        rs.getString("follow_create_at"))
                ,findFollowIdParams
        );
    }

    public PostFollowRes updateFollow(PostFollowReq postFollowReq,int follow_status) {
        String updateFollowQuery = "UPDATE Follow SET follow_status = ? WHERE follower_id = ? and followee_id = ?";
        Object[] updateFollowParams = new Object[]{follow_status,postFollowReq.getFollowerId(),postFollowReq.getFolloweeId()};
        this.jdbcTemplate.update(updateFollowQuery, updateFollowParams);

        int followId = getFollowId(postFollowReq.getFollowerId(),postFollowReq.getFolloweeId()).getFollowId();
        return new PostFollowRes(followId,follow_status);
    }


}
