package com.mycompany.app_fx;

import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.Helper;
import com.mycompany.helper.PostGrabber;
import com.mycompany.helper.Poster;
import com.mycompany.helper.Vk_api;
import com.mycompany.helper.Vk_preferences;
import com.vk.api.sdk.client.actors.UserActor;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Pair;

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
    private void handle_itemAuth(ActionEvent event) {

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

        for (ConstructorPost elt : postGrabber.listPost) {
            if (elt.flag) {
                list.add(elt);
            }
        }
        Poster poster = new Poster(list);
        poster.start();
    }

    @FXML
    private void handleButtonAction2(ActionEvent event) {

        List<Integer> providerList = new ArrayList<>();
        providerList.add(529989036);
        providerList.add(411014340);
        providerList.add(408902013);
        providerList.add(344417917);
        providerList.add(419021587);
        providerList.add(474456246);

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

    public class CheckedListViewCheckObserver<T> extends SimpleObjectProperty<Pair<T, Boolean>> {

    }

    public void setList(List<String> aaa) {
        ObservableList observableList = FXCollections.observableArrayList();
        observableList.setAll(aaa);
        postListView.setItems(observableList);
    }
}
