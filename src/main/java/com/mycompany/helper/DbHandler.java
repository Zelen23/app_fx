/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author adolf
 */
public class DbHandler {
    
public static Connection conn;  
public static Statement statmt;
public static ResultSet resultSet;

public static String url="jdbc:sqlite:vk_grabBase.db";

public static String table=
"CREATE TABLE [main] (\n" +
"  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n" +
"  [provider] INTEGER NOT NULL, \n" +
"  [name] VARCHAR, \n" +
"  [flag_post] BOOLEAN NOT NULL ON CONFLICT REPLACE DEFAULT true, \n" +
"  [user_id] VARCHAR, \n" +
"  [create_at] DATETIME, \n" +
"  [plase] VARCHAR);\n" +
"CREATE TABLE [settings]"
;
public static String table2=
"CREATE TABLE [settings] (\n" +
"  [key1] CHAR, \n" +
"  [value1] CHAR, \n" +
"  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT);"
;

public static String table3=
"CREATE TABLE [user] (\n" +
"  [token] CHAR, \n" +
"  [vk_id] INTEGER NOT NULL, \n" +
"  [create_at] DATETIME, \n" +      
"  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT);"
;

public static String trig=
"CREATE TRIGGER [chekInput]\n" +
"BEFORE INSERT\n" +
"ON [settings]\n" +
"FOR EACH ROW\n" +
"WHEN EXISTS (Select * From \n" +
"settings where \n" +
"key1=new.key1)\n" +
"BEGIN\n" +
"delete from settings\n" +
"where\n" +
"new.key1 = key1 \n" +
"and new.value1<>value1;\n" +
"END;";

public static void CreateDB(){
    try {
        conn=DriverManager.getConnection(url);
        statmt = conn.createStatement();
        statmt.execute(table);
        statmt.execute(table2);
        statmt.execute(table3);
        statmt.execute(trig);
        System.out.println("Соединения закрыты");
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public static Connection DBconnect(){
    try {
        conn=null;
        conn=DriverManager.getConnection(url);
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        System.out.println("База Подключена!");
    }
  return conn;
}

public static void CloseDB() throws ClassNotFoundException, SQLException{
    conn.close();
    statmt.close();
    resultSet.close();		
    System.out.println("Соединения закрыты");
}

public void insertInProvider(Integer provID){
    try {
        String inserString="Insert into main (provider)values (?)";
        Connection conn=this.DBconnect();
        PreparedStatement preparedStatement=conn.prepareStatement(inserString);
        preparedStatement.setInt(1, provID);
        preparedStatement.executeUpdate(); 
        
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public void insertSettings(String key,String value){
    try {
        String inserString="Insert into settings (key1,value1)values (?,?)";
        Connection conn=this.DBconnect();
        PreparedStatement preparedStatement=conn.prepareStatement(inserString);
        preparedStatement.setString(1, key);
        preparedStatement.setString(2, value);
        preparedStatement.executeUpdate(); 
        
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
}

public void deleteProvider(Integer provID){
     String deleteString="Delete from  main where provider = ?";
    try {
        Connection conn=this.DBconnect();
        PreparedStatement preparedStatement=conn.prepareStatement(deleteString);
        preparedStatement.setInt(1, provID); 
        preparedStatement.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }

}


public void updflag_post(Boolean flag_post, Integer provID){
     String deleteString="Update main set flag_post= ? where provider= ?";
    try {
        Connection conn=this.DBconnect();
        PreparedStatement preparedStatement=conn.prepareStatement(deleteString);
        preparedStatement.setBoolean(1, flag_post); 
        preparedStatement.setInt(2, provID); 
        preparedStatement.executeUpdate();
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }

}

public List<ConstructorProvider> providerDB(){
    List<ConstructorProvider> prov=new ArrayList<ConstructorProvider>();
    String selectString="Select name,provider,flag_post,plase from main";

    try {
        
        Connection conn=this.DBconnect();
        Statement statement=conn.createStatement();
        ResultSet resultSet=statement.executeQuery(selectString);
        
        while (resultSet.next()) {
        String name=resultSet.getString("name");
        String plase=resultSet.getString("plase");    
        Integer id=resultSet.getInt("provider");
        Boolean flag=resultSet.getBoolean("flag_post");
            
           prov.add(new ConstructorProvider(name, plase, id, flag));             
        }  
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return prov;
}

public String settingsList(String key){
    String settings = null;
    String selectString="Select value1 from settings where key1= '"+key+"'";
  
    try {
        
        Connection conn=this.DBconnect();
        Statement statement=conn.createStatement();
        ResultSet resultSet=statement.executeQuery(selectString);
        
        while (resultSet.next()) {
        String value=resultSet.getString("value1");    
        
           settings=value;  
       
        }  
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return settings;
}

  
}
