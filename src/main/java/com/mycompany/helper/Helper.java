/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import com.vk.api.sdk.objects.wall.responses.GetResponse;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author adolf
 *  https://oauth.vk.com/blank.html#access_token=14ec12b80e6b981fe2a6c5fb589b754935425fcb32cca37f2c87e13d23f6fc7f1318aad719a4486fc4c65&expires_in=86400&user_id=418739533
 */
public class Helper {
    
    
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
    
    public void addWallsList(GetResponse getwalls){
    }
    
}
