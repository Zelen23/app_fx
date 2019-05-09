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
 * всегда передавать vk_user_id insUser сохранять время получения токена и время
 * жизни новая таблица для хранения client_ID переименовать названия таблиц
 *
 * добавить таблицу с данными и временем произведенного поста
 *
 */
public class DbHandler {

    public static Connection conn;
    public static Statement statmt;
    public static ResultSet resultSet;

    public static String url = "jdbc:sqlite:vk_grabBase.db";

    public static String providers
            = "CREATE TABLE [providers] (\n"
            + "  [id] INTEGER, \n"
            + "  [provider] INTEGER NOT NULL PRIMARY KEY ON CONFLICT REPLACE, \n"
            + "  [name] VARCHAR, \n"
            + "  [flag_post] BOOLEAN NOT NULL ON CONFLICT REPLACE DEFAULT true, \n"
            + "  [user_id] VARCHAR, \n"
            + "  [create_at] INTEGER, \n"
            + "  [plase] VARCHAR, \n"
            + "  [Groups] nvARCHAR);";

    public static String groups
            = "CREATE TABLE [groups] (\n"
            + "  [id] INTEGER NOT NULL PRIMARY KEY ON CONFLICT IGNORE AUTOINCREMENT, \n"
            + "  [GroupName] VARCHAR);";

    public static String user
            = "CREATE TABLE [user] (\n"
            + "  [token] CHAR, \n"
            + "  [vk_id] INTEGER NOT NULL, \n"
            + "  [create_at] INTEGER, \n"
            + "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n"
            + "  [TimeInterval] INT NOT NULL ON CONFLICT REPLACE DEFAULT 30, \n"
            + "  [NumbersOfPosts] INT NOT NULL ON CONFLICT REPLACE DEFAULT 10, \n"
            + "  [name] VARCHAR);";

    public static String gpoup_provider
            = "CREATE TABLE [gpoup_provider] (\n"
            + "  [key1] INTEGER CONSTRAINT [group] REFERENCES [providers]([provider]) ON DELETE CASCADE ON UPDATE NO ACTION, \n"
            + "  [value1] INTEGER CONSTRAINT [groupVal] REFERENCES [Groups]([id]) ON DELETE CASCADE ON UPDATE NO ACTION DEFAULT 99, \n"
            + "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT);";

    public static String postInfo
            = "CREATE TABLE [postInfo] (\n"
            + "  [id] INTEGER NOT NULL ON CONFLICT REPLACE PRIMARY KEY AUTOINCREMENT, \n"
            + "  [provider_id] INT, \n"
            + "  [post_id] INT, \n"
            + "  [postMyWallId] INT, \n"
            + "  [user_id] INT, \n"
            + "  [created_at] INT);\n";

    public static String uniquIndex
            = "CREATE UNIQUE INDEX [group] ON [gpoup_provider] ([key1], [value1]);";

    public static String tr_addProviders
            = "CREATE TRIGGER [addProviders]\n"
            + "AFTER INSERT\n"
            + "ON [providers]\n"
            + "BEGIN\n"
            + "insert into gpoup_provider (key1)\n"
            + "values (new.provider);\n"
            + "END;";
    //убрал 
    public static String tr_chekInput
            = "CREATE TRIGGER [chekInput]\n"
            + "BEFORE INSERT\n"
            + "ON [gpoup_provider]\n"
            + "FOR EACH ROW\n"
            + "WHEN EXISTS (Select * From \n"
            + "gpoup_provider where \n"
            + "key1=new.key1)\n"
            + "BEGIN\n"
            + "delete from gpoup_provider\n"
            + "where\n"
            + "new.key1 = key1 \n"
            + "and new.value1<>value1;\n"
            + "END;";

    public static String tr_insertVk_id = "CREATE TRIGGER [insertVk_id]\n"
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

    public static String tr_limit100 = "CREATE TRIGGER [limit]\n"
            + "AFTER INSERT\n"
            + "ON [postInfo]\n"
            + "FOR EACH ROW\n"
            + "WHEN exists (select * from  postInfo where  \n"
            + "(select count(*) from postInfo where user_id=new.user_id)>=30)\n"
            + "BEGIN\n"
            + "\n"
            + "delete from postInfo\n"
            + "where user_id=new.user_id\n"
            + "and id=(select min(id) from postInfo where user_id=new.user_id);\n"
            + "\n"
            + "END;";

    public static String defaultGroupString
            = "insert into groups \n"
            + "(id,GroupName)\n"
            + "values\n"
            + "(99,'default');";

    public static void CreateDB() {
       

        try {
            conn = DriverManager.getConnection(url);
            statmt = conn.createStatement();
       
            statmt.execute(user);
            statmt.execute(providers);
            statmt.execute(groups);
         
            statmt.execute(gpoup_provider);
            statmt.execute(postInfo);

            statmt.execute(tr_addProviders);
            statmt.execute(tr_insertVk_id);
            statmt.execute(tr_limit100);
            

            statmt.execute(uniquIndex);
            statmt.execute(defaultGroupString);
        
            
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

    public void insertInProvider(Integer provID, Integer user_id, String name) {
        try {
            String inserString = "Insert into providers (provider,user_id,name)values (?,?,?)";
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
        String deleteString = "Delete from  providers where provider = ?";
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
        String deleteString = "Update providers set flag_post= ? where provider= ?";
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

    //вывод с учетом груп
    public List<ConstructorProvider> providerDB(Integer user_id) {
        List<ConstructorProvider> prov = new ArrayList<ConstructorProvider>();
        String selectString = "Select name,provider,flag_post,plase from providers where user_id = '" + user_id + "';";

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
                Integer create_at = resultSet.getInt("create_at");
                Integer TimeInterval = resultSet.getInt("TimeInterval");

                prov.add(new ConstructorProvider(name, new Helper().convertTime(create_at.longValue()), vk_id, false, "user_vk"));
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
            if(resultSet!=null){
                  while (resultSet.next()) {
                int value = resultSet.getInt(key);

                settings = value;

            }
            }

      
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return settings;
    }

    public void insUser(Integer user_id, String token, String name, String time) {
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

    public List<ConstructorProvider> providerDBX(Integer user_id, String id_group) {
        List<ConstructorProvider> prov = new ArrayList<ConstructorProvider>();
        Integer grind = 99;
        String selectString
                = " select \n"
                + "providers.provider,providers.name,providers.user_id,providers.plase,providers.flag_post, \n"
                + "groups.[GroupName],groups.[id],\n"
                + "gpoup_provider.[value1]       \n"
                + "from providers\n"
                + "inner join gpoup_provider\n"
                + "on providers.[provider]=gpoup_provider.[key1]\n"
                + "\n"
                + "inner join groups\n"
                + "on groups.[id]=gpoup_provider.[value1]\n"
                + "where gpoup_provider.[value1] in (" + id_group + ")  \n"
                + "and providers.[user_id]=" + user_id + " \n"
                + "group by providers.provider";

        System.err.println("providerDBX " + selectString);

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
        //System.err.println("providerDBX_resultprov "+prov.get(0).name);
        return prov;
    }

    public List<GroupsProvider> groupList() {
        List<GroupsProvider> group = new ArrayList<GroupsProvider>();
        String selectString = "Select id,GroupName from groups";

        try {

            Connection conn = this.DBconnect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                Integer id = resultSet.getInt("id");
                String GroupName = resultSet.getString("GroupName");
                group.add(new GroupsProvider(id, GroupName));
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return group;
    }

    // уникальность поддерживается эксепшоном
    public void addProviderToGroup(Integer Provider_id, Integer id_group) {

        String insQuerry
                = "insert into gpoup_provider \n"
                + "(key1,value1)\n"
                + "values\n"
                + "(?,?)";

        System.out.println("addProviderToGroup " + insQuerry);
        try {
            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(insQuerry);
            preparedStatement.setInt(1, Provider_id);
            preparedStatement.setInt(2, id_group);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void deleteProviderToGroup(Integer Provider_id) {

        String deleteString = "Delete from gpoup_provider where key1 = ?;";

        System.err.println("deleteProviderToGroup " + deleteString);

        try {
            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(deleteString);
            preparedStatement.setInt(1, Provider_id);
            preparedStatement.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public List<String> getGroupProvider(Integer Provider_id) {

        List<String> groupsList = new ArrayList<String>();
        String selectString
                = "Select gpoup_provider.key1,gpoup_provider.value1,\n"
                + "groups.[GroupName]\n"
                + "\n"
                + "from gpoup_provider\n"
                + "inner join groups\n"
                + "on \n"
                + "gpoup_provider.value1=groups.[id]\n"
                + "where key1=" + Provider_id + ";";

        System.err.println("getGroupProvider " + selectString);

        try {

            Connection conn = this.DBconnect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                String groupName = resultSet.getString("GroupName");
                Integer provider = resultSet.getInt("key1");

                groupsList.add(groupName);
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }
        return groupsList;
    }

    public void addGroup(String groupName) {

        try {
            String inserString = "Insert into groups ('GroupName')values (?)";
            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(inserString);
            preparedStatement.setString(1, groupName);
            preparedStatement.executeUpdate();

            System.err.println(inserString);

        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void postingInfo(Integer provider_id, Integer post_id,Integer postMyWallId, Integer user_id) {

        try {
            String inserString = "Insert into postInfo (provider_id,post_id,user_id,postMyWallId,created_at)values (?,?,?,?,?)";

            Connection conn = this.DBconnect();
            PreparedStatement preparedStatement = conn.prepareStatement(inserString);
            preparedStatement.setInt(1, provider_id);
            preparedStatement.setInt(2, post_id);
            preparedStatement.setInt(3, user_id);
            preparedStatement.setInt(4, postMyWallId);
            preparedStatement.setLong(5, new Helper().unixTime());
            preparedStatement.executeUpdate();

            System.err.println(inserString);

        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    public List<Integer> postedList(Integer provider_id,Integer user_id){
    
     List<Integer> posted = new ArrayList<Integer>();
        String selectString = "Select post_id from postInfo where"
                + " provider_id= "+provider_id
                + " and user_id= "+user_id;
        
    //    System.err.println(selectString);

        try {

            Connection conn = this.DBconnect();
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(selectString);

            while (resultSet.next()) {
                Integer id = resultSet.getInt("post_id");
               
                posted.add(id );
            }
        } catch (SQLException ex) {
            Logger.getLogger(DbHandler.class.getName()).log(Level.SEVERE, null, ex);
        }

        return posted;
    }

}
