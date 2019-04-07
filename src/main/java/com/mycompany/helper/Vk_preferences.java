/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import java.util.prefs.Preferences;

/**
 *
 * @author adolf
 */


public class Vk_preferences {
    
public static final String CLIENT_ID="client_id";
public static final String SERVICES_KEY="services_key";
public static final String SECRET_KEY="secret_key";
public static final String TOKEN="token";
public static final String VK_USER_ID="vk_user_id";
public static final String GROUPS_PROVIDER="groups_provider";

void pref(){
    Preferences preferences=Preferences.userNodeForPackage(Vk_preferences.class);
    
}

public void putPref(String key, String value){
    Preferences preferences=Preferences.userNodeForPackage(Vk_preferences.class);
    preferences.put(key, value);
}


public String getPref(String key){
    Preferences preferences=Preferences.userNodeForPackage(Vk_preferences.class);
    return preferences.get(key, "null");
  
    
}
}
