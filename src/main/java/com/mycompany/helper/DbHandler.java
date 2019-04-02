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
 * 
 * всегда передавать vk_user_id
 * insUser сохранять время получения токена и время жизни
 * новая таблица для хранения client_ID
 * переименовать названия таблиц
 * 
 */
public class DbHandler {

    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resultSet;

    public static String url = "jdbc:sqlite:vk_grabBase.db";

    public static String table
            = "CREATE TABLE [main] (\n"
            + "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n"
            + "  [provider] INTEGER NOT NULL, \n"
            + "  [name] VARCHAR, \n"
            + "  [flag_post] BOOLEAN NOT NULL ON CONFLICT REPLACE DEFAULT true, \n"
            + "  [user_id] INTEGER, \n"
            + "  [create_at] DATETIME, \n"
            + "  [plase] VARCHAR);\n"
            + "CREATE TABLE [settings]";
    public static String table2
            = "CREATE TABLE [settings] (\n"
            + "  [key1] CHAR, \n"
            + "  [value1] CHAR, \n"
            + "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT);";

    public static String table3
            = "CREATE TABLE [user] (\n"
            + "  [token] CHAR, \n"
            + "  [vk_id] INTEGER NOT NULL, \n"
            + "  [name] VARCHAR), \n"
            + "  [create_at] DATETIME, \n"
            + "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n"
            + "  [TimeInterval] INT NOT NULL ON CONFLICT REPLACE DEFAULT 30, \n"
            + "  [NumbersOfPosts] INT NOT NULL ON CONFLICT REPLACE DEFAULT 10);";

    public static String trig
            = "CREATE TRIGGER [chekInput]\n"
            + "BEFORE INSERT\n"
            + "ON [settings]\n"
            + "FOR EACH ROW\n"
            + "WHEN EXISTS (Select * From \n"
            + "settings where \n"
            + "key1=new.key1)\n"
            + "BEGIN\n"
            + "delete from settings\n"
            + "where\n"
            + "new.key1 = key1 \n"
            + "and new.value1<>value1;\n"
            + "END;";

    public static String trig2 = "CREATE TRIGGER [insertVk_id]\n"
            + "BEFORE INSERT\n"
            + "ON [user]\n"
            + "FOR EACH ROW\n"
            + "WHEN EXISTS (Select * From user where vk_id=new.vk_id)\n"
            + "BEGIN\n"
            + "delete from user\n"
            + "where\n"
            + "new.vk_id = vk_id \n"
            + "and new.token<>token;\n"
            + "END;";

    public static void CreateDB() {
        try {
            conn = DriverManager.getConnection(url);
            statmt = conn.createStatement();
            statmt.execute(table);
            statmt.execute(table2);
            statmt.execute(table3);
            statmt.execute(trig);
            statmt.execute(trig2);
            System.out.println("Соединения закрыты");
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Connection DBconnect() {
        try {
            conn = null;
            conn = DriverManager.getConnection(url);
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println("База Подключена!");
        }
        return conn;
    }

    public static void CloseDB() throws ClassNotFoundException, SQLException {
        conn.close();
        statmt.close();
        resultSet.close();
        System.out.println("Соединения закрыты");
    }

    public void insertInProvider(Integer provID, Integer user_id,String name) {
        try {
            String inserString = "Insert into main (provider,user_id,name)values (?,?,?)";
            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(inserString);
            preparedStatement.setInt(1, provID);
            preparedStatement.setInt(2, user_id);
            preparedStatement.setString(3, name);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteProvider(Integer provID) {
        String deleteString = "Delete from  main where provider = ?";
        try {
            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(deleteString);
            preparedStatement.setInt(1, provID);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void updflag_post(Boolean flag_post, Integer provID) {
        String deleteString = "Update main set flag_post= ? where provider= ?";
        try {
            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(deleteString);
            preparedStatement.setBoolean(1, flag_post);
            preparedStatement.setInt(2, provID);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<ConstructorProvider> providerDB(Integer user_id) {
        List<ConstructorProvider> prov = new ArrayList<ConstructorProvider>();
        String selectString = "Select name,provider,flag_post,plase from main where user_id = '" + user_id + "';";

        try {

            Connection conn = this.DBconnect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                String name = resultSet.getString("name");
                String plase = resultSet.getString("plase");
                Integer id = resultSet.getInt("provider");
                Boolean flag = resultSet.getBoolean("flag_post");

                prov.add(new ConstructorProvider(name, plase, id, flag, "provider"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prov;
    }

    public List<ConstructorProvider> userDB() {
        List<ConstructorProvider> prov = new ArrayList<ConstructorProvider>();
        String selectString = "Select vk_id,name,create_at,TimeInterval from user";

        try {

            Connection conn = this.DBconnect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                Integer vk_id = resultSet.getInt("vk_id");
                String name = resultSet.getString("name");
                String create_at = resultSet.getString("create_at");
                Integer TimeInterval = resultSet.getInt("TimeInterval");

                prov.add(new ConstructorProvider(name, create_at, vk_id, false, "user_vk"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return prov;
    }

    public void insertSettings(String key, String value, int vk_id) {
        try {
            String inserString = "update user set " + key + " = ? Where vk_id = ?";
            System.out.println("---- " + inserString);
            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(inserString);
            preparedStatement.setString(1, value);
            preparedStatement.setInt(2, vk_id);
            preparedStatement.executeUpdate();

        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public Integer settingsList(String key, int vk_id) {
        Integer settings = null;
        String selectString = "Select " + key + " from user where vk_id =" + vk_id;
        System.out.println("---- " + selectString);
        try {

            Connection conn = this.DBconnect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                int value = resultSet.getInt(key);

                settings = value;

            }
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return settings;
    }

    public void insUser(Integer user_id, String token,String name, String time) {
        try {
            String inserString = "Insert into user (vk_id,token,name,create_at)values (?,?,?,?)";
            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(inserString);
            preparedStatement.setInt(1, user_id);
            preparedStatement.setString(2, token);
            preparedStatement.setString(3, name);
            preparedStatement.setString(4, time);
            preparedStatement.executeUpdate();

            System.err.println(inserString);

        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public String getToken(int vk_id) {
        String querry = "Select token from user where vk_id = '" + vk_id + "';";
        String token = "";
        try {
            Connection conn = this.DBconnect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(querry);

            while (resultSet.next()) {
                token = resultSet.getString("token");

            }
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.err.println(querry);
        return token;
    }

}
