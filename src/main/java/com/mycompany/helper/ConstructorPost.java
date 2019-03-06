/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import java.util.List;

/**
 *
 * @author adolf
 */

public class ConstructorPost  {
    
        public    Integer provId;
        public   Integer postId;
        public   Long postdate ;
        public   Integer postLikes;
        public  Integer postViews;
        public  boolean flag;
        public String text ;
        public  Integer count_photo ;
        public List<String> listPhoto ;
        
    public void setSelected(boolean selected) {
            this.flag=selected;
        }    

    public ConstructorPost( Integer provId,Integer postId, Long postdate,Integer postViews, Integer postLikes, String text, Integer count_photo, List<String> listPhoto, boolean flag) {
        this.provId = provId;
        this.postId = postId;
        this.postdate = postdate;
        this.postLikes = postLikes;
        this.postViews =postViews;
        this.text = text;
        this.count_photo = count_photo;
        this.listPhoto = listPhoto;
        this.flag=flag;
    }
            
            
    
    
    
}
