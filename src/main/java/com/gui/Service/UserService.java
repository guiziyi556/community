package com.gui.Service;

import com.gui.mapper.UserMapper;
import com.gui.model.User;
import com.gui.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by uuu on 2019/12/11.
 */
@Service
public class UserService {
  @Resource
    private UserMapper userMapper;

    public void createOrUpdate(User user) {
        UserExample userExample= new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users= userMapper.selectByExample(userExample);
     // User dbUser= userMapper.findByAccountId(user.getAccountId());
      if(users.size()!=0){
          //更新
          User dbUser=users.get(0);
          User updateUser=new User();
          updateUser.setGmtModified(System.currentTimeMillis());
          updateUser.setAvatarUrl(user.getAvatarUrl());
          updateUser.setName(user.getName());
          updateUser.setToken(user.getToken());
          //userMapper.update(dbUser);
          UserExample example=new UserExample();
          example.createCriteria().andIdEqualTo(dbUser.getId());
          userMapper.updateByExampleSelective(updateUser,example);
      }else {
          //插入
          user.setGmtCreate(System.currentTimeMillis());//update不需要创建时间
          user.setGmtModified(user.getGmtCreate());
          userMapper.insert(user);

      }
    }
}
