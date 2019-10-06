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
    ProgressBar progressBar = new ProgressBar();
    Label status = new Label();

    public PostLinkGetter(List<String> postList, ListView postListView, ProgressBar progressBar, Label status) {
        this.postList = postList;
        this.postListView = postListView;
        this.progressBar = progressBar;
        this.status = status;
    }

    @Override
    public void run() {

        List<WallPostFull> wallItem = new ArrayList<WallPostFull>();
        wallItem = new Vk_api().getwallbyId(postList);

        PostGrabber postGrab = new PostGrabber(null, postListView, null, null,100);
        postGrab.filterX(wallItem);
    }

}
