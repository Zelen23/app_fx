/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.vk.api.sdk.client.actors.UserActor;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adolf
 */
public class PostGrabber extends Thread{
 
private  List<Integer> providerList=new ArrayList<Integer>();

Vk_api vk_api=new Vk_api();
UserActor userActor=vk_api.getActor(Integer.parseInt(
                 new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),  
                 new Vk_preferences().getPref(Vk_preferences.TOKEN));


    public PostGrabber(List<Integer> providerList) {
        this.providerList=providerList;
    }
//529989036
    /*цикл в час в этом цикле каждые 10мин перебираю*/
    @Override
    public void run() {
        
        while (true) {            
             try {
                for(int i=0;i<providerList.size();i++){ 
           vk_api.getwalls(userActor,providerList.get(i));
                }
               
               sleep(10000);
           } catch (InterruptedException ex) {
               Logger.getLogger(PostGrabber.class.getName()).log(Level.SEVERE, null, ex);
           }
       
        }
       
          
       
    }

    


    
    
    
}
