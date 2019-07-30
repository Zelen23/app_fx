/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.Helper;
import com.mycompany.helper.Vk_api;
import com.mycompany.helper.Vk_preferences;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

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

    Vk_api api = new Vk_api();
    Vk_preferences pref = new Vk_preferences();

    GetResponse resp;
    Integer count;
    Integer postID;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

// id предпоследней записи       
        resp = api.getwalls(
                Integer.parseInt(pref.getPref(pref.VK_USER_ID)),
                // офсетом двигать
                // 419021587,
                10,
                1);

        count = resp.getCount();

        l_count.setText("Count: " + count + "max_id" + resp.getItems().get(2).getId());

//slider    
        sliderClean.setMax(count);
        sliderClean.setMin(200);
        sliderClean.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {

                // респонс с 100 постов
                resp = api.getwalls(
                        Integer.parseInt(pref.getPref(pref.VK_USER_ID)),
                        // офсетом двигать
                        // 419021587,
                        2,
                        newValue.intValue());
                postID = resp.getItems().get(0).getId();

                l_info.setText(
                        "post_id" + postID+ "\r\n"
                        + new Helper().convertTime(
                                resp.getItems().get(0).getDate().longValue()) + "\r\n"
                        + resp.getItems().get(0).getText()
                );

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
        String post_id = pref.getPref(pref.VK_USER_ID) + "_" + postID;

        List l_post = new ArrayList<String>();
        l_post.add(post_id);
        List< WallPostFull> postDetails = api.getwallbyId(l_post);

//alert
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Date Post: " + new Helper().convertTime(postDetails.get(0).getDate().longValue()));
        alert.setHeaderText("text" + postDetails.get(0).getText());

        ButtonType delete = new ButtonType("delete");
        ButtonType no = new ButtonType("back");

        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(no, delete);

        Optional<ButtonType> option = alert.showAndWait();

        switch (option.get().getText()) {
            case "delete":
                deletePost(postID);

                break;
            case "back":
               /* System.out.println(
                        "delete from " + new Helper().convertTime(resp.getItems().get(0).getDate().longValue())
                        + "\n to^ " + new Helper().convertTime(resp.getItems().get(postList.size() - 1).getDate().longValue())
                );
                */

                break;
        }

    }

    public void deletePost(final Integer post) {

        /*count 2900
         ofset 100*/
        System.out.println(
                "count: " + count
                + "minPost " + post);
        Thread myThready;
        myThready = new Thread(new Runnable() {
            public void run() //Этот метод будет выполняться в побочном потоке
            {

                /* пройдусь по всем постамм пачками по 100,
                когда дойду до последнего и отниму 100(взможно будет отрицательное)
                верну эту сотку назад и не пойду на следущий цикл
                
                удаляю пачки  пока не найду пачку где есть искомый пост
                 */
                while (count > 0) {
                    List<Integer> postList = new ArrayList<>();
                    count = count - 100;

                    resp = api.getwalls(
                            Integer.parseInt(pref.getPref(pref.VK_USER_ID)),
                            100,
                            count > 0 ? count : count + 100);
                    for (WallPostFull elt : resp.getItems()) {
                        postList.add(elt.getId());
                         
                    }

                    /*удалять есе подряд если нашли индекс то до него*/
                    int index;
                    index = postList.indexOf(post);
                    if (index == -1) {

                        for (Integer obj : postList) {
                            System.out.println("delete " + obj);
                        }

                    } else {

                        //удаляем до 14 и выскаиваем
                        System.out.println("fucken " + index);

                        //вывел 100ку 150 был 3 по счету- удалил 1-2-3
                        /*общее кол-во*/
                        for (int i = postList.size()-1; i > index; i--) {
                            count = resp.getCount();
                            System.out.println("delete last " + postList.get(i));

                        }
                        break;
                    }

                }

            }

        });
        myThready.start();

    }

}
