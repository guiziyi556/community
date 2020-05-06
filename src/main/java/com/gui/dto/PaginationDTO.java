package com.gui.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by uuu on 2019/11/21.
 */
@Data
public class PaginationDTO<T> {
   private List<T> data;
   private boolean showFirstPage;
   private boolean showEndPage;
   private boolean showPrevious;
   private boolean showNext;
   private Integer page;  //当前页面
   private List<Integer> pages=new ArrayList<>();//页面列表
   private Integer totalPage;

    public void setPagination(Integer totalcount, Integer page, Integer size) {

         if(totalcount%size==0){
             totalPage=totalcount/size;
         }else{
             totalPage=totalcount/size+1;
         }
         /*容错机制*/
         if(page<=1){
             page=1;

         }else if(page>totalPage){
             page=totalPage;
         }
        this.page=page;
     /*插入page,根据当前的页面显示页面列表*/
        pages.add(page);
      for(int i=1;i<=3;i++){
          if(page-i>0){
              pages.add(0,page-i);
          }

          if(page+i<=totalPage){
              pages.add(page+i);
          }
      }

         /*是否显示上一页*/
         if(page==1){
             showPrevious=false;
         }else {
             showPrevious=true;
         }
         if(page==totalPage){
             showNext=false;
         }else{
             showNext=true;
         }
         /*是否展示第一页*/
         if(pages.contains(1)){
              showFirstPage=false;
         }else {
             showFirstPage=true;
         }
         /*是否展示最后一页*/
         if(pages.contains(totalPage)){
             showEndPage=false;
         }else{
             showEndPage=true;
         }
    }
}
