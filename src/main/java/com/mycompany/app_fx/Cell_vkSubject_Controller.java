package com.mycompany.app_fx;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import com.mycompany.helper.ConstructorPost;
import com.mycompany.helper.ConstructorProvider;
import com.mycompany.helper.DbHandler;
import com.mycompany.helper.GroupsProvider;
import com.mycompany.helper.Vk_preferences;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.web.WebEngine;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;
import org.controlsfx.control.IndexedCheckModel;

/**
 * FXML Controller class
 *
 * @author adolf тк провайдер контроллер достаточно универсальный юзаю его для
 * всех сущностией относящихся к vk пользователей в зависимости от типа сущности
 * разная рекация на чек бокс
 *
 * если меняю пользователя пишу его настройки в реестр vk_id токен
 *
 */
public class Cell_vkSubject_Controller {

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
    CheckComboBox provGroups;
    @FXML
    TextField provID;

    public Cell_vkSubject_Controller() {
        loadFXML();
    }

    private void loadFXML() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Cell_listAddProvider.fxml"));
            loader.setController(this);

            loader.load();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public HBox getBox() {
        return hBoxAddProv;
    }

    void setInfo(final ConstructorProvider item) {
        provName.setText(item.name);
        provID.setText(String.valueOf(item.id));
        //provPlase.setText(item.plase);
        if (item.type.equals("user_vk")) {
            provGroups.setVisible(false);
            provID.setText(item.plase);
            
            
            
        }
        setComBox(item.id);

        
        flag_prov.setSelected(item.flag);
        flag_prov.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                //+ добавить параметр в конструктор для оперделениия таблицы
                //- установить флаг только на одной записи-везде снять
                //+ очистить хеш браузера для получения токена
                //+ pref.putPref(Vk_preferences.VK_USER_ID, user_id);
                //- так же дополнить эксепшен выводом сообщенния об отстутствии токена
                switch (item.type) {
                    case "user_vk":
                        
                        if (newValue) {

                            new Vk_preferences().putPref(
                                    Vk_preferences.VK_USER_ID, String.valueOf(item.id));

                            new Vk_preferences().putPref(
                                    Vk_preferences.TOKEN, new DbHandler().getToken(item.id));

                            System.err.println("Cell_vkSubject_Controller " + item.id);

                            Stage stage = (Stage) flag_prov.getScene().getWindow();
                            stage.close();
                        }
                        break;

                    case "provider":
                        
                        new DbHandler().updflag_post(newValue, item.id);
                        break;

                }

            }
        });

    }

    public void setComBox(final Integer prov_id) {

        ObservableList<String> value = FXCollections.observableArrayList();
        final List<GroupsProvider> group = new DbHandler().groupList();
        List<String> groupName = new ArrayList<>();

        for (GroupsProvider elt : group) {
            groupName.add(elt.GroupName);
        }

        List<String> groupList = new ArrayList<>();
        groupList = new DbHandler().getGroupProvider(prov_id);

        value.setAll(groupName);
        provGroups.getItems().addAll(value);

        for (int i = 0; i < value.size(); i++) {

            if (groupList.contains(provGroups.getCheckModel().getItem(i))) {
                provGroups.getCheckModel().check(i);
            }

        }
        //начальная модель
        final ObservableList<Integer> init_value = provGroups.getCheckModel().getCheckedIndices();
        //добавить группы
        provGroups.getCheckModel().getCheckedItems().addListener(new ListChangeListener<String>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends String> c) {
                String id_group = "";
                ArrayList<String> id_groupList = new ArrayList<String>();
                ObservableList<Integer> value2 = provGroups.getCheckModel().getCheckedIndices();
                new DbHandler().deleteProviderToGroup(prov_id);
                for (Integer elt : value2) {

                    id_groupList.add("" + group.get(elt).id);

                    new DbHandler().addProviderToGroup(prov_id, group.get(elt).id);
                }
                id_group = String.join(",", id_groupList);
                System.out.println("setComBox " + id_group + "  provider " + prov_id);

            }
        });

    }
}
