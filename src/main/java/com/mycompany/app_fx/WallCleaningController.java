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
     
    Vk_api api=new Vk_api();
    Vk_preferences pref = new Vk_preferences();
    
    GetResponse resp;
     
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
       final int inc=3000;
       e_post.setText(""+inc);
        
        Runnable task =new Runnable(){
            @Override
            public void run() {
             resp=  api.getwalls(
             // Integer.parseInt( pref.getPref(pref.VK_USER_ID))
                // офсетом двигать
              419021587,
              10000, 
              Integer.parseInt(e_post.getText()));
            }
        };
        Thread thread = new Thread(task);
        thread.start();
        
        
        
     

       
        
        
    }   
    
     @FXML
    private void handler_CleanWall(ActionEvent event){
        
        List<Integer> postList=new ArrayList(); 
       for (WallPostFull elt:resp.getItems()){
       postList.add(elt.getId());
       System.out.println(elt.getId());
       }
        
        String post_id=419021587+"_"+postList.get(0);
        
        List l_post=new ArrayList<String>();
        l_post.add(post_id);
        List< WallPostFull> postDetails=api.getwallbyId(l_post);
        
        Alert alert =new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Date Post: "+new Helper().convertTime(postDetails.get(0).getDate().longValue()));
        alert.setHeaderText("text"+postDetails.get(0).getText());
        
        ButtonType delete=new ButtonType("delete");
        ButtonType no=new ButtonType("back");
        
        alert.getButtonTypes().clear();
        alert.getButtonTypes().addAll(no,delete);
        
        Optional<ButtonType> option =alert.showAndWait();
        
        switch(option.get().getText()){
            case "delete":
                
       
                
            break;
             case "back":
                 System.out.println("BBBBBBBBBB");
            break;
        }
        
 
        
                        
   }
        
        
    
    
}
