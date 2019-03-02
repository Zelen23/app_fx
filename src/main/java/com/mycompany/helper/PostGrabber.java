/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.mycompany.app_fx.FXMLController;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.objects.base.Geo;
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
import javafx.scene.control.ListView;
import javafx.stage.Stage;

/**
 *
 * @author adolf UH6sYetv5n
 */
public class PostGrabber extends Thread {

    private List<Integer> providerList = new ArrayList<Integer>();
    ListView postListView = new ListView();

    Vk_api vk_api = new Vk_api();
    UserActor userActor = vk_api.getActor(Integer.parseInt(
            new Vk_preferences().getPref(Vk_preferences.VK_USER_ID)),
            new Vk_preferences().getPref(Vk_preferences.TOKEN));
    List<GetResponse> massiveGetResponses = new ArrayList<GetResponse>();
    List<ConstructorPost> listPost = new ArrayList<ConstructorPost>();

    public PostGrabber(List<Integer> providerList, ListView postListView) {
        this.providerList = providerList;
        this.postListView = postListView;
    }
//529989036

    /*цикл в час в этом цикле каждые 10мин перебираю*/
    @Override
    public void run() {

        while (true) {
            try {
                for (int i = 0; i < providerList.size(); i++) {

                    // addtoListPost(
                    //         addtoWallsList(vk_api.getwalls(userActor, providerList.get(i),10,1))
                    // );
                    filterDataInPost(vk_api.getwalls(userActor, providerList.get(i), 10, 1));
                }
                sleep(50000);

            } catch (InterruptedException ex) {
                Logger.getLogger(PostGrabber.class.getName()).log(Level.SEVERE, null, ex);
            }

        }

    }

    public void filterDataInPost(GetResponse getwalls) {

        //System.out.println(getwalls.getItems().toString());
        /// System.out.println("getwalls.getCount() " + getwalls.getCount());
        //System.out.println("getwalls.getItems().size() " + getwalls.getItems().size());
        ConstructorPost post = null;
        Integer provId = null;
        Integer postId = null;
        Integer postdate = null;
        Integer postViews;
        Integer postLikes;
        String text = null;
        Integer count_itemsAttach = null;
        List<String> listPhoto = null;

        for (int i = 0; i < getwalls.getItems().size(); i++) {
            List<WallpostAttachment> attachments = getwalls.getItems().get(i).getAttachments();
            // если данные удовлетворяют могу добавить в listPost
            if (attachments != null) {

                provId = getwalls.getItems().get(i).getOwnerId();
                postId = getwalls.getItems().get(i).getId();
                postdate = getwalls.getItems().get(i).getDate();
//           postViews = getwalls.getItems().get(0).getViews().getCount();
//           postLikes = getwalls.getItems().get(0).getLikes().getCount();
                text = getwalls.getItems().get(i).getText();
                count_itemsAttach = getwalls.getItems().get(i).getAttachments().size();
                listPhoto = new ArrayList<String>();

                for (int j = 0; j < count_itemsAttach; j++) {
                    Photo isPhoto = getwalls.getItems().get(i).getAttachments().get(j).getPhoto();
                    if (isPhoto != null) {

                        listPhoto.add(
                                getwalls.getItems().get(i).getAttachments().get(j).getPhoto().getPhoto807());
                    }

                }
                //проверяю входит ди запись в массив записей 
                addtoListPost(new ConstructorPost(provId, postId, postdate, 1, 1, text, count_itemsAttach, listPhoto));

            }

        }

        // viewInListView(listPost);
    }

    void addtoListPost(ConstructorPost addtoWallsList) {

        System.out.println("addtoWallsList " + addtoWallsList.postId);

        if (addtoWallsList != null) {
            Boolean isExist = false;
            if (listPost.isEmpty()) {
                listPost.add(addtoWallsList);
                viewInListView(listPost);
            } else {
                for (int i = 0; i < listPost.size(); i++) {
                    if (listPost.get(i).postdate.equals(addtoWallsList.postdate)
                            && listPost.get(i).postId.equals(addtoWallsList.postId)
                            && listPost.get(i).text.equals(addtoWallsList.text)
                            && listPost.get(i).provId.equals(addtoWallsList.provId)) {
                        isExist = true;
                    }

                }
                if (isExist == false) {
                    listPost.add(addtoWallsList);
                    viewInListView(listPost);

                    //System.out.println("addToListPost " + addtoWallsList.postId + " from " + addtoWallsList.provId);
                } else {
                    // System.out.println("row " + addtoWallsList.postId + " from " + addtoWallsList.provId + "noUnique");
                }

            }
            //   viewInListView(listPost);

            // System.out.println("listPost " + listPost.size());
        }

    }

    void viewInListView(List<ConstructorPost> listPost) {
        List<String> aaa = new ArrayList<String>();
        for (int i = 0; i < listPost.size(); i++) {
            aaa.add(listPost.get(i).provId + "   " + listPost.get(i).postdate + "\n"
                    + listPost.get(i).postId + "   " + listPost.get(i).provId + "\n "
                    + listPost.get(i).text + " \n "
                    + listPost.get(i).listPhoto.toString());

            /*System.err.println(listPost.get(i).provId + "   " + listPost.get(i).postdate + "\n"
                    + listPost.get(i).postId + "   " + listPost.get(i).provId + "\n "
                    + listPost.get(i).text + " \n "
                    + listPost.get(i).listPhoto.toString());
             */
        }

        final ObservableList observableList = FXCollections.observableArrayList();
        observableList.setAll(aaa);
        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                postListView.setItems(observableList);
            }
        });

    }

    //_______________________  
    ConstructorPost addtoWallsList(GetResponse getwalls) {
        filterDataInPost(getwalls);

        //тут падаем если нет какого-то поля нужно обходить 
        Integer provId = getwalls.getItems().get(0).getOwnerId();
        Integer postId = getwalls.getItems().get(0).getId();
        Integer postdate = getwalls.getItems().get(0).getDate();
//        Integer postViews = getwalls.getItems().get(0).getViews().getCount();
//        Integer postLikes = getwalls.getItems().get(0).getLikes().getCount();

        String text = getwalls.getItems().get(0).getText();
        Integer count_photo = getwalls.getItems().get(0).getAttachments().size();
        List<String> listPhoto = new ArrayList<String>();

        for (int j = 0; j < count_photo; j++) {
            listPhoto.add(
                    getwalls.getItems().get(0).getAttachments().get(j).getPhoto().getPhoto807());
        }

        return new ConstructorPost(provId, postId, postdate, 1, 1, text, count_photo, listPhoto);

    }

}
