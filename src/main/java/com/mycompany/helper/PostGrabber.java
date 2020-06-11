/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.mycompany.app_fx.FXMLController;
import com.mycompany.app_fx.ListViewCell;
import com.vk.api.sdk.objects.base.LikesInfo;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.wall.Views;
import com.vk.api.sdk.objects.wall.WallPostFull;
import com.vk.api.sdk.objects.wall.WallpostAttachment;
import com.vk.api.sdk.objects.wall.WallpostAttachmentType;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressBar;
import javafx.util.Callback;
import org.joda.time.DateTime;
import org.joda.time.JodaTimePermission;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adolf UH6sYetv5n Прикрутить возможность фильтрации Прикрутить логи
 *
 * 3rd step: addtoListPostnull_null 2019-04-23 23:12:51 INFO PostGrabber:131 -
 * 1st_step:filterDataInPost 288914612_12679 2019-04-23 23:12:51 INFO
 * PostGrabber:308 - 2nd step:
 * gettingPhotohttps://sun1-28.userapi.com/c849328/v849328373/17b992/lq3gY-tYwG8.jpg
 * 288914612_456276193 Exception in thread "Thread-5"
 * java.lang.NullPointerException
 *
 * Exception in thread "Thread-5" java.lang.NullPointerException at
 * com.mycompany.helper.PostGrabber.addtoListPost(PostGrabber.java:230) at
 * com.mycompany.helper.PostGrabber.filterX(PostGrabber.java:160) at
 * com.mycompany.helper.PostGrabber.wallItemX(PostGrabber.java:108) at
 * com.mycompany.helper.PostGrabber.run(PostGrabber.java:80) апр 23, 2019
 * 11:15:24 PM javafx.fxml.FXMLLoader$ValueElement processValue
 */
public class PostGrabber extends Thread {

    private List<Integer> providerList = new ArrayList<Integer>();
    ListView postListView = new ListView();
    ProgressBar progressBar = new ProgressBar();
    Label status = new Label();
    int day;

    Vk_api vk_api = new Vk_api();
    List<GetResponse> massiveGetResponses = new ArrayList<GetResponse>();
    public List<ConstructorPost> listPost = new ArrayList<ConstructorPost>();

    public PostGrabber(List<Integer> providerList, ListView postListView, ProgressBar progressBar, Label status,int day) {
        this.providerList = providerList;
        this.postListView = postListView;
        this.progressBar = progressBar;
        this.status = status;
        this.day= day;

    }
    //529989036
    Vk_preferences pref = new Vk_preferences();
    Integer user_id = Integer.valueOf(pref.getPref(Vk_preferences.VK_USER_ID));
    Integer NumbersOfPosts = new DbHandler().settingsList("NumbersOfPosts",
            user_id);

    org.slf4j.Logger logger = LoggerFactory.getLogger(PostGrabber.class);

    /*цикл в час в этом цикле каждые 10мин перебираю*/
    @Override
    public void run() {
        //postListView=new  ListView();

        // while (true) {
        Task task = new Task() {
            @Override
            protected Object call() throws Exception {
                for (int i = 0; i < providerList.size(); i++) {
                    GetResponse getwalls = vk_api.getwalls(providerList.get(i), NumbersOfPosts, 0);
                    wallItemX(getwalls);

                    sleep(100);
                    updateProgress(i + 1, providerList.size());
                    updateMessage(providerList.get(i).toString());
                }

                return null;
            }
        };
        progessStatus(task);
    }

    public void wallItemX(GetResponse getwalls) {
        List<WallPostFull> list = new ArrayList<WallPostFull>();
        for (int i = 0; i < getwalls.getItems().size(); i++) {

            list.add(getwalls.getItems().get(i));
        }
        filterX(list);
    }

    public void filterX(List<WallPostFull> list) {

        ConstructorPost post = null;
        Integer provId = null;
        Integer postId = null;
        Long postdate = null;
        Integer postViews = 0;
        Integer postLikes = 0;
        String text = "";
        Integer count_itemsAttach = null;

        for (WallPostFull wallItem : list) {
            List<WallpostAttachment> attachments = wallItem.getAttachments();
            Integer isPinned = wallItem.getIsPinned();

            //если данные удовлетворяют могу добавить в listPost
            if (isPinned == null && attachments != null) {

                logger.info("1st_step:filterDataInPost " + wallItem.getOwnerId() + "_" + wallItem.getId());
                //вложения в одном посте            
                count_itemsAttach = wallItem.getAttachments().size();
                List<String> listPhoto = new ArrayList<String>();

                // посчитал все вложения в одном посте
                for (int j = 0; j < count_itemsAttach; j++) {

                    WallpostAttachmentType attachmentType
                            = wallItem.getAttachments().get(j).getType();

                    if (attachmentType == WallpostAttachmentType.PHOTO) {

                    }
                    Photo isPhoto = wallItem.getAttachments().get(j).getPhoto();
                    Views views = wallItem.getViews();
                    LikesInfo likesInfo = wallItem.getLikes();
                    //если вложение это фотка то забираем этот пост                

                    if (isPhoto != null) {

                        if (views != null) {
                            postViews = views.getCount();
                        };
                        if (likesInfo != null) {
                            postLikes = likesInfo.getCount();
                        };
                        provId = wallItem.getOwnerId();
                        postId = wallItem.getId();
                        postdate = wallItem.getDate().longValue();
                        text = wallItem.getText();

                        listPhoto.add(gettingPhoto(isPhoto));
                    }
                }
                //проверяю входит ди запись в массив записей 
                //херачит в несколько проходов

                if (listPhoto.size() > 0) {
                    // addtoListPost(new ConstructorPost(provId, postId, postdate, postViews, postLikes, text, count_itemsAttach, listPhoto, false));
                    fromFilterToListView(new ConstructorPost(provId, postId, postdate, postViews, postLikes, text, count_itemsAttach, listPhoto, false));
                }

            }
        }

    }

    void fromFilterToListView(ConstructorPost addtoWallsList) {
        // не показывать размещенные
        Boolean flagLogs = pref.getBooleanPref(Vk_preferences.SHOW_POSTED);
      
        // не показывать старше чем ...
        // не показывать если лайков меньше
        List<Integer> postIdList
                = new DbHandler().postedList(addtoWallsList.provId, user_id);

        if (flagLogs && postIdList.contains(addtoWallsList.postId)) {
            logger.info("this post is myWall: " + addtoWallsList.provId + "_" + addtoWallsList.postId);
        } else {
                if(addtoWallsList.postdate>=new Helper().unixTime()-(new DateTime().getSecondOfDay()*day)){
                 listPost.add(addtoWallsList);
                viewInListView(listPost);
                logger.info("3rd step: addtoListPost" + addtoWallsList.provId + "_" + addtoWallsList.postId);

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

    public void progessStatus(final Task task) {

        Platform.runLater(new Runnable() {

            @Override
            public void run() {
                progressBar.progressProperty().bind(task.progressProperty());
                status.textProperty().bind(task.messageProperty());
                new Thread(task).start();

            }
        });

    }

//-----
    // тут можно проводить фильтрацию
    void addtoListPost(ConstructorPost addtoWallsList) {

        List<Integer> postIdList
                = new DbHandler().postedList(addtoWallsList.provId, user_id);

        if (addtoWallsList != null) {
            Boolean isExist = false;
            if (listPost.isEmpty()) {
                listPost.add(addtoWallsList);
                viewInListView(listPost);
            } else {
//прохожу по всему листу и сравниваю каждую новую запись с тем что есть в  ListPost
// по=этому выходит много прогонов
                for (int i = 0; i < listPost.size(); i++) {

                    if (listPost.get(i).postdate.equals(addtoWallsList.postdate)
                            && listPost.get(i).postId.equals(addtoWallsList.postId)
                            && listPost.get(i).provId.equals(addtoWallsList.provId)) {
                        isExist = true;
                    }
                    if (postIdList.contains(listPost.get(i).postId)) {
                        //listPost.get(i).text="ПОСТИЛ \n"+listPost.get(i).text;
                        System.out.println("----" + addtoWallsList.provId + "_" + addtoWallsList.postId);
                        //isExist = true;

                    };

                }

                if (isExist == false) {

                    logger.info("3rd step: addtoListPost" + addtoWallsList.provId + "_" + addtoWallsList.postId);
                    listPost.add(addtoWallsList);
                    viewInListView(listPost);

                } else {
                    System.out.println("no add 3rd step: addtoListPost" + addtoWallsList.provId + "_" + addtoWallsList.postId);
                }

            }

        }

    }

}
