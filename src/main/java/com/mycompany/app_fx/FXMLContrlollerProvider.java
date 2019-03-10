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
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adolf
 * 
 
 */
public class FXMLContrlollerProvider implements Initializable {

    /**
     * Initializes the controller class.
     * 
     * Добавлять ид провайдеров
     * удалять ид провайдеров
     * сохранять
     * 
     * 
     * 
     */
    
    
    @FXML
    ListView ListAddProviders;
    
    @FXML
    Button okProviders;
    
    @FXML
    TextField fieldProvider;
    
    
     @FXML
    private void ButtProvClose(ActionEvent event) {
        // get a handle to the stage
        // Stage stage = (Stage) okProviders.getScene().getWindow();
        // do what you have to do
        // stage.close();
    }
    
         @FXML
    private void ButtDellete(ActionEvent event) {
        // get a handle to the stage
        // Stage stage = (Stage) okProviders.getScene().getWindow();
        // do what you have to do
        // stage.close();
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
