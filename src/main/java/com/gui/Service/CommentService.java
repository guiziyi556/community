package com.gui.Service;

import com.gui.dto.CommentDTO;
import com.gui.enums.CommentTypeEnum;
import com.gui.enums.NotificationTypeEnum;
import com.gui.exception.CustomizeErrorCode;
import com.gui.exception.CustomizeException;
import com.gui.mapper.*;
import com.gui.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by uuu on 2020/2/17.
 */
@Service
public class CommentService {
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private QuestionExtMapper questionExtMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private CommentExtMapper commentExtMapper;
    @Resource
    private NotificationMapper notificationMapper;

    //插入问题的评论
    @Transactional
    public void insert(Comment comment,User commentator) {
        if ((comment.getParentId() == null) || (comment.getParentId() == 0)) {
            //当问题不存在时，不能予以评论
            throw new CustomizeException(CustomizeErrorCode.TAGET_PARAM_NOT_FOUND);
        }
        //当回复类型不存在时，提示异常
        if ((comment.getType() == null) || (!CommentTypeEnum.isExist(comment.getType()))) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        if (comment.getType() == CommentTypeEnum.QUESTION.getType()) {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }

            //防止默认值为null产生的问题
            comment.setCommentCount(0);
            commentMapper.insert(comment);
            //回复问题时评论数加1
            question.setCommentCount(1);
            questionExtMapper.incCommentCount(question);
            //创建通知
            createNotify(comment,question.getCreator(),commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION);
        } else {
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            commentMapper.insert(comment);
            //增加评论数
            Comment parentComment=new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.incCommentCount(parentComment);
            //创建通知
            Question question=questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if(question==null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            createNotify(comment,dbComment.getCommentator(),commentator.getName(),question.getTitle(),NotificationTypeEnum.REPLY_COMMENT);
        }
    }

    //通过id来获取comment列表
    public List<CommentDTO> listByTargetId(Integer id, CommentTypeEnum type) {
        //获取所有的comment信息
        CommentExample commentExample = new CommentExample();
        //通过Type的不同来显示不同级别的comment列表
        commentExample.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
       if(comments.size()<=0){
           return new ArrayList<>();
       }
        //获取去重后评论人
     Set<Integer> commentators=comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
       List<Integer> userId=new ArrayList<>();
       userId.addAll(commentators);
       //通过userId来获取评论人，并转换为map
        UserExample userExample=new UserExample();
        userExample.createCriteria().andIdIn(userId);
        List<User> users=userMapper.selectByExample(userExample);
        //转换成map
        Map<Integer,User> userMap=users.stream().collect(Collectors.toMap(user->user.getId(),user->user));
        //将查找到的comment列表和User对象相互关联，转换为CommentDTO
        List<CommentDTO> commentDTOs=comments.stream().map(comment -> {
         CommentDTO commentDTO=new CommentDTO();
         BeanUtils.copyProperties(comment,commentDTO);
         commentDTO.setUser(userMap.get(comment.getCommentator()));
         return commentDTO;
        }).collect(Collectors.toList());
     return commentDTOs;
    }
    //创建通知
    private void createNotify(Comment comment, Integer receiver, String notifierName,String outerTitle,NotificationTypeEnum notificationType){
        //当回复自己时不产生提示通知。
        if(receiver==comment.getCommentator()){
            return;
        }
        Notification notification=new Notification();
        notification.setGmtCreate(System.currentTimeMillis());
        notification.setType(notificationType.getType());
        notification.setOuterid(comment.getParentId());  //被评论的问题或评论
        notification.setNotifier(comment.getCommentator());
        notification.setReceiver(receiver);     //接收者
        notification.setOuterTitle(outerTitle);     //回复的标题
        notification.setNotifierName(notifierName);  //通知者
        notificationMapper.insert(notification);
    }
}
