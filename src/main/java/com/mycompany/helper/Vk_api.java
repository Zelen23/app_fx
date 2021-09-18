/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import static com.mycompany.helper.Vk_preferences.CLIENT_ID;

import com.vk.api.sdk.client.TransportClient;
import com.vk.api.sdk.client.VkApiClient;
import com.vk.api.sdk.client.actors.UserActor;
import com.vk.api.sdk.exceptions.ApiException;
import com.vk.api.sdk.exceptions.ClientException;
import com.vk.api.sdk.httpclient.HttpTransportClient;
import com.vk.api.sdk.objects.base.responses.OkResponse;
import com.vk.api.sdk.objects.enums.WallFilter;
import com.vk.api.sdk.objects.photos.Photo;
import com.vk.api.sdk.objects.photos.PhotoUpload;
import com.vk.api.sdk.objects.photos.responses.CreateAlbumResponse;
import com.vk.api.sdk.objects.photos.responses.GetWallUploadServerResponse;
import com.vk.api.sdk.objects.photos.responses.SaveWallPhotoResponse;
import com.vk.api.sdk.objects.photos.responses.WallUploadResponse;
import com.vk.api.sdk.objects.users.Fields;
import com.vk.api.sdk.objects.users.UserXtrCounters;

import com.vk.api.sdk.objects.wall.WallpostFull;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.objects.wall.responses.PostResponse;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adolf
 *
 * мапить коды ошибок на мессаджи через swith
 *
 */
public class Vk_api {

    Vk_preferences pref = new Vk_preferences();

    String url = "https://oauth.vk.com/authorize?client_id=" + pref.getPref(CLIENT_ID)
            + "&display=page&redirect_uri=https://oauth.vk.com/blank.html"
            + "&scope=friends&response_type=token&v=5.131";
    String urlWall = "https://oauth.vk.com/authorize?client_id=" + pref.getPref(CLIENT_ID)
            + "&display=page&redirect_uri=https://oauth.vk.com/blank.html"
            + "&scope=wall&response_type=token&v=5.131";

    org.slf4j.Logger logger = LoggerFactory.getLogger(Vk_api.class);

    public UserActor getActor() {

        UserActor actor = new UserActor(
                Integer.parseInt(pref.getPref(Vk_preferences.VK_USER_ID)),
                pref.getPref(Vk_preferences.TOKEN));

        return actor;
    }


    public GetResponse getwalls(Integer provider_id, int count, int offset) {
        GetResponse walls = new GetResponse();

        try {
            try {
                Thread.sleep(600);

            } catch (InterruptedException ex) {
                Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            }
            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            walls = vk.wall().get(getActor())
                    .count(count)
                    .offset(offset)
                    .filter(WallFilter.OWNER)
                    .ownerId(provider_id)
                    .execute();

        } catch (ApiException ex) {
            java.util.logging.Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);

            final String alertInfo = ex.getMessage() + "\nprov: " + provider_id;

            new Helper().alertInfo(alertInfo);

        } catch (ClientException ex) {
            java.util.logging.Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);

            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        }

        return walls;
    }

    public List<WallpostFull> getwallbyId(List<String> posts) {
        List< WallpostFull> walls = new ArrayList<WallpostFull>();
        try {

            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            walls = vk.wall()
                    .getByIdExtended(getActor(), posts)
                    .execute().getItems();

        } catch (ApiException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        } catch (ClientException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        }

        return walls;
    }


    public List<String> addPhoto(List<String> listPhoto, String caption) {

        //получаю лист со ссылками фоток
        // воозвращаю лист
        List<String> ouList = new ArrayList<String>();
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        for (String elt : listPhoto) {
            try {

                File file = new Helper().saveFile(elt);

                GetWallUploadServerResponse serverResponse = vk.photos()
                        .getWallUploadServer(getActor()).execute();
                WallUploadResponse uploadResponse = vk.upload()
                        .photoWall(serverResponse.getUploadUrl().toString(), file).execute();

                List<SaveWallPhotoResponse> photoList = vk.photos().saveWallPhoto(getActor(), uploadResponse.getPhoto())
                        .caption(caption)
                        .server(uploadResponse.getServer())
                        .hash(uploadResponse.getHash())
                        .execute();

                SaveWallPhotoResponse photo = photoList.get(0);

                ouList.add("photo" + photo.getOwnerId() + "_" + photo.getId());
                Thread.sleep(1500);
            } catch (ApiException ex) {
                Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
                final String alertInfo = ex.getMessage();
                new Helper().alertInfo(alertInfo);
            } catch (ClientException ex) {
                Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
                final String alertInfo = ex.getMessage();
                new Helper().alertInfo(alertInfo);
            } catch (InterruptedException ex) {
                Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
                final String alertInfo = ex.getMessage();
                new Helper().alertInfo(alertInfo);
            }

        }
        logger.info("Succefull upload photo " + ouList);
        return ouList;
    }


    public List<com.vk.api.sdk.objects.users.responses.GetResponse> getUserInfo(String vk_id) {
        try {
            Thread.sleep(100);
        } catch (InterruptedException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
        }

        List<com.vk.api.sdk.objects.users.responses.GetResponse> userinfo = null;
        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            userinfo = vk.users().get(getActor())
                    .userIds(vk_id)
                    .fields(Fields.ABOUT)
                    .execute();

        } catch (ApiException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        } catch (ClientException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        }

        return userinfo;
    }




    public List<ConstructorProvider> fromUsertoProvider(List<com.vk.api.sdk.objects.users.responses.GetResponse> userinfo) {
        List<ConstructorProvider> vk_Providers = new ArrayList<>();

        for ( com.vk.api.sdk.objects.users.responses.GetResponse elt : userinfo) {
            vk_Providers.add(new ConstructorProvider(elt.getFirstName() + " " + elt.getLastName(),
                    "plase",
                    elt.getId(),
                    Boolean.FALSE,
                    "provider"));
            //         System.err.println(elt.getId());
            //        System.err.println(elt.getFirstName() + " " + elt.getLastName());
        }

        return vk_Providers;
    }

    public Integer setPost(String mess, Long pubdate, List<String> attach, Integer owner_id) {
        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            PostResponse postId = vk.wall().post(getActor())
                    .ownerId(owner_id)
                    .publishDate(pubdate.intValue())
                    .message(mess)
                    .attachments(attach)
                    .execute();

            logger.info("Send post to my wall: OK " + postId.getPostId());

            return postId.getPostId();
        } catch (ApiException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        } catch (ClientException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        }
        return null;
    }

    public String wallDelete(Integer ownId, Integer postId) {
        try {
            Thread.sleep(600);
        } catch (InterruptedException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
        }
        TransportClient transportClient = HttpTransportClient.getInstance();
        VkApiClient vk = new VkApiClient(transportClient);

        try {

            OkResponse deleteWpos = vk.wall().delete(getActor())
                    .ownerId(ownId)
                    .postId(postId)
                    .execute();
            System.out.println("wallDelete " + postId + "resp " + deleteWpos.getValue());

            return deleteWpos.getValue();

        } catch (ApiException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);

            return null;
        } catch (ClientException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);

            return null;
        }

    }

    public CreateAlbumResponse crtAlbum(Integer group_id, String title) {

        CreateAlbumResponse alb=new CreateAlbumResponse();

        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            alb = vk.photos()
                    .createAlbum(getActor(), title)
                    .description(title)
                    .privacyView("nobody")
                    .execute();
        } catch (ApiException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        } catch (ClientException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        }

        return alb;
    }

    public String movePhoto(Integer owner_id, Integer target_album_id, Integer photo_id) {
        OkResponse movePh = null;
        try {

            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            movePh = vk.photos()
                    .move(getActor(), target_album_id, photo_id)
                    .ownerId(owner_id)
                    .execute();
        } catch (ApiException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("movePhoto " + photo_id + " resp " + movePh.getValue());
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        } catch (ClientException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            final String alertInfo = ex.getMessage();
            new Helper().alertInfo(alertInfo);
        }

        return movePh.getValue();
    }

    //__________________________________
    public void getuser(UserActor actor) {

        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);

            com.vk.api.sdk.objects.friends.responses.GetResponse user = vk.friends().get(actor)
                    .execute();

            System.out.println(user.getItems());

        } catch (ApiException ex) {
            java.util.logging.Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("Token");
        } catch (ClientException ex) {
            java.util.logging.Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("ping");
        }
    }

/*
    public void openVK(Integer APP_ID, String REDIRECT_URI, String CLIENT_SECRET, String code) {
        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);

            UserAuthResponse authResponse = vk.oauth()
                    .userAuthorizationCodeFlow(APP_ID,
                            CLIENT_SECRET,
                            REDIRECT_URI,
                            code)
                    .execute();

            UserActor actor = new UserActor(authResponse.getUserId(), authResponse.getAccessToken());

            List<UserXtrCounters> users = vk.users().get(actor)
                    .userIds("418739533")
                    .execute();

            System.out.print(users.get(0).getLastName());
        } catch (ApiException ex) {
            java.util.logging.Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClientException ex) {
            java.util.logging.Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
*/

    boolean checkAvalable(Integer provider_id) {
        String avalable = getUserInfo(String.valueOf(provider_id)).get(0).getDeactivated();
        if (avalable == null) {
            return true;
        } else {
            //тут метод который  уберет галку с поставщика
            logger.info("provider_id: "+provider_id+"is "+avalable);
            new DbHandler().updflag_post(false, provider_id);
            return false;
        }

    }
}
