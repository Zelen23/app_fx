/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.Helper;
import com.mycompany.helper.Vk_preferences;
import java.net.URL;
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
 */
public class FXMLControllerAuth implements Initializable {
    
      /**
     * Initializes the controller class.
     */
    
    Vk_preferences pref=new Vk_preferences();
    Helper helper=new Helper();
    
     @FXML
    private Button close;
     
     @FXML
    private WebView webView;
       
     @FXML
    private void handleCloseButton(ActionEvent event) {
       // get a handle to the stage
    Stage stage = (Stage) close.getScene().getWindow();
    // do what you have to do
    stage.close();
    }

    
    
  
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        String vk_url="https://oauth.vk.com/authorize?client_id="+pref.getPref(Vk_preferences.CLIENT_ID)
            +"&display=page&redirect_uri=https://oauth.vk.com/blank.html"
            + "&scope=friends&response_type=token&v=5.52";
       
        WebEngine webEngine=webView.getEngine();
        webEngine.load(vk_url);
        webView.getEngine().locationProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if(helper.parseUrl(newValue).size()>1){
                    
                    String token= helper.parseUrl(newValue).get(0);
                        pref.putPref(Vk_preferences.TOKEN, token);
                    String user_id= helper.parseUrl(newValue).get(2);
                        pref.putPref(Vk_preferences.VK_USER_ID, user_id);  
                        
                    Stage stage = (Stage) close.getScene().getWindow();
                        stage.close();
                    
                }
                
                 
               
            }
        });
        
      
        
    }    
    
}
