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
       
        WebEngine webEngine=webView.getEngine();
        webEngine.load("https://vk.com/ngokson"); 
        System.err.println("4444");
    }    
    
}
