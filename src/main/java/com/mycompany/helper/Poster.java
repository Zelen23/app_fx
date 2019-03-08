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

    List<String>pppp=new ArrayList<>();
    
    @Override
    public void run() {
        //дата время
        //интервал
        //цикл в коллиество строк на отправку
    
    int i=10;  
    for(ConstructorPost elt:listPost){    
        Long time= helper.timeadd(helper.unixTime(), i);
        new Vk_api().setPost(
                userActor,
                elt.text,
                time
                //в метод передаю массив ссылок на фотку 
        );
        
         i=i+10;
    
    }
     
    

//To change body of generated methods, choose Tools | Templates.
    }
    
    
    
    
    
    
    
}
