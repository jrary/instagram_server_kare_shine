package com.example.demo.src.comment;

import com.example.demo.config.BaseException;
import com.example.demo.src.comment.model.GetCommentRes;
import com.example.demo.src.comment.model.GetHomeCommentRes;
import com.example.demo.src.comment.model.PostCommentReq;
import com.example.demo.src.user.UserDao;
import com.example.demo.src.user.UserProvider;
import com.example.demo.src.user.model.*;
import com.example.demo.utils.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.config.BaseResponseStatus.*;
import static com.example.demo.utils.SHA256.encrypt;

// Service Create, Update, Delete 의 로직 처리
@Service
public class CommentService {

    final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final CommentDao commentDao;
    private final CommentProvider commentProvider;
    private final JwtService jwtService;

    @Autowired
    public CommentService(CommentDao commentDao, CommentProvider commentProvider, JwtService jwtService) {
        this.commentDao = commentDao;
        this.commentProvider = commentProvider;
        this.jwtService = jwtService;

    }

    public List<GetCommentRes> commentList (int postId) throws BaseException{
        try{
            // 댓글 리스트 불러오기
            List<GetCommentRes> commentResList = commentDao.commentList(postId);
            return commentResList;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public int commentNum (int postId) throws BaseException{
        try{
            // 댓글 개수 카운트
            int commentNum = commentDao.commentNum(postId);
            return commentNum;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public List<GetHomeCommentRes> commentHome (int postId, Long userId) throws BaseException{
        try{
            // 홈 화면에서 볼 수 있는 댓글 리스트 불러오기
            List<GetHomeCommentRes> commentNum = commentDao.commentHome(postId, userId);
            return commentNum;
        } catch (Exception e){
            throw new BaseException(DATABASE_ERROR);
        }
    }

    public String postComment (PostCommentReq postCommentReq, Long userId, int postId) throws BaseException{
        // 성공했을 경우, 1이 반환됨. -> 1일때만 성공 메시지 반환
        if(commentDao.postComment(postCommentReq, userId, postId) == 1){
            return "댓글을 등록하였습니다.";
        } else {
            throw new BaseException(DATABASE_ERROR);
        }
    }
}
