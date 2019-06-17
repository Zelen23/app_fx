/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import java.net.URL;
import java.security.cert.PKIXRevocationChecker.Option;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
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
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }   
    
     @FXML
    private void handler_CleanWall(ActionEvent event){
        
        Alert alert =new Alert(AlertType.CONFIRMATION);
        alert.setTitle("title");
        alert.setHeaderText("head");
        
        ButtonType delete=new ButtonType("delete");
        ButtonType no=new ButtonType("back");
        
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(no,delete);
        
        Optional<ButtonType> option =alert.showAndWait();
        
        
        switch(option.get().getText()){
            case "delete":
                System.out.println("dddddddddddd");
                
            break;
             case "back":
                 System.out.println("BBBBBBBBBB");
            break;
        }
        
 
        
                        
   }
        
        
    
    
}
