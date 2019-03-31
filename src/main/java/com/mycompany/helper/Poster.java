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
    Long times;
    int vk_id;

    public Poster(List<ConstructorPost> listPost,Long times, int vk_id) {
        this.listPost = listPost;
        this.times=times;
        this.vk_id=vk_id;
    }
    
    
    
    Helper helper=new Helper();
       
    Vk_api vk_api = new Vk_api();
    UserActor userActor = vk_api.getActor(Integer.parseInt(
        new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
        new Vk_preferences().getPref(Vk_preferences.TOKEN));
   

   
    @Override
    public void run() {
        //дата время
        //интервал
        //цикл в коллиество строк на отправку
    System.out.println("Poster_RUN_actorID "+ userActor.getId());
    
    Integer TimeInterval=new DbHandler().settingsList("TimeInterval", vk_id);
    int i=1;  
    
    for(ConstructorPost elt:listPost){    
        i=i+TimeInterval;
       // Long time= helper.timeadd(helper.unixTime(), i);
        Long time= helper.timeadd(times, i);
      
        new Vk_api().setPost(
                userActor,
                elt.text,
                time,
                new Vk_api().addPhoto(userActor, elt.listPhoto, elt.text),
                userActor.getId()
                //в метод передаю массив ссылок на фотку 
        );
              
    }
     
    

//To change body of generated methods, choose Tools | Templates.
    }
    
      public UserActor Actor(){
      Vk_api vk_api = new Vk_api();
        UserActor userActor = vk_api.getActor(Integer.parseInt(
            new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
            new DbHandler().getToken(vk_id));
   return userActor;
   }
    
    
    
    
    
}
