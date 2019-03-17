/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.vk.api.sdk.client.actors.UserActor;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections4.list.AbstractLinkedList;

/**
 *
 * @author adolf
 */
public class Poster extends Thread{
    
    
    List<ConstructorPost> listPost;

    public Poster(List<ConstructorPost> listPost) {
        this.listPost = listPost;
    }
    
    
     Helper helper=new Helper();
       
       Vk_api vk_api = new Vk_api();
        UserActor userActor = vk_api.getActor(Integer.parseInt(
            new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
            new Vk_preferences().getPref(Vk_preferences.TOKEN));
       // System.out.println("text: "+ helper.unixTime());
       
   /*Получаю массив считаю колличество элементов*/
   Vk_preferences pref = new Vk_preferences();
   
   Integer TimeInterval=new DbHandler().settingsList("TimeInterval",
           Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID)));
    
    @Override
    public void run() {
        //дата время
        //интервал
        //цикл в коллиество строк на отправку
    
    int i=1;  
    for(ConstructorPost elt:listPost){    
        i=i+TimeInterval;
        Long time= helper.timeadd(helper.unixTime(), i);
      
        new Vk_api().setPost(
                userActor,
                elt.text,
                time,
                new Vk_api().addPhoto(userActor, elt.listPhoto),
                userActor.getId()
                //в метод передаю массив ссылок на фотку 
        );
        
         
        
        
    }
     
    

//To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
    
}
