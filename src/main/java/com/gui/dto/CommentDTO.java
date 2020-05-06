package com.gui.dto;

import com.gui.model.User;
import lombok.Data;

/**
 * Created by uuu on 2020/3/1.
 */
@Data
public class CommentDTO {
    private Integer id;
    private Integer parentId;
    private Integer type;
    private Integer commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private Long commentCount;
    private String content;
    private User user;
}
