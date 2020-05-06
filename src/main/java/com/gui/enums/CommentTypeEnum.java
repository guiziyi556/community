package com.gui.enums;

/**
 * Created by uuu on 2020/2/17.
 */
public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private  Integer type;
    public  Integer getType(){
        return type;
    }
    CommentTypeEnum(int type){
        this.type=type;
    }

    public static boolean isExist(Integer type) {
        for(CommentTypeEnum commentTypeEnum:CommentTypeEnum.values()){
            if(type==commentTypeEnum.getType()){
                return  true;
            }
        }
        return false;
    }
}
