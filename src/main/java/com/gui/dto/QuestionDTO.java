package com.gui.dto;

import com.gui.model.User;
import lombok.Data;

/**
 * Created by uuu on 2019/10/29.
 */
@Data
public class QuestionDTO {

        private Integer id;
        private  String title;
        private String description;
        private String tag;
        private Long gmtCreate;
        private Long gmtModified;
        private Integer creator;
        private  Integer viewCount;
        private Integer commentCount;
        private  Integer likeCount;
        private User user;
}
