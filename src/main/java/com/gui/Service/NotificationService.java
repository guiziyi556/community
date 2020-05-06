package com.gui.Service;

import com.gui.dto.NotificationDTO;
import com.gui.dto.PaginationDTO;
import com.gui.dto.QuestionDTO;
import com.gui.enums.NotificationStatusEnum;
import com.gui.enums.NotificationTypeEnum;
import com.gui.exception.CustomizeErrorCode;
import com.gui.exception.CustomizeException;
import com.gui.mapper.NotificationMapper;
import com.gui.model.*;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
public class NotificationService {
    @Resource
    private NotificationMapper notificationMapper;
    public PaginationDTO list(Integer id, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO<>();
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(id);
        notificationExample.setOrderByClause("gmt_create desc");
        Integer totalcount = (int) notificationMapper.countByExample(notificationExample);
        paginationDTO.setPagination(totalcount, page, size);
        if (page <= 1) {
            page = 1;

        } else if (page > paginationDTO.getTotalPage()) {
            page = paginationDTO.getTotalPage();
        }
        Integer offset = (page - 1) * size;

        //通过QuestionMapper来返回问题列表
        // List<Question> questions=questionMapper.listByUserId(userId,offset,size);
        List<QuestionDTO> questionDTOList = new ArrayList<>();
       //通过NotificationMapper来返回回复列表
       List<Notification> notifications=notificationMapper.selectByExampleWithRowbounds(notificationExample, new RowBounds(offset, size));
       if(notifications.size()==0){
           return paginationDTO;
       }
       //DTO的list存放
        List<NotificationDTO> notificationDTOS=new ArrayList<>();
       for (Notification notification: notifications) {
          NotificationDTO notificationDTO=new NotificationDTO();
          //复制属性
          BeanUtils.copyProperties(notification,notificationDTO);
          notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));

          notificationDTOS.add(notificationDTO);
        }
        paginationDTO.setData(notificationDTOS);

        return paginationDTO;
    }

    public Long unreadCount(Integer userId) {
        NotificationExample notificationExample=new NotificationExample();
        notificationExample.createCriteria().andReceiverEqualTo(userId).andStatusEqualTo(NotificationStatusEnum.UNREAD.getStatus());
        return notificationMapper.countByExample(notificationExample);
    }

    public NotificationDTO read(Integer id, User user) {
        Notification notification=notificationMapper.selectByPrimaryKey(id);
         if(notification==null){
             throw new CustomizeException(CustomizeErrorCode.NOTIFICATION_NOT_FOUND);
         }
         if(!Objects.equals(notification.getReceiver(),user.getId())){
             throw new CustomizeException(CustomizeErrorCode.READ_NOTIFICATION_FAIL);
         }
         //更改状态为已读
        notification.setStatus(NotificationStatusEnum.READ.getStatus());
         notificationMapper.updateByPrimaryKey(notification);
         //
         NotificationDTO notificationDTO=new NotificationDTO();
         BeanUtils.copyProperties(notification,notificationDTO);
         notificationDTO.setTypeName(NotificationTypeEnum.nameOfType(notification.getType()));
         return notificationDTO;
    }
}
