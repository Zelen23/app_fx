/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.mycompany.app_fx.FXMLController;
import com.vk.api.sdk.client.actors.UserActor;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import org.apache.commons.collections4.list.AbstractLinkedList;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adolf
 */
public class Poster extends Thread{
    
    List<ConstructorPost> listPost;
    Long times;
    int vk_id;
    Label l_status =new Label();

    public Poster(List<ConstructorPost> listPost,Long times, int vk_id, Label l_status) {
        this.listPost = listPost;
        this.times=times;
        this.vk_id=vk_id;
        this.l_status=l_status;
    }
    

    Helper helper=new Helper();
    Vk_api vk_api = new Vk_api(l_status);
    /*   
    Vk_api vk_api = new Vk_api();
    UserActor userActor = vk_api.getActor(Integer.parseInt(
        new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
        new Vk_preferences().getPref(Vk_preferences.TOKEN));
    */
     org.slf4j.Logger logger = LoggerFactory.getLogger(Poster.class);

    @Override
    public void run() {
        //дата время
        //интервал
        //цикл в коллиество строк на отправку
 
    Integer TimeInterval=new DbHandler().settingsList("TimeInterval", vk_id);
    int i=1;  
    
    for(ConstructorPost elt:listPost){    
        i=i+TimeInterval;
         new FXMLController(). setMess("set Post "+elt.provId+" "+elt.postId,l_status);
       // Long time= helper.timeadd(helper.unixTime(), i);
        Long time= helper.timeadd(times, i);
        logger.info("Send post to my wall from: "+elt.provId+"_"+elt.postId);
        new Vk_api(l_status).setPost(
                elt.text,
                time,
                vk_api.addPhoto(elt.listPhoto, elt.text),
                vk_api.getActor().getId()
                //в метод передаю массив ссылок на фотку 
        );
              
    }
     
    

//To change body of generated methods, choose Tools | Templates.
    }
    

    
    
    
    
    
}
