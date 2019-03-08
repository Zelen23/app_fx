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
import com.vk.api.sdk.objects.UserAuthResponse;
import com.vk.api.sdk.objects.ads.Client;
import com.vk.api.sdk.objects.users.UserXtrCounters;
import com.vk.api.sdk.objects.wall.responses.GetResponse;
import com.vk.api.sdk.queries.wall.WallGetFilter;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author adolf
 */
public class Vk_api {

    Vk_preferences pref = new Vk_preferences();

    String url = "https://oauth.vk.com/authorize?client_id=" + pref.getPref(CLIENT_ID)
            + "&display=page&redirect_uri=https://oauth.vk.com/blank.html"
            + "&scope=friends&response_type=token&v=5.52";
    String urlWall = "https://oauth.vk.com/authorize?client_id=" + pref.getPref(CLIENT_ID)
            + "&display=page&redirect_uri=https://oauth.vk.com/blank.html"
            + "&scope=wall&response_type=token&v=5.52";
    
    public UserActor getActor(Integer user_id, String token) {
        UserActor actor = new UserActor(user_id, token);

        return actor;
    }

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

    public GetResponse getwalls(UserActor actor, Integer provider_id,int count,int offset) {
        GetResponse walls = new GetResponse();
        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            walls = vk.wall().get(actor)
                    .count(count)
                    .offset(offset)
                    .filter(WallGetFilter.OWNER)
                    .ownerId(provider_id)
                    .execute();

            //  System.out.println(walls.getItems().get(0).getText()+" "
            //          +walls.getItems().get(0).getAttachments().get(0).getPhoto().getPhoto1280()
            //  );
        } catch (ApiException ex) {
            java.util.logging.Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("Token_getwalls_2");

        } catch (ClientException ex) {
            java.util.logging.Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
            System.err.println("ping_getwalls");
        }

        return walls;
    }
    
    public void setPost(UserActor actor, String mess,Long pubdate){
        try {
            TransportClient transportClient = HttpTransportClient.getInstance();
            VkApiClient vk = new VkApiClient(transportClient);
            vk.wall().post(actor)
                    .ownerId(418739533)
                    .publishDate(pubdate.intValue())
                    .message(mess)
                    .execute();
            
            System.out.println("date_in_setPost "+ pubdate.intValue());
        } catch (ApiException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClientException ex) {
            Logger.getLogger(Vk_api.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
    }

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

}
