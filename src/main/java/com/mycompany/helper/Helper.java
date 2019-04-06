/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.vk.api.sdk.objects.wall.responses.GetResponse;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FileUtils;

/**
 *
 * @author adolf
 *  https://oauth.vk.com/blank.html#access_token=14ec12b80e6b981fe2a6c5fb589b754935425fcb32cca37f2c87e13d23f6fc7f1318aad719a4486fc4c65&expires_in=86400&user_id=418739533
 */
public class Helper {
    static final long ONE_MINUTE_IN_MILLIS=60;
    
    public List<String> parseUrl(String url){
        List<String> list_blank= new ArrayList<>();
        
        if(url.contains("access_token")){
            url=url.substring(url.indexOf("#")+1);
            String [] token=url.split("&");
            for(int i=0;i<token.length;i++){
                list_blank.add(token[i].substring(token[i].indexOf("=")+1));  
            }
            
            System.out.println(list_blank.toString());
        }

         return list_blank;
    }
    
    public String convertTime(Long time){
       Date date = new Date();
       date.setTime((long)time*1000);
       
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        String times=    simpleDateFormat.format(date);
        
       // System.out.println("long "+time+" ");
    return times ;
    }
    
    public Long convertStrTimeToLong(String time){
     
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date date=simpleDateFormat.parse(time);
 
            Long ldate=date.getTime()/1000;
            if(ldate<unixTime()){
               ldate=unixTime();
            }
            
            return ldate;
            
        } catch (ParseException ex) {
            Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            return unixTime();
        }
    
    }
    
    public long unixTime(){  
    //Instant.now().getEpochSecond()
    ZoneId zoneId = ZoneId.of( "Europe/Moscow" );
    ZonedDateTime zdt = ZonedDateTime.ofInstant( Instant.now() , zoneId );
    return zdt.toEpochSecond();
    }
    
    public void parseunixTime(Long time){
        
        System.err.println("time "+convertTime(time));
        
        
    }
    
    
    public long  timeadd(Long time, int minute){
     
     return time+Integer.toUnsignedLong(minute)*60;
    }
    
   
    public File saveFile(String link){

        String[]parseUrl=link.split("/");
        String path = "C:" + File.separator + "vkTest" + File.separator + parseUrl[6];
        File file=new File(path);
            file.getParentFile().mkdirs(); 
            
            
            try {
               file.createNewFile();
               FileUtils.copyURLToFile(new URL(link),file);
            } catch (IOException ex) {
                Logger.getLogger(Helper.class.getName()).log(Level.SEVERE, null, ex);
            }
            return file;
        
    }   
    
    public void addWallsList(GetResponse getwalls){
    }
    
    public int stringToInt(String value){
        // exseption defaultValue
    return Integer.valueOf(value);
    }
    
   
    
}
