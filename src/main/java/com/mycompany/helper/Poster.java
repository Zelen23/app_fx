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
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.apache.commons.collections4.list.AbstractLinkedList;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adolf
 */
public class Poster extends Thread {

    List<ConstructorPost> listPost;
    Long times;
    int vk_id;
    ProgressBar progressBar = new ProgressBar();
    Label status = new Label();

    public Poster(List<ConstructorPost> listPost, Long times, int vk_id, ProgressBar progressBar, Label status) {
        this.listPost = listPost;
        this.times = times;
        this.vk_id = vk_id;
        this.progressBar = progressBar;
        this.status = status;
    }

    Helper helper = new Helper();
    Vk_api vk_api = new Vk_api();

    org.slf4j.Logger logger = LoggerFactory.getLogger(Poster.class);

    @Override
    public void run() {
        //дата время
        //интервал
        //цикл в коллиество строк на отправку

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {

                Integer TimeInterval = new DbHandler().settingsList("TimeInterval", vk_id);
                int i = 1;

                for (ConstructorPost elt : listPost) {
                    i = i + TimeInterval;

                    // Long time= helper.timeadd(helper.unixTime(), i);
                    Long time = helper.timeadd(times, i);
                    logger.info("Send post to my wall from: " + elt.provId + "_" + elt.postId);
                   int postMyWallid= new Vk_api().setPost(
                            elt.text,
                            time,
                            vk_api.addPhoto(elt.listPhoto, elt.text),
                            vk_api.getActor().getId()
                    //в метод передаю массив ссылок на фотку 
                    );
                    
                    new DbHandler().postingInfo(elt.provId, elt.postId,postMyWallid,vk_id);
                    
                    updateProgress(listPost.indexOf(elt) + 1, listPost.size());
                    updateMessage(elt.provId + "_" + elt.postId);
                }
                return null;
            }
        };
        progessStatus(task);
    }

    public void progessStatus(final Task task) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                progressBar.progressProperty().bind(task.progressProperty());
                status.textProperty().bind(task.messageProperty());
                new Thread(task).start();

            }
        });

    }
}
