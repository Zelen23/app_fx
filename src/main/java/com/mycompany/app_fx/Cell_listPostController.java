/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.Helper;
import com.mycompany.helper.PostGrabber;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.HBox;
import javafx.scene.text.Text;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class Cell_listPostController {

    @FXML
    TextArea text;

    @FXML
    TextField provideText;

    @FXML
    WebView postPicture;

    @FXML
    HBox hBox;

    @FXML
    Label l_likes;
    @FXML
    Label l_viewers;
    @FXML
    Label l_date;

    @FXML
    CheckBox check_toPost;

    Helper help = new Helper();
    double i = 0;
    int j = 0;

    /**
     * Initializes the controller class.
     */

    public Cell_listPostController() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Cell_listPost.fxml"));
            loader.setController(this);

            loader.load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setInfo(final ConstructorPost string) {
        text.setText(string.text);
        provideText.setText(string.provId.toString());
        l_likes.setText("likes: " + string.postLikes);
        l_viewers.setText("views: : " + string.postViews);
        l_date.setText(help.convertTime(string.postdate));
        check_toPost.setSelected(string.flag);
        final WebEngine webEngine = postPicture.getEngine();
        webEngine.load(string.listPhoto.get(0));
        final int size = string.listPhoto.size();

        postPicture.setOnScroll(new EventHandler<ScrollEvent>() {
            @Override
            public void handle(ScrollEvent event) {

                if (i < i + event.getDeltaY()) {
                    if (j > 0) {
                        j--;
                        webEngine.load(string.listPhoto.get(j));
                    }

                } else {
                    if (j < size - 1) {
                        j++;
                        webEngine.load(string.listPhoto.get(j));
                    }
                }

                i = i + event.getDeltaY();
            }
        });

        check_toPost.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                string.setSelected(newValue);

                string.setText(text.getText());
                /* тут же вносить именения в текст
                        (поправить сравняшку в постграббере и внести изменения в конструктор)*/

            }
        });
    }

    public HBox getBox() {
        return hBox;
    }

}
