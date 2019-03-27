package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.Helper;
import com.mycompany.helper.PostGrabber;
import com.mycompany.helper.Poster;
import com.mycompany.helper.Vk_preferences;
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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;


/*! вывод в лог в файл
если нет токена выкидывать окно
*/

public class FXMLController implements Initializable {

    PostGrabber postGrabber;

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
    private ListView postListView;
    @FXML
    private Label label;
    @FXML
    private Button button;
    @FXML
    private Button button2;
    
    @FXML
    private TextField eH;
    @FXML
    private TextField eM;
    @FXML
    private TextField eTimeInterval;
    @FXML
    DatePicker datePick;
    
    
    @FXML
    private void handle_itemUsers(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AddUser_id.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setTitle("AddUser");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
        
    }
    @FXML
    private void handle_itemAuth(ActionEvent event) {
        // если в настройках есть УЗ и в реестре храним user_id то открыть
        // если нет- окно добавления пользователя
        Boolean flag=true;
        if(!flag){
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
        }else{
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
        
    }
    @FXML
    private void handle_itemProviders(ActionEvent event) {

        try {
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/Provider.fxml"));
            Scene scene = new Scene(root);
            scene.getStylesheets().add("/styles/Styles.css");
            Stage stage = new Stage();
            stage.setTitle("AddProviders");
            stage.setScene(scene);
            stage.show();
        } catch (IOException ex) {
            Logger.getLogger(FXMLController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    @FXML
    private void onSettingsAction(ActionEvent event) {

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
    private void handle_itemPreferences(ActionEvent event) {

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
    private void handleButtonAction(ActionEvent event) {

        List<ConstructorPost> list = new ArrayList<ConstructorPost>();
        Vk_preferences pref = new Vk_preferences();
        int vk_id=Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
        for (ConstructorPost elt : postGrabber.listPost) {
            if (elt.flag) {
                list.add(elt);
               // new Helper().saveFile(elt.listPhoto.get(0));
               //System.err.println(elt.listPhoto);
            }
        }
        Poster poster = new Poster(list,getDateTimeinEdit(),vk_id);
        poster.start();
        System.err.println("rrr "+vk_id);
       
        
       
    }
    @FXML
    private void handleButtonAction2(ActionEvent event) {
        Vk_preferences pref = new Vk_preferences();
        int vk_id=Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
        List<ConstructorProvider> listProvDB=new DbHandler().providerDB(vk_id);
        List<Integer> providerList = new ArrayList<>();
        for(ConstructorProvider elt:listProvDB){
            if(elt.flag){
                 providerList.add(elt.id);
            }
       
        }
     
        postGrabber = new PostGrabber(providerList, postListView);
        postGrabber.start();

        /*
         Vk_api vk_api=new Vk_api();
         vk_api.getwalls(vk_api.getActor(Integer.parseInt(
                 new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),  
                 new Vk_preferences().getPref(Vk_preferences.TOKEN)),
                 529989036
               );
         */
    }

    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        /*при инициализации если в не выбрана учетка пользователя
        не давать открыть handle_itemAuth 
        если есть то  в prividerList и auth-выставляем данные по пользователю из параметров
        все запросы к vkApi делать через выбраного пользоватея
        */
       
        setDateTime();
        List<String> aaa = new ArrayList<String>();
        aaa.add("s");
        List<ConstructorPost> constructorPosts = new ArrayList<>();
        constructorPosts.add(new ConstructorPost(1, 1, new Long(11), 1, 1, "555", 1, aaa, false));

        final ObservableList<ConstructorPost> observableList = FXCollections.observableArrayList();
        observableList.setAll(constructorPosts);
        
        postListView.setItems(observableList);
        postListView.setCellFactory(new Callback<ListView<ConstructorPost>, ListCell<ConstructorPost>>() {
            @Override
            public ListCell<ConstructorPost> call(ListView<ConstructorPost> param) {
                return new ListViewCell();
            }

        });
        new DbHandler().CreateDB();
        /*
        final MultipleSelectionModel<ConstructorPost> selectionModel=postListView.getSelectionModel();
        
        selectionModel.selectedItemProperty().addListener(new ChangeListener<ConstructorPost>(){
             @Override
             public void changed(ObservableValue<? extends ConstructorPost> observable, ConstructorPost oldValue, ConstructorPost newValue) {
                  int item= selectionModel.getSelectedIndex();
                 System.err.println("item"+newValue.postId+" index "+item);
             
             }
         });
        
         */
    }
    
    public void setDateTime(){
        
         Helper nowDate=new Helper();
         String date[]=nowDate.convertTime(nowDate.unixTime()).split(" ");
         String hours=date[1].split(":")[0];
         String min=date[1].split(":")[1];
      
        datePick.setValue(LocalDate.parse(date[0], DateTimeFormatter.ISO_LOCAL_DATE));
        eH.setText(hours);
        eM.setText(min);
   
        Vk_preferences pref = new Vk_preferences();
        final int vk_id=Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
        final DbHandler db=new DbHandler();
        eTimeInterval.setText(""+db.settingsList("TimeInterval",vk_id));
        eTimeInterval.textProperty().addListener(new ChangeListener<String>(){
             @Override
             public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                 db.insertSettings("TimeInterval",eTimeInterval.getText(),vk_id);   
             }
         });
    }
    
    public Long getDateTimeinEdit(){
       // "yyyy-MM-dd HH:mm"
       LocalDate localDate=datePick.getValue();
       String hh=eH.getText();
       String mm=eM.getText();
       String strDateTime= localDate+" "+hh+":"+mm;
       
       
        
      
       System.err.println("local d "+strDateTime);
    
    return  new Helper().convertStrTimeToLong(strDateTime);
    }

    public void setList(List<String> aaa) {
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.setAll(aaa);
        postListView.setItems(observableList);
    }
    
    public class CheckedListViewCheckObserver<T> extends SimpleObjectProperty<Pair<T, Boolean>> {

    }
}
