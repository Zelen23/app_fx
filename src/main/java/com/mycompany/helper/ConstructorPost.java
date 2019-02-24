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


/* Integer provId = getwalls.getItems().get(0).getOwnerId();
            Integer postId = getwalls.getItems().get(0).getId();
            Integer postdate = getwalls.getItems().get(0).getDate();
            Integer postLikes = getwalls.getItems().get(0).getLikes().getCount();

            String text = getwalls.getItems().get(0).getText();
            Integer count_photo = getwalls.getItems().get(0).getAttachments().size();
            List<String> listPhoto = new ArrayList<String>();*/
public class ConstructorPost  {
    
            Integer postId;
            Integer postdate ;
            Integer postLikes;

            String text ;
            Integer count_photo ;
            List<String> listPhoto ;

    public ConstructorPost(Integer postId, Integer postdate, Integer postLikes, String text, Integer count_photo, List<String> listPhoto) {
        this.postId = postId;
        this.postdate = postdate;
        this.postLikes = postLikes;
        this.text = text;
        this.count_photo = count_photo;
        this.listPhoto = listPhoto;
    }
            
            
    
    
    
}
