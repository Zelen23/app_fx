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
import java.security.cert.PKIXRevocationChecker.Option;
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
        l_count.setText("Count: " + resp.getItems().get(0).getId());
        
        e_post.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (oldValue != newValue && newValue.length()>0) {

                    resp = api.getwalls(
                            Integer.parseInt(pref.getPref(pref.VK_USER_ID)),
                            // офсетом двигать
                            // 419021587,
                            10000,
                            Integer.parseInt(e_post.getText()));
                    
                    l_info.setText(
                            new Helper().convertTime(
                                    resp.getItems().get(0).getDate().longValue())+"\r\n"+
                                    resp.getItems().get(0).getText()
                             );
                }
               
            }
        });

    }

    @FXML
    private void handler_CleanWall(ActionEvent event) {

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
                        "delete from "+new Helper().convertTime(resp.getItems().get(0).getDate().longValue())+
                        "\n to^ "+new Helper().convertTime(resp.getItems().get(postList.size()-1).getDate().longValue())    
                );
                break;
            case "back":
                System.out.println("BBBBBBBBBB");
                break;
        }

    }

}
