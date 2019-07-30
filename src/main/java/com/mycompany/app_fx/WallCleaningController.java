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
    TextField e_post;
    @FXML
    Label l_count;
    @FXML
    Label l_before;
    @FXML
    Label l_info;

    Vk_api api = new Vk_api();
    Vk_preferences pref = new Vk_preferences();

    GetResponse resp;
    Integer count;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        final int inc = 100;
        e_post.setText("" + inc);

        resp = api.getwalls(
                Integer.parseInt(pref.getPref(pref.VK_USER_ID)),
                // офсетом двигать
                // 419021587,
                10,
                1);
// id предпоследней записи

        count = resp.getCount();
        l_count.setText("Count: " + count+ "max_id"+resp.getItems().get(2).getId());
// сдвинули оффсет- пересчитали ресонс        
        e_post.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (oldValue != newValue && newValue.length() > 0) {
// респонс с 100 постов
                    resp = api.getwalls(
                            Integer.parseInt(pref.getPref(pref.VK_USER_ID)),
                            // офсетом двигать
                            // 419021587,
                            100,
                            Integer.parseInt(e_post.getText()));

                    l_info.setText(
                            new Helper().convertTime(
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
// вывел id 100 постов
        List<Integer> postList = new ArrayList();
        for (WallPostFull elt : resp.getItems()) {
            postList.add(elt.getId());
            System.out.println(elt.getId());
        }

        String post_id = pref.getPref(pref.VK_USER_ID) + "_" + postList.get(0);

        List l_post = new ArrayList<String>();
        l_post.add(post_id);
        List< WallPostFull> postDetails = api.getwallbyId(l_post);

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
                System.out.println(
                        "delete from " + new Helper().convertTime(resp.getItems().get(0).getDate().longValue())
                        + "\n to^ " + new Helper().convertTime(resp.getItems().get(postList.size() - 1).getDate().longValue())
                );
                break;
            case "back":
                deletePost(postList.get(postList.size() - 1), Integer.parseInt(e_post.getText()));
                break;
        }

    }

    public void searchMinPostID(List<Integer> postList) {

        int before = postList.get(postList.size() - 1);
        int ink = before;
        int iter = 0;

        do {
            ink = before / 2;
            resp = api.getwalls(
                    Integer.parseInt(pref.getPref(pref.VK_USER_ID)),
                    100,
                    ink);
            iter++;
            System.out.println("count iteration " + iter);
        } while (resp.getItems().size() == 100);

        System.out.println("size older postList " + resp.getItems().size());
        for (WallPostFull elt : resp.getItems()) {
            postList.add(elt.getId());
            System.out.println(elt.getId());
        }

    }

    public void deletePost(final Integer post, final Integer offset) {

        /*count 2900
         ofset 100*/
        System.out.println(
                "count: " + count
                + "init offset " + offset
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

                        for (int i = 0; i <= index; i++) {
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
