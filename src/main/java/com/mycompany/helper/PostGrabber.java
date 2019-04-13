/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.mycompany.app_fx.Cell_listPostController;
import com.mycompany.app_fx.FXMLController;
import com.mycompany.app_fx.ListViewCell;
import com.mycompany.app_fx.PreferencesController;
import com.mycompany.app_fx.TaskCellFactory;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.base.Geo;
import com.vk.api.sdk.objects.base.LikesInfo;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.PostSource;
import com.vk.api.sdk.objects.wall.PostType;
import com.vk.api.sdk.objects.wall.Views;
import com.vk.api.sdk.objects.wall.WallPost;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adolf UH6sYetv5n
 * Прикрутить возможность фильтрации
 * Прикрутить логи
 */
public class PostGrabber extends Thread {

    private List<Integer> providerList = new ArrayList<Integer>();
    ListView postListView = new ListView();

    Vk_api vk_api = new Vk_api();
    UserActor userActor = vk_api.getActor(Integer.parseInt(
            new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
            new Vk_preferences().getPref(Vk_preferences.TOKEN));
    List<GetResponse> massiveGetResponses = new ArrayList<GetResponse>();
    public List<ConstructorPost> listPost = new ArrayList<ConstructorPost>();

    public PostGrabber(List<Integer> providerList, ListView postListView) {
        this.providerList = providerList;
        this.postListView = postListView;
    }
    //529989036
    Vk_preferences pref = new Vk_preferences();
    Integer NumbersOfPosts = new DbHandler().settingsList("NumbersOfPosts",
            Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID)));

    org.slf4j.Logger logger = LoggerFactory.getLogger(PostGrabber.class);
       
    /*цикл в час в этом цикле каждые 10мин перебираю*/
    @Override
    public void run() {

       // while (true) {
            try {
                for (int i = 0; i < providerList.size(); i++) {

                    filterDataInPost(vk_api.getwalls(userActor, providerList.get(i), NumbersOfPosts, 0));
                    sleep(100);
                }
                

            } catch (InterruptedException ex) {
                Logger.getLogger(PostGrabber.class.getName()).log(Level.SEVERE, null, ex);
            }

      //  }

    }

    public void filterDataInPost(GetResponse getwalls) {

        //System.out.println("getwalls.getItems().size() " + getwalls.getItems().size());
        ConstructorPost post = null;
        Integer provId = null;
        Integer postId = null;
        Long postdate = null;
        Integer postViews = 0;
        Integer postLikes = 0;
        String text = "";
        Integer count_itemsAttach = null;
        //Лист с постами от поставщика
        for (int i = 0; i < getwalls.getItems().size(); i++) {

            WallPostFull itemPost = getwalls.getItems().get(i);
            List<WallpostAttachment> attachments = itemPost.getAttachments();
            Integer isPinned = itemPost.getIsPinned();
        //если данные удовлетворяют могу добавить в listPost
            if (isPinned == null && attachments != null) {
                
                logger.info("1st_step:filterDataInPost " + itemPost.getOwnerId() + "_" + itemPost.getId());
        //вложения в одном посте            
                count_itemsAttach = itemPost.getAttachments().size();
                List<String> listPhoto = new ArrayList<String>();

                for (int j = 0; j < count_itemsAttach; j++) {

                    Photo isPhoto = itemPost.getAttachments().get(j).getPhoto();
                    Views views = itemPost.getViews();
                    LikesInfo likesInfo = itemPost.getLikes();
        //если вложение это фотка то забираем этот пост                
                    if (isPhoto != null) {

                        if (views != null) {
                            postViews = views.getCount();
                        };
                        if (likesInfo != null) {
                            postLikes = likesInfo.getCount();
                        };
                        provId = itemPost.getOwnerId();
                        postId = itemPost.getId();
                        postdate = itemPost.getDate().longValue();
                        text = itemPost.getText();

                        listPhoto.add(gettingPhoto(isPhoto));
                    }
                }
        //проверяю входит ди запись в массив записей 
        //херачит в несколько проходов
                addtoListPost(new ConstructorPost(provId, postId, postdate, postViews, postLikes, text, count_itemsAttach, listPhoto, false));

            }

        }

    }
    
    // тут можно проводить фильтрацию
    void addtoListPost(ConstructorPost addtoWallsList) {
        if (addtoWallsList != null) {
            Boolean isExist = false;
            if (listPost.isEmpty()) {
                listPost.add(addtoWallsList);
                viewInListView(listPost);
            } else {
                for (int i = 0; i < listPost.size(); i++) {

                    if (listPost.get(i).postdate.equals(addtoWallsList.postdate)
                            && listPost.get(i).postId.equals(addtoWallsList.postId)
                            && listPost.get(i).provId.equals(addtoWallsList.provId)) {
                        isExist = true;
                    }

                }
                if (isExist == false) {
                    System.out.println();
                    logger.info("3rd step: addtoListPost" + addtoWallsList.provId + "_" + addtoWallsList.postId);
                    listPost.add(addtoWallsList);
                    viewInListView(listPost);

                } else {
                  
                }

            }
         
        }

    }

    void viewInListView(List<ConstructorPost> listPost) {

        final ObservableList<ConstructorPost> observableList = FXCollections.observableArrayList();

        observableList.setAll(listPost);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                postListView.setItems(observableList);
                postListView.setCellFactory(new Callback<ListView<ConstructorPost>, ListCell<ConstructorPost>>() {
                    @Override
                    public ListCell<ConstructorPost> call(ListView<ConstructorPost> param) {
                        return new ListViewCell();
                    }

                });

            }
        });

    }
    
    public String gettingPhoto(Photo photo) {

        String returnPhoto = "";
        if (photo.getPhoto2560() != null) {

            returnPhoto = photo.getPhoto2560();
        } else {
            if (photo.getPhoto1280() != null) {

                returnPhoto = photo.getPhoto1280();
            } else {

                if (photo.getPhoto807() != null) {

                    returnPhoto = photo.getPhoto807();
                } else {
                    if (photo.getPhoto604() != null) {

                        returnPhoto = photo.getPhoto604();
                    } else {
                        if (photo.getPhoto130() != null) {

                            returnPhoto = photo.getPhoto130();
                        }

                    }
                }
            }

        }
        logger.info("    2nd step: gettingPhoto" + returnPhoto + " " + photo.getOwnerId() + "_" + photo.getId());
        return returnPhoto;

    }

}
