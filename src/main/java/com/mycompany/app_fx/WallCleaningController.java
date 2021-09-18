/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPhotoPost;
import com.mycompany.helper.Helper;
import com.mycompany.helper.Vk_api;
import com.mycompany.helper.Vk_preferences;
import com.vk.api.sdk.objects.wall.Wallpost;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import static javafx.scene.input.KeyCode.T;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

/**
 * FXML Controller class
 *
 * @author adolf
 */

/*Посчитать кол-во постов на стене (>12000)
  выбрать крайний пост (2000)
  По нажатию на кнопу удалить-алерт, который покажет последний пост
  и его дату и сообщение "Уверены что хотите почистить стену до поста <пост>
  размещенного <дата>"

 каждая итерация удаления создает альбом arch_timestamp
 и в него ложет фотки удвленный постов
поле окончания чистки постов- удалем фльбом

Нормальная закрывашка
понятные логи
страховка в настройках с интервалом времени по достижению которого рвем чистку
при окончании орбновлять форму

 */
public class WallCleaningController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    Button b_CleanWall;
    @FXML
    Label l_count;
    @FXML
    Label l_info;
    @FXML
    Slider sliderClean;
    @FXML
    ProgressBar prb_clean;
    
    @FXML 
    private AnchorPane AnchorPaneClean;

    Vk_api api = new Vk_api();
    Vk_preferences pref = new Vk_preferences();

    GetResponse resp;
    Integer count;
    Integer postID;
    ArrayList<Integer> photoList;
    Thread myThready;
    Boolean shotdown = false;
    Integer postToDeleteCount;
    Integer vkUserID = Integer.parseInt(pref.getPref(pref.VK_USER_ID));
    Integer j = 0;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

// id предпоследней записи       
        resp = api.getwalls(vkUserID, 10, 1);
        count = resp.getCount();

        l_count.setText("Count: " + count + "max_id" + resp.getItems().get(2).getId());

//slider    
        sliderClean.setMax(count);
        sliderClean.setMin(1);
        sliderClean.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                // респонс с 100 постов
                if (newValue.intValue() < count) {

                    postToDeleteCount = count - newValue.intValue();
                    resp = api.getwalls(vkUserID, 2, newValue.intValue());
                    postID = resp.getItems().get(0).getId();
                    photoList = photoFromPost(resp.getItems().get(0));

                    l_info.setText(
                            "post_id" + postID + "\r\n"
                            + new Helper().convertTime(
                                    resp.getItems().get(0).getDate().longValue()) + "\r\n"
                            + resp.getItems().get(0).getText()
                    );
                }

            }
        }); 
        
     
        
    }


    /*по нажатию на clean нужно собрать массив со всеми id  для удаления
    убедиться что последний пост- последий
    ебнуть в 10е циклов
    найти самый старый pоst_id 
    пост до которого нужно добоаться 4585
    
     */
    @FXML
    private void handler_CleanWall(ActionEvent event) {

        //postID
        //Count
        if (postID != null && myThready == null) {

            String post_id = pref.getPref(pref.VK_USER_ID) + "_" + postID;

            List l_post = new ArrayList<String>();
            l_post.add(post_id);
            List< Wallpost> postDetails = api.getwallbyId(l_post);

//alert
            Alert alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Date Post: " + new Helper().convertTime(postDetails.get(0).getDate().longValue()));
            alert.setHeaderText("text" + postDetails.get(0).getText());

            ButtonType delete = new ButtonType("delete");
            ButtonType no = new ButtonType("back");

            alert.getButtonTypes().clear();
            alert.getButtonTypes().addAll(no, delete);

            Optional<ButtonType> option = alert.showAndWait();
//button
            switch (option.get().getText()) {
                case "delete":
                    deletePost(postID);

                    break;
                case "back":

// получил пост_id взял фотки создал альбом переместил порст удалил
                    break;
            }
        } else {
            // myThready.interrupt();
            //myThready.stop();
            shutdown();
        }

    }

    public void shutdown() {
        shotdown = true;
    }

    public void deletePost(final Integer post) {

        /*count 2900
         ofset 100*/
        final Integer albumID = api.crtAlbum(
                vkUserID,
                "arch" + new Helper().unixTime())
                .getId();

        myThready = new Thread(new Runnable() {
            public void run() //Этот метод будет выполняться в побочном потоке  
            {

                /* пройдусь по всем постамм пачками по 100,
                когда дойду до последнего и отниму 100(взможно будет отрицательное)
                верну эту сотку назад и не пойду на следущий цикл
                удаляю пачки  пока не найду пачку где есть искомый пост
                 */
                while (count > 0 && !shotdown) {

                    //храню ИД поста и его фотки
                    ArrayList<ConstructorPhotoPost> photoPostList = new ArrayList<ConstructorPhotoPost>();
                    count = count - 100;
                    resp = api.getwalls(
                            vkUserID,
                            100,
                            count > 0 ? count : count + 100);

                    for (Wallpost elt : resp.getItems()) {
                        photoPostList.add(new ConstructorPhotoPost(
                                elt.getId(),
                                photoFromPost(elt))
                        );
                    }

                    /*удалять есе подряд если нашли индекс то до него*/
                    int indexPost = -1;
                    for (int i = 0; i < photoPostList.size(); i++) {
                        if (photoPostList.get(i).post_ID.equals(post)) {
                            indexPost = i;
                            System.out.println("bingo " + indexPost);
                        }
                    }

                    if (indexPost == -1) {
                        deletePostCombine(photoPostList, 0, albumID);
                    } else {
                        deletePostCombine(photoPostList, indexPost, albumID);
                        progessStatus(progress(postToDeleteCount));
                        System.err.println("finish");
                        break;
                    }

                }

            }

        });

        myThready.start();

    }

    // из топика постов вытащил картинки   
    ArrayList<Integer> photoFromPost(Wallpost elt) {

        ArrayList<Integer> list = new ArrayList<>();
        if (elt.getAttachments() != null) {

            for (WallpostAttachment obj : elt.getAttachments()) {

                if (obj.getPhoto() != null) {
                    list.add(obj.getPhoto().getId());

                }
            }

        }
        return list;
    }

    void movePhotoToArchAlbum(Integer alb, ArrayList<Integer> phID) {

        for (Integer elt : phID) {

            if (elt != null) {
                try {
                    Thread.sleep(600);
                    api.movePhoto(vkUserID,
                            alb,
                            elt);
                } catch (InterruptedException ex) {
                    Logger.getLogger(WallCleaningController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    void deletePostCombine(ArrayList<ConstructorPhotoPost> photoPostList, Integer index, Integer albom) {

        for (int i = photoPostList.size() - 1; i >= index; i--) {
            if (shotdown) {
                break;
            }

            String successDeletePost = api.wallDelete(
                    vkUserID,
                    photoPostList.get(i).post_ID);
            /*защита: если каунт esp.getCount(); меньше чем 300 */

            if (successDeletePost == "1") {
                movePhotoToArchAlbum(albom,
                        photoPostList.get(i).photoID_inPost
                );

                j++;
                progessStatus(progress(j));

            } else {
                progessStatus(progress(postToDeleteCount));
                break;
            }

        };
    }

    Task progress(final Integer ink) {

        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                updateProgress(ink, postToDeleteCount);
                updateMessage("stopClean");

                if (ink == postToDeleteCount) {
                    updateMessage("finish");
                }
                return null;
            }

        };
        return task;
    }

    public void progessStatus(final Task task) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {

                prb_clean.progressProperty().bind(task.progressProperty());
                new Thread(task).start();
                b_CleanWall.textProperty().bind(task.messageProperty());

            }
        });

    }

}
