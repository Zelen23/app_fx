package com.mycompany.app_fx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
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

/**
 * FXML Controller class
 *
 * @author adolf
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
                    
               new DbHandler().updflag_post(newValue,item.id);
                    
                }
            });

    }
    
}
