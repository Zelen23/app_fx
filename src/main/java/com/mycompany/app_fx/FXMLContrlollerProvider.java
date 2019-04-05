/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.Vk_api;
import com.mycompany.helper.Vk_preferences;
import com.vk.api.sdk.client.actors.UserActor;
import java.beans.ConstructorProperties;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.CheckComboBox;

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
     * при сохранении добавлять категории
     * у провайдера может быть несколько категорий
     * на форме экспанд-лист счекбоксами для выбора категорий поставщиков
     * у каждого поставщика так же должен быть этот экспанд 
     * при выставлении галки в бд летит запист с тегом категории
     * 
     */
        
    @FXML
    ListView ListAddProviders;
    
    @FXML
    Button okProviders;
    
    @FXML
    TextField fieldProvider;
    
    @FXML
    CheckComboBox checkComboBox; 
    
    
    
    Vk_preferences pref = new Vk_preferences();
    int vk_id=Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
    
    String[] groupsProvider={"1","2","3","4"};
    
    
   
    
        
     @FXML
    private void ButtProvClose(ActionEvent event) {
        // get a handle to the stage
        // Stage stage = (Stage) okProviders.getScene().getWindow();
        // do what you have to do
        // stage.close();
        //https://vk.com/id411014340
        Vk_api vk_api=new Vk_api();
        UserActor userActor = vk_api.getActor(Integer.parseInt(
            new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
            new Vk_preferences().getPref(Vk_preferences.TOKEN));
        
        String parseID=fieldProvider.getText();
        
        if(parseID.startsWith("https://vk.com/")){
            parseID=parseID.replace("https://vk.com/", "");
           
            List<ConstructorProvider> userInfo=vk_api.fromUsertoProvider(
                   vk_api.getUserInfo(userActor, parseID));
           
            new DbHandler().insertInProvider(userInfo.get(0).id,vk_id,userInfo.get(0).name);
            List<ConstructorProvider>list= new DbHandler().providerDB(vk_id);
            setListViewProvider(list);
        }else{
            System.err.println("FXMLContrlollerProvider_is_no_linkUser "+parseID);
        }
 
    }
    
         @FXML
    private void ButtDellete(ActionEvent event) {
        // get a handle to the stage
        // Stage stage = (Stage) okProviders.getScene().getWindow();
        // do what you have to do
        // stage.close();
        new DbHandler().deleteProvider(Integer.parseInt(fieldProvider.getText()));
         List<ConstructorProvider>list= new DbHandler().providerDB(vk_id);
         setListViewProvider(list);
    }
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       
        List<ConstructorProvider>list= new DbHandler().providerDBX(vk_id);
        setListViewProvider(list);
    
    }

    public void setListViewProvider( List<ConstructorProvider>list){
        
        ObservableList <String>value=FXCollections.observableArrayList();
        value.setAll(groupsProvider);
        checkComboBox.getItems().addAll(groupsProvider);
        
        
        final ObservableList<ConstructorProvider> observableList = FXCollections.observableArrayList();
        observableList.setAll(list);
        ListAddProviders.setItems(observableList);
        ListAddProviders.setCellFactory(new Callback<ListView<ConstructorProvider>,ListCell<ConstructorProvider>>(){
            @Override
            public ListCell<ConstructorProvider> call(ListView<ConstructorProvider> param) {
                return new ListProvCell();
            }
        });
}    
    
}
