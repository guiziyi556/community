package com.gui.Service;

import com.gui.dto.PaginationDTO;
import com.gui.dto.QuestionDTO;
import com.gui.dto.QuestionQueryDTO;
import com.gui.exception.CustomizeErrorCode;
import com.gui.exception.CustomizeException;
import com.gui.mapper.QuestionExtMapper;
import com.gui.mapper.QuestionMapper;
import com.gui.mapper.UserMapper;
import com.gui.model.Question;
import com.gui.model.QuestionExample;
import com.gui.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by uuu on 2019/10/29.
 */
@Service
public class QuestionService {
    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private UserMapper userMapper;
    @Resource
    private QuestionExtMapper questionExtMapper;

    public PaginationDTO list(String search, Integer page, Integer size) {
        /*主页的搜索按钮*/
        if(StringUtils.isNotBlank(search)){
            String[] tags=StringUtils.split(search," ");
            search=Arrays.stream(tags).collect(Collectors.joining("|"));
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        //Integer totalcount=questionMapper.count();
        Integer totalcount = (int) questionMapper.countByExample(new QuestionExample());
        paginationDTO.setPagination(totalcount, page, size);
        if (page <= 1) {
            page = 1;
        } else if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        Integer offset = (page - 1) * size;
        //通过QuestionMapper来返回问题列表
        // List<Question> questions=questionMapper.list(offset,size);
        QuestionExample questionExample = new QuestionExample();
        questionExample.setOrderByClause("gmt_create desc");
        //通过search来查找问题列表
        QuestionQueryDTO questionQueryDTO=new QuestionQueryDTO();
        questionQueryDTO.setSearch(search);
        questionQueryDTO.setPage(offset);
        questionQueryDTO.setSize(size);
        //返回问题列表
       List<Question> questions= questionExtMapper.selectBySearch(questionQueryDTO);
        //List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));

        List<QuestionDTO> questionDTOList = new ArrayList<>();


        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //复制对象
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    //根据登录的user来显示相应的问题列表
    public PaginationDTO list(Integer userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        // Integer totalcount=questionMapper.countByUserId(userId);
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalcount = (int) questionMapper.countByExample(questionExample);
        paginationDTO.setPagination(totalcount, page, size);
        if (page <= 1) {
            page = 1;

        } else if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        Integer offset = (page - 1) * size;

        //通过QuestionMapper来返回问题列表
        // List<Question> questions=questionMapper.listByUserId(userId,offset,size);
        QuestionExample questionExample1 = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        List<Question> questions = questionMapper.selectByExampleWithRowbounds(questionExample, new RowBounds(offset, size));
        List<QuestionDTO> questionDTOList = new ArrayList<>();

        for (Question question : questions) {
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //复制对象
            BeanUtils.copyProperties(question, questionDTO);
            questionDTO.setUser(user);
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);

        return paginationDTO;
    }

    public QuestionDTO getById(Integer id) {
        // Question question=questionMapper.getById(id);
        Question question = questionMapper.selectByPrimaryKey(id);
        //所访问的question不存在时，抛出异常
        if (question == null) {
            throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        BeanUtils.copyProperties(question, questionDTO);
        return questionDTO;
    }

    public void CreateOrUpdate(Question question) {
        if (question.getId() == null) {
            //创建
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            //questionMapper.create(question);
            //设置初始值（阅读数，评论数）
            question.setViewCount(0);
            question.setCommentCount(0);
            question.setLikeCount(0);
            questionMapper.insert(question);
        } else {
            //更新
            // question.setGmtModified(System.currentTimeMillis());
            //questionMapper.update(question);
            //并不是question中的所有属性都要更新
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(System.currentTimeMillis());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            int updated = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            if (updated != 1) {
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
        }
    }

    //用于记录阅读数
    public void incView(Integer id) {
        Question question = new Question();
        question.setId(id);
        //递增为1
        question.setViewCount(1);
        questionExtMapper.incView(question);
    }

    //用来显示相关问题
    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())) {
            return new ArrayList<>();
        }
        String[] tags = StringUtils.split(queryDTO.getTag(), ",");
        //regexpTag为处理后的标签字符串。
        String regexpTag = Arrays.stream(tags).collect(Collectors.joining("|"));
        //根据传入的question信息查找相关问题
        Question question = new Question();
        question.setId(queryDTO.getId());
        question.setTag(regexpTag);
        //查找相关问题
        List<Question> questions = questionExtMapper.selectRelated(question);
        List<QuestionDTO> questionDTOS=questions.stream().map(q->{
            QuestionDTO questionDTO=new QuestionDTO();
            BeanUtils.copyProperties(q,questionDTO);
            return questionDTO;
                }).collect(Collectors.toList());
       return questionDTOS;
    }
}

