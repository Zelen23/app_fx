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
    
            Integer provId;
            Integer postId;
            Integer postdate ;
            Integer postLikes;
            Integer postViews;

            String text ;
            Integer count_photo ;
            List<String> listPhoto ;

    public ConstructorPost( Integer provId,Integer postId, Integer postdate,Integer postViews, Integer postLikes, String text, Integer count_photo, List<String> listPhoto) {
        this.provId = provId;
        this.postId = postId;
        this.postdate = postdate;
        this.postLikes = postLikes;
        this.postViews =postViews;
        this.text = text;
        this.count_photo = count_photo;
        this.listPhoto = listPhoto;
    }
            
            
    
    
    
}
