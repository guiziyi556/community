package com.gui.cache;

import com.gui.dto.TagDTO;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by uuu on 2020/3/19.
 */
public class TagCache {
   //返回所有标签
    public  static List<TagDTO> get(){
        List<TagDTO> tagDTOS=new ArrayList<>();
        TagDTO program=new TagDTO();
        program.setCategoryName("开发语言");
        program.setTags(Arrays.asList("java","php","javascript","python","c++","c","golang"));
        tagDTOS.add(program);

        TagDTO framework=new TagDTO();
        framework.setCategoryName("平台框架");
        framework.setTags(Arrays.asList("spring","express","springBoot","springMVC"));
        tagDTOS.add(framework);

        TagDTO server=new TagDTO();
        server.setCategoryName("服务器");
        server.setTags(Arrays.asList("linux","nginx","apache","ubuntu","hadoop"));
        tagDTOS.add(server);

        TagDTO db=new TagDTO();
        db.setCategoryName("数据库");
        db.setTags(Arrays.asList("mysql","redis","mongodb","sql","oracle"));
        tagDTOS.add(db);

        TagDTO tool=new TagDTO();
        tool.setCategoryName("开发工具");
        tool.setTags(Arrays.asList("git","github","eclipse","maven","vim"));
        tagDTOS.add(tool);
        return tagDTOS;
    }
    //获取非法的标签集合
    public  static  String filterInvalid(String tags){
        String[] split= StringUtils.split(tags,",");
        List<TagDTO> tagDTOS=get();
       List<String> tagList= tagDTOS.stream().flatMap(tag->tag.getTags().stream()).collect(Collectors.toList());
      String invalid=Arrays.stream(split).filter(t->!tagList.contains(t)).collect(Collectors.joining(","));
      return invalid;
    }
}
