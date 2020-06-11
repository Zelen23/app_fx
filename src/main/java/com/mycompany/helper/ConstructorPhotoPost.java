/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import java.util.ArrayList;

/**
 *
 * @author azelinsky
 */
public class ConstructorPhotoPost {

    public ConstructorPhotoPost(Integer post_ID, ArrayList<Integer> photoID_inPost) {
        this.post_ID = post_ID;
        this.photoID_inPost = photoID_inPost;
    }
    
   public Integer post_ID;
   public ArrayList<Integer>photoID_inPost;
    
}
