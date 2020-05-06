package com.gui.dto;

import lombok.Data;

import java.util.List;

/**
 * Created by uuu on 2020/3/19.
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
