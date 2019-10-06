package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.Helper;
import com.mycompany.helper.PostGrabber;
import com.mycompany.helper.PostLinkGetter;
import com.mycompany.helper.Poster;
import com.mycompany.helper.Vk_api;
import com.mycompany.helper.Vk_preferences;
import com.vk.api.sdk.client.actors.UserActor;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.Month;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Pair;
import org.slf4j.LoggerFactory;


/*! вывод в лог в файл
если нет токена выкидывать окно

+назвать кнопки как положено
в лог вывести каждое нажатие
+initialize привести в порядок

        //529989036
        //344417917
 */
public class FXMLController implements Initializable {

    @FXML
    private MenuBar menuBar;
    @FXML
    private Menu menuFile;

    @FXML
    private ListView postListView;
    @FXML
    private Label label;

    @FXML
    private TextField eH;
    @FXML
    private TextField eM;
    @FXML
    private TextField eTimeInterval;
    @FXML
    private TextField eActualday;
    public int day=1;

    @FXML
    DatePicker datePick;

    @FXML
    ProgressBar progressBar = new ProgressBar(0);
    @FXML
    private Label l_status;

    Vk_preferences pref = new Vk_preferences();
    PostGrabber postGrabber;
    PostLinkGetter postLinkGetter;
    org.slf4j.Logger logger = LoggerFactory.getLogger(FXMLController.class);

    //====================================FILE=====================================   
    @FXML
    private MenuItem m_file_Users;
    @FXML
    private MenuItem m_file_Auth;
    @FXML
    private MenuItem m_file_ApiKey;
    @FXML
    private MenuItem m_file_Providers;

    @FXML
    private void handle_file_Users(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddUser_id.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setTitle("AddUser");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) progressBar.getScene().getWindow());
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void handle_file_Authorization(ActionEvent event) {
        // если в настройках есть УЗ и в реестре храним user_id то открыть
        // если нет- окно добавления пользователя
        Boolean flag = true;
        
        
        if (!flag) {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddUser_id.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/styles/Styles.css");
                Stage stage = new Stage();
                stage.setTitle("Get_token");
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("/fxml/AuthWebView.fxml"));
                Scene scene = new Scene(root);
                scene.getStylesheets().add("/styles/Styles.css");
                Stage stage = new Stage();
                stage.setTitle("Get_token");
                
                stage.initModality(Modality.WINDOW_MODAL);
                stage.initOwner((Stage) progressBar.getScene().getWindow());
                
                stage.setScene(scene);
                stage.show();
            } catch (IOException ex) {
                Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }

    }

    @FXML
    private void handle_file_ApiKey(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Preferences.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setTitle("Api key");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void handle_file_Providers(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Provider.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setTitle("AddProviders");
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner((Stage) progressBar.getScene().getWindow());
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

//=====================================EDIT=====================================    
    @FXML
    private void handle_edit_Settings(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Settings.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    @FXML
    private void handle_edit_WallCleaning(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/WallCleaning.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setTitle("Settings");
            stage.setScene(scene);
            stage.show();
            
            stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    System.err.println("Close");
                    if(new WallCleaningController().myThready!=null){
                 
                     System.err.println( "not null  "+new WallCleaningController().myThready.getId());
                        new WallCleaningController().myThready.stop();
                    }
                    
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

//====================================BUTTON====================================
    @FXML
    private Button bSendPOST;
    @FXML
    private Button bGetPostProviders;

    @FXML
    private void handle_Button_SendPOST(ActionEvent event) {

        List<ConstructorPost> list = new ArrayList<ConstructorPost>();
        int vk_id = Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));

        for (ConstructorPost elt : postGrabber.listPost) {
            if (elt.flag) {
                list.add(elt);

            }
        }
        Poster poster = new Poster(list, getDateTimeinEdit(), vk_id, progressBar, l_status);
        poster.start();
        logger.info("handle_Button_SendPOST to walls " + vk_id);

    }

    @FXML
    private void handle_Button_GetPostProviders(ActionEvent event) {

        int vk_id = Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
        day= Integer.parseInt(eActualday.getText());

        String groups = pref.getPref(Vk_preferences.GROUPS_PROVIDER);
        List<ConstructorProvider> listProvDBX = new DbHandler().providerDBX(vk_id, groups);
        List<Integer> providerList = new ArrayList<>();

        for (ConstructorProvider elt : listProvDBX) {

            if (elt.flag) {
                providerList.add(elt.id);
            }
        }
        postGrabber = new PostGrabber(providerList, postListView, progressBar, l_status,day);
        postGrabber.start();
        logger.info("handle_Button_GetPostProviders " + providerList);
    }

//==================================2nd-tab=====================================
    @FXML
    private Button bSendPostFromLink;

    @FXML
    private ListView listLinkPost;

    @FXML
    private TextField postLink;

    List<String> massPost = new ArrayList<>();

    @FXML
    private void handle_SendPostFromLink(ActionEvent event) {

        ObservableList<ConstructorPost> observableList = listLinkPost.getItems();

        List<ConstructorPost> list = new ArrayList<ConstructorPost>();
        int vk_id = Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));

        for (ConstructorPost elt : observableList) {
            if (elt.flag) {
                list.add(elt);
            }
        }
        Poster poster = new Poster(list, getDateTimeinEdit(), vk_id, progressBar, l_status);
        poster.start();
        logger.info("handle_SendPostFromLink " + vk_id);

    }

    public void secondTab() {
        postLink.textProperty().addListener(new ChangeListener<String>() {

            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                String parsePostItem = new Helper().wall(newValue);
                if (parsePostItem != null) {
                    massPost.add(parsePostItem);

                    postLinkGetter = new PostLinkGetter(massPost, listLinkPost, progressBar, l_status);
                    postLinkGetter.start();
                    postLink.clear();
                }

            }

        });

    }

//=====================================View=====================================    
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        /*при инициализации если в не выбрана учетка пользователя
        не давать открыть handle_itemAuth 
        если есть то  в prividerList и auth-выставляем данные по пользователю из параметров
        все запросы к vkApi делать через выбраного пользоватея
         */
      
        // если файл создан то не пытаться создавать
        
       
       File file=new File("vk_grabBase.db");
       if(file.exists()){
            new DbHandler().DBconnect();
            System.err.println("exist");
              setDateTime();
       }else{  
            new DbHandler().CreateDB();
            System.err.println("No_exist");
       }
        
        //при запуске группы по умолчанию
        pref.putPref(Vk_preferences.GROUPS_PROVIDER, "99");

        secondTab();

    }

    public void setDateTime() {

        Helper nowDate = new Helper();
        String date[] = nowDate.convertTime(nowDate.unixTime()).split(" ");
        String hours = date[1].split(":")[0];
        String min = date[1].split(":")[1];

        datePick.setValue(LocalDate.parse(date[0], DateTimeFormatter.ISO_LOCAL_DATE));
        eH.setText(hours);
        eM.setText(min);

        final int vk_id = Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
        final DbHandler db = new DbHandler();
        eTimeInterval.setText("" + db.settingsList("TimeInterval", vk_id));
        eTimeInterval.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                db.insertSettings("TimeInterval", eTimeInterval.getText(), vk_id);
            }
        });
    }

    public Long getDateTimeinEdit() {
        // "yyyy-MM-dd HH:mm"
        LocalDate localDate = datePick.getValue();
        String hh = eH.getText();
        String mm = eM.getText();
        String strDateTime = localDate + " " + hh + ":" + mm;

        return new Helper().convertStrTimeToLong(strDateTime);
    }

    public void setList(List<String> aaa) {
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.setAll(aaa);
        postListView.setItems(observableList);
    }

}
