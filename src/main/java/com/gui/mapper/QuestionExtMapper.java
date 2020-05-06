package com.gui.mapper;

import com.gui.dto.QuestionQueryDTO;
import com.gui.model.Question;
import com.gui.model.QuestionExample;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.RowBounds;

import java.util.List;

public interface QuestionExtMapper {
    //增加阅读数
    int incView(Question record);
    //增加评论数
    int incCommentCount(Question record);
    //根据标签查找相关问题
    List<Question> selectRelated(Question question);
    //通过搜索框查找相关问题
    List<Question> selectBySearch(QuestionQueryDTO questionQueryDTO);
}