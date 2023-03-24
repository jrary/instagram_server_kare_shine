package com.example.demo.src.store;


import com.example.demo.src.store.model.GetStoreIdRes;
import com.example.demo.src.store.model.GetStoreRes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.util.List;

@Repository
public class StoreDao {

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

    public int checkStoreAvailable(Long userId, int postId){
        String getStoreNumQuery = "select count(store_id) from Post_store where user_id = ? and post_id = ?";
        return this.jdbcTemplate.queryForObject(getStoreNumQuery,
                int.class,
                userId, postId);
    }

    public int newStore(Long userId, int postId){
        String storePostQuery = "insert into Post_store(user_id, post_id) " +
                "VALUES(?,?)";
        Object[] storePostParams = new Object[]{userId, postId};
        return this.jdbcTemplate.update(storePostQuery, storePostParams);
    }

    public int store(GetStoreIdRes getStoreIdRes){
        String storePostQuery = "update Post_store set store_status = 1 where store_id = ? ";
        Object[] storePostParams = new Object[]{getStoreIdRes.getStoreId()};
        return this.jdbcTemplate.update(storePostQuery, storePostParams);
    }

    public int cancelStore(GetStoreIdRes getStoreIdRes){
        String storePostQuery = "update Post_store set store_status = 0 where store_id = ? ";
        Object[] storePostParams = new Object[]{getStoreIdRes.getStoreId()};
        return this.jdbcTemplate.update(storePostQuery, storePostParams);
    }

    public GetStoreIdRes getStoreId(Long userId, int postId){
        String getStoreIdQuery = "select store_id from Post_store where user_id = ? and post_id = ? ";
        return this.jdbcTemplate.queryForObject(getStoreIdQuery,
                (rs, rowNum) -> new GetStoreIdRes(
                        rs.getInt("store_id")
                ), userId, postId);
    }

    public List<GetStoreRes> storeList(Long userId){
        String getStoreListQuery = "select s.store_id, p.post_id, pi.post_img_url\n" +
                "    from Post_store s\n" +
                "    join Post p on p.post_id = s.post_id\n" +
                "    left join (select min(post_img_create_at), post_img_id, post_img_url, post_id from Post_img group by post_id) as pi on p.post_id = pi.post_id\n" +
                "    join User u on u.user_id = s.user_id\n" +
                "where p.post_status = 1 and s.store_status = 1 and u.user_id = ? ";

        return this.jdbcTemplate.query(getStoreListQuery,
                (rs, rowNum)-> new GetStoreRes(
                        rs.getInt("store_id"),
                        rs.getInt("post_id"),
                        rs.getString("post_img_url")
                ), userId);
    }
}
