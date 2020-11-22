/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.vk.api.sdk.objects.wall.responses.GetResponse;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import org.apache.commons.io.FileUtils;
import org.slf4j.LoggerFactory;

/**
 *
 * @author adolf
 * https://oauth.vk.com/blank.html#access_token=14ec12b80e6b981fe2a6c5fb589b754935425fcb32cca37f2c87e13d23f6fc7f1318aad719a4486fc4c65&expires_in=86400&user_id=418739533
 */
public class Helper {

    static final long ONE_MINUTE_IN_MILLIS = 60;
    org.slf4j.Logger logger = LoggerFactory.getLogger(Poster.class);

    public List<String> parseUrl(String url) {
        List<String> list_blank = new ArrayList<>();

        if (url.contains("access_token")) {
            url = url.substring(url.indexOf("#") + 1);
            String[] token = url.split("&");
            for (int i = 0; i < token.length; i++) {
                list_blank.add(token[i].substring(token[i].indexOf("=") + 1));
            }

            System.out.println(list_blank.toString());
        }

        return list_blank;
    }

    public String convertTime(Long time) {
        Date date = new Date();
        date.setTime((long) time * 1000);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String times = simpleDateFormat.format(date);

        // System.out.println("long "+time+" ");
        return times;
    }

    public Long convertStrTimeToLong(String time) {

        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date = simpleDateFormat.parse(time);

            Long ldate = date.getTime() / 1000;
            if (ldate < unixTime()) {
                ldate = unixTime();
            }

            return ldate;

        } catch (ParseException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            return unixTime();
        }

    }

    public long unixTime() {
        //Instant.now().getEpochSecond()
        ZoneId zoneId = ZoneId.of("Europe/Moscow");
        ZonedDateTime zdt = ZonedDateTime.ofInstant(Instant.now(), zoneId);
        return zdt.toEpochSecond();
    }

    public void parseunixTime(Long time) {

        System.err.println("time " + convertTime(time));

    }

    public long timeadd(Long time, int minute) {

        return time + Integer.toUnsignedLong(minute) * 60;
    }

    public File saveFile(String link) {
        
     
        String cutParams=link.split(".jpg")[0];
        String[] parseUrl =cutParams.split("/");
        String path = "C:" + File.separator + "vkTest" + File.separator + parseUrl[parseUrl.length-1]+".jpg";
        /*найти .jpg*/
       // String pathX = "C:" + File.separator + "vkTest" + File.separator + parseUrl[parseUrl.length-1];

        File file = new File(path);
        file.getParentFile().mkdirs();
        logger.info("save file# "+path );

        try {
            file.createNewFile();
            FileUtils.copyURLToFile(new URL(link), file);
            logger.info("copyURLToFile");
        } catch (IOException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            logger.info("errrFile");
        }
          logger.info("return");
        return file;

    }

    public void addWallsList(GetResponse getwalls) {
    }

    public int stringToInt(String value) {
        // exseption defaultValue
        return Integer.valueOf(value);
    }

    public void alertInfo(final String message) {

        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING");
                alert.setContentText(message);

                alert.showAndWait();
            }

        });

    }
    
    
    
    public String wall(String postLink){
     //https://vk.com/g.fagradyan?w=wall288914612_12495
     String[] wallStrings = null;
    if(postLink.startsWith("https://vk.com/")& postLink.contains("wall")){
            
            postLink=postLink.replace("https://vk.com/", "");
            wallStrings=postLink.split("wall");
            
            for(String elt: wallStrings){
           //  System.err.println(elt);        
            }
           return  wallStrings[wallStrings.length-1];  
        }else{
            System.err.println("noPostLink ");
            return  null;
        }
 
    
    }

    public String wall(String string, ListView listLinkPost) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    

}
