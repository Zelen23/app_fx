package com.mycompany.app_fx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.Vk_preferences;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author adolf
 * тк провайдер контроллер достаточно универсальный
 * юзаю его для всех сущностией относящихся к vk пользователей
 * в зависимости от типа сущности разная рекация на чек бокс
 * 
 * если меняю пользователя пишу его настройки в реестр
 * vk_id
 * токен
 * 
 */
public class Cell_listAddProviderController  {

    /**
     * Initializes the controller class.
     */
    
    @FXML
    HBox hBoxAddProv;
    
    @FXML
    CheckBox flag_prov;        
    @FXML
    Label provName;     
    @FXML         
    TextField provPlase; 
    @FXML         
    TextField provID;
      
      
    
    public Cell_listAddProviderController() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Cell_listAddProvider.fxml"));
            loader.setController(this);
         
            loader.load();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

 public HBox getBox()
    {
        return hBoxAddProv;
    }

    void setInfo(final ConstructorProvider item) {
         provName.setText(item.name);
         provPlase.setText(item.plase);
         provID.setText(String.valueOf(item.id));
         flag_prov.setSelected(item.flag);
         flag_prov.selectedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                    // добавить параметр в конструктор для оперделениия таблицы
                    switch(item.type){
                    case "user_vk":
                        
                        if(newValue){
                             System.err.println(item.id); 
                          new Vk_preferences().putPref(Vk_preferences.VK_USER_ID, String.valueOf(item.id));
                           // get a handle to the stage
        Stage stage = (Stage) flag_prov.getScene().getWindow();
        // do what you have to do
         stage.close();
                        }
                        // установить флаг только на одной записи-везде снять
                        // очистить хеш браузера для получения токена
                        // pref.putPref(Vk_preferences.VK_USER_ID, user_id);
                        // так же дополнить эксепшен выводом сообщенния об отстутствии токена
                        System.err.println("user_vk");
                    break;
                    case "provider":
                        new DbHandler().updflag_post(newValue,item.id);
                    break;  
                        
                   
                    }
               
                    
                }
            });

    }
    
}
