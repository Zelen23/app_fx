/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.Vk_preferences;
import static com.mycompany.helper.Vk_preferences.CLIENT_ID;
import static com.mycompany.helper.Vk_preferences.SECRET_KEY;
import static com.mycompany.helper.Vk_preferences.SERVICES_KEY;
import static com.mycompany.helper.Vk_preferences.TOKEN;
import static com.mycompany.helper.Vk_preferences.VK_USER_ID;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

//UH6sYetv5n
/**
 * FXML Controller class
 *
 * @author adolf
 */
public class PreferencesController implements Initializable {

    /**
     * Initializes the controller class.
     * терерт настройки плотно завязаны с бд
     * явно созранять токен  и пользователя не вариант
     * 
     * разбить на 2е части отображаем токен, пользователя и остаток жизни
     * и то что мы можем записать в реестр и сохранить в бд
     */
    
    Vk_preferences pref=new Vk_preferences();
    
    @FXML
    TextField edit_token;
    @FXML
    TextField edit_client_id;
    @FXML
    TextField edit_sevices_key;
    @FXML
    TextField edit_secret_key;
    @FXML
    TextField edit_vk_id;
    
    @FXML
    Button b_saveProperties;
    
         @FXML
    private void handleSavePrefButton(ActionEvent event) {
       // get a handle to the stage

    pref.putPref(TOKEN, edit_token.getText());
    pref.putPref(VK_USER_ID,edit_vk_id.getText());
    
    pref.putPref(SERVICES_KEY,edit_sevices_key.getText());
    pref.putPref(SECRET_KEY,edit_secret_key.getText());
    pref.putPref(CLIENT_ID,edit_client_id.getText());
    
    Stage stage = (Stage) b_saveProperties.getScene().getWindow();
    // do what you have to do
    stage.close();
    }
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               
        org.slf4j.Logger logger = LoggerFactory.getLogger(PreferencesController.class);
        logger.info("test");
        
        edit_token.setText(pref.getPref(TOKEN));
        edit_vk_id.setText(pref.getPref(VK_USER_ID));
        edit_sevices_key.setText(pref.getPref(SERVICES_KEY));
        edit_secret_key.setText(pref.getPref(SECRET_KEY));
        edit_client_id.setText(pref.getPref(CLIENT_ID));
    }    
    
}
