/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.GroupsProvider;
import com.mycompany.helper.Helper;
import com.mycompany.helper.Vk_api;
import com.mycompany.helper.Vk_preferences;
import static com.mycompany.helper.Vk_preferences.SERVICES_KEY;
import com.vk.api.sdk.client.actors.UserActor;
import java.beans.ConstructorProperties;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
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
    
    public  String id_group="99";

    Vk_preferences pref = new Vk_preferences();
    int vk_id=Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
    
    
        
     @FXML
    private void ButtProvClose(ActionEvent event) {
        // get a handle to the stage
        // Stage stage = (Stage) okProviders.getScene().getWindow();
        // do what you have to do
        // stage.close();
        //https://vk.com/id411014340
        String parseID=fieldProvider.getText();
        
        if(parseID.startsWith("https://vk.com/")){
            
            parseID=parseID.replace("https://vk.com/", "");
            List<ConstructorProvider> userInfo=us_Info(parseID);
            new DbHandler().insertInProvider(
                    userInfo.get(0).id,
                    vk_id,
                    userInfo.get(0).name);
            
            setListViewProvider(id_group);
        }else{
            System.err.println("FXMLContrlollerProvider_is_no_linkUser "+parseID);
        }
 
    }
    public List<ConstructorProvider> us_Info(String parseID ){
    Vk_api vk_api=new Vk_api();
        UserActor userActor = vk_api.getActor(Integer.parseInt(
            new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
            new Vk_preferences().getPref(Vk_preferences.TOKEN));
        
        List<ConstructorProvider> userInfo=vk_api.fromUsertoProvider(
                   vk_api.getUserInfo(userActor, parseID));
           
       
    return userInfo;
    }
    
         @FXML
    private void ButtDellete(ActionEvent event) {
        // get a handle to the stage
        // Stage stage = (Stage) okProviders.getScene().getWindow();
        // do what you have to do
        // stage.close();
        new DbHandler().deleteProvider(Integer.parseInt(fieldProvider.getText()));
        setListViewProvider(id_group);
    }
    
   
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setComBox();
        setListViewProvider(id_group);
    
    }

    public void setListViewProvider(String id_group){
        
        List<ConstructorProvider>list= new DbHandler().providerDBX(vk_id,id_group);
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
    
    public void setComBox(){
    
        ObservableList <String>value=FXCollections.observableArrayList();
        final List<GroupsProvider> group =new DbHandler().groupList();
        List <String> groupName=new ArrayList<>();
        
        for(GroupsProvider elt:group ){
            groupName.add(elt.GroupName);
        }
        
        value.setAll(groupName);
        checkComboBox.getItems().addAll(value);
        checkComboBox.getCheckModel().check(0); //default

        checkComboBox.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>(){
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> c) {  
                
                ArrayList<String> id_groupList = new ArrayList<String>();
                ObservableList <Integer>value2 = checkComboBox.getCheckModel().getCheckedIndices();
                 for(Integer elt:value2 ){
                     
                    id_groupList.add(""+group.get(elt).id);
                 }
                 id_group= String.join(",", id_groupList);
                 System.out.println("setComBox "+ id_group);
                 
                 pref.putPref(Vk_preferences.GROUPS_PROVIDER,id_group);
                 setListViewProvider(id_group);
      
            }
        });
        
        
        
        
        /*слушать нажатие трушных элементов
        по индексу находить  елемент обьекта групп добавляю его к массиву*/
        
       
    
    }
    
}
