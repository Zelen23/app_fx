/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;

/**
 *
 * @author adolf
 */
public class PostLinkGetter extends Thread {

   
    ListView postListView;
    List<String> postList = new ArrayList<String>();
    Label l_status =new Label();
    ProgressBar progresBar=new ProgressBar(postList.size());
   

    public PostLinkGetter( List<String> postList, ListView postListView,Label l_status,ProgressBar progresBar) {
        this.postList = postList;
        this.postListView = postListView;
        this.l_status =l_status;
        this.progresBar=progresBar;
       
        
    }
 /*
    Vk_api vk_api = new Vk_api();
    UserActor userActor = vk_api.getActor(Integer.parseInt(
            new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
            new Vk_preferences().getPref(Vk_preferences.TOKEN));
*/
    @Override
    public void run() {

        List<WallPostFull> wallItem = new ArrayList<WallPostFull>();
        wallItem = new Vk_api().getwallbyId( postList);
        
        PostGrabber postGrab=new PostGrabber(null, postListView);
        postGrab.filterX(wallItem);
       // postGrab.progessStatus(1,"PostLinkGetter");
        

    }

}
