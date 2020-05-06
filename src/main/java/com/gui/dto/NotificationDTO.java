package com.gui.dto;

import lombok.Data;

@Data
public class NotificationDTO {
    private  Integer id;
    private  Long gmtCreate;
    private Integer status;//判断已读或未读
    private Integer notifier;
    private String notifierName;
    private String outerTitle;
    private Integer outerid;
    private String typeName;
    private Integer type;

}
