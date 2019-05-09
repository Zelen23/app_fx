/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.Helper;
import com.mycompany.helper.Vk_api;
import com.mycompany.helper.Vk_preferences;
import com.vk.api.sdk.client.actors.UserActor;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adolf
 * 
 * текущие настройи хранить в реестре
Добавляем пользоваетеля сохраняем его в реестре и базе

 
 */
public class FXMLControllerAuth implements Initializable {

    /**
     * Initializes the controller class.
     */
    Vk_preferences pref = new Vk_preferences();
    Helper helper = new Helper();


    @FXML
    private WebView webView;

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
 
        String vk_url = "https://oauth.vk.com/authorize?client_id=" + pref.getPref(Vk_preferences.CLIENT_ID)
                + "&display=page&redirect_uri=https://oauth.vk.com/blank.html"
                + "&scope=wall,photos&response_type=token&v=5.52";

        WebEngine webEngine = webView.getEngine();
        // Delete cache for navigate back
        webEngine.load("about:blank");
        // Delete cookies 
        java.net.CookieHandler.setDefault(new java.net.CookieManager());
        webEngine.load(vk_url);
        
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (helper.parseUrl(newValue).size() > 1) {

                    String token = helper.parseUrl(newValue).get(0);
                    pref.putPref(Vk_preferences.TOKEN, token);
                    
                    String user_id = helper.parseUrl(newValue).get(2);
                    pref.putPref(Vk_preferences.VK_USER_ID, user_id);
                    
                    String time = helper.parseUrl(newValue).get(1);
                    // записали в базу токен и Ид пользователя
                    Vk_api vk_api=new Vk_api();
                    /*
                    
                    UserActor userActor = vk_api.getActor(Integer.parseInt(
                    new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
                    new Vk_preferences().getPref(Vk_preferences.TOKEN));
                    */
                    List<ConstructorProvider> userInfo=vk_api.fromUsertoProvider(
                        vk_api.getUserInfo(user_id));
                    
                    new DbHandler().insUser(
                            Integer.valueOf(user_id),
                            token,
                            userInfo.get(0).name,
                            String.valueOf(new Helper().unixTime()));
                    
                   

                    Stage stage = (Stage) webView.getScene().getWindow();
                    stage.close();

                }

            }
        });

    }

}
