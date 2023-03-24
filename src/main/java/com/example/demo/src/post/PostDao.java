package com.example.demo.src.post;


import com.example.demo.src.post.model.Post;
import com.example.demo.src.post.model.PostPostsReq;
import com.example.demo.src.post.model.PostPostsRes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import javax.transaction.Transactional;
import java.util.List;

@Repository
public class PostDao {

    private JdbcTemplate jdbcTemplate;

    @Autowired
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Post> getPosts() {
        String getPostsQuery = "select Post.post_id post_id,user_id,post_text,post_status,post_create_at,post_img_id,post_img_url from Post left join Post_img Pi on Post.post_id = Pi.post_id order by post_update_at desc ";
        return this.jdbcTemplate.query(getPostsQuery,
                (rs,rowNum) -> new Post(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("post_text"),
                        rs.getInt("post_status"),
                        rs.getString("post_create_at"),
                        rs.getInt("post_img_id"),
                        rs.getString("post_img_url")

                )
        );
    }

    public List<Post> getHomePosts(int userId) {
        String getPostsQuery = "(select Post.post_id post_id, user_id, post_text, post_status,\n" +
                "        case when TIMESTAMPDIFF(minute, post_create_at, current_timestamp)<60 then concat(TIMESTAMPDIFF(minute, post_create_at, current_timestamp),'분 전')\n" +
                "              when TIMESTAMPDIFF(hour, post_create_at, current_timestamp)<24 then concat(TIMESTAMPDIFF(hour , post_create_at, current_timestamp),'시간 전')\n" +
                "              when TIMESTAMPDIFF(day, post_create_at, current_timestamp)<31 then concat(TIMESTAMPDIFF(day, post_create_at, current_timestamp),'일 전')\n" +
                "            when TIMESTAMPDIFF(month, post_create_at, current_timestamp)<13 then concat(TIMESTAMPDIFF(month, post_create_at, current_timestamp),'개월전')\n" +
                "        else\n" +
                "            DATE_FORMAT(post_create_at,'%M %d %Y') end as post_create_at\n" +
                "\n" +
                "       , post_img_id, post_img_url\n" +
                " from Post\n" +
                "          inner join Post_img Pi on Post.post_id = Pi.post_id\n" +
                "          inner join (select followee_id from Follow where follower_id = ?) friends\n" +
                "                     on Post.user_id = friends.followee_id where post_status = 1)\n" +
                "union\n" +
                "(select Post.post_id post_id, user_id, post_text, post_status,\n" +
                "        case when TIMESTAMPDIFF(minute, post_create_at, current_timestamp)<60 then concat(TIMESTAMPDIFF(minute, post_create_at, current_timestamp),'분 전')\n" +
                "              when TIMESTAMPDIFF(hour, post_create_at, current_timestamp)<24 then concat(TIMESTAMPDIFF(hour , post_create_at, current_timestamp),'시간 전')\n" +
                "              when TIMESTAMPDIFF(day, post_create_at, current_timestamp)<31 then concat(TIMESTAMPDIFF(day, post_create_at, current_timestamp),'일 전')\n" +
                "            when TIMESTAMPDIFF(month, post_create_at, current_timestamp)<13 then concat(TIMESTAMPDIFF(month, post_create_at, current_timestamp),'개월 전')\n" +
                "        else\n" +
                "            DATE_FORMAT(post_create_at,'%M %d %Y') end as post_create_at\n" +
                "      ,post_img_id, post_img_url\n" +
                " from Post\n" +
                "          left join Post_img Pi on Post.post_id = Pi.post_id\n" +
                " where user_id = ? and Post.post_status = 1)\n" +
                "order by post_create_at desc;";
        Object[] createUserIdParams = new Object[]{userId, userId};
        return this.jdbcTemplate.query(getPostsQuery,
                (rs,rowNum) -> new Post(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("post_text"),
                        rs.getInt("post_status"),
                        rs.getString("post_create_at"),
                        rs.getInt("post_img_id"),
                        rs.getString("post_img_url")),
                createUserIdParams
                );
    }

    public List<Post> getPostsUserId(int userId) {
        String getPostsUserIdQuery = "select Post.post_id post_id,user_id,post_text,post_status,post_create_at,post_img_id,post_img_url from Post  left join Post_img Pi on Post.post_id = Pi.post_id where Post.user_id = ? order by post_update_at desc;";
        int getPostParams = userId;
        return this.jdbcTemplate.query(getPostsUserIdQuery,
                (rs,rowNum) -> new Post(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("post_text"),
                        rs.getInt("post_status"),
                        rs.getString("post_create_at"),
                        rs.getInt("post_img_id"),
                        rs.getString("post_img_url"))
                , getPostParams
        );
    }

    public List<Post> getPostPostId(int postId) {
        String getPostPostIdQuery = "select Post.post_id post_id,user_id,post_text,post_status,case when TIMESTAMPDIFF(minute, post_create_at, current_timestamp)<60 then concat(TIMESTAMPDIFF(minute, post_create_at, current_timestamp),'분 전')\n" +
                "              when TIMESTAMPDIFF(hour, post_create_at, current_timestamp)<24 then concat(TIMESTAMPDIFF(hour , post_create_at, current_timestamp),'시간 전')\n" +
                "              when TIMESTAMPDIFF(day, post_create_at, current_timestamp)<31 then concat(TIMESTAMPDIFF(day, post_create_at, current_timestamp),'일 전')\n" +
                "            when TIMESTAMPDIFF(month, post_create_at, current_timestamp)<13 then concat(TIMESTAMPDIFF(month, post_create_at, current_timestamp),'개월전')\n" +
                "        else\n" +
                "            DATE_FORMAT(post_create_at,'%M %d %Y') end as post_create_at\n" +
                "     ,post_img_id,post_img_url from Post  left join Post_img Pi on Post.post_id = Pi.post_id where Post.post_id = ? ";
        int getPostParams = postId;
        return this.jdbcTemplate.query(getPostPostIdQuery,
                (rs,rowNum) -> new Post(
                        rs.getInt("post_id"),
                        rs.getInt("user_id"),
                        rs.getString("post_text"),
                        rs.getInt("post_status"),
                        rs.getString("post_create_at"),
                        rs.getInt("post_img_id"),
                        rs.getString("post_img_url"))
                ,getPostParams
        );
    }

    @Transactional
    public PostPostsRes createPost(PostPostsReq postPostsReq) {
        String createPostQuery = "insert into Post (user_id , post_text) VALUES (?,?)";
        Object[] createPostParams = new Object[]{postPostsReq.getUserId(), postPostsReq.getPostText()};
        this.jdbcTemplate.update(createPostQuery, createPostParams); // 포스트 생성
        String lastInserIdQuery = "select last_insert_id()";
        int newPostId = this.jdbcTemplate.queryForObject(lastInserIdQuery,int.class);
        String createPostImgQuery; // 포스트 img 처리
        Object[] createPostImgParams = new Object[2];
        for (int i = 0; i < postPostsReq.getPostImg().length; i++) {
            createPostImgParams[0] = postPostsReq.getPostImg()[i]; // post_img_url삽입
            createPostImgParams[1] = newPostId; // 생성하는 postId
            createPostImgQuery = "insert into Post_img (post_img_url,post_id) VALUES (?,?)";
            this.jdbcTemplate.update(createPostImgQuery,createPostImgParams);
        }

        return new PostPostsRes(newPostId,1);
    }

    public int getPostId(int postId) {
        String query = "select exists(select post_id from Post where post_id = ?);";
        return this.jdbcTemplate.queryForObject(query,
                int.class,
                postId);
    }

    public PostPostsRes getDeletePostId(int postId) {
        String updatePostQuery = "UPDATE Post SET post_status = ? WHERE post_id = ? ";
        Object[] updatePostParams = new Object[]{0,postId};
        this.jdbcTemplate.update(updatePostQuery, updatePostParams);
        return new PostPostsRes(postId,0);
    }
}
