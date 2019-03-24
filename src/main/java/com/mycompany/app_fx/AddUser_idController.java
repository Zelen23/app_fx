/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.Helper;
import java.net.URL;
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
 * @author azelinsky
 */
public class AddUser_idController implements Initializable {
    
     
    @FXML
    ListView ListAdduser;
    
    @FXML
    Button AddUser;
    
    @FXML
    Button deleteUser;
    
    @FXML
    TextField fieldUser;
    
    DbHandler dbHandler=new DbHandler();
    Helper helper=new Helper();
    
    //потом сюда привесить слушателя селекта который в настройки(settings) будет гнать user_id 
     @FXML
    private void ButtAddUser(ActionEvent event) {
        // get a handle to the stage
       // Stage stage = (Stage) AddUser.getScene().getWindow();
        // do what you have to do
       // stage.close();
       dbHandler.insUser(Integer.valueOf(fieldUser.getText()), "",String.valueOf(helper.unixTime()));
       List<ConstructorProvider>list= dbHandler.userDB();
       setListViewProvider(list);
        
       
    }
    
         @FXML
    private void ButtDelleteUser(ActionEvent event) {
        // get a handle to the stage
        // Stage stage = (Stage) okProviders.getScene().getWindow();
        // do what you have to do
        // stage.close();
      
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        setListViewProvider(dbHandler.userDB());
       
    }    
    
    
    public void setListViewProvider( List<ConstructorProvider>list){
        final ObservableList<ConstructorProvider> observableList = FXCollections.observableArrayList();
        observableList.setAll(list);
        ListAdduser.setItems(observableList);
        ListAdduser.setCellFactory(new Callback<ListView<ConstructorProvider>,ListCell<ConstructorProvider>>(){
            @Override
            public ListCell<ConstructorProvider> call(ListView<ConstructorProvider> param) {
                return new ListProvCell();
            }
        });
}    
}
