/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.DbHandler;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class SettingsController implements Initializable {
    @FXML
    TextField editTimeInterval;
    @FXML
    TextField editNumOfPosts;
    @FXML
    TextField editUser_id;
    @FXML
    Button b_saveSettings;
    
    DbHandler db=new DbHandler();
    
    @FXML
     private void handler_saveSettings(ActionEvent event) {
     
     db.insertSettings("TimeInterval",editTimeInterval.getText());    
     db.insertSettings("NumbersOfPosts",editNumOfPosts.getText());
     db.insertSettings("User_id",editUser_id.getText());       
     Stage stage = (Stage) b_saveSettings.getScene().getWindow();
     stage.close();
     }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        editTimeInterval.setText(db.settingsList("TimeInterval"));
        editNumOfPosts.setText(db.settingsList("NumbersOfPosts"));
        editUser_id.setText(db.settingsList("User_id"));
       
    }    
    
}
