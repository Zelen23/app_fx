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
            + "  [create_at] DATETIME, \n"
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
            + "  [create_at] TIMESTAMP, \n"
            + "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, \n"
            + "  [TimeInterval] INT NOT NULL ON CONFLICT REPLACE DEFAULT 30, \n"
            + "  [NumbersOfPosts] INT NOT NULL ON CONFLICT REPLACE DEFAULT 10, \n"
            + "  [name] VARCHAR);;";

    public static String gpoup_provider
            = "CREATE TABLE [gpoup_provider] (\n"
            + "  [key1] INTEGER CONSTRAINT [group] REFERENCES [providers]([provider]) ON DELETE CASCADE ON UPDATE NO ACTION, \n"
            + "  [value1] INTEGER CONSTRAINT [groupVal] REFERENCES [Groups]([id]) ON DELETE CASCADE ON UPDATE NO ACTION DEFAULT 99, \n"
            + "  [id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT);";

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

    public static String defaultGroupString
            = "insert into groups \n"
            + "(id,GroupName)\n"
            + "values\n"
            + "(99,'default');";

    public static void CreateDB() {
        try {
            conn = DriverManager.getConnection(url);
            statmt = conn.createStatement();

            statmt.execute(providers);
            statmt.execute(groups);
            statmt.execute(user);
            statmt.execute(gpoup_provider);

            statmt.execute(tr_addProviders);
            statmt.execute(tr_insertVk_id);

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
    
    public List<ConstructorProvider> providerDBX(Integer user_id,String id_group) {
        List<ConstructorProvider> prov = new ArrayList<ConstructorProvider>();
        Integer grind=99;
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
                + "where gpoup_provider.[value1] in ("+id_group+")  \n"
                + "and providers.[user_id]="+user_id+" \n"
                + "group by providers.provider";
        
        System.err.println("providerDBX "+selectString);

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
    
    public List<GroupsProvider> groupList(){
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

    // читать список групп
    //добавить группу
    //удалить группу-удалить все связи в settings
    //добавить группы провайдеру(группы)
    /*    
    insert into settings 
        (key1,value1)
    values
        (111,4),
        (111,3)
     */
    //удалить группу у провайдера
    //вывести список поставшиков по id группы и пользователю
    /*  select 
            main.id,main.provider,main.name,main.[user_id],
            Groups.[GroupName],Groups.[id],
            settings.[value1]       
        from main
        inner join settings
        on main.[provider]=settings.[key1]

        inner join Groups
        on Groups.[id]=settings.[value1]
        where settings.[value1] in (4,2)  
        -- and main.[user_id]=419021587
        -- group by main.provider
     */
}
