package com.gui.dto;

import lombok.Data;

/**
 * Created by uuu on 2019/9/9.
 */
@Data
public class AccessTokenDTO {
    private  String client_id;
    private  String client_secret;
    private  String code;
    private  String redirect_uri;
    private  String state;


}
