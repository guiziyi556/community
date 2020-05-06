package com.gui.dto;

import lombok.Data;

/**
 * Created by uuu on 2020/2/10.
 */
@Data
public class CommentCreateDTO {
    private  Integer parentId;
    private  String content;
    private  Integer type;
}
