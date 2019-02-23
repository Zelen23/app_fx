package com.mycompany.app_fx;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class FXMLController implements Initializable {
    

    
    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuFile;  
    @FXML
    private MenuItem m_itemAuth;
    @FXML
    private MenuItem m_itemApiKey;
     @FXML
    private MenuItem m_itemProviders;
    
    @FXML
    private void handle_itemAuth(ActionEvent event){
        
        try {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/AuthWebView.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        Stage stage = new Stage();
        stage.setTitle("Get_token");
        stage.setScene(scene);
        stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @FXML
    private void handle_itemPreferences(ActionEvent event){
        
        try {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/Preferences.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        Stage stage = new Stage();
        stage.setTitle("Pref");
        stage.setScene(scene);
        stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    
    
    
    
    @FXML
    private Label label;
    @FXML
    private Button button;
    @FXML
    private Button button2;
    @FXML
    private void handleButtonAction(ActionEvent event) {
        System.out.println("You clicked me!");
        label.setText("Hello World!");
    } 
    @FXML
    private void handleButtonAction2(ActionEvent event){
         try {
        Parent root = FXMLLoader.load(getClass().getResource("/fxml/FXML.fxml"));
        Scene scene = new Scene(root);
        scene.getStylesheets().add("/styles/Styles.css");
        Stage stage = new Stage();
        stage.setTitle("JavaFX and Maven");
        stage.setScene(scene);
        stage.show();
 
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
   
    
    
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
}
