/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.Vk_preferences;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

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
    
     Vk_preferences pref = new Vk_preferences();
        int vk_id=Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
     @FXML
    private void ButtProvClose(ActionEvent event) {
        // get a handle to the stage
        // Stage stage = (Stage) okProviders.getScene().getWindow();
        // do what you have to do
        // stage.close();
       
        new DbHandler().insertInProvider(Integer.parseInt(fieldProvider.getText()),vk_id);
        List<ConstructorProvider>list= new DbHandler().providerDB(vk_id);
        setListViewProvider(list);
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
       
        List<ConstructorProvider>list= new DbHandler().providerDB(vk_id);
        setListViewProvider(list);
       
    }

    public void setListViewProvider( List<ConstructorProvider>list){
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
