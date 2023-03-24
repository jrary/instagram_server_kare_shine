package com.example.demo.src.post;

import com.example.demo.config.BaseException;
import com.example.demo.src.post.model.GetPostRes;
import com.example.demo.src.post.model.Post;
import com.example.demo.src.post.model.PostPostsRes;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.DATABASE_ERROR;
import static com.example.demo.config.BaseResponseStatus.NONPOSTID_ERROR;

@Service
public class PostProvider {


    private PostDao postDao;

    public PostProvider(PostDao postDao) {
        this.postDao = postDao;
    }

    public List<GetPostRes> getPosts() throws BaseException {
        try{
            List<Post> posts = postDao.getPosts();
            GetPostRes getPostRes = new GetPostRes();
            List<GetPostRes> getPostReses = getPostRes.getPostsResList(posts);
            return getPostReses;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPostRes> getPostsUserId(int userId) throws BaseException {

        try{
            List<Post> posts = postDao.getPostsUserId(userId);
            GetPostRes getPostRes = new GetPostRes();
            List<GetPostRes> getPostReses = getPostRes.getPostsResList(posts);
            return getPostReses;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPostRes> getHomePosts(int userId) throws BaseException {
        try{
            List<Post> posts = postDao.getHomePosts(userId);
//            System.out.println(posts.toString());
            GetPostRes getPostRes = new GetPostRes();
            List<GetPostRes> getPostReses = getPostRes.getPostsResList(posts);
//            System.out.println(getPostReses);
            return getPostReses;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetPostRes> getPostPostId(int postId) throws BaseException {
        if(postDao.getPostId(postId) == 0){
            throw new BaseException(NONPOSTID_ERROR);
        }
        try{
            List<Post> posts = postDao.getPostPostId(postId);
            GetPostRes getPostRes = new GetPostRes();
            List<GetPostRes> getPostReses= getPostRes.getPostsResList(posts);
            return getPostReses;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public PostPostsRes getDeletePostId(int postId) throws BaseException {
        if(postDao.getPostId(postId) == 0){
            throw new BaseException(NONPOSTID_ERROR);
        }
        try{
            PostPostsRes posts = postDao.getDeletePostId(postId);
            return posts;
        }
        catch (Exception exception) {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
