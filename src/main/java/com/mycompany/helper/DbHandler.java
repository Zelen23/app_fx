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
"  [create_at] DATETIME, \n" +
"  [plase] VARCHAR);\n" +
"\n" +
"\n" +
"CREATE TABLE [settings] (\n" +
"  [key] CHAR, \n" +
"  [value] CHAR, \n" +
"  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT);"
;


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

public List<ConstructorProvider> providerDB(){
    List<ConstructorProvider> prov=new ArrayList<ConstructorProvider>();
    String selectString="Select name,provider,flag_post from main";

    try {
        
        Connection conn=this.DBconnect();
        Statement statement=conn.createStatement();
        ResultSet resultSet=statement.executeQuery(selectString);
        
        while (resultSet.next()) {
        String name=resultSet.getString("name");
        String plase=resultSet.getString("plase");    
        Integer id=resultSet.getInt("provider");
        Boolean flag=resultSet.getBoolean("flag_post");;
            
           prov.add(new ConstructorProvider(name, plase, id, flag));             
        }  
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
    return prov;
}

public static void CreateDB(){
    try {
        conn=DriverManager.getConnection(url);
        statmt = conn.createStatement();
        statmt.execute(table);
        System.out.println("Соединения закрыты");
    } catch (SQLException ex) {
        Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
    }
}       
}
