/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;

/**
 * FXML Controller class
 *
 * @author adolf
 */
public class PreferencesController implements Initializable {

    /**
     * Initializes the controller class.
     */
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

        
        
    Stage stage = (Stage) b_saveProperties.getScene().getWindow();
    // do what you have to do
    stage.close();
    }
            
    @Override
    public void initialize(URL url, ResourceBundle rb) {
               
        org.slf4j.Logger logger = LoggerFactory.getLogger(PreferencesController.class);
        logger.info("test");
        System.err.println("rrrr");
        
      
        // TODO
    }    
    
}
